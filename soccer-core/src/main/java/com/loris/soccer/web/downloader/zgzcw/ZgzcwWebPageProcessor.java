package com.loris.soccer.web.downloader.zgzcw;

import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.web.page.WebPage;
import com.loris.soccer.bean.table.BdMatch;
import com.loris.soccer.bean.table.JcMatch;
import com.loris.soccer.bean.table.LotteryCalendar;
import com.loris.soccer.bean.table.Match;
import com.loris.soccer.bean.table.Op;
import com.loris.soccer.bean.table.Rank;
import com.loris.soccer.bean.table.Round;
import com.loris.soccer.bean.table.Season;
import com.loris.soccer.bean.table.Team;
import com.loris.soccer.bean.table.Yp;
import com.loris.soccer.bean.table.ZcMatch;
import com.loris.soccer.repository.SoccerManager;
import com.loris.soccer.web.downloader.SoccerWebPageProcessor;
import com.loris.soccer.web.downloader.zgzcw.page.LotteryCalendarWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.LotteryWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsOpWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsOpZhishuWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsYpWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsYpZhishuWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.RankWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.RoundCupWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.RoundLeagueWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.SeasonWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.TeamWebPage;
import com.loris.soccer.web.downloader.zgzcw.parser.LotteryBdWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.LotteryCalendarWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.LotteryJcWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.LotteryZcWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.OddsOpWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.OddsYpWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.RankWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.RoundCupWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.RoundLeagueWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.SeasonWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.TeamWebPageParser;
import com.loris.soccer.web.repository.SoccerWebPageManager;

/**
 * 页面处理器，用于对页面的数据进行解析、清洗、存储等。
 * 
 * @author jiean
 *
 */
public class ZgzcwWebPageProcessor implements SoccerWebPageProcessor
{
	private static Logger logger = Logger.getLogger(ZgzcwWebPageProcessor.class);
	
	private static final String bd = "bd";
	private static final String jc = "jc";
	private static final String zc = "zc";

	/** SoccerManager. */
	private SoccerManager soccerManager;

	/** SoccerWebPageManager. */
	private SoccerWebPageManager soccerWebPageManager;

	/**
	 * Create a new instance of ZgzcwWebPageProcessor.
	 */
	public ZgzcwWebPageProcessor()
	{
	}

	/**
	 * Create a new instance of ZgzcwWebPageProcessor
	 * 
	 * @param soccerManager
	 *            SoccerManager.
	 * @param webPageManager
	 *            SoccerWebPageManager
	 */
	public ZgzcwWebPageProcessor(SoccerManager soccerManager, SoccerWebPageManager webPageManager)
	{
		this.soccerManager = soccerManager;
		this.soccerWebPageManager = webPageManager;
	}

	/**
	 * Check the web page content is validate.
	 * 
	 * @param page
	 * @return
	 */
	public boolean checkPageContent(WebPage page)
	{
		if (page.getContent().contains("<title>403 Forbidden</title>"))
		{
			return false;
		}
		return true;
	}

	/**
	 * 设置SoccerManager.
	 * 
	 * @param soccerManager
	 *            基本数据管理器
	 */
	public void setSoccerManager(SoccerManager soccerManager)
	{
		this.soccerManager = soccerManager;
	}

	/**
	 * 设置SoccerWebPageManager
	 * 
	 * @param soccerWebPageManager
	 *            页面数据管理器
	 */
	public void setSoccerWebPageManager(SoccerWebPageManager soccerWebPageManager)
	{
		this.soccerWebPageManager = soccerWebPageManager;
	}

