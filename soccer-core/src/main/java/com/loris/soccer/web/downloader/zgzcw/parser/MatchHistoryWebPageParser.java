package com.loris.soccer.web.downloader.zgzcw.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.loris.base.bean.web.WebPage;
import com.loris.base.util.DateUtil;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.bean.data.table.league.League;
import com.loris.soccer.bean.data.table.league.Match;
import com.loris.soccer.repository.SoccerManager;

public class MatchHistoryWebPageParser extends AbstractWebPageParser
{
	/** 比赛历史 */
	List<Match> historyMatchs = new ArrayList<>();
	
	final static String MATCH_HISTORY_TYPE = "history";
	
	/**
	 * 数据解析
	 * 
	 * @param page 数据页面
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		if(!checkBase(page, MATCH_HISTORY_TYPE))
		{
			return false;
		}
		
		//解析数据页面
		Document doc = parseHtml(page);
		if(doc == null)
		{
			return false;
		}
		
		//
		Element element = doc.selectFirst(".bsls .bslsr");
		if(element == null)
		{
			return false;
		}
		
		Elements elements = element.children();
		
		//至少有三个节点，
		//第0个节点的数据是主队的最近几场交战记录
		//第1个节点的数据是客队的最的几场交战记录
		//第2个节点的数据是双方最近的交战记录
		if(elements == null || elements.size() < 3)  
		{
			return false;
		}
		
		//主队历史比赛
		Element e = elements.get(1);
		processBsls(e);
		
		//客队历史比赛
		e = elements.get(2);
		processBsls(e);
		
		//两队交锋历史
		e = elements.get(3);
		processBsls(e);
		
		return true;
	}
	
	/**
	 * 历史数据解析
	 * 
	 * @param element 数据元素
	 */
	protected void processBsls(Element element)
	{
		Elements els = element.select("tbody tr");
		for (Element el : els)
		{
			processBslsMatchRecord(el);
		}
	}
	
	/**
	 * 处理比赛历史数据
	 * 
	 * @param element 每一条数据记录
	 */
	protected void processBslsMatchRecord(Element element)
	{
		Elements elements = element.select("td");
		if(elements == null || elements.size() < 6)
		{
			return;
		}
		
		String mid;
		String time;
		String hteam;
		String round;
		String cteam;
		String score;
		String halfscore;
		String handicap;		
		String leaguename;
		String season;
		
		leaguename = elements.get(0).text();
		round = elements.get(1).text();
		season = elements.get(2).text();
		hteam = elements.get(3).selectFirst("a").attr("href");
		hteam = getLastNumberValue(hteam);
		Element e1 = elements.get(4).selectFirst("a");
		score = e1.text();
		mid = getLastNumberValue(e1.attr("href"));
		cteam = elements.get(5).selectFirst("a").attr("href");
		cteam = getLastNumberValue(cteam);
		halfscore = elements.get(6).text();		
		e1 = elements.get(8).selectFirst(".peilv-span .ls-peilv5");
		handicap = e1 == null ? "": e1.text();
		Date date = DateUtil.tryToParseDate(season);
		time = DateUtil.formatDay(date);
		League league = searchLeague(leaguename);
		
		//比赛还没有结果，则不需要该数据
		if("VS".equalsIgnoreCase(score))
		{
			return;
		}
		
		Match match = new Match();
		match.setMid(mid);
		match.setLid(league == null ? leaguename : league.getLid());
		match.setRound(round);
		match.setMatchtime(time);
		match.setSeason(season);
		match.setHomeid(hteam);
		match.setClientid(cteam);
		match.setEnded();
		match.setHandicap(handicap);
		match.setScore(score);
		match.setHalfscore(halfscore);
		
		addHistoryMatch(match);
	}
	
	/**
	 * 添加比赛到历史数据库中
	 * 
	 * @param match 比赛数据
	 */
	protected void addHistoryMatch(Match match)
	{
		for (Match m : historyMatchs)
		{
			//如果已经存在于数据库中，则不添加
			if(m.getMid().equals(match.getMid()))
			{
				return;
			}
		}
		historyMatchs.add(match);
	}
	
	/**
	 * 
	 * @param lid
	 * @param day
	 * @return
	 */
	protected String getSeason(String lid, String day)
	{
		Date date = DateUtil.tryToParseDate(day);
		if(date == null)
		{
			return day;
		}
		
		return "";
	}
	
	
	/**
	 * Get the league value.
	 * 
	 * @param name
	 * @return
	 */
	public League searchLeague(String name)
	{
		return SoccerManager.getLeagueByName(name);
	}

	/**
	 * 获得历史比赛数据
	 * 
	 * @return 历史比赛数据集
	 */
	public List<Match> getHistoryMatchs()
	{
		return historyMatchs;
	}
}
