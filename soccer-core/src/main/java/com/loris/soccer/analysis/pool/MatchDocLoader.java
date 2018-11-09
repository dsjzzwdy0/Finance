package com.loris.soccer.analysis.pool;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.data.PairValue;
import com.loris.base.util.DateUtil;
import com.loris.base.util.NumberUtil;
import com.loris.soccer.analysis.data.MatchData;
import com.loris.soccer.analysis.data.MatchDoc;
import com.loris.soccer.analysis.data.MatchOdds;
import com.loris.soccer.analysis.data.MatchRank;
import com.loris.soccer.analysis.util.IssueMatchUtil;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.data.table.league.Match;
import com.loris.soccer.bean.data.table.lottery.BdMatch;
import com.loris.soccer.bean.data.table.lottery.Corporate;
import com.loris.soccer.bean.data.table.lottery.JcMatch;
import com.loris.soccer.bean.data.table.lottery.UserCorporate;
import com.loris.soccer.bean.data.table.odds.Op;
import com.loris.soccer.bean.data.table.odds.Yp;
import com.loris.soccer.bean.data.view.MatchInfo;
import com.loris.soccer.bean.data.view.RankInfo;
import com.loris.soccer.bean.element.MatchCorpOddsElement;
import com.loris.soccer.bean.element.MatchSynthElement;
import com.loris.soccer.bean.item.IssueMatch;
import com.loris.soccer.bean.model.IssueMatchMapping;
import com.loris.soccer.bean.okooo.OkoooBdMatch;
import com.loris.soccer.bean.okooo.OkoooYp;
import com.loris.soccer.bean.setting.CorpSetting;
import com.loris.soccer.repository.SoccerManager;

/**
 * 足球比赛预测工厂，在这里，预测工厂将解决以下几个方面的问题：<br>
 * 1、获得足彩公司的配置数据，将采用哪些博彩公司的数据；<br>
 * 2、获得足球比赛的足彩公司的亚赔、欧赔数据；<br>
 * 
 * @author dsj
 *
 */
public class MatchDocLoader
{
	private static Logger logger = Logger.getLogger(MatchDocLoader.class);

	/** 用户默认定义的赔率公司模式 */
	public static final String CORP_ODDS_DEFAULT = "user_default";

	/** 查询历史数据时，以该值回溯的年份，如设置为2，则以两年前的1月1日为起始点 */
	public static int yearToInterval = 1;

	/** 博彩公司列表 */
	protected static List<UserCorporate> corporates;

	/**
	 * 获得足球数据管理类
	 * 
	 * @return 数据管理类
	 */
	private static SoccerManager getSoccerManager()
	{
		SoccerManager manager = SoccerManager.getInstance();
		if (manager == null)
		{
			throw new IllegalArgumentException("Error, please initialize the SoccerManager first.");
		}
		return manager;
	}

	/**
	 * 获得赔率公司配置信息
	 * 
	 * @param corpType
	 *            赔率公司类型
	 * @return 赔率公司配置
	 */
	public static CorpSetting getCorpConfigure(String corpType)
	{
		switch (corpType) {
		case CORP_ODDS_DEFAULT:
			return getDefaultCorpSetting();
		default:
			break;
		}
		return null;
	}

	/**
	 * 获得默认的赔率公司配置数据
	 * 
	 * @return 赔率公司配置
	 */
	public static CorpSetting getDefaultCorpSetting()
	{
		SoccerManager manager = getSoccerManager();
		return manager.getDefaultCorpSetting();
	}

	/**
	 * 获得比赛数据元素列表
	 * 
	 * @param dataVector
	 *            数据容器
	 * @return 数据元素列表
	 */
	public static List<MatchSynthElement> getDefaultMatchSynthElement(MatchDoc dataVector)
	{
		List<MatchSynthElement> items = new ArrayList<>();
		for (IssueMatch match : dataVector.getMatches())
		{
			MatchData data = dataVector.getMatchData(match.getMid());
			MatchSynthElement item = data.createIssueMatchSynthElement();
			items.add(item);
		}
		return items;
	}

