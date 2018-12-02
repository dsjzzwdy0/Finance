package com.loris.soccer.bean.data.table.league;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.soccer.bean.item.MatchItem;
import com.loris.soccer.bean.item.ScoreItem;

@TableName("soccer_match")
public class Match extends MatchItem
{
	/***/
	private static final long serialVersionUID = 1L;

	protected String season;    	//赛季类别
	protected String round;     	//比赛轮次: 1、2.... 第一轮、第二轮、半决赛、决赛等
	protected String score;     	//比分
	protected String halfscore; 	//半场比分
	protected String handicap;  	//让球盘
	protected boolean ended;    	//是否关闭
	
	public Match()
	{
	}
	
	public Match(MatchItem item)
	{
		setMatchItem(item);
	}
	
	public String getMid()
	{
		return mid;
	}
	public void setMid(String mid)
	{
		this.mid = mid;
	}
	public String getSeason()
	{
		return season;
	}
	public void setSeason(String season)
	{
		this.season = season;
	}
	public String getLid()
	{
		return lid;
	}
	public void setLid(String lid)
	{
		this.lid = lid;
	}
	public boolean isEnded()
	{
		return ended;
	}
	public void setEnded(boolean ended)
	{
		this.ended = ended;
	}
	public void setEnded()
	{
		this.ended = true;
	}
	
	public String getRound()
	{
		return round;
	}
	public void setRound(String round)
	{
		this.round = round;
	}
	public String getMatchtime()
	{
		return matchtime;
	}
	public void setMatchtime(String matchtime)
	{
		this.matchtime = matchtime;
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
	
	public int getHomescore()
	{
		return getScoreResult().getWingoal();
	}
	
	public int getClientscore()
	{
		return getScoreResult().getLosegoal();
	}
	
	public void setMatch(Match match)
	{
		setMatchItem(match);
		season = match.season;
		round = match.round;
		score = match.score;
		halfscore = match.halfscore;
		ended = match.ended;
		handicap = match.handicap;
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
	
	/**
	 * 检测是否该轮次的比赛
	 * @param round Round对象
	 * @return 是否属于的标志
	 */
	public boolean isRoundMatch(Round r)
	{
		return lid.equals(r.getLid()) && season.equals(r.getSeason()) 
				&& round.equals(r.getName());
	}
	
	@Override
	public String toString()
	{
		return "Match [mid=" + mid + ", lid=" + lid + ", hteam=" + homeid + ", cteam=" + clientid + ", season=" + season
				+ ", round=" + round + ", matchtime=" + matchtime + ", score=" + score + ", halfscore=" + halfscore
				+ ", handicap=" + handicap + ", ended=" + ended + "]";
	}
}
