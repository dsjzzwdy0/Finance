package com.loris.soccer.web.downloader.zgzcw.loader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.ArraysUtil;
import com.loris.base.util.ArraysUtil.EqualChecker;
import com.loris.base.util.DateUtil;
import com.loris.base.web.http.UrlFetcher;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.analysis.checker.IssueMatchChecker;
import com.loris.soccer.analysis.checker.MatchChecker;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.item.IssueMatch;
import com.loris.soccer.bean.item.MatchItem;
import com.loris.soccer.bean.table.BdMatch;
import com.loris.soccer.bean.table.JcMatch;
import com.loris.soccer.bean.table.Match;
import com.loris.soccer.bean.table.Op;
import com.loris.soccer.bean.table.Yp;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwSoccerDownloader;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwWebPageCreator;
import com.loris.soccer.web.downloader.zgzcw.page.LotteryWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.MatchHistoryWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.MatchWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsOpWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsYpWebPage;
import com.loris.soccer.web.downloader.zgzcw.parser.LotteryBdWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.LotteryJcWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.MatchHistoryWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.OddsOpWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.OddsYpWebPageParser;

/**
 * 
 * @author jiean
 *
 */
public class IssueMatchDownloader extends ZgzcwSoccerDownloader
{
	private static Logger logger = Logger.getLogger(IssueMatchDownloader.class);
	
	/** 竞彩比赛列表 */
	private List<IssueMatch> issueMatchs = new ArrayList<>();
	
	/** 历史比赛数据 */
	private List<Match> historyMatches = new ArrayList<>();
	
	/** 历史比赛数据 */
	//private List<Match> historyMatches = new ArrayList<>();
		
	/** 竞彩比赛的期号 */
	private String date;
	
	/** 是否检测数据库中的现存数据  */
	protected boolean checkexist = true;
	
	/** 最大间隔毫秒时间 */
	protected long maxInterval = 3600 * 1000;	//毫秒
	
	/**
	 * Checker.
	 * @author jiean
	 *
	 * @param <T>
	 */
	class MatchWebPageChecker<T extends MatchWebPage> implements EqualChecker<T>
	{
		String mid;

		public void setMid(String mid)
		{
			this.mid = mid;
		}
		
		@Override
		public boolean isSameObject(T page)
		{
			if(page.getMid().equals(mid))
			{
				return true;
			}
			return false;
		}		
	}
	
	/**
	 * 
	 * @author jiean
	 *
	 */
	class OddsWebPageChecker<T extends MatchWebPage> extends MatchWebPageChecker<MatchWebPage>
	{
		long time = 0;
		
		public void setTime(long d)
		{
			this.time = d;
		}
		
		public long getTime()
		{
			return time;
		}

		public boolean isSameObject(MatchWebPage page)
		{
			if(!page.getMid().equals(mid))
			{
				return false;
			}
			Date d = DateUtil.tryToParseDate(page.getLoadtime());
			if(d != null && (time - d.getTime() < maxInterval))
			{
				return true;				
			}
			return false;
		}
	}
	
	/** 数据检测器*/
	OddsWebPageChecker<OddsOpWebPage> opChecker = new OddsWebPageChecker<>();
	
	/** 数据检测器*/
	OddsWebPageChecker<OddsYpWebPage> ypChecker = new OddsWebPageChecker<>();
	
