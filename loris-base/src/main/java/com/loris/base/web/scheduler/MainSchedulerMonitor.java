package com.loris.base.web.scheduler;

import java.util.Stack;

import org.apache.log4j.Logger;

/**
 * 
 * 主线程控制器
 * @author deng
 *
 */
public class MainSchedulerMonitor implements Runnable
{
	private static Logger logger = Logger.getLogger(MainSchedulerMonitor.class);
	
	/** The Main Thread stop flag.*/
	private boolean stop = false;
	
	/** 处理线程 */
	protected Stack<Runnable> runnerables = new Stack<>();

	/**
	 * 线程启动
	 */
	@Override
	public void run()
	{
		logger.info("Start MainSchedulerMonitor...");
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
					//thread.setDaemon(false);
					thread.start();
				}
			}
			catch(Exception e)
			{
				logger.info(e.toString());
			}
			if(stop)
			{
				logger.info("Stop MainSchedulerMonitor, exit.");
				break;
			}
			try
			{
				Thread.sleep(2000);
			}
			catch(Exception e)
			{
				logger.info("Error: " + e.toString());
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

	public void setStop(boolean stop)
	{
		this.stop = stop;
	}
}
