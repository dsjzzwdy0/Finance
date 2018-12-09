package com.loris.soccer.web.downloader.zgzcw.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.bean.table.Match;
import com.loris.soccer.bean.table.Team;
import com.loris.soccer.web.downloader.zgzcw.page.RoundLeagueWebPage;

public class RoundLeagueWebPageParser extends AbstractWebPageParser
{
	/** The matches.*/
	private List<Match> matches = new ArrayList<Match>();
	
	/** The Teams. */
	private List<Team> teams = new ArrayList<>();
	
	/** The lid value. */
	protected String lid;
	
	/** The season value. */
	protected String season;
	
	/** The round value. */
	protected String round;
	
	/**
	 * Create a new instance of LeagueMatchParser
	 */
	public RoundLeagueWebPageParser()
	{
		round = "";
	}
	
	/**
	 * Parse the web page.
	 * @return
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The WebPage is not completed or Content is null. ");
		}

		if(!(page instanceof RoundLeagueWebPage))
		{
			return false;
		}
		
		RoundLeagueWebPage page2 = (RoundLeagueWebPage)page;
		lid = page2.getLid();
		season = page2.getSeason();
		round = page2.getRound();
		
		Document document = parseHtml(page2);		
		Element element = document.select(".zstab").first();
		
		//解析比赛信息
		parseMatches(round, element);
		
		return true;
	}
	
	/**
	 * parse the match.
	 * 
	 * @param round
	 * @param element
	 */
	protected void parseMatches(String round, Element element)
	{
		String mid;
		String time;
		String hteam;
		//String hname;
		//String hlogo = "";
		String cteam;
		//String cname;
		//String clogo = "";
		String score;
		String halfscore;
		String handicap;
		
		Elements elements = element.select("tbody tr");
		for (Element el : elements)
		{
			Elements elements2 = el.select("td");
			if(elements2.size() != 7)
			{
				continue;
			}
			time = elements2.get(0).text();
			hteam = getTeamid(elements2.get(1));
			//hname = elements2.get(1).text();
			/*
			try
			{
				hlogo = elements2.get(1).selectFirst("img").attr("src");
				clogo = elements2.get(3).selectFirst("img").attr("src");
			}
			catch(Exception e)
			{
			}*/
			
			score = elements2.get(2).text();
			cteam = getTeamid(elements2.get(3));
			//cname = elements2.get(3).text();
			halfscore = elements2.get(4).text();
			handicap = elements2.get(5).text();
			mid = getMid(elements2.get(6));
			
			if(StringUtils.isEmpty(mid))
			{
				continue;
			}
			
			Match match = new Match();
			match.setHomeid(hteam);
			match.setMid(mid);
			match.setMatchtime(time);
			match.setClientid(cteam);
			match.setScore(score);
			match.setHalfscore(halfscore);
			match.setHandicap(handicap);
			match.setRound(round);
			match.setSeason(season);
			match.setLid(lid);

			matches.add(match);
			
			/*Team team = new Team();
			team.setTid(hteam);
			team.setName(hname);
			team.setLogo(hlogo);
			teams.add(team);
			
			team = new Team();
			team.setTid(cteam);
			team.setName(cname);
			team.setLogo(clogo);
			teams.add(team);*/
		}
	}
	
	/**
	 * Get team id value.
	 * 
	 * @param element
	 * @return
	 */
	protected String getTeamid(Element element)
	{
		Element element2 = element.select("a").first();
		if(element2 == null)
		{
			throw new IllegalArgumentException();
		}
		String url = element2.attr("href");
		String[] strings = url.split(RITHG_SLASH.pattern());
		int len = strings.length;
		return strings[len - 1];
	}
	
	/**
	 * Get the match id value.
	 * 
	 * @param element
	 * @return
	 */
	protected String getMid(Element element)
	{
		Element element2 = element.select("a").first();
		String url = element2.attr("href");
		String[] strings = url.split(RITHG_SLASH.pattern());
		int len = strings.length;
		return strings[len - 2];
	}

	/**
	 * Get the Matches.
	 * 
	 * @return
	 */
	public List<Match> getMatches()
	{
		return matches;
	}
	
	/**
	 * Get the Teams.
	 * 
	 * @return
	 */
	public List<Team> getTeams()
	{
		return teams;
	}

	public String getLid()
	{
		return lid;
	}


	public String getSeason()
	{
		return season;
	}

	public String getRound()
	{
		return round;
	}	
}
