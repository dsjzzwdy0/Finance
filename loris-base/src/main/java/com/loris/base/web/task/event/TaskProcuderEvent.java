package com.loris.base.web.task.event;

import com.loris.base.web.task.TaskProducer;

public class TaskProcuderEvent
{
	public static enum TaskProducerEventType
	{
		Add,
		Remove,
		Finished
	}
	
	protected TaskProducer producer;
	protected TaskProducerEventType type;
	
	public TaskProcuderEvent(TaskProducer producer, TaskProducerEventType type)
	{
		this.producer = producer;
		this.type = type;
	}

	public TaskProducer getProducer()
	{
		return producer;
	}

	public void setProducer(TaskProducer producer)
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
