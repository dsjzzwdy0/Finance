package com.loris.soccer.web.downloader.okooo;

import com.loris.base.web.http.WebClientFetcher;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.web.downloader.okooo.page.OkoooWebPage;
import com.loris.soccer.web.downloader.okooo.parser.OddsOpMainPageParser;

public class OkoooDataDownloader
{
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
			OddsOpMainPageParser parser = new OddsOpMainPageParser();
			
			
			if(parser.parseWebPage(webPage))
			{
				
			}
		}
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
	
}
