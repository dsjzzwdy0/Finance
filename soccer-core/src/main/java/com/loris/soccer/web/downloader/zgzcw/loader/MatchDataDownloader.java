package com.loris.soccer.web.downloader.zgzcw.loader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.ArraysUtil;
import com.loris.base.util.DateUtil;
import com.loris.base.web.http.UrlFetcher;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.analysis.checker.IssueMatchChecker;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.data.table.league.Match;
import com.loris.soccer.bean.data.table.lottery.BdMatch;
import com.loris.soccer.bean.data.table.lottery.JcMatch;
import com.loris.soccer.bean.data.table.lottery.UserCorporate;
import com.loris.soccer.bean.data.table.odds.Op;
import com.loris.soccer.bean.data.table.odds.Yp;
import com.loris.soccer.bean.item.IssueMatch;
import com.loris.soccer.bean.item.MatchItem;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwSoccerDownloader;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwWebPageCreator;
import com.loris.soccer.web.downloader.zgzcw.page.LotteryWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.MatchHistoryWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.MatchWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.MatchZhishuWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsOpWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsOpZhishuWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsYpWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsYpZhishuWebPage;
import com.loris.soccer.web.downloader.zgzcw.parser.LotteryBdWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.LotteryJcWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.MatchHistoryWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.OddsOpWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.OddsOpZhishuWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.OddsYpWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.OddsYpZhishuWebPageParser;

/**
 * 竞彩实时数据下载管理器，下载的内容包括：
 * <br/>
 * 1、竞彩数据下载面页：每次需重新下载，因为需要动态更新比赛结果；
 * <br/>
 * 2、每场比赛的欧赔数据：第一次下载百家欧赔页面和重点欧赔公司的详细页面。
 * <br/>
 * 3、每场比赛的亚盘数据：第一次下载亚盘的页面和重点亚盘公司 的详细页面。
 * <br/>
 * 4、每场比赛的大小球盘数据：只需要下载一次数据即可。
 * <br/>
 * 5、每场比赛的历史比赛数据：此只需要下载一次即可，不需要重复下载。
 * <br/>
 * 6、最近一场对阵的欧盘数据与亚盘数据：也只需要下载一次。
 * <br/>
 * 
 * @author Administrator
 *
 */
public class MatchDataDownloader extends ZgzcwSoccerDownloader
{
	private static Logger logger = Logger.getLogger(MatchDataDownloader.class);
	
	/** 竞彩比赛列表 */
	private List<BdMatch> matchs = new ArrayList<>();
	
	/** 历史比赛数据 */
	private List<Match> historyMatches = new ArrayList<>();
	
	/** 指定的欧赔公司 */
	private List<UserCorporate> userDefinedOpCorps; // = new ArrayList<>();
	
	/** 指定的亚盘公司 */
	private List<UserCorporate> userDefinedYpCorps; // = new ArrayList<>();
	
	/** 竞彩比赛的期号 */
	private String issue;
	
	/** 需要更新的数据时间，以小时计 */
	private double needRefreshIntervalHour = 1.0;
	
