package com.loris.base.web.task;

/**
 * 任务管理器
 * @author deng
 *
 */
public interface TaskQueue<T>
{
	/**
	 * 添加任务Task
	 * @param task
	 */
	void pushTask(T task);
	
	/**
	 * 弹出任务
	 * @return
	 */
	T popTask();
	
	/**
	 * 队列的个数
	 * @return
	 */
	int size();
	
	/**
	 * 检测是否有更多的任务
	 * @return
	 */
	boolean hasMoreTask();
}
