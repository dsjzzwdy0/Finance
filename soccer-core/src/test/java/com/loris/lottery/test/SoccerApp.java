package com.loris.lottery.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.loris.base.bean.User;
import com.loris.base.bean.entity.Entity;
import com.loris.base.bean.wrapper.Result;
import com.loris.base.context.LorisContext;
import com.loris.base.data.Keys;
import com.loris.base.repository.BasicManager;
import com.loris.base.util.ArraysUtil;
import com.loris.base.util.DateUtil;
import com.loris.base.util.FileUtils;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.http.UrlFetcher;
import com.loris.base.web.http.WebClientFetcher;
import com.loris.base.web.manager.Downloader;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.analysis.data.MatchOpVariance;
import com.loris.soccer.analysis.data.MatchData;
import com.loris.soccer.analysis.data.MatchDoc;
import com.loris.soccer.analysis.data.MatchOdds;
import com.loris.soccer.analysis.pool.MatchDocLoader;
import com.loris.soccer.analysis.pool.MatchOddsPool;
import com.loris.soccer.analysis.stat.CorporateStat;
import com.loris.soccer.analysis.util.PossionUtil;
import com.loris.soccer.analysis.util.LeagueDataUtil;
import com.loris.soccer.analysis.util.OddsUtil;
import com.loris.soccer.analysis.util.TeamHistoryCalculator;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.data.table.BdMatch;
import com.loris.soccer.bean.data.table.CountryLogo;
import com.loris.soccer.bean.data.table.JcMatch;
import com.loris.soccer.bean.data.table.League;
import com.loris.soccer.bean.data.table.Match;
import com.loris.soccer.bean.data.table.Op;
import com.loris.soccer.bean.data.table.Rank;
import com.loris.soccer.bean.data.table.Round;
import com.loris.soccer.bean.data.table.Season;
import com.loris.soccer.bean.data.table.SeasonTeam;
import com.loris.soccer.bean.data.table.Team;
import com.loris.soccer.bean.data.table.Yp;
import com.loris.soccer.bean.data.table.ZcMatch;
import com.loris.soccer.bean.data.view.MatchInfo;
import com.loris.soccer.bean.data.view.RankInfo;
import com.loris.soccer.bean.item.IssueMatch;
import com.loris.soccer.bean.item.MatchItem;
import com.loris.soccer.bean.item.YpValue;
import com.loris.soccer.bean.model.IssueMatchMapping;
import com.loris.soccer.bean.model.MatchList;
import com.loris.soccer.bean.okooo.OkoooBdMatch;
import com.loris.soccer.bean.okooo.OkoooJcMatch;
import com.loris.soccer.bean.okooo.OkoooOp;
import com.loris.soccer.bean.okooo.OkoooYp;
import com.loris.soccer.bean.setting.CorpSetting;
import com.loris.soccer.bean.setting.Parameter;
import com.loris.soccer.bean.setting.Setting;
import com.loris.soccer.repository.RemoteSoccerManager;
import com.loris.soccer.repository.SettingManager;
import com.loris.soccer.repository.SoccerContext;
import com.loris.soccer.repository.SoccerManager;
import com.loris.soccer.web.downloader.okooo.OkoooDataDownloader;
import com.loris.soccer.web.downloader.okooo.OkoooPageCreator;
import com.loris.soccer.web.downloader.okooo.loader.OkoooDailyDownloader;
import com.loris.soccer.web.downloader.okooo.page.OkoooRequestHeaderWebPage;
import com.loris.soccer.web.downloader.okooo.page.OkoooWebPage;
import com.loris.soccer.web.downloader.okooo.parser.OkoooJcPageParser;
import com.loris.soccer.web.downloader.okooo.parser.LeaguePageParser;
import com.loris.soccer.web.downloader.okooo.parser.OddsOpChildParser;
import com.loris.soccer.web.downloader.okooo.parser.OddsOpPageParser;
import com.loris.soccer.web.downloader.okooo.parser.OddsYpChangeParser;
import com.loris.soccer.web.downloader.okooo.parser.OddsYpChildParser;
import com.loris.soccer.web.downloader.okooo.parser.OkoooBdPageParser;
import com.loris.soccer.web.downloader.okooo.parser.SoccerPageParser;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwDataDownloader;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwWebPageCreator;
import com.loris.soccer.web.downloader.zgzcw.loader.IssueMatchDownloader;
import com.loris.soccer.web.downloader.zgzcw.loader.MatchDataDownloader;
import com.loris.soccer.web.downloader.zgzcw.loader.LotteryCalendarDownloader;
import com.loris.soccer.web.downloader.zgzcw.loader.LotteryMatchDownloader;
import com.loris.soccer.web.downloader.zgzcw.loader.OddsDownloader;
import com.loris.soccer.web.downloader.zgzcw.loader.RoundMatchDownloader;
import com.loris.soccer.web.downloader.zgzcw.loader.SeasonDownloader;
import com.loris.soccer.web.downloader.zgzcw.loader.TeamDownloader;
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
import com.loris.soccer.web.downloader.zgzcw.parser.LotteryBdWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.LotteryJcWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.LotteryZcWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.MatchHistoryWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.OddsOpWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.OddsOpZhishuWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.OddsYpWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.OddsYpZhishuWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.RankWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.RoundCupWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.RoundLeagueWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.SeasonWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.TeamWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.ZgzcwCenterParser;
import com.loris.soccer.web.scheduler.DataUploadScheduler;

public class SoccerApp
{
	private static Logger logger = Logger.getLogger(SoccerApp.class);

	/** The Application Context. */
	static ClassPathXmlApplicationContext context;

	static String OKOOO_OP_BASE_URL = "http://www.okooo.com/soccer/match/";

	static class EqualChecker implements ArraysUtil.EqualChecker<Op>
	{
		protected String gid;

		@Override
		public boolean isSameObject(Op obj)
		{
			if (obj.getGid().equals(gid))
				return true;
			return false;
		}

	}

