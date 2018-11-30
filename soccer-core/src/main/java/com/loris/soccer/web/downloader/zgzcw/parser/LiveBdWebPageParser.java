package com.loris.soccer.web.downloader.zgzcw.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.bean.data.table.lottery.BdMatch;
import com.loris.soccer.bean.model.Keys;
import com.loris.soccer.web.downloader.zgzcw.page.LiveWebPage;

/**
 * 解析北单数据的页面
 * @author jiean
 *
 */
public class LiveBdWebPageParser extends AbstractWebPageParser
{
	private static Logger logger = Logger.getLogger(LiveBdWebPageParser.class);
	
	/** 期次目录 */
	Keys issues = new Keys();
	
	/** 当前期次 */
	String currentIssue = "";
	
	/** 比赛列表 */
	List<BdMatch> matches = new ArrayList<>();

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
		
		Element matchesElement = document.selectFirst(".bf-main .live-sta #matchTable tbody");
		if(matchesElement != null)
		{
			parseMatches(matchesElement);
		}
		
		return false;
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
		/*String lid;
		String leaguename;
		String round;
		String matchtime;
		String homeid;
		String homename;
		String clientid;
		String clientname;
		String homerank;
		String clientrank;
		String rq;*/
		
		Elements es = element.getAllElements();
		
		BdMatch match = new BdMatch();
		ordinary = es.get(0).text();
		match.setOrdinary(ordinary);
		
		parseLeagueInfo(match, es.get(1));
		//lid = match.getLid();
		//leaguename = match.getLeaguename();
		
		matches.add(match);
	}
	
	/**
	 * 解析比赛的联赛信息
	 * @param match
	 * @param element
	 */
	protected void parseLeagueInfo(BdMatch match, Element element)
	{
		
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

	public List<BdMatch> getMatches()
	{
		return matches;
	}

	public void setMatches(List<BdMatch> matches)
	{
		this.matches = matches;
	}
}
