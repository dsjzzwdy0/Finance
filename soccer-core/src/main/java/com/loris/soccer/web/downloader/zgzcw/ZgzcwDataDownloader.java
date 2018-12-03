package com.loris.soccer.web.downloader.zgzcw;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.bean.wrapper.Result;
import com.loris.base.context.LorisContext;
import com.loris.base.util.ArraysUtil;
import com.loris.base.util.DateUtil;
import com.loris.base.web.http.UrlFetchException;
import com.loris.base.web.http.UrlFetcher;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.analysis.checker.IssueMatchChecker;
import com.loris.soccer.bean.data.table.BdMatch;
import com.loris.soccer.bean.data.table.JcMatch;
import com.loris.soccer.bean.data.table.League;
import com.loris.soccer.bean.data.table.Op;
import com.loris.soccer.bean.data.table.Yp;
import com.loris.soccer.bean.item.MatchItem;
import com.loris.soccer.bean.model.MatchList;
import com.loris.soccer.repository.SoccerManager;
import com.loris.soccer.web.downloader.zgzcw.page.LiveWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.LotteryWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsOpWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsYpWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.ZgzcwCenterPage;
import com.loris.soccer.web.downloader.zgzcw.parser.LiveBdWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.LiveIssueJcWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.LiveJcWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.LotteryBdWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.LotteryJcWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.OddsOpWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.OddsYpWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.ZgzcwCenterParser;
import com.loris.soccer.web.repository.SoccerWebPageManager;
import com.loris.soccer.web.task.MatchWebTask;
import com.loris.soccer.web.task.MatchWebTask.MatchWebTaskType;

/**
 * 竞彩网比赛数据下载管理器
 * @author deng
 *
 */
public class ZgzcwDataDownloader
{
	private static Logger logger = Logger.getLogger(ZgzcwDataDownloader.class);
	
	/** The processor. */
	protected static ZgzcwWebPageProcessor processor = null;
	
	/** Singleton instance. */
	private static ZgzcwDataDownloader instance;
	
	/** SoccerManager. */
	protected static SoccerManager soccerManager;
	
	/** SoccerWebPageManager. */
	protected static SoccerWebPageManager soccerWebPageManager;
	
	/**
	 * Create a new instance. This will not be created by other.
	 */
	private ZgzcwDataDownloader()
	{
	}
	
	/**
	 * 数据下载管理器
	 * @return
	 */
	public static ZgzcwDataDownloader getInstance()
	{
		if(instance == null)
		{
			instance = new ZgzcwDataDownloader();
		}
		return instance;
	}
	
	/**
	 * 初始化数据下载器
	 * @param context
	 */
	public static void initialize(LorisContext context)
	{
		soccerManager = context.getBean(SoccerManager.class);
		soccerWebPageManager = context.getBean(SoccerWebPageManager.class);
		
		processor = new ZgzcwWebPageProcessor(soccerManager, soccerWebPageManager);
	}
	
	/**
	 * 下载数据中心页面
	 * @return 联赛数据
	 * @throws UrlFetchException
	 */
	public static Result downloadZgzcwCenterPage() throws UrlFetchException
	{
		ZgzcwCenterPage page = ZgzcwWebPageCreator.createZgzcwMainPage();
		if(!download(page))
		{
			return null;
		}
		ZgzcwCenterParser parser = new ZgzcwCenterParser();
		if(parser.parseWebPage(page))
		{
			List<League> leagues = parser.getLeagues();
			soccerManager.addOrUpdateLeagues(leagues);
			
			Result result = new Result();
			result.put("league", leagues);
			return result;
		}
		return null;
	}
	
	/**
	 * 下载比赛的数据任务
	 * @param task 任务
	 * @return 是否完成的标志
	 */
	public static Result downloadMatchTask(MatchWebTask task) throws UrlFetchException
	{
		return downloadMatchOdds(task.getMatch(), task.getType());
	}
	
