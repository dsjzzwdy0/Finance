package com.loris.soccer.analysis.predict;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.context.LorisContext;
import com.loris.base.data.Region;
import com.loris.soccer.analysis.element.CorpStatElement;
import com.loris.soccer.bean.item.ScoreItem;
import com.loris.soccer.bean.model.OpList;
import com.loris.soccer.bean.model.OpList.OpListType;
import com.loris.soccer.bean.stat.CorpMatchResult;
import com.loris.soccer.bean.stat.CorpMatchStatResult;
import com.loris.soccer.bean.stat.CorpStatItem;
import com.loris.soccer.bean.stat.MatchCorpProb;
import com.loris.soccer.bean.table.Corporate;
import com.loris.soccer.bean.table.Match;
import com.loris.soccer.bean.table.Op;
import com.loris.soccer.repository.SoccerManager;

/**
 * 博彩公司数据统计类
 *
 * @author jiean
 *
 */
public class CorporateStat
{
	private static Logger logger = Logger.getLogger(CorporateStat.class);

	/** 数据管理器 */
	static SoccerManager soccerManager;

	static List<Region<Float>> regions = new ArrayList<>();

	static
	{
		addRegion(0.0f, 30.0f);
		addRegion(1.0f, 1.50f);
		addRegion(1.50f, 1.75f);
		addRegion(1.75f, 2.00f);
		addRegion(2.00f, 2.25f);
		addRegion(2.25f, 2.50f);
		addRegion(2.50f, 2.75f);
		addRegion(2.75f, 3.00f);
		addRegion(3.00f, 3.50f);
		addRegion(3.50f, 30.0f);
	}

	static class CorpStatList extends ArrayList<CorpStatElement>
	{
		/***/
		private static final long serialVersionUID = 1L;

		/**
		 * 创建统计值
		 * 
		 * @param corp
		 * @return
		 */
		public CorpStatElement getCorporateStat(Corporate corp)
		{
			for (CorpStatElement stat : this)
			{
				if (stat.getCorp().getGid().equals(corp.getGid()))
				{
					return stat;
				}
			}
			return null;
		}

		/**
		 * 如果存在，则直接返回数据统计值；如果不存在，则创建数据统计值之后返回
		 * 
		 * @param corp
		 * @param regions
		 * @return
		 */
		public CorpStatElement getOrCreateCorpStat(Corporate corp)
		{
			CorpStatElement stat = getCorporateStat(corp);
			if (stat == null)
			{
				stat = new CorpStatElement(corp, regions);
				// stat.createCorpStatElements(regions);
				this.add(stat);
			}
			return stat;
		}
	}

	/**
	 * 设置管理器
	 * 
	 * @param manager
	 */
	public static void initialize(LorisContext context)
	{
		soccerManager = context.getBean(SoccerManager.class);
	}

	/**
	 * 设置管理器
	 * 
	 * @param manager
	 */
	public static void initialize(SoccerManager manager)
	{
		soccerManager = manager;
	}

	/**
	 * 计算平均值
	 * 
	 * @param matches
	 * @param corpStatList
	 * @param type         平均值：0, 方差值：1
	 * @return
	 */
	protected static List<Match> computeStat(List<Match> matches, CorpStatList corpStatList, int type)
	{
		List<Match> existOpMatches = new ArrayList<>();
		int i = 1;
		for (Match match : matches)
		{
			logger.info("Compute " + (i++) + " of " + matches.size() + " Match: " + match);
			ScoreItem item = match.getScoreResult();
			if (item.getResult() < 0)
			{
				logger.info("");
			}

			List<Op> ops = soccerManager.getOpList(match.getMid(), true);
			if (ops == null || ops.isEmpty())
			{
				logger.info("There are no op record of '" + match.getMid() + " in database, next.");
				continue;
			}

			OpList list = new OpList(OpList.OpListType.GidUnique, ops);
			Op avgOp = list.getAvgOp();
			if (avgOp == null)
			{
				logger.info("There are no average op record of '" + match.getMid() + " in database, next.");
				continue;
			}

			existOpMatches.add(match);
			for (Op op : list)
			{
				// 平均欧赔值
				if ("0".equals(op.getGid()))
				{
					continue;
				}
				CorpStatElement stat = null;
				if (type == 0)
					stat = corpStatList.getOrCreateCorpStat(op.getCorporate());
				else
					stat = corpStatList.getCorporateStat(op.getCorporate());

				if (stat == null)
				{
					continue;
				}

				if (type == 0)
				{
					stat.addMeanValue(item, op, avgOp);
				} else
				{
					stat.addVarValue(item, op, avgOp);
				}
			}
		}
		return existOpMatches;
	}

	/**
	 * 计算统计值
	 * 
	 * @param start
	 * @param end
	 */
	public static void computeStat(String start, String end) throws IOException
	{
		CorpStatList corpStatList = new CorpStatList();
		List<Match> matches = soccerManager.getMatches(start, end);
		logger.info("Total match is : " + matches.size());

		List<Match> existOpMatches = new ArrayList<>();

		// 首先计算平均值
		existOpMatches = computeStat(matches, corpStatList, 0);

		// 计算平均值
		for (CorpStatElement corpStat : corpStatList)
		{
			corpStat.computeMean();
		}

		if (!existOpMatches.isEmpty())
		{
			computeStat(existOpMatches, corpStatList, 1);
		}

		// 计算数据的方差值
		for (CorpStatElement corpStat : corpStatList)
		{
			corpStat.computeStdErr();
		}

		// 保存到数据库
		int i = 1;
		for (CorpStatElement corpStat : corpStatList)
		{
			List<CorpStatItem> items = corpStat.getAllCorpStatItems();

			logger.info(i++ + ": " + items);
			for (CorpStatItem item : items)
			{
				// writer.write(i++ + ": " + item + "\r\n");
				soccerManager.addOrUpdateCorpStatItem(item);
			}
		}
	}

