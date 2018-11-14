package com.loris.soccer.web.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.web.task.Task;
import com.loris.base.web.task.TaskQueue;
import com.loris.base.web.task.event.TaskEvent;
import com.loris.base.web.task.event.TaskEventListener;
import com.loris.base.web.task.event.TaskEvent.TaskEventType;

/**
 * 
 * 任务生成器
 * 
 * @author jiean
 * @param <T>
 */
public abstract class TaskProduceScheduler<T extends Task> extends AbstractScheduler implements TaskEventListener
{
	private static Logger logger = Logger.getLogger(TaskProduceScheduler.class);
	
	/** 任务队列管理器 */
	protected TaskQueue<T> taskQueue;

	/** 当前的任务列表 */
	protected List<T> taskList = new ArrayList<>();

	/** 任务的编号,按照顺序号来进行管理 */
	protected SchedulerBatchInfo batchInfo;
	
	/** 批量编号 */
	protected int batchNo = 1;
	
	/** 是否已经初始化 */
	protected boolean initialized = false;
	
	/** 上一次初始化时间　*/
	protected Date lastInitTimeStamp;
	
	/** 上一次更新的时间　*/
	protected Date lastProduceTimeStamp;
	
	/** 最近一次执行时间 */
	protected Date lastExecuteTimeStamp;
	
	/**
	 * Create a new instance of TaskProducerScheduler.
	 */
	public TaskProduceScheduler()
	{
		interval = 10000;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isInitialized()
	{
		return initialized;
	}
	
	/**
	 * 线程运行
	 */
	@Override
	public void run()
	{
		while(true)
		{
			if(shouldBegin())
			{
				if(!isInitialized())
				{
					logger.info("Start to initialize the '" + name + "'.");
					initialize();
					//更新完成的时间
					setLastInitTimeStamp();
				}
				
				if(!taskList.isEmpty())
				{
					logger.info("There are " + taskList.size() + " task no finished, clear these task.");
					clearPreTasks();
				}

				try
				{
					logger.info("Start to produce the '" + name + "'.");
					produce();
					setLastProduceTimeStamp();
				}
				catch(Exception e)
				{
					logger.info("Error : " + e.toString());
					continue;
				}
				
				if(!taskList.isEmpty())
				{
					logger.info("Push '" + name + "' " + taskList.size() + " tasks to execute scheduler.");
					pushToExecute();
				}
			}
			
			//logger.info("Waiting next task produce.");
			sleep(interval);
		}
	}
	
	/**
	 * 初始化该程序
	 */
	public abstract boolean initialize();
	
	/**
	 * 生成任务
	 */
	public abstract boolean produce();
	
	/**
	 * 检测是否更新
	 */
	@Override
	public abstract boolean shouldBegin();

	/**
	 * 重置数据
	 */
	@Override
	public void reset()
	{
		logger.info("Reset the Task produce scheduler '" + name + "'.");
		clearPreTasks();
		initialized = false;
	}

	/**
	 * 清除原先未完成的任务
	 */
	protected void clearPreTasks()
	{
		synchronized (taskQueue)
		{
			for (T t : taskList)
			{
				taskQueue.remove(t);
			}
		}
		taskList.clear();
	}
	
	/**
	 * 加入到执行的任务列表
	 */
	protected void pushToExecute()
	{
		createBatchInfo();
		synchronized (taskQueue)
		{
			taskQueue.pushAllTasks(taskList);
		}
	}
	
	/**
	 * 处理之前
	 */
	public void createBatchInfo()
	{
		batchInfo = new SchedulerBatchInfo();
		batchInfo.create();
		batchInfo.user = "anonymous";
		batchInfo.date = new Date();
		batchInfo.name = name;
		batchInfo.batchno = batchNo ++;
		batchInfo.tasknum = taskList.size();
		batchInfo.finishnum = 0;
		
		//插入数据库中
	}
	
	/**
	 * 结束当前的批次
	 */
	public void finishBatch()
	{
		batchInfo.finishnum = batchInfo.tasknum - taskList.size();
		batchInfo.finishtime = getLastExecuteTimeStamp();
		
		//更新数据库
	}
	
	public TaskQueue<T> getTaskQueue()
	{
		return taskQueue;
	}

	public void setTaskQueue(TaskQueue<T> taskQueue)
	{
		this.taskQueue = taskQueue;
	}
	
	/**
	 * 获得当前批量处理的信息
	 * @return
	 */
	public SchedulerBatchInfo getSchedulerBatchInfo()
	{
		return batchInfo;
	}
	
	/**
	 * 通知任务处理器
	 * @param event 任务处理事件
	 */
	@Override
	public void notify(TaskEvent event)
	{
		Task task = event.getTask();
		TaskEventType type = event.getType();
		if (type == TaskEventType.Finished)
		{
			batchInfo.addFinish();
			taskList.remove(task);
			setLastExecuteTimeStamp();
			if(taskList.isEmpty())
			{
				finishBatch();
			}
		}
		else if(type == TaskEventType.Error)
		{
			batchInfo.addFinish();
			taskList.remove(task);
		}
	}
	
	public void setLastInitTimeStamp()
	{
		lastInitTimeStamp = new Date();
	}

	public Date getLastInitTimeStamp()
	{
		return lastInitTimeStamp;
	}

	public void setLastInitTimeStamp(Date lastInitTimeStamp)
	{
		this.lastInitTimeStamp = lastInitTimeStamp;
	}

	public Date getLastExecuteTimeStamp()
	{
		return lastExecuteTimeStamp;
	}

	public void setLastExecuteTimeStamp(Date lastExecuteTimeStamp)
	{
		this.lastExecuteTimeStamp = lastExecuteTimeStamp;
	}
	
	public void setLastExecuteTimeStamp()
	{
		lastExecuteTimeStamp = new Date();
	}
	
	public Date getLastProduceTimeStamp()
	{
		return lastProduceTimeStamp;
	}

	public void setLastProduceTimeStamp(Date lastProduceTimeStamp)
	{
		this.lastProduceTimeStamp = lastProduceTimeStamp;
	}
	
	public void setLastProduceTimeStamp()
	{
		lastProduceTimeStamp = new Date();
	}

	/**
	 * 获得小时数
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static double getHour(long time1, long time2)
	{
		return (time1 - time2) * 1.0 / (3600 * 1000);
	}
}
