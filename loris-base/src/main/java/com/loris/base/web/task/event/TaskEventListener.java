package com.loris.base.web.task.event;

/**
 * Task消息监听管理器
 * @author deng
 *
 */
public interface TaskEventListener
{
	/**
	 * 消息通知管理器
	 * @param event 消息信息
	 */
	void notify(TaskEvent event);
}
