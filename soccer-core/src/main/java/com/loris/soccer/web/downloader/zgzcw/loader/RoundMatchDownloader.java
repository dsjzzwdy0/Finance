package com.loris.soccer.web.downloader.zgzcw.loader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.loris.base.context.LorisContext;
import com.loris.base.util.ArraysUtil;
import com.loris.base.util.ArraysUtil.EqualChecker;
import com.loris.base.util.DateUtil;
import com.loris.base.web.http.UrlFetchException;
import com.loris.base.web.http.UrlFetcher;
import com.loris.base.web.http.WebClientFetcher;
import com.loris.base.web.manager.TaskMode;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.data.table.league.League;
import com.loris.soccer.bean.data.table.league.Match;
import com.loris.soccer.bean.data.table.league.Round;
import com.loris.soccer.bean.data.table.lottery.JcMatch;
import com.loris.soccer.bean.model.LeagueMap;
import com.loris.soccer.bean.model.LeagueSeason;
import com.loris.soccer.bean.type.LeagueType;
import com.loris.soccer.repository.SoccerManager;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwSoccerDownloader;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwWebPageCreator;
import com.loris.soccer.web.downloader.zgzcw.page.LeagueWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.RoundCupWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.RoundLeagueWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.ZgzcwCenterPage;
import com.loris.soccer.web.downloader.zgzcw.parser.RoundCupWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.RoundLeagueWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.ZgzcwCenterParser;

/**
 * 比赛数据下载管理器，该下载管理器包括有以下几方面的数据下载与更新：<br/>
 * 1、赛事中心数据页面，该页面下载联赛名称、编号等<br/>
 * 2、联赛中心数据页面，该页面下载联赛的赛季数据（下载前判断哪些数据不需要下载）<br/>
 * 3、联赛赛季各轮次数据下载页面（下载前判断哪些数据不需要下载）<br/>
 * 4、每一轮次数据下载页面（下载前判断哪些数据不需要下载）<br/>
 * 
 * @author jiean
 *
 */
public class RoundMatchDownloader extends ZgzcwSoccerDownloader
{
	private static Logger logger = Logger.getLogger(RoundMatchDownloader.class);
	
	/** 联赛数据 */
	private LeagueMap leagueMap = null;
	
	/** 开始年份 */
	private String startYear = "2017";
	
	/** 比赛轮次 */
	private List<Round> rounds = null;
	
	/** The fetcher.  */
	protected WebClientFetcher fetcher = null;
	
	/** 是否需要首页面 */
	protected boolean needDownCenterPage = true;
	
	/** 赛事中心数据下载页面的最小间隔 */
	protected int centerDayInterval = 100;
	
	/** 联赛中心数据下载页面最小间隔 */
	protected int leagueDayInterval = 30;
	
	/** 轮次数据下载页面最小间隔 */
	protected int roundDayinterval = 30;
	
	/** Use WebClient to Download. */
	protected boolean useWebClient = true;
	
	/** 主页与其它页之间等待的毫秒数 */
	protected long timeToWait = 2000;
	
	/** 已经下载的数据 */
	protected List<RoundLeagueWebPage> initPages = new ArrayList<>();
	
	/** 数据比较器 */
	protected Comparator<RoundLeagueWebPage> comparator = new Comparator<RoundLeagueWebPage>()
	{
		@Override
		public int compare(RoundLeagueWebPage page1, RoundLeagueWebPage page2)
		{
			String loadTime0 = page1.getLoadtime();
			String loadTime1 = page2.getLoadtime();
			
			//下载时间大于最后的时间
			return DateUtil.compareDateString(loadTime0, loadTime1);
		}
	};
	
	/**
	 * Create a new instance of RoundMatchDownloader.
	 */
	public RoundMatchDownloader()
	{
		super();
		taskMode = TaskMode.Single;
	}
	
