package com.loris.soccer.web.downloader.okooo.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.loris.base.util.DateUtil;
import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.bean.okooo.OkoooJcMatch;

/**
 * 竞彩足球页面数据解析，这个主页面的地址为：
 * http://www.okooo.com/jingcai/shengpingfu/
 * 
 * @author jiean
 *
 */
public class OkoooJcPageParser extends AbstractWebPageParser
{
	/** 解析的结果 */
	Map<String, List<OkoooJcMatch>> matchMap = new HashMap<>();

	/**
	 * 解析Okooo网络竞彩页面数据
	 * 
	 * @param page 下载页面
	 * @return 解析是否成功的标志
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The Okooo JcPageParser is not completed or Content is null. ");
		}
		
		if(!"jc".equals(page.getType()))
		{
			return false;
		}
		Document doc = parseHtml(page);
		if(doc == null)
		{
			return false;
		}
		return parseJcDocument(doc);
	}
	
	/**
	 * 解析文档结构数据
	 * 
	 * @param doc 文档结构数据
	 * @return 是否成功的标志
	 */
	protected boolean parseJcDocument(Document doc)
	{
		Elements elements = doc.select("#gametablesend .jcmaintable");
		if(elements == null || elements.size() <= 0)
		{
			return false;
		}
		for (Element element : elements)
		{
			parseJcIssue(element);
		}		
		return true;
	}
	
	/**
	 * 按照期号解析比赛数据
	 * 
	 * @param element 数据元素
	 */
	protected void parseJcIssue(Element element)
	{
		String issue;
		int size;
		Elements elements = element.select("tr");
		size = elements.size();
		if(size <= 0)
		{
			System.out.println("No issue element.");
			return;
		}
		
		Element el = elements.get(0);
		issue = getIssue(el);
		
		List<OkoooJcMatch> matchs = new ArrayList<>();
		OkoooJcMatch match = null;
		for(int i = 1; i < size; i ++)
		{
			el = elements.get(i);
			match = parseJcMatch(el);
			if(match != null)
			{
				match.setIssue(issue);
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
	private OkoooJcMatch parseJcMatch(Element element)
	{
		Elements elements = element.select("td");
		if(elements.size() <= 0)
		{
			return null;
		}
		
		OkoooJcMatch match = new OkoooJcMatch();

		String mid;
		String ordinary;
		String leaguename;
		String lid;
		
		String matchtime;
		String closetime;
		
		String homename;
		//String htid;
		
		//String ctid;
		String clientname;
		
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
		
		//球队信息
		el = elements.get(2);
		Elements teams = el.select("a");
		homename = teams.get(0).text();
		clientname = teams.get(1).text();
		mid = getMatchId(teams.get(0).attr("href"));
		
		//奖金信息
		el = elements.get(4);
		Element oddsEl = el.selectFirst(".frqBetObj");
		parseOdds(oddsEl, match);
		//让球信息
		oddsEl = el.selectFirst(".rqBetObj");
		parseRqOdds(oddsEl, match);
		
		//设置比赛的信息
		match.setMid(mid);
		match.setOrdinary(ordinary);
		match.setLeaguename(leaguename);
		match.setLid(lid);
		
		match.setHomename(homename);
		match.setClientname(clientname);
		
		match.setMatchtime(matchtime);
		match.setClosetime(closetime);
		
		return match;
	}
	
	/**
	 * 解析竞彩的赔率
	 * 
	 * @param element 竞彩赔率数据
	 * @param match 比赛数据
	 */
	private void parseOdds(Element element, OkoooJcMatch match)
	{
		String winodds;
		String drawodds;
		String loseodds;
		//boolean isopen = true;
		Elements elements = element.select("a");
		if(elements.size() <= 0)
		{
			match.setIsopen(false);
			return;
		}
		
		match.setIsopen(true);
		winodds = elements.get(0).text();
		drawodds = elements.get(1).text();
		loseodds = elements.get(2).text();
		
		match.setWinodds(winodds);
		match.setDrawodds(drawodds);
		match.setLoseodds(loseodds);
	}
	
	/**
	 * 解析让球数据
	 * 
	 * @param element 让球数据
	 * @param match 比赛数据
	 */
	private void parseRqOdds(Element element, OkoooJcMatch match)
	{
		String rq;
		String rqwinodds;
		String rqdrawodds;
		String rqloseodds;
				
		Elements elements = element.select("div");
		if(elements.size() < 2)
		{
			match.setIsrqopen(false);
			return;
		}
		match.setIsrqopen(true);
		rq = elements.get(0).selectFirst(".handicapObj").text();		
		Elements oddsEls = elements.get(2).select("a");
		
		if(oddsEls.size() < 3)
		{
			return;
		}
		rqwinodds = oddsEls.get(0).text();
		rqdrawodds = oddsEls.get(1).text();
		rqloseodds = oddsEls.get(2).text();
		
		match.setRangqiu(rq);
		match.setRqwinodds(rqwinodds);
		match.setRqdrawodds(rqdrawodds);
		match.setRqloseodds(rqloseodds);
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
	 * 
	 * @param matchtrends
	 * @return
	 */
	private String getMatchId(String matchtrends)
	{
		String[] values = matchtrends.split(RITHG_SLASH.pattern());
		int size = values.length;
		String mid = values[size - 2];
		return mid;
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

	/**
	 * 获得解析完成的比赛数据
	 * 
	 * @return 比赛信息列表
	 */
	public Map<String, List<OkoooJcMatch>> getMatchMap()
	{
		return matchMap;
	}
}
