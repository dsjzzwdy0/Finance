package com.loris.soccer.web.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.web.repository.WebPageManager;
import com.loris.soccer.web.downloader.okooo.page.OkoooWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.LeagueWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.LiveWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.LotteryWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.MatchHistoryWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsOpWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsOpZhishuWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsYpWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsYpZhishuWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.RankWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.RoundCupWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.RoundLeagueWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.SeasonWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.TeamWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.ZgzcwCenterPage;
import com.loris.soccer.web.page.SimpleOddsOpWebPage;
import com.loris.soccer.web.page.SimpleOddsYpWebPage;
import com.loris.soccer.web.repository.service.LeagueWebPageService;
import com.loris.soccer.web.repository.service.LiveWebPageService;
import com.loris.soccer.web.repository.service.LotteryWebPageService;
import com.loris.soccer.web.repository.service.MatchHistoryWebPageService;
import com.loris.soccer.web.repository.service.OddsOpWebPageService;
import com.loris.soccer.web.repository.service.OddsOpZhishuWebPageService;
import com.loris.soccer.web.repository.service.OddsYpWebPageService;
import com.loris.soccer.web.repository.service.OddsYpZhishuWebPageService;
import com.loris.soccer.web.repository.service.OkoooWebPageService;
import com.loris.soccer.web.repository.service.RankWebPageService;
import com.loris.soccer.web.repository.service.RoundCupWebPageService;
import com.loris.soccer.web.repository.service.RoundLeagueWebPageService;
import com.loris.soccer.web.repository.service.SeasonWebPageService;
import com.loris.soccer.web.repository.service.SimpleOddsOpWebPageService;
import com.loris.soccer.web.repository.service.SimpleOddsYpWebPageService;
import com.loris.soccer.web.repository.service.TeamWebPageService;
import com.loris.soccer.web.repository.service.ZgzcwCenterPageService;

@Component
public class SoccerWebPageManager extends WebPageManager
{
	@Autowired
	private SeasonWebPageService seasonWebPageService;
	
	@Autowired
	private RoundCupWebPageService roundCupWebPageService;
	
	@Autowired
	private RoundLeagueWebPageService roundLeagueWebPageService;
	
	@Autowired
	private MatchHistoryWebPageService matchHistoryWebPageService;
	
	@Autowired
	private TeamWebPageService teamWebPageService;
	
	@Autowired
	private LotteryWebPageService lotteryWebPageService;
	
	@Autowired
	private OddsOpWebPageService oddsOpWebPageService;
	
	@Autowired
	private SimpleOddsOpWebPageService simpleOddsOpWebPageService;
	
	@Autowired
	private OddsYpWebPageService oddsYpWebPageService;
	
	@Autowired
	private SimpleOddsYpWebPageService simpleOddsYpWebPageService;
	
	@Autowired
	private RankWebPageService rankWebPageService;
	
	@Autowired
	private OddsOpZhishuWebPageService oddsOpZhishuWebPageService;
	
	@Autowired
	private OddsYpZhishuWebPageService oddsYpZhishuWebPageService;
	
	@Autowired
	private OkoooWebPageService okoooWebPageService;
	
	@Autowired
	private ZgzcwCenterPageService zgzcwCenterPageService;
	
	@Autowired
	private LeagueWebPageService leagueWebPageService;
	
	@Autowired
	private LiveWebPageService liveWebPageService;
		
	
	/**
	 * Add the page.
	 * @param page
	 * @return
	 */
	public boolean addOrUpdateSeasonWebPage(SeasonWebPage page)
	{
		super.saveWebPage(page);
		if(StringUtils.isNotEmpty(page.getId()))
		{
			return seasonWebPageService.updateAllColumnById(page);
		}
		else
		{
			return seasonWebPageService.insert(page);
		}
	}
	
	/**
	 * Add the WebPage
	 * 
	 * @param page
	 * @return
	 */
	public boolean addOrUpdateTeamWebPage(TeamWebPage page)
	{
		super.saveWebPage(page);
		if(StringUtils.isNotEmpty(page.getId()))
		{
			return teamWebPageService.updateAllColumnById(page);
		}
		else
		{
			return teamWebPageService.insert(page);
		}
	}
	
