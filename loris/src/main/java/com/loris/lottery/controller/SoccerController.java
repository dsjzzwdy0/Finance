package com.loris.lottery.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.User;
import com.loris.base.util.DateUtil;
import com.loris.lottery.util.WebConstants;
import com.loris.soccer.analysis.util.PerformanceUtil;
import com.loris.soccer.bean.data.table.Logo;
import com.loris.soccer.bean.data.table.league.League;
import com.loris.soccer.bean.data.table.league.Round;
import com.loris.soccer.bean.data.table.league.Season;
import com.loris.soccer.bean.data.table.league.SeasonTeam;
import com.loris.soccer.bean.data.table.lottery.JcMatch;
import com.loris.soccer.bean.data.table.odds.Op;
import com.loris.soccer.bean.data.table.odds.Yp;
import com.loris.soccer.bean.data.view.MatchInfo;
import com.loris.soccer.bean.data.view.RankInfo;
import com.loris.soccer.bean.element.RankElement;
import com.loris.soccer.bean.setting.CorpSetting;
import com.loris.soccer.repository.SoccerManager;


@Controller
@RequestMapping("/soccer")
public class SoccerController extends BaseController
{	
	private static Logger logger = Logger.getLogger(SoccerManager.class);
	
	// 开盘基础数据 
	public final static String[][] ISSUE_PAGE_TYPES = {
		{"danchang", "北京单场"},				//北京单场数据
		{"jingcai",	"竞彩足球"},				//竞彩数据
		{"listvars", ""}				//所有单场欧赔分析
	};
	
	//当日开盘分析页面类型
	public final static String[][] ANALYSIS_PAGE_TYPES =
	{
		{"anarel", "关联分析" }, 				// 关联分析
		{"anaoy", "欧亚对比分析" }, 			// 欧亚对比分析
		{"anaop", "欧赔分析" }, 				// 欧赔分析
		{"anayp", "亚盘对比分析" }, 			// 亚盘对比分析
		{"anafc", "欧赔方差分析" }, 			// 欧赔方差分析
		{"anazj", "战绩对比分析" }, 			// 战绩对比分析
		{"anazh", "综合分析与结果推荐" } 		// 综合分析与结果推荐
	};
	
	//比赛页面
	public final static String[][] LEAGUE_PAGE_TYPES = {
		{"leaguerel", "联赛关联"},			//百家欧赔
		{"leagueoy", "欧亚对比"},				//欧亚对比
		{"leaguefirst", "开盘分析"},			//大小对比
		{"leagueop", "赔率分析"},				//赔率分析
		{"bsls", "比赛历史"},					//比赛历史
		{"qpql", "球盘球路"},					//球盘球路
		{"opvar", "欧赔分析"}					//欧赔分析			
	};
	
	//比赛页面
	public final static String[][] MATCH_PAGE_TYPES = {
		{"bjop", "百家欧赔"},					//百家欧赔
		{"ypdb", "亚盘数据"},					//亚盘
		{"dxdb", "大小对比"},					//大小对比
		{"bfyc", "八方预测"},					//八方预测
		{"bsls", "比赛历史"},					//比赛历史
		{"qpql", "球盘球路"},					//球盘球路
		{"opvar", "欧赔分析"}					//欧赔分析		
	};
	
	/** 数据管理器 */
	@Autowired
	private SoccerManager soccerManager;
	
	/**
	 * Create a new instance of SoccerManager.
	 */
	public SoccerController()
	{
	}
	
	/**
	 * 加入用户信息
	 * @param view
	 */
	protected void setUserObject(ModelAndView view)
	{
		User user = (User)request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if(user != null)
		{
			view.addObject("user", user);
		}
	}
	
	/**
	 * 系统主页面
	 * @return
	 */
	@RequestMapping("main")
	public ModelAndView getMainPage()
	{
		ModelAndView view = new ModelAndView("main.soccer");
		return view;
	}
	