	/**
	 * 下载赔率数据
	 * @param match
	 * @param oddsType
	 * @return
	 * @throws UrlFetchException
	 */
	public static Result downloadMatchOdds(MatchItem match, MatchWebTaskType oddsType) throws UrlFetchException
	{
		if(match == null)
		{
			return null;
		}
		if(oddsType == MatchWebTaskType.Op)
		{
			OddsOpWebPage page = ZgzcwWebPageCreator.createOddsOpWebPage(match.getMid());
			
			if(download(page))
			{
				return processOddsOpWebPage(match, page);
			}
		}
		else if(oddsType == MatchWebTaskType.Yp)
		{
			OddsYpWebPage page = ZgzcwWebPageCreator.createOddsYpWebPage(match.getMid());
			if(download(page))
			{
				return processOddsYpWebPage(match, page);
			}
		}
		return null;
	}
	
	
	/**
	 * 下载竞彩数据集
	 * @param date 日期
	 * @return 下载的数据集
	 */
	public static Result downloadLatestMatch(String type) throws UrlFetchException
	{
		LotteryWebPage page = null;
		if("bd".equalsIgnoreCase(type))
		{
			page = ZgzcwWebPageCreator.createBdWebPage("");
		}
		else if("jc".equalsIgnoreCase(type))
		{
			page = ZgzcwWebPageCreator.createJcWebPage("");
		}
		
		if(page == null)
		{
			throw new IllegalArgumentException("Error, please set the correct type and date first.");
		}
		
		if(download(page))
		{
			if("bd".equalsIgnoreCase(type))
			{
				return processBdWebPage(page);
			}
			else if("jc".equalsIgnoreCase(type))
			{
				return processJcWebPage(page);
			}
		}
		
		return null;
	}
	
	/**
	 * 下载北单数据的主页面
	 * @return
	 * @throws UrlFetchException
	 */
	public static Result downloadLiveBdWebPage() throws UrlFetchException
	{
		LiveWebPage page = ZgzcwWebPageCreator.createLiveWebPage("bd");
		if(download(page))
		{
			soccerWebPageManager.addOrUpdateLiveWebPage(page);
			
			LiveBdWebPageParser parser = new LiveBdWebPageParser();
			if(parser.parseWebPage(page))
			{
				List<BdMatch> matchs = parser.getMatches();
				soccerManager.addOrUpdateBdMatches(matchs);
				
				Result result = new Result("matches", new MatchList(matchs));
				result.put("current", parser.getCurrentIssue());
				result.put("issues", parser.getIssues());
				
				return result;
			}
		}		
		logger.info("Error when downloading or parsing WebPage: " + page);
		return null;
	}
	
	/**
	 * 下载北单数据的主页面
	 * @return
	 * @throws UrlFetchException
	 */
	public static Result downloadLiveJcWebPage() throws UrlFetchException
	{
		LiveWebPage page = ZgzcwWebPageCreator.createLiveWebPage("jz");
		if(download(page))
		{
			soccerWebPageManager.addOrUpdateLiveWebPage(page);
			
			LiveJcWebPageParser parser = new LiveJcWebPageParser();
			if(parser.parseWebPage(page))
			{
				List<JcMatch> matchs = parser.getMatches();
				soccerManager.addOrUpdateJcMatches(matchs);
				
				Result result = new Result("matches", new MatchList(matchs));
				result.put("current", parser.getCurrentIssue());
				result.put("issues", parser.getIssues());
				
				return result;
			}
		}		
		logger.info("Error when downloading or parsing WebPage: " + page);
		return null;
	}
	
	/**
	 * 下载及时竞彩数据
	 * @param issue
	 * @return
	 * @throws UrlFetchException
	 */
	public static Result downloadLiveJcWebPage(String issue) throws UrlFetchException
	{
		LiveWebPage page = ZgzcwWebPageCreator.createLiveIssueWebPage(issue, "jz");
		if(download(page))
		{
			soccerWebPageManager.addOrUpdateLiveWebPage(page);
			
			LiveIssueJcWebPageParser parser = new LiveIssueJcWebPageParser();
			parser.setCurrentIssue(issue);
			if(parser.parseWebPage(page))
			{
				List<JcMatch> matchs = parser.getMatches();				
				soccerManager.addOrUpdateJcMatches(matchs);
				return new Result("matches", new MatchList(matchs));
			}
		}
		
		logger.info("Error when downloading or parsing WebPage: " + page);
		return null;
	}
	
