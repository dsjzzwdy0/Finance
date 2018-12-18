package com.loris.soccer.bean.table;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.soccer.bean.item.IssueMatch;
import com.loris.soccer.bean.item.MatchItem;

@TableName("soccer_lottery_jc")
public class JcMatch extends IssueMatch
{
	/***/
	private static final long serialVersionUID = 1L;
	
	private String rqwinodds;  		//让球胜赔率
	private String rqdrawodds; 		//让球平赔率
	private String rqloseodds; 		//让球负赔率
	private boolean isrqopen;    	//让球是否开售
	
	public String getMatchhour()
	{
		String[] strings = matchtime.split(" ");
		return strings[strings.length - 1];
	}	
	public String getRqwinodds()
	{
		return rqwinodds;
	}
	public void setRqwinodds(String rqwinodds)
	{
		this.rqwinodds = rqwinodds;
	}
	public String getRqdrawodds()
	{
		return rqdrawodds;
	}
	public void setRqdrawodds(String rqdrawodds)
	{
		this.rqdrawodds = rqdrawodds;
	}
	public String getRqloseodds()
	{
		return rqloseodds;
	}
	public void setRqloseodds(String rqloseodds)
	{
		this.rqloseodds = rqloseodds;
	}
	public boolean isIsrqopen()
	{
		return isrqopen;
	}
	public void setIsrqopen(boolean isrqopen)
	{
		this.isrqopen = isrqopen;
	}
	
	public void setJcMatch(JcMatch match)
	{
		//setMatchInfo(match);
		setIssueMatch(match);		
		rqwinodds = match.rqwinodds;
		rqdrawodds = match.rqdrawodds;
		rqloseodds = match.rqloseodds;
		isrqopen = match.isrqopen;	
	}
	
	public boolean equal(JcMatch match)
	{
		if(this == match || mid.equalsIgnoreCase(match.getMid()))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 创建比赛对象
	 * 
	 * @return 比赛实体
	 */
	public MatchItem createMatch()
	{
		Match match = new Match();
		match.setMid(mid);
		match.setLid(lid);
		match.setHomeid(homeid);
		match.setClientid(clientid);
		match.setMatchtime(matchtime);		
		return match;
	}
		
	@Override
	public String toString()
	{
		return "JcMatch [mid=" + mid + ", ordinary=" + ordinary + ", issue=" + issue + ", lid=" + lid + ", leaguename="
				+ leaguename + ", closetime=" + closetime + ", matchtime=" + matchtime + ", homeid=" + homeid
				+ ", homename=" + homename + ", homelid=" + homelid + ", homerank=" + homerank + ", clientid="
				+ clientid + ", clientname=" + clientname + ", clientlid=" + clientlid + ", clientrank=" + clientrank
				+ ", winodds=" + winodds + ", drawodds=" + drawodds + ", loseodds=" + loseodds + ", isopen=" + isopen
				+ ", ranqiu=" + rangqiu + ", rqwinodds=" + rqwinodds + ", rqdrawodds=" + rqdrawodds + ", rqloseodds="
				+ rqloseodds + ", isrqopen=" + isrqopen + "]";
	}
}
