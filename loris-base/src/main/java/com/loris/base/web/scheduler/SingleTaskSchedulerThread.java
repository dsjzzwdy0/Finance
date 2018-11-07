package com.loris.base.web.scheduler;

import org.apache.log4j.Logger;

import com.loris.base.web.manager.Downloader;
import com.loris.base.web.manager.DownloaderStatus;
import com.loris.base.web.task.WebPageTask;
import com.loris.base.web.util.LoaderMonitor;
import com.loris.base.web.util.Monitor;
import com.loris.base.web.util.RandomUtil;

public class SingleTaskSchedulerThread extends Thread
{
	private static Logger logger = Logger.getLogger(SingleTaskSchedulerThread.class);

	/** The time value to sleep. */
	private long milliseconds;

	/** The WebPageManager. */
	private Downloader downloader;

	/** The max random value. */
	private int maxRand = 200;

	/** The Download Task index. */
	private int taskIndex = 1;

	/** The monitor. */
	private LoaderMonitor monitor = null;

	/** The task */
	private WebPageTask task;

	/**
	 * Create a new instance of SingleTaskSchedulerThread.
	 * 
	 * @param downloader
	 *            The Downloader.
	 * @param milliseconds
	 *            The time to stop when two task.
	 */
	public SingleTaskSchedulerThread(Downloader downloader, Class<? extends WebPageTask> taskClass, long milliseconds)
	{
		this.milliseconds = milliseconds;
		this.downloader = downloader;

		try
		{
			task = taskClass.newInstance();
		}
		catch (Exception e)
		{
			logger.info("Error when creating task: " + e.toString());
		}
	}

	/**
	 * Run the Thread.
	 */
	@Override
	public void run()
	{
		if (downloader == null)
		{
			logger.info("The Downloader is null, please set the downloader first, SingleTaskScheduler exit.");
			return;
		}

		if (task == null)
		{
			logger.info("The task class is null, please check the system first, SingleTaskScheduler exit.");
			return;
		}

		startMonitor();

		// 下载管理器是否已经准备就绪，如果没有准备，则需要初始化
		if (!downloader.isPrepared())
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
			catch (Exception e)
			{
				e.printStackTrace();
				logger.warn("Schdeuler exception '" + e.toString() + "' when prepare the manager.");
			}
		}
		
		//开始下载数据
		logger.info("Starting to loading '" + downloader.leftSize() + "' pages. ");
		downloader.setStatus(DownloaderStatus.STATUS_DOWN);

		// 下载当前的数据
		while (!Monitor.stopCrawler && downloader.hasNextWebPage())
		{
			/*
			 * if(monitor.getActiveThreadNum() >=
			 * downloader.getMaxActiveThreadNum()) { logger.
			 * info("The active thread number is up to the maxActiveThread number["
			 * + monitor.getActiveThreadNum() +
			 * "], waiting for a new idle thread."); } else { startNewTask(); }
			 */

			try
			{
				downloadNextPage();
			}
			catch (Throwable exception)
			{
				logger.info("Error occured when process downloading: " + exception.toString());
				exception.printStackTrace();
			}

			try
			{
				SingleTaskSchedulerThread.sleep(this.milliseconds + RandomUtil.getRandom(maxRand));
			}
			catch (InterruptedException ex)
			{
				logger.warn("Schdeuler interrupted");
			}

			// 当前已经停止数据
			if (downloader.isStopped())
			{
				stopMonitor();

				// 变更当前的状态
				downloader.setStatus(DownloaderStatus.STATUS_STOP);
				logger.info("SingleTaskScheduler '" + downloader.getName() + "' has been stopped.");
				return;
			}
		}

		finishDownloader();
	}

	/**
	 * 开始下载，则在主调度进程中增中一个主线程
	 */
	protected void startMonitor()
	{
		monitor = new LoaderMonitor(downloader);
		task.setLoaderMonitor(monitor);
		task.setDownloader(downloader);
		Monitor.addLoaderCrawler(monitor);

		// 增加一个主调度线程
		Monitor.numOfMainSchedulerThread++;
	}

	/**
	 * 停止下载，则在主调度进程中减去一个主线程
	 */
	protected void stopMonitor()
	{
		logger.info("Stop SingleTaskScheduler " + downloader.getName() + ".");
		Monitor.removeLoaderCrawler(monitor);
		Monitor.numOfMainSchedulerThread--;
		monitor = null;
	}

	/**
	 * 下载页面数据
	 */
	protected void downloadNextPage()
	{
		String info = downloader.getName() + " Task#[" + (taskIndex++) + "]=>(total=" + downloader.totalSize()
				+ ", left=" + downloader.leftSize() + ") ";
		task.setName(info);
		logger.info(info + " CREATED " + Monitor.getCurrentTime());

		task.run();
	}

	/**
	 * 结束下载器
	 */
	protected void finishDownloader()
	{
		// 停止下载器监控管理程序
		stopMonitor();

		downloader.setStatus(DownloaderStatus.STATUS_FINISH);
		try
		{
			if (downloader != null)
			{
				downloader.close();
			}
		}
		catch (Exception e)
		{
		}
		finally
		{
			logger.info("SingleTaskScheduler " + downloader.getName() + " has been finished, exit now.");
			downloader = null;
		}
	}
}
