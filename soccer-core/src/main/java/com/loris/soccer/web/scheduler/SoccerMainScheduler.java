package com.loris.soccer.web.scheduler;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.loris.base.context.LorisContext;
import com.loris.soccer.repository.SoccerContext;

/**
 * 
 * 主线程控制器
 * @author deng
 *
 */
public class SoccerMainScheduler implements Runnable
{
	private static Logger logger = Logger.getLogger(SoccerMainScheduler.class);
	
	/** The LorisContext object.*/
	protected static LorisContext appContext = null;
	
	/** The Application main scheduler. */
	protected static SoccerMainScheduler scheduler;
	
	
	private boolean stop = false;
	
	/**
	 * 开始主进程,程序的主入口
	 * @param args
	 */
	public static void main(String[] args)
	{
		scheduler = getInstance();
		scheduler.startMainThread(args);
	}
	
	/**
	 * 获得应用实例
	 * @return 实例
	 */
	public static SoccerMainScheduler getInstance()
	{
		if(scheduler == null)
		{
			scheduler = new SoccerMainScheduler();
		}
		return scheduler;
	}
	
	/**
	 * Get the LorisContext.
	 * @return LorisContext
	 */
	public static LorisContext getLorisContext()
	{
		if(appContext != null)
		{
			return appContext;
		}
		ClassPathXmlApplicationContext context;
		/** The Application Context. */
		context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		appContext = new SoccerContext(context);
		return appContext;
	}
	
	/**
	 * 主程序
	 * @param args
	 */
	public void startMainThread(String[] args)
	{
		logger.info("Start SoccerMainScheduler Downloader Application.");
		
		LorisContext context = getLorisContext();		
		if(context == null)
		{
			logger.info("The Application context is null, exit.");
			return;
		}		
		scheduler.stop = false;
		Thread mainThread = new Thread(scheduler);
		mainThread.start();
	}

	/**
	 * 线程启动
	 */
	@Override
	public void run()
	{
		/**
		 * 线程无限循环
		 */
		while(true)
		{
			if(stop)
			{
				break;
			}
			try
			{
				Thread.sleep(1000 * 60);
			}
			catch(Exception e)
			{
				break;
			}
			
			try
			{
				Runnable runner = getRunnable();
				if(runner != null)
				{					
					Thread thread = new Thread(runner);
					thread.start();
				}
			}
			catch(Exception e)
			{
				logger.info(e.toString());
			}
		}
	}
	
	/**
	 * 获取下一个需要开展的线程数据
	 * @return
	 */
	public Runnable getRunnable()
	{
		return null;
	}
	
	/**
	 * Check the thread is stopped.
	 * @return
	 */
	public boolean isStop()
	{
		return stop;
	}
	
	/**
	 * Stop the Main Thread.
	 */
	public void stop()
	{
		stop = true;
	}
}
