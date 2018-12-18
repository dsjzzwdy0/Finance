package com.loris.soccer.bean.item;

public class IssueMatch extends MatchInfoItem
{
	/***/
	private static final long serialVersionUID = 1L;

	protected String issue;
	protected String date;
	protected String ordinary;
	protected String homelid;      	//主队所属联赛编号
	protected String clientlid;    	//客队所属联赛编号	
	protected String homerank;     	//主队排名
	protected String clientrank;   	//客队排名
	protected String closetime;    	//截期时间
	protected boolean closed;			//是否结束
	
	protected String rangqiu;      	//让球数
	protected String winodds;      	//胜赔率
	protected String drawodds;     	//平赔率
	protected String loseodds;     	//负赔率
	protected boolean isopen;      	//是否开售
	
	public String getIssue()
	{
		return issue;
	}
	public void setIssue(String issue)
	{
		this.issue = issue;
	}
	public String getDate()
	{
		return date;
	}
	public void setDate(String date)
	{
		this.date = date;
	}
	public String getOrdinary()
	{
		return ordinary;
	}
	public void setOrdinary(String ordinary)
	{
		this.ordinary = ordinary;
	}
	
	public String getHomerank()
	{
		return homerank;
	}
	public void setHomerank(String homerank)
	{
		this.homerank = homerank;
	}
	public String getClientrank()
	{
		return clientrank;
	}
	public void setClientrank(String clientrank)
	{
		this.clientrank = clientrank;
	}
	public String getHomelid()
	{
		return homelid;
	}
	public void setHomelid(String homelid)
	{
		this.homelid = homelid;
	}
	public String getClientlid()
	{
		return clientlid;
	}
	public void setClientlid(String clientlid)
	{
		this.clientlid = clientlid;
	}
	public String getClosetime()
	{
		return closetime;
	}
	
	public boolean isClosed()
	{
		return closed;
	}
	public void setClosed(boolean closed)
	{
		this.closed = closed;
	}
	public String getRangqiu()
	{
		return rangqiu;
	}
	public void setRangqiu(String rangqiu)
	{
		this.rangqiu = rangqiu;
	}
	public String getWinodds()
	{
		return winodds;
	}
	public void setWinodds(String winodds)
	{
		this.winodds = winodds;
	}
	public String getDrawodds()
	{
		return drawodds;
	}
	public void setDrawodds(String drawodds)
	{
		this.drawodds = drawodds;
	}
	public String getLoseodds()
	{
		return loseodds;
	}
	public void setLoseodds(String loseodds)
	{
		this.loseodds = loseodds;
	}
	public boolean isIsopen()
	{
		return isopen;
	}
	public void setIsopen(boolean isopen)
	{
		this.isopen = isopen;
	}
	public void setClosetime(String closetime)
	{
		this.closetime = closetime;
	}
	public void setIssueMatch(IssueMatch match)
	{
		super.setMatchInfoItem(match);
		
		ordinary = match.ordinary;
		closetime = match.closetime; 
		winodds = match.winodds; 
		drawodds = match.drawodds;
		loseodds = match.loseodds;
		isopen  = match.isopen;

		rangqiu = match.rangqiu;
		homelid = match.homelid;
		clientlid = match.clientlid;
		issue = match.issue;
		ordinary = match.ordinary;
		homerank = match.homerank;  
		clientrank = match.clientrank;
		closetime = match.closetime;
		closed = match.closed;
	}
	
	/**
	 * 检测是否同一期比赛
	 * @param is 期号
	 * @return 是同一期比赛的标志
	 */
	public boolean checkIssue(String issue)
	{
		return this.issue.equals(issue);
	}
}
