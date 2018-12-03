package com.loris.soccer.web.downloader.zgzcw.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.bean.data.table.Match;
import com.loris.soccer.bean.data.table.Round;
import com.loris.soccer.web.downloader.zgzcw.page.RoundCupWebPage;

/**
 * 解析杯赛比赛信息页面
 * 
 * @author jiean
 *
 */
public class RoundCupWebPageParser extends SeasonWebPageParser
{
	/** The matches. */
	private List<Match> matches = new ArrayList<Match>();

	/**
	 * Parse the web page.
	 * 
	 * @return if parse the correct page.
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The WebPage is not completed or Content is null. ");
		}

		if (!(page instanceof RoundCupWebPage))
		{
			throw new IllegalArgumentException("The WebPage is not a validate RoundCupWebPage. ");
		}

		lid = ((RoundCupWebPage) page).getLid();

		Document doc = parseHtml(page);
		Element element = doc.select(".league .left .team_out .div-select").first();

		// 解析赛季信息
		parseSeasons(element);

		// 赛季信息
		if (StringUtils.isEmpty(seasonInfo))
		{
			if (seasons.size() > 0)
			{
				seasonInfo = seasons.get(0).getSeason();
			}
		}

		// 解析球队信息
		Elements elements = doc.select(".league .left .tongji_list");
		for (Element element2 : elements)
		{
			element = element2.select(">div").first();
			String name = element.text();
			// System.out.println("Parse the Teams: " + name);
			if ("球队列表".equals(name))
			{
				// 解析球队信息
				parseTeams(element2);
			}
		}

		// 解析比赛轮次信息
		element = doc.select(".league .league_right .table_head #tabs9").first();
		parseRound(element);

		// System.out.println(rounds.size() + ": " + rounds);

		// 解析比赛信息
		elements = doc.select(".league .league_right .cupBlock01 .cup .tabs9_main1");
		// System.out.println("Matches Elements Size: " + elements.size());
		parseMatches(elements);

		return true;
	}

	/**
	 * Parse the round info.
	 * 
	 * @param element
	 */
	protected void parseRound(Element element)
	{
		if (element == null)
		{
			return;
		}
		String name;
		String rid;
		Elements elements = element.select("li");
		for (Element element2 : elements)
		{
			rid = element2.attr("id");
			name = element2.text();
			addRound(name, rid);
		}
	}

	/**
	 * 解析比赛信息
	 * 
	 * @param elements
	 */
	protected void parseMatches(Elements elements)
	{
		int size = elements.size();
		// System.out.println("Matches Elements Size: " + size);
		for (int i = 0; i < size; i++)
		{
			Round round = rounds.get(i);			
			Element element = elements.get(i);
			Elements elements2 = element.select(".zstab");
			for (Element element2 : elements2)
			{
				parseMatches(round, element2);
			}
		}
	}

	/**
	 * 按照轮次解析比赛信息
	 * 
	 * @param round
	 * @param element
	 */
	protected void parseMatches(Round round, Element element)
	{
		String mid;
		String time;
		String hteam;
		String cteam;
		String score;
		String halfscore;
		String handicap;

		Elements elements = element.select("tbody tr");
		for (Element el : elements)
		{
			Elements elements2 = el.select("td");
			if (elements2.size() != 7)
			{
				continue;
			}
			time = elements2.get(0).text();
			hteam = getTeamid(elements2.get(1));
			score = elements2.get(2).text();
			cteam = getTeamid(elements2.get(3));
			halfscore = elements2.get(4).text();
			handicap = elements2.get(5).text();
			mid = getMid(elements2.get(6));

			if (StringUtils.isEmpty(mid))
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
			match.setRound(round.getName());
			match.setSeason(seasonInfo);
			match.setLid(lid);
			
			round.setMatchTime(time);
			
			matches.add(match);
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
		if (element2 == null)
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
}
