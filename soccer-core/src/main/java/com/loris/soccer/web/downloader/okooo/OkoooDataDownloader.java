package com.loris.soccer.web.downloader.okooo;

import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.web.http.UrlFetchException;
import com.loris.base.web.http.WebClientFetcher;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.bean.okooo.OkoooOp;
import com.loris.soccer.web.downloader.okooo.page.OkoooRequestHeaderWebPage;
import com.loris.soccer.web.downloader.okooo.page.OkoooWebPage;
import com.loris.soccer.web.downloader.okooo.parser.OddsOpPageParser;

public class OkoooDataDownloader
{
	private static Logger logger = Logger.getLogger(OkoooDataDownloader.class);
	
	/**
	 * 下载澳客的欧赔数据
	 * @param mid
	 * @return
	 */
	public static WebPage downloadMatchMainOp(WebClientFetcher fetcher, String mid)
	{
		OkoooWebPage webPage = OkoooPageCreator.createOpWebPage(mid);
		if(fetcher.fetch(webPage))
		{
			OddsOpPageParser parser = new OddsOpPageParser();
			parser.setMid(mid);
			if(parser.parseWebPage(webPage))
			{
				List<OkoooOp> ops = parser.getOps();
				int i = 0;
				for (OkoooOp okoooOp : ops)
				{
					logger.info(i +++ ": " + okoooOp);
				}
				
				logger.info("There are total " + parser.getCorpNum() + " corprates.");
				
				return webPage;
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
	public static WebPage downloadMatchMainYp(String mid)
	{
		return null;
	}
	
	/**
	 * 下载更多的页面。
	 * @param fetcher
	 * @param mid
	 * @param startIndex
	 */
	protected static void downloadMorePage(WebClientFetcher fetcher, String mid, int startIndex, int perPageNum)
	{
		int pageIndex = startIndex;
		while(true)
		{
			OkoooRequestHeaderWebPage morePage = OkoooPageCreator.createOpPageWebPage(mid, pageIndex);
			logger.info("Downloading '" + mid + "' page " + startIndex);
			
			try
			{
				if(download(fetcher, morePage))
				{
					
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
}
