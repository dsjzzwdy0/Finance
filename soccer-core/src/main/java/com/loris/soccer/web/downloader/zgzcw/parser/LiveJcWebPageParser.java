package com.loris.soccer.web.downloader.zgzcw.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.data.Keys;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.analysis.util.IssueMatchUtil;
import com.loris.soccer.bean.data.table.JcMatch;
import com.loris.soccer.bean.type.MatchTeamType;
import com.loris.soccer.web.downloader.zgzcw.page.LiveWebPage;

public class LiveJcWebPageParser extends AbstractWebPageParser
{
	private static Logger logger = Logger.getLogger(LiveJcWebPageParser.class);
	
	List<JcMatch> matches = new ArrayList<>();
	
	String currentIssue = "";
	
	/** 期次目录 */
	Keys issues = new Keys();
	
	@Override
	public boolean parseWebPage(WebPage page)
	{
		List<Class<?>> clazzes = new ArrayList<>();
		clazzes.add(LiveWebPage.class);
		if(!checkBasicInfo(page, clazzes))
		{
			return false;
		}

		Document document = parseHtml(page);
		if(document == null)
		{
			logger.info("The parse result is null, exit.");
			return false;
		}
		
		Element issueEl = document.selectFirst(".top-chosse #matchSel");
		if(issueEl != null)
		{
			currentIssue = getSelectElementValue(issueEl);
			issues = new Keys(getSelectElementValues(issueEl));
		}
		
		Element matchesElement = document.selectFirst(".bf-main .live-sta #matchTable tbody");
		if(matchesElement != null)
		{
			parseMatches(matchesElement);
		}
		
		return true;
	}
	
	/**
	 * 解析比赛数据
	 * @param element
	 */
	protected void parseMatches(Element element)
	{
		Elements elements = element.select("tr");
		for (Element element2 : elements)
		{
			parseMatch(element2);
		}
	}
	
	/**
	 * 解析比赛数据
	 * @param element
	 */
	protected void parseMatch(Element element)
	{
		String ordinary;
		String mid;
		//String round;
		String matchtime;
		//String score;
		//String halfscore;

		
		Elements es = element.children();
		
		JcMatch match = new JcMatch();
		mid = element.attr("matchid");
		ordinary = es.get(0).text();
		if(StringUtils.isNotEmpty(ordinary) && ordinary.startsWith("周"))
		{
			ordinary = ordinary.substring(2);
		}
		//round = es.get(2).text();
		matchtime = es.get(3).attr("date");
		//score = es.get(6).text();
		//halfscore = es.get(8).text();
		
		parseLeagueInfo(match, es.get(1));								//联赛编号、名称
		parseTeamInfo(match, es.get(5), MatchTeamType.HOME);			//主队名称、编号、排名、让球数
		parseTeamInfo(match, es.get(7), MatchTeamType.CLIENT);			//客队名称、编号、排名
		
		match.setOrdinary(ordinary);
		match.setMid(mid);
		match.setMatchtime(matchtime);
		match.setIssue(currentIssue);
		match.setDate(IssueMatchUtil.getIssueDay(matchtime));
		
		matches.add(match);
	}
	
	/**
	 * 解析比赛的联赛信息
	 * @param match
	 * @param element
	 */
	protected void parseLeagueInfo(JcMatch match, Element element)
	{
		Element el = element.selectFirst("a");
		String leagueInfo = el.attr("href");
		leagueInfo = getLastNumberValue(leagueInfo);
		String leagueName = element.selectFirst("span").text();
		match.setLid(leagueInfo);
		match.setLeaguename(leagueName);
	}
	
	/**
	 * 解析球队信息
	 * @param match
	 * @param element
	 * @param type
	 */
	protected void parseTeamInfo(JcMatch match, Element element, MatchTeamType type)
	{
		String rank = "";
		String name = "";
		String teamid = "";
		
		Element teamInfo = element.selectFirst("a");
		name = teamInfo.text();
		teamid = getLastNumberValue(teamInfo.attr("href"));
		Element paim = element.selectFirst(".paim");
		if(paim != null)
		{
			rank = NumberUtil.parseFirstIntegerString(paim.text());
			
		}
		if(type == MatchTeamType.HOME)
		{
			Element rqEl = element.selectFirst(".rq strong");
			if(rqEl != null)
			{
				match.setRangqiu(rqEl.text());
			}
		}
		
		if(MatchTeamType.HOME == type)
		{
			match.setHomeid(teamid);
			match.setHomerank(rank);
			match.setHomename(name);
		}
		else
		{
			match.setClientid(teamid);
			match.setClientname(name);
			match.setClientrank(rank);
		}
	}

	public List<JcMatch> getMatches()
	{
		return matches;
	}

	public String getCurrentIssue()
	{
		return currentIssue;
	}

	public void setCurrentIssue(String currentIssue)
	{
		this.currentIssue = currentIssue;
	}

	public Keys getIssues()
	{
		return issues;
	}

	public void setIssues(Keys issues)
	{
		this.issues = issues;
	}
}
