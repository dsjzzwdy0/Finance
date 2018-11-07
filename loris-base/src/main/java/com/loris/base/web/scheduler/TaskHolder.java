package com.loris.base.web.scheduler;

import com.loris.base.web.task.Task;

/**
 * 任务管理器
 * @author deng
 *
 */
public interface TaskHolder
{
	/**
	 * 添加任务Task
	 * @param task
	 */
	void pushTask(Task task);
	
	/**
	 * 弹出任务
	 * @return
	 */
	Task popTask();
	
	/**
	 * 检测是否有更多的任务
	 * @return
	 */
	boolean hasMoreTask();
}
