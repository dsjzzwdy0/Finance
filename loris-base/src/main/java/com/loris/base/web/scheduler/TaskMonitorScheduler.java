package com.loris.base.web.scheduler;

import java.util.List;
import java.util.ArrayList;

import com.loris.base.web.task.Task;

public class TaskMonitorScheduler implements Runnable
{
	/** */
	class TaskThread extends Thread
	{
		
	}
	
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
		while(true)
		{
			Task task = taskScheduler.popTask();
			if(task != null)
			{
				long waitTime = 2000;
				sleep(waitTime);
				
				try
				{
					Thread thread = new Thread(task);
					thread.start();
				}
				catch (Exception e) {
				}
			}
			else  //没有新的任务时,则进入休眠时间
			{
				sleep(idleTimeLong);
			}
		}
	}

	/**
	 * 休眠时间
	 * @param millseconds
	 */
	public void sleep(long millseconds)
	{
		try
		{
			Thread.sleep(millseconds);
		}
		catch (Exception e) {
		}
	}
	public TaskScheduler getTaskScheduler()
	{
		return taskScheduler;
	}

	public void setTaskScheduler(TaskScheduler taskScheduler)
	{
		this.taskScheduler = taskScheduler;
	}
}