	/**
	 * 获得关联比赛的数据,包含欧赔数据、亚盘数据，相关联比赛的同联赛、同赛季轮次的比赛数据
	 * 
	 * @param sid
	 * @param odds
	 * @param gid
	 * @param threshold
	 * @param bwin
	 * @param first
	 * @return
	 */
	public static List<MatchOdds> getRelationMatchOdds(String sid, float odds, String gid, float threshold,
			boolean bwin, boolean first)
	{
		return null;
	}

	/**
	 * 获得比赛数据元素列表
	 * 
	 * @param dataVector
	 *            数据容器
	 * @return 数据元素列表
	 */
	public static List<MatchSynthElement> getJcMatchSynthElement(MatchDoc dataVector, String issue)
	{
		SoccerManager manager = getSoccerManager();
		List<JcMatch> matchs = manager.getJcMatches(issue);
		List<MatchSynthElement> items = new ArrayList<>();

		for (JcMatch match : matchs)
		{
			MatchData data = dataVector.getMatchData(match.getMid());
			if (data == null)
			{
				continue;
			}

			MatchSynthElement item = data.createIssueMatchSynthElement();
			item.setOrdinary(match.getOrdinary());
			items.add(item);
		}
		return items;
	}

	/**
	 * 获得某一场比赛欧赔数据与亚盘数据的比较值
	 * 
	 * @param mid
	 *            比赛编号
	 * @param opgid
	 *            欧赔公司编号
	 * @param opsource
	 *            欧赔公司来源
	 * @param ypgid
	 *            亚盘公司编号
	 * @param ypsource
	 *            亚盘公司来源
	 * @return 数据详细列表
	 */
	public static MatchCorpOddsElement getMatchCorpOddsElement(String mid, String opgid, String opsource, String ypgid,
			String ypsource)
	{
		SoccerManager soccerManager = getSoccerManager();
		if (soccerManager == null)
		{
			return null;
		}
		IssueMatch match = soccerManager.getJcMatch(mid);

		if (match == null)
		{
			return null;
		}

		if (StringUtils.isEmpty(opsource))
		{
			opsource = SoccerConstants.DATA_SOURCE_ZGZCW;
		}
		if (StringUtils.isEmpty(ypsource))
		{
			ypsource = SoccerConstants.DATA_SOURCE_ZGZCW;
		}

		MatchCorpOddsElement element = new MatchCorpOddsElement(match);
		Corporate opcorp = getCorporate(opgid, SoccerConstants.ODDS_TYPE_OP, opsource);
		Corporate ypcorp = getCorporate(ypgid, SoccerConstants.ODDS_TYPE_YP, ypsource);

		getMatchCorpOdds(element, opcorp, ypcorp);
		return element;
	}

	/**
	 * 加载某一个场比赛的欧赔数据与亚盘数据，用于对比分析
	 * 
	 * @param element
	 *            比赛信息
	 * @param opcorp
	 *            欧赔公司
	 * @param ypcorp
	 *            亚盘公司
	 */
	public static void getMatchCorpOdds(MatchCorpOddsElement element, Corporate opcorp, Corporate ypcorp)
	{
		SoccerManager soccerManager = getSoccerManager();
		if (soccerManager == null)
		{
			return;
		}

		String mid = element.getMid();
		if (opcorp != null)
		{
			element.setOpCorp(opcorp);
			String opsource = opcorp.getSource();
			if (opsource.equalsIgnoreCase(SoccerConstants.DATA_SOURCE_OKOOO))
			{

			}
			else
			{
				List<Op> ops = soccerManager.getOpList(mid, opcorp.getGid());
				element.addOpList(ops);
			}

		}
		if (ypcorp != null)
		{
			element.setYpCorp(ypcorp);
			String ypsource = ypcorp.getSource();
			if (ypsource.equalsIgnoreCase(SoccerConstants.DATA_SOURCE_OKOOO))
			{
				List<OkoooYp> yps = soccerManager.getOkoooYpList(mid, ypcorp.getGid());
				for (OkoooYp okoooYp : yps)
				{
					okoooYp.setFirstwinodds(okoooYp.getFirstwinodds() - 1.0f);
					okoooYp.setFirstloseodds(okoooYp.getFirstloseodds() - 1.0f);
					okoooYp.setWinodds(okoooYp.getWinodds() - 1.0f);
					okoooYp.setLoseodds(okoooYp.getLoseodds() - 1.0f);
				}
				element.addYpList(yps);
			}
			else
			{
				List<Yp> yps = soccerManager.getYpList(mid, ypcorp.getGid());
				element.addYpList(yps);
			}
		}
	}

