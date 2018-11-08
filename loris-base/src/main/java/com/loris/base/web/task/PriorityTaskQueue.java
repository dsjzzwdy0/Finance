package com.loris.base.web.task;

import java.util.PriorityQueue;

public class PriorityTaskQueue<T extends Task> extends AbstractTaskQueue<T>
{
	public PriorityTaskQueue()
	{
		queue = new PriorityQueue<T>();
	}
}
