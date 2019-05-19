package com.loris.soccer.web.downloader.zgzcw.loader;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.loris.base.util.DateUtil;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.model.LeagueSeason;
import com.loris.soccer.bean.table.League;
import com.loris.soccer.bean.table.Season;
import com.loris.soccer.repository.SoccerManager;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwSoccerDownloader;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwWebPageCreator;
import com.loris.soccer.web.downloader.zgzcw.page.SeasonWebPage;

/**
 * 下载各赛季比赛数据, 其数据处理过程如下：
 * 第一步：下载赛事中心页面数据，获取足球比赛有哪些联赛
 * 第二步：对联赛（杯赛）页面进行下载，获取有哪些赛季数据
 * 
 * @author jiean
 *
 */
public class SeasonDownloader extends ZgzcwSoccerDownloader
{
	private static Logger logger = Logger.getLogger(SeasonDownloader.class);
	
	/** League List. */
	private List<League> leagues;
	
	/** 时间间隔 */
	private int intervalDay = 5;
	
	/** 开始的赛季*/
	private String startYear = "2016";
	
	
	private boolean refresh = false;
	
	/**
	 * 初始化数据下载管理器
	 */
	@Override
	public boolean prepare()
	{
		if (soccerManager == null || soccerWebPageManager == null)
		{
			logger.info("SoccerManager is not initialized, stop.");
			return false;
		}
		
		leagues = SoccerManager.getLeagues();
		List<SeasonWebPage> list = soccerWebPageManager.getDownloadedSeasonWebPage(startYear, null);
		Map<String, LeagueSeason> leagueSeasons = soccerManager.getSeasonMap(startYear);
		
		for (League league : leagues)
		{
			if(!(league.getType().equalsIgnoreCase(SoccerConstants.MATCH_TYPE_CUP) ||
					league.getType().equalsIgnoreCase(SoccerConstants.MATCH_TYPE_LEAGUE)))
			{
				continue;
			}
			LeagueSeason leagueSeason = leagueSeasons.get(league.getLid());
			
			if(leagueSeason == null)
			{
				SeasonWebPage page = ZgzcwWebPageCreator.createSeasonWebPage(league.getLid(), league.getType(), "");
				pages.put(page);
				continue;
			}
			int size = leagueSeason.size();
			for(int i = 0; i < size; i ++)
			{
				Season season = leagueSeason.getSeason(i);
				
				//如果已经下载，则不需要再行下载数据
				if(!isDownloaded(list, season.getLid(), season.getSeason(), league.getType()))
				{
					SeasonWebPage page = ZgzcwWebPageCreator.createSeasonWebPage(league.getLid(), league.getType(), season.getSeason());
					pages.put(page);
				}
			}
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
		if(!processor.processWebPage((SeasonWebPage)page, flag))
		{
			logger.info("Error when process the WebPage: " + page);
		}
		
		super.afterDownload(page, flag);
	}
	
	/**
	 * 判断数据是否已经下载
	 * @param pages 已经下载的数据列表
	 * @param lid 联赛编号
	 * @param type 赛事类型
	 * @return 是否已经下载的标志
	 * @param season 赛季
	 * @return 是否已经下载的标志
	 */
	protected boolean isDownloaded(List<SeasonWebPage> pages, String lid, String season, String type)
	{
		for (SeasonWebPage seasonWebPage : pages)
		{
			if(!seasonWebPage.isCompleted())
			{
				continue;
			}
			
			if(lid.equals(seasonWebPage.getLid()) && season.equals(seasonWebPage.getSeason())
					&& type.equals(seasonWebPage.getType()))
			{
				String date = seasonWebPage.getLoadtime();
				int day = DateUtil.getDiscrepantDays(DateUtil.tryToParseDate(date), new Date());
				if(day < intervalDay)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 判断数据是否已经下载
	 * 
	 * @param pages 已经下载的数据列表
	 * @param lid 联赛编号
	 * @param type 赛事类型
	 * @return 是否已经下载的标志
	 */
	protected boolean isDownloaded(List<SeasonWebPage> pages, String lid, String type)
	{
		for (SeasonWebPage seasonWebPage : pages)
		{
			if(!seasonWebPage.isCompleted())
			{
				continue;
			}
			
			if(lid.equals(seasonWebPage.getLid()) && type.equals(seasonWebPage.getType()))
			{
				if(refresh)
				{
					String date = seasonWebPage.getLoadtime();
					int day = DateUtil.getDiscrepantDays(DateUtil.tryToParseDate(date), new Date());
					if(day < intervalDay)
					{
						return true;
					}
				}
				else
				{
					return true;
				}
			}
		}
		return false;
	}

	public boolean isRefresh()
	{
		return refresh;
	}

	public void setRefresh(boolean refresh)
	{
		this.refresh = refresh;
	}

	public int getIntervalDay()
	{
		return intervalDay;
	}

	public void setIntervalDay(int intervalDay)
	{
		this.intervalDay = intervalDay;
	}

	public String getStartYear()
	{
		return startYear;
	}

	public void setStartYear(String startYear)
	{
		this.startYear = startYear;
	}
}
