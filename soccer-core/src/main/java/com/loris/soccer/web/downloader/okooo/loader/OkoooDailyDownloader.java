package com.loris.soccer.web.downloader.okooo.loader;

import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.loris.base.util.ArraysUtil;
import com.loris.base.util.DateUtil;
import com.loris.base.web.http.UrlFetchException;
import com.loris.base.web.http.WebClientFetcher;
import com.loris.base.web.manager.TaskMode;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.bean.data.table.lottery.UserCorporate;
import com.loris.soccer.bean.item.IssueMatch;
import com.loris.soccer.bean.okooo.OkoooBdMatch;
import com.loris.soccer.bean.okooo.OkoooJcMatch;
import com.loris.soccer.bean.okooo.OkoooYp;
import com.loris.soccer.web.downloader.okooo.OkoooDownloader;
import com.loris.soccer.web.downloader.okooo.OkoooPageCreator;
import com.loris.soccer.web.downloader.okooo.page.OkoooRequestHeaderWebPage;
import com.loris.soccer.web.downloader.okooo.page.OkoooWebPage;
import com.loris.soccer.web.downloader.okooo.parser.OddsYpChangeParser;
import com.loris.soccer.web.downloader.okooo.parser.OddsYpChildParser;
import com.loris.soccer.web.downloader.okooo.parser.OddsYpPageParser;
import com.loris.soccer.web.downloader.okooo.parser.OkoooBdPageParser;
import com.loris.soccer.web.downloader.okooo.parser.OkoooJcPageParser;


/**
 * 澳客每日数据下载
 * 
 * @author jiean
 *
 */
public class OkoooDailyDownloader extends OkoooDownloader
{
	private static Logger logger = Logger.getLogger(OkoooDailyDownloader.class);
			
	/** The WebClientFetcher. */
	protected WebClientFetcher fetcher;
	
	/** The Matches. */
	protected List<? extends IssueMatch> matchs = null;
	
	/** 子下载线程中的暂停时间*/
	protected int childInterval = 1000;
	
	/** 今天的期号 */
	protected String issue;
	
	/**
	 * Create a instance of OkoooDailyDownloader.
	 */
	public OkoooDailyDownloader()
	{
		super();
		taskMode = TaskMode.Single;
	}
	
	class DetailPageChecker implements ArraysUtil.EqualChecker<OkoooWebPage>
	{
		Date date;
		String mid;
		String gid;
		
		public void setDate(Date d)
		{
			date = d;
		}

		public void setMid(String mid)
		{
			this.mid = mid;
		}

		public void setGid(String gid)
		{
			this.gid = gid;
		}

		@Override
		public boolean isSameObject(OkoooWebPage obj)
		{
			if(!gid.equals(obj.getGid()))
			{
				return false;
			}
			Date d = DateUtil.tryToParseDate(obj.getLoadtime());
			long interval = date.getTime() - d.getTime();
			if(interval < 3600000)
			{
				return true;
			}			
			return false;
		}
		
	}
	
	
	/**
	 * 数据下载准备
	 */
	@Override
	public boolean prepare()
	{
		OkoooWebPage basePage = creator.createBaseWebPage();
		try
		{
			fetcher = WebClientFetcher.createFetcher(basePage);
			fetcher.waitForBackgroundJavaScript(10000);
			if(!basePage.isCompleted())
			{
				logger.info("Error loading the base page '" + basePage.getFullURL() + ", the prepare process is not success, exit.");
				//logger.info(basePage.getContent());
				return false;
			}
			
			issue = DateUtil.getCurDayStr();
			
			//处理基础页面下载数据
			processBdBaseWebPage(basePage, issue);
			
			//处理页面
			if(matchs == null || matchs.size() == 0)
			{
				logger.info("There are no Matches to be downloaded, exit.");
				return false;
			}
			
			
			List<String> mids = ArraysUtil.getObjectFieldValue(matchs, IssueMatch.class, "mid");
			
			//计算下载页面
			List<OkoooWebPage> downloadedMainYpPages = soccerWebManager.getOkoooYpMainPages(mids);
			List<UserCorporate> corporates = soccerManager.getUserYpCorporates(true, OkoooDownloader.SOURCE_OKOOO);
			
			List<OkoooWebPage> downloadedYpPages = soccerWebManager.getOkoooYpPages(mids, "ypchange");
			
			int matchSize = matchs.size();
			int corpSize = corporates.size();
			OkoooWebPage page = null;
			
			DetailPageChecker checker = new DetailPageChecker();
			checker.setDate(new Date());
			
			for (IssueMatch issueMatch : matchs)
			{
				if(!isMatchOkoooYpPageDownloaded(downloadedMainYpPages, issueMatch.getMid()))
				{
					page = creator.createYpWebPage(issueMatch.getMid());
					pages.put(page);
				}
				checker.setMid(issueMatch.getMid());
				
				for (UserCorporate userCorporate : corporates)
				{
					checker.setGid(userCorporate.getGid());					
					if(ArraysUtil.hasSameObject(downloadedYpPages, checker))
					{
						continue;
					}
					page = creator.createYpChangeWebPage(issueMatch.getMid(), userCorporate.getGid());
					pages.put(page);
				}
			}
			
			totalSize = matchSize + corpSize * matchSize;
			//logger.info("There are total " + matchSize + " yp pages to be downloaded.");
			logger.info("There are total " + totalSize + " and left " + pages.length() + " pages to be downloaded.");
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.info("Error when prepare for the OkoooDailyDownloader, exception is '" + e.toString() + "'.");
			return false;
		}
	}
	
