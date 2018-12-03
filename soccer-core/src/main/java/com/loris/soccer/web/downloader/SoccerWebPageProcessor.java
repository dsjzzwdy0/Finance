package com.loris.soccer.web.downloader;

import com.loris.base.web.page.WebPage;
import com.loris.soccer.bean.data.table.Team;
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

public interface SoccerWebPageProcessor
{
	/**
	 * 页面处理器，页面下载之后的后续处理过程
	 * 
	 * @param page 等处理的页面
	 * @param flag 页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	boolean processWebPage(WebPage page, boolean flag);
	
	/**
	 * 页面处理器，LotteryCalendar页面下载之后的后续处理过程
	 * 
	 * @param page 等处理的页面
	 * @param flag 页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	boolean processWebPage(LotteryCalendarWebPage page, boolean flag);
	
	/**
	 * 页面处理器，Lottery页面下载之后的后续处理过程
	 * 
	 * @param page 等处理的页面
	 * @param flag 页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	boolean processWebPage(LotteryWebPage page, boolean flag);
	
	/**
	 * 页面处理器，OddsOpWebPage页面下载之后的后续处理过程
	 * 
	 * @param page 等处理的页面
	 * @param flag 页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	boolean processWebPage(OddsOpWebPage page, boolean flag);
	
	/**
	 * 页面处理器，OddsYpWebPage页面下载之后的后续处理过程
	 * 
	 * @param page 等处理的页面
	 * @param flag 页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	boolean processWebPage(OddsYpWebPage page, boolean flag);
	
	/**
	 * 页面处理器，RankWebPage页面下载之后的后续处理过程
	 * 
	 * @param page 等处理的页面
	 * @param flag 页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	boolean processWebPage(RankWebPage page, boolean flag);
	
	/**
	 * 页面处理器，RoundCupWebPage页面下载之后的后续处理过程
	 * 
	 * @param page 等处理的页面
	 * @param flag 页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	boolean processWebPage(RoundCupWebPage page, boolean flag);
	
	/**
	 * 页面处理器，RoundLeagueWebPage页面下载之后的后续处理过程
	 * 
	 * @param page 等处理的页面
	 * @param flag 页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	boolean processWebPage(RoundLeagueWebPage page, boolean flag);
	
	/**
	 * 页面处理器，SeasonWebPage页面下载之后的后续处理过程
	 * 
	 * @param page 等处理的页面
	 * @param flag 页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	boolean processWebPage(SeasonWebPage page, boolean flag);
	
	/**
	 * 页面处理器，TeamWebPage页面下载之后的后续处理过程
	 * 
	 * @param page 等处理的页面
	 * @param flag 页面下载是否成功能的标志
	 * @return 是否处理成功能标志
	 */
	boolean processWebPage(Team team, TeamWebPage page, boolean flag);
	
	/**
	 * 页面处理器，OddsOpZhishuWebPage页面下载 之后的后续处理过程
	 * 
	 * @param page 待处理的页面
	 * @param flag 页面下载是否成功的标志
	 * @return 是否处理成功的标志
	 */
	boolean processWebPage(OddsOpZhishuWebPage page, boolean flag);
	
	/**
	 * 页面处理器，OddsYpZhishuWebPage页面下载 之后的后续处理过程
	 * 
	 * @param page 待处理的页面
	 * @param flag 页面下载是否成功的标志
	 * @return 是否处理成功的标志
	 */
	boolean processWebPage(OddsYpZhishuWebPage page, boolean flag);
}
