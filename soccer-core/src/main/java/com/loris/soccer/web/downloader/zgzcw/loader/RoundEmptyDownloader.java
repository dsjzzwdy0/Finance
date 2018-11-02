package com.loris.soccer.web.downloader.zgzcw.loader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.web.WebPage;
import com.loris.base.util.DateUtil;
import com.loris.soccer.analysis.util.PerformanceUtil;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.data.table.league.League;
import com.loris.soccer.bean.data.table.league.Match;
import com.loris.soccer.bean.data.table.league.Round;
import com.loris.soccer.repository.SoccerManager;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwSoccerDownloader;
import com.loris.soccer.web.downloader.zgzcw.page.RoundCupWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.RoundLeagueWebPage;
import com.loris.soccer.web.downloader.zgzcw.parser.RoundCupWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.RoundLeagueWebPageParser;

/**
 * 轮次数据下载
 * 
 * @author jiean
 *
 */
public class RoundEmptyDownloader extends ZgzcwSoccerDownloader
{
	private static Logger logger = Logger.getLogger(RoundEmptyDownloader.class);

	/** 联赛数据 */
	private List<League> leagues = null;

	/**
	 * 下载数据准备
	 */
	@Override
	public boolean prepare()
	{
		if (soccerManager == null || soccerWebPageManager == null)
		{
			logger.info("TeamDownloader is not initialized, stop.");
			return false;
		}

		if (StringUtils.isEmpty(start) || StringUtils.isEmpty(end))
		{
			logger.info("RoundDownloader start or end time is not set, please set the date first. stop.");
			return false;
		}

		if (leagues == null)
		{
			leagues = SoccerManager.getLeagues();
		}

		List<Round> rounds = soccerManager.getEmptyRounds();
		League league = null;

		List<Round> needToDownloadRounds = new ArrayList<>();

		// 计算需要下载的轮次数据
		for (Round round : rounds)
		{
			// 查找联赛信息
			league = getLeague(round.getLid());
			// logger.info(round + ": " + league);
			if (league == null)
			{
				continue;
			}
			
			//round.getSeason().contains("2017") || 
			if(round.getSeason().contains("2018"))
			{
				// 杯赛
				if (SoccerConstants.MATCH_TYPE_CUP.equals(league.getType()))
				{
					if (!isCupRoundInDownloadPages(needToDownloadRounds, round))
					{
						needToDownloadRounds.add(round);
					}
				}
				else
				{
					needToDownloadRounds.add(round);
				}
			}
		}

		/** 已经下载的数据 */
		List<RoundLeagueWebPage> downloadedRoundLeaguePagesadedRoundLeaguePages = soccerWebPageManager
				.getDownloadedLeagueRounds();
		List<RoundCupWebPage> downloadedRoundCupPages = soccerWebPageManager.getDownloadedCupPages();
		Date date = new Date();

		// 计算
		WebPage page = null;
		for (Round round : needToDownloadRounds)
		{
			// 查找联赛信息
			league = getLeague(round.getLid());
			if (league == null)
			{
				continue;
			}

			page = null;

			// 杯赛
			if (SoccerConstants.MATCH_TYPE_CUP.equals(league.getType()))
			{
				if (isDownloadedCupRound(downloadedRoundCupPages, round, date))
				{
					continue;
				}
				page = creator.createRoundCupWebpage(round.getLid(), round.getSeason());
			}
			// 联赛
			else if (SoccerConstants.MATCH_TYPE_LEAGUE.equals(league.getType()))
			{
				if (isDownloadedLeagueRound(downloadedRoundLeaguePagesadedRoundLeaguePages, round))
				{
					continue;
				}
				page = creator.createRoundLeagueWebPage(round.getLid(), round.getSeason(), round.getName());
			}

			if (page != null)
			{
				page.setParam(round);
				pages.put(page);
			}
		}

		totalSize = rounds.size();
		logger.info("There are " + totalSize + " pages and there are " + leftSize() + " pages to be downloaded.");
		return true;
	}

