package com.loris.soccer.web.downloader.zgzcw;

import java.net.URLEncoder;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.DateUtil;
import com.loris.base.web.manager.Downloader;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.web.downloader.zgzcw.page.LeagueWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.LiveWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.LotteryCalendarWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.LotteryWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.MatchHistoryWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsOpWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsOpZhishuWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsYpWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsYpZhishuWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.RankWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.RoundCupWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.RoundLeagueWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.SeasonWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.TeamWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.ZgzcwCenterPage;

/**
 * 下载页面创建器
 * 
 * @author jiean
 *
 */
public class ZgzcwWebPageCreator
{
	/** The Header Referer name. */
	public static final String ZGZCW_HEADER_REFERER = "Referer";
	
	/** 右斜框 */
	public static final String RIGHT_SPASH = "/";
	
	/** BASE_URL */
	static final public String[] PAGE_URLS =
	{
		"http://saishi.zgzcw.com/soccer/", 												// "cup/51/2017-2018/"   杯赛类型的数据
		"http://saishi.zgzcw.com/summary/liansaiAjax.action",      						// ?source_league_id=8&currentRound=3&season=2017-2018&seasonType=";  //联赛类型的数据
		"http://saishi.zgzcw.com/soccer/", 												// "cup/51/2017-2018/"   杯赛类型的数据
		"http://cp.zgzcw.com/lottery/bdplayvsforJsp.action?lotteryId=200",    			//&issue=80401 	北单足彩
		"http://cp.zgzcw.com/lottery/jchtplayvsForJsp.action?lotteryId=47&type=jcmini", // &issue=2018-03-25  	竞彩足球
		"http://cp.zgzcw.com/lottery/zcplayvs.action?lotteryId=13", 					//&issue=300&v=2018-02-22"	 足彩足球
		"http://fenxi.zgzcw.com/",														//2249815/bjop /mid/bjop
		"http://fenxi.zgzcw.com/",														//2249815/ypdb /mid/ypdb
		"http://saishi.zgzcw.com/soccer/league/", 										// lid 联赛球队排名数据获取
		"http://saishi.zgzcw.com/soccer/team/",											// tid 球队主页信息
		"http://cp.zgzcw.com/lottery/queryCPRL.action",									// 足球数据竞彩日历页面	 //?date=2018-01-01&length=90
		"http://fenxi.zgzcw.com/",														//2373979/bjop/zhishu?company_id=115&company=%E5%A8%81%E5%BB%89%E5%B8%8C%E5%B0%94
		"http://fenxi.zgzcw.com/",														//2373979/ypdb/zhishu?company_id=115&company=%E5%A8%81%E5%BB%89%E5%B8%8C%E5%B0%94
		"http://fenxi.zgzcw.com/", 														//2282960/bsls
		"http://saishi.zgzcw.com/soccer/",												//数据主页面
		"http://saishi.zgzcw.com/soccer/",												//数据主页面
		"http://live.zgzcw.com/",														//实时数据页面   //ls/AllData.action
		"http://live.zgzcw.com/",														//实时数据页面   //ls/AllData.action
	};
	
	/** 页面的类型. */
	static final String[] PAGE_TYPES = 
	{
		"season",
		"league",
		"cup",
		SoccerConstants.LOTTERY_BD,
		SoccerConstants.LOTTERY_JC,
		SoccerConstants.LOTTERY_ZC,
		"op",
		"yp",
		"rank",
		"team",
		"calendar",
		"opzhishu",
		"ypzhishu",
		"history",
		"center",
		"leaguem",
		"live",							//竞彩
	};
	
	/** 通用的页面编码 */
	protected static String encoding = Downloader.ENCODING_UTF8;
	
	/**
	 * Create a new instance of WebPageCreator.
	 */
	private ZgzcwWebPageCreator()
	{
		encoding = Downloader.ENCODING_UTF8;
	}
	
