package com.loris.soccer.bean.data.table.league;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.soccer.bean.item.PerformItem;

@TableName("soccer_round_rank")
public class Rank extends PerformItem
{
	/***/
	private static final long serialVersionUID = 1L;
	
	private String lid;					//联赛编号
	private String leaguename; 			//联赛名称
	private String season;				//赛季编号
	private String round;				//轮次
	private String type; 				//类型：全部a、主场h、客场c、上半场、下半场
	private int rank; 					//球队排名
	private String updatetime;			//更新时间
	
	/**
	 * Set the Round Info
	 * @param round
	 */
	public void setRoundInfo(Round round)
	{
		this.season = round.getSeason();
		this.round = round.getRid();
	}
	
	public String getLid()
	{
		return lid;
	}
	public void setLid(String lid)
	{
		this.lid = lid;
	}
	public String getLeaguename()
	{
		return leaguename;
	}
	public void setLeaguename(String leaguename)
	{
		this.leaguename = leaguename;
	}
	public String getSeason()
	{
		return season;
	}
	public void setSeason(String season)
	{
		this.season = season;
	}
	public String getRound()
	{
		return round;
	}
	public void setRound(String round)
	{
		this.round = round;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	
	public int getRank()
	{
		return rank;
	}
	public void setRank(int rank)
	{
		this.rank = rank;
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
		return "Rank [lid=" + lid + ", leaguename=" + leaguename + ", season=" + season + ", round=" + round + ", type="
				+ type + ", tid=" + tid + ", name=" + name + ", rank=" + rank + ", gamenum=" + gamenum + ", winnum="
				+ winnum + ", drawnum=" + drawnum + ", losenum=" + losenum + ", score=" + score + ", goal=" + goal
				+ ", losegoal=" + losegoal + "]";
	}
}