	/**
	 * 计算某一场比赛的数据
	 * 
	 * @param gid
	 * @param gname
	 * @param start
	 * @param end
	 */
	public static void computeCorpStat(String gid, String start, String end)
	{
		List<Match> matches = soccerManager.getMatches(start, end);
		List<CorpStatItem> items = soccerManager.getCorpStatItems(gid);
		String avgGid = "0";

		List<String> gids = new ArrayList<>();
		gids.add(avgGid);
		gids.add(gid);

		int i = 1;
		int size = matches.size();
		for (Match match : matches)
		{
			i++;
			ScoreItem score = match.getScoreResult();
			if (score.getResult() < 0)
			{
				continue;
			}
			
			OpList list = getOp(match.getMid(), gids);
			Op avgOp = list.getOpByGid(avgGid);
			Op op = list.getOpByGid(gid);

			if (avgOp == null || op == null)
			{
				//logger.info("The match " + match.getMid() + " has no op value.");
				continue;
			}
			
			logger.info(i + " of [" + size + "] " + match);
			logger.info("平均欧赔: " + avgOp);
			logger.info("欧赔: " + op);
			float odds = op.getFirstwinodds();
			for (CorpStatItem item : items)
			{
				if (item.getOddsmin() <= odds && item.getOddsmax() >= odds)
				{
					float fwinDiff;
					float fdrawDiff;
					float floseDiff;
					float winDiff;
					float drawDiff;
					float loseDiff;

					fwinDiff = op.getFirstwinodds() - avgOp.getFirstwinodds();
					fdrawDiff = op.getFirstdrawodds() - avgOp.getFirstdrawodds();
					floseDiff = op.getFirstloseodds() - avgOp.getFirstloseodds();

					winDiff = op.getWinodds() - avgOp.getWinodds();
					drawDiff = op.getDrawodds() - avgOp.getDrawodds();
					loseDiff = op.getLoseodds() - avgOp.getLoseodds();

					String info = fwinDiff + ", " + fdrawDiff + ", " + floseDiff + ", " + winDiff + ", " + drawDiff
							+ ", " + loseDiff;
					logger.info(item);
					logger.info(info);
				}
			}
		}

	}

	/**
	 * 
	 * @param mid
	 * @param gids
	 * @return
	 */
	protected static OpList getOp(String mid, List<String> gids)
	{
		List<Op> ops = soccerManager.getOddsOp(mid, gids);
		return new OpList(OpListType.GidUnique, ops);
	}

	protected static List<Match> computeCorpStatElement(List<Match> matchs, CorpStatElement element, int type)
	{
		List<Match> existOpMatches = new ArrayList<>();
		return existOpMatches;
	}

	/**
	 * 数据未知
	 * 
	 * @param min
	 * @param max
	 */
	public static void addRegion(float min, float max)
	{
		regions.add(new Region<>(min, max));
	}

	/**
	 * 计算博彩公司的正确率
	 * 
	 * @param matchMaps
	 */
	public static void computeCorpAccuracy(List<Match> matchs)
	{
		CorpMatchStatResult corpMatchStatResult = new CorpMatchStatResult(regions);

		int i = 1;
		int size = matchs.size();
		for (Match match : matchs)
		{
			i++;
			ScoreItem score = match.getScoreResult();
			if (score.getResult() < 0)
			{
				continue;
			}
			logger.info(i + " of [" + size + "]: " + match);
			List<MatchCorpProb> probs = soccerManager.getMatchCorpProbsByMid(match.getMid());
			List<Op> ops = soccerManager.getOddsOp(match.getMid());
			OpList opList = new OpList(OpListType.GidUnique, ops);

			for (MatchCorpProb prob : probs)
			{
				Op op = opList.getOpByGid(prob.getGid());
				if (op == null)
				{
					continue;
				}
				List<CorpMatchResult> results = corpMatchStatResult.getCorpMatchResult(prob.getGid(), prob.getName());
				if (results.isEmpty())
				{
					continue;
				}

				for (CorpMatchResult r : results)
				{
					if (r.contains(op.getFirstwinodds()))
					{
						// 处理数据统计工具
						addCorpMatchResult(r, prob, score);
					}
				}
			}
		}

		i = 1;
		for (CorpMatchResult corpMatchResult : corpMatchStatResult)
		{
			logger.info(i++ + ": " + corpMatchResult);
			soccerManager.addCorpMatchResult(corpMatchResult);
		}
	}

	/**
	 * 
	 * @param result
	 * @param prob
	 * @param score
	 */
	protected static void addCorpMatchResult(CorpMatchResult result, MatchCorpProb prob, ScoreItem score)
	{
		int r = score.getResult();
		int p = prob.getResult();
		result.addNum();
		switch (r)
		{
		case 3:
			result.addWinNum();
			if (p == 3)
			{
				result.addWinpredictwin();
			} else if (p == 1)
			{
				result.addWinpredictdraw();
			} else
			{
				result.addWinpredictlose();
			}
			break;
		case 1:
			result.addDrawnum();
			if (p == 3)
			{
				result.addDrawpredictwin();
			} else if (p == 1)
			{
				result.addDrawpredictdraw();
			} else
			{
				result.addDrawpredictlose();
			}
			break;
		case 0:
			result.addLosenum();
			if (p == 3)
			{
				result.addLosepredictwin();
			} else if (p == 1)
			{
				result.addLosepredictdraw();
			} else
			{
				result.addLosepredictlose();
			}
			break;
		default:
			break;
		}
	}
}
