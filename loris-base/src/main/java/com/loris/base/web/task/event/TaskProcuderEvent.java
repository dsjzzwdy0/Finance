package com.loris.base.web.task.event;

import com.loris.base.web.task.Task;
import com.loris.base.web.task.TaskProducer;

public class TaskProcuderEvent<T extends Task>
{
	public static enum TaskProducerEventType
	{
		Add,
		Remove,
		Finished
	}
	
	protected TaskProducer<T> producer;
	protected TaskProducerEventType type;
	
	public TaskProcuderEvent(TaskProducer<T> producer, TaskProducerEventType type)
	{
		this.producer = producer;
		this.type = type;
	}

	public TaskProducer<T> getProducer()
	{
		return producer;
	}

	public void setProducer(TaskProducer<T> producer)
	{
		this.producer = producer;
	}

	public TaskProducerEventType getType()
	{
		return type;
	}

	public void setType(TaskProducerEventType type)
	{
		this.type = type;
	}
}
