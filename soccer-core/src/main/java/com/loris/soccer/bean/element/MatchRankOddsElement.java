package com.loris.soccer.bean.element;

import com.loris.soccer.analysis.data.MatchOdds;
<<<<<<< HEAD
import com.loris.soccer.analysis.util.PossionUtil;
=======
import com.loris.soccer.analysis.util.HandicapUtil;
>>>>>>> 9b450c39b7c085402877e394d4583d6f2ceaf855
import com.loris.soccer.analysis.util.OddsUtil;
import com.loris.soccer.bean.data.view.RankInfo;

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
		
<<<<<<< HEAD
		double[] probs = PossionUtil.computeOddsProb(homeavg, clientavg);
=======
		double[] probs = HandicapUtil.computeOddsProb(homeavg, clientavg);
>>>>>>> 9b450c39b7c085402877e394d4583d6f2ceaf855
		
		OddsElement element1 = OddsUtil.createOpItem(probs);
		this.addOpItem(element1);
	}
}