	/**
	 * Get the DownloadedTeams.
	 * 
	 * @return
	 */
	public List<String> getDownloadedTeams()
	{
		return teamWebPageService.getDownloadedTeams();
	}
	
	/**
	 * 获取已经下载的数据页面
	 * 
	 * @return 下载的数据页
	 */
	public List<RoundLeagueWebPage> getDownloadedLeagueRounds()
	{
		return roundLeagueWebPageService.getDownloadedRounds();
	}
	
	/**
	 * 获取已经下载的杯赛页面
	 * 
	 * @return
	 */
	public List<RoundCupWebPage> getDownloadedCupPages()
	{
		return roundCupWebPageService.getDownloadedRounds();
	}
	
	/**
	 * Add or Update the RoundCupWebPage Information.
	 * 
	 * @param page
	 * @return
	 */
	public boolean addOrUpdateRoundCupWebPage(RoundCupWebPage page)
	{
		super.saveWebPage(page);
		if(StringUtils.isNotEmpty(page.getId()))
		{
			return roundCupWebPageService.updateAllColumnById(page);
		}
		else
		{
			return roundCupWebPageService.insert(page);
		}
	}
	
	/**
	 * Add or Update the RoundLeagueWebPage.
	 * 
	 * @param page
	 * @return
	 */
	public boolean addOrUpdateRoundLeagueWebPage(RoundLeagueWebPage page)
	{
		super.saveWebPage(page);
		if(StringUtils.isNotEmpty(page.getId()))
		{
			return roundLeagueWebPageService.updateAllColumnById(page);
		}
		else
		{
			return roundLeagueWebPageService.insert(page);
		}
	}
	
	/**
	 * Add or Update the LotteryBdWebPage.
	 * 
	 * @param page
	 * @return
	 */
	public boolean addOrUpdateLotteryWebPage(LotteryWebPage page)
	{
		super.saveWebPage(page);
		if(StringUtils.isNotEmpty(page.getId()))
		{
			return lotteryWebPageService.updateAllColumnById(page);
		}
		else
		{
			return lotteryWebPageService.insert(page);
		}
	}
	
	/**
	 * 检测是否已经在下载列表中
	 * 
	 * @param issue 比赛期号
	 * @param type 类型
	 * @return 是否存在的标志
	 */
	public boolean hasExistLotteryWebPage(String issue, String type)
	{
		EntityWrapper<LotteryWebPage> ew = new EntityWrapper<>();
		ew.eq("issue", issue);
		ew.eq("completed", "1");
		ew.eq("type", type);
		int count = lotteryWebPageService.selectCount(ew);
		return (count > 0);
	}
	
	/**
	 * 查找已经下载的赛季数据页面
	 * 
	 * @return 已经下载的赛季数据页面
	 */
	public List<SeasonWebPage> getDownloadedSeasonWebPage()
	{
		return seasonWebPageService.getDownloadedSeasons();
		/*
		EntityWrapper<SeasonWebPage> ew = new EntityWrapper<>();
		return seasonWebPageService.selectList(ew);*/
	}
	
	/**
	 * 获得下载的季节数据
	 * @param start
	 * @param end
	 * @return
	 */
	public List<SeasonWebPage> getDownloadedSeasonWebPage(String start, String end)
	{
		EntityWrapper<SeasonWebPage> ew = new EntityWrapper<>();
		ew.eq("1", "1");
		if(StringUtils.isNotEmpty(start))
		{
			ew.andNew().gt("season", start).or().eq("season", start);
		}
		if(StringUtils.isNotEmpty(end))
		{
			ew.andNew().eq("season", end).or().lt("season", end);
		}
		return seasonWebPageService.selectList(ew);
	}
	
	/**
	 * 获取已经下载的页面
	 * 
	 * @param type 类型语句
	 * @return 下载的数据列表
	 */
	public List<LotteryWebPage> getDownloadedLotteryWebPages(String type)
	{
		return lotteryWebPageService.getDownloadedPages(type);
	}
	
