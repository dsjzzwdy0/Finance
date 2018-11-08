package com.loris.base.web.task.event;

import com.loris.base.web.task.Task;

public interface TaskProducerEventListener<T extends Task>
{
	/**
	 * 消息通知管理器
	 * @param event 消息信息
	 */
	void notify(TaskProcuderEvent<T> event);
}
