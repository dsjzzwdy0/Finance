package com.loris.lottery.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.analysis.graph.Graph;
import com.loris.base.bean.wrapper.PageWrapper;
import com.loris.base.bean.wrapper.Rest;
import com.loris.base.util.ArraysUtil;
import com.loris.base.util.DateUtil;
import com.loris.base.util.NumberUtil;
import com.loris.soccer.analysis.data.MatchOpVariance;
import com.loris.soccer.analysis.element.MatchCorpOddsElement;
import com.loris.soccer.analysis.element.MatchOddsElement;
import com.loris.soccer.analysis.element.MatchRankOddsElement;
import com.loris.soccer.analysis.element.MatchSynthElement;
import com.loris.soccer.analysis.element.OddsElement;
import com.loris.soccer.analysis.checker.CorpChecker;
import com.loris.soccer.analysis.data.LeagueMatchDoc;
import com.loris.soccer.analysis.data.MatchData;
import com.loris.soccer.analysis.data.MatchDoc;
import com.loris.soccer.analysis.data.MatchOdds;
import com.loris.soccer.analysis.pool.MatchDocLoader;
import com.loris.soccer.analysis.pool.MatchDocPool;
import com.loris.soccer.analysis.pool.MatchOddsPool;
import com.loris.soccer.analysis.util.IssueMatchUtil;
import com.loris.soccer.analysis.util.MatchGraph;
import com.loris.soccer.analysis.util.OddsUtil;
import com.loris.soccer.analysis.util.PossionUtil;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.item.SettingItem;
import com.loris.soccer.bean.model.OpList;
import com.loris.soccer.bean.stat.CorpStatItem;
import com.loris.soccer.bean.table.CorpSetting;
import com.loris.soccer.bean.table.CorpSettingParameter;
import com.loris.soccer.bean.table.Corporate;
import com.loris.soccer.bean.table.JcMatch;
import com.loris.soccer.bean.table.Match;
import com.loris.soccer.bean.table.Op;
import com.loris.soccer.bean.table.UserCorporate;
import com.loris.soccer.bean.table.Yp;
import com.loris.soccer.bean.view.MatchInfo;
import com.loris.soccer.bean.view.RankInfo;
import com.loris.soccer.bean.view.RoundInfo;
import com.loris.soccer.repository.OkoooSqlHelper;
import com.loris.soccer.repository.SoccerManager;
import com.loris.soccer.repository.service.MatchInfoService;
import com.loris.soccer.repository.service.RoundInfoService;
import com.loris.soccer.repository.service.UserCorporateService;

/*
import it.uniroma1.dis.wsngroup.gexf4j.core.Edge;
import it.uniroma1.dis.wsngroup.gexf4j.core.EdgeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.Gexf;
import it.uniroma1.dis.wsngroup.gexf4j.core.Graph;
import it.uniroma1.dis.wsngroup.gexf4j.core.Mode;
import it.uniroma1.dis.wsngroup.gexf4j.core.Node;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.Attribute;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeClass;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeList;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.GexfImpl;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.StaxGraphWriter;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.data.AttributeListImpl;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.viz.PositionImpl;
*/
/**
 * 足球数据服务接口
 * 
 * @author jiean
 *
 */
@Controller
@RequestMapping("/soccerdata")
public class SoccerDataController
{
	private static Logger logger = Logger.getLogger(SoccerDataController.class);
	
	/** 数据池的大小 */
	public static int POOL_SIZE = 5;

	/** 每页的默认记录数 */
	public static int DEFAULT_PAGE_SIZE = 20;

	@Autowired
	private SoccerManager soccerManager;
	
	@Autowired
	OkoooSqlHelper okoooSqlHelper;

	@Autowired
	private MatchInfoService matchInfoService;

	@Autowired
	private RoundInfoService roundInfoService;
	
	@Autowired
	private UserCorporateService userCorporateService;
	
	/** 数据池，为提高系统运行的效率，在这里存储数据 */
	//protected Map<String, MatchDoc> dataPools = new HashMap<>();
	
	protected MatchDocPool dataPools;
	
	/**
	 * Create a new instance of SoccerDataController.
	 */
	public SoccerDataController()
	{
		dataPools = MatchDocPool.getInstance();
		dataPools.setPoolSize(POOL_SIZE);
	}
	
	/**
	 * 计算比赛的欧赔赔率方差值
	 * @param issue 日期
	 * @return 数据列表
	 */
	@ResponseBody
	@RequestMapping("/getMatchesOpVar")
	public Rest getMatchesOpVariance(String issue, String type)
	{
		if(StringUtils.isEmpty(issue))
		{
			return Rest.failure("The date has not been initialized, please set the date value.");
		}
		
		List<MatchOpVariance> variances = null;
		if("jc".equalsIgnoreCase(type))
		{
			variances = MatchOddsPool.computeJcMatchsOpVariance(issue);
		}
		else
		{
			variances = MatchOddsPool.computeBdMatchsOpVariance(issue);
		}
		if(variances != null && variances.size() >= 0)
		{
			return Rest.okData(variances);
		}
		else
		{
			return Rest.failure("Error when compute the MatchOddsVariance.");
		}
	}
	
