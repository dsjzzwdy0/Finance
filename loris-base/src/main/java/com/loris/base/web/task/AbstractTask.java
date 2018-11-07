package com.loris.base.web.task;

import java.util.List;
import com.loris.base.web.task.event.TaskEvent;
import com.loris.base.web.task.event.TaskEventListener;
import com.loris.base.web.task.event.TaskEvent.TaskEventType;

import java.util.ArrayList;

public abstract class AbstractTask implements WebTask
{
	/** 等待时间 (毫秒) */
	protected long waitTime = 1000;
	
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
	
	/**
	 * 开始任务
	 */
	protected void start()
	{
		notify(new TaskEvent(this, TaskEventType.Start));
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