	/**
	 * 检查是否已经下载了该页的欧赔主页数据
	 * 
	 * @param mid 比赛编号
	 * @return 是否下载的标志
	 */
	public boolean hasDownloadOddsOpWebPage(String mid)
	{
		EntityWrapper<OddsOpWebPage> ew = new EntityWrapper<>();
		ew.eq("mid", mid);
		List<OddsOpWebPage> pages = oddsOpWebPageService.selectList(ew);
		return pages.size() > 0;
	}
	
	/**
	 * 获得下载的数据
	 * @param mids 比赛列表
	 * @return 下载的页面
	 */
	public List<SimpleOddsOpWebPage> getDownloadedOddsWebPages(List<String> mids)
	{
		EntityWrapper<SimpleOddsOpWebPage> ew = new EntityWrapper<>();
		if(mids != null && mids.size() > 0)
		{
			ew.in("mid", mids);
		}
		return simpleOddsOpWebPageService.selectList(ew);
	}
	
	/**
	 * 获得下载的数据
	 * @param mids 比赛列表
	 * @return 下载的页面
	 */
	public List<SimpleOddsOpWebPage> getDownloadedOddsWebPages()
	{
		EntityWrapper<SimpleOddsOpWebPage> ew = new EntityWrapper<>();
		return simpleOddsOpWebPageService.selectList(ew);
	}
	
	/**
	 * 检查是否已经下载了该页的亚盘主页数据
	 * 
	 * @param mid 比赛编号
	 * @return 是否下载的标志
	 */
	public boolean hasDownloadOddsYpWebPage(String mid)
	{
		EntityWrapper<OddsYpWebPage> ew = new EntityWrapper<>();
		ew.eq("mid", mid);
		List<OddsYpWebPage> pages = oddsYpWebPageService.selectList(ew);
		return pages.size() > 0;
	}
	
	/**
	 * 获得数据页面
	 * @param type 类型
	 * @param issue 期号
	 * @return
	 */
	public LotteryWebPage getLotteryPage(String type, String issue)
	{
		EntityWrapper<LotteryWebPage> ew = new EntityWrapper<>();
		ew.eq("type", type);
		ew.eq("issue", issue);
		return lotteryWebPageService.selectOne(ew);
	}
	
	/**
	 * Add or Update the OddsOpWebPage.
	 * 
	 * @param page
	 * @return
	 */
	public boolean addOrUpdateOpWebPage(OddsOpWebPage page)
	{
		super.saveWebPage(page);
		if(StringUtils.isNotEmpty(page.getId()))
		{
			return oddsOpWebPageService.updateAllColumnById(page);
		}
		else
		{
			return oddsOpWebPageService.insert(page);
		}
	}
	
	/**
	 * Add or Update the OddsOpWebPage.
	 * 
	 * @param page
	 * @return
	 */
	public boolean addOrUpdateYpWebPage(OddsYpWebPage page)
	{
		super.saveWebPage(page);
		if(StringUtils.isNotEmpty(page.getId()))
		{
			return oddsYpWebPageService.updateAllColumnById(page);
		}
		else
		{
			return oddsYpWebPageService.insert(page);
		}
	}
	
	/**
	 * Add or Update the RankWebPage
	 * 
	 * @param page Page value.
	 * @return The flag if add or update successful
	 */
	public boolean addOrUpdateRankWebPage(RankWebPage page)
	{
		super.saveWebPage(page);
		if(StringUtils.isNotEmpty(page.getId()))
		{
			return rankWebPageService.updateAllColumnById(page);
		}
		else
		{
			return rankWebPageService.insert(page);
		}
	}
	
	/**
	 * 获得当前已经下载的排名页面
	 * @return 排名页面
	 */
	public List<RankWebPage> getDownloadedRankPages()
	{
		return rankWebPageService.getDownloadedRanks();
	}
	
	/**
	 * 更新或添加欧赔指数据数据页面
	 * 
	 * @param page 数据页面
	 * @return 成功与否的标志
	 */
	public boolean addOrUpdateOddsOpZhishuPage(OddsOpZhishuWebPage page)
	{
		super.saveWebPage(page);
		if(StringUtils.isNotEmpty(page.getId()))
		{
			return oddsOpZhishuWebPageService.updateAllColumnById(page);
		}
		else
		{
			return oddsOpZhishuWebPageService.insert(page);
		}
	}
	
