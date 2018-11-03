package com.loris.soccer.bean.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.loris.base.util.ArraysUtil;
import com.loris.base.util.ArraysUtil.EqualChecker;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.data.table.league.League;

/**
 * 联赛数据类
 * @author jiean
 *
 */
public class LeagueMap
{
	/** The maps. */
	Map<String, League> maps = new HashMap<>();
	
	/** 不需要更新的列表 */
	public static String[] EXCLUDE_LEAGUES = {
		"264", //name=意超杯
		"704", //name=西超杯,
		"842", //name=德超杯,
		"698", //name=法超杯,
		"69", //name=葡超杯, 
		"58", //name=荷超杯, 
		"110", //name=比超杯,
		"195", //name=爱足杯,
		"191", //name=俄超杯,
		"494", //name=罗超杯,
		"705", //name=土超杯,
		"248", //name=克超杯,
		"496", //name=保超盃,
		"1164", //name=欧青赛
		"834", //name=奥迪杯,
		"664", //name=加拿冠,
		"232", //name=金杯赛,
		"424", //name=中美杯,
		"374", //name=韩国杯,
		"892", //name=世预赛
		"304", //name=世俱杯,
		"88", //name=联合会杯
		"76", //name=世青,
		"1299", //name=国冠杯
	};
	
	/**
	 * 杯赛数据检测器
	 */
	private static EqualChecker<League> cupChecker = new ArraysUtil.EqualChecker<League>()
	{	
		@Override
		public boolean isSameObject(League obj)
		{
			return SoccerConstants.MATCH_TYPE_CUP.equalsIgnoreCase(obj.getType());
		}
	};
	
	/**
	 * 联赛数据检测器
	 */
	private static EqualChecker<League> leagueChecker = new ArraysUtil.EqualChecker<League>()
	{	
		@Override
		public boolean isSameObject(League obj)
		{
			return SoccerConstants.MATCH_TYPE_LEAGUE.equalsIgnoreCase(obj.getType());
		}
	};
	
	/**
	 * Create a new instance of LeagueMap.
	 */
	public LeagueMap()
	{
	}
	
	/**
	 * Create the LeagueMap value.
	 * @param leagues League List value.
	 */
	public LeagueMap(List<League> leagues)
	{
		addLeagues(leagues);
	}
	
	/**
	 * 加入League
	 * @param leagues League List value.
	 */
	public void addLeagues(List<League> leagues)
	{
		for (League league : leagues)
		{
			maps.put(league.getLid(), league);
		}
	}
	
	/**
	 * Get the League value.
	 * @param lid League id value.
	 * @return League.
	 */
	public League getLeague(String lid)
	{
		return maps.get(lid);
	}
	
	/**
	 * 获得杯赛的数据列表
	 * @return 数据列表
	 */
	public List<League> getCupLeagues()
	{
		List<League> leagues = new ArrayList<>();
		ArraysUtil.getListValues(maps.values(), leagues, cupChecker);
		return leagues;
	}
	
	/**
	 * 获得联赛数据列表
	 * @return 联赛数据列表
	 */
	public List<League> getLeagueLeagues()
	{
		List<League> leagues = new ArrayList<>();
		ArraysUtil.getListValues(maps.values(), leagues, leagueChecker);
		return leagues;
	}
	
	/**
	 * KeySet value.
	 * @return Key Set value.
	 */
	public Set<String> keySet()
	{
		return maps.keySet();
	}
	
	/**
	 * 检测某一联赛是否有比赛
	 * @param lid 联赛编号
	 * @return 是否在进行中的标志
	 */
	public boolean isNotActive(String lid)
	{
		for (String string : EXCLUDE_LEAGUES)
		{
			if(lid.equalsIgnoreCase(string))
			{
				return true;
			}
		}
		return false;
	}
}
