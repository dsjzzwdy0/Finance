package com.loris.base.web.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.loris.base.web.task.event.TaskEvent;
import com.loris.base.web.task.event.TaskProducerEventListener;

public abstract class AbstractTaskProducer implements TaskProducer
{
	/** The Producer's name. */
	protected String name;
	
	/** 最近一次完成的时间 */
	protected Date lastFinishedTimeStamp;
	
	/** 最近一次创建时间 */
	protected Date lastTimeStamp;
	
	/** 仅进行一次的任务 */
	protected boolean oneTimeFlag = false;
	
	/** 任务队列 */
	protected TaskQueue taskQueue;
	
	/** 任务监听器 */
	protected List<TaskProducerEventListener> listeners = new ArrayList<>();
	
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
	public void addTaskProducerEventListener(TaskProducerEventListener listener)
	{
		if(!listeners.contains(listener))
		{
			listeners.add(listener);
		}
	}

	@Override
	public void removeTaskProducerEventListener(TaskProducerEventListener listener)
	{
		listeners.remove(listener);
	}

	@Override
	public void setTaskQueue(TaskQueue queue)
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
}
