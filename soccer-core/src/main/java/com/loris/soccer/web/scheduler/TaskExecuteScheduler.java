package com.loris.soccer.web.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.web.task.Task;
import com.loris.base.web.task.TaskQueue;
import com.loris.base.web.task.event.TaskEvent;
import com.loris.base.web.task.event.TaskEvent.TaskEventType;
import com.loris.base.web.task.event.TaskEventListener;

/**
 * 任务处理器调度管理程序
 * 
 * @author jiean
 *
 */
public class TaskExecuteScheduler<T extends Task> extends AbstractScheduler implements TaskEventListener
{
	class TaskThread extends Thread
	{
		Task task;
		
		public TaskThread(Task task)
		{
			this.task = task;
		}
		
		public void run()
		{			
			try
			{
				//调用执行任务之前的方法
				task.preExecute();
				
				logger.info("Call Task[" + task.getName() + "] run() method.");
				task.run();
				
				logger.info("Task " + task.getName() + " has finished, post execute...");
				//调用任务执行完成之后的方法
				task.postExecute();
			}
			catch(Exception e)
			{
				task.errExecute();
				logger.info("Error: " + e.toString());
			}
		}
	}
	
	private static Logger logger =Logger.getLogger(TaskExecuteScheduler.class);
	
	/** 最大运行的任务数 */
	protected int maxRunningTaskNum = 10;

	/** 当前正在运行的任务 */
	protected List<Task> runningTasks = new ArrayList<>();
	
	/** 默认的空闲时间 */
	protected long idleTimeLong = 1000 * 10;
	
	/** 任务列表 */
	protected TaskQueue<T> taskQueue = null;
	
	@Override
	public void run()
	{
		T currentTask = null;
		while (true)
		{
			if(!hasIdleTaskThread())
			{
				logger.info("There are " + runningTasks.size() + " running, waiting for new idle task...");
				sleep(idleTimeLong);
			}
			else if (((currentTask = taskQueue.popTask()) != null))
			{
				logger.info("Execute task, and there are left " + taskQueue.size() + " task.");
				long waitTime = computeWaitTime(currentTask.getWaitTime());
				sleep(waitTime);
				executeTask(currentTask);
			}
			else // 没有新的任务时,则进入休眠时间
			{
				//logger.info("Waiting for new task...");
				sleep(idleTimeLong);
			}
		}
	}
	
	/**
	 * 执行任务
	 * @param task
	 */
	protected void executeTask(Task task)
	{
		try
		{
			//添加到当前正在执行的任务
			runningTasks.add(task);
			task.addTaskEventListener(this);
			//执行任务之前的调用方法
			TaskThread thread = new TaskThread(task);
			thread.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断是否有空闲的进程
	 * @return 是否有空闲任务的标志
	 */
	public boolean hasIdleTaskThread()
	{
		return runningTasks.size() < maxRunningTaskNum;
	}
	
	/**
	 * 加入运行的运务列表中
	 * @param task
	 */
	public void addRunningTask(Task task)
	{
		runningTasks.add(task);
	}
	
	/**
	 * 从运行的任务列表中删除任务
	 * @param task
	 */
	public void removeRunningTask(Task task)
	{
		runningTasks.remove(task);
	}

	public long getIdleTimeLong()
	{
		return idleTimeLong;
	}

	public void setIdleTimeLong(long idleTimeLong)
	{
		this.idleTimeLong = idleTimeLong;
	}

	@Override
	public void notify(TaskEvent event)
	{
		//logger.info("Notify event: " + event.getTask() + ", " + event.getType());
		Task task = event.getTask();
		TaskEventType type = event.getType();
		if(type == TaskEventType.Finished)
		{
			runningTasks.remove(task);
		}
	}

	public TaskQueue<? extends T> getTaskQueue()
	{
		return taskQueue;
	}

	public void setTaskQueue(TaskQueue<T> taskQueue)
	{
		this.taskQueue = taskQueue;
	}
	
	/**
	 * 计算等待时间
	 * @param waitTime
	 * @return
	 */
	public long computeWaitTime(long waitTime)
	{
		int rand = random.nextInt((int)(waitTime * 0.4));
		//waitTime = (rand % 2 == 0) ? (waitTime + rand) : (waitTime + rand);
		waitTime += rand;
		return waitTime;
	}
}