	/**
	 * 
	 * @param matchOdds
	 * @param mids
	 * @param gids
	 * @param type
	 */
	public static void loadMatchOdds(List<MatchOdds> matchOdds, List<String> mids, List<String> gids, String type)
	{
		if (mids == null || mids.size() == 0 || gids == null || gids.size() == 0)
		{
			return;
		}
		SoccerManager manager = SoccerManager.getInstance();

		if (SoccerConstants.ODDS_TYPE_OP.equalsIgnoreCase(type))
		{
			List<Op> ops = manager.getOddsOp(mids, gids, true);
			for (Op op : ops)
			{
				for (MatchOdds m : matchOdds)
				{
					if (op.getMid().equals(m.getMid()))
					{
						m.addOp(op);
						continue;
					}
				}
			}
		}
		else if (SoccerConstants.ODDS_TYPE_YP.equalsIgnoreCase(type))
		{
			List<Yp> yps = manager.getOddsYp(mids, gids, true);
			for (Yp yp : yps)
			{
				for (MatchOdds m : matchOdds)
				{
					if (yp.getMid().equals(m.getMid()))
					{
						m.addYp(yp);
						continue;
					}
				}
			}
		}
	}

	/**
	 * 加载数据记录
	 * 
	 * @param issue
	 * @param type
	 * @param gids
	 * @param oddsType
	 * @return
	 */
	public static List<MatchOdds> loadMatchOdds(String issue, String type, List<String> gids, String oddsType)
	{
		SoccerManager manager = SoccerManager.getInstance();
		List<? extends IssueMatch> matchs = null;
		if (SoccerConstants.LOTTERY_JC.equalsIgnoreCase(type))
		{
			matchs = manager.getJcMatches(issue);
		}
		else
		{
			matchs = manager.getBdMatches(issue);
		}

		if (matchs == null || matchs.size() == 0)
		{
			return null;
		}

		List<String> mids = new ArrayList<>();

		List<MatchOdds> list = new ArrayList<>();
		for (IssueMatch match : matchs)
		{
			MatchOdds m = new MatchOdds(match);
			mids.add(match.getMid());
			list.add(m);
		}
		loadMatchOdds(list, mids, gids, oddsType);
		return list;
	}

	/**
	 * 加载联赛最新的排名数据
	 * 
	 * @param lid
	 * @param season
	 * @param round
	 * @return
	 */
	public static List<MatchRank> loadRoundMatchRanks(String lid, String season, String round)
	{
		SoccerManager manager = SoccerManager.getInstance();
		List<MatchInfo> matchs = manager.getMatchInfos(lid, season, round);
		if (matchs == null || matchs.size() == 0)
		{
			return null;
		}

		List<RankInfo> rankInfos = manager.getLatestRanks(lid, SoccerConstants.RANK_TOTAL);
		List<MatchRank> matchRanks = new ArrayList<>();

		for (MatchInfo matchInfo : matchs)
		{
			MatchRank rank = new MatchRank(matchInfo);
			for (RankInfo r : rankInfos)
			{
				if (r.getTid().equals(rank.getHomeid()))
				{
					rank.setHomeRank(r);
				}
				if (r.getTid().equals(rank.getClientid()))
				{
					rank.setClientRank(r);
				}
			}
			matchRanks.add(rank);
		}

		return matchRanks;
	}

