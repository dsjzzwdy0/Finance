package com.loris.soccer.web.downloader.eastsoccer;

import org.apache.log4j.Logger;

import com.loris.base.bean.wrapper.Result;
import com.loris.base.web.http.UrlFetchException;
import com.loris.base.web.http.UrlFetcher;
import com.loris.base.web.page.WebPage;

public class EastSoccerDownloader
{
	private static Logger logger = Logger.getLogger(EastSoccerDownloader.class);
	
	/**
	 * 下载欧赔数据
	 * @param mid
	 * @return
	 * @throws UrlFetchException
	 */
	public static Result downloadMatchOps(String mid) throws UrlFetchException
	{
		WebPage page = EastSoccerWebPageCreator.createMatchOddsPage(mid);
		if(UrlFetcher.fetch(page))
		{
			//logger.info("Content: " + page.getContent());
			logger.info("Success to download: " + page.getUrl());
			return EastSoccerWebPageParser.parseMatchOps(page);		
		}
		else
		{
			logger.info("Error when downloading: " + page);
		}
		return null;
	}
	
}
