package com.loris.base.web.scheduler;

import java.util.List;
import org.apache.log4j.Logger;
import java.util.ArrayList;

import com.loris.base.web.task.Task;

/**
 * 任务管理与调度器,这里是一个无限循环的线程,一旦有任务加入,则开始任务工作.
 * @author deng
 *
 */
public class TaskMonitorScheduler implements Runnable
{
	private static Logger logger = Logger.getLogger(TaskMonitorScheduler.class);

	/** TaskScheduler. */
	protected TaskScheduler taskScheduler;

	/** 默认的空闲时间 */
	protected long idleTimeLong = 1000 * 60;

	/** 当前正在运行的任务 */
	protected List<Thread> currentRunningThreads = new ArrayList<>();

	/**
	 * Create a new instance of TaskMonitorScheduler.
	 */
	public TaskMonitorScheduler()
	{
	}

	/**
	 * Create a new instance of TaskMonitorScheduler.
	 * 
	 * @param scheduler
	 */
	public TaskMonitorScheduler(TaskScheduler scheduler)
	{
		this.taskScheduler = scheduler;
	}

	/**
	 * Task monitor scheduler runnable.
	 */
	@Override
	public void run()
	{
		while (true)
		{
			Task task = taskScheduler.popTask();
			if (task != null)
			{
				long waitTime = task.getWaitTime();
				sleep(waitTime);

				try
				{
					Thread thread = new Thread(task);
					thread.start();
				}
				catch (Exception e)
				{
					logger.info("Error: " + e.toString());
				}
			}
			else // 没有新的任务时,则进入休眠时间
			{
				sleep(idleTimeLong);
			}
		}
	}

	/**
	 * 休眠时间
	 * 
	 * @param millseconds
	 */
	public void sleep(long millseconds)
	{
		try
		{
			Thread.sleep(millseconds);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * 任务调度器
	 * @return
	 */
	public TaskScheduler getTaskScheduler()
	{
		return taskScheduler;
	}

	/**
	 * 任务调度器
	 * @param taskScheduler
	 */
	public void setTaskScheduler(TaskScheduler taskScheduler)
	{
		this.taskScheduler = taskScheduler;
	}
}
