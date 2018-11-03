package com.loris.soccer.web.downloader.zgzcw.parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.web.WebPage;
import com.loris.base.util.DateUtil;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.bean.data.table.lottery.BdMatch;
import com.loris.soccer.web.downloader.zgzcw.page.LotteryWebPage;

public class LotteryBdWebPageParser extends AbstractWebPageParser
{
	/** The Matches. */
	private List<BdMatch> matches = new ArrayList<>();

	/** The Issue value. */
	private String issue;
	
	private String dateReg = "\\d{4}[-]\\d{2}[-]\\d{2}";

	/**
	 * Get the Matches.
	 * 
	 * @return
	 */
	public List<BdMatch> getMatches()
	{
		return matches;
	}

	/**
	 * Get the Issue value.
	 * 
	 * @return
	 */
	public String getIssue()
	{
		return issue;
	}

	/**
	 * Parse WebPage.
	 * 
	 * @param page
	 *            Page Content.
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The WebPage is not completed or Content is null. ");
		}

		if (!(page instanceof LotteryWebPage))
		{
			return false;
		}

		LotteryWebPage page2 = (LotteryWebPage) page;

		if (!"bd".equals(page2.getIssuetype()))
		{
			return false;
		}

		//issue = page2.getIssue();
		Document document = parseHtml(page2);

		return parseIssueWebPage(document);
	}

	/**
	 * Parse the IssueWebPage.
	 * 
	 * @param page
	 * @return
	 */
	protected boolean parseIssueWebPage(Document document)
	{
		parseIssueElement(document);

		Element element = document.selectFirst("#tw #dcc");

		Elements childElements = element.children();
		int childnum = childElements.size() / 2;

		for (int i = 0; i < childnum; i++)
		{
			Element el0 = childElements.get(i * 2);
			Element el1 = childElements.get(i * 2 + 1);

			if (el0 == null || el1 == null)
			{
				continue;
			}
			Element dateEl = el0.selectFirst(".fl strong");
			String dString = "";
			if (dateEl != null)
			{
				dString = dateEl.text();
				//System.out.println("DString: " + dString);
				dString = DateUtil.findDateFromString(dString, dateReg);
			}
			
			parseMatchList(el1, dString);
		}

		/*
		Elements elements = element.select("table");
		// int size = elements.size();
		for (Element el : elements)
		{
			parseMatchList(el);
		}*/
		return true;
	}
	
	protected void parseMatchList(Element element, String date)
	{
		Elements elements = element.select("tbody tr");
		for (Element el : elements)
		{
			String t = el.attr("t");
			String lname = el.attr("m");
			String ord = el.attr("i");
			String expired = el.attr("expire");

			BdMatch match = new BdMatch();
			match.setLeaguename(lname);
			match.setMatchtime(t);
			match.setOrdinary(ord);
			match.setClosed("0".equals(expired));
			match.setDate(date);
			match.setIssue(issue);

			Elements els = el.select("td");
			parseMatchInfo(match, els);

			matches.add(match);
		}
	}

	/**
	 * 解析比赛期号
	 * 
	 * @param document
	 */
	protected void parseIssueElement(Document document)
	{
		Element issueElement = document.selectFirst(".tz-condition .qici #selectissue");
		Elements elements = issueElement.children();
		for (Element element : elements)
		{
			if (element.hasAttr("selected"))
			{
				issue = element.text();
				if (StringUtils.isNotEmpty(issue))
				{
					issue = NumberUtil.parseIntegerFromString(issue) + "";
					// System.out.println("Issuse: " + issue);
				}
				break;
			}
		}
	}

	/**
	 * Parse the MatchList.
	 * 
	 * @param element
	 */
	protected void parseMatchList(Element element)
	{
		Elements elements = element.select("tbody tr");
		for (Element el : elements)
		{
			String t = el.attr("t");
			String lname = el.attr("m");
			String ord = el.attr("i");
			String expired = el.attr("expire");

			BdMatch match = new BdMatch();
			match.setLeaguename(lname);
			match.setMatchtime(t);
			match.setOrdinary(ord);
			match.setClosed("0".equals(expired));
			//match.setDate(getDate(t));
			//match.setIssue(issue);

			Elements els = el.select("td");
			parseMatchInfo(match, els);

			matches.add(match);
		}
	}

	/**
	 * 解析比赛信息数据
	 * 
	 * @param match
	 * @param els
	 */
	protected void parseMatchInfo(BdMatch match, Elements els)
	{
		for (Element element : els)
		{
			if (element.hasClass("wh-1"))
			{

			}
			else if (element.hasClass("wh-2"))
			{
				Element e = element.select("a").first();
				if (e != null)
				{
					String url = e.attr("href");
					String lid = getLastIdValue(url);
					match.setLid(lid);
				}
			}
			else if (element.hasClass("wh-3"))
			{
				Element el2 = element.select("span").get(1);
				String endtime = el2.html();
				match.setClosetime(endtime);
			}
			else if (element.hasClass("wh-4"))
			{
				String tn = element.attr("tn");
				match.setHomename(tn);
				Element e = element.selectFirst("a");
				if (e != null)
				{
					String url = e.attr("href");
					String tid = getLastIdValue(url);
					match.setHomeid(tid);
				}

				e = element.selectFirst(".pm");
				if (e != null)
				{
					String pm = e.text();
					pm = NumberUtil.parseFirstIntegerString(pm);
					match.setHomerank(pm);
				}
			}
			else if (element.hasClass("wh-5"))
			{

			}
			else if (element.hasClass("wh-6"))
			{
				String tn = element.attr("tn");
				match.setClientname(tn);
				Element e = element.selectFirst("a");
				if (e != null)
				{
					String url = e.attr("href");
					String tid = getLastIdValue(url);
					match.setClientid(tid);
				}

				e = element.selectFirst(".pm");
				if (e != null)
				{
					String pm = e.text();
					pm = NumberUtil.parseFirstIntegerString(pm);
					match.setClientrank(pm);
				}
			}
			else if (element.hasClass("wh-8"))
			{
				Element rq = element.selectFirst(".rq");
				String rqvalue = rq.html();
				match.setRangqiu(rqvalue);

				Elements oddslist = element.select(".bets-area a");
				String win = oddslist.get(0).text();
				String draw = oddslist.get(1).text();
				String lose = oddslist.get(2).text();
				match.setWinodds(win);
				match.setDrawodds(draw);
				match.setLoseodds(lose);
			}
			else if (element.hasClass("wh-10"))
			{
				String mid = element.attr("newplayid");
				match.setMid(mid);
			}
			else if (element.hasClass("wh-11"))
			{

			}
			else if (element.hasClass("wh-12"))
			{

			}
		}
	}

	/**
	 * Get the League ID value.
	 * 
	 * @param url
	 * @return
	 */
	private String getLastIdValue(String url)
	{
		String[] strings = url.split(RITHG_SLASH.pattern());
		if (strings.length > 0)
		{
			return strings[strings.length - 1];
		}
		else
		{
			return "";
		}
	}

	/**
	 * Get the Match Issue.
	 * 
	 * @param time
	 * @return
	 */
	protected String getDate(String time)
	{
		Date date = DateUtil.parseDate(time);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if (calendar.get(Calendar.HOUR_OF_DAY) < 12)
		{
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			return DateUtil.formatDay(calendar.getTime());
		}
		else
		{
			return DateUtil.formatDay(calendar.getTime());
		}
	}
}