	/**
	 * Set the LorisContext.
	 * @param context LorisContext
	 */
	@Override
	public void setLorisContext(LorisContext context)
	{
		super.setLorisContext(context);
		leagueMap = SoccerManager.getLeagueMap();
	}
	
	
	/**
	 * 下载数据准备
	 */
	@Override
	public boolean prepare()
	{
		if (soccerManager == null || soccerWebPageManager == null)
		{
			logger.info("RoundMatchDownloader is not initialized, stop.");
			return false;
		}
		
		if(StringUtils.isEmpty(start) || StringUtils.isEmpty(end))
		{
			logger.info("RoundMatchDownloader start or end time is not set, please set the date first. stop.");
			return false;
		}
		
		//处理中心页面数据下载
		if(needDownCenterPage && isCenterPageNeedToDownload(centerDayInterval))
		{			
			downloadCenterPage();
		}		
		
		if(leagueMap == null)
		{
			//赛事数据列表
			leagueMap = SoccerManager.getLeagueMap();
		}
		
		//检查所有赛事的主页是否需要下载
		checkLeagueMainPage();
		
		//检测赛事轮次数据下载
		//checkRoundPage();
		
		checkLeagueRoundPages();
		
		checkCupPages();
		
		/*
		List<Round> rounds = soccerManager.getRounds(start, end, true);
		League league = null;
		
		List<Round> needToDownloadRounds = new ArrayList<>();
		
		
		//计算需要下载的轮次数据
		for (Round round : rounds)
		{			
			//查找联赛信息
			league = getLeague(round.getLid());			
			//logger.info(round + ": " + league);
			if(league == null)
			{
				continue;
			}
			
			//杯赛
			if(SoccerConstants.MATCH_TYPE_CUP.equals(league.getType()))
			{
				if(!isCupRoundInDownloadPages(needToDownloadRounds, round))
				{
					needToDownloadRounds.add(round);
				}
			}
			else
			{
				needToDownloadRounds.add(round);
			}
		}
		

		List<RoundLeagueWebPage> downloadedRoundLeaguePagesadedRoundLeaguePages = soccerWebPageManager.getDownloadedLeagueRounds();
		List<RoundCupWebPage> downloadedRoundCupPages = soccerWebPageManager.getDownloadedCupPages();
		Date date = new Date();
		
		//计算
		WebPage page = null;
		for (Round round : needToDownloadRounds)
		{
			//查找联赛信息
			league = getLeague(round.getLid());			
			if(league == null)
			{
				continue;
			}
			
			//杯赛
			if(SoccerConstants.MATCH_TYPE_CUP.equals(league.getType()))
			{
				if(isDownloadedCupRound(downloadedRoundCupPages, round, date))
				{
					continue;
				}
				page = creator.createRoundCupWebpage(round.getLid(), round.getSeason());
				pages.put(page);
			}
			//联赛
			else if(SoccerConstants.MATCH_TYPE_LEAGUE.equals(league.getType()))
			{
				if(isDownloadedLeagueRound(downloadedRoundLeaguePagesadedRoundLeaguePages, round))
				{
					continue;
				}
				page = creator.createRoundLeagueWebPage(round.getLid(), round.getSeason(), round.getName());
				pages.put(page);
			}
		}*/
		
		//totalSize = rounds.size();
		//totalSize = pages.length();
		//logger.info("There are total " + totalSize + " pages and there are " + leftSize() + " pages lefted to be downloaded.");
		return true;
	}
	
	/**
	 * 按照联赛比赛的轮次获取下载的数据
	 */
	public void checkLeagueRoundPages()
	{
		rounds = soccerManager.getRounds(startYear, start, end, true, true);
		if(rounds == null || rounds.size() == 0)
		{
			return;
		}
		
		ArraysUtil.EqualChecker<Round> checker = new EqualChecker<Round>()
		{			
			@Override
			public boolean isSameObject(Round round)
			{
				League league = leagueMap.getLeague(round.getLid());
				if(league == null)
				{
					return false;
				}
				if(SoccerConstants.MATCH_TYPE_LEAGUE.equalsIgnoreCase(league.getType()))
				{
					return true;
				}
				return false;
			}
		};
		
		List<Round> leagueRounds = new ArrayList<>();
		int size = ArraysUtil.getListValues(rounds, leagueRounds, checker);
		if(size <= 0)
		{
			return;
		}
		
		WebPage page = null;
		Date date = new Date();
		
		/** 已经下载的数据  */
		List<RoundLeagueWebPage> downloadedRoundLeaguePages = soccerWebPageManager.getDownloadedLeagueRounds();
		
		for (Round round : rounds)
		{
			if(isDownloadedLeagueRound(downloadedRoundLeaguePages, round, date))
			{
				continue;
			}
			page = ZgzcwWebPageCreator.createRoundLeagueWebPage(round.getLid(), round.getSeason(), round.getName());
			pages.put(page);
		}
	}
	
