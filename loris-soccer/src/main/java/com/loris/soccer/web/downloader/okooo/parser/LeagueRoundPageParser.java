package com.loris.soccer.web.downloader.okooo.parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.bean.okooo.OkoooMatch;
import com.loris.soccer.web.downloader.okooo.page.OkoooWebPage;

/**
 * 解析数据
 * @author jiean
 *
 */
public class LeagueRoundPageParser extends AbstractWebPageParser
{
	private static Logger logger = Logger.getLogger(LeagueRoundPageParser.class);
	
	String lid;
	String season;
	String round;
	int year;
	
	List<OkoooMatch> matches = new ArrayList<>();
	
	/**
	 * 解析联赛的比赛数据
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The Okooo JcPageParser is not completed or Content is null. ");
		}
		
		if(!"curround".equals(page.getType()))
		{
			return false;
		}
		
		OkoooWebPage page2 = (OkoooWebPage)page;
		
		//联赛编号，暂时使用这个数据吧
		lid = page2.getMid();
		
		Document doc = parseHtml(page2);
		if(doc == null)
		{
			return false;
		}
		year = Calendar.getInstance().get(Calendar.YEAR);
		Element element = doc.selectFirst(".ddbox .zxmaindata");
		if(element == null)
		{
			return false;
		}
		
		parseMatches(element);
		parseRanks(element);
		
		return true;
	}
	
	/**
	 * 解析比赛数据
	 * @param element
	 */
	public void parseMatches(Element element)
	{
		if(!parseCurrentRound(element))
		{
			logger.info("Parse round info error.");
			return;
		}
		
		Elements element2 = element.select("#team_fight_table tr");
		for (Element el : element2)
		{
			parseMatch(el);
		}
	}
	
	/**
	 * 解析排名数据
	 */
	public void parseRanks(Element element)
	{
		
	}
	
	/**
	 * 解析比赛数据
	 * @param element
	 */
	public void parseMatch(Element element)
	{
		String mid = element.attr("matchid");
		if(StringUtils.isEmpty(mid))
		{
			logger.info("The elelemt: " + element.tagName() + " has no matchid, no parse.");
			return;
		}
		Elements childs = element.children();
		String time = childs.get(0).text();
		String round = childs.get(1).text();
		String homename = childs.get(2).text();
		String score = childs.get(3).text().trim();
		String clientname = childs.get(4).text();
		
		if("vs".equalsIgnoreCase(score))
		{
			score = "";
		}
		
		OkoooMatch match = new OkoooMatch();
		match.setLid(lid);
		match.setMid(mid);
		match.setScore(score);
		match.setHomeid(homename);
		match.setClientid(clientname);
		match.setRound(round);
		match.setSeason(season);
		match.setMatchtime(year + "-" + time);
		
		matches.add(match);
	}
	
	/**
	 * 解析当前轮次数据
	 * @param element
	 * @return
	 */
	public boolean parseCurrentRound(Element element)
	{
		Element roundEle = element.selectFirst(".tableborder .linkblock .linkblock_select");
		if(roundEle != null)
		{			
			String roundinfo = roundEle.select("a").attr("href");
			String[] strings = roundinfo.split("/");
			round = strings[strings.length - 1];
			season = strings[strings.length - 2];
			logger.info("Season: " + season + ", round: " + round);
			return true;
		}
		return false;
	}
	
	public List<OkoooMatch> getMatches()
	{
		return matches;
	}
}
