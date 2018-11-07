package com.loris.base.web.scheduler;

import java.util.List;
import org.apache.log4j.Logger;
import java.util.ArrayList;

import com.loris.base.web.task.Task;
import com.loris.base.web.task.event.TaskEvent;
import com.loris.base.web.task.event.TaskEventListener;
import com.loris.base.web.task.event.TaskEvent.TaskEventType;

/**
 * 任务管理与调度器,这里是一个无限循环的线程,一旦有任务加入,则开始任务工作.
 * @author deng
 *
 */
public class TaskSchedulerMonitor implements Runnable, TaskEventListener
{
	private static Logger logger = Logger.getLogger(TaskSchedulerMonitor.class);

	/** TaskScheduler. */
	protected TaskHolder taskScheduler;

	/** 默认的空闲时间 */
	protected long idleTimeLong = 1000 * 60;
	
	/** 最大运行的任务数 */
	protected int maxRunningTaskNum = 10;

	/** 当前正在运行的任务 */
	protected List<Task> currentRunningTasks = new ArrayList<>();

	/**
	 * Create a new instance of TaskMonitorScheduler.
	 */
	public TaskSchedulerMonitor()
	{
	}

	/**
	 * Create a new instance of TaskMonitorScheduler.
	 * 
	 * @param scheduler
	 */
	public TaskSchedulerMonitor(TaskHolder scheduler)
	{
		this.taskScheduler = scheduler;
	}

	/**
	 * Task monitor scheduler runnable.
	 */
	@Override
	public void run()
	{
		Task currentTask = null;
		while (true)
		{
			if (hasIdleTaskThread() && ((currentTask = taskScheduler.popTask()) != null))
			{
				try
				{
					currentTask.addTaskEventListener(this);
					long waitTime = currentTask.getWaitTime();
					sleep(waitTime);
					
					Thread thread = new Thread(currentTask);
					thread.start();
				}
				catch (Exception e)
				{
					logger.info("Error: " + e.toString());
					
					//如果出现处理错误,则进入待处理任务
					taskScheduler.pushTask(currentTask);
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
	public TaskHolder getTaskScheduler()
	{
		return taskScheduler;
	}

	/**
	 * 任务调度器
	 * @param taskScheduler
	 */
	public void setTaskScheduler(TaskHolder taskScheduler)
	{
		this.taskScheduler = taskScheduler;
	}

	/**
	 * 通知信息
	 * @param event 消息信息
	 */
	@Override
	public void notify(TaskEvent event)
	{
		TaskEvent.TaskEventType type = event.getType();
		Task task = event.getTask();
		if(type == TaskEventType.Add)
		{
			if(task != null)
				currentRunningTasks.add(task);
		}
		else if(type == TaskEventType.Start)
		{
			if(task != null)
				currentRunningTasks.add(task);
		}
		else if(type == TaskEventType.Removed)
		{
			if(task != null)
				currentRunningTasks.remove(task);
		}
		else if(type == TaskEventType.Finished)
		{
			currentRunningTasks.remove(task);
		}
	}
	
	/**
	 * 判断是否有空闲的进程
	 * @return 是否有空闲任务的标志
	 */
	public boolean hasIdleTaskThread()
	{
		return currentRunningTasks.size() < maxRunningTaskNum;
	}
}
