package com.loris.base.web.task.event;

import com.loris.base.web.task.Task;

/**
 * Task的消息信息
 * @author deng
 *
 */
public class TaskEvent
{
	public static enum TaskEventType
	{
		Add,
		Start,
		Finished,
		Removed
	}
	
	/** Task */
	protected Task task;
	
	/** 消息类型 */
	protected TaskEventType type;
	
	/**
	 * TaskEvent Instance.
	 * @param task
	 */
	public TaskEvent(Task task, TaskEventType type)
	{
		this.task = task;
	}

	public Task getTask()
	{
		return task;
	}

	public void setTask(Task task)
	{
		this.task = task;
	}

	public TaskEventType getType()
	{
		return type;
	}

	public void setType(TaskEventType type)
	{
		this.type = type;
	}
}