	/**
	 * 检查杯赛的数据
	 */
	public void checkCupPages()
	{
		List<JcMatch> jcMatchs = soccerManager.getJcMatchesByDate(start, end);
		List<String> cupLeagues = new ArrayList<>();
		
		for (JcMatch match : jcMatchs)
		{
			String lid = match.getLid();
			if(StringUtils.isEmpty(lid))
			{
				continue;
			}
			if(!cupLeagues.contains(lid))
			{
				cupLeagues.add(lid);
			}
		}
		
		League league = null;
		WebPage page = null;
		//int i = 1;
		for (String lid : cupLeagues)
		{
			//logger.info(i +++ ": " + leagueMap.getLeague(lid));
			league = leagueMap.getLeague(lid);
			if(league != null && league.getLeagueType() == LeagueType.CUP)
			{
				page = ZgzcwWebPageCreator.createRoundCupWebpage(lid, null);
				pages.put(page);
			}			
		}
	}
	
	
	/**
	 * 检测赛事轮次数据是否是最新的，需要更新与下载
	 * 
	 */
	public void checkRoundPage()
	{
		rounds = soccerManager.getRounds(startYear, start, end, true, true);
		
		if(rounds == null || rounds.size() == 0)
		{
			return;
		}
		
		/** 已经下载的数据  */
		List<RoundLeagueWebPage> downloadedRoundLeaguePages = soccerWebPageManager.getDownloadedLeagueRounds();
		List<RoundCupWebPage> downloadedRoundCupPages = soccerWebPageManager.getDownloadedCupPages();
		
		List<Round> cupRoundsInPages = new ArrayList<>();
		Date date = new Date();
		
		//int i = 1;
		for (Round round : rounds)
		{
			League league = leagueMap.getLeague(round.getLid());
			if(league == null)
			{
				continue;
			}
			//logger.info(i +++ ": " + league + "=>" + round);
			
			WebPage page = null;
			//杯赛
			if(SoccerConstants.MATCH_TYPE_CUP.equals(league.getType()))
			{
				if(existCupRoundDownload(cupRoundsInPages, round))
				{
					//logger.info("Exist in the download list: " + round);
					continue;
				}
				
				if(isDownloadedCupRound(downloadedRoundCupPages, round, date))
				{
					//logger.info("Has downloaded round: " + round);
					continue;
				}
				cupRoundsInPages.add(round);
				page = ZgzcwWebPageCreator.createRoundCupWebpage(round.getLid(), round.getSeason());
				pages.put(page);
			}
			//联赛
			else if(SoccerConstants.MATCH_TYPE_LEAGUE.equals(league.getType()))
			{
				if(isDownloadedLeagueRound(downloadedRoundLeaguePages, round, date))
				{
					continue;
				}
				page = ZgzcwWebPageCreator.createRoundLeagueWebPage(round.getLid(), round.getSeason(), round.getName());
				pages.put(page);
			}
		}
	}
	
	/**
	 * Download the WebPage.
	 * 
	 * @param page The WebPage
	 * @return The download flag.
	 */
	@Override
	public boolean download(WebPage page) throws UrlFetchException
	{
		if(useWebClient && (page instanceof RoundLeagueWebPage))
		{
			RoundLeagueWebPage page2 = (RoundLeagueWebPage) page;
			if(!initialized(page2))
			{
				initPages.add(page2);
				try
				{
					String baseUrl = page2.getParentURL();
					logger.info("Download Parent URL: " + baseUrl);
					
					if(fetcher == null)
					{
						WebPage p = new WebPage();
						p.setUrl(page2.getParentURL());
						fetcher = WebClientFetcher.createFetcher(p);
					}
					else
					{	
						HtmlPage p0 = fetcher.fetch(baseUrl);			
						if(p0.getWebResponse().getStatusCode() != 200)
						{
							return false;
						}						
					}
					
					Thread.sleep(timeToWait);
				}
				catch (FailingHttpStatusCodeException e)
				{					
					int statusCode = e.getStatusCode();
					if(statusCode == 503)
					{
						e.printStackTrace();
						page.setHttpstatus(HttpStatus.SC_FORBIDDEN);
						return false;
					}					
				}
				catch(Exception e)
				{
					e.printStackTrace();
					return false;
				}
			}
			
			return fetcher.fetch(page2);
		}
		else
		{
			return UrlFetcher.fetch(page);
		}
	}
	
