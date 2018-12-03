package com.loris.soccer.analysis.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.soccer.bean.data.table.Match;
import com.loris.soccer.bean.data.table.Round;
import com.loris.soccer.repository.SoccerManager;

public class LeagueDataUtil
{
	private static Logger logger = Logger.getLogger(LeagueDataUtil.class);
	
	/**
	 * 计算比赛赛事轮次的比赛时间
	 * @param soccerManager The SoccerManager.
	 * @throws Exception Excpetion
	 */
	public static void computeRoundTime(SoccerManager soccerManager) throws Exception
	{
		logger.info("计算比赛赛事轮次的起止时间");
		
		List<Round> rounds = soccerManager.getNullTimeRounds();
		List<Match> matchs = soccerManager.getMatches("", "");
		
		logger.info("Total rounds null time is " + rounds.size());
		logger.info("Total match size is " + matchs.size());
		
		for (Match match : matchs)
		{
			for (Round round : rounds)
			{
				if(match.isRoundMatch(round))
				{
					round.setMatchTime(match.getMatchtime());
				}
			}
		}
		
		List<Round> notNullRounds = new ArrayList<>();
		for (Round round : rounds)
		{
			if(StringUtils.isNotEmpty(round.getStarttime()) && StringUtils.isNotEmpty(round.getEndtime()))
			{
				notNullRounds.add(round);
			}
		}
		
		logger.info("更新轮次数据 Round size " + notNullRounds.size() + "...");
		if(notNullRounds.size() > 0)
		{
			soccerManager.updateRounds(notNullRounds);
		}
	}
}
