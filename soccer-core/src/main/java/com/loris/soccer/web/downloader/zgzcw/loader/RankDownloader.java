package com.loris.soccer.web.downloader.zgzcw.loader;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.util.ArraysUtil;
import com.loris.base.util.DateUtil;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.bean.model.LeagueMap;
import com.loris.soccer.bean.table.League;
import com.loris.soccer.repository.SoccerManager;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwSoccerDownloader;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwWebPageCreator;
import com.loris.soccer.web.downloader.zgzcw.page.RankWebPage;

public class RankDownloader extends ZgzcwSoccerDownloader
{
	private static Logger logger = Logger.getLogger(RankDownloader.class);
	
	/** League List. */
	private LeagueMap leagueMap;
	
	/** 时间间隔 */
	private int intervalDay = 5;
	
	/** 数据检测器*/
	protected Checker checker = new Checker();
	
	/** 数据检测类 */
	class Checker implements ArraysUtil.EqualChecker<RankWebPage>
	{		
		public String lid;
		public Date date;
		
		public Checker()
		{
			date = new Date();
		}
		
		public void setLid(String lid)
		{
			this.lid = lid;
		}
		
		@Override
		public boolean isSameObject(RankWebPage page)
		{
			if(!page.getLid().equalsIgnoreCase(lid))
			{
				return false;
			}
			
			Date d = DateUtil.tryToParseDate(page.getLoadtime());
			int interval = DateUtil.getDiscrepantDays(d, date);
			if(interval <= intervalDay)
			{
				return true;
			}			
			return false;
		}		
	}
	
	/**
	 * 数据准备
	 */
	@Override
	public boolean prepare()
	{
		if (soccerManager == null || soccerWebPageManager == null)
		{
			logger.info("SoccerManager is not initialized, stop.");
			return false;
		}
		
		leagueMap = SoccerManager.getLeagueMap();
		
		List<League> leagues = leagueMap.getLeagueLeagues();
		List<RankWebPage> downloadedPages = soccerWebPageManager.getDownloadedRankPages();
		
		RankWebPage page = null;
		
		for (League league : leagues)
		{
			checker.setLid(league.getLid());
			if(ArraysUtil.hasSameObject(downloadedPages, checker))
			{
				continue;
			}
			
			page = ZgzcwWebPageCreator.createRankWebPage(league.getLid());
			pages.put(page);
		}
		return true;
	}
	
	/**
	 * 数据下载之后 的处理
	 * 
	 * @param page 下载的页面
	 * @param flag 成载成功的标志
	 */
	@Override
	public void afterDownload(WebPage page, boolean flag)
	{
		if(!processor.processWebPage((RankWebPage)page, flag))
		{
			logger.info("Error when process the WebPage: " + page);
		}
		
		super.afterDownload(page, flag);
	}
}
