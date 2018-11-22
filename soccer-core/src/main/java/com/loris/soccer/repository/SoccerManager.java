package com.loris.soccer.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.ArraysUtil;
import com.loris.base.util.DateUtil;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.data.table.Logo;
import com.loris.soccer.bean.data.table.league.League;
import com.loris.soccer.bean.data.table.league.Match;
import com.loris.soccer.bean.data.table.league.Rank;
import com.loris.soccer.bean.data.table.league.Round;
import com.loris.soccer.bean.data.table.league.Season;
import com.loris.soccer.bean.data.table.league.SeasonTeam;
import com.loris.soccer.bean.data.table.league.Team;
import com.loris.soccer.bean.data.table.lottery.BdMatch;
import com.loris.soccer.bean.data.table.lottery.JcMatch;
import com.loris.soccer.bean.data.table.lottery.LotteryCalendar;
import com.loris.soccer.bean.data.table.lottery.UserCorporate;
import com.loris.soccer.bean.data.table.lottery.ZcMatch;
import com.loris.soccer.bean.data.table.odds.Op;
import com.loris.soccer.bean.data.table.odds.Yp;
import com.loris.soccer.bean.data.view.MatchInfo;
import com.loris.soccer.bean.data.view.RankInfo;
import com.loris.soccer.bean.item.IssueMatch;
import com.loris.soccer.bean.item.MatchItem;
import com.loris.soccer.bean.model.LeagueMap;
import com.loris.soccer.bean.model.LeagueSeason;
import com.loris.soccer.bean.okooo.OkoooBdMatch;
import com.loris.soccer.bean.okooo.OkoooJcMatch;
import com.loris.soccer.bean.okooo.OkoooYp;
import com.loris.soccer.bean.setting.CorpSetting;
import com.loris.soccer.bean.setting.Parameter;
import com.loris.soccer.repository.service.BdMatchService;
import com.loris.soccer.repository.service.CorpSettingService;
import com.loris.soccer.repository.service.JcMatchService;
import com.loris.soccer.repository.service.LeagueService;
import com.loris.soccer.repository.service.LotteryCalendarService;
import com.loris.soccer.repository.service.MatchInfoService;
import com.loris.soccer.repository.service.MatchService;
import com.loris.soccer.repository.service.OkoooBdMatchService;
import com.loris.soccer.repository.service.OkoooJcMatchService;
import com.loris.soccer.repository.service.OkoooYpService;
import com.loris.soccer.repository.service.OpService;
import com.loris.soccer.repository.service.ParameterService;
import com.loris.soccer.repository.service.RankInfoService;
import com.loris.soccer.repository.service.RankService;
import com.loris.soccer.repository.service.RoundService;
import com.loris.soccer.repository.service.SeasonService;
import com.loris.soccer.repository.service.SeasonTeamService;
import com.loris.soccer.repository.service.LogoService;
import com.loris.soccer.repository.service.TeamService;
import com.loris.soccer.repository.service.UserCorporateService;
import com.loris.soccer.repository.service.YpService;
import com.loris.soccer.repository.service.ZcMatchService;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwSoccerDownloader;

/**
 * 数据综合管理器类
 * 
 * @author jiean
 *
 */
@Component
public class SoccerManager
{	
	@Autowired
	private LeagueService leagueService;

	@Autowired
	private SeasonService seasonService;
	
	@Autowired
	private SeasonTeamService seasonTeamService;
	
	@Autowired
	private MatchService matchService;
	
	@Autowired
	private MatchInfoService matchInfoService;
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private LogoService logoService;
	
	@Autowired
	private RoundService roundService;
	
	@Autowired
	private RankService rankService;
	
	@Autowired
	private RankInfoService rankInfoService;
	
	@Autowired
	private BdMatchService bdMatchService;
	
	@Autowired
	private JcMatchService jcMatchService;
	
	@Autowired
	private ZcMatchService zcMatchService;
	
	@Autowired
	private LotteryCalendarService lotteryCalendarService; 
	
	@Autowired
	private OpService opService;
	
	@Autowired
	private YpService ypService;
	
	@Autowired
	private UserCorporateService userCorporateService;	
	
	@Autowired
	private OkoooJcMatchService okoooJcMatchService;
	
	@Autowired
	private OkoooYpService okoooYpService;
	
	@Autowired
	private OkoooBdMatchService okoooBdMatchService;
		
	@Autowired
	private ParameterService parameterService;
	
	@Autowired
	private CorpSettingService corpSettingService;
	
	/** 唯一实例 */
	private static SoccerManager singleton = null;
	
	/** 联赛信息*/
	private static List<League> leagues = null;
	
	/**
	 * 获得唯一的实例
	 * @return 管理器
	 */
	public static SoccerManager getInstance()
	{		
		return singleton;
	}
	
	/**
	 * 查找联赛数据
	 * @param name 名称
	 * @return 联赛
	 */
	public static League getLeagueByName(String name)
	{
		if(singleton == null)
		{
			return null;
		}
		if(leagues == null && (leagues = getLeagues()) == null)
		{
			return null;
		}
		for (League league : leagues)
		{
			if(name.equalsIgnoreCase(league.getName()) || 
					name.equalsIgnoreCase(league.getLid()))
			{
				return league;
			}
		}
		return null;
	}
	
	/**
	 * 创建一个唯一实例
	 */
	protected SoccerManager()
	{
		singleton = this;
	}
	
	/**
	 * Get all the Leagues.
	 * 
	 * @return All the leagues.
	 */
	public static List<League> getLeagues()
	{
		if(leagues != null)
		{
			return leagues;
		}
		else if(singleton == null)
		{
			return null;
		}
		else
		{
			leagues = singleton.getAllLeagues();		
			return leagues;
		}
	}
	
	/**
	 * Get the LeagueMap.
	 * @return LeagueMap value.
	 */
	public static LeagueMap getLeagueMap()
	{
		return new LeagueMap(getLeagues());
	}
	
	/**
	 * Get the League
	 * 
	 * @param lid 联赛编号
	 * @return 联赛
	 */
	public League getLeague(String lid)
	{
		EntityWrapper<League> ew = new EntityWrapper<>();
		ew.eq("lid", lid);
		return leagueService.selectOne(ew);
	}
	
	/**
	 * 向系统添加联赛数据或更新联赛数据
	 * @param leagues 数据列表
	 * @return 是否成功的标志
	 */
	public boolean addOrUpdateLeagues(List<League> leagueList)
	{
		List<League> newLeagues = new ArrayList<>();
		for (League league : leagueList)
		{
			if(league.getType().equalsIgnoreCase(SoccerConstants.MATCH_TYPE_CUP) || 
					league.getType().equalsIgnoreCase(SoccerConstants.MATCH_TYPE_LEAGUE))
			{
				if(!existInLeagues(league))
				{
					newLeagues.add(league);
					leagues.add(league);
				}
			}
		}
		
		if(newLeagues.size() > 0)
		{
			leagueService.insertBatch(newLeagues);
		}
		
		leagues = null;		
		return true;
	}
	
	/**
	 * 获得所有的赛事类型数据
	 * @return 赛事数据列表
	 */
	protected List<League> getAllLeagues()
	{
		EntityWrapper<League> ew = new EntityWrapper<>();
		ew.eq("type", SoccerConstants.MATCH_TYPE_CUP).or().eq("type", SoccerConstants.MATCH_TYPE_LEAGUE);
		
		return leagueService.selectList(ew);
	}
	
