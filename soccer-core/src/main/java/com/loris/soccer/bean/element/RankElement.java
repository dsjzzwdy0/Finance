package com.loris.soccer.bean.element;

import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.data.view.RankInfo;
import com.loris.soccer.bean.item.MatchItem;

import java.util.ArrayList;
import java.util.List;

public class RankElement extends MatchItem
{
	/***/
	private static final long serialVersionUID = 1L;

	protected List<RankInfo> ranks = new ArrayList<>();
	
	protected RankInfo htotalrank;
	protected RankInfo ctotalrank;
	protected RankInfo hhomerank;
	protected RankInfo cguestrank;
	
	public RankElement(MatchItem item)
	{
		setMatchItem(item);
	}
	
	public RankElement(MatchItem item, List<RankInfo> rankInfos)
	{
		this(item);
		setRanks(rankInfos);
	}
	
	public List<RankInfo> getRanks()
	{
		return ranks;
	}
	public void setRanks(List<RankInfo> ranks)
	{
		this.ranks = ranks;
	}	
	public void addRanks(List<RankInfo> rankInfos)
	{
		ranks.addAll(rankInfos);
	}
	public RankInfo getHtotalrank()
	{
		return getRankInfo(homeid, SoccerConstants.RANK_TOTAL);
	}
	public RankInfo getCtotalrank()
	{
		return getRankInfo(clientid, SoccerConstants.RANK_TOTAL);
	}
	public RankInfo getHhomerank()
	{
		return getRankInfo(homeid, SoccerConstants.RANK_HOME);
	}
	public RankInfo getCguestrank()
	{
		return getRankInfo(clientid, SoccerConstants.RANK_CLIENT);
	}

	protected RankInfo getRankInfo(String tid, String type)
	{
		for (RankInfo rankInfo : ranks)
		{
			if(rankInfo.getTid().equals(tid) && type.equals(rankInfo.getType()))
			{
				return rankInfo;
			}
		}
		return null;
	}
}