	/**
	 * 数据下载准备
	 */
	@Override
	public boolean prepare()
	{
		if(StringUtils.isEmpty(issue))
		{
			issue = DateUtil.getCurDayStr();
		}
		
		matchs = soccerManager.getBdMatches(issue);
		if(matchs == null || matchs.size() <= 0)
		{
			LotteryWebPage webPage = ZgzcwWebPageCreator.createBdWebPage(issue);
			downloadLotteryPage(webPage);
		}
		
		// 如果没有比赛的话，则不进行数据的下载
		if (matchs == null || matchs.size() <= 0)
		{
			return false;
		}
		
		// 亚盘公司与欧赔公司
		userDefinedYpCorps = soccerManager.getUserYpCorporates(true);
		userDefinedOpCorps = soccerManager.getUserOpCorporates(true);
		
		logger.info("欧赔数据下载: " + userDefinedOpCorps);
		logger.info("亚盘数据下载: " + userDefinedYpCorps);
		
		List<String> mids = ArraysUtil.getObjectFieldValue(matchs, IssueMatch.class, "mid");
		
		//List<OddsOpWebPage> opWebPages = soccerWebPageManager.getDownloadedOddsOpWebPages(mids);
		//List<OddsYpWebPage> ypWebPages = soccerWebPageManager.getDownloadedOddsYpWebPages(mids);
		//List<MatchHistoryWebPage> historyWebPages = soccerWebPageManager.getDownloadedMatchHistoryPages(mids);
		
		List<OddsOpZhishuWebPage> opZhishuWebPages = soccerWebPageManager.getDownloadedOddsOpZhishuPages(mids);
		List<OddsYpZhishuWebPage> ypZhishuWebPages = soccerWebPageManager.getDownloadedOddsYpZhishuPages(mids);
		
		Date date = new Date();
		String mid;
		for (IssueMatch match : matchs)
		{
			mid = match.getMid();
			
			// 是否需要下载欧赔数据
			/*
			if (staticDataNeedToLoad(opWebPages, mid, date))
			{
				OddsOpWebPage opWebPage = creator.createOddsOpWebPage(mid);
				pages.put(opWebPage);
			}
			
			// 是否需要下载亚盘数据
			if (staticDataNeedToLoad(ypWebPages, mid, date))
			{
				OddsYpWebPage ypWebPage = creator.createOddsYpWebPage(mid);
				pages.put(ypWebPage);
			}
			
			// 比赛历史是否需要下载
			if (staticDataNeedToLoad(historyWebPages, mid, date))
			{
				MatchHistoryWebPage historyWebPage = creator.createMatchHistoryWebPage(mid);
				pages.put(historyWebPage);
			}*/
			
			// 欧赔数据下载
			for (UserCorporate corporate : userDefinedOpCorps)
			{
				if (dynamicDataNeedToLoad(opZhishuWebPages, mid, corporate.getGid(), date))
				{
					OddsOpZhishuWebPage opZhishuPage = ZgzcwWebPageCreator.createOddsOpZhishuWebPage(mid,
							corporate.getGid(), corporate.getName());
					pages.put(opZhishuPage);
				}
			}
			
			// 亚盘数据下载
			for (UserCorporate corporate : userDefinedYpCorps)
			{
				if (dynamicDataNeedToLoad(ypZhishuWebPages, mid, corporate.getGid(), date))
				{
					OddsYpZhishuWebPage ypZhishuPage = ZgzcwWebPageCreator.createOddsYpZhishuWebPage(mid, corporate.getGid(),
					        corporate.getName());
					pages.put(ypZhishuPage);
				}
			}
		}
		
		return true;		
	}
	
	@Override
	public void afterPrepared()
	{
		int matchSize = matchs.size();
		totalSize = matchSize * 3 + matchSize * userDefinedOpCorps.size() + matchSize * userDefinedYpCorps.size();
		logger.info("There are total " + totalSize + " and left " + pages.length() + " pages to be downloaded.");
	}
	
	
	/**
	 * 下载数据页面
	 * @param page 页面
	 */
	protected void downloadLotteryPage(LotteryWebPage page)
	{
		try
		{
			if(!UrlFetcher.fetch(page))
			{
				logger.info("Error when download: " + page + ".");
				return;
			}
			
			boolean flag = false;
			switch(page.getType())
			{
			case SoccerConstants.LOTTERY_BD:
				flag = processBdWebPage(page);
				break;
			default:
				logger.info("There are no defined type: " + page.getType());
				break;
			}
			
			if(!flag)
			{
				logger.info("Error when process page: " + page);
			}
			//afterDownload(page, true);
		}
		catch (Exception e) {
			logger.info(e.toString());
		}
	}
	
