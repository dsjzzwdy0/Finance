package com.loris.soccer.bean.data.table;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.entity.AutoIdEntity;

@TableName("soccer_league_season")
public class Season extends AutoIdEntity
{
	/***/
	private static final long serialVersionUID = 1L;
	
	private String lid;             //联赛编号
	private String season;          //赛季数据
	private String name;			//名称
	private String starttime;		//开始时间
	private String endtime; 		//结束时间
	
	public String getLid()
	{
		return lid;
	}
	public void setLid(String lid)
	{
		this.lid = lid;
	}
	public String getSeason()
	{
		return season;
	}
	public void setSeason(String season)
	{
		this.season = season;
	}
	public String getStarttime()
	{
		return starttime;
	}
	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}
	public String getEndtime()
	{
		return endtime;
	}
	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	public boolean equals(Season s)
	{
		return lid.equals(s.getLid()) && season.equals(s.getSeason());
	}
	@Override
	public String toString()
	{
		return "Season [lid=" + lid + ", season=" + season + ", name=" + name + ", starttime=" + starttime
				+ ", endtime=" + endtime + "]";
	}
	
}