	/**
	 * Test main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		// System.out.println( "Hello World!" );
		try
		{
			LorisContext context = getLorisContext();
			// createSeasonDownloader(context);
			// testRoundLeagueDownloader(context);
			// testRankDownloader(context);
			// createSeasonDownloader(context);
			// testTeamDownloader(context);
			// testLotteryBdDownloader(context);
			// testLotteryJcDownloader(context);
			// testLotteryZcDownloader(context);
			// testRoundCupDownloader(context);
			// testOddsOpDownloader(context);
			// testOddsYpDownloader(context);
			// testLotteryCalendarDownloader(context);
			// testTeamWebPageDownloader(context);
			// testRoundDownloader(context);
			// testLotteryDownloader(context);
			// testSplitString(context);
			// testOkoooSoccerWebPage(context);
			// testOkoooLeagueWebPage(context);
			// testOkoooOpWebPage(context);
			// testNumberParser(context);
			// testOkoooSpfWebPage(context);
			// testOddsZhishuWebPage(context);
			// testOddsDownloader(context);
			// testMatchHistory(context);
			// testJcMatchData(context);
			// testOkoooYpData(context);
			// testSearchMatchHistory(context);
			// testOddsOpList(context);
			// testRegex(context);
			// testJcMatchCalculator(context);
			// testOkoooDailyDownloader(context);
			// testOkoooDownloader(context);
			// testL500WebPageDownloader(context);
			// testOddsCorpConfigure(context);
			// testJcMatchMapping(context);
			// testJcMatchDataVector(context);
			// testMainPageParser(context);

			// testRoundMatch(context);

			// testOpStat(context);

			// testOkoooBdMatch(context);
			// testRoundCupDownloader(context);
			// testOddsConversion(context);
			// testSetting(context);
			// testOpVariance(context);
			// testSeasonDownloader(context);
			// testLeagueUtil(context);
			// testIssueMatchDownloader(context);
			// testRoundMatchDownloader(context);

			// testRankInfo(context);
			// testOYCompare(context);

			// testOddsUtil(context);
			// testPossion(context);
			// testIssueLeagueMatches(context);

			// testTaskQueue(context);

			//testRemoteManager(context);
			
			//testUploadDataSchecduler(context);
			// testDownloadOkoooOpWebPage(context);
			testDownloadLiveJcWebPage(context);
			//testComputeCorpStat(context);
			//testOkoooChileYpParser(context);

			close();
			// context = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void testComputeCorpStat(LorisContext context) throws IOException
	{
		CorporateStat.initialize(context);
		
		String start = "2018-04-01";
		String end = "2018-11-30";
		
		CorporateStat.computeStat(start, end);
	}
	
	public static void testDownloadLiveJcWebPage(LorisContext context) throws Exception
	{
		ZgzcwDataDownloader.initialize(context);
		Result result = ZgzcwDataDownloader.downloadLiveJcWebPage("2018-12-06");
		
		if(result == null)
		{
			logger.info("Error when downloading BdMatches.");
			return;
		}
		
		MatchList list = (MatchList)result.get("matches");
		if(list == null || list.isEmpty())
		{
			logger.info("The MatchList is Null.");
			return;
		}
		
		logger.info("There are total: " + list.size() + " matches.");
		
		int i = 1;
		for (MatchItem matchItem : list)
		{
			logger.info(i +++ ": " + matchItem);
		}
	}
	
	public static void testDownloadLiveWebPage(LorisContext context) throws Exception
	{
		ZgzcwDataDownloader.initialize(context);
		Result result = ZgzcwDataDownloader.downloadLiveBdWebPage();
		
		if(result == null)
		{
			logger.info("Error when downloading BdMatches.");
			return;
		}
		MatchList list = (MatchList)result.get("matches");
		if(list == null)
		{
			logger.info("The MatchList is Null.");
			return;
		}
		
		int i = 1;
		for (MatchItem matchItem : list)
		{
			logger.info(i +++ ": " + matchItem);
		}
		
		Keys keys = (Keys)result.get("issues");
		for (String string : keys)
		{
			logger.info(": " + string);
		}
	}
	
	
	public static void testDownloadOkoooOpWebPage(LorisContext context) throws Exception
	{
		OkoooWebPage basePage = OkoooPageCreator.createBaseWebPage();
		WebClientFetcher fetcher = WebClientFetcher.createFetcher(basePage);
		fetcher.waitForBackgroundJavaScript(10000);
		
		if(!basePage.isCompleted())
		{
			logger.info("Error loading the base page '" + basePage.getFullURL() + ", the prepare process is not success, exit.");
			//logger.info(basePage.getContent());
			return;
		}
		
		List<OkoooBdMatch> matchs = processBdBaseWebPage(basePage);
		if(matchs == null)
		{
			return;
		}
		
		int i = 1;
		OkoooBdMatch downloadMatch = null;
		for (OkoooBdMatch okoooBdMatch : matchs)
		{
			if(downloadMatch == null && DateUtil.isSameDay(okoooBdMatch.getMatchDate(), new Date()))
			{
				logger.info("Will download match: " + okoooBdMatch);
				downloadMatch = okoooBdMatch;
			}
			logger.info(i +++ ": " + okoooBdMatch);
		}
		
		if(downloadMatch != null)
		{
			/*logger.info("Will download '" + mid + " 欧赔 datas.");
			List<OkoooOp> ops = OkoooDataDownloader.downloadMatchMainOp(fetcher, downloadMatch);
			if(ops == null)
			{
				logger.info("The op data is null");
			}
			else
			{
				i = 1;
				for (OkoooOp okoooOp : ops)
				{
					logger.info(i +++ ": " + okoooOp);
				}
			}
			*/
			logger.info("Will download '" + downloadMatch.getMid() + " 亚盘 datas.");
			List<OkoooYp> yps = OkoooDataDownloader.downloadMatchMainYp(fetcher, downloadMatch);
			if(yps == null)
			{
				logger.info("The yp data is null");
			}
			else
			{			
				i = 1;
				for (OkoooYp okoooYp : yps)
				{
					logger.info(i +++ ": " + okoooYp);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testOkoooChileYpParser(LorisContext context) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		FileUtils.readToBuffer(sb, "D:\\tmp\\test2.txt");
		logger.info(sb.toString());
		
		WebPage page = new OkoooWebPage();
		page.setContent(sb.toString());
		page.setContent(sb.toString());
		page.setCompleted(true);
		
		OddsYpChildParser parser = new OddsYpChildParser();
		parser.setCurrentTime(new Date());
		if(parser.parseWebPage(page))
		{
			logger.info("Success to parse the WebPage: " + page);
			List<OkoooYp> yps = parser.getYps();
			int i = 1;
			for (OkoooYp okoooOp : yps)
			{
				logger.info(i +++ ": " + okoooOp);
			}
			
			//logger.info("Total Corprate number is : " + parser.getCorpNum());
		}
		else
		{
			logger.info("Error when parse the page: " + page);
		}
	}
	
	/**
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testOkoooChileOpParser(LorisContext context) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		FileUtils.readToBuffer(sb, "D:\\tmp\\test.txt");
		//logger.info(sb.toString());
		
		WebPage page = new OkoooWebPage();
		page.setContent(sb.toString());
		page.setContent(sb.toString());
		page.setCompleted(true);
		
		OddsOpChildParser parser = new OddsOpChildParser();
		if(parser.parseWebPage(page))
		{
			logger.info("Success to parse the WebPage: " + page);
			List<OkoooOp> ops = parser.getOps();
			int i = 1;
			for (OkoooOp okoooOp : ops)
			{
				logger.info(i +++ ": " + okoooOp);
			}
			
			logger.info("Total Corprate number is : " + parser.getCorpNum());
		}
		else
		{
			logger.info("Error when parse the page: " + page);
		}
	}
	
	
	protected static List<OkoooBdMatch> processBdBaseWebPage(OkoooWebPage page)
	{
		OkoooBdPageParser parser = new OkoooBdPageParser();
		if(parser.parseWebPage(page))
		{
			logger.info("Success to parse Bd WebPage " + page.getFullURL() + ".");
			Map<String, List<OkoooBdMatch>> pairs = parser.getMatchMap();
			
	
			List<OkoooBdMatch> matchList = new ArrayList<>();
			for (String key : pairs.keySet())
			{
				List<OkoooBdMatch> list = pairs.get(key);
				if(list != null && list.size() > 0)
				{
					matchList.addAll(list);
				}
			}
			return matchList;
		}
		else
		{
			logger.info(page.getContent());
			logger.info("Error occured when parsing the base page '" + page.getFullURL() + ".");
			return null;
		}
	}
	
	
	public static void testUploadDataSchecduler(LorisContext context) throws Exception
	{
		DataUploadScheduler scheduler = new DataUploadScheduler();
		scheduler.initialize();
		
		Thread thread = new Thread(scheduler);
		thread.start();
	}

	public static void testRemoteManager(LorisContext context) throws Exception
	{
		
		//getFileApplicationContext();		
		BasicManager soccerManager = context.getBean(BasicManager.class);

		// ClassPathXmlApplicationContext appContext;
		/** The Application Context. */
		RemoteSoccerManager manager = (RemoteSoccerManager) context.getBean("remoteSoccerManager");
		logger.info("BaseURL: " + manager.getBaseUrl());
		/*
		 * manager.setHost("localhost"); manager.setPort("80");
		 * manager.setUri("/loris/upload/table");
		 */
		List<Entity> entities = new ArrayList<>();
		List<User> users = soccerManager.getUserList();
		entities.addAll(users);

		long st = System.currentTimeMillis();
		String result = manager.saveEntities(entities);
		long en = System.currentTimeMillis();
		logger.info(result);
		logger.info("Total spend " + (en - st) + "ms.");
	}

	/**
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testTaskQueue(LorisContext context) throws Exception
	{
		ZgzcwDataDownloader.initialize(context);

		/*TaskQueue<MatchWebTask> queue = new PriorityTaskQueue<>();
		SoccerMatchTaskProducer producer = new SoccerMatchTaskProducer();
		producer.setTaskQueue(queue);

		Thread thread = new Thread(producer);
		thread.start();*/

		/*
		 * SoccerManager soccerManager = context.getBean(SoccerManager.class);
		 * 
		 * String st = "2018-11-07"; String en = "2018-11-09"; List<BdMatch>
		 * matchs = soccerManager.getBdMatchByMatchtime(st, en);
		 * 
		 * TaskQueue<MatchWebTask> queue = new PriorityTaskQueue<>();
		 * 
		 * int index = 1; for (BdMatch match : matchs) { //logger.info(index +++
		 * ": " + match);
		 * 
		 * MatchWebTask task = new MatchWebTask(match, MatchWebTaskType.Op);
		 * queue.pushTask(task); }
		 * 
		 * int size = queue.size(); for(index = 0; index < size; index ++) {
		 * logger.info(index + ": " + queue.popTask().getMatch()); }
		 */