	/**
	 * 更新或添加亚盘指数据数据页面
	 * 
	 * @param page 数据页面
	 * @return 成功与否的标志
	 */
	public boolean addOrUpdateOddsYpZhishuPage(OddsYpZhishuWebPage page)
	{
		super.saveWebPage(page);
		if(StringUtils.isNotEmpty(page.getId()))
		{
			return oddsYpZhishuWebPageService.updateAllColumnById(page);
		}
		else
		{
			return oddsYpZhishuWebPageService.insert(page);
		}
	}
	
	/**
	 * 获得已经下载的欧赔指数据数据
	 * 
	 * @return 数据列表
	 */
	public List<OddsOpZhishuWebPage> getDownloadedOddsOpZhishuPages()
	{
		return oddsOpZhishuWebPageService.getDownloadedPages();
	}
	
	/**
	 * 获得已经下载的欧赔指数据数据
	 * 
	 * @return 数据列表
	 */
	public List<OddsOpZhishuWebPage> getDownloadedOddsOpZhishuPages(List<String> mids)
	{
		EntityWrapper<OddsOpZhishuWebPage> ew = new EntityWrapper<>();
		ew.in("mid", mids);
		return oddsOpZhishuWebPageService.selectList(ew);
	}
	
	/**
	 * 获得已经下载的亚盘指数据数据
	 * 
	 * @return 数据列表
	 */
	public List<OddsYpZhishuWebPage> getDownloadedOddsYpZhishuPages()
	{
		return oddsYpZhishuWebPageService.getDownloadedPages();
	}
	
	/**
	 * 获得已经下载的欧赔指数据数据
	 * 
	 * @return 数据列表
	 */
	public List<OddsYpZhishuWebPage> getDownloadedOddsYpZhishuPages(List<String> mids)
	{
		EntityWrapper<OddsYpZhishuWebPage> ew = new EntityWrapper<>();
		ew.in("mid", mids);
		return oddsYpZhishuWebPageService.selectList(ew);
	}
	
	/**
	 * 查找已经下载的OP页面数据
	 * 
	 * @return 下载的数据列表
	 */
	public List<OddsOpWebPage> getDownloadedOddsOpWebPages()
	{
		return oddsOpWebPageService.getDownloadedPages();
	}
	
	/**
	 * 下载的欧赔数据
	 * @param mids
	 * @return
	 */
	public List<OddsOpWebPage> getDownloadedOddsOpWebPages(List<String> mids)
	{
		EntityWrapper<OddsOpWebPage> ew = new EntityWrapper<>();
		ew.in("mid", mids);
		return oddsOpWebPageService.selectList(ew);
	}
	
	/**
	 * 查找已经下载的YP页面数据
	 * 
	 * @return 下载的数据列表
	 */
	public List<OddsYpWebPage> getDownloadedOddsYpWebPages()
	{
		return oddsYpWebPageService.getDownloadedPages();
	}
		
	/**
	 * 下载已经下载的亚盘数据列表
	 * @param mids
	 * @return
	 */
	public List<OddsYpWebPage> getDownloadedOddsYpWebPages(List<String> mids)
	{
		EntityWrapper<OddsYpWebPage> ew = new EntityWrapper<>();
		ew.in("mid", mids);
		return oddsYpWebPageService.selectList(ew);
	}

	/**
	 * 下载已经下载的亚盘数据列表
	 * @param mids
	 * @return
	 */
	public List<SimpleOddsYpWebPage> getDownloadedSimpleOddsYpWebPages()
	{
		EntityWrapper<SimpleOddsYpWebPage> ew = new EntityWrapper<>();
		return simpleOddsYpWebPageService.selectList(ew);
	}

	/**
	 * 获得已经下载的比赛历史页列表
	 * 
	 * @return 下载的数据列表
	 */
	public List<MatchHistoryWebPage> getDownloadedMatchHistoryPages()
	{
		return matchHistoryWebPageService.getDownloadedPages();
	}
	
	
	
	
	/**
	 * 获得已经下载的比赛历史页列表
	 * 
	 * @return 下载的数据列表
	 */
	public List<MatchHistoryWebPage> getDownloadedMatchHistoryPages(List<String> mids)
	{
		EntityWrapper<MatchHistoryWebPage> ew = new EntityWrapper<>();
		ew.in("mid", mids);
		return matchHistoryWebPageService.selectList(ew);
	}
	
