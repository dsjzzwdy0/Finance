package com.loris.base.web.task;

import java.util.List;
import com.loris.base.web.task.event.TaskEvent;
import com.loris.base.web.task.event.TaskEventListener;
import com.loris.base.web.task.event.TaskEvent.TaskEventType;

import java.util.ArrayList;

/**
 * 多线程中的任务
 * @author deng
 *
 */
public abstract class AbstractTask implements Task
{
	/** 等待时间 (毫秒) */
	protected long waitTime = 1000;
	
	/** 任务的名称 */
	protected String name;
	
	/** 任务的优先度,默认设置为1 */
	protected int priority = 1;
	
	/** 任务消息管理器 */
	protected List<TaskEventListener> listeners = new ArrayList<>();
	
	public long getWaitTime()
	{
		return waitTime;
	}

	public void setWaitTime(long waitTime)
	{
		this.waitTime = waitTime;
	}
	
	@Override
	public void addTaskEventListener(TaskEventListener listener)
	{
		if(!listeners.contains(listener))
			listeners.add(listener);
	}

	@Override
	public void removeTaskEventListener(TaskEventListener listener)
	{
		listeners.remove(listener);
	}
	
	/**
	 * 通知消息
	 * @param event
	 */
	protected void notify(TaskEvent event)
	{
		for (TaskEventListener taskEventListener : listeners)
		{
			taskEventListener.notify(event);
		}
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	/**
	 * 开始任务
	 */
	protected void start()
	{
		notify(new TaskEvent(this, TaskEventType.Start));
	}
	
	/**
	 * 在执行任务之前
	 */
	@Override
	public void preExecute()
	{
	}
	
	/**
	 * 在执行任务之后
	 */
	@Override
	public void postExecute()
	{
	}
	
	/**
	 * 在执行任务过程中发现有问题
	 */
	@Override
	public void errExecute()
	{
	}
	
	/**
	 * 任务结束
	 */
	protected void finish()
	{
		notify(new TaskEvent(this, TaskEventType.Finished));
	}
	
	@Override
	public abstract void run();
}