		logger.info("Test exit.");
	}

	public static void testPossion(LorisContext context) throws Exception
	{
		SoccerManager soccerManager = context.getBean(SoccerManager.class);
		String lid = "284";
		// String season = "2018-2019";
		// int round = 26;

		List<RankInfo> ranks = soccerManager.getLatestRanks(lid, SoccerConstants.RANK_TOTAL);

		int size = ranks.size();
		double[] p = new double[size];
		double[] scores = new double[size];

		int i = 0;
		for (RankInfo rankInfo : ranks)
		{
			p[i] = rankInfo.getGoal() * 1.0 / rankInfo.getLosegoal();
			scores[i] = rankInfo.getScore();

			i++;
			// logger.info(i +++ ": " + rankInfo);
		}

		logger.info(Arrays.toString(p));
		logger.info(Arrays.toString(scores));
		double cov = new PearsonsCorrelation().correlation(p, scores);
		logger.info("Covraiance: " + cov);

		double lamda1 = 1.71;
		double lamda2 = 2.14;
		double[] prob = PossionUtil.computeOddsProb(lamda1, lamda2);
		logger.info(Arrays.toString(prob));
	}

	/**
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testIssueLeagueMatches(LorisContext context) throws Exception
	{
		context.getBean(SoccerManager.class);
		String issue = "2018-09-19";
		String lid = "127";

		long st = System.currentTimeMillis();
		List<MatchInfo> matchInfos = MatchDocLoader.getMatchInfos(issue, lid);
		long en = System.currentTimeMillis();
		logger.info("Total spend time to load " + (en - st) + " ms.");

		if (matchInfos == null)
		{
			logger.info("There are no matchs in the issue=" + issue + " lid=" + lid);
			return;
		}
		int i = 1;
		for (MatchInfo matchInfo : matchInfos)
		{
			logger.info(i++ + ": " + matchInfo);
		}
	}

	/**
	 * 测试转换的数据
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testOddsUtil(LorisContext context) throws Exception
	{
		String string = "平/半↓";
		String string2 = formatHandicap(string);

		logger.info("String: " + string + " Format: " + string2);

		String handicap = "平/半";
		YpValue value = new YpValue("平手", 0.78f, 1.16f);

		YpValue value2 = OddsUtil.formatYpValue(value, handicap);
		logger.info("Info: " + value + " -> Info:" + value2);
	}

	/**
	 * 测试对比分析的情况
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testOYCompare(LorisContext context) throws Exception
	{
		SoccerManager soccerManager = context.getBean(SoccerManager.class);
		String issue = "2018-09-08";

		long st = System.currentTimeMillis();
		List<BdMatch> matchs = soccerManager.getBdMatches(issue);
		List<String> mids = ArraysUtil.getObjectFieldValue(matchs, BdMatch.class, "mid");

		List<MatchOdds> matchOdds = new ArrayList<>();
		for (BdMatch bdMatch : matchs)
		{
			matchOdds.add(new MatchOdds(bdMatch));
		}

		List<Op> ops = soccerManager.getOddsOp(mids, true);
		List<Yp> yps = soccerManager.getYpList(mids, true);

		// int i = 0;
		for (Op op : ops)
		{
			// logger.info(i +++ ": " + op);
			for (MatchOdds match : matchOdds)
			{
				if (op.getMid().equals(match.getMid()))
				{
					match.addOp(op);
					break;
				}
			}
		}
		for (Yp yp : yps)
		{
			float win, lose;
			float ratio;
			win = yp.getWinodds() + 1.0f;
			lose = yp.getLoseodds() + 1.0f;
			ratio = yp.getLossratio();

			String info = yp.getMid() + ": " + yp.getHandicap();
			info += " Odds Prob(" + NumberUtil.setFloatScale(2, 100.0 * ratio / win) + ",";
			info += NumberUtil.setFloatScale(2, 100.0 * ratio / lose) + ") Prob(" + yp.getWinprob() + ", "
					+ yp.getLoseprob() + ")";

			logger.info(info);
			// logger.info(i +++ ": " + yp);
			for (MatchOdds match : matchOdds)
			{

				if (yp.getMid().equals(match.getMid()))
				{
					match.addYp(yp);
					break;
				}
			}
		}

		long en = System.currentTimeMillis();
		logger.info("Total spend " + (en - st) + " ms to load '" + issue + "' " + matchOdds.size() + " data.");

		List<MatchInfo> matchInfos = soccerManager.getMatchInfos(mids);
		int i = 1;
		for (MatchInfo matchInfo : matchInfos)
		{
			logger.info(i++ + ": " + matchInfo);
		}
	}

	/**
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testOkoooBdMatch(LorisContext context) throws Exception
	{
		WebPage page = OkoooPageCreator.createBdWebPage();

		try (WebClientFetcher fetcher = WebClientFetcher.createFetcher(page))
		{
			int i = 0;
			logger.info("Success fetch: " + page);

			OkoooBdPageParser pageParser = new OkoooBdPageParser();
			if (pageParser.parseWebPage(page))
			{
				Map<String, List<OkoooBdMatch>> map = pageParser.getMatchMap();

				for (String key : map.keySet())
				{
					logger.info("Issue: " + key);

					List<OkoooBdMatch> matchs = map.get(key);
					for (OkoooBdMatch okoooBdMatch : matchs)
					{
						logger.info(i++ + ": " + okoooBdMatch);
					}
				}
			}
			else
			{
				logger.info("Parse web page error.");
			}

		}
	}

	public static void testOpStat(LorisContext context) throws Exception
	{
		String mid = "2413492"; // 2342630
		SoccerManager soccerManager = context.getBean(SoccerManager.class);

		List<Op> ops = soccerManager.getOpListOrderByTime(mid, true);
		List<Op> cleanOps = new ArrayList<>();

		EqualChecker checker = new EqualChecker();

		for (Op op : ops)
		{
			checker.gid = op.getGid();
			Op op1 = ArraysUtil.getSameObject(cleanOps, checker);
			if (op1 == null)
			{
				cleanOps.add(op);
			}
			else
			{
				if (op.getLastTimeValue() > op1.getLastTimeValue())
				{
					cleanOps.remove(op1);
					cleanOps.add(op);
				}
			}
		}
		// ArraysUtil.getListValues(ops, cleanOps, checker)

		// OpVariance vars = MatchOddsUtil.computeMatchOpVariance(mid,
		// cleanOps);
		// for (String key : vars.keySet())
		// {
		// logger.info(vars.getOpVariance(key));
		// }
	}

	public static void testRankInfo(LorisContext context) throws Exception
	{
		SoccerManager soccerManager = context.getBean(SoccerManager.class);
		String lid = "915";
		List<RankInfo> ranks = soccerManager.getLatestAllRanks(lid);

		int i = 1;
		for (RankInfo rankInfo : ranks)
		{
			logger.info(i++ + ": " + rankInfo);
		}
	}

	public static void testIssueMatchDownloader(LorisContext context) throws Exception
	{
		/*
		 * SoccerWebPageManager soccerManager =
		 * context.getBean(SoccerWebPageManager.class);
		 * 
		 * String sqlwhere = "jc"; List<LotteryWebPage> pages =
		 * soccerManager.getDownloadedLotteryWebPages(sqlwhere);
		 * logger.info(pages.size());
		 */

		context.getBean(SoccerManager.class);

		IssueMatchDownloader downloader = new IssueMatchDownloader();
		downloader.setLorisContext(context);
		downloader.setCheckexist(false);

		if (downloader.prepare())
		{
			List<IssueMatch> matchs = downloader.getIssueMatchs();
			// int i = 0;

			logger.info("Match size: " + (matchs.size()));
			// for (BdMatch bdMatch : matchs)
			// {
			// logger.info(i +++ ": " + bdMatch);
			// }
			logger.info(downloader.totalSize());
		}

		downloader.close();

		/*
		 * long start = System.currentTimeMillis(); ZgzcwWebPageCreator ZgzcwWebPageCreator
		 * = new ZgzcwWebPageCreator(); LotteryWebPage page =
		 * ZgzcwWebPageCreator.createBdWebPage(""); if(UrlFetcher.fetch(page)) {
		 * LotteryBdWebPageParser parser = new LotteryBdWebPageParser();
		 * if(parser.parseWebPage(page)) { List<BdMatch> bdMatchs =
		 * parser.getMatches(); logger.info("Total match is :" +
		 * bdMatchs.size());
		 * 
		 * int i = 0;
		 * 
		 * logger.info("Match size: " + (bdMatchs.size())); for (BdMatch bdMatch
		 * : bdMatchs) { logger.info(i +++ ": " + bdMatch); } } } long en =
		 * System.currentTimeMillis(); logger.info("Total spend time is " + (en
		 * - start) + " ms.");
		 */
	}

	/**
	 * 测试欧赔数据的方差值计算
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testOpVariance(LorisContext context) throws Exception
	{
		SoccerManager manager = context.getBean(SoccerManager.class);

		if (manager == null)
		{
			logger.info("The manager is not initialized.");
			return;
		}
		String issue = "2018-08-20";
		int i = 1;

		List<MatchOpVariance> vars = MatchOddsPool.computeJcMatchsOpVariance(issue);
		for (MatchOpVariance matchOpVariance : vars)
		{
			logger.info(i++ + ": " + matchOpVariance);

			// OpVariance var = matchOpVariance.getVariance();

			// logger.info(" 初始方差值： " + matchOpVariance.getFirstVariance());
			logger.info("    即时方差值： " + matchOpVariance);
			// logger.info(" 胜赔超限： " + var.getWinextremenum()
			// + " 平赔超限: " + var.getDrawextremenum()
			// + " 负赔超限： " + var.getLoseextremenum());

		}
	}

	/**
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testRoundMatchDownloader(LorisContext context) throws Exception
	{
		context.getBean(SoccerManager.class);
		RoundMatchDownloader downloader = new RoundMatchDownloader();
		downloader.setLorisContext(context);

		downloader.setStart("2018-08-12");
		downloader.setEnd("2018-08-18");
		downloader.checkCupPages();

		int i = 1;
		while (downloader.hasNextWebPage())
		{
			logger.info(i++ + ": " + downloader.popWebPage());
		}

		downloader.close();
	}

	public static void testSetting(LorisContext context) throws Exception
	{
		context.getBean(SoccerManager.class);
		List<Setting> settings = SettingManager.getSettingManager().getSettings();
		for (Setting setting : settings)
		{
			logger.info(setting);
		}
		// Setting setting =
		// SettingManager.getSettingManager().createDefaultCorpSetting();
		// SettingManager.updateSetting(setting);
	}

	public static void testOddsConversion(LorisContext context) throws Exception
	{
		double v = 1.15;
		List<OddsUtil.OddsMapping> list = OddsUtil.getOddsFromOp(v);
		int i = 1;
		for (OddsUtil.OddsMapping odds : list)
		{
			logger.info(i++ + ": " + odds);
		}

		logger.info("****************");

		String handicap = "球半";
		v = 0.89;
		OddsUtil.OddsMapping odds = OddsUtil.getOddsFromYp(handicap, v);
		logger.info("Get Odds conversion: " + odds);

		/*
		 * Map<String, OddsConversion.OddsList> list =
		 * OddsConversion.OP_YP_LIST; int i = 1; for (String name :
		 * list.keySet()) { logger.info(i +++ ": " + name + ": " +
		 * list.get(name)); }
		 */
		/*
		 * int lenType = OddsConversion.NAME_YPS.length;
		 * logger.info("NAME_YPS len =" + lenType);
		 * 
		 * String oddsvalues; int m = OddsConversion.OP_YP_TABLE.length; int n =
		 * OddsConversion.OP_YP_TABLE[0].length / 2;
		 * 
		 * for(int i = 0; i < n; i ++) { //int len =
		 * OddsConversion.OP_YP_TABLE[i].length; oddsvalues = "{"; for(int j =
		 * 0; j < m; j ++ ) { oddsvalues += NumberUtil.formatDouble(3,
		 * OddsConversion.OP_YP_TABLE[j][i * 2]) + ", " +
		 * NumberUtil.formatDouble(3, OddsConversion.OP_YP_TABLE[j][i * 2 + 1]);
		 * if(j != n - 1) { oddsvalues += ", "; } } oddsvalues += "},";
		 * logger.info(oddsvalues); }
		 */
	}

	/**
	 * 测试RoundLeague下载数据
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testRoundCupDownloader(LorisContext context) throws Exception
	{
		SoccerManager manager = context.getBean(SoccerManager.class);
		String lid = "114";
		String season = "";

		RoundCupWebPage page = ZgzcwWebPageCreator.createRoundCupWebpage(lid, season);
		logger.info("Downloading " + page);

		if (UrlFetcher.fetch(page))
		{
			RoundCupWebPageParser parser = new RoundCupWebPageParser();
			if (parser.parseWebPage(page))
			{
				List<Match> matchs = parser.getMatches();
				int i = 1;
				for (Match match : matchs)
				{
					logger.info(i++ + ":" + match);
				}
				manager.addOrUpdateMatches(matchs);

				List<Round> rounds = parser.getRounds();
				i = 1;
				for (Round round : rounds)
				{
					logger.info(i++ + ": " + round);
				}
				manager.addOrUpdateRound(rounds.get(0));
			}
			else
			{
				logger.info("Error when parsing page.");
			}
		}
		else
		{
			logger.info("Error when downloading page.");
		}
	}

	public static void testLeagueUtil(LorisContext context) throws Exception
	{
		SoccerManager soccerManager = context.getBean(SoccerManager.class);
		LeagueDataUtil.computeRoundTime(soccerManager);
	}

	public static void testSeasonDownloader(LorisContext context) throws Exception
	{
		context.getBean(SoccerManager.class);

		SeasonDownloader downloader = new SeasonDownloader();
		downloader.setLorisContext(context);
		downloader.prepare();

		int i = 1;
		while (downloader.hasNextWebPage())
		{
			WebPage page = downloader.popWebPage();
			logger.info(i + ": " + page);

			i++;
		}

		downloader.close();
	}

	public static void testRoundMatch(LorisContext context) throws Exception
	{
		context.getBean(SoccerManager.class);

		String start = "2018-09-01";
		String end = "2018-09-05";

		RoundMatchDownloader downloader = new RoundMatchDownloader();
		downloader.setLorisContext(context);
		downloader.setStart(start);
		downloader.setEnd(end);

		// logger.info("Check League Main page...");
		// downloader.checkLeagueMainPage();

		// logger.info("Check League round page...");
		// downloader.checkRoundPage();
		downloader.prepare();

		int i = 1;
		// int downpage = 0;

		// WebClientFetcher fetcher = null;

		while (downloader.hasNextWebPage())
		{
			WebPage page = downloader.popWebPage();
			logger.info(i++ + ": " + page);
			/*
			 * if(page instanceof RoundCupWebPage) { logger.info(i + ": " +
			 * page); } else if(page instanceof RoundLeagueWebPage) {
			 * RoundLeagueWebPage page2 = (RoundLeagueWebPage) page;
			 * 
			 * WebPage p = new WebPage(); p.setUrl(page2.getParentURL());
			 * 
			 * if(fetcher == null) { fetcher =
			 * WebClientFetcher.createFetcher(p); }
			 * 
			 * if(fetcher.fetch(page2)) { logger.info(i + " Page: " + page2 +
			 * "\r\n" + page2.getContent()); }
			 * 
			 * downpage ++; if(downpage == 20) {
			 * 
			 * break; } }
			 */
			// i ++;
		}

		/*
		 * if(fetcher != null) { fetcher.close(); }
		 */

		downloader.close();

		/*
		 * Map<String, LeagueSeason> maps = manager.getSeasonsMap();
		 * 
		 * Date date = new Date(); boolean b;
		 * 
		 * for (String key : maps.keySet()) { LeagueSeason leagueSeason =
		 * maps.get(key); Season season = leagueSeason.getLastSeason(); b =
		 * leagueSeason.hasLastSeason(date); logger.info(season + ": " + (b ?
		 * "更新" : "不更新")); }
		 */
	}

	public static void testMainPageParser(LorisContext context) throws Exception
	{
		WebPage page = ZgzcwWebPageCreator.createZgzcwMainPage();

		if (UrlFetcher.fetch(page))
		{
			ZgzcwCenterParser parser = new ZgzcwCenterParser();
			if (parser.parseWebPage(page))
			{
				List<League> leagues = parser.getLeagues();
				int i = 0;
				for (League league : leagues)
				{
					logger.info(i++ + ": " + league);
				}
			}
			else
			{
				logger.info("Error occured when parsing page: " + page);
			}
		}
		else
		{
			logger.info("Error occered when Fetching page: " + page);
		}

	}

	/**
	 * 测试竞彩数据的内容
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testJcMatchDataVector(LorisContext context) throws Exception
	{
		context.getBean(SoccerManager.class);
		long st = System.currentTimeMillis();

		String issue = "2018-06-02";
		CorpSetting configure = MatchDocLoader.getDefaultCorpSetting();
		logger.info("Configured UserCorporate number is " + configure.getParams().size());

		MatchDoc dataVector = MatchDocLoader.getMatchDoc(issue, configure);
		logger.info(dataVector.size());

		// int size = dataVector.size();
		int i = 1;
		for (String mid : dataVector.keySet())
		{
			MatchData data = dataVector.getMatchData(mid);
			logger.info(i++ + ": " + data);

			List<Match> history = data.getHistoryMatch();
			for (Match match : history)
			{
				logger.info(match);
			}
		}

		long en = System.currentTimeMillis();
		logger.info("Total spend " + (en - st) + " ms to process the data.");
	}

	/**
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testJcMatchMapping(LorisContext context) throws Exception
	{
		SoccerManager soccerManager = context.getBean(SoccerManager.class);
		// OkoooManager okoooManager = context.getBean(OkoooManager.class);

		long st = System.currentTimeMillis();
		String issue = "2018-05-27";
		List<JcMatch> sourceMatches = soccerManager.getJcMatches(issue);
		List<OkoooJcMatch> destMatches = soccerManager.getOkoooJcMatches(issue);

		logger.info("Source size " + sourceMatches.size() + ", Dest size " + destMatches.size());

		IssueMatchMapping mapping = MatchDocLoader.mappingJcMatchIds(issue, SoccerConstants.DATA_SOURCE_ZGZCW,
				sourceMatches, SoccerConstants.DATA_SOURCE_OKOOO, destMatches);

		long en = System.currentTimeMillis();
		logger.info("Total spend " + (en - st) + " ms to process the data.");

		/*
		 * int size = mapping.size(); for(int i = 0; i < size; i ++) {
		 * PairValue<JcMatch, JcMatch> p = mapping.getPairValue(i); String info
		 * = p.getKey().getHomename() + " vs " + p.getKey().getClientname();
		 * info += " => " + p.getValue().getHomename() + " vs " +
		 * p.getValue().getClientname(); logger.info((i + 1) + ": " + info); }
		 */

		for (JcMatch match : sourceMatches)
		{
			if (!mapping.contains(match))
			{
				logger.info("Source: " + match.getHomename() + " vs " + match.getClientname() + " is not mapped."
						+ match.getOrdinary());
			}
		}

		for (JcMatch match : destMatches)
		{
			if (!mapping.contains(match))
			{
				logger.info("Dest: " + match.getHomename() + " vs " + match.getClientname() + " is not mapped."
						+ match.getOrdinary());
			}
		}
	}

	/**
	 * Test for the OddsCorp
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testOddsCorpConfigure(LorisContext context) throws Exception
	{
		context.getBean(SoccerManager.class);

		CorpSetting configure = MatchDocLoader.getDefaultCorpSetting();
		List<Parameter> corporates = configure.getParams();

		int i = 1;
		for (Parameter corp : corporates)
		{
			logger.info(i++ + ": " + corp);
		}

		List<String> mids = new ArrayList<>();
		List<String> gids = new ArrayList<>();
		mids.add("2342367");
		List<Parameter> opCorps = configure.getCorporates("zgzcw", "op");
		for (Parameter userCorporate : opCorps)
		{
			gids.add(userCorporate.getValue1());
		}
		List<Op> ops = SoccerManager.getInstance().getOddsOp(mids, gids, true);
		i = 1;
		for (Op op : ops)
		{
			logger.info(i++ + ": " + op);
		}
	}

	/**
	 * 测试Okooo
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testOkoooDownloader(LorisContext context) throws Exception
	{
		OkoooWebPage basePage = OkoooPageCreator.createBaseWebPage();
		String issue = DateUtil.getCurDayStr();

		try (WebClientFetcher fetcher = WebClientFetcher.createFetcher(basePage))
		{
			// 检测是否完成数据下载
			if (!basePage.isCompleted())
			{
				logger.info("Download the BasePage error, exit. ");
				return;
			}

			// 进行页面数据解析
			OkoooJcPageParser parser = new OkoooJcPageParser();
			if (!parser.parseWebPage(basePage))
			{
				logger.info("Parse the BasePage error, exit.");
				return;
			}
			// 获得今天的比赛数据
			List<OkoooJcMatch> matchs = parser.getMatchMap().get(issue);
			if (matchs == null)
			{
				logger.info("Download the BasePage error, exit. ");
				return;
			}

			int i = 1;
			OkoooJcMatch match = null;
			for (OkoooJcMatch m : matchs)
			{
				if (i == 1)
				{
					match = m;
				}
				logger.info(i++ + ": " + m);
			}

			// 暂停下载数据
			try
			{
				Thread.sleep(1000);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			// 亚盘
			OkoooWebPage ypPage = OkoooPageCreator.createYpWebPage(match.getMid());
			if (fetcher.fetch(ypPage))
			{
				// 亚盘数据下载
				com.loris.soccer.web.downloader.okooo.parser.OddsYpPageParser parser2 = new com.loris.soccer.web.downloader.okooo.parser.OddsYpPageParser();
				parser2.setMatchtime(DateUtil.tryToParseDate(match.getMatchtime()));
				parser2.setMid(match.getMid());

				int k = 1;
				if (parser2.parseWebPage(ypPage))
				{
					List<OkoooYp> yps = parser2.getYps();

					for (OkoooYp okoooYp : yps)
					{
						logger.info(k++ + ": " + okoooYp);
					}
				}

				for (int j = 1; j < 3; j++)
				{
					// 暂停下载数据
					try
					{
						Thread.sleep(100);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					OkoooRequestHeaderWebPage morePage = OkoooPageCreator.createYpPageWebPage(match.getMid(), j);

					// 数据下载
					if (!fetcher.fetch(morePage))
					{
						continue;
					}
					OddsYpChildParser parser3 = new OddsYpChildParser();
					parser3.setMid(match.getMid());
					parser3.setCurrentTime(DateUtil.tryToParseDate(morePage.getLoadtime()));

					if (parser3.parseWebPage(morePage))
					{
						// int k = 1;
						List<OkoooYp> yps = parser3.getYps();
						for (OkoooYp okoooYp : yps)
						{
							logger.info(k++ + ": " + okoooYp);
						}
					}
				}
			}
		}
	}

	public static void testL500WebPageDownloader(LorisContext context) throws Exception
	{
		String url = "http://odds.500.com/fenxi/ouzhi-711570.shtml";
		url = "http://odds.500.com/fenxi1/ouzhi.php?id=711570&ctype=1&start=120&r=1&style=0&guojia=0&chupan=1";
		WebPage page = new WebPage();
		page.setUrl(url);
		page.setEncoding("gb2312");
		page.setEncoding("utf-8");
		page.setZiptype("gzip");

		if (UrlFetcher.fetch(page))
		{

			// logger.info(page.getContent());
		}
	}

	public static void testOkoooDailyDownloader(LorisContext context) throws Exception
	{
		OkoooDailyDownloader downloader = new OkoooDailyDownloader();
		downloader.setLorisContext(context);

		downloader.prepare();
		int i = 1;
		while (downloader.hasNextWebPage())
		{
			OkoooWebPage page = (OkoooWebPage) downloader.popWebPage();

			if (i == 1)
			{
			}
			if (downloader.download(page))
			{
				if (SoccerConstants.ODDS_TYPE_YP.equalsIgnoreCase(page.getType()))
				{
					// http://www.okooo.com/soccer/match/1023935/ah/ajax/?page=1&trnum=30&companytype=BaijiaBooks

					com.loris.soccer.web.downloader.okooo.parser.OddsYpPageParser parser = new com.loris.soccer.web.downloader.okooo.parser.OddsYpPageParser();
					parser.setMatchtime(new Date());
					if (parser.parseWebPage(page))
					{
						List<OkoooYp> yps = parser.getYps();
						int j = 1;
						for (Yp yp : yps)
						{
							logger.info(j++ + ": " + yp);
						}
					}

					if (OkoooPageCreator.PAGE_TYPES[2].equalsIgnoreCase(page.getType()))
					{
						String mid = page.getMid();
						String url = "http://www.okooo.com/soccer/match/" + mid
								+ "/ah/ajax/?page=1&trnum=30&companytype=BaijiaBooks";
						OkoooWebPage page2 = new OkoooWebPage();
						page2.setUrl(url);
						page.setEncoding("utf-8");

						downloader.download(page2);
						logger.info(page2.toString());
					}

					break;
				}
				else if ("ypchange".equalsIgnoreCase(page.getType()))
				{
					OddsYpChangeParser parser = new OddsYpChangeParser();
					if (parser.parseWebPage(page))
					{
						List<OkoooYp> yps = parser.getOddslist();
						for (OkoooYp yp : yps)
						{
							logger.info(yp);
						}
					}
				}
			}

			try
			{
				Thread.sleep(5000);
			}
			catch (Exception e)
			{

			}
			logger.info(i++ + ": " + page.getFullURL());
		}

		downloader.close();
	}

	public static void testOkoooWeb(LorisContext context) throws Exception
	{
		String baseUrl = "http://www.okooo.com/jingcai/";
		WebPage page = new WebPage();
		page.setUrl(baseUrl);

		WebClientFetcher fetcher = WebClientFetcher.createFetcher(page);
		// logger.info(page.getContent());

		WebPage p1 = new WebPage();
		String url = "http://www.okooo.com/soccer/match/1009824/ah/";
		p1.setUrl(url);

		if (fetcher.fetch(p1))
		{
			// logger.info(p1.getContent());
		}

		fetcher.close();

	}

	/**
	 * 测试竞彩比赛数据分析
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testJcMatchCalculator(LorisContext context) throws Exception
	{
		context.getApplicationContext().getBean(SoccerManager.class);
		// String issue = "2018-05-05";
		// List<JcMatch> matchs = soccerManager.getJcMatches(issue);

		/*
		 * AnalyzerFactory calculator = new AnalyzerFactory();
		 * 
		 * long st = System.currentTimeMillis(); if
		 * (!calculator.loadJcMatchFromDatabase(issue)) {
		 * logger.info("Error when loading data from database."); return; }
		 * 
		 * long en = System.currentTimeMillis(); logger.info( "Today '" + issue
		 * + "' there are " + calculator.size() + " matches, and spend " + (en -
		 * st) + " ms.");
		 * 
		 * st = System.currentTimeMillis(); if
		 * (!calculator.loadOddsFromDatabase(null)) {
		 * logger.info("Error when loading odds data from database."); }
		 * 
		 * en = System.currentTimeMillis();
		 * logger.info("Loaded Yp and Op data from database spend " + (en - st)
		 * + " ms.");
		 * 
		 * List<MatchDataList> vectors = calculator.getLeagueVectors(); Graph
		 * graph = MatchGraph.createGraph(issue, vectors);
		 * 
		 * List<GraphNode> nodes = graph.getNodes(); for (GraphNode graphNode :
		 * nodes) { logger.info(graphNode); }
		 * 
		 * List<GraphEdge> edges = graph.getLinks(); for (GraphEdge graphEdge :
		 * edges) { logger.info(graphEdge); }
		 * 
		 * /* List<LeagueMatchVector> leagues = calculator.getLeagueVectors();
		 * for (LeagueMatchVector league : leagues) { String info =
		 * league.getLid() + ": " + league.getLeaguename(); logger.info(info);
		 * 
		 * league.computeCorrelationMatchs();
		 * 
		 * List<MatchResult> results = league.getResults(); for (MatchResult
		 * matchResult : results) { logger.info("" + matchResult +
		 * " correlation: " + matchResult.getMids());
		 * 
		 * List<Op> ops = matchResult.getOpList(); for (Op op : ops) {
		 * logger.info(op); } }
		 * 
		 * logger.info("------------------------------"); }
		 * 
		 * logger.info("平均欧赔：" + calculator.getOpAverageCorp());
		 * 
		 * st = System.currentTimeMillis(); calculator.loadHistoryMatches(); en
		 * = System.currentTimeMillis();
		 * 
		 * int lastMatchSize = 6;
		 * 
		 * for (LeagueMatchVector league : leagues) { List<MatchResult> results
		 * = league.getResults(); for (MatchResult result : results) { int
		 * homeHisSize = result.getHomeHistories().size(); int clientHisSize =
		 * result.getClientHistories().size(); logger.info("(" +
		 * result.getMatch().getLid() + ", " + result.getMid() + " Homeid[" +
		 * result.getMatch().getHomeid() + "]=" + homeHisSize + " Clientid[" +
		 * result.getMatch().getClientid() + "]=" + clientHisSize);
		 * 
		 * Performance homePerform =
		 * result.getHomeTeamPerformance(MatchType.ALL, lastMatchSize);
		 * Performance homeHPeform =
		 * result.getHomeTeamPerformance(MatchType.HOME, lastMatchSize);
		 * 
		 * logger.info("主队战绩：" + homePerform); logger.info("主队主场战绩：" +
		 * homeHPeform);
		 * 
		 * Performance clientPerform =
		 * result.getClientTeamPerformance(MatchType.ALL, lastMatchSize);
		 * Performance clientCPerform =
		 * result.getClientTeamPerformance(MatchType.CLIENT, lastMatchSize);
		 * 
		 * logger.info("客队战绩：" + clientPerform); logger.info("客队客场战绩：" +
		 * clientCPerform);
		 * 
		 * List<BaseMatch> matchs = result.getHomeHistories(MatchType.ALL,
		 * lastMatchSize); int index = 1; for (BaseMatch baseMatch : matchs) {
		 * logger.info(index +++ ": " + baseMatch); }
		 * 
		 * } }
		 * 
		 * logger.info("Loaded history data from database spend " + (en - st) +
		 * " ms.");
		 */
		/*
		 * List<UserCorporate> corps = soccerManager.getUserOpCorporates(true);
		 * for (UserCorporate userCorporate : corps) { logger.info("Op: " +
		 * userCorporate); }
		 * 
		 * List<UserCorporate> ypcorps =
		 * soccerManager.getUserYpCorporates(true); for (UserCorporate
		 * userCorporate : ypcorps) { logger.info("Yp: " + userCorporate); }
		 */

	}

	/**
	 * 测试正则表达式
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testRegex(LorisContext context) throws Exception
	{
		String handicap = "一/球半↑↓";
		String regex = "↓|↑";
		String result = handicap.replaceAll(regex, "");
		logger.info("Source: " + handicap + " -> " + result);

		logger.info("E: " + Math.E);
		logger.info(1.0 / Math.pow(Math.E, 0));
		logger.info(1.0 / Math.pow(Math.E, 0.4));
		logger.info(1.0 / Math.pow(Math.E, 1));
		logger.info(1.0 / Math.pow(Math.E, 4));
		logger.info(1.0 / Math.pow(Math.E, 10));
	}

	public static void testOddsOpList(LorisContext context) throws Exception
	{
		context.getApplicationContext().getBean(SoccerManager.class);

		String mid = "2366950";
		List<Op> ops = SoccerManager.getInstance().getOddsOp(mid);
		for (Op op : ops)
		{
			logger.info(op);
		}
	}

	/**
	 * 测试查询历史数据
	 * 
	 * @param context
	 *            运行环境
	 * @throws Exception
	 *             异常处理
	 */
	public static void testSearchMatchHistory(LorisContext context) throws Exception
	{
		context.getApplicationContext().getBean(SoccerManager.class);

		String t1 = "1890";
		String t2 = "1956";
		String time = DateUtil.getCurTimeStr();

		SoccerManager soccerManager = SoccerManager.getInstance();

		long st = System.currentTimeMillis();
		List<MatchInfo> matchs = soccerManager.getMatchHistory(t1, t2, time, false);
		long en = System.currentTimeMillis();
		logger.info("Total spend time is " + (en - st) + " ms.");
		logger.info("Total size : " + matchs.size());

		int size = 10;
		List<MatchInfo> homeHistories = TeamHistoryCalculator.getLastMatchs(matchs, t1, size);
		int i = 1;
		for (MatchInfo match : homeHistories)
		{
			logger.info(i++ + ": " + match);
		}

		List<MatchInfo> clientHistories = TeamHistoryCalculator.getLastMatchs(matchs, t2, size);
		i = 1;
		for (MatchInfo match : clientHistories)
		{
			logger.info(i++ + ": " + match);
		}
	}

	public static void testJcMatchData(LorisContext context) throws Exception
	{
		context.getApplicationContext().getBean(SoccerManager.class);
		MatchDataDownloader downloader = new MatchDataDownloader();
		downloader.setLorisContext(context);
		String issue = "2018-04-27";
		downloader.setIssue(issue);

		downloader.prepare();
		int i = 1;
		while (downloader.hasNextWebPage())
		{
			logger.info(i++ + ": " + downloader.popWebPage());
		}

		downloader.close();
	}

	public static void testMatchHistory(LorisContext context) throws Exception
	{
		context.getApplicationContext().getBean(SoccerManager.class);
		String mid = "2350309";
		MatchHistoryWebPage page = ZgzcwWebPageCreator.createMatchHistoryWebPage(mid);

		// 调取数据
		if (!UrlFetcher.fetch(page))
		{
			logger.info("Error occured when fetch: " + page.toString());
			return;
		}

		MatchHistoryWebPageParser parser = new MatchHistoryWebPageParser();
		if (!parser.parseWebPage(page))
		{
			logger.info("Error when parse the WebPage: " + page);
			return;
		}

		List<Match> matchs = parser.getHistoryMatchs();
		int i = 1;
		for (Match match : matchs)
		{
			logger.info(i++ + ": " + match);
		}
	}

	public static void testNumberParser(LorisContext context) throws Exception
	{
		String time = "开赔时间：赛前61时27分";
		int[] n = NumberUtil.parseAllIntegerFromString(time);
		logger.info("size = " + (n == null ? 0 : n.length));

		if (n != null)
		{
			for (int i : n)
			{
				logger.info(": " + i);
			}
		}
	}

	public static void testOkoooYpData(LorisContext context) throws Exception
	{
		String url = "http://www.okooo.com/soccer/match/962533/ah/";
		OkoooWebPage page = new OkoooWebPage();
		page.setEncoding(Downloader.ENCODING_GB2312);
		page.setUrl(url);
		page.setCreatetime(DateUtil.getCurTimeStr());

		if (!UrlFetcher.fetch(page))
		{
			logger.info("Error occured when fetch: " + page.toString());
			return;
		}

		logger.info("PageContent: " + page.getContent());
	}

	public static void testOkoooLeagueWebPage(LorisContext context) throws Exception
	{
		OkoooWebPage page = new OkoooWebPage();
		page.setEncoding(Downloader.ENCODING_GB2312);
		page.setUrl("http://www.okooo.com/soccer/league/17/");
		page.setCreatetime(DateUtil.getCurTimeStr());
		page.setType("league");

		League league = new League();
		league.setLid("17");
		league.setName("英超");

		if (!UrlFetcher.fetch(page))
		{
			logger.info("Error occured when fetch: " + page.toString());
			return;
		}

		LeaguePageParser parser = new LeaguePageParser();
		parser.setLeague(league);
		int i = 0;
		if (parser.parseWebPage(page))
		{
			logger.info("Parse success");
			List<Team> teams = parser.getTeams();
			for (Team team : teams)
			{
				logger.info(i++ + ": " + team);
			}

			List<SeasonTeam> seasonTeams = parser.getSeasonTeams();
			i = 0;
			for (SeasonTeam seasonTeam : seasonTeams)
			{
				logger.info(i++ + ": " + seasonTeam);
			}

			List<Season> seasons = parser.getSeasons();
			i = 0;
			for (Season season : seasons)
			{
				logger.info(i++ + ": " + season);
			}
		}
		else
		{
			logger.info("Error occured when parse page");
		}

	}

	/**
	 * 测试
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testOkoooSoccerWebPage(LorisContext context) throws Exception
	{
		OkoooWebPage page = new OkoooWebPage();
		page.setEncoding(Downloader.ENCODING_GB2312);
		page.setUrl("http://www.okooo.com/soccer/");
		page.setCreatetime(DateUtil.getCurTimeStr());
		page.setType("soccer");

		if (!UrlFetcher.fetch(page))
		{
			logger.info("Error occured when fetch: " + page.toString());
			return;
		}

		SoccerPageParser parser = new SoccerPageParser();
		int i = 0;
		if (parser.parseWebPage(page))
		{
			logger.info("Parse success");
			List<League> leagues = parser.getLeagues();

			for (League league : leagues)
			{
				logger.info(i++ + ": " + league);
			}

			i = 0;
			List<CountryLogo> logos = parser.getLogos();
			for (CountryLogo countryLogo : logos)
			{
				logger.info(i++ + ": " + countryLogo);
			}
		}
		else
		{
			logger.info("Error occured when parse page");
		}
	}

	/**
	 * 测试Okooo网站数据
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testOkoooOpWebPage(LorisContext context) throws Exception
	{
		String mid = "1026861"; // "1007916";
		String url = OKOOO_OP_BASE_URL + mid + "/odds/";

		WebClient client = new WebClient(BrowserVersion.FIREFOX_52);
		// client.setJavaScriptEnabled(false);
		client.getOptions().setJavaScriptEnabled(true);
		client.getOptions().setCssEnabled(false);
		client.getCookieManager().setCookiesEnabled(true);
		client.setAjaxController(new NicelyResynchronizingAjaxController());
		client.getOptions().setTimeout(5000); // 设置连接超时时间 ，这里是10S。如果为0，则无限期等待
		client.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常
		// client.getOptions().setSSLInsecureProtocol(sslClientProtocols[2]);

		OkoooWebPage page = new OkoooWebPage();
		page.setEncoding(Downloader.ENCODING_GB2312);
		page.setUrl(url);
		page.setCreatetime(DateUtil.getCurTimeStr());
		page.setType("odds");

		HtmlPage page2 = client.getPage(url);
		client.waitForBackgroundJavaScript(10000);
		ScriptResult result = page2.executeJavaScript("window.scrollTo(0, 99999)");
		HtmlPage jspage = (HtmlPage) result.getNewPage();

		page.setCompleted(true);
		page.setContent(jspage.asXml());

		OddsOpPageParser parser = new OddsOpPageParser();
		if (parser.parseWebPage(page))
		{
			logger.info("Parse success");
			Match match = parser.getMatch();
			logger.info("Match: " + match);
			logger.info("HomeName: " + parser.getHomename() + " ClientName: " + parser.getClientname());

			List<OkoooOp> ops = parser.getOps();
			int i = 1;
			for (OkoooOp oddsOp : ops)
			{
				logger.info(i++ + ": " + oddsOp);
			}
		}
		else
		{
			logger.info("Error occured when parse page");
		}

		/*
		 * logger.info("Start get ajax page..."); String urlajax =
		 * "http://www.okooo.com/soccer/match/"+ mid +
		 * "/odds/ajax/?page=1&trnum=30&companytype=BaijiaBooks&type=1";
		 * HtmlPage reponse = client.getPage(urlajax);
		 * logger.info(reponse.asXml());
		 */

		/*
		 * Corporate corporate = new Corporate(); corporate.setGid("14");
		 * corporate.setName("威廉.希尔"); testDownloadOkoooOpPage(context, client,
		 * mid, corporate);
		 * 
		 * Corporate corporate2 = new Corporate(); corporate2.setGid("24");
		 * corporate2.setName("99家平均欧赔"); testDownloadOkoooOpPage(context,
		 * client, mid, corporate2);
		 */

		client.close();
	}

	/**
	 * 测试下载欧赔数据页
	 * 
	 * @param context
	 * @param client
	 * @param corporate
	 * 
	 *            protected static void testDownloadOkoooOpPage(LorisContext
	 *            context, WebClient client, String mid, Corporate corporate)
	 *            throws IOException { // 设置页面 OkoooWebPage page = new
	 *            OkoooWebPage(); page.setEncoding(Downloader.ENCODING_GB2312);
	 *            page.setUrl(OKOOO_OP_BASE_URL + mid + "/odds/change/" +
	 *            corporate.getGid() + "/");
	 *            page.setCreatetime(DateUtil.getCurTimeStr());
	 *            page.setType("op");
	 * 
	 *            if (!UrlFetcher.fetch(page, client)) { logger.info("Error
	 *            occured when fetch: " + page.toString()); return; }
	 * 
	 *            OddsOpPageParser parser = new OddsOpPageParser();
	 *            parser.setMid(mid); int i = 0; if (parser.parseWebPage(page))
	 *            { logger.info("Parse success"); List<OddsOp> ops =
	 *            parser.getOps(); for (OddsOp oddsOp : ops) { logger.info(i++ +
	 *            ": " + oddsOp); }
	 * 
	 *            List<Corporate> corps = parser.getCorps(); i = 1; for
	 *            (Corporate corp : corps) { logger.info(i++ + ": " + corp);
	 *            /*if("24".equals(corp.getGid())) {
	 *            testDownloadOkoooOpPage(context, client, mid, corp); } } }
	 *            else { logger.info("Error occured when parse page"); } }
	 */

	/**
	 * 测试Okooo网站数据
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testOkoooSpfWebPage(LorisContext context) throws Exception
	{
		OkoooWebPage page = new OkoooWebPage();
		page.setEncoding(Downloader.ENCODING_GB2312);
		page.setUrl("http://www.okooo.com/jingcai/shengpingfu/");
		page.setCreatetime(DateUtil.getCurTimeStr());
		page.setType("jc");

		if (!UrlFetcher.fetch(page))
		{
			logger.info("Error occured when fetch: " + page.toString());
			return;
		}

		OkoooJcPageParser parser = new OkoooJcPageParser();
		int i = 0;
		if (parser.parseWebPage(page))
		{
			logger.info("Parse success");
			Map<String, List<OkoooJcMatch>> map = parser.getMatchMap();
			for (String key : map.keySet())
			{
				List<OkoooJcMatch> matchs = map.get(key);

				logger.info("Issue: " + key + " has " + matchs.size() + " matches.");
				i = 0;
				for (JcMatch jcMatch : matchs)
				{
					logger.info(i++ + ": " + jcMatch);
				}
			}
		}
		else
		{
			logger.info("Error occured when parse page");
		}
	}

	public static void testOddsZhishuWebPage(LorisContext context) throws Exception
	{
		String mid = "2373979";
		String gid = "8";
		String gname = "Bet365";
		OddsYpZhishuWebPage yppage = ZgzcwWebPageCreator.createOddsYpZhishuWebPage(mid, gid, gname);

		if (UrlFetcher.fetch(yppage))
		{
			OddsYpZhishuWebPageParser parser = new OddsYpZhishuWebPageParser();
			if (parser.parseWebPage(yppage))
			{
				List<Yp> list = parser.getOddsList();
				int i = 1;
				for (Yp yp : list)
				{
					logger.info(i++ + ": " + yp);
				}
			}
			else
			{
				logger.info("Parse Yp page error.");
			}
		}

		gid = "281";
		gname = "Bet365";
		OddsOpZhishuWebPage oppage = ZgzcwWebPageCreator.createOddsOpZhishuWebPage(mid, gid, gname);

		if (UrlFetcher.fetch(oppage))
		{
			OddsOpZhishuWebPageParser parser = new OddsOpZhishuWebPageParser();
			if (parser.parseWebPage(oppage))
			{
				List<Op> list = parser.getOddsList();
				int i = 1;
				for (Op op : list)
				{
					logger.info(i++ + ": " + op);
				}
			}
			else
			{
				logger.info("Parse Op page error.");
			}
		}
	}

	/**
	 * 测试轮次数据下载器
	 * 
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testOddsDownloader(LorisContext context) throws Exception
	{
		logger.info("Test OddsDownloader");
		OddsDownloader downloader = new OddsDownloader();
		downloader.setLorisContext(context);
		String start = "2018-03-30";
		String end = "2018-04-02";

		downloader.setStart(start);
		downloader.setEnd(end);

		downloader.prepare();

		int i = 1;
		while (downloader.hasNextWebPage())
		{
			// logger.info(i ++ + ":" + downloader.popWebPage());
			WebPage page = downloader.popWebPage();
			logger.info(i++ + ":" + page);

			if ((page instanceof OddsOpWebPage) && UrlFetcher.fetch(page))
			{
				OddsOpWebPageParser parser = new OddsOpWebPageParser();
				if (parser.parseWebPage(page))
				{
					List<Op> list = parser.getOddsList();
					logger.info("Total Op size = " + list.size());
					/*
					 * for (Op op : list) { logger.info("" + op); }
					 */
				}
				break;
			}
		}
		downloader.close();
	}

	public static void testSplitString(LorisContext context) throws Exception
	{
		/*
		 * String st = "20|-|-"; String spl = "\\|"; String[] strings =
		 * st.split(spl); for (String string : strings) { logger.info("Child: "
		 * + string); }
		 */

		String string = "[18] dfadfasd";
		int d = NumberUtil.parseIntegerFromString(string);
		logger.info("Source: " + string);
		logger.info("Result: " + d);
	}

	public static void testLotteryDownloader(LorisContext context) throws Exception
	{
		logger.info("Test LotteryMatchDownloader");
		LotteryMatchDownloader downloader = new LotteryMatchDownloader();

		downloader.setLorisContext(context);
		String start = "2018-02-01";
		String end = "2018-03-01";

		downloader.setStart(start);
		downloader.setEnd(end);

		downloader.prepare();

		int i = 1;
		while (downloader.hasNextWebPage())
		{
			WebPage page = downloader.popWebPage();
			logger.info(i++ + ":" + page);
		}
		downloader.close();
	}

	/**
	 * 测试轮次数据下载器
	 * 
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testRoundDownloader(LorisContext context) throws Exception
	{
		logger.info("Test TeamDownloader");
		RoundMatchDownloader downloader = new RoundMatchDownloader();
		downloader.setLorisContext(context);
		String start = "2018-02-01";
		String end = "2018-03-01";

		downloader.setStart(start);
		downloader.setEnd(end);

		downloader.prepare();

		int i = 1;
		while (downloader.hasNextWebPage())
		{
			// logger.info(i ++ + ":" + downloader.popWebPage());
			WebPage page = downloader.popWebPage();
			logger.info(i++ + ":" + page);
			if (page instanceof RoundCupWebPage)
			{
				if ("103".equals(((RoundCupWebPage) page).getLid()))
				{
					if (UrlFetcher.fetch(page))
					{
						// logger.info(page.getContent());
						downloader.afterDownload(page, true);
						break;
					}
				}
			}
		}
		downloader.close();
	}

	/**
	 * 测试球队信息下载页面
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testTeamWebPageDownloader(LorisContext context) throws Exception
	{
		logger.info("Test TeamDownloader");
		TeamDownloader downloader = new TeamDownloader();
		downloader.setLorisContext(context);

		downloader.prepare();

		int i = 1;
		while (downloader.hasNextWebPage())
		{
			logger.info(i++ + ":" + downloader.popWebPage());
		}
		int total = downloader.totalSize();
		logger.info("TeamDownloader total=" + total + " left=" + downloader.leftSize());
		downloader.close();
	}

	/**
	 * 测试足彩日历数据
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testLotteryCalendarDownloader(LorisContext context) throws Exception
	{
		logger.info("Test LotteryCalendarDownloader...");
		LotteryCalendarDownloader downloader = new LotteryCalendarDownloader();

		String start = "2018-03-21";
		String end = "2018-04-01";

		downloader.setLorisContext(context);
		downloader.setStart(start);
		downloader.setEnd(end);

		downloader.prepare();

		while (downloader.hasNextWebPage())
		{
			LotteryCalendarWebPage page = (LotteryCalendarWebPage) downloader.popWebPage();
			logger.info("Download: " + page);
		}

		downloader.close();

		/*
		 * ZgzcwWebPageCreator ZgzcwWebPageCreator = new ZgzcwWebPageCreator(); String mid =
		 * "2018-03-21"; int num = 10;
		 * 
		 * LotteryCalendarWebPage page =
		 * ZgzcwWebPageCreator.createLotteryCalendarWebPage(mid, num);
		 * logger.info("Downloading " + page);
		 * 
		 * if(UrlFetcher.fetch(page)) { LotteryCalendarWebPageParser parser =
		 * new LotteryCalendarWebPageParser(); if(parser.parseWebPage(page)) {
		 * List<LotteryCalendar> oddsList = parser.getCalendars(); int i = 1;
		 * for (LotteryCalendar yp : oddsList) { logger.info(i +++ ": " + yp); }
		 * } else { logger.info("Error when parsing page."); } } else {
		 * logger.info("Error when downloading page."); }
		 */
	}

	/**
	 * 测试足彩下载数据
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testOddsYpDownloader(LorisContext context) throws Exception
	{
		String mid = "2467763";

		OddsYpWebPage page = ZgzcwWebPageCreator.createOddsYpWebPage(mid);
		logger.info("Downloading " + page);

		if (UrlFetcher.fetch(page))
		{
			OddsYpWebPageParser parser = new OddsYpWebPageParser();
			if (parser.parseWebPage(page))
			{
				List<Yp> oddsList = parser.getOddsList();
				int i = 1;
				for (Yp yp : oddsList)
				{
					logger.info(i++ + ": " + yp);
				}
			}
			else
			{
				logger.info("Error when parsing page.");
			}
		}
		else
		{
			logger.info("Error when downloading page.");
		}
	}

	/**
	 * 测试足彩下载数据
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testOddsOpDownloader(LorisContext context) throws Exception
	{
		String mid = "2249815";

		OddsOpWebPage page = ZgzcwWebPageCreator.createOddsOpWebPage(mid);
		logger.info("Downloading " + page);

		if (UrlFetcher.fetch(page))
		{
			OddsOpWebPageParser parser = new OddsOpWebPageParser();
			if (parser.parseWebPage(page))
			{
				List<Op> oddsList = parser.getOddsList();
				int i = 1;
				for (Op op : oddsList)
				{
					logger.info(i++ + ": " + op);
				}
			}
			else
			{
				logger.info("Error when parsing page.");
			}
		}
		else
		{
			logger.info("Error when downloading page.");
		}
	}

	/**
	 * 测试足彩下载数据
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testLotteryZcDownloader(LorisContext context) throws Exception
	{
		String issue = "18043";

		LotteryWebPage page = ZgzcwWebPageCreator.createZcWebPage(issue);
		logger.info("Downloading " + page);

		if (UrlFetcher.fetch(page))
		{
			LotteryZcWebPageParser parser = new LotteryZcWebPageParser();
			if (parser.parseWebPage(page))
			{
				List<ZcMatch> matchs = parser.getMatches();
				int i = 1;
				for (ZcMatch zcMatch : matchs)
				{
					logger.info(i++ + ": " + zcMatch);
				}
			}
			else
			{
				logger.info("Error when parsing page.");
			}
		}
		else
		{
			logger.info("Error when downloading page.");
		}
	}

	/**
	 * 测试竞彩下载数据
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testLotteryJcDownloader(LorisContext context) throws Exception
	{
		context.getApplicationContext().getBean(SoccerManager.class);
		logger.info("SoccerManager is " + (SoccerManager.getInstance() == null));
		int i = 0;

		String issue = DateUtil.getCurDayStr();

		LotteryWebPage page = ZgzcwWebPageCreator.createJcWebPage(issue);
		logger.info("Downloading " + page);

		if (UrlFetcher.fetch(page))
		{
			LotteryJcWebPageParser parser = new LotteryJcWebPageParser();
			if (parser.parseWebPage(page))
			{
				List<JcMatch> jcMatchs = parser.getJcMatches();
				i = 1;
				for (JcMatch jcMatch : jcMatchs)
				{
					logger.info(i++ + ": " + jcMatch);
					// logger.info("League: " +
					// SoccerManager.getLeagueByName(jcMatch.getLeaguename()));
				}
			}
			else
			{
				logger.info("Error when parsing page.");
			}
		}
		else
		{
			logger.info("Error when downloading page.");
		}
	}

	/**
	 * 测试RoundLeague下载数据
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testLotteryBdDownloader(LorisContext context) throws Exception
	{
		String issue = "80401";

		LotteryWebPage page = ZgzcwWebPageCreator.createBdWebPage(issue);
		logger.info("Downloading " + page);

		if (UrlFetcher.fetch(page))
		{
			LotteryBdWebPageParser parser = new LotteryBdWebPageParser();
			if (parser.parseWebPage(page))
			{
				List<BdMatch> bdMatchs = parser.getMatches();
				int i = 1;
				for (BdMatch bdMatch : bdMatchs)
				{
					logger.info(i++ + ": " + bdMatch);
				}

			}
			else
			{
				logger.info("Error when parsing page.");
			}
		}
		else
		{
			logger.info("Error when downloading page.");
		}
	}

	/**
	 * 测试RoundLeague下载数据
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testTeamDownloader(LorisContext context) throws Exception
	{
		String tid = "165";

		TeamWebPage page = ZgzcwWebPageCreator.createTeamWebPage(tid);
		logger.info("Downloading " + page);

		if (UrlFetcher.fetch(page))
		{
			TeamWebPageParser parser = new TeamWebPageParser();
			if (parser.parseWebPage(page))
			{
				Team team = parser.getTeam();
				logger.info(team);
			}
			else
			{
				logger.info("Error when parsing page.");
			}
		}
		else
		{
			logger.info("Error when downloading page.");
		}
	}

	/**
	 * 创建轮次的比赛时间
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void createSeasonDownloader(LorisContext context) throws Exception
	{
		logger.info("Test Create Round Downloader.");
		String lid = "8";
		// String season = "2017-2018";

		SeasonWebPage page = ZgzcwWebPageCreator.createSeasonWebPage(lid, "", "");
		logger.info("Downloading " + page);

		if (UrlFetcher.fetch(page))
		{
			SeasonWebPageParser parser = new SeasonWebPageParser();
			if (parser.parseWebPage(page))
			{
				List<Team> teams = parser.getTeams();
				int i = 1;
				for (Team team : teams)
				{
					logger.info(i++ + ": " + team);
				}

				i = 1;
				List<SeasonTeam> seasonTeams = parser.getSeasonTeams();
				for (SeasonTeam seasonTeam : seasonTeams)
				{
					logger.info(i++ + ": " + seasonTeam);
				}

				i = 1;
				List<Season> seasons = parser.getSeasons();
				for (Season season : seasons)
				{
					logger.info(i++ + ": " + season);
				}

				i = 1;
				List<Round> rounds = parser.getRounds();
				for (Round round : rounds)
				{
					logger.info(i++ + ": " + round);
				}
			}
			else
			{
				logger.info("Error when parsing page.");
			}
		}
		else
		{
			logger.info("Error when downloading page.");
		}
	}

	/**
	 * 测试RoundLeague下载数据
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testRankDownloader(LorisContext context) throws Exception
	{
		String lid = "8";

		RankWebPage page = ZgzcwWebPageCreator.createRankWebPage(lid);
		logger.info("Downloading " + page);

		if (UrlFetcher.fetch(page))
		{
			RankWebPageParser parser = new RankWebPageParser();
			if (parser.parseWebPage(page))
			{
				List<Rank> ranks = parser.getRanks();
				int i = 1;
				for (Rank rank : ranks)
				{
					logger.info(i++ + ":" + rank);
				}
			}
			else
			{
				logger.info("Error when parsing page.");
			}
		}
		else
		{
			logger.info("Error when downloading page.");
		}
	}

	/**
	 * 测试RoundLeague下载数据
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testRoundLeagueDownloader(LorisContext context) throws Exception
	{
		String lid = "8";
		String season = "2017-2018";
		String round = "6";

		RoundLeagueWebPage page = ZgzcwWebPageCreator.createRoundLeagueWebPage(lid, season, round);
		logger.info("Downloading " + page);

		if (UrlFetcher.fetch(page))
		{
			RoundLeagueWebPageParser parser = new RoundLeagueWebPageParser();
			if (parser.parseWebPage(page))
			{
				List<Match> matchs = parser.getMatches();
				int i = 1;
				for (Match match : matchs)
				{
					logger.info(i++ + ":" + match);
				}

				i = 1;
				List<Team> teams = parser.getTeams();
				for (Team team : teams)
				{
					logger.info(i++ + ": " + team);
				}
			}
			else
			{
				logger.info("Error when parsing page.");
			}
		}
		else
		{
			logger.info("Error when downloading page.");
		}
	}
	
	/**
	 * 配置文件
	 * @return
	 */
	public static ApplicationContext getFileApplicationContext()
	{
		try
		{
			ApplicationContext appContenxt = new FileSystemXmlApplicationContext("file:../conf/spring.xml");
			return appContenxt;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public static LorisContext getLorisContext()
	{
		/** The Application Context. */
		context = new ClassPathXmlApplicationContext("classpath*:soccerApplicationContext.xml");
		LorisContext appContext = new SoccerContext(context);
		return appContext;
	}

	static char[] chars =
	{ '↓', '↑', '→' };

	/**
	 * 标准化亚盘让球数据
	 * 
	 * @param handicap
	 * @return
	 */
	protected static String formatHandicap(String handicap)
	{
		for (char c : chars)
		{
			handicap = handicap.replace("" + c, "");
		}
		// handicap = handicap.replaceAll("↑", "");
		return handicap;
	}

	public static void close()
	{
		try
		{
			if (context != null)
				context.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
