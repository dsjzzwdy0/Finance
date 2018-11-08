package com.loris.base.web.scheduler;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.loris.base.web.task.Task;
import com.loris.base.web.task.TaskQueue;
import com.loris.base.web.task.event.TaskEvent;
import com.loris.base.web.task.event.TaskEventListener;
import com.loris.base.web.task.event.TaskEvent.TaskEventType;

/**
 * 任务管理与调度器,这里是一个无限循环的线程,一旦有任务加入,则开始任务工作.
 * @author deng
 *
 */
public class TaskSchedulerMonitor<T extends Task> implements Runnable, TaskEventListener
{
	private static Logger logger = Logger.getLogger(TaskSchedulerMonitor.class);

	/** TaskScheduler. */
	protected TaskQueue<T> taskQueue;

	/** 默认的空闲时间 */
	protected long idleTimeLong = 1000 * 60;
	
	/** 最大运行的任务数 */
	protected int maxRunningTaskNum = 10;

	/** 当前正在运行的任务 */
	protected List<Task> currentRunningTasks = new ArrayList<>();
	
	protected Random random = new Random();

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
	public TaskSchedulerMonitor(TaskQueue<T> scheduler)
	{
		this.taskQueue = scheduler;
	}

	/**
	 * Task monitor scheduler runnable.
	 */
	@Override
	public void run()
	{
		T currentTask = null;
		while (true)
		{
			if (hasIdleTaskThread() && ((currentTask = taskQueue.popTask()) != null))
			{
				try
				{
					currentTask.addTaskEventListener(this);
					long waitTime = currentTask.getWaitTime();
					
					int rand = random.nextInt((int)(waitTime * 0.2));
					waitTime = (rand % 2 == 0) ? (waitTime + rand) : (waitTime - rand);
					sleep(waitTime);
					
					logger.info("Starting task: " + currentTask);
					Thread thread = new Thread(currentTask);
					thread.start();
				}
				catch (Exception e)
				{
					logger.info("Error: " + e.toString());
					
					//如果出现处理错误,则进入待处理任务
					taskQueue.pushTask(currentTask);
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
	public TaskQueue<T> getTaskQueue()
	{
		return taskQueue;
	}

	/**
	 * 任务调度器
	 * @param taskScheduler
	 */
	public void setTaskQueue(TaskQueue<T> taskScheduler)
	{
		this.taskQueue = taskScheduler;
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
			logger.info("Finished task: " + task + "\r\n");
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