	/**
	 * 数据主页面
	 * 
	 * @return
	 */
	public static ZgzcwCenterPage createZgzcwMainPage()
	{
		int pageType = 14;
		ZgzcwCenterPage page = new ZgzcwCenterPage();
		setBasicParams(page, pageType);
		page.setUrl(PAGE_URLS[pageType]);
		return page;
	}
	
	/**
	 * 创建赛季信息下载页面
	 * 
	 * @param lid 联赛编号
	 * @param type 联赛类型
	 * @param season 赛季编号
	 * @return 赛季信息下载页面
	 */
	public static SeasonWebPage createSeasonWebPage(String lid, String type, String season)
	{
		int pageType = 0;
		SeasonWebPage page = new SeasonWebPage();
		setBasicParams(page, pageType);
		page.setLid(lid);
		page.setType(type);
		page.setSeason(season);
		
		String url = PAGE_URLS[pageType] + type + RIGHT_SPASH + lid;
		if(StringUtils.isNotEmpty(season))
		{
			url += RIGHT_SPASH + season;
		}
		page.setUrl(url);
		
		return page;
	}
	
	/**
	 * 创建联赛比赛下载 页面
	 * 
	 * @param lid 联赛编号
	 * @param season 赛季编号
	 * @param round 比赛轮次
	 * @return 联赛比赛下载页面
	 */
	public static RoundLeagueWebPage createRoundLeagueWebPage(String lid, String season, String round)
	{
		int pageType = 1;
		RoundLeagueWebPage page = new RoundLeagueWebPage();
		setBasicParams(page, pageType);
		page.setLid(lid);
		page.setSeason(season);
		page.setRound(round);
		page.setRacetype(SoccerConstants.MATCH_TYPE_LEAGUE);
		page.setUrl(PAGE_URLS[pageType]);
		page.setMethod(WebPage.HTTP_METHOD_POST);
		
		return page;
	}

	/**
	 * 创建杯赛比赛下载页面
	 * 
	 * @param lid 联赛编号
	 * @param season 赛季编号
	 * @return 杯赛下载页面
	 */
	public static RoundCupWebPage createRoundCupWebpage(String lid, String season)
	{
		int pageType = 2;
		RoundCupWebPage page = new RoundCupWebPage();
		setBasicParams(page, pageType);
		page.setLid(lid);
		page.setSeason(season);
		page.setRacetype(SoccerConstants.MATCH_TYPE_CUP);

		String url = PAGE_URLS[pageType] + SoccerConstants.MATCH_TYPE_CUP + RIGHT_SPASH + lid;
		if(StringUtils.isNotEmpty(season))
		{
			url += RIGHT_SPASH + season;
		}
		page.setUrl(url);
		
		return page;
	}
	
	/**
	 * 创建北单下载页面
	 * 
	 * @param issue 北单下载期号
	 * @return 北单下载页面
	 */
	public static LotteryWebPage createBdWebPage(String issue)
	{
		int pageType = 3;
		LotteryWebPage page = new LotteryWebPage();
		setBasicParams(page, pageType);
		page.setIssue(issue);
		page.setIssuetype(PAGE_TYPES[pageType]);
		
		String url = PAGE_URLS[pageType];
		//非默认的北单期号
		if(StringUtils.isNotEmpty(issue))
		{
			url += "&issue=" + issue;
		}
		page.setUrl(url);
		
		return page;
	}
	
	/**
	 * 创建竞彩足球下载页面
	 * 
	 * @param issue 期号
	 * @return 竞彩足球下载页面
	 */
	public static LotteryWebPage createJcWebPage(String issue)
	{
		int pageType = 4;
		LotteryWebPage page = new LotteryWebPage();
		setBasicParams(page, pageType);
		page.setIssue(issue);
		page.setIssuetype(PAGE_TYPES[pageType]);
		
		String url = PAGE_URLS[pageType];
		//非默认的竞彩足球期号
		if(StringUtils.isNotEmpty(issue))
		{
			url += "&issue=" + issue;
		}
		page.setUrl(url);
		
		return page;
	}

