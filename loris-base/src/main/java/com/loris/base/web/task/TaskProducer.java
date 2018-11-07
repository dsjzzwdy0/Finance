package com.loris.base.web.task;

import java.util.Date;

import com.loris.base.web.task.event.TaskEventListener;
import com.loris.base.web.task.event.TaskProducerEventListener;

/**
 * 任务生成器,通过任务生成器,可以产生需要处理的任务.
 * 
 * @author deng
 *
 */
public interface TaskProducer extends Runnable, TaskEventListener
{
	/**
	 * 添加任务生成器监听管理器
	 * @param listener 监听器
	 */
	void addTaskProducerEventListener(TaskProducerEventListener listener);
	
	/**
	 * 删除任务生成器监听管理器
	 * @param listener
	 */
	void removeTaskProducerEventListener(TaskProducerEventListener listener);
	
	/**
	 * 任务队列管理器
	 * @param queue
	 */
	void setTaskQueue(TaskQueue queue);
	
	/**
	 * 是否需要启动管理器
	 * @return 是否需要启动的标志
	 */
	boolean needToStart();
	
	/**
	 * 是否只是一次性的任务
	 * @return 是否一次性任务的标志
	 */
	boolean isOneTimeTaskProducer();
	
	/**
	 * 获得任务生成器的名称
	 * @return 名称
	 */
	String getName();
	
	/**
	 * 获得最近一次完成任务的时间
	 * @return
	 */
	Date getLastTimeStamp();
	
	/**
	 * 更新最近一次完成任务的时间
	 * @param timeStamp
	 */
	void updateLastTimeStamp(Date timeStamp);
	
	/**
	 * 设置最后一次完成的时间
	 * @param timeStamp
	 */
	void updateLastFinishedTimeStamp(Date timeStamp);
	
	/**
	 * 获得最后一次完成的时间
	 * @return
	 */
	Date getLastFinishedTimeStamp();
}
