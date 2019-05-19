package com.loris.soccer.web.downloader.zgzcw.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.bean.table.Round;
import com.loris.soccer.bean.table.Season;
import com.loris.soccer.bean.table.SeasonTeam;
import com.loris.soccer.bean.table.Team;
import com.loris.soccer.web.downloader.zgzcw.page.SeasonWebPage;

/**
 * 赛季数据下载页面
 * 
 * @author jiean
 *
 */
public class SeasonWebPageParser extends AbstractWebPageParser
{
	/** The season list. */
	protected List<Season> seasons = new ArrayList<Season>();
	
	/** The teams list. */
	protected List<Team> teams = new ArrayList<Team>();
	
	/** The League season teams. */
	protected List<SeasonTeam> seasonTeams = new ArrayList<>();
	
	/** The round info. */
	protected List<Round> rounds = new ArrayList<Round>();
	
	/** the league id value. */
	protected String lid;
	
	/** The season. */
	protected String seasonInfo;
	
	/**
	 * Create a new instance of LeagurePageParser.
	 * 
	 */
	public SeasonWebPageParser()
	{
	}
	
	public String getLid()
	{
		return lid;
	}

	public String getSeasonInfo()
	{
		return seasonInfo;
	}

	public List<Season> getSeasons()
	{
		return seasons;
	}

	public List<Team> getTeams()
	{
		return teams;
	}
	
	public List<Round> getRounds()
	{
		return rounds;
	}
	
	public List<SeasonTeam> getSeasonTeams()
	{
		return seasonTeams;
	}

	/**
	 * Create a new instance of LeaguePageParser.
	 * 
	 * @param lid
	 */
	public SeasonWebPageParser(String lid, String season)
	{
		this.lid = lid;
		this.seasonInfo = season;
	}
	
	/**
	 * Set the season info.
	 * 
	 * @param seasonInfo
	 */
	public void setSeasonInfo(String seasonInfo)
	{
		this.seasonInfo = seasonInfo;
	}

	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The WebPage is not completed or Content is null. ");
		}
		
		if(!(page instanceof SeasonWebPage))
		{
			throw new IllegalArgumentException("The WebPage is not a validate SeasonWebPage. ");
		}
		
		SeasonWebPage page2 = (SeasonWebPage) page;
		lid = page2.getLid();
		seasonInfo = page2.getSeason();

		Document doc = parseHtml(page2);
		Element element = doc.select(".league .left .team_out .div-select").first();
		//解析赛季信息
		parseSeasons(element);
		
		if(StringUtils.isEmpty(seasonInfo))
		{
			if(seasons.size() > 0)
			{
				seasonInfo = seasons.get(0).getSeason();
			}
			
			page2.setSeason(seasonInfo);
		}
		
		//解析球队列表情况
		Elements elements = doc.select(".league .left .tongji_list");
		for (Element element2 : elements)
		{
			element = element2.select(">div").first();
			String name = element.text();
			//System.out.println("Parse the Teams: " + name);
			if("球队列表".equals(name))
			{
				//解析球队信息				
				parseTeams(element2);
			}
		}
		
		//解析比赛轮次
		element = doc.select(".league .league_right .table_out .luncib").first();
		parseRound(element);
		
		return true;
	}
	
	/**
	 * Parse the round info.
	 * 
	 * @param element
	 */
	protected void parseRound(Element element)
	{
		if(element == null)
		{
			return;
		}
		String name;
		Elements elements = element.select("em");
		//System.out.println("轮次：");
		for (Element element2 : elements)
		{
			name = element2.text();
			addRound(name, name);
		}
	}
	
	/**
	 * parse the teams.
	 * 
	 * @param element
	 */
	protected void parseTeams(Element element)
	{
		if(element == null)
		{
			return;
		}
		String name;
		String url;
		Elements elements = element.select("a");
		for (Element element2 : elements)
		{
			url = element2.attr("href");
			name = element2.select("li").first().text();
			addTeam(url, name);
		}
	}
	
	/**
	 * parse the seasons.
	 * 
	 * @param element
	 */
	protected void parseSeasons(Element element)
	{
		if(element == null)
		{
			return;
		}
		String url;
		String season;
		Elements elements = element.select("a");
		for (Element element2 : elements)
		{
			url = element2.attr("href");
			season = element2.select("li").first().text();
			addSeason(url, season);
		}
	}
	
	/**
	 * Add the season.
	 * 
	 * @param url
	 * @param season
	 */
	protected void addSeason(String url, String season)
	{
		Season season2 = new Season();
		season2.setSeason(season);
		season2.setLid(lid);	
		seasons.add(season2);
	}
	
	/**
	 * Add the team
	 * 
	 * @param url
	 * @param name
	 */
	protected void addTeam(String url, String name)
	{
		String tid = getTeamId(url);
		
		//球队信息
		Team team = new Team();
		team.setName(name);
		team.setTid(tid);		
		teams.add(team);
		
		//赛季的信息
		SeasonTeam seasonTeam = new SeasonTeam();
		seasonTeam.setTid(tid);
		seasonTeam.setSeason(getSeasonInfo());
		seasonTeam.setLid(lid);
		seasonTeams.add(seasonTeam);
	}
	
	/**
	 * 设置球队的LOGO值
	 * 
	 * @param tid
	 * @param logo
	 */
	protected void setTeamLogo(String tid, String logo)
	{
		for (Team team : teams)
		{
			if(tid.equals(team.getTid()))
			{
				team.setLogo(logo);
			}
		}
	}
	
	/**
	 * Get the TeamId value.
	 * @param url
	 * @return
	 */
	protected String getTeamId(String url)
	{
		String[] values = url.split(RITHG_SLASH.pattern());
		int size = values.length;
		String tid = values[size - 1];
		return tid;
	}
	
	/**
	 * 
	 * @param name
	 */
	protected void addRound(String name, String rid)
	{
		Round round = new Round();
		round.setLid(lid);
		round.setSeason(seasonInfo);
		round.setName(name);
		round.setRid(rid);
		rounds.add(round);
	}

}