	/**
	 * 检查是否已经下载数据
	 * @param pages 已经下载的数据
	 * @param mid 被检测的比赛
	 * @return 是否已经下载的标志
	 */
	public boolean isMatchOkoooYpPageDownloaded(List<OkoooWebPage> pages, String mid)
	{
		if(pages == null)
		{
			return false;
		}
		for (OkoooWebPage okoooWebPage : pages)
		{
			if(mid.equalsIgnoreCase(okoooWebPage.getMid()))
			{
				return true;
			}
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
			logger.info("The page is not successful to download, this will not be processed: '" + page + "");
			return;
		}
		
		OkoooWebPage page2 = (OkoooWebPage)page;
		
		//亚盘主页数据
		if(OkoooPageCreator.PAGE_TYPES[6].equalsIgnoreCase(page.getType()))
		{
			processJcBaseWebPage(page2, issue);
		}
		else if(OkoooPageCreator.PAGE_TYPES[2].equalsIgnoreCase(page.getType()))
		{
			processMainYpWebPage(page2);
		}
		else if(OkoooPageCreator.PAGE_TYPES[4].equalsIgnoreCase(page.getType()))
		{
			processYpChangeWebPage(page2);
		}
		super.afterDownload(page2, flag);
	}
	
	/**
	 * 解析亚盘主页面数据
	 * @param page
	 */
	protected void processMainYpWebPage(OkoooWebPage page)
	{
		String mid = page.getMid();
		IssueMatch match = getMatch(mid);
		
		//不存在该场比赛，则不进行任何处理
		if(match == null)
		{
			return;
		}
		
		OddsYpPageParser parser = new OddsYpPageParser();
		parser.setMatchtime(DateUtil.parseDate(match.getMatchtime()));
		parser.setMid(mid);
		
		List<OkoooYp> yps = new ArrayList<>();
		
		if(parser.parseWebPage(page))
		{
			List<OkoooYp> firstYps = parser.getYps();
			yps.addAll(firstYps);
			
			//下载数据
			downMoreYPRecords(match, yps, 1);
		}
		
		//保存WEB下载的页面
		synchronized(soccerWebManager)
		{
			soccerWebManager.addOrUpdateOkoooWebPage(page);
		}
		
		//保存亚盘数据
		synchronized (soccerManager)
		{
			soccerManager.addOkoooYpList(yps);
		}
	}
	
