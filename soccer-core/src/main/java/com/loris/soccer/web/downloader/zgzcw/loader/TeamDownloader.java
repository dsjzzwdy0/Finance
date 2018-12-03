package com.loris.soccer.web.downloader.zgzcw.loader;

import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.web.page.WebPage;
import com.loris.soccer.bean.data.table.Team;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwSoccerDownloader;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwWebPageCreator;
import com.loris.soccer.web.downloader.zgzcw.page.TeamWebPage;
import com.loris.soccer.web.downloader.zgzcw.parser.TeamWebPageParser;

public class TeamDownloader extends ZgzcwSoccerDownloader
{
	private static Logger logger = Logger.getLogger(TeamDownloader.class);

	/** The Teams. */
	protected List<Team> teams;

	/** 已经下载的数据 */
	protected List<String> downTids;

	/**
	 * Create a new instance of TeamDownloader.
	 */
	public TeamDownloader()
	{
	}

	/**
	 * 初始化
	 */
	@Override
	public boolean prepare()
	{
		if (soccerManager == null || soccerWebPageManager == null)
		{
			logger.info("TeamDownloader is not initialized, stop.");
			return false;
		}
		logger.info("Prepare the TeamDownloader...");
		teams = soccerManager.getAllTeams();
		downTids = soccerWebPageManager.getDownloadedTeams();
		totalSize = teams.size();

		for (Team team : teams)
		{
			if (!isDownloaded(team))
			{
				pages.put(ZgzcwWebPageCreator.createTeamWebPage(team.getTid()));
			}
		}
		return true;
	}

	/**
	 * 检查是否已经下载
	 * 
	 * @param team
	 * @return
	 */
	protected boolean isDownloaded(Team team)
	{
		int downSize = downTids.size();
		for (int i = 0; i < downSize; i++)
		{
			String tid = downTids.get(i);
			if (tid.equals(team.getTid()))
			{
				downTids.remove(i);
				return true;
			}
		}
		return false;
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

		TeamWebPage page2 = (TeamWebPage) page;
		TeamWebPageParser parser = new TeamWebPageParser();

		Team team = getTeam(page2);
		parser.setTeam(team);

		//数据解析成功时，添加到数据库中，如果数据解析不成功，则设置数据下载未完成标志
		if (parser.parseWebPage(page2))
		{
			team = parser.getTeam();

			// 加入数据库中
			synchronized (soccerManager)
			{
				soccerManager.addOrUpdateTeam(team);
			}
		}
		else
		{
			page2.setCompleted(false);
		}

		// 加入数据库中
		synchronized (soccerManager)
		{
			soccerWebPageManager.addOrUpdateTeamWebPage(page2);
		}

		super.afterDownload(page2, flag);
	}

	/**
	 * Get the Team.
	 * 
	 * @param page
	 * @return
	 */
	protected Team getTeam(TeamWebPage page)
	{
		for (Team team : teams)
		{
			if (team.getTid().equals(page.getTid()))
			{
				return team;
			}
		}
		return new Team();
	}
}