	/**
	 * 北单数据页面下载处理器
	 * @param page 数据页
	 * @return 返回是否成功的标志
	 */
	protected boolean processBdWebPage(LotteryWebPage page)
	{
		LotteryBdWebPageParser parser = new LotteryBdWebPageParser();
		if(parser.parseWebPage(page))
		{
			List<BdMatch> bdMatchs = parser.getMatches();			
			matchs = new ArrayList<>();
			
			//保留数据
			List<BdMatch> saveMatches = new ArrayList<>();
			IssueMatchChecker<BdMatch> checker = new IssueMatchChecker<>(DateUtil.getCurDayStr());
			ArraysUtil.getListValues(bdMatchs, saveMatches, checker);
			
			//仅对当天的数据进行下载
			checker.setSame(true);
			ArraysUtil.getListValues(bdMatchs, matchs, checker);
			
			return soccerManager.addOrUpdateBdMatches(saveMatches);
		}
		return false;
	}
	
	
	
	/**
	 * 后处理下载的数据页面
	 * 
	 * @param page
	 *            下载的数据页面
	 * @param flag
	 *            下载是否成功的标识
	 */
	@Override
	public void afterDownload(WebPage page, boolean flag)
	{
		if(!flag)
		{
			return;
		}
		
		//进行分发处理
		if(page instanceof OddsOpWebPage)
		{
			processOddsOpWebPage((OddsOpWebPage)page);
		}
		else if(page instanceof OddsOpZhishuWebPage)
		{
			processOddsOpZhishuWebPage((OddsOpZhishuWebPage)page);
		}
		else if(page instanceof OddsYpWebPage)
		{
			processOddsYpWebPage((OddsYpWebPage)page);
		}
		else if(page instanceof OddsYpZhishuWebPage)
		{
			processOddsYpZhishuWebPage((OddsYpZhishuWebPage)page);
		}
		else if(page instanceof MatchHistoryWebPage)
		{
			processMatchHistoryPage((MatchHistoryWebPage)page);
		}
		
		super.afterDownload(page, flag);
	}
	
	/**
	 * 处理欧赔主页数据
	 * 
	 * @param page 欧赔主页数据
	 */
	protected void processOddsOpWebPage(OddsOpWebPage page)
	{
		OddsOpWebPageParser parser = new OddsOpWebPageParser();
		if(!parser.parseWebPage(page))
		{
			logger.info("Error occured when parser " + page + ".");
			return;
		}
		
		//解析得到欧赔数据
		List<Op> ops = parser.getOddsList();
		synchronized (soccerManager)
		{
			soccerWebPageManager.addOrUpdateOpWebPage(page);
			soccerManager.addNewOpList(page.getMid(), ops);
		}		
	}
	
	/**
	 * 处理欧赔详细指数据数据
	 * 
	 * @param page 欧赔详细指数据数据 
	 */
	protected void processOddsOpZhishuWebPage(OddsOpZhishuWebPage page)
	{
		OddsOpZhishuWebPageParser parser = new OddsOpZhishuWebPageParser();
		if(!parser.parseWebPage(page))
		{
			logger.info("Error occured when parser " + page + ".");
			return;
		}
		
		//解析得到欧赔数据
		List<Op> ops = parser.getOddsList();
		synchronized (soccerManager)
		{
			soccerWebPageManager.addOrUpdateOddsOpZhishuPage(page);
			soccerManager.addNewOpList(page.getMid(), ops);
		}	
	}
	
	/**
	 * 处理亚盘主页数据
	 * 
	 * @param page 亚盘主页数据
	 */
	protected void processOddsYpWebPage(OddsYpWebPage page)
	{
		OddsYpWebPageParser parser = new OddsYpWebPageParser();
		String mid = page.getMid();
		
		MatchItem match = getMatchItem(mid);
		if(match != null)
		{
			Date date = DateUtil.tryToParseDate(match.getMatchtime());
			parser.setMatchTime(date);
		}
		
		if(!parser.parseWebPage(page))
		{
			logger.info("Error occured when parser " + page + ".");
			return;
		}
		
		List<Yp> yps = parser.getOddsList();
		synchronized (soccerManager)
		{
			soccerWebPageManager.addOrUpdateYpWebPage(page);
			soccerManager.addNewYpList(page.getMid(), yps);
		}
	}
	