	/**
	 * Check if the page has been downloaded first.
	 * @param page The Round League Page
	 * @return The flag.
	 */
	protected boolean initialized(RoundLeagueWebPage page)
	{
		for (RoundLeagueWebPage p : initPages)
		{
			if(p.getLid().equals(page.getLid()) && p.getSeason().equals(page.getSeason()))
			{
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 检查所有赛事（联赛、杯赛）数据是否需要下载该主页<br/>
	 * 该主页包含的信息有：赛季、轮次信息、参与该项赛事的球队<br/>
	 * 只要最新的赛季已经有该信息，则不应该再下载了。<br/>
	 */
	public void checkLeagueMainPage()
	{
		Map<String, LeagueSeason> seasons = soccerManager.getSeasonsMap();
		for (String lid : leagueMap.keySet())
		{
			League league = leagueMap.getLeague(lid);
			LeagueSeason leagueSeason = seasons.get(league.getLid());
			if(isLeagueNeedToDownloaded(league, leagueSeason) && (!leagueMap.isNotActive(league.getLid())))
			{
				LeagueWebPage page = ZgzcwWebPageCreator.createLeagueWebPage(league.getLid(), league.getType());
				pages.put(page);
				logger.info("下载：" + league);
			}
		}
	}
	
	/**
	 * 检测是否需要该下载该联赛的赛季数据
	 * @param league 联赛
	 * @param leagueSeason 赛季
	 * @return
	 */
	protected boolean isLeagueNeedToDownloaded(League league, LeagueSeason leagueSeason)
	{
		if(leagueSeason == null)
		{
			return true;
		}
		return leagueSeason.hasLastSeason(new Date());		
	}
	
	
	/**
	 * 下载数据中心页面
	 */
	protected void downloadCenterPage()
	{
		ZgzcwCenterPage page = ZgzcwWebPageCreator.createZgzcwMainPage();
		logger.info("Downloading Zgzcw main page: " + page.getFullURL());
		try
		{
			if(download(page))
			{
				afterCenterPageDownloaded(page);
			}
		}
		catch(UrlFetchException exception)
		{
			logger.info(exception.toString());
		}
	}
	
	/**
	 * 数据中心页面下载后处理
	 * @param page 数据页面
	 */
	protected void afterCenterPageDownloaded(ZgzcwCenterPage page)
	{
		ZgzcwCenterParser parser = new ZgzcwCenterParser();
		if(parser.parseWebPage(page))
		{
			List<League> leagues = parser.getLeagues();
			
			synchronized(soccerManager)
			{
				soccerManager.addOrUpdateLeagues(leagues);
			}			
			soccerWebPageManager.addOrUpateZgzcwCenterPage(page);			
		}
	}
	
	/**
	 * After download the page.
	 * 
	 * @param page
	 *            Page Value.
	 * @param flag
	 *            The flag value.
	 */
	@Override
	public void afterDownload(WebPage page, boolean flag)
	{
		if (!flag)
		{
			// this will do nothing.
			return;
		}
		
		//检测数据是否正常
		if(!checkPageContent(page))
		{
			return;
		}
		
		List<Match> matchs = null;
		
		if(page instanceof RoundCupWebPage)
		{
			RoundCupWebPage page2 = (RoundCupWebPage)page;
			RoundCupWebPageParser parser = new RoundCupWebPageParser();
			parser.setSeasonInfo(page2.getSeason());
			
			logger.info("Parsing Cup Web Page: " + page2);
			if(parser.parseWebPage(page2))
			{
				matchs = parser.getMatches();
				
				logger.info("The Cup Web Page has " + matchs.size() + " matches.");
				
				List<Round> candidateRounds = parser.getRounds();
				
				List<Round> updateRounds = new ArrayList<>();
				for (Round round : candidateRounds)
				{
					Round round2 = getRound(round.getLid(), round.getSeason(), round.getName());
					if(round2 != null)
					{
						round2.setStarttime(round.getStarttime());
						round2.setEndtime(round.getEndtime());
						updateRounds.add(round2);
					}
					
					/*
					for (Round r : rounds)
					{
						if(r.equals(round))
						{
							r.setStarttime(round.getStarttime());
							r.setEndtime(round.getEndtime());							
							updateRounds.add(r);
							break;
						}
					}*/
				}
				
				if(updateRounds.size() > 0)
				{
					logger.info("Update Cup : " + page2 + ", Round:" + updateRounds);
					soccerManager.updateRounds(updateRounds);
				}
			}
			else
			{
				page2.setCompleted(false);
			}
			
			synchronized (soccerWebPageManager)
			{
				soccerWebPageManager.addOrUpdateRoundCupWebPage(page2);
			}
		}
		else if(page instanceof RoundLeagueWebPage)
		{
			RoundLeagueWebPage page2 = (RoundLeagueWebPage) page;
			RoundLeagueWebPageParser parser = new RoundLeagueWebPageParser();
			logger.info("Page: " + page);
			
			if(parser.parseWebPage(page2))
			{
				matchs = parser.getMatches();
				Round round = getRound(page2.getLid(), page2.getSeason(), page2.getRound());
				if(round != null)
				{
					for (Match match : matchs)
					{
						round.setMatchTime(match.getMatchtime());
					}
					
					logger.info("Update Round: " + round + ", Match size =" + matchs.size());
					soccerManager.addOrUpdateRound(round);
				}
			}
			else
			{
				page.setCompleted(false);
			}
			
			synchronized (soccerWebPageManager)
			{
				soccerWebPageManager.addOrUpdateRoundLeagueWebPage((RoundLeagueWebPage)page);
			}
		}
		
		if(matchs != null && matchs.size() > 0)
		{
			/*int i = 1;
			for (Match match : matchs)
			{
				logger.info(i +++ ": " + match);
			}*/
			soccerManager.addOrUpdateMatches(matchs);
		}		
		
		super.afterDownload(page, flag);
	}
	
	/**
	 * Get the Round from cache.
	 * @param lid Lid value.
	 * @param season Season
	 * @param round Round name
	 * @return Round objec.
	 */
	protected Round getRound(String lid, String season, String round)
	{
		for (Round r : rounds)
		{
			if(lid.equalsIgnoreCase(r.getLid()) 
				&& season.equalsIgnoreCase(r.getSeason())
				&& round.equals(r.getName()))
			{
				return r;
			}
		}
		return null;
	}
	
	/**
	 * 检测是否已经下载
	 * 
	 * @param downPages 已经下载的日期
	 * @param round 轮次数据
	 * @param curDate 今天的日期
	 * @return 是否已经下载
	 */
	protected boolean isDownloadedLeagueRound(List<RoundLeagueWebPage> downPages, Round round, Date curDate)
	{
		String roundEndTime = round.getEndtime();
		if(StringUtils.isEmpty(roundEndTime))
		{
			return false;
		}
		
		Date endTime = DateUtil.tryToParseDate(roundEndTime);
		if(endTime == null)
		{
			return false;
		}
		
		//页面检测器
		ArraysUtil.EqualChecker<RoundLeagueWebPage> checker = new ArraysUtil.EqualChecker<RoundLeagueWebPage>()
		{
			@Override
			public boolean isSameObject(RoundLeagueWebPage page)
			{
				return (page.getLid().equals(round.getLid()) && 
						page.getSeason().equals(round.getSeason()) && 
						page.getRound().equals(round.getName()));
			}
		};

		RoundLeagueWebPage lastLoadPage = ArraysUtil.getLastObject(downPages, checker, comparator);
		
		/*
		RoundLeagueWebPage lastLoadPage = null;
		for (RoundLeagueWebPage page : downPages)
		{
			if(page.getLid().equals(round.getLid()) && page.getSeason().equals(round.getSeason()) && 
					page.getRound().equals(round.getName()))
			{
				if(lastLoadPage == null)
				{
					lastLoadPage = page;
					continue;
				}
				
				String loadTime0 = lastLoadPage.getLoadtime();
				String loadTime1 = page.getLoadtime();
				
				//下载时间大于最后的时间
				int status = DateUtil.compareDateString(loadTime1, loadTime0);
				if(status > 0)
				{
					lastLoadPage = page;
				}
			}
		}*/
		
		//是否找到已经下载的数据
		if(lastLoadPage == null)
		{
			return false;
		}
		
		String pageLoadTime = lastLoadPage.getLoadtime();
		if(StringUtils.isEmpty(pageLoadTime))
		{
			return false;
		}
		
		Date loadTime = DateUtil.tryToParseDate(pageLoadTime);
		if(loadTime == null)
		{
			return false;
		}
		
		if(loadTime.compareTo(endTime) >= 0)
		{
			//logger.info(lastLoadPage + " d1: " + pageLoadTime + " > " + roundEndTime + ", need not to load.");
			return true;
		}
		
		int intDay = DateUtil.getDiscrepantDays(loadTime, curDate);
		if(intDay < 1)
		{
			return true;
		}		
		
		return false;
	}
	
	/**
	 * Check the round has exist in the download list.
	 * @param cupRounds cup rounds.
	 * @param round round
	 * @return The flag.
	 */
	protected boolean existCupRoundDownload(List<Round> cupRounds, Round round)
	{
		for (Round r : cupRounds)
		{
			if(r.getLid().equals(round.getLid()) && r.getSeason().equals(round.getSeason()))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 检测是否已经下载
	 * 
	 * @param round 轮次数据
	 * @return 是否已经下载
	 */
	protected boolean isDownloadedCupRound(List<RoundCupWebPage> downPages, Round round, Date date)
	{
		for (RoundCupWebPage page : downPages)
		{
			if(page.getLid().equals(round.getLid()) && page.getSeason().equals(round.getSeason()))
			{
				Date d1 = DateUtil.tryToParseDate(page.getLoadtime());
				if(d1 != null)
				{
					int num = DateUtil.getDiscrepantDays(d1, date);
					if(num <= 1)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 检测是否该杯赛的数据已经在下开列表中
	 * 
	 * @param lid 联赛编号
	 * @return 是否已经包含在下载列表中
	 */
	protected boolean isCupRoundInDownloadPages(List<Round> downRounds, Round round)
	{
		for (Round r : downRounds)
		{
			if(r.getLid().equals(round.getLid()) && r.getSeason().equals(round.getSeason()))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获得最近一次下载的主页数据
	 * @return 数据下载的页面
	 */
	protected ZgzcwCenterPage getLastCenterPage()
	{
		List<ZgzcwCenterPage> centerPages = soccerWebPageManager.getZgzcwCenterPages();
		if(centerPages != null && centerPages.size() > 0)
		{
			return centerPages.get(0);
		}
		return null;
	}
	
	
	/**
	 * 测试是否主页需要进行下载
	 * @param centerIntervalDay
	 * @return 检测是否需要下载的标志
	 */
	protected boolean isCenterPageNeedToDownload(int centerIntervalDay)
	{
		ZgzcwCenterPage page = getLastCenterPage();
		if(page == null)
		{
			return true;
		}
		Date date = new Date();
		Date d1 = DateUtil.tryToParseDate(page.getLoadtime());
		if(d1 != null)
		{
			int num = DateUtil.getDiscrepantDays(d1, date);
			if(num < centerIntervalDay)
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 查找联赛信息
	 * 
	 * @param lid
	 * @return
	 */
	protected League getLeague(String lid)
	{
		return leagueMap.getLeague(lid);
	}

	public int getCenterDayInterval()
	{
		return centerDayInterval;
	}

	public void setCenterDayInterval(int centerDayInterval)
	{
		this.centerDayInterval = centerDayInterval;
	}

	public int getLeagueDayInterval()
	{
		return leagueDayInterval;
	}

	public void setLeagueDayInterval(int leagueDayInterval)
	{
		this.leagueDayInterval = leagueDayInterval;
	}

	public int getRoundDayinterval()
	{
		return roundDayinterval;
	}

	public void setRoundDayinterval(int roundDayinterval)
	{
		this.roundDayinterval = roundDayinterval;
	}
	
	public boolean isUseWebClient()
	{
		return useWebClient;
	}

	public void setUseWebClient(boolean useWebClient)
	{
		this.useWebClient = useWebClient;
	}

	public long getTimeToWait()
	{
		return timeToWait;
	}

	public void setTimeToWait(long timeToWait)
	{
		this.timeToWait = timeToWait;
	}

	public String getStartYear()
	{
		return startYear;
	}

	public void setStartYear(String startYear)
	{
		this.startYear = startYear;
	}

	/**
	 * Close the downloader.
	 */
	@Override
	public void close() throws IOException
	{
		try
		{
			if(fetcher != null)
			{
				fetcher.close();
				fetcher = null;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		super.close();
		
	}
}
