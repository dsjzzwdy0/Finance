package com.loris.base.web.task.event;

public interface TaskProducerEventListener
{
	/**
	 * 消息通知管理器
	 * @param event 消息信息
	 */
	void notify(TaskProcuderEvent event);
}
