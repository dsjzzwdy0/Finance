package com.loris.soccer.web.downloader.zgzcw.parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.DateUtil;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.bean.data.table.JcMatch;
import com.loris.soccer.bean.data.table.League;
import com.loris.soccer.bean.item.MatchItem;
import com.loris.soccer.repository.SoccerManager;
import com.loris.soccer.web.downloader.zgzcw.page.LotteryWebPage;

public class LotteryJcWebPageParser extends AbstractWebPageParser
{
	private List<JcMatch> jcmatches = new ArrayList<JcMatch>();
	
	/**
	 * Parse the web page.
	 * 
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The WebPage is not completed or Content is null. ");
		}
		
		if(!(page instanceof LotteryWebPage))
		{
			return false;
		}

		LotteryWebPage page2 = (LotteryWebPage)page;
		if(!"jc".equals(page2.getIssuetype()))
		{
			return false;
		}
		
		//leagues = LotteryManager.getInstance().getLeagueList();
		
		Document doc = parseHtml(page2);
		
		Elements elements = doc.select(".tz-wap .tz-body table");
		for (Element element : elements)
		{
			parseIssueMatches(page2.getIssue(), element);
		}
		return true;
	}
	
	/**
	 * Parse the Issue
	 * @param element
	 */
	protected void parseIssueMatches(String issue, Element element2)
	{
		Elements elements = element2.select("tbody tr");
		for (Element element : elements)
		{
			JcMatch match = new JcMatch();

			String opentime = element.attr("t");
			//String id = element.attr("id");
			String expire = element.attr("expire");
			match.setClosed("0".equals(expire));
			//String ltype = element.attr("m");
			match.setMatchtime(opentime);
			String i = getIssue(opentime);
			i = StringUtils.isEmpty(i) ? issue : i;			
			match.setIssue(i);
			match.setDate(i);
			
			parseJcMatch(match, element);
			
			jcmatches.add(match);
		}
	}
	
	/**
	 * Parse the match info.
	 * 
	 * @param match
	 * @param element
	 */
	protected void parseJcMatch(JcMatch match, Element element0)
	{
		int index = 0;
		Elements elements = element0.select("td");
		Element element = elements.get(index ++);   //0
		String value = element.select("i").first().text();
		match.setOrdinary(value);
	 
		element = elements.get(index ++);   //1: wh-2
		value = element.text();
		if(value != null)
		{
			League league = searchLeague(value.trim());
			match.setLeaguename(value);
			match.setLid(league != null ? league.getLid() : "");
		}
		
		element = elements.get(index ++);    //2
		processIsuueMatchTime(match, element);
		
		element = elements.get(index ++);    //3 : wh-4
		processTeamInfo(match, true, element);
		
		element = elements.get(index ++);    //4 , wh-5
		
		
		element = elements.get(index ++);    //5,  wh-6
		processTeamInfo(match, false, element);
		
		element = elements.get(index ++);    //6, wh-8
		processOddsInfo(match, element);
		
		element = elements.get(index ++);    //7, wh-10
		value = element.attr("newPlayid");
		match.setMid(value);                  //比赛的ID编号
		
		//System.out.println(elements.size());
	}
	
	/**
	 * Parse the match time.
	 * 
	 * @param match
	 * @param element0
	 */
	protected void processIsuueMatchTime(JcMatch match, Element element0)
	{
		String value;
		Elements elements = element0.select("span");
		Element element = elements.get(0);
		//if(match.is())
		//{
		//	value = element.text();
			//match(value);
		//}
		element = elements.get(1);
		value = element.attr("title");
		if(value != null)
		{
			value = value.replace("截期时间:", "");
		}
		match.setClosetime(value);
		
		element = elements.get(2);
		value = element.attr("title");
		if(value != null)
		{
			value = value.replace("比赛时间:", "");
		}
		match.setMatchtime(value);
	}
	
	/**
	 * Process the team info.
	 * <em class="pm ">
     * 		[1]</br>
     *      <i title="马来西亚超级联赛">马超</i>
     * </em>
     * <a href="http://saishi.zgzcw.com/soccer/team/3629" target="_blank" title="">柔　佛</a>
	 * @param match
	 * @param host
	 * @param element0
	 */
	protected void processTeamInfo(JcMatch match, boolean host, Element element0)
	{
		Element element = element0.selectFirst("a");
		Element element2 = element0.selectFirst("em").selectFirst("i");
		Element element3 = element0.selectFirst("em");
		
		
		//System.out.println("element2: " + element2);
		//System.out.println("element2 > em > i: " + element2.selectFirst("i"));
		
		String value = element.text();
		value = trimSpace(value);
		String pm = element3.text();
		String lname = element2 == null ? match.getLeaguename() : element2.text();
		String lid = element2 == null ? match.getLid() : searchLeagueId(lname);
		String tid = element.attr("href");
		int r = 0;
		if(StringUtils.isNotEmpty(pm))
		{
			r = NumberUtil.parseIntegerFromString(pm);
		}
		tid = getTeamId(tid);
		if(host)
		{
			match.setHomename(value);
			match.setHomelid(lid);
			match.setHomeid(tid);
			if(r > 0)
			{
				match.setHomerank(Integer.toString(r));
			}
		}
		else 
		{
			match.setClientname(value);
			match.setClientlid(lid);
			match.setClientid(tid);
			if(r > 0)
			{
				match.setClientrank(Integer.toString(r));
			}
		}
	}
	