	/**
	 * 加载联赛某一轮次的比赛数据
	 * 
	 * @param lid
	 * @param season
	 * @param round
	 * @param setting
	 * @return
	 */
	public static List<MatchOdds> loadRoundMatchOdds(String lid, String season, String round, CorpSetting setting)
	{
		SoccerManager manager = SoccerManager.getInstance();
		List<MatchInfo> matchs = manager.getMatchInfos(lid, season, round);
		if (matchs == null || matchs.size() == 0)
		{
			return null;
		}

		List<MatchOdds> list = new ArrayList<>();
		List<String> mids = new ArrayList<>();

		int i = 1;
		for (MatchInfo matchInfo : matchs)
		{
			mids.add(matchInfo.getMid());
			MatchOdds m = new MatchOdds(matchInfo);
			m.setOrdinary(Integer.toString(i++));
			list.add(m);
		}

		// 加载欧赔与亚盘数据
		List<String> gids = setting.getCorporateIds(SoccerConstants.DATA_SOURCE_ZGZCW, SoccerConstants.ODDS_TYPE_OP);
		loadMatchOdds(list, mids, gids, SoccerConstants.ODDS_TYPE_OP);
		gids = setting.getCorporateIds(SoccerConstants.DATA_SOURCE_ZGZCW, SoccerConstants.ODDS_TYPE_YP);
		loadMatchOdds(list, mids, gids, SoccerConstants.ODDS_TYPE_YP);

		return list;
	}

	/**
	 * 加载数据记录
	 * 
	 * @param issue
	 * @param type
	 * @return
	 */
	public static List<MatchOdds> loadMatchOdds(String issue, String type, List<String> lids, CorpSetting setting)
	{
		SoccerManager manager = SoccerManager.getInstance();
		List<? extends IssueMatch> matchs = null;
		if (SoccerConstants.LOTTERY_JC.equalsIgnoreCase(type))
		{
			matchs = manager.getJcMatches(issue);
		}
		else
		{
			matchs = manager.getBdMatches(issue);
		}

		if (matchs == null || matchs.size() == 0)
		{
			return null;
		}

		List<String> mids = new ArrayList<>();

		List<MatchOdds> list = new ArrayList<>();
		for (IssueMatch match : matchs)
		{
			// 过滤联赛类型
			if (lids != null && lids.size() > 0)
			{
				boolean inLeagues = false;
				for (String lid : lids)
				{
					if (lid.equals(match.getLid()))
					{
						inLeagues = true;
						break;
					}
				}
				if (!inLeagues)
				{
					continue;
				}
			}
			MatchOdds m = new MatchOdds(match);
			mids.add(match.getMid());
			list.add(m);
		}

		// 加载欧赔与亚盘数据
		List<String> gids = setting.getCorporateIds(SoccerConstants.DATA_SOURCE_ZGZCW, SoccerConstants.ODDS_TYPE_OP);
		loadMatchOdds(list, mids, gids, SoccerConstants.ODDS_TYPE_OP);
		gids = setting.getCorporateIds(SoccerConstants.DATA_SOURCE_ZGZCW, SoccerConstants.ODDS_TYPE_YP);
		loadMatchOdds(list, mids, gids, SoccerConstants.ODDS_TYPE_YP);

		return list;
	}