	/**
	 * 处理业盘详细指数据数据
	 * 
	 * @param page 亚盘详细指数据数据 
	 */
	protected void processOddsYpZhishuWebPage(OddsYpZhishuWebPage page)
	{
		OddsYpZhishuWebPageParser parser = new OddsYpZhishuWebPageParser();
		if(!parser.parseWebPage(page))
		{
			logger.info("Error occured when parser " + page + ".");
			return;
		}
		
		List<Yp> yps = parser.getOddsList();
		synchronized (soccerManager)
		{
			soccerWebPageManager.addOrUpdateOddsYpZhishuPage(page);
			soccerManager.addNewYpList(page.getMid(), yps);
		}
	}
	
	/**
	 * 处理比赛历史数据
	 * 
	 * @param page 比赛历史数据页面
	 */
	protected void processMatchHistoryPage(MatchHistoryWebPage page)
	{
		MatchHistoryWebPageParser parser = new MatchHistoryWebPageParser();
		if(!parser.parseWebPage(page))
		{
			logger.info("Error occured when parser " + page + ".");
			return;
		}
		
		List<Match> matchs = parser.getHistoryMatchs();
		String mid = page.getMid();
		IssueMatch match = getIssueMatch(mid);
		
		//将两队最近交锋的欧赔数据和亚盘数据下载下来
		Match lastMatch = getLastMatch(matchs, match.getHomeid(), match.getClientid());
		if(lastMatch != null)
		{
			historyMatches.add(lastMatch);
			String lastMid = lastMatch.getMid();
			totalSize ++;
			if(!isOddsOpPageNeedToLoad(lastMid))
			{				
				OddsOpWebPage opWebPage = ZgzcwWebPageCreator.createOddsOpWebPage(lastMid);
				pages.put(opWebPage);
			}
			
			totalSize ++;
			if(!isOddsYpPageNeedToLoad(lastMid))
			{
				OddsYpWebPage ypWebPage = ZgzcwWebPageCreator.createOddsYpWebPage(lastMid);
				pages.put(ypWebPage);
			}
		}
		
		synchronized(soccerManager)
		{
			soccerWebPageManager.addOrUpdateMatchHistoryPage(page);
			soccerManager.addOrUpdateMatches(matchs, false);
		}
		
	}
	
	/**
	 * 处理竞彩数据下载页面
	 * 
	 * @param page
	 *            数据页面
	 */
	protected void processJcMatchWebPage(LotteryWebPage page)
	{
		if (!SoccerConstants.LOTTERY_JC.equalsIgnoreCase(page.getType()))
		{
			return;
		}
		
		LotteryJcWebPageParser parser = new LotteryJcWebPageParser();
		if (!parser.parseWebPage(page))
		{
			return;
		}
		List<JcMatch> matchs = parser.getJcMatches();
		for (JcMatch jcMatch : matchs)
		{
			if (issue.equalsIgnoreCase(jcMatch.getIssue()))
			{
				matchs.add(jcMatch);
			}
		}
		// 添加到数据库中
		synchronized (soccerManager)
		{
			soccerManager.addOrUpdateJcMatches(matchs);
			soccerWebPageManager.addOrUpdateLotteryWebPage(page);
		}
	}
	
