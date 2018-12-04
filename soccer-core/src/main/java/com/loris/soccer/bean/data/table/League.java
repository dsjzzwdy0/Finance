package com.loris.soccer.bean.data.table;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.entity.AutoIdEntity;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.type.LeagueType;

@TableName("soccer_league")
public class League extends AutoIdEntity
{
	/**The serial uid. */
	private static final long serialVersionUID = -2540313810534972738L;
	
	//public static final String TYPE_LEAGUE = "league";
	//public static final String TYPE_CUP = "cup";

	protected String lid;          	//赛事编号
	protected String name;         	//赛事名称
	protected String country;      	//所属国家
	protected String type;         	//赛事类型: league(联赛)、cup(杯赛)
	protected String continent;    	//所属洲
	protected String introduction; 	//简要介绍
	protected String logo; 			//图标
	
	public String getLid()
	{
		return lid;
	}
	public void setLid(String lid)
	{
		this.lid = lid;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public LeagueType getLeagueType()
	{
		switch (type) {
		case SoccerConstants.MATCH_TYPE_CUP:
			return LeagueType.CUP;
		case SoccerConstants.MATCH_TYPE_LEAGUE:
			return LeagueType.LEAGUE;
		default:
			return LeagueType.ALL;
		}
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getCountry()
	{
		return country;
	}
	public void setCountry(String country)
	{
		this.country = country;
	}
	public String getContinent()
	{
		return continent;
	}
	public void setContinent(String continent)
	{
		this.continent = continent;
	}
	public String getIntroduction()
	{
		return introduction;
	}
	public void setIntroduction(String introduction)
	{
		this.introduction = introduction;
	}
	public String getLogo()
	{
		return logo;
	}
	public void setLogo(String logo)
	{
		this.logo = logo;
	}
	@Override
	public String toString()
	{
		return "League [lid=" + lid + ", name=" + name + ", country=" + country + ", type=" + type + ", continent="
				+ continent + ", introduction=" + introduction + "]";
	}
}