	/**
	 * 加载所有的比赛数据
	 * @param mids
	 * @param setting
	 * @return
	 */
	public static List<MatchOdds> loadMatchesOdds(List<String> mids, CorpSetting setting)
	{
		SoccerManager manager = SoccerManager.getInstance();
		List<BdMatch> matchs = manager.getBdMatches(mids);
		List<MatchOdds> list = new ArrayList<>();
		for (BdMatch match : matchs)
		{
			MatchOdds m = new MatchOdds(match);
			list.add(m);
		}
		// 加载欧赔与亚盘数据
		List<String> gids = setting.getCorporateIds(SoccerConstants.DATA_SOURCE_ZGZCW, SoccerConstants.ODDS_TYPE_OP);
		loadMatchOdds(list, mids, gids, SoccerConstants.ODDS_TYPE_OP);
		gids = setting.getCorporateIds(SoccerConstants.DATA_SOURCE_ZGZCW, SoccerConstants.ODDS_TYPE_YP);
		loadMatchOdds(list, mids, gids, SoccerConstants.ODDS_TYPE_YP);
		return list;
	}

	/**
	 * 获取竞彩期号的比赛数据
	 * 
	 * @param issue
	 *            期号
	 * @return 比赛数据
	 */
	public static MatchDoc getMatchDoc(String issue, CorpSetting configure)
	{
		SoccerManager manager = getSoccerManager();
		return loadIssueMatchDataFromDatabase(manager, issue, configure);
	}

	/**
	 * 从数据库加载竞彩比赛相关数据
	 * 
	 * @param soccerManager
	 *            数据管理类
	 * @param issue
	 *            比赛期号
	 * @param configure
	 *            博彩公司配置
	 * @return 是否加载成功的标志
	 */
	protected static MatchDoc loadIssueMatchDataFromDatabase(SoccerManager soccerManager, String issue,
			CorpSetting configure)
	{
		if (StringUtils.isEmpty(issue))
		{
			logger.info("The issue value should not be null, please set the issue.");
			return null;
		}
		List<? extends IssueMatch> matches = soccerManager.getBdMatches(issue);
		if (matches == null || matches.size() <= 0)
		{
			logger.info(
					"Error, load JcMatch '" + issue
							+ "' is empty, check the database and ensure the JcMatch data exist in database."
			);
			return null;
		}

		// 用于计时
		long st, en;

		// 初始化比赛数据
		MatchDoc matchDoc = new MatchDoc(matches);
		// matchDoc.setCorpSetting(configure);

		st = System.currentTimeMillis();
		// 加载历史比赛数据
		loadHistoryMatchFromDatabase(soccerManager, matchDoc);

		en = System.currentTimeMillis();
		logger.info("Total spend " + (en - st) + " ms to load history data.");
		// st = System.currentTimeMillis();

		st = System.currentTimeMillis();
		// 加载赔率数据
		loadOddsDataFromDatabase(soccerManager, issue, matchDoc, configure);
		en = System.currentTimeMillis();
		logger.info("Total spend " + (en - st) + " ms to load odds data.");

		return matchDoc;
	}

	/**
	 * 加载与之关联的历史比赛数据
	 * 
	 * @param soccerManager
	 *            数据仓库
	 * @param dataVector
	 *            数据数组
	 */
	protected static void loadHistoryMatchFromDatabase(SoccerManager soccerManager, MatchDoc dataVector)
	{
		// 加载历史比赛数据
		List<String> tids = dataVector.getTeamIds();
		Date d1 = new Date();
		String lastDate = DateUtil.getTimeString(d1);
		Date d2 = DateUtil.getFirstDateBefore(d1, yearToInterval);
		String firstDate = DateUtil.getTimeString(d2);
		List<Match> baseMatchs = soccerManager.getMatches(tids, firstDate, lastDate);
		for (Match baseMatch : baseMatchs)
		{
			dataVector.addHistoryMatch(baseMatch);
		}
	}

