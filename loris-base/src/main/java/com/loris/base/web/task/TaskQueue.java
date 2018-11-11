package com.loris.base.web.task;

import java.util.List;

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
	<K extends T> void pushTask(K task);
	
	/**
	 * 添加所有的任务
	 * @param tasks
	 */
	<K extends T> void pushAllTasks(List<K> tasks);
	
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
	 * 删除任务
	 * @param task
	 */
	void remove(T task);
	
	/**
	 * 检测是否有更多的任务
	 * @return
	 */
	boolean hasMoreTask();
}