	/**
	 * 创建足彩下载页面
	 * 
	 * @param issue 足彩期号
	 * @return 足彩期号
	 */
	public static LotteryWebPage createZcWebPage(String issue)
	{
		int pageType = 5;
		LotteryWebPage page = new LotteryWebPage();
		setBasicParams(page, pageType);
		page.setIssue(issue);
		page.setIssuetype(PAGE_TYPES[pageType]);
		
		String url = PAGE_URLS[pageType] + "&issue=" + issue + "&v=" + DateUtil.getCurDayStr();
		page.setUrl(url);
		
		return page;
	}

	/**
	 * 创建欧赔数据下载页面
	 * 
	 * @param mid 比赛编号
	 * @return 欧赔数据下载页面
	 */
	public static OddsOpWebPage createOddsOpWebPage(String mid)
	{
		int pageType = 6;
		OddsOpWebPage page = new OddsOpWebPage();
		setBasicParams(page, pageType);
		page.setMid(mid);
		String url = PAGE_URLS[pageType] + mid + RIGHT_SPASH + "bjop";
		page.setUrl(url);		
		
		return page;
	}

	/**
	 * 创建亚赔数据下载页面
	 * 
	 * @param mid 比赛编号
	 * @return 亚盘数据下载页面
	 */
	public static OddsYpWebPage createOddsYpWebPage(String mid)
	{
		int pageType = 7;
		OddsYpWebPage page = new OddsYpWebPage();
		setBasicParams(page, pageType);
		page.setMid(mid);
		String url = PAGE_URLS[pageType] + mid + RIGHT_SPASH + "ypdb";
		page.setUrl(url);	
		
		return page;
	}
	
	/**
	 * 创建联赛排名数据下载页面
	 * 
	 * @param mid 比赛编号
	 * @return 亚盘数据下载页面
	 */
	public static RankWebPage createRankWebPage(String lid)
	{
		int pageType = 8;
		RankWebPage page = new RankWebPage();
		setBasicParams(page, pageType);
		page.setLid(lid);
		String url = PAGE_URLS[pageType] + lid;
		page.setUrl(url);	
		
		return page;
	}
	
	/**
	 * 创建联赛排名数据下载页面
	 * 
	 * @param mid 比赛编号
	 * @return 亚盘数据下载页面
	 */
	public static TeamWebPage createTeamWebPage(String tid)
	{
		int pageType = 9;
		TeamWebPage page = new TeamWebPage();
		setBasicParams(page, pageType);
		page.setTid(tid);;
		String url = PAGE_URLS[pageType] + tid;
		page.setUrl(url);	
		
		return page;
	}
	
	/**
	 * 创建足彩数据日历下载页面
	 * 
	 * @param startdate
	 * @param daynum
	 * @return
	 */
	public static LotteryCalendarWebPage createLotteryCalendarWebPage(String startdate, int daynum)
	{
		int pageType = 10;
		LotteryCalendarWebPage page = new LotteryCalendarWebPage();
		setBasicParams(page, pageType);
		page.setStartdate(startdate);
		page.setNum(daynum);
		
		String url = PAGE_URLS[pageType] + "?date=" + startdate + "&length=" + daynum;
		page.setUrl(url);
		
		return page;
	}
	
	/**
	 * 欧赔指数数据下载
	 * 
	 * @param mid 比赛编号
	 * @param gid 博彩公司编号
	 * @param gname 博彩公司名称
	 * @return 页面
	 */
	public static OddsOpZhishuWebPage createOddsOpZhishuWebPage(String mid, String gid, String gname)
	{
		int pageType = 11;
		OddsOpZhishuWebPage page = new OddsOpZhishuWebPage();
		setBasicParams(page, pageType);
		page.setMid(mid);
		page.setGid(gid);
		page.setGname(gname);
		try
		{
			String url = PAGE_URLS[pageType] + mid + "/bjop/zhishu?company_id=" + 
					gid + "&company=" + URLEncoder.encode(gname, "utf-8");
			page.setUrl(url);
		}
		catch (Exception e)
		{
		}
		
		return page;
	}
	
