package com.loris.soccer.bean.data.view;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.soccer.bean.data.table.Round;

@TableName("soccer_league_round_matchnum")
public class RoundInfo extends Round
{
	/***/
	private static final long serialVersionUID = 1L;

	private String leaguename;
	private String continent;
	private String country;
	private String leagueid;
	private String type;
	private String introduction;
	private int matchnum;
	
	public String getLeaguename()
	{
		return leaguename;
	}
	public void setLeaguename(String leaguename)
	{
		this.leaguename = leaguename;
	}
	public String getContinent()
	{
		return continent;
	}
	public void setContinent(String continent)
	{
		this.continent = continent;
	}
	public String getCountry()
	{
		return country;
	}
	public void setCountry(String country)
	{
		this.country = country;
	}
	public String getLeagueid()
	{
		return leagueid;
	}
	public void setLeagueid(String leagueid)
	{
		this.leagueid = leagueid;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getIntroduction()
	{
		return introduction;
	}
	public void setIntroduction(String introduction)
	{
		this.introduction = introduction;
	}
	public int getMatchnum()
	{
		return matchnum;
	}
	public void setMatchnum(int matchnum)
	{
		this.matchnum = matchnum;
	}
}
