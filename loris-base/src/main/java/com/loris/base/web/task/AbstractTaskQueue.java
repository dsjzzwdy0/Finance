package com.loris.base.web.task;

import java.util.List;
import java.util.Queue;

/**
 * 任务管理器的队列数据
 * @author deng
 *
 */
public class AbstractTaskQueue<T> implements TaskQueue<T>
{
	/** 队列数据 */
	protected Queue<T> queue;
	
	/**
	 * 加入任务到队列数据中
	 */
	@Override
	public <K extends T> void pushTask(K task)
	{
		synchronized (queue)
		{
			queue.add(task);
		}
	}

	@Override
	public T popTask()
	{
		return queue.poll();
	}

	@Override
	public int size()
	{
		return queue.size();
	}

	@Override
	public boolean hasMoreTask()
	{
		return !queue.isEmpty();
	}

	@Override
	public void remove(T task)
	{
		synchronized(queue)
		{
			queue.remove(task);
		}
	}

	@Override
	public <K extends T> void pushAllTasks(List<K> tasks)
	{
		queue.addAll(tasks);
	}

}
