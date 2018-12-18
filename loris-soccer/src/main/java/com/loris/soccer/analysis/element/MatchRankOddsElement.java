package com.loris.soccer.analysis.element;

import com.loris.soccer.analysis.data.MatchOdds;
import com.loris.soccer.analysis.util.PossionUtil;
import com.loris.soccer.bean.view.RankInfo;
import com.loris.soccer.analysis.util.OddsUtil;

public class MatchRankOddsElement extends MatchOddsElement
{
	/***/
	private static final long serialVersionUID = 1L;
	
	protected RankInfo homeRankInfo;
	protected RankInfo clientRankInfo;
	
	public MatchRankOddsElement(MatchOdds ops)
	{
		super(ops);
	}

	public RankInfo getHomeRankInfo()
	{
		return homeRankInfo;
	}

	public void setHomeRankInfo(RankInfo homeRankInfo)
	{
		this.homeRankInfo = homeRankInfo;
	}

	public RankInfo getClientRankInfo()
	{
		return clientRankInfo;
	}

	public void setClientRankInfo(RankInfo clientRankInfo)
	{
		this.clientRankInfo = clientRankInfo;
	}

	public void createTheoryOdds()
	{
		if(homeRankInfo == null || clientRankInfo == null)
		{
			return;
		}
		double homeavg = homeRankInfo.getGoal() * 1.0 / homeRankInfo.getGamenum();
		double clientavg = clientRankInfo.getGoal() * 1.0 / clientRankInfo.getGamenum();
		
		double[] probs = PossionUtil.computeOddsProb(homeavg, clientavg);

		OddsElement element1 = OddsUtil.createOpItem(probs);
		this.addOpItem(element1);
	}
}