	/**
	 * 从数据库中加载赔率数据
	 * 
	 * @param soccerManager
	 *            数据管理器
	 * @param dataVector
	 *            比赛数组
	 * @param configure
	 *            公司配置
	 */
	protected static void loadOddsDataFromDatabase(SoccerManager soccerManager, String issue, MatchDoc dataVector,
			CorpSetting configure)
	{

		List<String> sources = configure.getSources();

		for (String source : sources)
		{
			// 处理中国足彩网定义的数据
			if (SoccerConstants.DATA_SOURCE_ZGZCW.equalsIgnoreCase(source))
			{
				List<String> mids = dataVector.getMatchIds();
				// 欧赔数据
				List<String> gids = configure.getCorporateIds(source, SoccerConstants.ODDS_TYPE_OP);
				List<Op> ops = soccerManager.getOddsOp(mids, gids, true);
				dataVector.addOpList(ops);

				// 亚盘数据
				gids = configure.getCorporateIds(source, SoccerConstants.ODDS_TYPE_YP);
				List<Yp> yps = soccerManager.getOddsYp(mids, gids, true);
				dataVector.addYpList(yps);
			}
			else if (SoccerConstants.DATA_SOURCE_OKOOO.equalsIgnoreCase(source))
			{
				IssueMatchMapping mapping = getMatchMapping(
						issue, SoccerConstants.DATA_SOURCE_OKOOO, SoccerConstants.DATA_SOURCE_ZGZCW,
						dataVector.getMatches()
				);
				dataVector.addIssueMatchMapping(SoccerConstants.DATA_SOURCE_OKOOO, mapping);

				List<String> mids = mapping.getDestMatchIds();

				// 澳客网的数据目前仅需要使用亚盘数据
				List<String> gids = configure.getCorporateIds(source, SoccerConstants.ODDS_TYPE_YP);
				List<OkoooYp> yps = soccerManager.getOkoooYpList(mids, gids, true);

				dataVector.addYpList(SoccerConstants.DATA_SOURCE_OKOOO, yps);
			}
		}
	}

	/**
	 * 获得数据映射表
	 * 
	 * @param dest
	 *            目标数据源
	 * @param issue
	 *            比赛期号
	 * @param source
	 * @param sourceMatches
	 * @return
	 */
	public static IssueMatchMapping getMatchMapping(String issue, String dest, String source,
			List<IssueMatch> sourceMatches)
	{
		SoccerManager manager = getSoccerManager();

		if (SoccerConstants.DATA_SOURCE_OKOOO.equalsIgnoreCase(dest))
		{
			List<OkoooBdMatch> okoooJcMatchs = manager.getOkoooBdMatches(issue);
			IssueMatchMapping mapping = mappingJcMatchIds(
					issue, source, sourceMatches, SoccerConstants.DATA_SOURCE_OKOOO, okoooJcMatchs
			);
			return mapping;
		}
		return null;
	}

	/**
	 * 从两个不同的数据源来的比赛，对ID值进行映射匹配
	 * 
	 * @param issue
	 *            比赛期号
	 * @param source
	 *            数据来源
	 * @param matchs
	 *            来源数据列表
	 * @param dest
	 *            目标
	 * @param matchs2
	 *            目标数据列表
	 * @return 映射表
	 */
	public static IssueMatchMapping mappingJcMatchIds(String issue, String source, List<? extends IssueMatch> matchs,
			String dest, List<? extends IssueMatch> matchs2)
	{
		List<IssueMatch> sourceMatches = shuffleMatch(issue, matchs);
		List<IssueMatch> destMatches = shuffleMatch(issue, matchs2);
		IssueMatchMapping pairs = new IssueMatchMapping(source, dest);

		HashSet<Integer> columns = new HashSet<>();

		int k = destMatches.size();
		for (IssueMatch sourceMatch : sourceMatches)
		{
			for (int j = 0; j < k; j++)
			{
				if (columns.contains(j))
				{
					continue;
				}

				IssueMatch destMatch = destMatches.get(j);
				if (sourceMatch.getOrdinary().equals(destMatch.getOrdinary()))
				{
					columns.add(j);
					pairs.addMapping(sourceMatch, destMatch);
				}
			}
		}
		return pairs;
	}

