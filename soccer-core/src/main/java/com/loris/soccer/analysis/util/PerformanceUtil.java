package com.loris.soccer.analysis.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.soccer.bean.data.table.JcMatch;
import com.loris.soccer.bean.data.table.Match;
import com.loris.soccer.bean.data.table.Rank;
import com.loris.soccer.bean.data.table.Round;
import com.loris.soccer.bean.item.PerformItem;
import com.loris.soccer.bean.item.ScoreItem;
import com.loris.soccer.bean.type.MatchTeamType;

/**
 * 球队战线计算工厂
 * 
 * @author dsj
 *
 */
public class PerformanceUtil
{	
	/**
	 * 统计计算排位数据
	 * 
	 * @param rank
	 * @param match
	 */
	public static void computeRank(List<Rank> ranks, List<Match> matches, Round round)
	{
		for (Match match : matches)
		{
			addOrCreateRank(ranks, match, round);
		}
	}

	/**
	 * 把统计数据加入到统计信息中
	 * 
	 * @param ranks
	 * @param match
	 * @return
	 */
	protected static void addOrCreateRank(List<Rank> ranks, Match match, Round round)
	{
		// 没有结束的比赛，不进行统计
		if (!match.isEnded())
			return;

		// 比赛结果
		ScoreItem result = match.getScoreResult();
		if (result.getResult() < 0)
			return;

		// 处理主队
		boolean isExist = false;
		for (Rank rank : ranks)
		{
			if (rank.getTid().equals(match.getHomeid())) // 主队
			{
				addPerformanceInfo(rank, result, MatchTeamType.HOME);
				isExist = true;
				break;
			}
		}
		if (!isExist)
		{
			Rank rank = new Rank();
			// rank.setMatch(match, MatchType.HOME);
			rank.setRoundInfo(round);

			addPerformanceInfo(rank, result, MatchTeamType.HOME);
			ranks.add(rank);
		}

		// 处理客队
		isExist = false;
		for (Rank rank : ranks)
		{
			if (rank.getTid().equals(match.getClientid())) // 客队
			{
				addPerformanceInfo(rank, result, MatchTeamType.CLIENT);
				isExist = true;
				break;
			}
		}

		if (!isExist)
		{
			Rank rank = new Rank();
			// rank.setMatch(match, MatchType.CLIENT);
			rank.setRoundInfo(round);

			addPerformanceInfo(rank, result, MatchTeamType.CLIENT);
			ranks.add(rank);
		}
	}