	/**
	 * 下载更多的亚盘数据
	 * @param match
	 * @param yps
	 */
	protected void downMoreYPRecords(IssueMatch match, List<OkoooYp> yps, int pageIndex)
	{
		//为了不被网站封停，需要对下载时间作一些调整，暂停下载数据
		try
		{
			Thread.sleep(childInterval);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		try
		{
			OkoooRequestHeaderWebPage morePage = creator.createYpPageWebPage(match.getMid(), pageIndex);
			logger.info("Downloading '" + match.getMid() + "' page " + pageIndex);
			
			//数据下载
			if(!download(morePage))
			{
				return;
			}
			OddsYpChildParser parser = new OddsYpChildParser();
			parser.setMid(match.getMid());			
			parser.setCurrentTime(DateUtil.tryToParseDate(morePage.getLoadtime()));
			
			//解析数据
			if(parser.parseWebPage(morePage))
			{
				//int k = 1;
				List<OkoooYp> moreYps = parser.getYps();
				yps.addAll(moreYps);
				
				//判断是否有更多的数据需要下载
				if(moreYps.size() >= 30)
				{
					downMoreYPRecords(match, yps, (pageIndex +1));
				}
			}
		}
		catch(Exception e)
		{
			logger.info("Error when downloading more OkoooYpPages: " + pageIndex);
		}
		return;
	}
	
	/**
	 * 处理澳客网具体亚盘数据页面
	 * @param page 数据页面
	 */
	protected void processYpChangeWebPage(OkoooWebPage page)
	{
		String mid = page.getMid();
		IssueMatch match = getMatch(mid);
		
		//不存在该场比赛，则不进行任何处理
		if(match == null)
		{
			return;
		}
		
		OddsYpChangeParser parser = new OddsYpChangeParser();
		//parser.setMatchtime(DateUtil.parseDate(match.getMatchtime()));
		parser.setMid(mid);
		
		if(parser.parseWebPage(page))
		{
			List<OkoooYp> yps = parser.getOddslist();
			soccerManager.addNewOkoooYpList(mid, yps);
		}
		
		synchronized(soccerWebManager)
		{
			soccerWebManager.addOrUpdateOkoooWebPage(page);
		}
	}
	
	protected void processBdBaseWebPage(OkoooWebPage page, String issue)
	{
		OkoooBdPageParser parser = new OkoooBdPageParser();
		if(parser.parseWebPage(page))
		{
			logger.info("Success to parse Bd WebPage " + page.getFullURL() + ".");
			Map<String, List<OkoooBdMatch>> pairs = parser.getMatchMap();
			
			//List<OkoooBdMatch> ms = pairs.get(issue);
			//String today = DateUtil.getCurDayStr();
			
			//加入数据库
			soccerWebManager.addOrUpdateOkoooWebPage(page);
			
			List<OkoooBdMatch> matchList = new ArrayList<>();
			for (String key : pairs.keySet())
			{
				if(issue.compareTo(key) < 0)
				{
					continue;
				}
				List<OkoooBdMatch> list = pairs.get(key);
				if(list != null && list.size() > 0)
				{
					matchList.addAll(list);
				}
				
				if(issue.equals(key))
				{
					matchs = pairs.get(key);
					logger.info("Match Issue '" + issue + "'has " + (matchs != null ? matchs.size() : 0 ) + " matchs");
				}
			}
			
			//加入数据库中
			if(matchList != null && matchList.size() > 0)
			{
				soccerManager.addOrUpdateOkoooBdMatches(matchList);
			}
		}
		else
		{
			logger.info(page.getContent());
			logger.info("Error occured when parsing the base page '" + page.getFullURL() + ".");
		}
	}
	
	/**
	 * 处理主页面数据
	 * 
	 * @param page 基本页面
	 */
	protected void processJcBaseWebPage(OkoooWebPage page, String issue)
	{
		OkoooJcPageParser parser = new OkoooJcPageParser();
		if(parser.parseWebPage(page))
		{
			logger.info("Parsing success.");
			//String today = DateUtil.getCurDayStr();
			Map<String, List<OkoooJcMatch>> pairs = parser.getMatchMap();
			
			List<OkoooJcMatch> ms = pairs.get(issue);
			logger.info("JcMatch Issue '" + issue + "'has " + (ms != null ? ms.size() : 0 ) + " matchs"); 
			
			//加入数据库
			soccerWebManager.addOrUpdateOkoooWebPage(page);
			
			//加入数据库中
			if(ms != null && ms.size() > 0)
			{
				soccerManager.addOrUpdateOkoooJcMatches(ms);
			}
		}
		else
		{
			logger.info(page.getContent());
			logger.info("Error occured when parsing the base page '" + page.getFullURL() + ".");
		}
	}
	
	public int getChildInterval()
	{
		return childInterval;
	}


	public void setChildInterval(int childInterval)
	{
		this.childInterval = childInterval;
	}


	/**
	 * 查找竞彩比赛数据
	 * @param mid 比赛编号
	 * @return 竞彩比赛
	 */
	protected IssueMatch getMatch(String mid)
	{
		for (IssueMatch match : matchs)
		{
			if(mid.equalsIgnoreCase(match.getMid()))
			{
				return match;
			}
		}
		return null;
	}
	
	
	/**
	 * 下载数据
	 * @param page 数据页面
	 * @return 下载是否完成的标志
	 */
	@Override
	public boolean download(WebPage page) throws UrlFetchException
	{
		if(fetcher == null)
		{
			return false;
		}
		
		//同步，为了保证不被网站封闭，不能同时下载多个实例
		synchronized (fetcher)
		{
			return fetcher.fetch(page);
		}
	}
	
	/**
	 * Close the downloader.
	 */
	@Override
	public void close() throws IOException
	{
		try
		{
			fetcher.close();
			fetcher = null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		super.close();
		
	}
}
