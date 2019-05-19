package com.loris.soccer.analysis.util;

import java.util.ArrayList;
import java.util.List;

import com.loris.soccer.bean.view.MatchInfo;

public class TeamHistoryCalculator
{
	/**
	 * 获取最近的比赛
	 * 
	 * @param matchs 比赛列表
	 * @param t1 球队1编号
	 * @param t2 球队2编号
	 * @return 比赛
	 */
	public static MatchInfo getLastMatch(List<? extends MatchInfo> matchs, String t1, String t2)
	{
		MatchInfo match = null;
		for (MatchInfo m : matchs)
		{
			if(m.isTwoTeam(t1, t2))
			{
				if(match == null)
				{
					match = m;
					continue;
				}
				else
				{
					if(m.getMatchtime().compareTo(match.getMatchtime()) > 0)
					{
						match = m;
					}
				}
			}
		}
		
		return match;
	}
	
	/**
	 * 获得历史比赛数据信息
	 * 
	 * @param matches 比赛数据列表
	 * @param t1 球队1的编号
	 * @param t2 球队2的编号
	 * @param size 比赛数量值
	 * @return 符合条件的比赛数据
	 */
	public static List<MatchInfo> getLastMatchs(List<? extends MatchInfo> matches, String t1, String t2, int size)
	{
		List<MatchInfo> result = new ArrayList<>();
		for (MatchInfo m : matches)
		{
			if(m.isTwoTeam(t1, t2))
			{
				if(existInMatchList(result, m))
				{
					continue;
				}				
				result.add(m);
				if(result.size() == size)
				{
					return result;
				}
			}
		}
		return result;
	}
	
	/**
	 * 获得历史比赛数据信息
	 * 
	 * @param matches 比赛数据列表
	 * @param tid 球队编号
	 * @param size 比赛数量值
	 * @return 符合条件的比赛数据
	 */
	public static List<MatchInfo> getLastMatchs(List<? extends MatchInfo> matches, String tid, int size)
	{
		List<MatchInfo> result = new ArrayList<>();
		for (MatchInfo match : matches)
		{
			if(match.getHomeid().equals(tid) || match.getClientid().equals(tid))
			{
				if(existInMatchList(result, match))
				{
					continue;
				}				
				result.add(match);
				if(result.size() == size)
				{
					return result;
				}
			}
		}
		return result;
	}
	
	/**
	 * 检测是否在比赛列表中已经存在该场比赛
	 * 这里的检测是指采用比赛编号
	 * 
	 * @param matchs 比赛列表
	 * @param match 比赛
	 * @return 是否存在的标志
	 */
	protected static boolean existInMatchList(List<? extends MatchInfo> matchs, MatchInfo match)
	{
		for (MatchInfo m : matchs)
		{
			if(m.getMid().equals(match.getMid()))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 从比赛列表中查找某一个编号的比赛记录
	 * 
	 * @param matchs 比赛列表
	 * @param mid 比赛编号
	 * @return 比赛记录，如果没有符合条件的，则返回null
	 */
	protected static MatchInfo getMatchFromList(List<? extends MatchInfo> matchs, String mid)
	{
		for (MatchInfo match : matchs)
		{
			if(mid.equals(match.getMid()))
			{
				return match;
			}
		}
		return null;
	}
}
