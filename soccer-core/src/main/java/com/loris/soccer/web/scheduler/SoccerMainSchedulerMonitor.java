package com.loris.soccer.web.scheduler;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.loris.base.context.LorisContext;
import com.loris.base.web.scheduler.MainSchedulerMonitor;
import com.loris.base.web.scheduler.TaskSchedulerMonitor;
import com.loris.base.web.task.PriorityTaskQueue;
import com.loris.base.web.task.TaskQueue;
import com.loris.soccer.repository.SoccerContext;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwDataDownloader;
import com.loris.soccer.web.task.MatchWebTask;
import com.loris.soccer.web.task.SoccerMatchTaskProducer;

/**
 * 足球数据下载总调度类
 * 
 * @author deng
 *
 */
public class SoccerMainSchedulerMonitor
{
	private static Logger logger = Logger.getLogger(SoccerMainSchedulerMonitor.class);
	
	/** The Context */
	private static SoccerContext appContext;
	
	/**
	 * 开始主进程,程序的主入口
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			startMainThread(args);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
			logger.info("Error: " + exception.toString());
		}
	}
	
	/**
	 * Get the LorisContext.
	 * @return LorisContext
	 */
	public static SoccerContext getLorisContext()
	{
		if(appContext != null)
		{
			return appContext;
		}
		ClassPathXmlApplicationContext context;
		/** The Application Context. */
		context = new ClassPathXmlApplicationContext("classpath:soccerApplicationContext.xml");
		appContext = new SoccerContext(context);
		return appContext;
	}
	
	/**
	 * 主程序
	 * @param args
	 */
	public static void startMainThread(String[] args)
	{
		logger.info("Start SoccerMainScheduler Downloader Application.");
		MainSchedulerMonitor scheduler = new MainSchedulerMonitor();
		if(!initSchedulerMonitor(scheduler))
		{
			return;
		}
		scheduler.setStop(false);
		
		TaskQueue<MatchWebTask> queue = new PriorityTaskQueue<>();
		
		//产生器
		SoccerMatchTaskProducer producer = new SoccerMatchTaskProducer(queue);
		scheduler.addRunnerable(producer);
		
		TaskSchedulerMonitor<MatchWebTask> monitor = new TaskSchedulerMonitor<>(queue);
		scheduler.addRunnerable(monitor);
		
		Thread mainThread = new Thread(scheduler);
		mainThread.start();
	}
	
	/**
	 * 主管理程序的初始化
	 * @param monitor
	 */
	protected static boolean initSchedulerMonitor(MainSchedulerMonitor monitor)
	{
		LorisContext context = getLorisContext();		
		if(context == null)
		{
			logger.info("The Application context is null, exit.");
			return false;
		}
		
		//初始化下载管理器
		ZgzcwDataDownloader.initialize(context);
		
		return true;
	}
}