	/**
	 * 统计数据
	 * 
	 * @param rank
	 * @param match
	 * @param type
	 *            类型，1表示是主队统计数据，0表示是统计客队数据
	 */
	public static void addPerformanceInfo(PerformItem rank, ScoreItem result, MatchTeamType type)
	{
		if (result.getResult() < 0)
		{
			return;
		}

		// 主队
		if (type == MatchTeamType.HOME)
		{
			rank.addGoal(result.getWingoal());
			rank.addLoseGoal(result.getLosegoal());

			switch (result.getResult()) {
			case 3:
				rank.incWinGame();
				rank.addScore(3);
				break;
			case 1:
				rank.incDrawGame();
				rank.addScore(1);
				break;
			case 0:
				rank.incLoseGame();
				break;
			default:
				break;
			}
		}
		else // 客队
		{
			rank.addGoal(result.getLosegoal());
			rank.addLoseGoal(result.getWingoal());
			switch (result.getResult()) {
			case 3:
				rank.incLoseGame();
				break;
			case 1:
				rank.incDrawGame();
				rank.addScore(1);
				break;
			case 0:
				rank.incWinGame();
				rank.addScore(3);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 按照让球数统计各个类别的数据
	 * 
	 * @param matchs
	 * @return
	 */
	public static Map<String, Integer> computeRatingNumber(List<JcMatch> matchs)
	{
		Map<String, Integer> ratings = new HashMap<>();
		String r;
		boolean exist = false;
		for (JcMatch match : matchs)
		{
			r = match.getRangqiu();
			exist = false;
			for (String key : ratings.keySet())
			{
				if (key.equals(r))
				{
					exist = true;
					ratings.put(key, ratings.get(key) + 1);
				}
			}

			// 统计列表中没有，则新生一个数据
			if (!exist)
			{
				ratings.put(r, 1);
			}
		}
		return ratings;
	}

	/**
	 * 按照联赛编号统计数据
	 * 
	 * @param matchs
	 * @return
	 */
	public static Map<String, Integer> computeLeagueNumber(List<JcMatch> matchs)
	{
		Map<String, Integer> leagues = new HashMap<>();
		String l;
		boolean exist = false;
		for (JcMatch match : matchs)
		{
			l = match.getRangqiu();
			exist = false;
			for (String key : leagues.keySet())
			{
				if (key.equals(l))
				{
					exist = true;
					leagues.put(key, leagues.get(key) + 1);
				}
			}

			// 统计列表中没有，则新一个数据
			if (!exist)
			{
				leagues.put(l, 1);
			}
		}
		return leagues;
	}

	/**
	 * 创建轮次的比赛时间
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static boolean createRoundTime(Round round, List<Match> matchs)
	{
		String start = null;
		String end = null;
		String matchtime;

		start = null;
		end = null;

		for (Match match : matchs)
		{
			matchtime = match.getMatchtime();
			if (StringUtils.isEmpty(start))
			{
				start = matchtime;
			}
			if (StringUtils.isEmpty(end))
			{
				end = matchtime;
			}

			if (start.compareTo(matchtime) > 0)
			{
				start = matchtime;
			}
			if (end.compareTo(matchtime) < 0)
			{
				end = matchtime;
			}
		}

		if(StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end))
		{
			round.setStarttime(start);
			round.setEndtime(end);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 从比赛列表中按照要求获得比赛的数据
	 * 	 
	 * @param matchs 比赛列表
	 * @param team 球队编号
	 * @param lasxMatchSize 最近的比赛场次
	 * @param type 类型：主场、客场、全部
	 * @return 比赛列表
	 */
	public static List<Match> getMatchList(List<Match> matchs, String team, 
			int lastMatchSize, MatchTeamType type)
	{
		List<Match> rs = new ArrayList<>();
		if(lastMatchSize <= 0)
		{
			return rs;
		}
		for (Match match : matchs)
		{
			if(type == MatchTeamType.HOME)
			{
				if(match.isHomeTeam(team))
				{
					rs.add(match);
				}
			}
			else if(type == MatchTeamType.CLIENT)
			{
				if(match.isClientTeam(team))
				{
					rs.add(match);
				}
			}
			else if(type == MatchTeamType.ALL)
			{
				if(match.isTeam(team))
				{
					rs.add(match);
				}
			}
			
			if(rs.size() == lastMatchSize)
			{
				return rs;
			}
		}
		return rs;
	}
	
	
	/**
	 * 计算球队的最近战绩
	 * 
	 * @param tid 球队编号
	 * @param type 比赛类型：主场、客场、全部
	 * @param matchs 所有相关的历史比赛数据
	 * @param lastMatchSize 比赛场次
	 * @return 战线数据
	 */
	public static PerformItem computeTeamPerformance(String tid, MatchTeamType type, List<Match> matchs, int lastMatchSize)
	{
		if(matchs == null || matchs.size() <= 0)
		{
			throw new IllegalArgumentException("Error, Team<" + tid + "> has no performance matchs, please check the matchs data.");
		}
		//排序，确保取得最近的比赛数据
		Collections.sort(matchs, new Comparator<Match>()
		{
			//要按照时间进行降序排列，因此需要把第2个时间写在前面
			@Override
			public int compare(Match o1, Match o2)
			{
				return o2.compare(o1);
			}
		});
		
		PerformItem perform = new PerformItem(tid);
		List<Match> mlist = PerformanceUtil.getMatchList(matchs, 
				tid, lastMatchSize, type);
		if(mlist.size() > 0)
		{
			PerformanceUtil.computeTeamPerformance(perform, mlist);			
		}
		return perform;
	}
	
	
	
	/**
	 * 计算球队战绩：按照胜、平、负、进球、失球、净胜球、胜率等指标进行
	 * 
	 * @param perform 球队战绩
	 * @param matchs 最近的比赛
	 */
	public static void computeTeamPerformance(PerformItem perform, List<Match> matchs)
	{
		for (Match match : matchs)
		{
			addMatchToPerformance(perform, match);
		}
	}
	
	/**
	 * 添加比赛到战绩表中
	 * 
	 * @param perform 战绩表
	 * @param match 比赛
	 */
	protected static void addMatchToPerformance(PerformItem perform, Match match)
	{
		ScoreItem score = match.getScoreResult();
		if(score.getResult() < 0)
		{
			return;
		}
		
		if(perform.isTeam(match.getHomeid()))
		{
			perform.addGoal(score.getWingoal());
			perform.addLoseGoal(score.getLosegoal());

			switch (score.getResult()) {
			case 3:
				perform.incWinGame();
				perform.addScore(3);
				break;
			case 1:
				perform.incDrawGame();
				perform.addScore(1);
				break;
			case 0:
				perform.incLoseGame();
				break;
			default:
				break;
			}
		}
		else if(perform.isTeam(match.getClientid()))
		{
			perform.addGoal(score.getLosegoal());
			perform.addLoseGoal(score.getWingoal());
			switch (score.getResult())
			{
			case 3:
				perform.incLoseGame();
				break;
			case 1:
				perform.incDrawGame();
				perform.addScore(1);
				break;
			case 0:
				perform.incWinGame();
				perform.addScore(3);
				break;
			default:
				break;
			}
		}
	}
}