	/**
	 * 获得比赛分析页面
	 * @param type 类型
	 * @param mid 比赛编号
	 * @return 分析页面
	 */
	@RequestMapping("/match")
	public ModelAndView getMatchPage(String type, String mid)
	{
		int index = getMatchPageIndex(type);
		MatchInfo match = soccerManager.getMatchInfo(mid);
		if(match == null)
		{
			String errorInfo = "The match(" + mid + ") dosen't exist.";
			return error(errorInfo);
		}
		
		List<RankInfo> ranks = soccerManager.getMatchTeamLatestRank(match);
		RankElement rankElement = new RankElement(match, ranks);
		//logger.info(ranks);
		ModelAndView view = new ModelAndView("match" + MATCH_PAGE_TYPES[index][0] + ".soccer");
		view.addObject("type", "match");
		view.addObject("page", MATCH_PAGE_TYPES[index][0]);
		view.addObject("title", MATCH_PAGE_TYPES[index][1]);
		view.addObject("match", match);
		view.addObject("rank", rankElement);
		
		setUserObject(view);
		
		return view;
	}
	
	/**
	 * 获得数据分析页面
	 * 
	 * @param type 分析页面的类型
	 * @return
	 */
	@RequestMapping("/analysis")
	public ModelAndView getAnalysisPage(String type)
	{
		int index = getAnalysisPageIndex(type);
		List<CorpSetting> settings = soccerManager.getCorpSettings();
		ModelAndView view = new ModelAndView(ANALYSIS_PAGE_TYPES[index][0] + ".soccer");
		view.addObject("type", "analysis");
		view.addObject("page", ANALYSIS_PAGE_TYPES[index][0]);
		view.addObject("title", ANALYSIS_PAGE_TYPES[index][1]);
		view.addObject("issues", getLatestIssues(10));		
		view.addObject("settings", settings);	

		setUserObject(view);
		return view;
	}
	
	/**
	 * 联赛分析页面
	 * @param type 分析类型
	 * @param mid 比赛编号
	 * @param round 赛事轮次
	 * @return
	 */
	@RequestMapping("/analeague")
	public ModelAndView getLeagueAnalysisPage(String type, String mid, Round round)
	{
		String info = "";
		int index = getLeaguePageIndex(type);
		if(StringUtils.isNotEmpty(mid))
		{
			MatchInfo m = soccerManager.getMatchInfo(mid);
			if(m == null)
			{
				info = "There is no match in database of mid value '" + mid +"'";
			}
			else
			{
				round.setLid(m.getLid());
				round.setSeason(m.getSeason());
				round.setRid(m.getRound());
			}
		}		
		if(StringUtils.isEmpty(round.getRid()) || StringUtils.isEmpty(round.getSeason()) 
				|| StringUtils.isEmpty(round.getLid()))
		{
			info = "The round " + round.toString() + " is empty value, please check the round value.";			
		}
		if(StringUtils.isNotEmpty(info))
		{
			return error(info);
		}		
		League league = soccerManager.getLeague(round.getLid());
		List<CorpSetting> settings = soccerManager.getCorpSettings();
		ModelAndView view = new ModelAndView(LEAGUE_PAGE_TYPES[index][0] + ".soccer");
		view.addObject("type", "league");
		view.addObject("page", LEAGUE_PAGE_TYPES[index][0]);
		view.addObject("title", LEAGUE_PAGE_TYPES[index][1]);
		view.addObject("league", league);
		view.addObject("round", round);
		view.addObject("settings", settings);

		setUserObject(view);
		//view.addObject("rounds", rounds);
		return view;
	}
	
	/**
	 * 赛事中心页面：一张总的数据页面
	 * 
	 * @return 赛事中心
	 */
	@RequestMapping("/sszx")
	public ModelAndView getSszx()
	{
		ModelAndView view = new ModelAndView("sszx.soccer");
		return view;
	}
		
