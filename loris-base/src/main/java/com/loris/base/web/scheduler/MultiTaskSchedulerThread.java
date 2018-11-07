/*
 * @Author Irakli Nadareishvili
 * CVS-ID: $Id: MainSchedulerThread.java,v 1.3 2005/02/03 01:43:52 idumali Exp $
 *
 * Copyright (c) 2004 Development Gateway Foundation, Inc. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Common Public License v1.0 which accompanies this
 * distribution, and is available at:
 * http://www.opensource.org/licenses/cpl.php
 *
 *****************************************************************************/

package com.loris.base.web.scheduler;

import org.apache.log4j.Logger;

import com.loris.base.web.manager.DownloaderStatus;
import com.gargoylesoftware.htmlunit.WebClient;
import com.loris.base.web.manager.Downloader;
import com.loris.base.web.task.WebTask;
import com.loris.base.web.util.LoaderMonitor;
import com.loris.base.web.util.Monitor;
import com.loris.base.web.util.RandomUtil;

public class MultiTaskSchedulerThread extends Thread
{
	private static Logger logger = Logger.getLogger(MultiTaskSchedulerThread.class);

	/** The Task Class name. */
	private Class<? extends WebTask> taskClass;
	
	/** The time value to sleep. */
	private long milliseconds;
	
	/** The WebPageManager. */
	private Downloader downloader;
	
	/** The monitor. */
	private LoaderMonitor monitor = null;
	
	/** The thread number. */
	private int threadIndex = 1;
	
	/** The max random value. */
	private int maxRand = 200;

	/**
	 * @todo make sure that task class extends Thread.
	 */
	public MultiTaskSchedulerThread(Downloader manager, Class<? extends WebTask> taskClass, long milliseconds)
	{
		this.downloader = manager;
		this.taskClass = taskClass;
		this.milliseconds = milliseconds;
	}
	
	/**
	 * @todo make sure that task class extends Thread.
	 */
	public MultiTaskSchedulerThread(Downloader manager, Class<? extends WebTask> taskClass, long milliseconds, 
			WebClient client)
	{
		this.downloader = manager;
		this.taskClass = taskClass;
		this.milliseconds = milliseconds;
	}
	
	/**
	 * 创建下载监控器
	 */
	protected void startDownloader()
	{
		monitor = new LoaderMonitor(downloader);
		Monitor.addLoaderCrawler(monitor);
		
		//增加一个主调度线程
		Monitor.numOfMainSchedulerThread ++;
	}
	
	/**
	 * 停止下载器监控
	 */
	protected void stopLoaderMonitor()
	{
		try
		{
			int counter = 1;
			//如果存在下载的线程数，则需要等待
			while(monitor != null && monitor.getActiveThreadNum() > 0)
			{
				if(counter % 10 == 0)
				{
					logger.info("Waiting, there are " + monitor.getActiveThreadNum() + " threads still running.");
				}
				MultiTaskSchedulerThread.sleep(this.milliseconds );
				counter ++;
			}
		}
		catch(Exception e)
		{
			//
		}
		logger.info("Stop MultiTaskScheduler " + downloader.getName() + ".");
		Monitor.removeLoaderCrawler(monitor);		
		Monitor.numOfMainSchedulerThread --;
		monitor = null;
	}
	
	/**
	 * 终止下载监控器,这里在停止当前监控的基础上，需要释放下载管理器的资源
	 */
	protected void finishDownloader()
	{
		//停止下载器监控管理程序
		stopLoaderMonitor();
		
		downloader.setStatus(DownloaderStatus.STATUS_FINISH);		
		try
		{
			if(downloader != null)
			{
				downloader.close();
			}
		}
		catch (Exception e) 
		{
		}
		finally
		{
			logger.info("MultiTaskScheduler " + downloader.getName() + " has been finished, exit now.");
			downloader = null;
		}
	}
	
	/**
	 * 开始新的下载任务
	 */
	protected void startNewTask()
	{
		try
		{
			//Monitor.restartIfNeeded();
			WebTask task = (WebTask) taskClass.newInstance();
			task.setDownloader(downloader);
			task.setLoaderMonitor(monitor);
			
			Thread thread = new Thread(task);
			
			thread.setPriority(Thread.MIN_PRIORITY);				
			//Monitor.incCounter();

			String info = downloader.getName() +" THREAD#[" + (threadIndex ++) + "]=>(total=" + downloader.totalSize() 
				+ ", left=" + downloader.leftSize() + ") " ;
			thread.setName(info);
			task.setName(info);
			logger.info(info + " CREATED " + Monitor.getCurrentTime());
			
			//thread.setDaemon(true);
			thread.setDaemon(false);
			thread.start();

		}
		catch (IllegalAccessException ex2)
		{
			logger.error("Can not create instance of " + taskClass.getClass(), ex2);
		}
		catch (InstantiationException ex2)
		{
			logger.error("Can not create instance of " + taskClass.getClass(), ex2);
		}
	}

	/**
	 * Run the Thread.
	 */
	@Override
	public void run()
	{
		if(downloader == null)
		{
			logger.info("The Downloader is null, please set the downloader first, MultiTaskScheduler exit.");
			return;
		}
		
		//开始监控管理器
		startDownloader();
		
		//下载管理器是否已经准备就绪，如果没有准备，则需要初始化
		if(!downloader.isPrepared())
		{		
			try
			{
				logger.info("Start prepare '" + downloader.getName() + "'... ");				
				
				if(downloader.prepare())
				{
					downloader.afterPrepared();
				}
				else
				{
					logger.info("Downloader " + downloader.getName() + " has not parepared, exit.");
					return;
				}
				
				downloader.setStatus(DownloaderStatus.STATUS_PREPARED);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				logger.warn("MultiTaskScheduler exception '" + e.toString() + "' when prepare the manager.");
			}
		}

		//开始下载数据
		logger.info("Starting to loading '" + downloader.leftSize() + "' pages. ");
		downloader.setStatus(DownloaderStatus.STATUS_DOWN);
		
		//下载当前的数据
		while (!Monitor.stopCrawler && downloader.hasNextWebPage())
		{
			if(monitor.getActiveThreadNum() >= downloader.getMaxActiveThreadNum())
			{
				logger.info("The active thread number is up to the maxActiveThread number[" + 
						monitor.getActiveThreadNum() + "], waiting for a new idle thread.");
			}
			else
			{
				startNewTask();
			}
			
			try
			{
				MultiTaskSchedulerThread.sleep(this.milliseconds + RandomUtil.getRandom(maxRand));
			}
			catch (InterruptedException ex)
			{
				logger.warn("MultiTaskScheduler interrupted");
			}
			
			//当前已经停止数据
			if(downloader.isStopped())
			{
				stopLoaderMonitor();
				
				//变更当前的状态
				downloader.setStatus(DownloaderStatus.STATUS_STOP);
				logger.info("MultiTaskScheduler '" + downloader.getName() + "' has been stopped.");
				return;
			}			
		}
		
		//完成下载线程
		finishDownloader();
	}

}
