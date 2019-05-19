package com.loris.soccer.web.scheduler;

import java.util.Date;

import com.loris.base.bean.entity.UUIDEntity;

/**
 * SchedulerBatchInfo
 * 
 * @author jiean
 *
 */
public class SchedulerBatchInfo extends UUIDEntity
{
	/***/
	private static final long serialVersionUID = 1L;
	protected String user;
	protected Date date;
	protected String name;
	protected int batchno;
	protected int tasknum;
	protected int finishnum = 0;
	protected Date finishtime;
	
	public String getUser()
	{
		return user;
	}
	public void setUser(String user)
	{
		this.user = user;
	}
	public Date getDate()
	{
		return date;
	}
	public void setDate(Date date)
	{
		this.date = date;
	}
	public Date getFinishtime()
	{
		return finishtime;
	}
	public void setFinishtime(Date finishtime)
	{
		this.finishtime = finishtime;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getBatchno()
	{
		return batchno;
	}
	public void setBatchno(int batchno)
	{
		this.batchno = batchno;
	}
	public int getTasknum()
	{
		return tasknum;
	}
	public void setTasknum(int tasknum)
	{
		this.tasknum = tasknum;
	}
	public int getFinishnum()
	{
		return finishnum;
	}
	public void addFinishnum(int num)
	{
		finishnum += num;
	}
	public void addFinish()
	{
		finishnum ++;
	}
	@Override
	public String toString()
	{
		return "SchedulerBatchInfo [user=" + user + ", date=" + date + ", name=" + name + ", batchno=" + batchno
				+ ", tasknum=" + tasknum + ", finishnum=" + finishnum + ", finishtime=" + finishtime + "]";
	}
}
