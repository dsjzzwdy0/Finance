package com.loris.soccer.bean.data.table;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.entity.AutoIdEntity;
import com.loris.base.util.DateUtil;

@TableName("soccer_league_round")
public class Round extends AutoIdEntity
{
	/***/
	private static final long serialVersionUID = 1L;
	
	private String lid;					//联赛编号
	private String season;				//赛季
	private String group;				//分组
	private String name;				//名称
	private String rid;					//轮次
	private String starttime;			//起始时间
	private String endtime;				//结束时间
	
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
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getRid()
	{
		return rid;
	}
	public void setRid(String rid)
	{
		this.rid = rid;
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
	public String getGroup()
	{
		return group;
	}
	public void setGroup(String group)
	{
		this.group = group;
	}
	
	/**
	 * Set the MatchTime
	 * @param matchtime
	 */
	public void setMatchTime(String matchtime)
	{
		Date date = DateUtil.tryToParseDate(matchtime);
		if(date == null)
		{
			return;
		}
		String d = DateUtil.DATE_TIME_FORMAT.format(date);
		if(StringUtils.isEmpty(starttime) || (d.compareTo(starttime) < 0))
		{
			starttime = d;
		}
		
		if(StringUtils.isEmpty(endtime) || (d.compareTo(endtime) > 0))
		{
			endtime = d;
		}
		
	}
	
	/**
	 * Check the time of the round.
	 * @return Flag value.
	 */
	public boolean checkTime()
	{
		return StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime);
	}
	
	/**
	 * Check if the round equals.
	 * @param round Round value.
	 * @return The flag.
	 */
	public boolean equals(Round round)
	{
		return lid.equals(round.getLid()) && season.equals(round.getSeason()) && name.equals(round.getName());
	}
	
	@Override
	public String toString()
	{
		return "Round [" + getId() + ", lid=" + lid + ", season=" + season + ", name=" + name + ", rid=" + rid + ", starttime="
				+ starttime + ", endtime=" + endtime + "]";
	}
}
