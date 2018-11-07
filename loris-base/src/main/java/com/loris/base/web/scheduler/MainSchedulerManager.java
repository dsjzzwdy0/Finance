package com.loris.base.web.scheduler;

import java.util.Stack;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.loris.base.context.DefaultLorisContext;
import com.loris.base.context.LorisContext;

/**
 * 
 * 主线程控制器
 * @author deng
 *
 */
public class MainSchedulerManager implements Runnable
{
	private static Logger logger = Logger.getLogger(MainSchedulerManager.class);
	
	/** The LorisContext object.*/
	protected static LorisContext appContext = null;
	
	/** The Application main scheduler. */
	protected static MainSchedulerManager scheduler;
	
	/** The Main Thread stop flag.*/
	private boolean stop = false;
	
	/** 处理线程 */
	protected Stack<Runnable> runnerables = new Stack<>();
	
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
	public static MainSchedulerManager getInstance()
	{
		if(scheduler == null)
		{
			scheduler = new MainSchedulerManager();
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
		appContext = new DefaultLorisContext(context);
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
		while(hasMoreRunnerable())
		{
			try
			{
				Runnable runner = popRunnerable();
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
			if(stop)
			{
				break;
			}
			try
			{
				Thread.sleep(1000 * 5);
			}
			catch(Exception e)
			{
				break;
			}
		}
	}
	
	/**
	 * 获取下一个需要开展的线程数据
	 * @return
	 */
	public Runnable popRunnerable()
	{
		return runnerables.pop();
	}
	
	/**
	 * 添加线程处理器
	 * @param runnable
	 */
	public void addRunnerable(Runnable runnable)
	{
		runnerables.push(runnable);
	}
	
	/**
	 * 检测是否有更多的线程管理器
	 * @return
	 */
	public boolean hasMoreRunnerable()
	{
		return !runnerables.empty();
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