	/**
	 * 比赛数据列表
	 * 
	 * @return 比赛中心
	 */
	@RequestMapping("/matchs")
	public ModelAndView getMatchList()
	{
		ModelAndView view = new ModelAndView("matchs.soccer");
		return view;
	}
	
	/**
	 * 查看比赛信息
	 * 
	 * @param mid 比赛编号
	 * @return 比赛数据列表
	 */
	@RequestMapping("/view")
	public ModelAndView viewMatchData(String mid)
	{
		ModelAndView view = new ModelAndView("matchview.soccer");
		return view;
	}
	
	/**
	 * 设置用户自定义的博彩公司数据
	 * @return 博彩公司数据设置页面
	 */
	@RequestMapping("/usercorp")
	public ModelAndView getUserCorps()
	{
		ModelAndView view = new ModelAndView("usercorp.soccer");
		view.addObject("type", "setting");
		return view;
	}
	
	/**
	 * 设置用户自定义的博彩公司数据
	 * @return 博彩公司数据设置页面
	 */
	@RequestMapping("/settings")
	public ModelAndView getSettings()
	{
		ModelAndView view = new ModelAndView("settings.soccer");
		view.addObject("type", "setting");
		return view;
	}
	
	/**
	 * 联赛页面
	 * 
	 * @param lid 联赛编号
	 * @param season 赛季编号
	 * @param round 轮次编号
	 * @return 联赛主页面
	 */
	@RequestMapping("/league")
	public ModelAndView getLeague(String lid, String season, String round)
	{
		String errorInfo = "";
		League league = null;
		List<Season> seasons = null;
		List<Round> rounds = null;
		List<MatchInfo> matchs = null;
		List<SeasonTeam> teams = null;
		
		league = soccerManager.getLeague(lid);
		if(league == null)
		{
			errorInfo = "League " + lid + " is not exist.";
			logger.info(errorInfo);
			return error(errorInfo);
		}
		
		seasons = soccerManager.getSeasons(lid);
		if(seasons == null || seasons.size() <= 0)
		{
			errorInfo = "League " + lid + " has no season datas.";
			logger.info(errorInfo);
			return error(errorInfo);
		}
		
		season = StringUtils.isNotEmpty(season) ? season : seasons.get(0).getSeason();
		rounds = soccerManager.getSeasonRounds(lid, season);
		if(rounds == null || rounds.size() <= 0)
		{
			errorInfo = "League " + lid + " season " + season  + " has no round datas.";
			logger.info(errorInfo);
			return error(errorInfo);
		}
		round = StringUtils.isNotEmpty(round) ? round : rounds.get(0).getRid();
		
		matchs = soccerManager.getMatchInfos(lid, season, round);
		teams = soccerManager.getSeasonTeams(lid, season);
		
		ModelAndView view = new ModelAndView("league.soccer");
		view.addObject("seasons", seasons);
		view.addObject("league", league);
		view.addObject("rounds", rounds);
		view.addObject("season", season);
		view.addObject("round", round);
		view.addObject("matchs", matchs);
		view.addObject("teams", teams);
		return view;
	}
	
