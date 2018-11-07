package com.loris.base.web.task;

public interface Task extends Runnable
{
	/**
	 * 任务开始之前等候的时间,这项参数很重要
	 * @return 时间值(毫秒)
	 */
	long getWaitTime();
	
	/**
	 * 设置等待的时间
	 * @param waitTime 等待的时间
	 */
	void setWaitTime(long waitTime);
}
