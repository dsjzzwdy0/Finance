package com.loris.soccer.web.downloader.okooo;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.context.LorisContext;
import com.loris.base.util.DateUtil;
import com.loris.base.web.http.UrlFetchException;
import com.loris.base.web.http.WebClientFetcher;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.bean.item.MatchItem;
import com.loris.soccer.bean.okooo.OkoooOp;
import com.loris.soccer.bean.okooo.OkoooYp;
import com.loris.soccer.web.downloader.okooo.page.OkoooRequestHeaderWebPage;
import com.loris.soccer.web.downloader.okooo.page.OkoooWebPage;
import com.loris.soccer.web.downloader.okooo.parser.OddsOpChildParser;
import com.loris.soccer.web.downloader.okooo.parser.OddsOpPageParser;
import com.loris.soccer.web.downloader.okooo.parser.OddsYpChildParser;
import com.loris.soccer.web.downloader.okooo.parser.OddsYpPageParser;
import com.loris.soccer.web.repository.SoccerWebPageManager;

public class OkoooDataDownloader
{
	private static Logger logger = Logger.getLogger(OkoooDataDownloader.class);
	
	/** The SoccerWebPageManager. */
	private static SoccerWebPageManager soccerWebPageManager;
	
	/**
	 * 系统初始化设置
	 * @param context
	 * @return
	 */
	public static boolean initialize(LorisContext context)
	{
		soccerWebPageManager = context.getBean(SoccerWebPageManager.class);
		return false;
	}
	
	/**
	 * 下载澳客的欧赔数据
	 * @param mid
	 * @return
	 */
	public static List<OkoooOp> downloadMatchMainOp(WebClientFetcher fetcher, MatchItem match)
	{
		OkoooWebPage webPage = OkoooPageCreator.createOpWebPage(match.getMid());
		if(fetcher.fetch(webPage))
		{
			OddsOpPageParser parser = new OddsOpPageParser();
			parser.setMid(match.getMid());
			if(parser.parseWebPage(webPage))
			{
				List<OkoooOp> ops = parser.getOps();
				/*int i = 0;
				for (OkoooOp okoooOp : ops)
				{
					logger.info(i +++ ": " + okoooOp);
				}*/
				
				saveOkoooWebPage(webPage);				
				downloadMoreOpPage(fetcher, match.getMid(), ops, 1, 30);
				
				//logger.info("There are total " + parser.getCorpNum() + " corprates.");
				return ops;
			}
			else
			{
				logger.info("Error when parse : " + webPage);
			}
		}
		logger.info("Error when fetch: " + webPage);
		return null;
	}
	
	/**
	 * 下载澳客的亚盘数据
	 * @param mid
	 * @return
	 */
	public static List<OkoooYp> downloadMatchMainYp(WebClientFetcher fetcher, MatchItem match)
	{
		OkoooWebPage webPage = OkoooPageCreator.createYpWebPage(match.getMid());
		if(fetcher.fetch(webPage))
		{
			OddsYpPageParser parser = new OddsYpPageParser();
			parser.setMid(match.getMid());
			parser.setMatchtime(DateUtil.parseDate(match.getMatchtime()));
			if(parser.parseWebPage(webPage))
			{
				List<OkoooYp> yps= parser.getYps();
				
				/*int i = 0;
				for (OkoooYp okoooOp : yps)s
				{
					logger.info(i +++ ": " + okoooOp);
				}*/
				saveOkoooWebPage(webPage);
				downloadMoreYpPage(fetcher, match.getMid(), yps, 1, 30);
				
				//logger.info("There are total " + parser.getCorpNum() + " corprates.");
				return yps;
			}
			else
			{
				logger.info("Error when parse : " + webPage);
			}
		}
		logger.info("Error when fetch: " + webPage);
		return null;
	}
	