	/**
	 * 加入或更新下载的历史数据页面
	 * 
	 * @param page 历史数据页面
	 * @return 是否成功的标志
	 */
	public boolean addOrUpdateMatchHistoryPage(MatchHistoryWebPage page)
	{
		super.saveWebPage(page);
		if(StringUtils.isNotEmpty(page.getId()))
		{
			return matchHistoryWebPageService.updateAllColumnById(page);
		}
		else
		{
			return matchHistoryWebPageService.insert(page);
		}
	}
	
	/**
	 * 加入或更新下载的历史数据页面
	 * 
	 * @param page 历史数据页面
	 * @return 是否成功的标志
	 */
	public boolean addOrUpdateLiveWebPage(LiveWebPage page)
	{
		super.saveWebPage(page);
		if(StringUtils.isNotEmpty(page.getId()))
		{
			return liveWebPageService.updateAllColumnById(page);
		}
		else
		{
			return liveWebPageService.insert(page);
		}
	}
	
	/**
	 * 加入或更新下载的Okooo数据页面
	 * @param page 澳客网页数据
	 * @return 数据增加或更新成功的标志
	 */
	public boolean addOrUpdateOkoooWebPage(OkoooWebPage page)
	{
		super.saveWebPage(page);
		if(StringUtils.isEmpty(page.getId()))
		{
			return okoooWebPageService.insert(page);
		}
		else
		{
			return okoooWebPageService.updateAllColumnById(page);
		}
	}
	
	/**
	 * 加入或更新中国足彩网数据页面
	 * @param page 数据页面
	 * @return 数据添加或更新成功的标志
	 */
	public boolean addOrUpateZgzcwCenterPage(ZgzcwCenterPage page)
	{
		super.saveWebPage(page);
		if(StringUtils.isEmpty(page.getId()))
		{
			return zgzcwCenterPageService.insert(page);
		}
		else
		{
			return zgzcwCenterPageService.updateAllColumnById(page);
		}
	}
	
	/**
	 * 获得已经下载的中心数据页面
	 * @return 数据页面
	 */
	public List<ZgzcwCenterPage> getZgzcwCenterPages()
	{
		EntityWrapper<ZgzcwCenterPage> ew = new EntityWrapper<>();
		ew.orderBy("loadtime", false);
		return zgzcwCenterPageService.selectList(ew);
	}
	
	/**
	 * 加入或更新中国足彩网数据页面
	 * @param page 数据页面
	 * @return 数据添加或更新成功的标志
	 */
	public boolean addOrUpdateLeagueWebPage(LeagueWebPage page)
	{
		super.saveWebPage(page);
		if(StringUtils.isEmpty(page.getId()))
		{
			return leagueWebPageService.insert(page);
		}
		else
		{
			return leagueWebPageService.updateAllColumnById(page);
		}
	}
	
	/**
	 * 获得已经下载的联赛主页面
	 * @return 已经下载的赛事主页面
	 */
	public List<LeagueWebPage> getLeaguePages()
	{
		EntityWrapper<LeagueWebPage> ew = new EntityWrapper<>();
		return leagueWebPageService.selectList(ew);
	}
	
	/**
	 * 获得已经下载的亚盘数据
	 * @param mids 比赛编号
	 * @return 下载的页
	 */
	public List<OkoooWebPage> getOkoooYpMainPages(List<String> mids)
	{
		return getOkoooYpPages(mids, "yp");
	}
	/**
	 * 查找已经下载的亚盘主页面数据
	 * @param mids 比赛编号
	 * @return 页面列表
	 */
	public List<OkoooWebPage> getOkoooYpPages(List<String> mids, String type)
	{
		EntityWrapper<OkoooWebPage> ew = new EntityWrapper<>();
		ew.in("mid", mids);
		if(StringUtils.isNotEmpty(type))
		{
			ew.and().eq("type", "yp");
		}
		return okoooWebPageService.selectList(ew);
	}
}