	/**
	 * 获得某一场比赛的关联比赛
	 * @param sid 欧赔配置方案
	 * @param mid 比赛编号
	 * @return 比赛的数据
	 */
	@ResponseBody
	@RequestMapping("/getRelationMatches")
	public Rest getRelationMatches(String mid, String sid, String first, String threshold)
	{
		return Rest.ok();
	}
	
	/**
	 * 获取相关比赛的信息
	 * @param mids 比赛列表
	 * @param sid 欧赔配置方案
	 * @return　比赛信息列表
	 */
	@ResponseBody
	@RequestMapping("/getRelationMatchesOdds")
	public Rest getRelationMatchesOdds(String mids, String sid)
	{
		List<String> midlist = splitString(mids, ",");
		if(midlist == null || midlist.isEmpty())
		{
			return Rest.failure("There are no matches in the list.");
		}
		
		CorpSetting setting = null;
		setting = soccerManager.getCorpSetting(sid);
		if(setting == null)
		{
			return Rest.failure("The CorpSetting '" + sid + "' is not set correctly.");
		}
		
		long st = System.currentTimeMillis();		
		List<MatchOdds> ops = MatchDocLoader.loadMatchesOdds(midlist, setting);
		long en = System.currentTimeMillis();
		if(ops == null)
		{
			String info = "There are no match ops in the database. ";
			logger.info(info);
			return Rest.failure(info);
		}
		
		List<MatchOddsElement> elements = new ArrayList<>();
		for (MatchOdds op : ops)
		{
			elements.add(new MatchOddsElement(op));
		}
		
		logger.info("Total spend " + (en - st) + " ms to load " + ops.size() + " match ops.");
		
		Map<String, Object> map = new HashMap<>();
		map.put("setting", setting);
		map.put("matches", elements);
		return Rest.okData(map);
	}
	
	/**
	 * 计算比赛的欧赔赔率方差值
	 * @param issue 日期
	 * @return 数据列表
	 */
	@ResponseBody
	@RequestMapping("/getMatchOpVar")
	public Rest getMatchOpVariance(String mid)
	{
		MatchOpVariance var = MatchOddsPool.computeMatchOpVariance(mid);
		if(var != null)
		{
			return Rest.okData(var);
		}
		else
		{
			return Rest.failure("There are no match variance.");
		}
	}
	
	
	/**
	 * 获得比赛的欧赔数据
	 * @param mid 比赛编号
	 * @return 比赛与欧赔数据
	 */
	@ResponseBody
	@RequestMapping("/getMatchOps")
	public Rest getMatchOps(String mid)
	{
		if(StringUtils.isEmpty(mid))
		{
			return Rest.failure("The Match mid is null.");
		}
		MatchInfo match = soccerManager.getMatchInfo(mid);
		if(match == null)
		{
			return Rest.failure("The match is null.");
		}
		MatchOdds matchOps = new MatchOdds(match);
		List<Op> ops = soccerManager.getOpList(mid, true);
		matchOps.setOps(ops);
		
		return Rest.okData(matchOps);
	}
	
	/**
	 * 获得比赛的赔率数据
	 * @param mid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getMatchOdds")
	public Rest getMatchOdds(String mid)
	{
		MatchOdds matchOdds = new MatchOdds();
		matchOdds.setMid(mid);
		List<Op> ops = soccerManager.getOddsOp(mid);
		List<Yp> yps = soccerManager.getOddsYp(mid);
		matchOdds.setOps(ops);
		matchOdds.setYps(yps);
		if((ops == null || ops.isEmpty()) && (yps == null || yps.isEmpty()))
		{
			return Rest.failure("There are no odds of Match'" + mid + "'");
		}
		return Rest.okData(matchOdds);
	}
	
	/**
	 * 获得比赛的数据
	 * @param start
	 * @param end
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getMatchesInfo")
	public Rest getMatchesInfo(String start, String end)
	{
		List<Match> matchs = soccerManager.getMatches(start, end);
		if(matchs == null || matchs.isEmpty())
		{
			return Rest.failure("There are no matches in database from '" + start + "' to '" + end + "'");
		}
		return Rest.okData(matchs);
	}
	
	/**
	 * 获得比赛列表的欧赔数据
	 * @param issue
	 * @param type
	 * @param gids 欧赔公司的ID值
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getMatchesOps")
	public Rest getMatchesOps(String issue, String type, String gids)
	{
		List<String> ids = JSON.parseArray(gids, String.class);
		List<MatchOdds> matchOdds = MatchDocLoader.loadMatchOdds(issue, type, ids, SoccerConstants.ODDS_TYPE_OP);		
		return Rest.okData(matchOdds);
	}
	
	/**
	 * 获得联赛的最新排名信息
	 * @param lid 联赛编号
	 * @return 联赛信息列表
	 */
	@ResponseBody
	@RequestMapping("/getLeagueAllRanks")
	public Rest getLatestAllRanks(String lid)
	{
		if(StringUtils.isEmpty(lid))
		{
			return Rest.failure("The League name is null.");
		}		
		List<RankInfo> ranks = soccerManager.getLatestAllRanks(lid);
		if(ranks == null || ranks.size() == 0)
		{
			return Rest.failure("The League '" + lid + "' has no latest rank records.");
		}
		return Rest.okData(ranks);
	}
	