	/**
	 * 北单数据页面下载处理器
	 * @param page 数据页
	 * @return 返回是否成功的标志
	 */
	protected static Result processBdWebPage(LotteryWebPage page)
	{
		LotteryBdWebPageParser parser = new LotteryBdWebPageParser();
		if(parser.parseWebPage(page))
		{
			List<BdMatch> matchs = parser.getMatches();	
			
			//保留数据
			List<BdMatch> saveMatches = new ArrayList<>();
			IssueMatchChecker<BdMatch> checker = new IssueMatchChecker<>(DateUtil.getCurDayStr());			
			checker.setSame(false);			
			ArraysUtil.getListValues(matchs, saveMatches, checker);
			
			/*
			IssueMatchChecker<IssueMatch> checker2 = new IssueMatchChecker<>(DateUtil.getCurDayStr());	
			checker.setSame(true);
			ArraysUtil.getListValues(matchs, issueMatchs, checker2);
			logger.info("The issue '" + "' has " + issueMatchs.size() + " matches.");
			*/
			saveLotteryWebPage(page);
			
			soccerManager.addOrUpdateBdMatches(saveMatches);
			
			logger.info("Total BdMatch size is " + matchs.size());
			return new Result("matches", new MatchList(matchs));
		}
		return null;
	}
	
	/**
	 * 处理欧赔主页数据
	 * 
	 * @param page 欧赔主页数据
	 */
	protected static Result processOddsOpWebPage(MatchItem match, OddsOpWebPage page)
	{
		OddsOpWebPageParser parser = new OddsOpWebPageParser();
		if(!parser.parseWebPage(page))
		{
			logger.info("Error occured when parse " + page + ".");
			return null;
		}
		
		//解析得到欧赔数据
		List<Op> ops = parser.getOddsList();
		Result result = new Result("ops", ops);
		synchronized (soccerManager)
		{
			soccerWebPageManager.addOrUpdateOpWebPage(page);
			soccerManager.addNewOpList(page.getMid(), ops);
		}
		return result;
	}
	
	/**
	 * 处理Jc页面数据
	 * 
	 * @param page
	 * @return
	 */
	protected static Result processJcWebPage(LotteryWebPage page)
	{
		LotteryJcWebPageParser parser = new LotteryJcWebPageParser();
		if(parser.parseWebPage(page))
		{
			List<JcMatch> matchs = parser.getJcMatches();
			if(matchs == null || matchs.size() <= 0)
			{
				return null;
			}
			//保留数据
			IssueMatchChecker<JcMatch> checker = new IssueMatchChecker<>(DateUtil.getCurDayStr());
			List<JcMatch> saveMatches = new ArrayList<>();
			ArraysUtil.getListValues(matchs, saveMatches, checker);
			
			saveLotteryWebPage(page);
			
			soccerManager.addOrUpdateJcMatches(saveMatches);			
			logger.info("Total JcMatch size is " + matchs.size());
			return new Result("matchs", new MatchList(matchs));
		}
		return null;
	}
	
	/**
	 * 处理亚盘主页数据
	 * 
	 * @param page 亚盘主页数据
	 */
	protected static Result processOddsYpWebPage(MatchItem match, OddsYpWebPage page)
	{
		OddsYpWebPageParser parser = new OddsYpWebPageParser();
		
		if(match != null)
		{
			Date date = DateUtil.tryToParseDate(match.getMatchtime());
			parser.setMatchTime(date);
		}
		
		if(!parser.parseWebPage(page))
		{
			logger.info("Error occured when parser " + page + ".");
			return null;
		}
		
		List<Yp> yps = parser.getOddsList();
		synchronized (soccerManager)
		{
			soccerWebPageManager.addOrUpdateYpWebPage(page);
			soccerManager.addNewYpList(page.getMid(), yps);
		}
		return new Result("yps", yps);
	}
	
	/**
	 * 下载数据
	 * @param page
	 */
	protected static boolean download(WebPage page) throws UrlFetchException
	{
		return UrlFetcher.fetch(page);
	}
	
	/**
	 * 保存数据页面
	 * @param page
	 */
	protected static void saveLotteryWebPage(LotteryWebPage page)
	{
		try
		{
			soccerWebPageManager.addOrUpdateLotteryWebPage(page);
		}
		catch(Exception e)
		{
			logger.info(e.toString());
		}
	}
}
