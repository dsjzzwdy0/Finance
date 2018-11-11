package com.loris.base.web.task;

import com.loris.base.web.task.event.TaskEventListener;

/**
 * 多线程任务
 * @author deng
 *
 */
public interface Task extends Runnable
{
	/**
	 * 名称
	 * @return
	 */
	String getName();
	
	/**
	 * 任务开始之前等候的时间,这项参数很重要
	 * @return 时间值(毫秒)
	 */
	long getWaitTime();
	
	/**
	 * 任务的优先度
	 * @return 优先度
	 */
	int getPriority();
	
	/**
	 * 设置等待的时间
	 * @param waitTime 等待的时间
	 */
	void setWaitTime(long waitTime);
	
	/**
	 * 在执行任务之前
	 */
	void preExecute();
	
	/**
	 * 在执行任务之后
	 */
	void postExecute();
	
	/**
	 * 在执行任务过程中发现有问题
	 */
	void errExecute();
	
	/**
	 * 加入消息监听器
	 * @param listener
	 */
	void addTaskEventListener(TaskEventListener listener);
	
	/**
	 * 删除消息监听器
	 * @param listener
	 */
	void removeTaskEventListener(TaskEventListener listener);
}