	/**
	 * 从两个不同的数据源来的比赛，对ID值进行映射匹配
	 * 
	 * @param issue
	 *            比赛期号
	 * @param source
	 *            数据来源
	 * @param matchs
	 *            来源的比赛数据列表
	 * @param dest
	 *            数据来源
	 * @param okoooJcMatchs
	 *            数据目标的比赛列表
	 * @return 映射表
	 */
	public static IssueMatchMapping mappingMatchIdsBySimilarity(String issue, String source, List<IssueMatch> matchs,
			String dest, List<? extends IssueMatch> okoooJcMatchs)
	{
		List<IssueMatch> sourceMatches = shuffleMatch(issue, matchs);
		List<IssueMatch> destMatches = shuffleMatch(issue, okoooJcMatchs);

		int m = sourceMatches.size();
		int k = destMatches.size();
		// boolean matched = false;
		double[][] similarity = new double[m][k];

		// 映射值数据
		// List<PairValue<JcMatch, JcMatch>> pairs = new ArrayList<>();
		IssueMatchMapping pairs = new IssueMatchMapping(source, dest);
		HashSet<Integer> rows = new HashSet<>();
		HashSet<Integer> columns = new HashSet<>();

		// 进行数据映射处理
		for (int i = 0; i < m; i++)
		{
			IssueMatch match = sourceMatches.get(i);
			// matched = false;

			// 每一场比赛的进行比对
			for (int j = 0; j < k; j++)
			{
				// 已经处理完成，不再进行计算
				if (columns.contains(j))
				{
					continue;
				}

				IssueMatch destMatch = destMatches.get(j);
				similarity[i][j] = IssueMatchUtil.computeSimilarity(match, destMatch);

				// 如果满足完全相等，则不需要计算其它和比赛
				if (NumberUtil.isNotLessThan(similarity[i][j], 0.90))
				{
					logger.info(getJcMatchInfo(match, destMatch) + ": " + similarity[i][j]);
					// 把已经完全匹配的其它数据置为0，这样为了统计方便
					for (int l = 0; l < j; l++)
					{
						similarity[i][l] = 0.0;
					}

					addMatchedData(match, i, destMatch, j, pairs, rows, columns);
					break;
				}
			}
		}

		// 变量定义
		double maxSimilarity;
		int maxSimIndex = 0, maxSimIndex2;

		// 对数据进行匹配搜索
		for (int i = 0; i < m;)
		{
			if (rows.contains(i))
			{
				i++;
				continue;
			}

			// 该行的最大的相似度
			maxSimilarity = -100.0;

			// 每一场比赛的进行比对
			for (int j = 0; j < k; j++)
			{
				if (columns.contains(j))
				{
					continue;
				}

				// 选择最大的相似度的数据
				if (maxSimilarity < similarity[i][j])
				{
					maxSimilarity = similarity[i][j];
					maxSimIndex = j;
				}
			}

			if (maxSimilarity < 0.0001)
			{
				i++;
				continue;
			}

			maxSimIndex2 = getMaxColomnIndex(similarity, m, maxSimIndex, rows);
			if (maxSimIndex2 == i) // 当前列、行所对应的最大的值就是，则进行匹配
			{
				IssueMatch match = sourceMatches.get(i);
				IssueMatch destMatch = destMatches.get(maxSimIndex);
				addMatchedData(match, i, destMatch, maxSimIndex, pairs, rows, columns);
				i++;
			}

			// logger.info("Current index = " + i);
		}
		return pairs;
	}

	/**
	 * 
	 * @param similarity
	 * @param rowNum
	 * @param columnIndex
	 * @param rows
	 * @return
	 */
	protected static int getMaxColomnIndex(double[][] similarity, int rowNum, int columnIndex, HashSet<Integer> rows)
	{
		double maxSimilarity = -100.0;
		int maxSimIndex = 0;

		for (int i = 0; i < rowNum; i++)
		{
			if (rows.contains(i))
			{
				continue;
			}

			// 选择最大的相似度的数据
			if (maxSimilarity < similarity[i][columnIndex])
			{
				maxSimilarity = similarity[i][columnIndex];
				maxSimIndex = i;
			}
		}
		return maxSimIndex;
	}

