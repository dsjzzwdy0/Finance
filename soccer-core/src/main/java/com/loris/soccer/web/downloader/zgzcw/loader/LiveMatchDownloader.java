package com.loris.soccer.web.downloader.zgzcw.loader;

import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.bean.wrapper.Result;
import com.loris.base.context.LorisContext;
import com.loris.base.util.ArraysUtil;
import com.loris.soccer.bean.item.MatchItem;
import com.loris.soccer.bean.model.Keys;
import com.loris.soccer.bean.model.MatchList;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwDataDownloader;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwWebPageCreator;
import com.loris.soccer.web.downloader.zgzcw.page.OddsOpWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsYpWebPage;

/**
 * 创建动态数据下载管理器
 * @author jiean
 *
 */
public class LiveMatchDownloader extends IssueMatchDownloader
{
	private static Logger logger = Logger.getLogger(LiveMatchDownloader.class);
	
	/** 竞彩比赛列表 */
	private MatchList matches = new MatchList();
	
	
	public LiveMatchDownloader()
	{
	}
	
	/**
	 * 设置运行时态环境
	 * @param context
	 */
	@Override
	public void setLorisContext(LorisContext context)
	{
		super.setLorisContext(context);
		ZgzcwDataDownloader.initialize(context);
	}
	
	
	/**
	 * 数据下载准备
	 */
	@Override
	public boolean prepare()
	{
		logger.info("Starting to preparing LiveMatchDownloader...");
		try
		{
			Result result = ZgzcwDataDownloader.downloadLiveJcWebPage();
			if(result.get("matches") != null)
			{
				matches.addAll((MatchList)result.get("matches"));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.info("Error when downloading Jc Live page.");
		}
		
		try
		{
			Thread.sleep(3000);
		}
		catch(Exception e)
		{
		}
		
		try
		{
			Result result = ZgzcwDataDownloader.downloadLiveBdWebPage();
			if(result.get("matches") != null)
			{
				matches.addSingleItems((MatchList)result.get("matches"));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.info("Error when downloading Jc Live page.");
		}
		
		if(matches.size() < 0)
		{
			logger.info("There are no matches to be downloaded.");
			return false;
		}
		
		Keys mids = matches.getMids();
		List<OddsOpWebPage> downOpPages = soccerWebPageManager.getDownloadedOddsOpWebPages(mids);
		List<OddsYpWebPage> downYpPages = soccerWebPageManager.getDownloadedOddsYpWebPages(mids);
		opChecker.setTime(System.currentTimeMillis());
		ypChecker.setTime(opChecker.getTime());
		
		for (MatchItem match : matches)
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
		}
	
		totalSize = matches.size() * 4;
		
		return true;
	}
}