	/**
	 * 动态加载各公司的赔率。在Okooo网的设计中，页面的加载不是一次性的，而是分很多次的渐进式加载模式
	 * @param fetcher
	 * @param mid
	 * @param yps
	 * @param startIndex
	 * @param perPageNum
	 */
	protected static void downloadMoreYpPage(WebClientFetcher fetcher, String mid, List<OkoooYp> yps,
			int startIndex, int perPageNum)
	{
		int pageIndex = startIndex;
		Date currentTime = new Date();
		int corpNum = -1;
		//int i = 0;
		while((corpNum <= 0) || (pageIndex * perPageNum < corpNum))
		{
			OkoooRequestHeaderWebPage morePage = OkoooPageCreator.createYpPageWebPage(mid, pageIndex, perPageNum);
			logger.info("Downloading '" + mid + "' page " + pageIndex);
			
			try
			{
				if(download(fetcher, morePage))
				{
					OddsYpChildParser parser = new OddsYpChildParser();
					parser.setCurrentTime(currentTime);
					parser.setMid(mid);
					
					if(parser.parseWebPage(morePage))
					{
						List<OkoooYp> childOps = parser.getYps();
						if(!childOps.isEmpty())
						{
							yps.addAll(childOps);
						}
						/*for (OkoooOp okoooOp : childOps)
						{
							logger.info(i +++ ": " + okoooOp);
						}*/
						
						if(childOps.size() < perPageNum)
						{
							logger.info("The op number is all.");
							break;
						}
					}
					else
					{
						logger.info("Error when parsing : " + morePage);
						break;
					}
					
					pageIndex ++;
				}
				else
				{
					logger.info("Error when downloading : " + morePage);
					break;
				}
			}
			catch(Exception e)
			{
				logger.info("Error when downloading : " + morePage);
				break;
			}
			
			try
			{
				Thread.sleep(10);
			}
			catch(Exception exception)
			{
				logger.info("Error when sleep.");
			}
		}
	}
	
	/**
	 * 动态加载各公司的赔率。在Okooo网的设计中，页面的加载不是一次性的，而是分很多次的渐进式加载模式
	 * @param fetcher
	 * @param mid
	 * @param ops
	 * @param startIndex
	 * @param perPageNum
	 */
	protected static void downloadMoreOpPage(WebClientFetcher fetcher, String mid, List<OkoooOp> ops,
			int startIndex, int perPageNum)
	{
		int pageIndex = startIndex;
		Date currentTime = new Date();
		int corpNum = -1;
		//int i = 0;
		while((corpNum <= 0) || (pageIndex * perPageNum < corpNum))
		{
			OkoooRequestHeaderWebPage morePage = OkoooPageCreator.createOpPageWebPage(mid, pageIndex, perPageNum);
			logger.info("Downloading '" + mid + "' page " + pageIndex);
			
			try
			{
				if(download(fetcher, morePage))
				{
					OddsOpChildParser parser = new OddsOpChildParser();
					parser.setCurrentTime(currentTime);
					parser.setMid(mid);
					
					if(parser.parseWebPage(morePage))
					{
						List<OkoooOp> childOps = parser.getOps();
						if(!childOps.isEmpty())
						{
							ops.addAll(childOps);
						}
						/*for (OkoooOp okoooOp : childOps)
						{
							logger.info(i +++ ": " + okoooOp);
						}*/
						
						if(childOps.size() < perPageNum)
						{
							logger.info("The op number is all.");
							break;
						}
						
						//设置数据
						if(corpNum <= 0)
						{
							corpNum = parser.getCorpNum();
							logger.info("The total corp num is: " + corpNum);
						}
					}
					
					pageIndex ++;
				}
				else
				{
					logger.info("Error when downloading : " + morePage);
					break;
				}
			}
			catch(Exception e)
			{
				logger.info("Error when downloading : " + morePage);
				break;
			}
			
			try
			{
				Thread.sleep(10);
			}
			catch(Exception exception)
			{
				logger.info("Error when sleep.");
			}
		}
	}
	
	/**
	 * 下载数据
	 * @param page 数据页面
	 * @return 下载是否完成的标志
	 */
	public static boolean download(WebClientFetcher fetcher, WebPage page) throws UrlFetchException
	{
		//同步，为了保证不被网站封闭，不能同时下载多个实例
		synchronized (fetcher)
		{
			return fetcher.fetch(page);
		}
	}
	
	/**
	 * 保存澳客数据网页
	 * @param page
	 */
	public static void saveOkoooWebPage(OkoooWebPage page)
	{
		try {
			if(soccerWebPageManager != null)
			{
				soccerWebPageManager.addOrUpdateOkoooWebPage(page);
			}
		}
		catch (Exception e) {
			logger.info("Error when save OkoooWebPage: " + page);
		}
	}
}