	/**
	 * 图标数据服务接口
	 * 
	 * @param type 图标类型，这里默认有联赛图标、球队的图标
	 * @param id 图标的唯一编号
	 * @param response 返回数据接口
	 */
	@RequestMapping(value="/getImage",method = RequestMethod.GET) 
	public void getImage(String type, String id, HttpServletResponse response)
	{		
		Logo logo = soccerManager.getLogo(type, id);
		if(logo == null)
		{
			try
			{
				response.sendRedirect("../content/images/shouye_tu.png");
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			return;
		}
		
		byte[] data = logo.getImages();
		if(StringUtils.isNotEmpty(logo.getMediatype()))
		{
			response.setContentType(logo.getMediatype());
		}
		else
		{
			response.setContentType("image/jpeg");
		}
        response.setCharacterEncoding("utf-8");
        
        try
        {
        	OutputStream outputStream=response.getOutputStream(); 
        	outputStream.write(data);
        	outputStream.flush();
        	outputStream.close();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
	}
	
	/**
	 * 胜负彩页面
	 * 
	 * @param issue
	 * @return
	 */
	@RequestMapping("/sfc")
	public ModelAndView getSfcIndex(String issue)
	{
		ModelAndView view = new ModelAndView("sfc.soccer");
		return view;
	}
	
	/**
	 * 胜负彩比赛列表页面（中国足球竞彩数据列表）
	 * 
	 * @param issue 期号
	 * @return 页面数据
	 */
	@RequestMapping("/sfclist")
	public ModelAndView getSfcList(@RequestParam(value = "issue", defaultValue = "") String issue)
	{
		EntityWrapper<JcMatch> ew = new EntityWrapper<>();
		if (StringUtils.isEmpty(issue))
		{
			issue = DateUtil.getCurDayStr();
		}
		ew.eq("issue", issue);
		ew.orderBy("ordinary");
		
		List<JcMatch> matchs = soccerManager.getJcMatches(issue);		
		List<String> issues = getIssueList(issue);
		Map<String, Integer> rates = PerformanceUtil.computeRatingNumber(matchs);
		Map<String, Integer> leagues = PerformanceUtil.computeLeagueNumber(matchs);
		String week = DateUtil.getWeekName(issue);
		
		ModelAndView view = new ModelAndView("soccer/sfc/sfcdetail");
		view.addObject("matches", matchs);
		view.addObject("week", week);
		view.addObject("issue", issue);
		view.addObject("issues", issues);
		view.addObject("rates", rates);
		view.addObject("leagues", leagues);
		
		return view;
	}
	
	/**
	 * 百家欧赔数据列表页面
	 * 
	 * @return 百家欧赔页面
	 */
	@RequestMapping("/bjop")
	public ModelAndView getBjop(String mid)
	{
		String errorInfo = "";
		MatchInfo match = soccerManager.getMatchInfo(mid);
		List<Op> list = soccerManager.getOpList(mid, "ordinary+0", true);
		//VarianceItem var = MatchOddsUtil.computeOpVariance(mid, list);
		//VarianceItem firstVar = MatchOddsUtil.computeOpFirstVariance(mid, list);
		//System.out.println("OPList: " + list.size());
		//System.out.println("Match: " + match);
		//logger.info("Match: " + match);
		if(list.size() == 0 || match == null )
		{
			//System.out.println("");
			errorInfo = match == null ? "The match(" + mid + ") dosen't exist." : 
				"The match(" + mid + ") has no op record.";
			return error(errorInfo);
		}
		ModelAndView view = new ModelAndView("bjop.soccer");
		view.addObject("match", match);
		view.addObject("oddslist", list);
		//view.addObject("var", var);
		//view.addObject("firstvar", firstVar);
		
		view.addObject("page", MATCH_PAGE_TYPES[0]);
		return view;
	}
	
	/**
	 * 欧赔数据分析页面
	 * @param mid 比赛编号
	 * @return 欧赔页面
	 */
	@RequestMapping("/opvar")
	public ModelAndView getOpVarPage(String mid)
	{
		String errorInfo = "";
		MatchInfo match = soccerManager.getMatchInfo(mid);
		if( match == null )
		{
			//System.out.println("");
			errorInfo = match == null ? "The match(" + mid + ") dosen't exist." : 
				"The match(" + mid + ") has no op record.";
			return error(errorInfo);
		}
		ModelAndView view = new ModelAndView("opvar.soccer");
		view.addObject("match", match);		
		view.addObject("page", MATCH_PAGE_TYPES[6]);
		return view;
	}
	
	/**
	 * 百家欧赔数据列表页面
	 * 
	 * @return 百家欧赔页面
	 */
	@RequestMapping("/ypdb")
	public ModelAndView getYpdb(String mid)
	{
		String errorInfo = "";
		List<Yp> list = soccerManager.getYpList(mid, "ordinary+0", true);
		MatchInfo match = soccerManager.getMatchInfo(mid);
		
		if(list.size() == 0 || match == null )
		{
			//System.out.println("");
			errorInfo = match == null ? "The match(" + mid + ") dosen't exist." : 
				"The match(" + mid + ") has no op record.";
			//System.out.println("");
			return error(errorInfo);
		}
		ModelAndView view = new ModelAndView("ypdb.soccer");
		view.addObject("match", match);
		view.addObject("yplist", list);
		view.addObject("page", MATCH_PAGE_TYPES[1]);
		return view;
	}
	
	/**
	 * 百家欧赔数据列表页面
	 * 
	 * @return 百家欧赔页面
	 */
	@RequestMapping("/compare")
	public ModelAndView sfcCompare(String issue)
	{
		ModelAndView view = new ModelAndView("compare.soccer");
		return view;
	}
	
	/**
	 * 百家欧赔数据列表页面
	 * 
	 * @param issue 竞彩期号
	 * @return 百家欧赔页面
	 */
	@RequestMapping("/jingcai")
	public ModelAndView dataJingcai(String issue)
	{
		ModelAndView view = new ModelAndView("jingcai.soccer");
		view.addObject("page", ISSUE_PAGE_TYPES[1]);
		return view;
	}
	
	/**
	 * 百家欧赔数据列表页面
	 * 
	 * @param issue 竞彩期号
	 * @return 百家欧赔页面
	 */
	@RequestMapping("/danchang")
	public ModelAndView dataDangchang(String issue)
	{
		ModelAndView view = new ModelAndView("danchang.soccer");
		view.addObject("page", ISSUE_PAGE_TYPES[0]);
		return view;
	}
	
	/**
	 * 欧赔赔率方差计算中心
	 * @return 方差计算中心页面
	 */
	@RequestMapping("/listvars")
	public ModelAndView getVariances()
	{
		ModelAndView view = new ModelAndView("listvars.soccer");
		view.addObject("page", MATCH_PAGE_TYPES[3]);
		return view;
	}
	
	/**
	 * 数据关联分析页面
	 * @param issue 竞彩期号
	 * @param setting 设置名称
	 * @return 关联分析页面
	 */
	@RequestMapping("/association")
	public ModelAndView associationCetner(String issue, String setting)
	{
		ModelAndView view = new ModelAndView("association.soccer");
		return view;
	}
	
	/**
	 * 公司赔率，包含亚盘、欧赔数据
	 * @param mid 比赛编号
	 * @param gid 公司编写
	 * @param type 类型
	 * @param source 数据来源
	 * @return
	 */
	@RequestMapping("/corpodds")
	public ModelAndView corpOdds(String mid, String gid, String type, String source)
	{
		String errorInfo = null;
		MatchInfo match = soccerManager.getMatchInfo(mid);
		
		if(match == null )
		{
			errorInfo = "The match(" + mid + ") dosen't exist." ;
			return error(errorInfo);
		}
		
		ModelAndView view = new ModelAndView("corpodds.soccer");
		view.addObject("match", match);
		view.addObject("page", MATCH_PAGE_TYPES[2]);
		return view;
	}
	
	/**
	 * 出错处理页面
	 * 
	 * @return 错误页面
	 */
	@RequestMapping("/error")
	public ModelAndView error(String error)
	{
		ModelAndView view = new ModelAndView("error.soccer");
		view.addObject("info", error);
		return view;
	}
	
	/**
	 * 联赛关联分析数据页面
	 * 
	 * @return 关联分析页面
	 */
	@RequestMapping("/glfx")
	public ModelAndView leagueCorrelation()
	{
		ModelAndView view = new ModelAndView("glfx.soccer");
		return view;
	}
	
	/**
	 * 联赛关联分析数据页面
	 * 
	 * @return 关联分析页面
	 */
	@RequestMapping("/test")
	public ModelAndView test()
	{
		ModelAndView view = new ModelAndView("league.soccer");
		return view;
	}
	
	/**
	 * 获得SFC测试页面
	 * @return SFC测试页面
	 */
	@RequestMapping("/sfctest")
	public ModelAndView getSfcPage()
	{
		ModelAndView view = new ModelAndView("soccer/sfc_y");
		return view;
	}
	
	/**
	 * 理论值的计算
	 * @param homevalue
	 * @param clientvalue
	 * @return
	 */
	@RequestMapping("/computeodds")
	public ModelAndView computeOdds()
	{
		ModelAndView view = new ModelAndView("computeodds.soccer");
		return view;
	}
	
	/**
	 * 初始化比赛页面
	 * @param mid 比赛编号
	 * @param view 视图
	 * @return 是否初始化的标志
	 */
	protected boolean initMatchInfo(String mid, ModelAndView view)
	{
		String errorInfo;
		MatchInfo match = soccerManager.getMatchInfo(mid);		
		if(match == null )
		{
			errorInfo = "The match(" + mid + ") dosen't exist.";
			logger.info(errorInfo);
			return false;
		}
		return true;
	}
	
	/**
	 * 处理档期列表数据
	 * 
	 * @param issues
	 * @param curissue
	 */
	protected List<String> getIssueList(String curIssue)
	{
		List<String> issues = new ArrayList<>();
		Date date = DateUtil.tryToParseDate(curIssue);
		
		List<Date> dates = DateUtil.getDates(date, -5);
		for (Date date2 : dates)
		{
			issues.add(DateUtil.formatDay(date2));
		}
		
		return issues;
	}
	
	/**
	 * 获得页面的序号
	 * @param type
	 * @param names
	 * @return
	 */
	protected int getPageIndex(String type, String[][] names)
	{
		if(StringUtils.isEmpty(type))
		{
			return 0;
		}
		for (int i = 0; i < names.length; i ++)
		{
			if(type.equalsIgnoreCase(names[i][0]))
			{
				return i;
			}
		}
		try
		{
			int index = Integer.parseInt(type);
			if(index >= names.length)
			{
				index = 0;
			}
			return index;
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	
	/**
	 * 解析分析数据页面
	 * @param type 页面类型
	 * @return 页面名称
	 */
	protected int getAnalysisPageIndex(String type)
	{
		return getPageIndex(type, ANALYSIS_PAGE_TYPES);
	}
	
	/**
	 * 解析比赛页面序号
	 * @param type
	 * @return
	 */
	public int getMatchPageIndex(String type)
	{
		return getPageIndex(type, MATCH_PAGE_TYPES);
	}
	
	/**
	 * 解析比赛页面序号
	 * @param type
	 * @return
	 */
	public int getLeaguePageIndex(String type)
	{
		return getPageIndex(type, LEAGUE_PAGE_TYPES);
	}
	
	/**
	 * 解析分析数据页面
	 * @param type 页面类型
	 * @return 页面名称
	 
	protected String getAnalysisPageName(String type)
	{
		if(StringUtils.isEmpty(type))
		{
			return ANALYSIS_PAGE_TYPES[0][0];
		}
		for (String string : ANALYSIS_PAGE_TYPES[])
		{
			if(type.equalsIgnoreCase(string))
			{
				return string;
			}
		}
		try
		{
			int index = Integer.parseInt(type);
			if(index >= ANALYSIS_PAGE_TYPES.length)
			{
				index = 0;
			}
			return ANALYSIS_PAGE_TYPES[index][0];
		}
		catch(Exception e)
		{
			return ANALYSIS_PAGE_TYPES[0][0];
		}
	}*/
	
	/**
	 * 获得最近的几期数据
	 * @param size
	 * @return
	 */
	protected List<String> getLatestIssues(int size)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		List<String> issues = new ArrayList<>();
		
		issues.add(DateUtil.formatDay(calendar.getTime()));
		for(int i = 0; i < size; i ++)
		{
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			issues.add(DateUtil.formatDay(calendar.getTime()));
		}
		return issues;
	}
}
