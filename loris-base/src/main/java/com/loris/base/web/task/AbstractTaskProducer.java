package com.loris.base.web.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.loris.base.web.task.event.TaskEvent;
import com.loris.base.web.task.event.TaskProducerEventListener;

public abstract class AbstractTaskProducer<T extends Task> implements TaskProducer<T>
{
	/** The Producer's name. */
	protected String name;
	
	/** 最近一次完成的时间 */
	protected Date lastFinishedTimeStamp;
	
	/** 最近一次创建时间 */
	protected Date lastTimeStamp;
	
	/** 是否已经初始化 */
	protected boolean initialized = false;
	
	/** 仅进行一次的任务 */
	protected boolean oneTimeFlag = false;
	
	/** 任务队列 */
	protected TaskQueue<T> taskQueue;
	
	/** 等待时间 */
	protected long waitTime = 1000;
	
	/** 任务监听器 */
	protected List<TaskProducerEventListener<T>> listeners = new ArrayList<>();
	
	@Override
	public abstract void run();
	
	@Override
	public abstract boolean needToStart();

	@Override
	public abstract void notify(TaskEvent event);

	/**
	 * 添加任务生产器的监听器
	 * @param listener 监听器
	 */
	@Override
	public void addTaskProducerEventListener(TaskProducerEventListener<T> listener)
	{
		if(!listeners.contains(listener))
		{
			listeners.add(listener);
		}
	}

	@Override
	public void removeTaskProducerEventListener(TaskProducerEventListener<T> listener)
	{
		listeners.remove(listener);
	}

	@Override
	public void setTaskQueue(TaskQueue<T> queue)
	{
		this.taskQueue = queue;
	}

	@Override
	public boolean isOneTimeTaskProducer()
	{
		return oneTimeFlag;
	}

	@Override
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public Date getLastTimeStamp()
	{
		return lastTimeStamp;
	}

	@Override
	public void updateLastTimeStamp(Date timeStamp)
	{
		this.lastTimeStamp = timeStamp;
	}

	@Override
	public void updateLastFinishedTimeStamp(Date timeStamp)
	{
		this.lastFinishedTimeStamp = timeStamp;
	}

	@Override
	public Date getLastFinishedTimeStamp()
	{
		return lastFinishedTimeStamp;
	}

	public long getWaitTime()
	{
		return waitTime;
	}

	public void setWaitTime(long waitTime)
	{
		this.waitTime = waitTime;
	}

	public boolean isInitialized()
	{
		return initialized;
	}

	public void setInitialized(boolean initialized)
	{
		this.initialized = initialized;
	}
	
	@Override
	public void reset()
	{
		this.initialized = false;
		this.lastTimeStamp = null;
		this.lastFinishedTimeStamp = null;
	}
}
