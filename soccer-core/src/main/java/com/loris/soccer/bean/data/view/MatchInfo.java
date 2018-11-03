package com.loris.soccer.bean.data.view;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.soccer.bean.item.MatchInfoItem;
import com.loris.soccer.bean.item.ScoreItem;

@TableName("soccer_match_info")
public class MatchInfo extends MatchInfoItem
{
	/***/
	private static final long serialVersionUID = 1L;
	
	protected String season;    	//赛季类别
	protected String round;     	//比赛轮次: 1、2.... 第一轮、第二轮、半决赛、决赛等
	protected String score;     	//比分
	protected String halfscore; 	//半场比分
	protected String handicap;  	//让球盘
	protected boolean ended;    	//是否关闭
	
	protected String leaguetype;
	protected String continent;
	
	public String getLeaguename()
	{
		return leaguename;
	}
	public void setLeaguename(String leaguename)
	{
		this.leaguename = leaguename;
	}
	public String getLeaguetype()
	{
		return leaguetype;
	}
	public void setLeaguetype(String leaguetype)
	{
		this.leaguetype = leaguetype;
	}
	public String getContinent()
	{
		return continent;
	}
	public void setContinent(String continent)
	{
		this.continent = continent;
	}
	
	public void setMatchInfo(MatchInfo info)
	{
		if(info == null)
		{
			return;
		}
		setMatchInfoItem(info);
		leaguetype = info.leaguetype;
		continent = info.continent;
		season = info.season;
		round = info.round;
		score = info.score;
		halfscore = info.halfscore;
		ended = info.ended;
		handicap = info.handicap;
	}
	public int getHomescore()
	{
		return getScoreResult().getWingoal();
	}
	
	public int getClientscore()
	{
		return getScoreResult().getLosegoal();
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
	public String getScore()
	{
		return score;
	}
	public void setScore(String score)
	{
		this.score = score;
	}
	public String getHalfscore()
	{
		return halfscore;
	}
	public void setHalfscore(String halfscore)
	{
		this.halfscore = halfscore;
	}
	public String getHandicap()
	{
		return handicap;
	}
	public void setHandicap(String handicap)
	{
		this.handicap = handicap;
	}
	public boolean isEnded()
	{
		return ended;
	}
	public void setEnded(boolean ended)
	{
		this.ended = ended;
	}
	
	/**
	 * 比赛结果
	 * 
	 * @return
	 */
	public ScoreItem getScoreResult()
	{
		return new ScoreItem(score);
	}
	
	/**
	 * 半场比赛结果
	 * 
	 * @return
	 */
	public ScoreItem getHalfMatchResult()
	{
		return new ScoreItem(halfscore);
	}
	@Override
	public String toString()
	{
		return "MatchInfo [homename=" + homename + ", clientname=" + clientname + ", leaguename=" + leaguename
				+ ", leaguetype=" + leaguetype + ", continent=" + continent + ", mid=" + getMid()
				+ ", season=" + getSeason() + ", lid=" + getLid() + ", htid=" + getClientid()
				+ ", ctid=" + getClientid() + ", ended=" + isEnded() + ", round=" + getRound()
				+ ", matchtime=" + getMatchtime() + ", score=" + getScore() + ", halfscore="
				+ getHalfscore() + ", handicap=" + getHandicap() + "]";
	}
}
