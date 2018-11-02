package com.loris.soccer.bean.data.table.lottery;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.entity.UserIDEntity;

@TableName("soccer_lottery_calendar")
public class LotteryCalendar extends UserIDEntity
{
	/***/
	private static final long serialVersionUID = 1L;

	private String date;
	private String bdissue;
	private int bd;
	private String jcissue;
	private int jc;
	private String zc;
	private String all;
	private String updatetime;
	
	public String getDate()
	{
		return date;
	}
	public void setDate(String date)
	{
		this.date = date;
	}
	public String getBdissue()
	{
		return bdissue;
	}
	public void setBdissue(String bdissue)
	{
		this.bdissue = bdissue;
	}
	public int getBd()
	{
		return bd;
	}
	public void setBd(int bd)
	{
		this.bd = bd;
	}
	public String getJcissue()
	{
		return jcissue;
	}
	public void setJcissue(String jcissue)
	{
		this.jcissue = jcissue;
	}
	public int getJc()
	{
		return jc;
	}
	public void setJc(int jc)
	{
		this.jc = jc;
	}
	public String getZc()
	{
		return zc;
	}
	public void setZc(String zc)
	{
		this.zc = zc;
	}
	
	public String getAll()
	{
		return all;
	}
	public void setAll(String all)
	{
		this.all = all;
	}
	public String getUpdatetime()
	{
		return updatetime;
	}
	public void setUpdatetime(String updatetime)
	{
		this.updatetime = updatetime;
	}
	@Override
	public String toString()
	{
		return "MatchCalendar [date=" + date + ", bdissue=" + bdissue + ", bd=" + bd + ", jcissue=" + jcissue + ", jc="
				+ jc + ", zc=" + zc + "]";
	}
}
