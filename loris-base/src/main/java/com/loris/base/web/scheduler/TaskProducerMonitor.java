package com.loris.base.web.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.web.task.Task;
import com.loris.base.web.task.TaskProducer;
import com.loris.base.web.task.event.TaskProcuderEvent;
import com.loris.base.web.task.event.TaskProcuderEvent.TaskProducerEventType;
import com.loris.base.web.task.event.TaskProducerEventListener;

/**
 * 任务生成器的管理器
 * @author deng
 *
 */
public class TaskProducerMonitor<T extends Task> implements Runnable, TaskProducerEventListener<T>
{
	private static Logger logger = Logger.getLogger(TaskProducerMonitor.class);
	
	/** TaskProducer. */
	protected List<TaskProducer<T>> producers = new ArrayList<>();
	
	/** 空闲时间 */
	protected long idleTime = 60000;
	
	/**
	 * 启动线程
	 */
	@Override
	public void run()
	{
		TaskProducer<T> producer = null;
		
		//这里是一个无限循环的工作
		while(true)
		{
			//没有生成器,则停止该循环处理工作
			if(producers.size() == 0)
			{
				logger.info("There are no producers, exit.");
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
					//continue;
				}
				else
				{
					logger.info(producer.getName() + " neednot to start.");
				}
			}
			
			//设置空闲资源
			try
			{
				logger.info("Waiting for next producing...");
				Thread.sleep(idleTime);
			}
			catch (Exception e) {
				logger.info("Error: " + e.toString());
			}
		}
	}
	
	/**
	 * 添加任务生成器
	 * @param producer 任务生成器
	 */
	public void addTaskProducer(TaskProducer<T> producer)
	{
		producer.addTaskProducerEventListener(this);
		producers.add(producer);
	}
	
	/**
	 * 删除任务生成器
	 * @param producer 任务生成器
	 */
	public void removeTaskProducer(TaskProducer<T> producer)
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
	public void notify(TaskProcuderEvent<T> event)
	{
		TaskProducerEventType type = event.getType();
		TaskProducer<T> producer = event.getProducer();
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