	/**
	 * 获得联赛的最新排名信息
	 * @param lid 联赛编号
	 * @return 联赛信息列表
	 */
	@ResponseBody
	@RequestMapping("/getLeagueRanks")
	public Rest getLatestRanks(String lid)
	{
		if(StringUtils.isEmpty(lid))
		{
			return Rest.failure("The League name is null.");
		}		
		List<RankInfo> ranks = soccerManager.getLatestRanks(lid, SoccerConstants.RANK_TOTAL);
		if(ranks == null)
		{
			return Rest.failure("The League '" + lid + "' has no latest rank records.");
		}
		return Rest.okData(ranks);
	}
	
	/**
	 * 获得比赛数据结果
	 * @param lid
	 * @param season
	 * @param pagination
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getMatches")
	public PageWrapper<MatchInfo> getMatches(String lid, String season, String round, String date, Pagination pagination)
	{
		EntityWrapper<MatchInfo> ew = new EntityWrapper<>();
		
		if(StringUtils.isNotEmpty(lid))
		{
			ew.eq("lid", lid);	
		}
		if(StringUtils.isNotEmpty(season))
		{
			ew.eq("season", season);
		}
		if(StringUtils.isNotEmpty(round))
		{
			ew.eq("round", round);
		}
		
		if(StringUtils.isNotEmpty(date))
		{
			Date d = DateUtil.tryToParseDate(date);
			if(d != null)
			{
				String d1 = DateUtil.getDayString(date);
				ew.eq("date(matchtime)", d1);
			}			
		}
		
		int current = 1;
		int size = DEFAULT_PAGE_SIZE;

		if (pagination != null)
		{
			current = pagination.getCurrent();
			size = pagination.getSize();
			size = size == 0 ? DEFAULT_PAGE_SIZE : size;
		}
		
		if (pagination.getAscs() != null && pagination.getAscs().size() > 0)
		{
			List<String> columns = processOrderFields(pagination.getAscs());
			ew.orderAsc(columns);
		}
		if (pagination.getDescs() != null && pagination.getDescs().size() > 0)
		{
			List<String> columns = processOrderFields(pagination.getDescs());
			ew.orderDesc(columns);
		}
		
		Page<MatchInfo> page = new Page<>(current, size);
		Page<MatchInfo> selectedPage = matchInfoService.selectPage(page, ew);
		return new PageWrapper<>(selectedPage);
	}
	
	/**
	 * 获得比赛数据的列表
	 * @param issue 比赛期号
	 * @param type 比赛类型(这里可以是jc/bd两种类型)
	 * @return 比赛列表
	 */
	@ResponseBody
	@RequestMapping("/getMatchItems")
	public Rest getMatchItems(String issue, String type, String sid)
	{
		
		CorpSetting setting = soccerManager.getCorpSetting(sid);
		if(StringUtils.isEmpty(issue))
		{
			issue = IssueMatchUtil.getCurrentIssue();
		}		
		MatchDoc dataVector = MatchDocLoader.getMatchDoc(issue, setting);
		if(dataVector == null)
		{
			String info = "There are not any JcMatchDatas in database of issue value '" + issue + "'. ";
			logger.info(info);
			return Rest.failure(info);		
		}
		
		List<MatchSynthElement> items;
		if(StringUtils.isNotEmpty(type) && SoccerConstants.LOTTERY_JC.equalsIgnoreCase(type))
		{
			items = MatchDocLoader.getJcMatchSynthElement(dataVector, issue);
		}
		else
		{
			items = MatchDocLoader.getDefaultMatchSynthElement(dataVector);
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("setting", setting);
		map.put("matches", items);
		return Rest.okData(map);
	}
	
	/**
	 * 获得联赛轮次的比赛数据
	 * @param sid 配置
	 * @param lid
	 * @param season
	 * @param round
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getRoundMatchesOdds")
	public Rest getRoundMatchesOdds(String sid, String lid, String season, String round)
	{
		String info;
		CorpSetting setting = soccerManager.getCorpSetting(sid);
		List<MatchOdds> ops = MatchDocLoader.loadRoundMatchOdds(lid, season, round, setting);
		if(ops == null)
		{
			info = "There are no match ops in the database. ";
			logger.info(info);
			return Rest.failure(info);
		}
		
		List<MatchOddsElement> elements = new ArrayList<>();
		for (MatchOdds op : ops)
		{
			elements.add(new MatchOddsElement(op));
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("setting", setting);
		map.put("matches", elements);
		return Rest.okData(map);
	}
	
	/**
	 * 
	 * @param lid
	 * @param season
	 * @param round
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getRoundMatchRanks")
	public Rest getRoundMatchRanks(String lid, String season, String round, String sid)
	{
		CorpSetting setting = null;
		if(StringUtils.isEmpty(sid))
		{
			sid = "0cb50f8e-9118-4358-bf2c-2ae44fa4a902";
		}
		setting = soccerManager.getCorpSetting(sid);
		if(setting == null)
		{
			return Rest.failure("The CorpSetting '" + sid + "' is not set correctly.");
		}
		
		UserCorporate corporate = new UserCorporate();
		corporate.setGid("-100");
		corporate.setId("-100");
		corporate.setIsmain(true);
		corporate.setName("理论计算值");
		corporate.setType(SoccerConstants.ODDS_TYPE_OP);
		corporate.setUserid("anonymous");
		setting.addUserCorporate(corporate);
	
		
		List<MatchOdds> ops = MatchDocLoader.loadRoundMatchOdds(lid, season, round, setting);
		List<MatchRankOddsElement> elements = new ArrayList<>();
		
		List<RankInfo> ranks = soccerManager.getLatestRanks(lid, SoccerConstants.RANK_TOTAL);		
		for (MatchOdds op : ops)
		{
			MatchRankOddsElement element = new MatchRankOddsElement(op);
			elements.add(element);
			for (RankInfo rankInfo : ranks)
			{
				if(op.getHomeid().equals(rankInfo.getTid()))
				{
					element.setHomeRankInfo(rankInfo);
				}
				if(op.getClientid().equals(rankInfo.getTid()))
				{
					element.setClientRankInfo(rankInfo);
				}
			}
			
			element.createTheoryOdds();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("setting", setting);
		map.put("matches", elements);
		return Rest.okData(map);
	}
	
	/**
	 * 获得比赛数据的欧赔数据列表
	 * @param issue
	 * @param type
	 * @param refresh
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getMatchesOdds")
	public Rest getMatchesOdds(String issue, String lid, String type, String sid)
	{
		String info;
		//boolean needToRefresh = false;
		if(StringUtils.isEmpty(issue))
		{
			issue = IssueMatchUtil.getCurrentIssue();
		}
		
		CorpSetting setting = null;
		setting = soccerManager.getCorpSetting(sid);
		if(setting == null)
		{
			return Rest.failure("The CorpSetting '" + sid + "' is not set correctly.");
		}
		
		String source = setting.getSource();
		if(StringUtils.isEmpty(source))
		{
			source = SoccerConstants.DATA_SOURCE_OKOOO;
		}
		
		List<String> lids = null;
		if(StringUtils.isNotEmpty(lid))
		{
			lids = new ArrayList<>();
			lids.add(lid);
		}
		
		long st = System.currentTimeMillis();		
		List<MatchOdds> ops = MatchDocLoader.loadMatchOdds(issue, type, lids, setting, source);
		long en = System.currentTimeMillis();
		if(ops == null)
		{
			info = "There are no match ops in the database. ";
			logger.info(info);
			return Rest.failure(info);
		}
		
		List<MatchOddsElement> elements = new ArrayList<>();
		for (MatchOdds op : ops)
		{
			elements.add(new MatchOddsElement(op));
		}
		
		logger.info("Total spend " + (en - st) + " ms to load " + ops.size() + " match ops.");
		
		Map<String, Object> map = new HashMap<>();
		map.put("setting", setting);
		map.put("matches", elements);
		return Rest.okData(map);
	}
	

	/**
	 * 查看某一场比赛的欧赔数据
	 * 
	 * @param mid
	 *            比赛编号
	 * @return 欧赔数据列表
	 */
	@ResponseBody
	@RequestMapping("/oddsop")
	public Rest getOddsOpList(String mid)
	{
		List<Op> oddsList = soccerManager.getOpListOrderByTime(mid, true);
		return Rest.okData(oddsList);
	}

	/**
	 * 获得联赛、赛季某一轮次的比赛数据
	 * 
	 * @param lid
	 *            联赛编号
	 * @param season
	 *            赛季编号
	 * @param round
	 *            轮次编号
	 * @return 比赛数据
	 */
	@ResponseBody
	@RequestMapping("/getRoundMatchs")
	public Rest getRoundMatchInfos(String lid, String season, String round)
	{
		EntityWrapper<MatchInfo> ew = new EntityWrapper<>();
		ew.eq("lid", lid);
		ew.eq("season", season);
		ew.eq("round", round);
		List<MatchInfo> matchs = matchInfoService.selectList(ew);

		if (matchs != null && matchs.size() > 0)
		{
			return Rest.okData(matchs);
		}
		else
		{
			return Rest.failure("No match data in database.");
		}
	}

	/**
	 * 按照分页方式，获取联赛的比赛数据
	 * 
	 * @param lid
	 *            联赛编号
	 * @param season
	 *            赛季编号
	 * @param pagination
	 *            分页编辑器
	 * @return 联赛比赛的编号
	 */
	@ResponseBody
	@RequestMapping("/getRoundMatchNum")
	public PageWrapper<RoundInfo> getRoundMatchNum(String lid, String season, Pagination pagination)
	{
		EntityWrapper<RoundInfo> ew = new EntityWrapper<>();
		if (StringUtils.isNotEmpty(lid))
		{
			ew.eq("lid", lid);
		}
		// 赛季要用类似的方式
		if (StringUtils.isNotEmpty(season))
		{
			ew.like("season", season);
		}

		int current = 1;
		int size = DEFAULT_PAGE_SIZE;

		if (pagination != null)
		{
			current = pagination.getCurrent();
			size = pagination.getSize();
			size = size == 0 ? DEFAULT_PAGE_SIZE : size;
		}

		if (pagination.getAscs() != null && pagination.getAscs().size() > 0)
		{
			List<String> columns = processOrderFields(pagination.getAscs());
			ew.orderAsc(columns);
		}

		if (pagination.getDescs() != null && pagination.getDescs().size() > 0)
		{
			List<String> columns = processOrderFields(pagination.getDescs());
			ew.orderDesc(columns);
		}

		Page<RoundInfo> page = new Page<>(current, size);
		Page<RoundInfo> selectedPage = roundInfoService.selectPage(page, ew);

		return new PageWrapper<>(selectedPage);
	}
	
	/**
	 * 获得某一场比赛欧赔数据与亚盘数据的比较值
	 * @param mid 比赛编号
	 * @param opgid 欧赔公司编号
	 * @param opsource 欧赔公司来源
	 * @param ypgid 亚盘公司编号
	 * @param ypsource 亚盘公司来源
	 * @return 数据详细列表
	 */
	@ResponseBody
	@RequestMapping("/getMatchCompareOdds")
	public Rest getMatchCompareOdds(String mid, String opgid, String opsource, String ypgid, String ypsource)
	{
		String info = null;
				
		if(StringUtils.isEmpty(opsource))
		{
			opsource = SoccerConstants.DATA_SOURCE_ZGZCW;
		}
		if(StringUtils.isEmpty(ypsource))
		{
			ypsource = SoccerConstants.DATA_SOURCE_ZGZCW;
		}
		
		MatchCorpOddsElement element = MatchDocLoader.getMatchCorpOddsElement(mid, opgid, opsource, ypgid, ypsource);
		if(element != null)
		{
			return Rest.okData(element);
		}
		else
		{
			info = "There are no JcMatch '" + mid + "' in the database.";
			logger.info(info);
			return Rest.failure(info);
		}
	}
	
	/**
	 * 获得某一场比赛的赔率数据
	 * @param mid 比赛编号
	 * @param gid 公司编号
	 * @param type 公司类型
	 * @param source 数据来源
	 * @return 数据列表
	 */
	@ResponseBody
	@RequestMapping("/getMatchCompOdds")
	public Rest getMatchCompOdds(String mid, String gid, String type, String source)
	{
		String info = null;
		JcMatch match = soccerManager.getJcMatch(mid);
		
		if(match == null)
		{
			info = "There are no JcMatch '" + mid + "' in the database.";
			logger.info(info);
			return Rest.failure(info);
		}
		
		if(StringUtils.isEmpty(source))
		{
			source = SoccerConstants.DATA_SOURCE_ZGZCW;
		}
		if(StringUtils.isEmpty(type))
		{
			type = SoccerConstants.ODDS_TYPE_OP;
		}
		
		MatchCorpOddsElement element = new MatchCorpOddsElement(match);
		
		Corporate ypcorp = null;
		Corporate opcorp = null;
		
		//如果是亚盘数据
		if(SoccerConstants.ODDS_TYPE_YP.equalsIgnoreCase(type))
		{
			ypcorp = MatchDocLoader.getCorporate(gid, type, source);
			//存在的情况下
			if(ypcorp != null)
			{							
				opcorp = MatchDocLoader.getCorporate(ypcorp.getName(), SoccerConstants.ODDS_TYPE_OP, null);
			}
			else
			{
				info = "There are no gid '" + gid + "' corporate exist in the database.";
				return Rest.failure(info);
			}
		}
		else
		{
			opcorp = MatchDocLoader.getCorporate(gid, type, source);
			if(opcorp != null)
			{
				ypcorp = MatchDocLoader.getCorporate(opcorp.getName(), SoccerConstants.ODDS_TYPE_YP, null);
				if(ypcorp == null)
				{
					ypcorp = MatchDocLoader.getCorporate(opcorp.getName(), SoccerConstants.ODDS_TYPE_YP, SoccerConstants.DATA_SOURCE_OKOOO);
				}
			}
			else
			{
				info = "There are no gid '" + gid + "' corporate exist in the database.";
				return Rest.failure(info);
			}
		}
		
		if(ypcorp != null || opcorp != null)
		{
			MatchDocLoader.getMatchCorpOdds(element, opcorp, ypcorp);
		}
		
		return Rest.okData(element);
	}

	/**
	 * 获得某一联赛在某一赛季的轮次数据，如果赛季为空，则默认为最新的赛季
	 * 
	 * @param lid
	 *            联赛编号
	 * @param season
	 *            赛季编号
	 * @return 轮次数据列表
	 */
	@ResponseBody
	@RequestMapping("/getRounds")
	public Rest getRoundInfos(String lid, String season)
	{
		EntityWrapper<RoundInfo> ew = new EntityWrapper<>();

		if (StringUtils.isNotEmpty(season))
		{
			ew.eq("lid", lid);
			ew.eq("season", season);
		}
		else
		{
			String sqlWhere = "season=(select max(season) from soccer_league_round_info where lid='" + lid + "')";
			ew.where(sqlWhere).where("lid={0}", lid);
		}
		ew.orderBy("rid+0");

		// System.out.println(ew.toString());
		List<RoundInfo> rounds = roundInfoService.selectList(ew);
		if (rounds != null && rounds.size() > 0)
		{
			return Rest.okData(rounds);
		}
		else
		{
			return Rest.failure("No (" + lid + ", " + season + ") " + " round in database.");
		}
	}

	/**
	 * 处理有些排序字段，如lid 在数据库表中是按照字符串的形式，为了按照数字模式排序，则需要 对lid +0处理。
	 * 
	 * @param columns
	 *            字段列表
	 * @return 处理后的排序列表
	 */
	protected List<String> processOrderFields(List<String> columns)
	{
		List<String> fields = new ArrayList<>();
		for (String string : columns)
		{
			if ("lid".equals(string) || "rid".equals(string))
			{
				string += "+0";
			}
			fields.add(string);
		}
		return fields;
	}

	/**
	 * 获取当前的博彩公司
	 * 
	 * 
	 * @return 博彩公司列表
	 */
	@ResponseBody
	@RequestMapping("/getUserCorps")
	public Rest getUserCorporates(String type, String source)
	{
		List<UserCorporate> list = soccerManager.getUserCorporates(type, source);
		return Rest.okData(list);
	}
	
	/**
	 * 获得配置信息
	 * @param sid 配置的编号
	 * @return 配置对象
	 */
	@ResponseBody
	@RequestMapping("/getCorpSettingData")
	public Rest getCorpSettingData(String sid)
	{
		Map<String, Object> settings = new HashMap<>();
		SettingItem setting = soccerManager.getCorpSetting(sid);
		List<UserCorporate> corporates = soccerManager.getUserCorporates("", "");
		settings.put("setting", setting);
		settings.put("corps", corporates);
		return Rest.okData(settings);
	}
	
	/**
	 * 获得配置信息
	 * @param sid 配置的编号
	 * @return 配置对象
	 */
	@ResponseBody
	@RequestMapping("/getCorpSetting")
	public Rest getCorpSetting(String sid)
	{
		SettingItem setting = soccerManager.getCorpSetting(sid);
		if(setting == null)
		{
			return Rest.failure("There are no Setting '" + sid + "' data in dabasebase.");
		}
		else
		{
			return Rest.okData(setting);
		}
	}
	
	/**
	 * 获得博彩公司数据.
	 * 
	 * @param pagination 分页参数
	 * @return 分页后的数据结果
	 */
	@ResponseBody
	@RequestMapping("/getCorpSettings")
	public PageWrapper<CorpSetting> getSettings(Pagination pagination, String name, String type)
	{
		int current = 1;
		int size = DEFAULT_PAGE_SIZE;
		
		if(pagination != null)
		{
			current = pagination.getCurrent();
			size = pagination.getSize();
			size = size == 0 ? DEFAULT_PAGE_SIZE : size;
		}
		
		//System.out.println("Pagination: " + pagination);
		//System.out.println("Descs:" + pagination.getDescs());
		//System.out.println("Ascs: " + pagination.getAscs());
		
		EntityWrapper<CorpSetting> ew = new EntityWrapper<>();
		
		if(pagination.getAscs() != null && pagination.getAscs().size() > 0)
		{
			ew.orderAsc(pagination.getAscs());
		}
		
		if(pagination.getDescs() != null && pagination.getDescs().size() > 0)
		{
			ew.orderDesc(pagination.getDescs());
		}

	    Page<CorpSetting> page = new Page<>(current, size);
	    Page<CorpSetting> selectPage = soccerManager.selectSettingPage(page, ew);
		
		return new PageWrapper<>(selectPage);
	}
	
	/**
	 * 删除用户设置的信息
	 * @param sid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteCorpSetting")
	public Rest deleteCorpSetting(String sid)
	{
		boolean flag = soccerManager.deleteCorpSetting(sid);
		//logger.info("Delete : " + sid + " flag =" + flag);
		if(flag)
		{
			return Rest.ok();
		}
		else
		{
			return Rest.failure("Failed to delete the CorpSetting '" + sid + "'. ");
		}
	}
	
	/**
	 * 获得博彩公司数据.
	 * 
	 * @param pagination 分页参数
	 * @return 分页后的数据结果
	 */
	@ResponseBody
	@RequestMapping("/getCorps")
	public PageWrapper<UserCorporate> getUserCorprates(Pagination pagination, String name, String type)
	{
		int current = 1;
		int size = DEFAULT_PAGE_SIZE;
		
		if(pagination != null)
		{
			current = pagination.getCurrent();
			size = pagination.getSize();
			size = size == 0 ? DEFAULT_PAGE_SIZE : size;
		}
		
		//System.out.println("Pagination: " + pagination);
		//System.out.println("Descs:" + pagination.getDescs());
		//System.out.println("Ascs: " + pagination.getAscs());
		
		EntityWrapper<UserCorporate> ew = new EntityWrapper<>();
		
		if(pagination.getAscs() != null && pagination.getAscs().size() > 0)
		{
			ew.orderAsc(pagination.getAscs());
		}
		
		if(pagination.getDescs() != null && pagination.getDescs().size() > 0)
		{
			ew.orderDesc(pagination.getDescs());
		}

	    Page<UserCorporate> page = new Page<>(current, size);
	    Page<UserCorporate> selectPage = userCorporateService.selectPage(page, ew);
		
		return new PageWrapper<>(selectPage);
	}

	/**
	 * 更新博彩公司的数据
	 * 
	 * @param query
	 *            json串，包含各种公司信息
	 * @return 更新的信息
	 */
	@ResponseBody
	@RequestMapping("/updateUserCorps")
	public Rest updateUserCorporates(String query)
	{
		// logger.info(query);
		List<UserCorporate> corps = JSON.parseArray(query, UserCorporate.class);
		for (UserCorporate userCorporate : corps)
		{
			logger.info(userCorporate);
		}
		return Rest.ok("更新完成");
	}

	/**
	 * 获得竞彩数据的欧赔数据
	 * 
	 * @param issue
	 *            期号
	 * @return 竞彩数据列表
	 */
	@ResponseBody
	@RequestMapping("/getJcMatchOps")
	public Rest getJcMatchOdds(String issue)
	{
		String info = null;
		if (StringUtils.isEmpty(issue))
		{
			info = "Issue is empty.";
			logger.info(info);
			return Rest.failure(info);
		}
		List<JcMatch> jcMatchs = soccerManager.getJcMatches(issue);
		List<String> mids = getMidList(jcMatchs);
		if (mids.size() <= 0)
		{
			info = "The issue '" + issue + " has no JcMatch data.";
			logger.info(info);
			return Rest.failure(info);
		}
		/*
		Map<String, List<Op>> maps = soccerManager.getOddsOpMap(mids);
		List<JcMatchData> data = new ArrayList<>();
		for (JcMatch match : jcMatchs)
		{
			JcMatchData matchData = new JcMatchData(match);
			List<Op> ops = maps.get(match.getMid());
			if (ops != null)
			{
				matchData.setOps(ops);
			}
			data.add(matchData);
		}*/

		return Rest.okData(jcMatchs);
	}
	
	/**
	 * 获得竞彩足球比赛基本赔率分析数据:<br>
	 * 1、基本欧赔数据(初始赔率、最新赔率)：百家平均欧赔、<br>
	 * 2、基本亚盘数据(初始赔率、最新赔率)：澳门、Interwenten等
	 * 
	 * @param issue 期号
	 * @return 数据结果
	 */
	@ResponseBody
	@RequestMapping("/getJcBasicOdds")
	public Rest getJcMatchBasicOddsData(String issue)
	{
		return Rest.ok();
	}
	
	/**
	 * 获得比赛的统计值
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCorpStatItems")
	public Rest getCorpStatItems()
	{
		List<CorpStatItem> items = soccerManager.getCorpStatItems();
		return Rest.okData(items);
	}
	
	/**
	 * 获得博彩公司的统计值
	 * @param gid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCorpStatItem")
	public Rest getCorpStatItem(String gid)
	{
		CorpStatItem item = soccerManager.getCorpStatItem(gid);
		if(item == null)
		{
			return Rest.failure("There are no CorpStatItem of " + gid);
		}
		else
		{
			return Rest.okData(item);
		}
	}
	
	/**
	 * 欧赔数据列表
	 * @param gid
	 * @param start
	 * @param end
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCorpOdds")
	public Rest getCorpOdds(String gid, String start, String end)
	{
		List<Op> ops = soccerManager.getOpListByGid(gid, start, end);
		logger.info("There are " + ops.size() + " Op values.");
		OpList opList = new OpList(OpList.OpListType.MidUnique);
		opList.addAll(ops);
		List<OddsElement> elements = new ArrayList<>();
		for (Op op : opList)
		{
			elements.add(OddsUtil.createOpItem(op, 0));
		}

		return Rest.okData(elements);
	}
	
	/**
	 * 根据数据计算欧赔的值
	 * @param homevalue
	 * @param clientvalue
	 * @param lossratio
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/computeOdds")
	public Rest computeOddsValue(String homevalue, String clientvalue, String lossratio)
	{
		double home = NumberUtil.parseDouble(homevalue);
		double client = NumberUtil.parseDouble(clientvalue);
		double loss = NumberUtil.parseDouble(lossratio);
		
		if(home <= 0.3 || home > 10 || client < 0.3 || client > 10)
		{
			return Rest.failure("您输入的数据不合要求");
		}
		
		if(loss > 1.0 || loss < 0.6)
		{
			loss = 0.89;
		}
		
		int k = 7;
		double[][] goals = PossionUtil.computeProb(home, client, k);
		double[] p = PossionUtil.computeOddsProb(goals, k);
		Map<String, Object> datas = new HashMap<>();
		datas.put("goals", goals);
		datas.put("prob", p);
		
		return Rest.okData(datas);
	}
	
	/**
	 * 创建图谱数据
	 * 
	 * @return 图
	 */
	@ResponseBody
	@RequestMapping("/getGraph")
	public Rest getGraph(String issue, String lid)
	{		
		try
		{
			String info = null;
			MatchDoc dataVector = dataPools.getMatchDocsFromPool(issue, false);		
			if (dataVector == null)
			{
				info = "The issue '" + issue + " has no " + lid + "  JcMatch data.";
				logger.info(info);
				return Rest.failure(info);
			}
			
			LeagueMatchDoc leagueVector = dataVector.getMatchDataList(lid);
			List<LeagueMatchDoc> leagues = new ArrayList<>();
			leagues.add(leagueVector);

			Graph graph = MatchGraph.createGraph(issue, leagues);			
			return Rest.okData(graph);
		}
		catch(Exception e)
		{
			return Rest.failure("Get League Error.");
		}
	}
	
	/**
	 * 保存配置信息
	 * @param json 字符串
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveCorpSetting")
	public Rest saveCorpSetting(String sid, String name, String json)
	{
		//logger.info("Id: " + sid + ", name: " + name); // + ", json: " + json);		
		
		CorpSetting setting = soccerManager.getCorpSetting(sid, false);
		if(setting == null)
		{
			setting = new CorpSetting();
		}
		setting.setName(name);
		setting.setModifytime(DateUtil.getCurTimeStr());
		setting.setUser("anonymous");
		
		List<UserCorporate> corps = soccerManager.getUserCorporates("", SoccerConstants.DATA_SOURCE_ZGZCW);
		CorpChecker<UserCorporate> checker = new CorpChecker<>();
		
		JSONObject obj = JSON.parseObject(json);
		for (String key : obj.keySet())
		{
			if(!obj.getBoolean(key))
			{
				continue;
			}
			checker.setGid(key);
			UserCorporate corp = ArraysUtil.getSameObject(corps, checker);
			if(corp != null)
			{
				CorpSettingParameter parameter = CorpSetting.createParameter(corp);
				setting.addParameter(parameter);
			}
		}
		
		logger.info("This value size is " + setting.getParams().size());
		
		if(soccerManager.addOrUpdateCorpSetting(setting))
		{
			return Rest.okData(setting);
		}
		else
		{
			return Rest.failure("保存失败");
		}		
	}
	
	
	/**
	 * 获得某一场比赛的分析数据及分析结果
	 * @param mid 比赛编号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getMatchItem")
	public Rest getMatchAnalysis(String mid, String refresh)
	{
		MatchData data = null;
		boolean needToRefresh = false;		
		//是否需要更新数据集
		if(!StringUtils.isEmpty(refresh) && ("true".equalsIgnoreCase(refresh) ||
				"1".equalsIgnoreCase(refresh)))
		{
			needToRefresh = true;
		}
		
		//如果不需要新加载数据，则只需要从内存中查找该竞彩成果
		if(!needToRefresh)
		{
			data = dataPools.getJcMatchDataFromPool(mid);
		}

		//如果数据为空，则需要加载数据记录
		if(data == null)
		{
			JcMatch jcMatch = soccerManager.getJcMatch(mid);
			if(jcMatch == null)
			{
				return Rest.failure("The match '" + mid + "' is not exist in the database.");
			}
			
			String issue = jcMatch.getIssue();
			
			MatchDoc dataVector = dataPools.getMatchDocsFromPool(issue, true);
			
			if(dataVector == null)
			{
				return Rest.failure("The match '" + mid + "' of issue '" + issue + "' is not exist in the database.");
			}
			
			data = dataVector.getMatchData(mid);			
			if(data == null)
			{
				return Rest.failure("The match '" + mid + "' is not exist in the database.");
			}
		}
		return Rest.okData(data.createIssueMatchSynthElement());
	}
	

	/**
	 * 获得竞彩比赛分析对比数据结果
	 * 
	 * @param issue
	 *            期号
	 * @param lid
	 *            联赛编号
	 * @return 竞彩数据分析结果
	 */
	@ResponseBody
	@RequestMapping("/getJcMatchAnalysis")
	public Rest getJcMatchAnalysis(String issue, String lid)
	{
		String info = null;
		MatchDoc dataVector = dataPools.getMatchDocsFromPool(issue, false);		
		if (dataVector == null)
		{
			info = "The issue '" + issue + " has no " + lid + "  JcMatch data.";
			logger.info(info);
			return Rest.failure(info);
		}
		
		LeagueMatchDoc leagueVector = dataVector.getMatchDataList(lid);

		return Rest.okData(leagueVector);
	}
	
	

	/**
	 * 得到比赛编号信息列表
	 * 
	 * @param matchs
	 *            比赛数据列表
	 * @return 编号信息列表
	 */
	protected List<String> getMidList(List<JcMatch> matchs)
	{
		List<String> mids = new ArrayList<>();
		for (JcMatch match : matchs)
		{
			mids.add(match.getMid());
		}
		return mids;
	}

	/**
	 * 获得竞彩公司编号列表
	 * 
	 * @param corps
	 *            竞彩公司编号
	 * @return 编号列表
	 */
	protected List<String> getGidList(List<UserCorporate> corps)
	{
		List<String> gids = new ArrayList<>();
		for (UserCorporate corp : corps)
		{
			gids.add(corp.getGid());
		}
		return gids;
	}
	
	/**
	 * 分割字符串
	 * @param str
	 * @param regex
	 * @return
	 */
	protected static List<String> splitString(String str, String regex)
	{
		String[] reStrings = str.split(regex);
		List<String> list = new ArrayList<>();
		for (String string : reStrings)
		{
			string = string.trim();
			if(StringUtils.isNotEmpty(string))
				list.add(string);
		}
		return list;
	}
}