	/**
	 * After download the page.
	 * 
	 * @param page
	 *            Page Value.
	 * @param flag
	 *            The flag value.
	 */
	@Override
	public void afterDownload(WebPage page, boolean flag)
	{
		if (!flag)
		{
			// this will do nothing.
			return;
		}

		// 检测数据是否正常
		if (!checkPageContent(page))
		{
			return;
		}
		
		//logger.info("Content: " + page.getContent());

		List<Match> matchs = null;
		Round round = null;

		if (page instanceof RoundCupWebPage)
		{
			RoundCupWebPage page2 = (RoundCupWebPage) page;
			RoundCupWebPageParser parser = new RoundCupWebPageParser();
			if (parser.parseWebPage(page2))
			{
				matchs = parser.getMatches();
			}
			else
			{
				page2.setCompleted(false);
			}

			synchronized (soccerWebPageManager)
			{
				soccerWebPageManager.addOrUpdateRoundCupWebPage(page2);
			}
		}
		else if (page instanceof RoundLeagueWebPage)
		{
			RoundLeagueWebPageParser parser = new RoundLeagueWebPageParser();
			if (parser.parseWebPage(page))
			{
				matchs = parser.getMatches();
			}
			else
			{
				page.setCompleted(false);
			}

			synchronized (soccerWebPageManager)
			{
				soccerWebPageManager.addOrUpdateRoundLeagueWebPage((RoundLeagueWebPage) page);
			}
		}

		if (matchs != null && matchs.size() > 0)
		{
			/*
			 * int i = 1; for (Match match : matchs) { logger.info(i +++ ": " +
			 * match); }
			 */
			
			logger.info("There are " + matchs.size() + " matches to be updated. ");

			try
			{
				round = (Round) page.getParam();
				if (round != null && PerformanceUtil.createRoundTime(round, matchs))
				{
					synchronized (soccerManager)
					{
						soccerManager.addOrUpdateRound(round);
					}
				}
			}
			catch (Exception e)
			{
			}

			synchronized (soccerManager)
			{
				soccerManager.addOrUpdateMatches(matchs);
			}
		}

		super.afterDownload(page, flag);
	}

	/**
	 * 检测是否已经下载
	 * 
	 * @param round
	 *            轮次数据
	 * @return 是否已经下载
	 */
	protected boolean isDownloadedLeagueRound(List<RoundLeagueWebPage> downPages, Round round)
	{
		for (RoundLeagueWebPage page : downPages)
		{
			if (page.getLid().equals(round.getLid()) && page.getSeason().equals(round.getSeason())
					&& page.getRound().equals(round.getName()))
			{
				String d1 = page.getLoadtime();
				String d2 = round.getEndtime();

				// 下载时间大于最后的时间
				int status = DateUtil.compareDateString(d1, d2);
				if (status >= 0)
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 检测是否已经下载
	 * 
	 * @param round
	 *            轮次数据
	 * @return 是否已经下载
	 */
	protected boolean isDownloadedCupRound(List<RoundCupWebPage> downPages, Round round, Date date)
	{
		for (RoundCupWebPage page : downPages)
		{
			if (page.getLid().equals(round.getLid()) && page.getSeason().equals(round.getSeason()))
			{
				Date d1 = DateUtil.tryToParseDate(page.getLoadtime());
				if (d1 != null)
				{
					int num = DateUtil.getDiscrepantDays(d1, date);
					if (num <= 1)
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 检测是否该杯赛的数据已经在下开列表中
	 * 
	 * @param lid
	 *            联赛编号
	 * @return 是否已经包含在下载列表中
	 */
	protected boolean isCupRoundInDownloadPages(List<Round> downRounds, Round round)
	{
		for (Round r : downRounds)
		{
			if (r.getLid().equals(round.getLid()) && r.getSeason().equals(round.getSeason()))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 查找联赛信息
	 * 
	 * @param lid
	 * @return
	 */
	protected League getLeague(String lid)
	{
		for (League league : leagues)
		{
			if (lid.equals(league.getLid()))
			{
				return league;
			}
		}
		return null;
	}
}