	/**
	 * 欧赔指数数据下载
	 * 
	 * @param mid 比赛编号
	 * @param gid 博彩公司编号
	 * @param gname 博彩公司名称
	 * @return 页面
	 */
	public static OddsYpZhishuWebPage createOddsYpZhishuWebPage(String mid, String gid, String gname)
	{
		int pageType = 12;
		OddsYpZhishuWebPage page = new OddsYpZhishuWebPage();
		setBasicParams(page, pageType);
		page.setMid(mid);
		page.setGid(gid);
		page.setGname(gname);
		try
		{
			String url = PAGE_URLS[pageType] + mid + "/ypdb/zhishu?company_id=" + 
					gid + "&company=" + URLEncoder.encode(gname, "utf-8");
			page.setUrl(url);
		}
		catch (Exception e)
		{
		}		
		return page;
	}
	
	/**
	 * 创建比赛历史下载页面
	 * 
	 * @param mid 比赛编号
	 * @return 下载页面
	 */
	public static MatchHistoryWebPage createMatchHistoryWebPage(String mid)
	{
		int pageType = 13;
		MatchHistoryWebPage page = new MatchHistoryWebPage();
		setBasicParams(page, pageType);
		page.setMid(mid);
		String url = PAGE_URLS[pageType] + mid + "/bsls";
		page.setUrl(url);
		
		return page;
	}
	
	/**
	 * 创建联赛主页下载页面
	 * @param lid
	 * @param leagueType
	 * @return
	 */
	public static LeagueWebPage createLeagueWebPage(String lid, String leagueType)
	{
		int pageType = 14;
		LeagueWebPage page = new LeagueWebPage();
		setBasicParams(page, pageType);
		page.setLid(lid);
		page.setLeaguetype(leagueType);
		String url = PAGE_URLS[pageType] + leagueType + "/" + lid + "/";
		page.setUrl(url);
		
		return page;
	}
	
	/**
	 * 创建页面
	 * @param issue
	 * @return
	 */
	public static LiveWebPage createLiveWebPage(String type)
	{
		int pageType = 16;
		LiveWebPage page = new LiveWebPage();
		setBasicParams(page, pageType);
		String url =PAGE_URLS[pageType] + type + "/";
		page.setUrl(url);
		page.setLotterytype(type);
		return page;
	}
	
	/**
	 * 
	 * @param issue
	 * @param type
	 * @return
	 */
	public static LiveWebPage createLiveIssueWebPage(String issue, String type)
	{
		int pageType = 16;
		LiveWebPage page = new LiveWebPage();
		page.setHasMoreHeader(true);
		setBasicParams(page, pageType);
		String url = PAGE_URLS[pageType] + "ls/AllData.action";
		page.setUrl(url);
		page.setMethod(WebPage.HTTP_METHOD_POST);
		page.addHeader(ZGZCW_HEADER_REFERER, PAGE_URLS[pageType]);
		page.addParam("code", "201");
		page.addParam("ajax", "true");
		page.addParam("date", issue);		
		return page;
	}

	/**
	 * 基本信息，共同具有的特征
	 * 
	 * @param page
	 */
	protected static void setBasicParams(WebPage page, int typeIndex)
	{
		page.setEncoding(encoding);
		page.setType(PAGE_TYPES[typeIndex]);
		page.setCreatetime(DateUtil.getCurTimeStr());
	}

	/**
	 * 数据编码，一般同一网站的数据编码规则一致
	 * 
	 * @return 编码类型
	 */
	public static String getEncoding()
	{
		return encoding;
	}

	/**
	 * 设置编码类型
	 * 
	 * @param encoding 编码
	 */
	public static void setEncoding(String encoding1)
	{
		encoding = encoding1;
	}
}