	/**
	 * 静态数据，只需要下载一次。 判断是否需要重新下载
	 * 
	 * @param mid
	 *            比赛编号
	 * @param downloadedPages
	 *            已经下载页面
	 * @param date
	 *            当前日期
	 * @return 是否需要下载的标志
	 */
	protected boolean staticDataNeedToLoad(List<? extends MatchWebPage> downloadedPages, String mid, Date date)
	{
		for (MatchWebPage webPage : downloadedPages)
		{
			if (webPage.getMid().equals(mid))
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 检测系统中是否已经下载了竞彩数据页面
	 * 
	 * @param issue 竞彩期号
	 * @return 是否下载的标志
	 */
	protected boolean isJcWebPageDownloaded(String issue)
	{
		return soccerWebPageManager.hasExistLotteryWebPage(issue, SoccerConstants.LOTTERY_JC);
	}
	
	/**
	 * 动态数据，需要动态进行跟踪 判断是否需要重新下载，这里对时间进行判断
	 * 
	 * @param mid
	 *            比赛编号
	 * @param downloadedPages
	 *            已经下载页面
	 * @param date
	 *            当前日期
	 * @return 是否需要下载的标志
	 */
	protected boolean dynamicDataNeedToLoad(List<? extends MatchZhishuWebPage> downloadedPages, String mid, String gid,
	        Date date)
	{
		Date loadDate;
		for (MatchZhishuWebPage webPage : downloadedPages)
		{
			if (webPage.getMid().equals(mid) && gid.equalsIgnoreCase(webPage.getGid()))
			{
				loadDate = DateUtil.parseDate(webPage.getLoadtime());
				long t = loadDate.getTime();
				long ct = date.getTime();
				
				long dif = Math.abs(ct - t);
				double difHour = dif / (3600000);
				
				if (difHour <= needRefreshIntervalHour)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 查看欧赔数据是否需要下载
	 * 
	 * @param mid 比赛编号
	 * @return 是否需要下载的标志
	 */
	protected boolean isOddsOpPageNeedToLoad(String mid)
	{
		return soccerWebPageManager.hasDownloadOddsOpWebPage(mid);
	}
	
	/**
	 * 查看欧赔数据是否需要下载
	 * 
	 * @param mid 比赛编号
	 * @return 是否需要下载的标志
	 */
	protected boolean isOddsYpPageNeedToLoad(String mid)
	{
		return soccerWebPageManager.hasDownloadOddsYpWebPage(mid);
	}
	
	
	/**
	 * 获得竞彩期号
	 * 
	 * @return 竞彩期号
	 */
	public String getIssue()
	{
		return issue;
	}
	
	/**
	 * 设置起始日期
	 * @param start 日期
	 */
	@Override
	public void setStart(String start)
	{
		this.issue = start;
	}
	
	public void setIssue(String issue)
	{
		this.issue = issue;
	}
	
	public List<? extends IssueMatch> getJcmatchs()
	{
		return matchs;
	}
	
	public double getNeedRefreshIntervalHour()
	{
		return needRefreshIntervalHour;
	}
	
	public void setNeedRefreshIntervalHour(double needRefreshIntervalHour)
	{
		this.needRefreshIntervalHour = needRefreshIntervalHour;
	}
	
	/**
	 * 按照比赛编号查找竞彩比赛
	 * 
	 * @param mid 比赛编号
	 * @return 竞彩比赛
	 */
	public IssueMatch getIssueMatch(String mid)
	{
		for (IssueMatch jcMatch : matchs)
		{
			if(mid.equalsIgnoreCase(jcMatch.getMid()))
			{
				return jcMatch;
			}
		}
		return null;
	}
	
	/**
	 * 查找比赛信息数据
	 * @param mid 比赛编号
	 * @return 比赛元素
	 */
	public MatchItem getMatchItem(String mid)
	{
		MatchItem match = getIssueMatch(mid);
		if(match != null)
		{
			return match;
		}
		
		for (Match m : historyMatches)
		{
			if(mid.equals(m.getMid()))
			{
				return m;
			}
		}
		return null;
	}
	
	/**
	 * 获得两队最近时间的一场比赛
	 * 
	 * @param hid
	 * @param cid
	 * @return
	 */
	public Match getLastMatch(List<Match> matches, String hid, String cid)
	{
		Match m = null;
		for (Match match : matches)
		{
			if(!match.isTwoTeam(hid, cid))
			{
				continue;
			}
			if(m == null)
			{
				m = match;
				continue;
			}
			
			if(match.getMatchtime().compareTo(m.getMatchtime()) > 0)
			{
				m = match;
			}
		}
		return m;
	}
}
