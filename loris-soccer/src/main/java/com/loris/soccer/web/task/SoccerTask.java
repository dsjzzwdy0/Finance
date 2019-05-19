package com.loris.soccer.web.task;

import org.apache.log4j.Logger;

import com.loris.base.web.task.AbstractTask;
import com.loris.base.web.task.event.TaskEvent;
import com.loris.base.web.task.event.TaskEvent.TaskEventType;

/**
 * 
 * @author jiean
 *
 */
public class SoccerTask extends AbstractTask
{
	private static Logger logger = Logger.getLogger(SoccerTask.class);
	
	/** 批次编号　 */
	protected int batchNo;
	
	/** 任务序号　*/
	protected int taskIndex;
	
	/**
	 * Create a new instance of SoccerTask.
	 */
	public SoccerTask()
	{
		name = "SoccerTask";
	}
	
	@Override
	public void run()
	{
	}
	
	/**
	 * 在任务执行之前调用该程序
	 */
	@Override
	public void preExecute()
	{
		logger.info("Start to excute Task-" + name + "[" + batchNo + "][" + taskIndex + "]...");
		notify(new TaskEvent(this, TaskEventType.Start));
	}
	@Override
	public void postExecute()
	{
		logger.info("Finish to excute Task-" + name + "[" + batchNo + "][" + taskIndex + "]...");
		notify(new TaskEvent(this, TaskEventType.Finished));
	}
	@Override
	public void errExecute()
	{
		logger.info("Error occured when excuting Task-243211`" + name + "[" + batchNo + "][" + taskIndex + "]...");
		notify(new TaskEvent(this, TaskEventType.Error));
	}

	@Override
	public int getPriority()
	{
		return 0;
	}

	public int getBatchNo()
	{
		return batchNo;
	}

	public void setBatchNo(int batchNo)
	{
		this.batchNo = batchNo;
	}

	public int getTaskIndex()
	{
		return taskIndex;
	}

	public void setTaskIndex(int taskIndex)
	{
		this.taskIndex = taskIndex;
	}
}
