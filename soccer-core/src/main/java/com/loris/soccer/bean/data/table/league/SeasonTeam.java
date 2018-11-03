package com.loris.soccer.bean.data.table.league;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.entity.AutoIdEntity;

@TableName("soccer_league_season_team")
public class SeasonTeam extends AutoIdEntity
{
	/***/
	private static final long serialVersionUID = 1L;
	
	private String lid;
	private String season;
	private String tid;
	private String name;
	
	public String getTid()
	{
		return tid;
	}
	public void setTid(String tid)
	{
		this.tid = tid;
	}
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
	@Override
	public String toString()
	{
		return "SeasonTeam [tid=" + tid + ", lid=" + lid + ", season=" + season + "]";
	}
}