	/**
	 * Process the Odds info.
	 * @param match
	 * @param element0
	 */
	protected void processOddsInfo(JcMatch match, Element element0)
	{
		Elements elements = element0.select("div");
		processOddsValue(match, 0, elements.get(0));
		processOddsValue(match, 1, elements.get(1));
		
	}
	
	/**
	 * Process the odds value. Example:
	 * <div class="tz-area frq" isdg="0" pid='49' id="ch_103373_49" mid='103373'>
     *    <em class="rq ">0</em>                  
     *    <a href="javascript:void(0)" id="td_103373_49_0" class="weisai">1.24<s></s></a>
     *    <a href="javascript:void(0)" id="td_103373_49_1" class="weisai">4.90<s></s></a>
     *    <a href="javascript:void(0)" id="td_103373_49_2" class="weisai">8.40<s></s></a>
     *    <em class="all" id="all_103373_49">
     *    <input type="checkbox" class="martop9" /></em>
     *</div>
	 * 
	 * @param match
	 * @param type
	 * @param element0
	 */
	protected void processOddsValue(JcMatch match, int type, Element element0)
	{
		
		Element e2 = element0.selectFirst("em");
		String value = e2.text();
		Elements es = element0.select("a");
		String win, draw, lose;
		win = es.get(0).text();
		if("未开售".equals(win))
		{
			if(type == 0)
			{
				match.setIsopen(false);
			}
			else
			{
				match.setIsrqopen(false);
			}
			return;
		}
		else
		{
			if(type == 0)
			{
				match.setIsopen(true);
			}
			else
			{
				match.setIsrqopen(true);
			}
		}
		draw = es.get(1).text();
		lose = es.get(2).text();
		
		if(type == 0)
		{
			match.setWinodds(win);
			match.setDrawodds(draw);
			match.setLoseodds(lose);
		}
		else
		{
			match.setRangqiu(value);
			match.setRqwinodds(win);
			match.setRqdrawodds(draw);
			match.setRqloseodds(lose);
		}
	}
	
	/**
	 * Example: http://saishi.zgzcw.com/soccer/team/9946
	 * 
	 * @param str
	 * @return
	 */
	protected String getTeamId(String str)
	{
		if(StringUtils.isEmpty(str))
		{
			return "";
		}
		int pos = str.lastIndexOf("/");
		return str.substring(pos >= 0 ? pos + 1 : 0);
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
	 * Search league id.
	 * 
	 * @param name
	 * @return
	 */
	public String searchLeagueId(String name)
	{
		League league = searchLeague(name);
		return league == null ? "" : league.getLid();
	}
	
	/**
	 * Get the matches.
	 * 
	 * @return
	 */
	public List<JcMatch> getJcMatches()
	{
		return jcmatches;
	}
	
	/**
	 * 获得比赛列表
	 * 
	 * @return 比赛列表
	 */
	public List<MatchItem> getMatches()
	{
		List<MatchItem> matchs = new ArrayList<>();
		for (JcMatch jcmatch : jcmatches)
		{
			matchs.add(jcmatch.createMatch());
		}
		return matchs;
	}
	
	/**
	 * Trim space in the middle of the str value.
	 * 
	 * @param str
	 * @return
	 */
	protected String trimSpace(String str)
	{
		return str.replaceAll("　", "");
	}
	
	/**
	 * 计算ISSUE的值, 11:30~次日11:30
	 * 
	 * @param time 比赛时间
	 * @return issue值
	 */
	protected String getIssue(String time)
	{
		Date date = DateUtil.tryToParseDate(time);
		if(date == null)
		{
			return "";
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int h = calendar.get(Calendar.HOUR_OF_DAY);
		int m = calendar.get(Calendar.MINUTE);
		
		if((h > 11) || (h == 11 && m > 30))
		{
			return DateUtil.formatDay(date);
		}		
		else
		{
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			return DateUtil.formatDay(calendar.getTime());
		}		
	}
}
