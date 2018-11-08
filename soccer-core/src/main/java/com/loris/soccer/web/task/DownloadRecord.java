package com.loris.soccer.web.task;

import java.util.Date;

import com.loris.base.data.Record;

/**
 * 数据下载的记录
 * @author deng
 *
 */
public class DownloadRecord extends Record<String, Date>
{
	/**
	 * Create a new instance of DownloadRecord.
	 * @param key
	 */
	public DownloadRecord(String key)
	{
		this.key = key;
	}
	
	/**
	 * 加入当前的时间记录
	 */
	public void addCurrentRecord()
	{
		addRecord(new Date());
	}
	
	/**
	 * 清除有些已经比较旧的数据记录
	 * @param date
	 */
	public void clearRecord(Date date)
	{
		int size = size();
		Date d;
		long time = date.getTime();
		for (int i = size -1; i >= 0; i --)
		{
			d = list.get(i);
			if(d.getTime() < time)
			{
				list.remove(i);
			}
		}
	}
}
