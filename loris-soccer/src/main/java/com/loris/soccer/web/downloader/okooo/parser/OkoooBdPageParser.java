package com.loris.soccer.web.downloader.okooo.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.DateUtil;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.bean.okooo.OkoooBdMatch;

public class OkoooBdPageParser extends AbstractWebPageParser
{
	private static Logger logger = Logger.getLogger(OkoooBdPageParser.class);
	
	/** 解析的结果 */
	Map<String, List<OkoooBdMatch>> matchMap = new HashMap<>();

	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The Okooo JcPageParser is not completed or Content is null. ");
		}
		
		if(!"bd".equals(page.getType()))
		{
			return false;
		}
		Document doc = parseHtml(page);
		if(doc == null)
		{
			return false;
		}
		return parseBdMatches(doc);
	}
	
	/**
	 * 解析北单数据
	 * @param doc
	 * @return
	 */
	protected boolean parseBdMatches(Document doc)
	{
		Elements elements = doc.select("#gametablesend .jcmaintable");
		if(elements == null || elements.size() <= 0)
		{
			return false;
		}
		for (Element element : elements)
		{
			parseBdIssue(element);
		}
		return true;
	}
	
	/**
	 * 解析北单数据
	 * @param element
	 */
	protected void parseBdIssue(Element element)
	{
		String issue;
		int size;
		Elements elements = element.select("tr");
		size = elements.size();
		if(size <= 0)
		{
			logger.info("No issue element.");
			return;
		}	
		
		Element el = elements.get(0);
		issue = getIssue(el);
		//logger.info("Issue: " + issue);
		
		List<OkoooBdMatch> matchs = new ArrayList<>();
		OkoooBdMatch match = null;
		for(int i = 1; i < size; i ++)
		{
			el = elements.get(i);
			match = parseBdMatch(el);
			if(match != null)
			{
				match.setIssue(issue);
				match.setDate(issue);
				matchs.add(match);
			}
		}
		
		matchMap.put(issue, matchs);
	}
	
	/**
	 * 解析比赛数据
	 * 
	 * @param element 每场比赛的数据
	 * @return 解析完成的比赛数据
	 */
	private OkoooBdMatch parseBdMatch(Element element)
	{
		Elements elements = element.children();
		if(elements.size() <= 0)
		{
			return null;
		}
		
		OkoooBdMatch match = new OkoooBdMatch();
		String mid = "";
		String ordinary;
		String leaguename;
		String lid;
		
		String matchtime;
		String closetime;
		
		mid = element.attr("matchid");
		
		//联赛信息
		Element el = elements.get(0);
		ordinary = el.selectFirst("span i").text();
		Element e2 = el.selectFirst("a");
		leaguename = e2.text();
		lid = getLeadueId(e2.attr("href"));
		
		//比赛时间信息
		el = elements.get(1);
		matchtime = el.attr("title");
		matchtime = getMatchTime(matchtime);
		closetime = el.selectFirst(".BuyTime").text();
		closetime = getCloseTime(matchtime, closetime);
		
		//球队信息与奖金信息
		el = elements.get(2);
		if(!parseTeamAndOdds(el, match))
		{
			return null;
		}
		
		/*
		Elements teams = el.select("a");
		homename = teams.get(0).text();
		clientname = teams.get(1).text();
		mid = getMatchId(teams.get(0).attr("href"));
		
		//奖金信息
		el = elements.get(4);
		Element oddsEl = el.selectFirst(".frqBetObj");
		parseOdds(oddsEl, match);*/

		//el = elements.get(7);
		//mid = getMatchId(el.selectFirst("a").attr("href"));
		
		//设置比赛的信息
		match.setMid(mid);
		match.setOrdinary(ordinary);
		match.setLeaguename(leaguename);
		match.setLid(lid);
		match.setMatchtime(matchtime);
		match.setClosetime(closetime);
		
		return match;
	}
	
	/**
	 * 解析主队客队信息
	 * @param element
	 * @param match
	 * @return 解析是否成功的标志
	 */
	protected boolean parseTeamAndOdds(Element element, OkoooBdMatch match)
	{
		String homename;
		String clientname;
		String homerank = "";
		String clientrank = "";
		String rq;
		String winodds;
		String drawodds;
		String loseodds;
		
		Elements elements = element.children();
		if(elements.size() < 3)
		{
			return false;
		}
		
		//主队信息
		Element hrefElemement = elements.get(0);
		Element e1 = hrefElemement.selectFirst(".paim_em");
		if(e1 != null && e1.children() != null && e1.children().size() == 2)
		{
			homerank = "" + NumberUtil.parseIntegerFromString(e1.children().get(1).text());
		}
		homename = hrefElemement.selectFirst(".homename").text();
		rq = parseRangqiu(hrefElemement.selectFirst(".handicapobj").text());
		winodds = hrefElemement.selectFirst(".pltxt").text();
		
		//平局信息
		hrefElemement = elements.get(1);
		drawodds = hrefElemement.selectFirst("em").text();
		
		//客队信息
		hrefElemement = elements.get(2);
		e1 = hrefElemement.selectFirst(".paim_em");
		if(e1 != null && e1.children() != null && e1.children().size() == 2)
		{
			clientrank = "" + NumberUtil.parseIntegerFromString(e1.children().get(1).text());
		}
		clientname = hrefElemement.selectFirst(".awayname").text();
		loseodds = hrefElemement.selectFirst(".pltxt").text();
		
		match.setHomename(homename);
		match.setHomerank(homerank);
		match.setClientrank(clientrank);
		match.setClientname(clientname);
		match.setRangqiu(rq);
		match.setWinodds(winodds);
		match.setDrawodds(drawodds);
		match.setLoseodds(loseodds);
		return true;
	}
	
	/**
	 * 处理让球数据
	 * @param value 原始值
	 * @return 让球数据
	 */
	protected String parseRangqiu(String value)
	{
		if(StringUtils.isEmpty(value))
		{
			return "";
		}
		value = value.trim();
		value = value.replace("(", "");
		value = value.replace(")", "");
		return value;
	}
	
	/**
	 * Get the Lid value.
	 * 
	 * @param url
	 * @return
	 */
	protected String getLeadueId(String url)
	{
		String[] values = url.split(RITHG_SLASH.pattern());
		int size = values.length;
		String tid = values[size - 1];
		return tid;
	}
	
	/**
	 * 获得比赛时间
	 * 
	 * @param matchtime
	 * @return
	 */
	private String getMatchTime(String matchtime)
	{
		String str = matchtime.replace("比赛时间：", "");
		return str;
	}
	
	/**
	 * 获得比赛的ID值
	 * @param matchtrends
	 * @return
	 */
	protected String getMatchId(String matchtrends)
	{
		String[] values = matchtrends.split(RITHG_SLASH.pattern());
		int size = values.length;
		String mid = values[size - 2];
		return mid;
	}
	
	/**
	 * 获得期号信息
	 * 
	 * @param titleElement 信息元素
	 * @return 期号
	 */
	private String getIssue(Element titleElement)
	{
		Element el = titleElement.selectFirst("td span span");
		if(el != null)
		{
			String issue = el.text();
			Date date = DateUtil.tryToParseDate(issue);
			if(date != null)
			{
				return DateUtil.formatDay(date);
			}
		}
		return "";
	}
	
	/**
	 * 获得截止时间
	 * 
	 * @param matchtime 比赛时间
	 * @param closetime 截止小时
	 * @return 关闭的时间
	 */
	private String getCloseTime(String matchtime, String closetime)
	{
		return closetime;
	}

	public Map<String, List<OkoooBdMatch>> getMatchMap()
	{
		return matchMap;
	}	
}
