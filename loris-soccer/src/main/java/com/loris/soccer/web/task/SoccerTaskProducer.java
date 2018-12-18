package com.loris.soccer.web.task;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.loris.base.web.task.AbstractTaskProducer;
import com.loris.base.web.task.Task;
import com.loris.base.web.task.event.TaskEvent;
import com.loris.base.web.task.event.TaskEvent.TaskEventType;

/**
 * 足彩数据的生成器
 * 
 * @author deng
 *
 */
public abstract class SoccerTaskProducer extends AbstractTaskProducer<MatchWebTask>
{
	/** 数据下载的记录 */
	protected Map<String, DownloadRecord> downRecords = new HashMap<>();
	
	/** 数据下载的间隔,默认是一个小时*/
	protected long interval = 1000 * 60 * 60;

	@Override
	public void notify(TaskEvent event)
	{
		Task task = event.getTask();
		TaskEventType type = event.getType();
		
		if(task == null)
		{
			return;
		}
		if(task instanceof MatchWebTask)
		{
			if(type == TaskEventType.Finished)
			{
				MatchWebTask task2 = (MatchWebTask)task;
				addDownRecord(task2.getMatch().getMid());
			}
			return;
		}
	}
	
	/**
	 * 加入当前的下载记录
	 * @param key
	 */
	public void addDownRecord(String key)
	{
		DownloadRecord record = downRecords.get(key);
		if(record == null)
		{
			record = new DownloadRecord(key);
			downRecords.put(key, record);
		}
		record.addCurrentRecord();
	}
	
	/**
	 * 删除数据下载的记录
	 * @param key
	 */
	public void removeDownRecord(String key)
	{
		downRecords.remove(key);
	}
	
	/**
	 * 获得最近的下载时间
	 * @param key
	 * @return
	 */
	public Date getLastDownTime(String key)
	{
		DownloadRecord record = downRecords.get(key);
		if(record == null)
		{
			return null;
		}
		else
		{
			return record.getLastDate();
		}
	}
}
