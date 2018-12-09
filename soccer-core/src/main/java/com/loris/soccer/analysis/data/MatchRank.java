package com.loris.soccer.analysis.data;

import com.loris.soccer.bean.item.IssueMatch;
import com.loris.soccer.bean.view.MatchInfo;
import com.loris.soccer.bean.view.RankInfo;

public class MatchRank extends IssueMatch
{
	/***/
	private static final long serialVersionUID = 1L;

	protected RankInfo homeRank;
	protected RankInfo clientRank;
	
	
	/**
	 * 比赛信息数据
	 * @param matchInfo
	 */
	public MatchRank(MatchInfo matchInfo)
	{
		setMatchInfoItem(matchInfo);
	}
	
	/**
	 * 创建比赛
	 * @param issueMatch
	 */
	public MatchRank(IssueMatch issueMatch)
	{
		setIssueMatch(issueMatch);
		setOrdinary(issueMatch.getOrdinary());
	}

	public RankInfo getHomeRank()
	{
		return homeRank;
	}

	public void setHomeRank(RankInfo homeRank)
	{
		this.homeRank = homeRank;
	}

	public RankInfo getClientRank()
	{
		return clientRank;
	}

	public void setClientRank(RankInfo clientRank)
	{
		this.clientRank = clientRank;
	}
	
	
}
