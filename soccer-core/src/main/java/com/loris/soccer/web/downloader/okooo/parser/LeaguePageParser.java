package com.loris.soccer.web.downloader.okooo.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.bean.data.table.League;
import com.loris.soccer.bean.data.table.Round;
import com.loris.soccer.bean.data.table.Season;
import com.loris.soccer.bean.data.table.SeasonTeam;
import com.loris.soccer.bean.data.table.Team;

/**
 * 联赛主页数据解析
 * 
 * @author jiean
 *
 */
public class LeaguePageParser extends AbstractWebPageParser
{
	/** 球队数据 */
	List<Team> teams = new ArrayList<>();
	
	/** 赛季列表 */
	List<Season> seasons = new ArrayList<>();
	
	/** 赛季球队数据 */
	List<SeasonTeam> seasonTeams = new ArrayList<>();
	
	/** 赛季轮次 */
	List<Round> rounds = new ArrayList<>();
	
	/** 联赛数据 */
	League league;
	
	/** 赛季数据 */
	Season season;
	
	
	/**
	 * 解析联赛主页面
	 * 
	 * @param page 页面数据
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The Okooo JcPageParser is not completed or Content is null. ");
		}
		
		if(!"league".equals(page.getType()))
		{
			return false;
		}
		Document doc = parseHtml(page);
		if(doc == null)
		{
			return false;
		}
		
		//解析赛季数据与球队数据
		Element element = doc.selectFirst(".zxmainmenu .LeftLittleWidth");
		if(element == null)
		{
			return false;
		}
		parseSeasonsAndTeams(element);
		
		return true;
	}
	
	/**
	 * 解析赛季数据与球队数据
	 * 
	 * @param element 
	 */
	protected void parseSeasonsAndTeams(Element element)
	{
		Elements elements = element.select(".LotteryListBrim");
		Element e1 = elements.get(0);
		String logo = e1.selectFirst("img").attr("src");
		if(StringUtils.isNotEmpty(logo))
		{
			league.setLogo(logo);
		}
		
		e1 = elements.get(2);
		parseSeasons(e1);
		
		//设置赛季数年
		if(season == null)
		{
			season = seasons.get(0);
		}
		
		e1 = elements.get(3);
		parseTeams(e1);
	}
	
	/**
	 * 解析球队数据
	 * 
	 * @param element
	 */
	protected void parseTeams(Element element)
	{
		Elements elements = element.select(".LotteryList_Data table tr");
		String name;
		String tid;
		
		
		int size = elements.size();
		for (int i = 1; i < size; i ++)
		{
			Elements es = elements.get(i).select("td");
			Element e1 = es.get(0).selectFirst("a");
			if(e1 == null)
			{
				continue;
			}
			name = e1.text();
			tid = getLastNumberValue(e1.attr("href"));
			
			if(!NumberUtil.isNumber(tid))
			{
				continue;
			}
			
			Team team = new Team();
			team.setName(name);
			team.setTid(tid);
			
			teams.add(team);
			
			SeasonTeam seasonTeam = new SeasonTeam();
			seasonTeam.setTid(tid);
			seasonTeam.setName(name);
			seasonTeam.setLid(league.getLid());
			seasonTeam.setSeason(season.getSeason());
			seasonTeams.add(seasonTeam);
		}
	}
	
	/**
	 * 解析赛季数据
	 * 
	 * @param element 赛季数据
	 */
	protected void parseSeasons(Element element)
	{
		String season;
		String name;
		Elements elements = element.select(".LotteryList_Data ul li");
		for (Element element2 : elements)
		{
			Element e1 = element2.selectFirst("a");
			season = e1.attr("href");
			name = e1.text();
			
			if(StringUtils.isEmpty(name) || StringUtils.isEmpty(season))
			{
				continue;
			}
			season = getLastNumberValue(season);
			
			Season season2 = new Season();
			season2.setLid(league.getLid());
			season2.setSeason(season);
			season2.setName(name);
			seasons.add(season2);
		}
	}

	/**
	 * 获得球队数据
	 * 
	 * @return 获得球队数据
	 */
	public List<Team> getTeams()
	{
		return teams;
	}

	public List<Season> getSeasons()
	{
		return seasons;
	}

	public List<SeasonTeam> getSeasonTeams()
	{
		return seasonTeams;
	}

	public List<Round> getRounds()
	{
		return rounds;
	}

	public League getLeague()
	{
		return league;
	}


	public void setLeague(League league)
	{
		this.league = league;
	}
}