	/**
	 * 页面处理器，WebPage页面下载之后的后续处理过程
	 * 
	 * @param page
	 *            等处理的页面
	 * @param flag
	 *            页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	@Override
	public boolean processWebPage(WebPage page, boolean flag)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 页面处理器，LotteryCalendarWebPage页面下载之后的后续处理过程
	 * 
	 * @param page
	 *            等处理的页面
	 * @param flag
	 *            页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	@Override
	public boolean processWebPage(LotteryCalendarWebPage page, boolean flag)
	{
		if (!flag)
		{
			// this will do nothing.
			return false;
		}

		// 检测数据是否正常
		if (!checkPageContent(page))
		{
			return false;
		}

		LotteryCalendarWebPage page2 = (LotteryCalendarWebPage) page;
		LotteryCalendarWebPageParser parser = new LotteryCalendarWebPageParser();
		boolean success = false;

		if (parser.parseWebPage(page2))
		{
			List<LotteryCalendar> calendars = parser.getCalendars();
			synchronized (soccerManager)
			{
				success = soccerManager.addOrUpdateLotteryCalendars(calendars);
			}
		}

		return success;
	}

	/**
	 * 页面处理器，LotteryWebPage页面下载之后的后续处理过程
	 * 
	 * @param page
	 *            等处理的页面
	 * @param flag
	 *            页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	@Override
	public boolean processWebPage(LotteryWebPage page, boolean flag)
	{
		if (!flag)
		{
			// this will do nothing.
			return false;
		}

		// 检测数据是否正常
		if (!checkPageContent(page))
		{
			return false;
		}
		LotteryWebPage page2 = (LotteryWebPage) page;
		String issueType = page2.getIssuetype();
		boolean success = false;

		synchronized (soccerManager)
		{
			switch (issueType) {
			case bd:
				success = processBdWebPage(page2);
				break;
			case jc:
				success = processJcWebPage(page2);
				break;
			case zc:
				success = processZcWebPage(page2);
				break;
			default:
				break;
			}
		}

		if (!success)
		{
			page2.setCompleted(false);
		}

		synchronized (soccerWebPageManager)
		{
			soccerWebPageManager.addOrUpdateLotteryWebPage(page2);
		}
		
		return success;
	}

	/**
	 * 页面处理器，OddsOpWebPage页面下载之后的后续处理过程
	 * 
	 * @param page
	 *            等处理的页面
	 * @param flag
	 *            页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	@Override
	public boolean processWebPage(OddsOpWebPage page, boolean flag)
	{
		if (!flag)
		{
			// this will do nothing.
			return false;
		}

		// 检测数据是否正常
		if (!checkPageContent(page))
		{
			return false;
		}
		
		OddsOpWebPage page2 = (OddsOpWebPage)page;
		OddsOpWebPageParser parser = new OddsOpWebPageParser();
		boolean success = false;
		
		if(parser.parseWebPage(page2))
		{
			List<Op> opList = parser.getOddsList();
			if(opList.isEmpty())
			{
				logger.info("Error, no Op Data can be parsed: " + page2);
			}
			else
			{
				synchronized (soccerManager)
				{
					success = soccerManager.addOpList(opList);
				}
			}
		}
		else
		{
			page2.setCompleted(false);
		}
		
		synchronized (soccerWebPageManager)
		{
			success = soccerWebPageManager.addOrUpdateOpWebPage(page2);
		}
		return success;
	}
	
	/**
	 * 页面处理器，OddsYpWebPage页面下载之后的后续处理过程
	 * 
	 * @param page
	 *            等处理的页面
	 * @param flag
	 *            页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	@Override
	public boolean processWebPage(OddsYpWebPage page, boolean flag)
	{
		if (!flag)
		{
			// this will do nothing.
			return false;
		}

		// 检测数据是否正常
		if (!checkPageContent(page))
		{
			return false;
		}
		OddsYpWebPageParser parser = new OddsYpWebPageParser();
		boolean success = false;
		if(parser.parseWebPage(page))
		{
			List<Yp> ypList = parser.getOddsList();
			synchronized (soccerManager)
			{
				success = soccerManager.addYpList(ypList);
			}
		}
		else
		{
			page.setCompleted(false);
		}
		
		synchronized (soccerWebPageManager)
		{
			success = soccerWebPageManager.addOrUpdateYpWebPage(page);
		}
		return success;
	}

	/**
	 * 页面处理器，RankWebPage页面下载之后的后续处理过程
	 * 
	 * @param page
	 *            等处理的页面
	 * @param flag
	 *            页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	@Override
	public boolean processWebPage(RankWebPage page, boolean flag)
	{
		if (!flag)
		{
			// this will do nothing.
			return false;
		}

		// 检测数据是否正常
		if (!checkPageContent(page))
		{
			return false;
		}
		
		boolean success = false;
		RankWebPageParser parser = new RankWebPageParser();
		if(parser.parseWebPage(page))
		{
			List<Rank> ranks = parser.getRanks();
			if(ranks != null && ranks.size() > 0)
			{
				synchronized (soccerManager)
				{
					success = soccerManager.addRanks(ranks);
				}
			}
		}
		else
		{
			page.setCompleted(false);
		}
		
		//加入页面数据
		synchronized (soccerWebPageManager)
		{
			success = soccerWebPageManager.addOrUpdateRankWebPage(page);
		}
		
		return success;
	}

	/**
	 * 页面处理器，RoundCupWebPage页面下载之后的后续处理过程
	 * 
	 * @param page
	 *            等处理的页面
	 * @param flag
	 *            页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	@Override
	public boolean processWebPage(RoundCupWebPage page, boolean flag)
	{
		if (!flag)
		{
			// this will do nothing.
			return false;
		}

		// 检测数据是否正常
		if (!checkPageContent(page))
		{
			return false;
		}
		List<Match> matchs = null;
		boolean success = false;
		RoundCupWebPageParser parser = new RoundCupWebPageParser();
		if(parser.parseWebPage(page))
		{
			matchs = parser.getMatches();
			
			if(matchs != null && matchs.size() > 0)
			{
				synchronized (soccerManager)
				{
					success = soccerManager.addOrUpdateMatches(matchs);
				}
			}	
		}
		else
		{
			page.setCompleted(false);
		}
		
		synchronized (soccerWebPageManager)
		{
			success = soccerWebPageManager.addOrUpdateRoundCupWebPage(page);
		}
		return success;
	}

	/**
	 * 页面处理器，RoundLeagueWebPage页面下载之后的后续处理过程
	 * 
	 * @param page
	 *            等处理的页面
	 * @param flag
	 *            页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	@Override
	public boolean processWebPage(RoundLeagueWebPage page, boolean flag)
	{
		if (!flag)
		{
			// this will do nothing.
			return false;
		}

		// 检测数据是否正常
		if (!checkPageContent(page))
		{
			return false;
		}
		List<Match> matchs = null;
		boolean success = false;
		RoundLeagueWebPageParser parser = new RoundLeagueWebPageParser();
		if(parser.parseWebPage(page))
		{
			matchs = parser.getMatches();
			if(matchs != null && matchs.size() > 0)
			{
				synchronized (soccerManager)
				{
					success = soccerManager.addOrUpdateMatches(matchs);
				}
			}
		}
		else
		{
			page.setCompleted(false);
		}
		
		synchronized (soccerWebPageManager)
		{
			success = soccerWebPageManager.addOrUpdateRoundLeagueWebPage((RoundLeagueWebPage)page);
		}
		return success;
	}

	/**
	 * 页面处理器，SeasonWebPage页面下载之后的后续处理过程
	 * 
	 * @param page
	 *            等处理的页面
	 * @param flag
	 *            页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	@Override
	public boolean processWebPage(SeasonWebPage page, boolean flag)
	{
		if (!flag)
		{
			// this will do nothing.
			return false;
		}

		// 检测数据是否正常
		if (!checkPageContent(page))
		{
			return false;
		}
		
		SeasonWebPageParser parser = new SeasonWebPageParser();
		boolean success = false;
		if(parser.parseWebPage(page))
		{
			List<Season> seasons = parser.getSeasons();
			List<Round> rounds = parser.getRounds();
			
			String lid = parser.getLid();
			String season = parser.getSeasonInfo();
			
			synchronized (soccerManager)
			{
				soccerManager.addNewSeasons(lid, seasons);
				soccerManager.addNewRounds(lid, season, rounds);
			}
		}
		else
		{
			page.setCompleted(false);
		}
		
		synchronized (soccerWebPageManager)
		{
			page.setContent("");
			success = soccerWebPageManager.addOrUpdateSeasonWebPage(page);
		}
		
		return success;
	}

	/**
	 * 页面处理器，TeamWebPage页面下载之后的后续处理过程
	 * 
	 * @param team
	 * 			     球队数据
	 * @param page
	 *            等处理的页面
	 * @param flag
	 *            页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	@Override
	public boolean processWebPage(Team team, TeamWebPage page, boolean flag)
	{
		if (!flag)
		{
			// this will do nothing.
			return false;
		}

		// 检测数据是否正常
		if (!checkPageContent(page))
		{
			return false;
		}
		TeamWebPageParser parser = new TeamWebPageParser();
		boolean success = false;
		parser.setTeam(team);

		//数据解析成功时，添加到数据库中，如果数据解析不成功，则设置数据下载未完成标志
		if (parser.parseWebPage(page))
		{
			team = parser.getTeam();

			// 加入数据库中
			synchronized (soccerManager)
			{
				success = soccerManager.addOrUpdateTeam(team);
			}
		}
		else
		{
			page.setCompleted(false);
		}

		// 加入数据库中
		synchronized (soccerManager)
		{
			success = soccerWebPageManager.addOrUpdateTeamWebPage(page);
		}

		return success;
	}
	
	/**
	 * 页面处理器，OddsOpZhishuWebPage页面下载之后的后续处理过程
	 * 
	 * @param page
	 *            等处理的页面
	 * @param flag
	 *            页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	@Override
	public boolean processWebPage(OddsOpZhishuWebPage page, boolean flag)
	{
		if (!flag)
		{
			// this will do nothing.
			return false;
		}

		// 检测数据是否正常
		if (!checkPageContent(page))
		{
			return false;
		}
		return false;
	}

	/**
	 * 页面处理器，OddsYpZhishuWebPage页面下载之后的后续处理过程
	 * 
	 * @param page
	 *            等处理的页面
	 * @param flag
	 *            页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	@Override
	public boolean processWebPage(OddsYpZhishuWebPage page, boolean flag)
	{
		if (!flag)
		{
			// this will do nothing.
			return false;
		}

		// 检测数据是否正常
		if (!checkPageContent(page))
		{
			return false;
		}
		return false;
	}

	
	/**
	 * 处理BD页面数据
	 * 
	 * @param page
	 * @return
	 */
	protected boolean processBdWebPage(LotteryWebPage page)
	{
		LotteryBdWebPageParser parser = new LotteryBdWebPageParser();
		if(parser.parseWebPage(page))
		{
			List<BdMatch> matchs = parser.getMatches();
			return soccerManager.addOrUpdateBdMatches(matchs);
		}
		return false;
	}
	
	/**
	 * 处理Jc页面数据
	 * 
	 * @param page
	 * @return
	 */
	protected boolean processJcWebPage(LotteryWebPage page)
	{
		LotteryJcWebPageParser parser = new LotteryJcWebPageParser();
		if(parser.parseWebPage(page))
		{
			List<JcMatch> matchs = parser.getJcMatches();
			return soccerManager.addOrUpdateJcMatches(matchs);
		}
		return false;
	}
	
	/**
	 * 处理Zc页面数据
	 * 
	 * @param page
	 * @return
	 */
	protected boolean processZcWebPage(LotteryWebPage page)
	{
		LotteryZcWebPageParser parser = new LotteryZcWebPageParser();
		if(parser.parseWebPage(page))
		{
			List<ZcMatch> matchs = parser.getMatches();
			return soccerManager.addOrUpdateZcMatches(matchs);
		}
		return false;
	}

	
}