	/**
	 * 添加到匹配的数据列表中
	 * 
	 * @param m1
	 * @param m2
	 * @param pairValues
	 * @param set
	 */
	protected static void addMatchedData(IssueMatch m1, int rowNum, IssueMatch m2, int columnNum,
			IssueMatchMapping mapping, HashSet<Integer> rows, HashSet<Integer> columns)
	{
		/*
		 * PairValue<JcMatch, JcMatch> p = new PairValue<>(m1, m2);
		 * pairValues.addPairValue(p);
		 */
		mapping.addMapping(m1, m2);

		rows.add(rowNum);
		columns.add(columnNum);
	}

	/**
	 * 清洗数据
	 * 
	 * @param issue
	 * @param matchs
	 * @return
	 */
	protected static List<IssueMatch> shuffleMatch(String issue, List<? extends IssueMatch> matchs)
	{
		List<IssueMatch> sourceMatches = new ArrayList<>();
		// 数据清洗，将去掉不符合要求的数据
		for (IssueMatch jcMatch : matchs)
		{
			if (jcMatch.checkIssue(issue))
			{
				sourceMatches.add(jcMatch);
			}
		}
		return sourceMatches;
	}

	/**
	 * 格式化信息
	 * 
	 * @param p
	 * @return
	 */
	protected static String getPairValueInfo(PairValue<JcMatch, JcMatch> p)
	{
		String info = p.getKey().getHomename() + " vs " + p.getKey().getClientname();
		info += " => " + p.getValue().getHomename() + " vs " + p.getValue().getClientname();
		return info;
	}

	/**
	 * 格式化比赛信息
	 * 
	 * @param m1
	 * @param m2
	 * @return
	 */
	protected static String getJcMatchInfo(IssueMatch m1, IssueMatch m2)
	{
		String info = m1.getHomename() + " vs " + m1.getClientname();
		info += " => " + m2.getHomename() + " vs " + m2.getClientname();
		return info;
	}

	/**
	 * 获得博彩公司，如果source为空时，则默认的为zgzcw
	 * 
	 * @param key
	 *            公司编号、公司名称等
	 * @param source
	 *            数据来源
	 * @return 博彩公司
	 */
	public static Corporate getCorporate(String key, String type, String source)
	{
		if (corporates == null)
		{
			corporates = getSoccerManager().getUserCoporates();
		}

		if (StringUtils.isEmpty(type))
		{
			type = SoccerConstants.ODDS_TYPE_OP;
		}
		if (StringUtils.isEmpty(source))
		{
			source = SoccerConstants.DATA_SOURCE_ZGZCW;
		}

		for (UserCorporate userCorporate : corporates)
		{
			if (!source.equalsIgnoreCase(userCorporate.getSource()))
			{
				continue;
			}
			if (key.equals(userCorporate.getGid()) || key.equalsIgnoreCase(userCorporate.getName()))
			{
				return userCorporate;
			}
		}
		return null;
	}

	/**
	 * 某一轮次的某一联赛的比赛数据
	 * 
	 * @param issue
	 *            期号
	 * @param lid
	 *            联赛编号
	 * @return
	 */
	public static List<MatchInfo> getMatchInfos(String issue, String lid)
	{
		SoccerManager soccerManager = getSoccerManager();
		List<BdMatch> matchs = soccerManager.getBdMatches(issue, lid);
		if (matchs == null || matchs.size() == 0)
		{
			return null;
		}
		for (BdMatch bdMatch : matchs)
		{
			logger.info(bdMatch);
		}
		MatchInfo m = soccerManager.getMatchInfo(matchs.get(0).getMid());
		logger.info(m);
		if (m == null)
		{
			return null;
		}
		return soccerManager.getMatchInfos(lid, m.getSeason(), m.getRound());
	}
}
