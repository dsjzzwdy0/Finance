package com.loris.base.web.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.web.task.TaskProducer;
import com.loris.base.web.task.event.TaskProcuderEvent;
import com.loris.base.web.task.event.TaskProcuderEvent.TaskProducerEventType;
import com.loris.base.web.task.event.TaskProducerEventListener;

/**
 * 任务生成器的管理器
 * @author deng
 *
 */
public class TaskProducerMonitor implements Runnable, TaskProducerEventListener
{
	private static Logger logger = Logger.getLogger(TaskProducerMonitor.class);
	
	/** TaskProducer. */
	protected List<TaskProducer> producers = new ArrayList<>();
	
	/** 空闲时间 */
	protected long idleTime = 10000;
	
	/**
	 * 启动线程
	 */
	@Override
	public void run()
	{
		TaskProducer producer = null;
		
		//这里是一个无限循环的工作
		while(true)
		{
			//没有生成器,则停止该循环处理工作
			if(producers.size() == 0)
			{
				break;
			}
			
			int size = producers.size();
			for(int i = 0; i < size; i ++)
			{
				producer = producers.get(i);
				if(producer.needToStart())
				{
					logger.info("Start the producer '" + producer.getName() + "'.");
					
					Thread thread = new Thread(producer);
					thread.start();
				}
				
				//设置空闲资源
				try
				{
					Thread.sleep(idleTime);
				}
				catch (Exception e) {
				}
			}
		}
	}
	
	/**
	 * 添加任务生成器
	 * @param producer 任务生成器
	 */
	public void addTaskProducer(TaskProducer producer)
	{
		producer.addTaskProducerEventListener(this);
		producers.add(producer);
	}
	
	/**
	 * 删除任务生成器
	 * @param producer 任务生成器
	 */
	public void removeTaskProducer(TaskProducer producer)
	{
		producer.removeTaskProducerEventListener(this);
		producers.remove(producer);
	}

	/**
	 * 监听任务生成器事件
	 * 
	 * @param event 任务生成器事件
	 */
	@Override
	public void notify(TaskProcuderEvent event)
	{
		TaskProducerEventType type = event.getType();
		TaskProducer producer = event.getProducer();
		if(type == TaskProducerEventType.Add)
		{
			if(producer != null)
				producers.add(producer);
		}
		else if(type == TaskProducerEventType.Remove)
		{
			if(producer != null)
				producers.remove(producer);
		}
	}
}