	/** 历史数据检测器 */
	MatchWebPageChecker<MatchHistoryWebPage> historyChecker = new MatchWebPageChecker<>();
	
	
	/**
	 * 数据下载准备
	 */
	@Override
	public boolean prepare()
	{
		//下载今天的数据
		if(StringUtils.isEmpty(date))
		{
			date = DateUtil.getCurDayStr();
		}
		
		if(!checkexist || !isIssueMatchDownloaded(SoccerConstants.LOTTERY_BD, date))
		{
			LotteryWebPage bdWebPage = ZgzcwWebPageCreator.createBdWebPage("");
			downloadLotteryPage(bdWebPage);
		}
		else
		{
			List<BdMatch> m2 = soccerManager.getBdMatches(date);
			issueMatchs.addAll(m2);
			logger.info("Get BdMatchs from database, and get " + issueMatchs.size() + " matchs.");
		}
		
		if(!checkexist || !isIssueMatchDownloaded(SoccerConstants.LOTTERY_JC, date))
		{
			LotteryWebPage jcWebPage = ZgzcwWebPageCreator.createJcWebPage(date);
			downloadLotteryPage(jcWebPage);
		}
		
		logger.info("There are " + issueMatchs.size() + " matches to be downloaded. ");
		
		if(issueMatchs != null && issueMatchs.size() >0)
		{
			List<String> mids = new ArrayList<>();
			ArraysUtil.getObjectFieldValue(issueMatchs, mids, BdMatch.class, "mid");
			
			List<OddsOpWebPage> downOpPages = soccerWebPageManager.getDownloadedOddsOpWebPages(mids);
			List<OddsYpWebPage> downYpPages = soccerWebPageManager.getDownloadedOddsYpWebPages(mids);
			List<MatchHistoryWebPage> historyWebPages = soccerWebPageManager.getDownloadedMatchHistoryPages(mids);
			opChecker.setTime(System.currentTimeMillis());
			ypChecker.setTime(opChecker.getTime());
			
			for (IssueMatch match : issueMatchs)
			{
				String mid = match.getMid();
				opChecker.setMid(mid);
				ypChecker.setMid(mid);
				historyChecker.setMid(mid);
				
				//该数据需要下多次
				if(!ArraysUtil.hasSameObject(downOpPages, opChecker))
				{
					OddsOpWebPage opWebPage = ZgzcwWebPageCreator.createOddsOpWebPage(mid);
					pages.put(opWebPage);
				}
				
				if(!ArraysUtil.hasSameObject(downYpPages, ypChecker))
				{
					OddsYpWebPage ypWebPage = ZgzcwWebPageCreator.createOddsYpWebPage(mid);
					pages.put(ypWebPage);
				} 
				
				//该数据只需要下载一次
				if(!ArraysUtil.hasSameObject(historyWebPages, historyChecker))
				{
					//历史数据不再下载
					//MatchHistoryWebPage historyWebPage = creator.createMatchHistoryWebPage(mid);
					//pages.put(historyWebPage);
				}
			}
		}
		
		totalSize = issueMatchs.size() * 4;
		/*
		if(!checkexist || !isIssueMatchDownloaded(SoccerConstants.LOTTERY_JC, issue))
		{
			LotteryWebPage zcWebPage = creator.createZcWebPage(issue);
		}*/
		
		return true;
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
			case SoccerConstants.LOTTERY_JC:
				flag = processJcWebPage(page);
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
			e.printStackTrace();
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
			List<BdMatch> matchs = parser.getMatches();			
			//bdMatchs = new ArrayList<>();
			
			//保留数据
			List<BdMatch> saveMatches = new ArrayList<>();
			IssueMatchChecker<BdMatch> checker = new IssueMatchChecker<>(DateUtil.getCurDayStr());			
			checker.setSame(false);			
			ArraysUtil.getListValues(matchs, saveMatches, checker);
			
			//仅对当天的数据进行下载
			IssueMatchChecker<IssueMatch> checker2 = new IssueMatchChecker<>(DateUtil.getCurDayStr());	
			checker.setSame(true);
			ArraysUtil.getListValues(matchs, issueMatchs, checker2);
			logger.info("The issue '" + "' has " + issueMatchs.size() + " matches.");
			
			saveLotteryWebPage(page);
			
			return soccerManager.addOrUpdateBdMatches(saveMatches);
		}
		return false;
	}
	
	/**
	 * 处理Jc页面数据
	 * 
	 * @param page
	 * @return
	 */
	protected boolean processJcWebPage(LotteryWebPage page)
	{
		LotteryJcWebPageParser parser = new LotteryJcWebPageParser();
		if(parser.parseWebPage(page))
		{
			List<JcMatch> matchs = parser.getJcMatches();
			if(matchs == null || matchs.size() <= 0)
			{
				return false;
			}
			//
			IssueMatchChecker<JcMatch> checker = new IssueMatchChecker<>(DateUtil.getCurDayStr());
			List<JcMatch> saveMatches = new ArrayList<>();
			ArraysUtil.getListValues(matchs, saveMatches, checker);
			
			saveLotteryWebPage(page);
			
			return soccerManager.addOrUpdateJcMatches(saveMatches);
		}
		return false;
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
			//totalSize ++;
			if(!isOddsOpPageNeedToLoad(lastMid))
			{				
				OddsOpWebPage opWebPage = ZgzcwWebPageCreator.createOddsOpWebPage(lastMid);
				pages.put(opWebPage);
			}
			
			/*
			//totalSize ++;
			if(!isOddsYpPageNeedToLoad(lastMid))
			{
				OddsYpWebPage ypWebPage = creator.createOddsYpWebPage(lastMid);
				pages.put(ypWebPage);
			}*/
		}
		
		matchs = excludeExistMatch(matchs);
		synchronized(soccerManager)
		{
			soccerWebPageManager.addOrUpdateMatchHistoryPage(page);
			soccerManager.addOrUpdateMatches(matchs, false);
		}		
	}
	
	
	protected List<Match> excludeExistMatch(List<Match> matchs)
	{
		List<String> mids = new ArrayList<>();
		ArraysUtil.getObjectFieldValue(matchs, Match.class, "mid");
		
		if(mids.size() < 0)
		{
			logger.info("Error when get the matchs.");
			return null;
		}
		
		MatchChecker<Match> checker = new MatchChecker<>();		
		List<Match> matchs2 = soccerManager.getMatches(mids);
		
		List<Match> matchs3 = new ArrayList<>();
		
		for (Match m : matchs)
		{
			checker.setMid(m.getMid());
			if(ArraysUtil.hasSameObject(matchs2, checker))
			{
				continue;
			}
			matchs3.add(m);
		}
		
		return matchs3;
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
	 * 保存数据页面
	 * @param page
	 */
	protected void saveLotteryWebPage(LotteryWebPage page)
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
	
	/**
	 * 检测今天的北单数据是否
	 * @param type 类型
	 * @param issue 日期
	 * @return
	 */
	protected boolean isIssueMatchDownloaded(String type, String issue)
	{
		LotteryWebPage page = soccerWebPageManager.getLotteryPage(type, issue);
		return page == null ? false : true;
	}

	public List<IssueMatch> getIssueMatchs()
	{
		return issueMatchs;
	}

	public String getIssue()
	{
		return date;
	}

	public void setIssue(String issue)
	{
		this.date = issue;
	}

	public boolean isCheckexist()
	{
		return checkexist;
	}

	public void setCheckexist(boolean checkexist)
	{
		this.checkexist = checkexist;
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
		else if(page instanceof OddsYpWebPage)
		{
			processOddsYpWebPage((OddsYpWebPage)page);
		}
		if(page instanceof MatchHistoryWebPage)
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
		
		return null;
	}
	
	/**
	 * 按照比赛编号查找竞彩比赛
	 * 
	 * @param mid 比赛编号
	 * @return 竞彩比赛
	 */
	public IssueMatch getIssueMatch(String mid)
	{
		for (IssueMatch jcMatch : issueMatchs)
		{
			if(mid.equalsIgnoreCase(jcMatch.getMid()))
			{
				return jcMatch;
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