	/**
	 * 检测是否已经存在联赛数据
	 * @param league 联赛数据
	 * @return 是否存在的标志
	 */
	private boolean existInLeagues(League league)
	{
		for (League l : leagues)
		{
			if(league.getLid().equals(l.getLid()))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 查询比赛列表
	 * 
	 * @param lid 联赛编号
	 * @param season 赛季编号
	 * @param round 比赛轮次
	 * @return 比赛列表
	 */
	public List<Match> getMatches(String lid, String season, String round)
	{
		EntityWrapper<Match> ew = new EntityWrapper<>();
		ew.eq("lid", lid);
		ew.eq("season", season);
		ew.eq("round", round);
		return matchService.selectList(ew);
	}
	
	/**
	 * 查询比赛数据
	 * 
	 * @param tids 球队列表
	 * @param lastDate 截止时间
	 * @param firstDate 起始时间
	 * @return
	 */
	public List<Match> getMatches(List<String> tids, String firstDate, String lastDate)
	{
		if(tids == null || tids.isEmpty())
		{
			return null;
		}
		
		EntityWrapper<Match> ew = new EntityWrapper<>();
		
		ew.eq("1", "1");
		
		ew.andNew();
		ew.in("homeid", tids);
		ew.or().in("clientid", tids);
		/*
		ew.andNew();
		boolean first = true;
		for (String tid : tids)
		{
			if(first)
			{
				first = false;
			}
			else
			{
				ew.or();
			}
			ew.eq("homeid", tid);
			ew.or().eq("clientid", tid);
		}*/
		//起始时间
		if(StringUtils.isNotEmpty(firstDate))
		{
			ew.andNew().gt("matchtime", firstDate);
		}
		//截止时间
		if(StringUtils.isNotEmpty(lastDate))
		{
			ew.andNew().lt("matchtime", lastDate);
		}
		
		//按照时间排降序，最晚的比赛排在最前面
		ew.orderBy("matchtime", false);
		
		return matchService.selectList(ew);
	}
	
	/**
	 * 获得联赛某赛季的参赛球队
	 * 
	 * @param lid
	 * @param season
	 * @return
	 */
	public List<SeasonTeam> getSeasonTeams(String lid, String season)
	{
		EntityWrapper<SeasonTeam> ew = new EntityWrapper<>();
		ew.eq("lid", lid);
		ew.eq("season", season);
		return seasonTeamService.selectList(ew);
	}
	
	/**
	 * 获得博彩公司 的配置信息
	 * @param type
	 * @param source
	 * @return
	 */
	public List<UserCorporate> getUserCorporates(String type, String source)
	{
		EntityWrapper<UserCorporate> ew = new EntityWrapper<>();
		ew.eq("1", "1");
		if(StringUtils.isNotEmpty(type))
		{
			ew.eq("type", type);
		}
		
		if(StringUtils.isNotEmpty(source))
		{
			ew.eq("source", source);
		}
		else
		{
			ew.eq("source", "zgzcw");
		}
		ew.orderBy("gid+0");
		return userCorporateService.selectList(ew);
	}
	
	/**
	 * 获得当前所有的公司
	 * 
	 * @return 所有的博彩公司
	 */
	public List<UserCorporate> getUserCoporates()
	{
		EntityWrapper<UserCorporate> ew = new EntityWrapper<>();
		ew.orderBy("gid+0");
		return userCorporateService.selectList(ew);
	}
	
	/**
	 * 获得用户定义的博彩公司列表
	 * @param isUserOnly 是否用户已经指定
	 * @return 公司列表
	 */
	public List<UserCorporate> getUserCoporates(boolean isUserOnly)
	{
		EntityWrapper<UserCorporate> ew = new EntityWrapper<>();
		ew.eq("userid", isUserOnly);
		ew.orderBy("gid+0");
		return userCorporateService.selectList(ew);
	}
	
	/**
	 * 按照公司ID值，查询欧赔的数据
	 * 
	 * @param mid 比赛编号
	 * @param gid 公司编号
	 * @return 欧赔数据列表
	 */
	public List<Op> getOpList(String mid, String gid)
	{
		EntityWrapper<Op> ew = new EntityWrapper<>();
		ew.eq("mid", mid);
		if(StringUtils.isNotEmpty(gid))
		{
			ew.eq("gid", gid);
		}
		ew.orderBy("lasttime", false);
		return opService.selectList(ew);
	}
	
	/**
	 * 获得该比赛的所有欧赔数据
	 * 
	 * @param mid 比赛编号
	 * @return 欧赔数据列表
	 */
	public List<Op> getOddsOp(String mid)
	{
		EntityWrapper<Op> ew = new EntityWrapper<>();
		ew.eq("mid", mid);
		return opService.selectList(ew);
	}
	
	/**
	 * 查询欧赔数据
	 * 
	 * @param mids 比赛编号
	 * @return 欧赔数据
	 */
	public List<Op> getOddsOp(List<String> mids)
	{
		return getOddsOp(mids, false);
	}
	
	/**
	 * 查询欧赔数据集
	 * 
	 * @param mids 比赛编号
	 * @param gids 欧赔公司编号
	 * @return 欧赔数据列表
	 */
	public List<Op> getOddsOp(List<String> mids, boolean onlyFirstOdds)
	{
		return getOddsOp(mids, null, onlyFirstOdds);
	}
	
	/**
	 * 查询欧赔数据集
	 * 
	 * @param mids 比赛编号
	 * @param gids 欧赔公司编号
	 * @return 欧赔数据列表
	 */
	public List<Op> getOddsOp(List<String> mids, List<String> gids, boolean onlyFirstOdds)
	{
		EntityWrapper<Op> ew = new EntityWrapper<>();
		ew.eq("1", "1");
		if(mids != null && mids.size() > 0)
		{
			ew.andNew().in("mid", mids);
		}		
		if(gids != null && gids.size() > 0)
		{
			ew.andNew().in("gid", gids);
		}
		if(onlyFirstOdds)
		{
			ew.andNew().gt("firsttime", "0");
		}
		return opService.selectList(ew);
	}
	
	/**
	 * 查询亚盘数据集
	 * 
	 * @param mids 比赛编号
	 * @param gids 亚盘公司编号
	 * @return 亚盘数据列表
	 */
	public List<Yp> getOddsYp(List<String> mids, List<String> gids, boolean onlyFirstOdds)
	{
		EntityWrapper<Yp> ew = new EntityWrapper<>();
		ew.eq("1", "1");
		ew.andNew().in("mid", mids);
		
		if(gids.size() > 0)
		{
			ew.andNew().in("gid", gids);
		}
		if(onlyFirstOdds)
		{
			ew.andNew().gt("firsttime", "0");
		}
		
		/*
		for (String mid : mids)
		{
			ew.or().eq("mid", mid);
		}*/
		
		/*
		if(gids.size() > 0)
		{
			ew.andNew();
			boolean first = true;
			for (String gid : gids)
			{
				if(first)
				{					
					first = false;
				}
				else
				{
					ew.or();
				}
				ew.eq("gid", gid);
			}
		}
		
		if(onlyFirstOdds)
		{
			ew.andNew().isNotNull("firsttime");
		}*/
		//System.out.println(ew.toString());
		return ypService.selectList(ew);
	}
	
	/**
	 * 获得欧赔数据表
	 * 
	 * @param mids 比赛编号表
	 * @return 欧赔数据
	 */
	public Map<String, List<Op>> getOddsOpMap(List<String> mids)
	{
		List<Op> ops = getOddsOp(mids);
		Map<String, List<Op>> maps = new HashMap<>();
		for (Op op : ops)
		{
			addToMap(op, maps);
		}
		return maps;
	}
	
	/**
	 * 添加到HASH表中
	 * 
	 * @param op 欧赔数据
	 * @param maps 映射表
	 */
	protected void addToMap(Op op, Map<String, List<Op>> maps)
	{
		for (String key : maps.keySet())
		{
			if(op.getMid().equals(key))
			{
				maps.get(key).add(op);
				return;
			}
		}
		List<Op> ops = new ArrayList<>();
		ops.add(op);
		maps.put(op.getMid(), ops);
	}
	
	/**
	 * 更新当前的所有用户公司
	 * 
	 * @param corporates 所有的公司
	 * @return 是否更新成功的标志
	 */
	public boolean updateUserCorporates(List<UserCorporate> corporates)
	{
		return userCorporateService.updateBatchById(corporates);
	}
	
	/**
	 * Add or update all the matches in list.
	 * 
	 * @param matchs Match
	 * @return The flag.
	 */
	public boolean addOrUpdateMatches(List<Match> matchs)
	{
		EntityWrapper<Match> ew = new EntityWrapper<>();
		
		List<String> mids = new ArrayList<>();
		ArraysUtil.getObjectFieldValue(matchs, mids, Match.class, "mid");
		ew.in("mid", mids);
		/*ew.andNew();
		
		for (Match match : matchs)
		{
			ew.or().eq("mid", match.getMid());
		}*/
		
		//System.out.println("SQL: " + ew.toString());
		matchService.delete(ew);
		
		return matchService.insertBatch(matchs);
	}
	
	/**
	 * 添加或更新比赛数据，与上一个函数不一样的是，这里有一个是否更新的标志数据
	 * 如果设置overwrite为true，则与上一个函数一样；如果设置overwrite为false，
	 * 则如果系统中已经存在的比赛数据不进行更新。
	 * 
	 * @param matchs 比赛数据
	 * @param overwrite 是否覆盖数据
	 * @return
	 */
	public boolean addOrUpdateMatches(List<Match> matchs, boolean overwrite)
	{
		if(overwrite)
		{
			return addOrUpdateMatches(matchs);
		}
		else
		{
			EntityWrapper<Match> ew = new EntityWrapper<>();
			ew.andNew();
			for (Match match : matchs)
			{
				ew.or().eq("mid", match.getMid());
			}
			
			List<Match> existMatchs = matchService.selectList(ew);
			List<Match> needToAdd = new ArrayList<>();
			for (Match match : matchs)
			{
				if(!existInMatchs(existMatchs, match.getMid()))
				{
					needToAdd.add(match);
				}
			}
			
			if(needToAdd.size() > 0)
			{
				return matchService.insertBatch(needToAdd);
			}
			return true;
		}
	}
	
	/**
	 * 检测比赛编号是否已经存在数据列表
	 * 
	 * @param matchs 比赛列表
	 * @param mid 比赛编号
	 * @return 是否存在的标志
	 */
	public boolean existInMatchs(List<Match> matchs, String mid)
	{
		for (Match match : matchs)
		{
			if(mid.equalsIgnoreCase(match.getMid()))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get the Matches between start and end.
	 * 
	 * @param start 起始日期
	 * @param end 截止日期
	 * @return 所有的比赛信息
	 */
	public List<Match> getMatches(String start, String end)
	{
		String field = "date(matchtime)";
		EntityWrapper<Match> ew = new EntityWrapper<>();
		
		if(StringUtils.isNotEmpty(start))
		{
			ew.andNew().eq(field, start).or().gt(field, start);
		}
		if(StringUtils.isNotEmpty(end))
		{
			ew.andNew().eq(field, end).or().lt(field, end);
		}
		return matchService.selectList(ew);
	}
	
	/**
	 * 获得比赛
	 * @param mids
	 * @return
	 */
	public List<Match> getMatches(List<String> mids)
	{
		EntityWrapper<Match> ew = new EntityWrapper<>();
		ew.in("mid", mids);
		return matchService.selectList(ew);
	}
	
	/**
	 * 将赛季信息加入到数据库中
	 * 
	 * @param seasons 赛季列表信息
	 * @return 是否加入成功能标志
	 */
	public boolean addNewSeasons(String lid, List<Season> seasons)
	{
		EntityWrapper<Season> ew = new EntityWrapper<>();
		ew.eq("lid", lid);
		List<Season> existSeasons = seasonService.selectList(ew);
		
		//判断是否已经在数据库中
		List<Season> newSeasons = new ArrayList<>();
		boolean exist = false;
		for (Season season : seasons)
		{
			exist = false;
			for (Season tmpSeason : existSeasons)
			{
				if(season.equals(tmpSeason))
				{
					exist = true;
					break;
				}
			}
			if(!exist)
			{
				newSeasons.add(season);
			}
		}
		
		if(newSeasons.size() <= 0)
		{
			return true;
		}
		
		return seasonService.insertBatch(newSeasons);		
	}
	
	/**
	 * Get the Seasons.
	 * 
	 * @param lid 联赛编号
	 * @return 赛季数据列表
	 */
	public List<Season> getSeasons(String lid)
	{
		EntityWrapper<Season> ew = new EntityWrapper<>();
		ew.eq("lid", lid);
		return seasonService.selectList(ew);
	}
	
	
	
	/**
	 * Get the seasons.
	 * @return 赛季数据列表
	 */
	public List<Season> getSeasons()
	{
		EntityWrapper<Season> ew = new EntityWrapper<>();
		return seasonService.selectList(ew);
	}
	
	/**
	 * 获得比赛赛季数据
	 * @param seasonValue
	 * @return
	 */
	public Map<String, LeagueSeason> getSeasonMap(String seasonValue)
	{
		String field = "season";
		EntityWrapper<Season> ew = new EntityWrapper<>();
		ew.gt(field, seasonValue);
		ew.orNew().eq(field, seasonValue);
		
		List<Season> seasons = seasonService.selectList(ew);
		Map<String, LeagueSeason> maps = new HashMap<>();
		
		for (Season season : seasons)
		{
			LeagueSeason list = maps.get(season.getLid());
			if(list == null)
			{
				list = new LeagueSeason(season.getLid());
				list.add(season);
				maps.put(season.getLid(), list);
			}
			else
			{
				list.add(season);
			}
		}		
		return maps;
	}
	
	/**
	 * Get the seasons by Lid value.
	 * @return Maps.
	 */
	public Map<String, LeagueSeason> getSeasonsMap()
	{
		List<Season> seasons = getSeasons();
		Map<String, LeagueSeason> maps = new HashMap<>();
		
		for (Season season : seasons)
		{
			LeagueSeason list = maps.get(season.getLid());
			if(list == null)
			{
				list = new LeagueSeason(season.getLid());
				list.add(season);
				maps.put(season.getLid(), list);
			}
			else
			{
				list.add(season);
			}
		}
		return maps;
	}
	
	/**
	 * Update the round by id value.
	 * @param rounds Round List.
	 * @return The flag.
	 */
	public boolean updateRounds(List<Round> rounds)
	{
		return roundService.updateAllColumnBatchById(rounds);
	}
	
	/**
	 * 加入新的数据轮次到数据库表中
	 * 
	 * @param lid 联赛编号
	 * @param season 赛季编号
	 * @param rounds 轮次数据
	 * @return 是否加入成功标志
	 */
	public boolean addNewRounds(String lid, String season, List<Round> rounds)
	{
		EntityWrapper<Round> ew = new EntityWrapper<>();
		ew.eq("lid", lid);
		ew.eq("season", season);
		
		List<Round> existRounds = roundService.selectList(ew);
		List<Round> newRounds = new ArrayList<>();
		boolean exist = false;
		for (Round round : rounds)
		{
			exist = false;
			for (Round tmpRound : existRounds)
			{
				if(round.equals(tmpRound))
				{
					exist = true;
					break;
				}
			}
			if(!exist)
			{
				newRounds.add(round);
			}
		}
		
		if(newRounds.size() == 0)
		{
			return true;
		}
		
		return roundService.insertBatch(newRounds);
	}
	
	/**
	 * Add the Round or update the round.
	 * 
	 * @param round Round Entity.
	 * @return 更新或插入是否成功标志
	 */
	public boolean addOrUpdateRound(Round round)
	{
		if(StringUtils.isNotEmpty(round.getId()))
		{
			return roundService.updateAllColumnById(round);
		}
		else
		{
			return roundService.insert(round);
		}
	}
	
	
	/**
	 * 查询比赛数据
	 * 
	 * @param mid 比赛编号
	 * @return 比赛信息
	 */
	public MatchInfo getMatchInfo(String mid)
	{
		EntityWrapper<MatchInfo> ew = new EntityWrapper<>();
		ew.eq("mid", mid);
		return matchInfoService.selectOne(ew);
	}
	
	/**
	 * 查询比赛列表
	 * 
	 * @param lid 联赛编号
	 * @param season 赛季编号
	 * @param round 比赛轮次
	 * @return 比赛列表
	 */
	public List<MatchInfo> getMatchInfos(String lid, String season, String round)
	{
		EntityWrapper<MatchInfo> ew = new EntityWrapper<>();
		ew.eq("lid", lid);
		ew.eq("season", season);
		ew.eq("round", round);
		ew.orderBy("matchtime");
		return matchInfoService.selectList(ew);
	}
	
	/**
	 * 获得比赛信息
	 * @param mids
	 * @return
	 */
	public List<MatchInfo> getMatchInfos(List<String> mids)
	{
		EntityWrapper<MatchInfo> ew = new EntityWrapper<>();
		ew.in("mid", mids);
		return matchInfoService.selectList(ew);
	}
	
	/**
	 * 获得北单数据
	 * @param mids
	 * @return
	 */
	public List<BdMatch> getBdMatches(List<String> mids)
	{
		EntityWrapper<BdMatch> ew = new EntityWrapper<>();
		ew.in("mid", mids);
		return bdMatchService.selectList(ew);
	}
	
	/**
	 * 按照日期获得比赛数据
	 * @param date 日期
	 * @return 比赛列表
	 */
	public List<MatchInfo> getMatchInfoByDate(String date)
	{
		EntityWrapper<MatchInfo> ew = new EntityWrapper<>();
		ew.eq("date(matchtime)", date);
		return matchInfoService.selectList(ew);
	}	
	
	/**
	 * 按照日期获得比赛数据
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return 比赛列表
	 */
	public List<MatchInfo> getMatchInfoByMatchtime(String start, String end)
	{
		EntityWrapper<MatchInfo> ew = new EntityWrapper<>();
		ew.gt("matchtime", start).and().lt("matchtime", end);
		return matchInfoService.selectList(ew);
	}
	
	/**
	 * 获得北单比赛数据
	 * @param issue 比赛日期
	 * @return 数据列表
	 */
	public List<BdMatch> getBdMatches(String issue)
	{
		EntityWrapper<BdMatch> ew = new EntityWrapper<>();
		ew.eq("date", issue);
		return bdMatchService.selectList(ew);
	}
	
	/**
	 * 获得北单比赛数据
	 * @param issue 比赛日期
	 * @return 数据列表
	 */
	public List<BdMatch> getBdMatches(String issue, String lid)
	{
		EntityWrapper<BdMatch> ew = new EntityWrapper<>();
		ew.eq("date", issue);		
		if(StringUtils.isNotEmpty(lid))
		{
			ew.eq("lid", lid);
		}
		return bdMatchService.selectList(ew);
	}
	
	/**
	 * 获得北单比赛数据
	 * @param date 比赛日期
	 * @return 数据列表
	 */
	public List<BdMatch> getBdMatchByMatchtime(String start, String end)
	{
		EntityWrapper<BdMatch> ew = new EntityWrapper<>();
		ew.gt("matchtime", start).and().lt("matchtime", end);
		return bdMatchService.selectList(ew);
	}
	
	/**
	 * 获得竞彩或北单比赛数据
	 * @param mid 比赛编号
	 * @return 比赛数据
	 */
	public IssueMatch getIssueMatch(String mid)
	{
		IssueMatch match = getBdMatch(mid);
		if(match == null)
		{
			match = getJcMatch(mid);
		}
		return match;
	}
	
	/**
	 * 获得北单比赛数据
	 * @param mid 比赛编号
	 * @return 比赛数据
	 */
	public BdMatch getBdMatch(String mid)
	{
		EntityWrapper<BdMatch> ew = new EntityWrapper<>();
		ew.eq("mid", mid);
		return bdMatchService.selectOne(ew);
	}
	
	/**
	 * 查询竞彩比赛的数据
	 * 
	 * @param issue 期号
	 * @return 竞彩比赛数据列表
	 */
	public List<JcMatch> getJcMatches(String issue)
	{
		EntityWrapper<JcMatch> ew = new EntityWrapper<>();
		if (StringUtils.isEmpty(issue))
		{
			issue = DateUtil.getCurDayStr();
		}
		ew.eq("issue", issue);
		ew.orderBy("ordinary + 0");
		return jcMatchService.selectList(ew);
	}
	
	/**
	 * 查询竞彩比赛数据
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return 竞彩数据列表
	 */
	public List<JcMatch> getJcMatchesByDate(String start, String end)
	{
		EntityWrapper<JcMatch> ew = new EntityWrapper<>();
		ew.gt("matchtime", start).and().lt("matchtime", end);
		return jcMatchService.selectList(ew);
	}
	
	
	/**
	 * 获得竞彩比赛数据
	 * @param mid 比赛编号
	 * @return 竞彩比赛
	 */
	public JcMatch getJcMatch(String mid)
	{
		EntityWrapper<JcMatch> ew = new EntityWrapper<>();
		ew.eq("mid", mid);
		return jcMatchService.selectOne(ew);
	}
	
	/**
	 * 查询竞彩比赛的数据
	 * 
	 * @param issue 期号
	 * @param lid 联赛编号
	 * @return 比赛数据
	 */
	public List<JcMatch> getJcMatches(String issue, String lid)
	{
		EntityWrapper<JcMatch> ew = new EntityWrapper<>();
		if (StringUtils.isEmpty(issue))
		{
			issue = DateUtil.getCurDayStr();
		}
		ew.eq("issue", issue);
		if(StringUtils.isNotEmpty(lid))
		{
			ew.eq("lid", lid);
		}
		ew.orderBy("ordinary + 0");
		return jcMatchService.selectList(ew);
	}
	
	/**
	 * 查询两个球队的比赛历史，这里时间是指在此之前的
	 * 
	 * @param t1 球队1的编号
	 * @param t2 球队2的编号
	 * @param endtime 截止时间
	 * @return 返回比赛的历史
	 */
	public List<MatchInfo> getMatchHistory(String t1, String t2, String endtime)
	{
		EntityWrapper<MatchInfo> ew = new EntityWrapper<>();
		ew.andNew().eq("htid", t1).and().eq("ctid", t2);
		ew.orNew().eq("ctid", t1).and().eq("htid", t2);
		ew.andNew().lt("matchtime", endtime);
		ew.orderBy("matchtime", false);
		
		//System.out.println(ew.toString());
		return matchInfoService.selectList(ew);
	}
	
	/**
	 * 获得两个比赛队伍的历史比赛战绩
	 * 
	 * @param t1 球队1的编号
	 * @param t2 球队2的编号
	 * @param endtime 截止时间
	 * @param between 是否是两个队之间的比赛
	 * @return 比赛历史
	 */
	public List<MatchInfo> getMatchHistory(String t1, String t2, 
			String endtime, boolean between)
	{
		if(between)
		{
			return getMatchHistory(t1, t2, endtime);
		}
		else
		{
			EntityWrapper<MatchInfo> ew = new EntityWrapper<>();
			ew.andNew().eq("htid", t1).or().eq("htid", t2);
			ew.orNew().eq("ctid", t1).or().eq("ctid", t2);
			ew.andNew().lt("matchtime", endtime);
			ew.orderBy("matchtime", false);
			return matchInfoService.selectList(ew);
		}
	}
	
	/**
	 * 添加欧赔数据到数据库列表中
	 * 
	 * @param list 欧赔数据列表
	 * @return 是否加入成功
	 */
	public boolean addOpList(List<Op> list)
	{
		return opService.insertBatch(list);
	}
	
	/**
	 * 添加或更新欧赔数据
	 * @param mid
	 * @param list
	 * @return
	 */
	public boolean addOrUpdateMatchOps(String mid, List<Op> list)
	{
		EntityWrapper<Op> ew = new EntityWrapper<>();
		ew.eq("mid", mid);
		List<Op> ops = opService.selectList(ew);
		
		List<Op> results = new ArrayList<>();
		for (Op op : list)
		{
			if(!mid.equals(op.getMid()))
			{
				continue;
			}
			boolean contains = false;
			for (Op op2 : ops)
			{
				if(op2.equal(op))
				{
					contains = true;
					break;
				}
			}
			if(!contains)
			{
				results.add(op);
			}
		}
		return opService.insertBatch(results);
	}
	
	/**
	 * 添加或更新欧赔数据
	 * @param mid
	 * @param list
	 * @return
	 */
	public boolean addOrUpdateMatchYps(String mid, List<Yp> list)
	{
		EntityWrapper<Yp> ew = new EntityWrapper<>();
		ew.eq("mid", mid);
		List<Yp> ops = ypService.selectList(ew);
		
		List<Yp> results = new ArrayList<>();
		for (Yp op : list)
		{
			if(!mid.equals(op.getMid()))
			{
				continue;
			}
			boolean contains = false;
			for (Yp op2 : ops)
			{
				if(op2.equal(op))
				{
					contains = true;
					break;
				}
			}
			if(!contains)
			{
				results.add(op);
			}
		}
		return ypService.insertBatch(results);
	}
	
	/**
	 * 查询某一场比赛的欧赔数据
	 * 
	 * @param mid 比赛编号
	 * @return 欧赔数据列表
	 */
	public List<Op> getOpListOrderByTime(String mid, boolean hasFirst)
	{
		return getOpList(mid, "firsttime, ordinary+0", hasFirst);
	}
	
	/**
	 * 查询某一场比赛的欧赔数据
	 * 
	 * @param mid 比赛编号
	 * @return 欧赔数据列表
	 */
	public List<Op> getOpList(String mid, boolean hasFirst)
	{
		return getOpList(mid, "ordinary+0, lasttime", hasFirst);
	}
	
	/**
	 * 只添加新的欧赔数据
	 * 
	 * @param mid 比赛编号
	 * @param ops 欧赔列表数据
	 * @return 是否添加成功的标志
	 */
	public boolean addNewOpList(String mid, List<Op> ops)
	{
		List<Op> newOps = new ArrayList<>();
		List<Op> existOps = getOpListOrderByTime(mid, false);
		for (Op op : ops)
		{
			if(!existOpValue(existOps, op))
			{
				newOps.add(op);
			}
		}		
		if(newOps.size() > 0)
		{
			return opService.insertBatch(newOps);
		}
		return true;
	}
	
	/**
	 * 查出现有欧赔数据中是否已经存在该赔率
	 * 
	 * @param existOps 数据库中的欧赔数据列表
	 * @param op 待比较的欧赔数据
	 * @return 是否相等的标志
	 */
	public boolean existOpValue(List<Op> existOps, Op op)
	{
		for (Op existOp : existOps)
		{
			if(op.equal(existOp))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 查询某一场比赛的欧赔数据
	 * 
	 * @param mid 比赛编号
	 * @param orderColumns 排序字段
	 * @param hasFirst 是否有初始赔率值
	 * @return 欧赔数据列表
	 */
	public List<Op> getOpList(String mid, String orderColumns, boolean hasFirst)
	{
		EntityWrapper<Op> ew = new EntityWrapper<>();
		ew.eq("mid", mid);
		if(hasFirst)
		{
			ew.isNotNull("firsttime");
		}
		if(StringUtils.isNotEmpty(orderColumns))
		{
			ew.orderBy(orderColumns, true);
		}
		return opService.selectList(ew);
	}
	
	/**
	 * 添加亚盘数据到数据库列表中
	 * 
	 * @param list 亚盘数据列表
	 * @return 是否加入成功
	 */
	public boolean addYpList(List<Yp> list)
	{
		return ypService.insertBatch(list);
	}
	
	/**
	 * 只添加新的欧赔数据
	 * 
	 * @param mid 比赛编号
	 * @param yps 欧赔列表数据
	 * @return 是否添加成功的标志
	 */
	public boolean addNewYpList(String mid, List<Yp> yps)
	{
		List<Yp> newYps = new ArrayList<>();
		List<Yp> existYps = getYpList(mid);
		for (Yp yp : yps)
		{
			if(!existYpValue(existYps, yp))
			{
				newYps.add(yp);
			}
		}		
		if(newYps.size() > 0)
		{
			return ypService.insertBatch(newYps);
		}
		return true;
	}
	
	/**
	 * 查出现有欧赔数据中是否已经存在该赔率
	 * 
	 * @param existOps 数据库中的欧赔数据列表
	 * @param op 待比较的欧赔数据
	 * @return 是否相等的标志
	 */
	public boolean existYpValue(List<Yp> existYps, Yp yp)
	{
		for (Yp existYp : existYps)
		{
			if(yp.equal(existYp))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获得亚盘数据列表
	 * @param mids
	 * @param hasFirstValue
	 * @return
	 */
	public List<Yp> getYpList(List<String> mids, boolean hasFirstValue)
	{
		return getYpList(mids, null, hasFirstValue);
	}
	
	/**
	 * 获得亚盘数据列表
	 * @param mids
	 * @param gids
	 * @param hasFirstValue
	 * @return
	 */
	public List<Yp> getYpList(List<String> mids, List<String> gids, boolean hasFirstValue)
	{
		EntityWrapper<Yp> ew = new EntityWrapper<>();
		ew.eq("1", "1");
		if(mids != null && mids.size() > 0)
		{
			ew.andNew().in("mid", mids);
		}
		if(gids != null && gids.size() > 0)
		{
			ew.andNew().in("gid", gids);
		}
		if(hasFirstValue)
		{
			ew.andNew().gt("firsttime", "0");
		}
		ew.orderBy("ordinary+0", true);
		return ypService.selectList(ew);
	}
	
	/**
	 * 查询某一场比赛的亚盘数据
	 * 
	 * @param mid 比赛编号
	 * @return 亚盘数据列表
	 */
	public List<Yp> getYpList(String mid)
	{
		EntityWrapper<Yp> ew = new EntityWrapper<>();
		ew.eq("mid", mid);
		ew.orderBy("ordinary+0", true);
		return ypService.selectList(ew);
	}
	
	/**
	 * 查询一场比赛、公司的亚盘数据
	 * @param mid
	 * @param gid
	 * @return
	 */
	public List<Yp> getYpList(String mid, String gid)
	{
		EntityWrapper<Yp> ew = new EntityWrapper<>();
		ew.eq("mid", mid);
		ew.eq("gid", gid);
		ew.orderBy("lasttime", false);
		return ypService.selectList(ew);
	}
	
	/**
	 * 查询某一场比赛的亚盘数据
	 * 
	 * @param mid 比赛编号
	 * @param orderColumns 排序字段
	 * @param hasFirst 是否有初始赔率值
	 * @return 亚盘数据列表
	 */
	public List<Yp> getYpList(String mid, String orderColumns, boolean hasFirst)
	{
		EntityWrapper<Yp> ew = new EntityWrapper<>();
		ew.eq("mid", mid);
		if(hasFirst)
		{
			ew.isNotNull("firsttime");
		}
		
		if(StringUtils.isNotEmpty(orderColumns))
		{
			ew.orderBy(orderColumns, true);
		}
		return ypService.selectList(ew);
	}
	
	/**
	 * Add or update all the matches in list.
	 * 
	 * @param matchs Match
	 * @return The flag.
	 */
	public boolean addOrUpdateBdMatches(List<BdMatch> matchs)
	{
		if(matchs == null || matchs.size() == 0)
		{
			return false;
		}
		EntityWrapper<BdMatch> ew = new EntityWrapper<>();
		
		List<String> mids = new ArrayList<>();
		ArraysUtil.getObjectFieldValue(matchs, mids, BdMatch.class, "mid");
		
		ew.in("mid", mids);
		/*ew.andNew();
		for (BdMatch match : matchs)
		{
			ew.or().eq("mid", match.getMid());
		}*/
		bdMatchService.delete(ew);		
		return bdMatchService.insertBatch(matchs);
	}
	
	/**
	 * Add or Update the TeamLogo
	 * 
	 * @param logo
	 * @return
	 */
	public boolean addOrUpdateTeamLogo(Logo logo)
	{
		if(StringUtils.isNotEmpty(logo.getId()))
		{
			return logoService.updateAllColumnById(logo);
		}
		else
		{
			return logoService.insert(logo);
		}
	}
	
	/**
	 * Get All of the Logos.
	 * 
	 * @return Logo list.
	 */
	public List<Logo> getAllLogos()
	{
		EntityWrapper<Logo> ew = new EntityWrapper<>();
		return logoService.selectList(ew);
	}
	
	/**
	 * Add or Update the logo.
	 * 
	 * @param logo
	 * @return
	 */
	public boolean addOrUpdateLogo(Logo logo)
	{
		if(StringUtils.isNotEmpty(logo.getId()))
		{
			return logoService.updateAllColumnById(logo);
		}
		else
		{
			return logoService.insert(logo);
		}
	}
	
	/**
	 * 查找LOGO值
	 * 
	 * @param type 类型：联赛、球队
	 * @param id 唯一ID值
	 * @return LOGO
	 */
	public Logo getLogo(String type, String id)
	{
		EntityWrapper<Logo> ew = new EntityWrapper<>();
		if(StringUtils.isEmpty(type))
		{
			type = Logo.LOGO_TYPE_TEAM;
		}
		ew.eq("type", type);
		ew.eq("tid", id);
		
		return logoService.selectOne(ew);
	}
	
	/**
	 * Add or update all the matches in list.
	 * 
	 * @param matchs Match
	 * @return The flag.
	 */
	public boolean addOrUpdateJcMatches(List<JcMatch> matchs)
	{
		if(matchs == null || matchs.size() == 0)
		{
			return false;
		}
		EntityWrapper<JcMatch> ew = new EntityWrapper<>();
		List<String> mids = new ArrayList<>();
		ArraysUtil.getObjectFieldValue(matchs, mids, JcMatch.class, "mid");
		
		ew.in("mid", mids);
		/*ew.andNew();
		for (JcMatch match : matchs)
		{
			ew.or().eq("mid", match.getMid());
		}*/
		jcMatchService.delete(ew);
		
		return jcMatchService.insertBatch(matchs);
	}
	
	/**
	 * Add or update all the matches in list.
	 * 
	 * @param matchs Match
	 * @return The flag.
	 */
	public boolean addOrUpdateZcMatches(List<ZcMatch> matchs)
	{
		if(matchs == null || matchs.size() == 0)
		{
			return false;
		}
		EntityWrapper<ZcMatch> ew = new EntityWrapper<>();
		List<String> mids = new ArrayList<>();
		ArraysUtil.getObjectFieldValue(matchs, mids, ZcMatch.class, "mid");
		
		ew.in("mid", mids);
		/*
		EntityWrapper<ZcMatch> ew = new EntityWrapper<>();
		ew.andNew();
		for (ZcMatch match : matchs)
		{
			ew.or().eq("mid", match.getMid());
		}*/
		zcMatchService.delete(ew);
		
		return zcMatchService.insertBatch(matchs);
	}
	
	/**
	 * Get all the teams.
	 * 
	 * @return All the teams.
	 */
	public List<Team> getAllTeams()
	{
		EntityWrapper<Team> ew = new EntityWrapper<>();
		return teamService.selectList(ew);
	}
	
	/**
	 * Add all the Ranks.
	 * 
	 * @param ranks
	 * @return
	 */
	public boolean addRanks(List<Rank> ranks)
	{
		return rankService.insertBatch(ranks);
	}
	
	/**
	 * 获得最新的比赛排名数据
	 * @param lid 联赛编号
	 * @return 编号列表
	 */
	public List<RankInfo> getLatestAllRanks(String lid)
	{
		EntityWrapper<RankInfo> ew = new EntityWrapper<>();
		ew.eq("lid", lid);
		return rankInfoService.selectList(ew);
	}
	
	/**
	 * 获得最新的比赛排名数据
	 * @param lid 联赛编号
	 * @param type 排名类型
	 * @return 编号列表
	 */
	public List<RankInfo> getLatestRanks(String lid, String type)
	{
		EntityWrapper<RankInfo> ew = new EntityWrapper<>();
		ew.eq("lid", lid);
		ew.eq("type", type);
		return rankInfoService.selectList(ew);
	}
	
	/**
	 * 获得球队的排名信息
	 * @param tids 球队列表
	 * @return 
	 */
	public List<RankInfo> getTeamLatestRank(List<String> tids)
	{
		EntityWrapper<RankInfo> ew = new EntityWrapper<>();
		ew.in("tid", tids);
		ew.orderBy("rank");
		return rankInfoService.selectList(ew);
	}
	
	/**
	 * 获得比赛双方最近的排名数据
	 * @param item 比赛信息
	 * @return 排名列表
	 */
	public List<RankInfo> getMatchTeamLatestRank(MatchItem item)
	{
		List<String> tids = new ArrayList<>();
		tids.add(item.getHomeid());
		tids.add(item.getClientid());
		return getTeamLatestRank(tids);
	}
	
	/**
	 * Add or Update the team.
	 * 
	 * @param team
	 * @return
	 */
	public boolean addOrUpdateTeam(Team team)
	{
		if(StringUtils.isNotEmpty(team.getId()))
		{
			return teamService.updateAllColumnById(team);
		}
		else
		{
			return teamService.insert(team);
		}
	}
	
	/**
	 * 获得下载的日历
	 * 
	 * @param start 初始日期
	 * @param end  结束日期
	 * @return  日历表
	 */
	public List<LotteryCalendar> getLotteryCalendars(String start, String end)
	{
		EntityWrapper<LotteryCalendar> ew = new EntityWrapper<>();
		if(StringUtils.isNotEmpty(start))
		{
			ew.andNew().gt("date", start).or().eq("date", start);
		}
		if(StringUtils.isNotEmpty(end))
		{
			ew.andNew().lt("date", end).or().eq("date", end);
		}
		return lotteryCalendarService.selectList(ew);
	}
	
	/**
	 * 更新竞彩日历数据表
	 * 
	 * @param calendars
	 * @return
	 */
	public boolean addOrUpdateLotteryCalendars(List<LotteryCalendar> calendars)
	{
		List<String> ids = new ArrayList<>();
		
		for (LotteryCalendar calendar : calendars)
		{
			ids.add(calendar.getId());
		}
		lotteryCalendarService.deleteBatchIds(ids);

		return lotteryCalendarService.insertBatch(calendars);
	}
	
	/**
	 * 获得比赛轮次数据数据
	 * @param isTimeNull
	 * @return
	 */
	public List<Round> getNullTimeRounds()
	{
		EntityWrapper<Round> ew = new EntityWrapper<>();
		ew.isNull("starttime").or().isNull("endtime");
		return roundService.selectList(ew);
	}
	
	/**
	 * 获得比赛轮次数据
	 * @param startYear 开始年份
	 * @param start 开始时间
	 * @param end 结束时间
	 * @param checkNullValue 是否检测空值
	 * @param orderByLid 是否按照LID值排序
	 * @return 轮次列表
	 */
	public List<Round> getRounds(String startYear, String start, String end,
			boolean checkNullValue, boolean orderByLid)
	{
		String startField = "date(starttime)";
		String endField = "date(endtime)";
		EntityWrapper<Round> ew = new EntityWrapper<>();
		if(StringUtils.isNotEmpty(startYear))
		{
			ew.andNew().gt("season", startYear).or().eq("season", startYear);
		}
		
		if(StringUtils.isNotEmpty(start))
		{
			ew.andNew().gt(endField, start).or().eq(endField, start);
		}
		if(StringUtils.isNotEmpty(end))
		{
			ew.andNew().lt(startField, end).or().eq(startField, end);
		}
		if(checkNullValue)
		{
			ew.orNew().isNull("starttime").or().isNull("endtime");
		}
		if(orderByLid)
		{
			String columns = "lid+0, season, name";
			ew.orderBy(columns);
		}
		return roundService.selectList(ew);
	}
	
	
	/**
	 * Get the Round data.
	 * 
	 * @param lid 联赛编号
	 * @param season 赛季编号
	 * @return 轮次列表
	 */
	public List<Round> getSeasonRounds(String lid, String season)
	{
		EntityWrapper<Round> ew = new EntityWrapper<>();
		ew.eq("lid", lid);
		ew.eq("season", season);
		return roundService.selectList(ew);
	}
	
	/*
	public List<RoundInfo> getSeasonRoundInfos(String lid, String season)
	{
		EntityWrapper<RoundInfo> ew = new EntityWrapper<>();
		ew.eq("lid", lid);
		ew.eq("season", season);
		return roundIn
	}*/
	
	/**
	 * 获得比赛为空的比赛轮次
	 * 
	 * @return 轮次数据
	 */
	public List<Round> getEmptyRounds()
	{
		EntityWrapper<Round> ew = new EntityWrapper<>();
		ew.isNull("starttime");
		return roundService.selectList(ew);
	}
	
	/**
	 * 获得用户定义的欧赔公司
	 * 
	 * @param onlyUserDefined 是否只查找用户定义的公司
	 * @param source 数据来源
	 * @return 欧赔公司数据列表
	 */
	public List<UserCorporate> getUserOpCorporates(boolean onlyUserDefined)
	{
		return getUserOpCorporates(onlyUserDefined, ZgzcwSoccerDownloader.SOURCE_ZGZCW);
	}
	
	/**
	 * 获得用户定义的欧赔公司
	 * 
	 * @param onlyUserDefined 是否只查找用户定义的公司
	 * @param source 数据来源
	 * @return 欧赔公司数据列表
	 */
	public List<UserCorporate> getUserOpCorporates(boolean onlyUserDefined, String source)
	{
		EntityWrapper<UserCorporate> ew = new EntityWrapper<>();
		ew.eq("type", SoccerConstants.ODDS_TYPE_OP);
		if(StringUtils.isNotEmpty(source))
		{
			ew.eq("source", source);
		}
		if(onlyUserDefined)
		{
			ew.eq("userid", true);
		}
		return userCorporateService.selectList(ew);
	}
	
	/**
	 * 获得用户定义的欧赔公司
	 * 
	 * @param onlyUserDefined 是否只查找用户定义的公司
	 * @return 欧赔公司数据列表
	 */
	public List<UserCorporate> getUserYpCorporates(boolean onlyUserDefined)
	{
		return getUserYpCorporates(onlyUserDefined, ZgzcwSoccerDownloader.SOURCE_ZGZCW);
	}
	
	/**
	 * 获得用户定义的欧赔公司
	 * 
	 * @param onlyUserDefined 是否只查找用户定义的公司
	 * @param source 数据来源
	 * @return 欧赔公司数据列表
	 */
	public List<UserCorporate> getUserYpCorporates(boolean onlyUserDefined, String source)
	{
		EntityWrapper<UserCorporate> ew = new EntityWrapper<>();
		ew.eq("type", SoccerConstants.ODDS_TYPE_YP);
		ew.eq("source", source);
		if(onlyUserDefined)
		{
			ew.eq("userid", true);
		}
		return userCorporateService.selectList(ew);
	}
	
	/**
	 * 添加亚盘数据列表
	 * @param yps 亚盘数据
	 * @return 添加是否成功的标志
	 */
	public boolean addOkoooYpList(List<OkoooYp> yps)
	{
		if(yps == null || yps.size() == 0)
		{
			return false;
		}
		return okoooYpService.insertBatch(yps);
	}
	
	/**
	 * 添加新的亚盘数据到数据库
	 * @param mid 比赛编号
	 * @param yps 亚盘数据列表
	 * @return 添加是否成功的标志
	 */
	public boolean addNewOkoooYpList(String mid, List<OkoooYp> yps)
	{
		List<OkoooYp> newYps = new ArrayList<>();
		List<OkoooYp> existYps = getOkoooYpList(mid);
		for (OkoooYp yp : yps)
		{
			if(!existOkoooYpValue(existYps, yp))
			{
				newYps.add(yp);
			}
		}		
		if(newYps.size() > 0)
		{
			return okoooYpService.insertBatch(newYps);
		}
		return true;
	}
	
	/**
	 * 获取澳客网亚盘数据
	 * @param mid 比赛编号
	 * @return 亚盘数据列表
	 */
	public List<OkoooYp> getOkoooYpList(String mid)
	{
		EntityWrapper<OkoooYp> ew = new EntityWrapper<>();
		ew.eq("mid", mid);
		ew.orderBy("ordinary+0", true);
		return okoooYpService.selectList(ew);
	}
	
	/**
	 * 获取澳客网亚盘数据
	 * @param mid 比赛编号
	 * @return 亚盘数据列表
	 */
	public List<OkoooYp> getOkoooYpList(String mid, String gid)
	{
		EntityWrapper<OkoooYp> ew = new EntityWrapper<>();
		ew.eq("mid", mid);
		ew.eq("gid", gid);
		ew.orderBy("ordinary+0", true);
		return okoooYpService.selectList(ew);
	}
	
	/**
	 * 查询亚盘数据集
	 * 
	 * @param mids 比赛编号
	 * @param gids 亚盘公司编号
	 * @return 亚盘数据列表
	 */
	public List<OkoooYp> getOkoooYpList(List<String> mids, List<String> gids, boolean onlyFirstOdds)
	{
		EntityWrapper<OkoooYp> ew = new EntityWrapper<>();
		ew.eq("1", "1");
		
		if(mids != null && mids.size() > 0)
		{
			ew.andNew().in("mid", mids);
		}
		else
		{
			return new ArrayList<OkoooYp>();
		}
		
		if(gids != null && gids.size() > 0)
		{
			ew.andNew().in("gid", gids);
		}
		/*
		if(onlyFirstOdds)
		{
			ew.andNew().gt("firsttime", "");
		}*/
		/*for (String mid : mids)
		{
			ew.or().eq("mid", mid);
		}
		if(gids.size() > 0)
		{
			ew.andNew();
			boolean first = true;
			for (String gid : gids)
			{
				if(first)
				{					
					first = false;
				}
				else
				{
					ew.or();
				}
				ew.eq("gid", gid);
			}
		}
		
		if(onlyFirstOdds)
		{
			ew.andNew().isNotNull("firsttime");
		}*/
		//System.out.println(ew.toString());
		return okoooYpService.selectList(ew);
	}
	
	/**
	 * 查出现有欧赔数据中是否已经存在该赔率
	 * 
	 * @param existOps 数据库中的欧赔数据列表
	 * @param op 待比较的欧赔数据
	 * @return 是否相等的标志
	 */
	public boolean existOkoooYpValue(List<OkoooYp> existYps, Yp yp)
	{
		for (Yp existYp : existYps)
		{
			if(yp.equal(existYp))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获得OKOOO竞彩比赛数据列表
	 * @param issue
	 * @return
	 */
	public List<OkoooJcMatch> getOkoooJcMatches(String issue)
	{
		EntityWrapper<OkoooJcMatch> ew = new EntityWrapper<>();
		ew.eq("issue", issue);
		return okoooJcMatchService.selectList(ew);
	}
	
	/**
	 * Add or update all the matches in list.
	 * 
	 * @param matchs Match
	 * @return The flag.
	 */
	public boolean addOrUpdateOkoooJcMatches(List<OkoooJcMatch> matchs)
	{
		if(matchs == null || matchs.size() == 0)
		{
			return false;
		}
		EntityWrapper<OkoooJcMatch> ew = new EntityWrapper<>();
		List<String> mids = new ArrayList<>();
		ArraysUtil.getObjectFieldValue(matchs, mids, OkoooJcMatch.class, "mid");
		ew.in("mid", mids);
		/*ew.andNew();
		for (OkoooJcMatch okoooJcMatch : matchs)
		{
			ew.or().eq("mid", okoooJcMatch.getMid());
		}*/
		okoooJcMatchService.delete(ew);
		return okoooJcMatchService.insertBatch(matchs);
	}
	
	/**
	 * 获得OKOOO竞彩比赛数据列表
	 * @param issue
	 * @return
	 */
	public List<OkoooBdMatch> getOkoooBdMatches(String issue)
	{
		EntityWrapper<OkoooBdMatch> ew = new EntityWrapper<>();
		ew.eq("issue", issue);
		return okoooBdMatchService.selectList(ew);
	}
	
	/**
	 * Add or update all the matches in list.
	 * 
	 * @param matchs Match
	 * @return The flag.
	 */
	public boolean addOrUpdateOkoooBdMatches(List<OkoooBdMatch> matchs)
	{
		if(matchs == null || matchs.size() == 0)
		{
			return false;
		}
		EntityWrapper<OkoooBdMatch> ew = new EntityWrapper<>();
		List<String> mids = new ArrayList<>();
		ArraysUtil.getObjectFieldValue(matchs, mids, OkoooBdMatch.class, "mid");
		ew.in("mid", mids);
		okoooBdMatchService.delete(ew);
		return okoooBdMatchService.insertBatch(matchs);
	}
	
	/**
	 * 得到所有的设置参数值
	 * 
	 * @param pname 配置名称
	 * @return 参数列表
	 */
	public List<Parameter> getParameters(String pname)
	{
		EntityWrapper<Parameter> ew = new EntityWrapper<>();
		if(StringUtils.isNotEmpty(pname))
		{
			ew.eq("pname", pname).or().eq("pid", pname);			
		}
		return parameterService.selectList(ew);
	}
	
	
	/**
	 * Add or update the parameters.
	 * 
	 * @param parameters
	 * @return
	 */
	public boolean addOrUpdateParameters(List<Parameter> parameters)
	{
		if(parameters == null || parameters.size() == 0)
		{
			return false;
		}
		
		//先要删除那些已经存在的，然后再添加所有的到数据库中
		EntityWrapper<Parameter> ew = new EntityWrapper<>();
		ew.andNew();
		for (Parameter parameter : parameters)
		{
			if(StringUtils.isNotEmpty(parameter.getId()))
			{
				ew.or().eq("id", parameter.getId());
			}
		}
		parameterService.delete(ew);
		
		return parameterService.insertBatch(parameters);
	}
	
	/**
	 * 获得默认的设置
	 * @return
	 */
	public CorpSetting getDefaultCorpSetting()
	{
		EntityWrapper<CorpSetting> ew = new EntityWrapper<>();
		ew.orderBy("id");
		List<CorpSetting> settings = corpSettingService.selectList(ew);
		if(settings == null || settings.size() == 0)
		{
			return null;
		}
		CorpSetting setting = settings.get(0);	
		loadCorpSettingParams(setting);		
		return setting;		
	}
	
	/**
	 * 获得公司的配置信息
	 * @param sid 编号
	 * @return 公司配置
	 */
	public CorpSetting getCorpSetting(String sid)
	{
		if(StringUtils.isEmpty(sid))
		{
			return getDefaultCorpSetting();
		}
		return getCorpSetting(sid, true);
	}
	/**
	 * 获得公司的配置信息
	 * @param sid 编号
	 * @return 公司配置
	 */
	public CorpSetting getCorpSetting(String sid, boolean loadParams)
	{
		CorpSetting setting = corpSettingService.selectById(sid);
		if(setting == null)
		{
			return null;
		}
		
		if(loadParams)
		{
			EntityWrapper<Parameter> ew = new EntityWrapper<>();
			ew.eq("pid", sid);
			ew.orderBy("value+0");
			List<Parameter> parameters = parameterService.selectList(ew);
			setting.addParams(parameters);
		}
		return setting;
	}
	
	/**
	 * 加载
	 * @param setting
	 * @return
	 */
	public boolean loadCorpSettingParams(CorpSetting setting)
	{
		EntityWrapper<Parameter> ew = new EntityWrapper<>();
		ew.eq("pid", setting.getId());
		ew.orderBy("value+0");
		List<Parameter> parameters = parameterService.selectList(ew);
		setting.clearParams();
		setting.addParams(parameters);
		return parameters.size() > 0;
	}
	
	/**
	 * 获得类型
	 * @return
	 */
	public List<CorpSetting> getCorpSettings()
	{
		EntityWrapper<CorpSetting> ew = new EntityWrapper<>();
		ew.orderBy("name");
		return corpSettingService.selectList(ew);
	}
	
	/**
	 * 删除用户设置的信息
	 * @param sid
	 * @return
	 */
	public boolean deleteCorpSetting(String sid)
	{	
		try
		{
			EntityWrapper<Parameter> ew0 = new EntityWrapper<>();
			ew0.eq("pid", sid);
			parameterService.delete(ew0);
		}
		catch(Exception e)
		{
		}
		EntityWrapper<CorpSetting> ew = new EntityWrapper<>();
		ew.eq("id", sid);
		
		return corpSettingService.deleteById(sid);
		//return corpSettingService.delete(ew);
	}
	
	/**
	 * 添加或创建数据库中的设置与配置
	 * @param setting
	 * @return
	 */
	public boolean addOrUpdateCorpSetting(CorpSetting setting)
	{
		if(StringUtils.isEmpty(setting.getId()))
		{
			setting.create();
			List<Parameter> parameters = setting.getParams();
			if(parameters.size() > 0)
			{
				parameterService.insertBatch(setting.getParams());
			}
			return corpSettingService.insert(setting);
		}
		else
		{
			
			EntityWrapper<Parameter> ew = new EntityWrapper<>();
			ew.eq("pid", setting.getId());
			parameterService.delete(ew);
			
			List<Parameter> parameters = setting.getParams();
			if(parameters.size() > 0)
			{
				parameterService.insertBatch(setting.getParams());
			}
			
			return corpSettingService.updateAllColumnById(setting);
		}
	}
	
	/**
	 * 选择页面数据
	 * @param page
	 * @param ew
	 * @return
	 */
	public Page<CorpSetting> selectSettingPage(Page<CorpSetting> page, EntityWrapper<CorpSetting> ew)
	{
		return corpSettingService.selectPage(page, ew);
	}
}
