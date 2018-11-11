package com.loris.soccer.web.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.loris.base.context.LorisContext;
import com.loris.base.web.task.PriorityTaskQueue;
import com.loris.base.web.task.TaskQueue;
import com.loris.soccer.repository.SoccerContext;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwDataDownloader;
import com.loris.soccer.web.task.SoccerTask;

/**
 * 足球数据下载总调度类
 * 
 * @author deng
 *
 */
public class SoccerMainSchedulerMonitor implements Runnable
{
	private static Logger logger = Logger.getLogger(SoccerMainSchedulerMonitor.class);
	
	/** The Context */
	private static SoccerContext appContext;
	
	/** Threads. */
	protected List<Scheduler> threads = new ArrayList<>();
	
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
		SoccerMainSchedulerMonitor scheduler = new SoccerMainSchedulerMonitor();
		if(!initSchedulerMonitor(scheduler))
		{
			return;
		}
		TaskQueue<SoccerTask> queue = new PriorityTaskQueue<>();
		
		//任务生成器管理调度
		OddsTaskProduceScheduler producer = new OddsTaskProduceScheduler();
		producer.setTaskQueue(queue);
		scheduler.addScheduler(producer);
		
		//任务执行进程管理器
		TaskExecuteScheduler<SoccerTask> processor = new TaskExecuteScheduler<>();
		processor.setTaskQueue(queue);
		scheduler.addScheduler(processor);
		
		//启动调动器
		scheduler.run();
	}
	
	/**
	 * 主管理程序的初始化
	 * @param monitor
	 */
	protected static boolean initSchedulerMonitor(SoccerMainSchedulerMonitor monitor)
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

	/**
	 * 启动各种线程
	 */
	@Override
	public void run()
	{
		for (Scheduler scheduler : threads)
		{
			Thread thread = new Thread(scheduler);
			thread.start();
		}
	}
	
	/**
	 * 加入调度管理器
	 * @param scheduler
	 */
	public void addScheduler(Scheduler scheduler)
	{
		threads.add(scheduler);
	}
}
