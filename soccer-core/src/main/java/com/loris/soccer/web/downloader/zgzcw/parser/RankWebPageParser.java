package com.loris.soccer.web.downloader.zgzcw.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.DateUtil;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.data.table.league.Rank;
import com.loris.soccer.web.downloader.zgzcw.page.RankWebPage;

public class RankWebPageParser extends SeasonWebPageParser
{
	/** 球队排名 */
	protected List<Rank> ranks = new ArrayList<>();

	/** 联赛编号 */
	protected String lid;

	/** 轮次 */
	protected String round;

	/**
	 * 解析数据下载
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The WebPage is not completed or Content is null. ");
		}

		if (!(page instanceof RankWebPage))
		{
			throw new IllegalArgumentException("The WebPage is not a validate RankWebPage. ");
		}

		lid = ((RankWebPage) page).getLid();

		Document doc = parseHtml(page);
		Element element = doc.select(".league .left .team_out .div-select").first();
		// 解析赛季信息
		parseSeasons(element);

		if (StringUtils.isEmpty(seasonInfo))
		{
			if (seasons.size() > 0)
			{
				seasonInfo = seasons.get(0).getSeason();
			}
		}

		// 球队的排名情况
		Elements elements = doc.select(".league .league_right #hideList .table_out .tabs1_main_ul");
		String type = SoccerConstants.RANK_TOTAL;
		parseRanks(type, elements.get(0));
		type = SoccerConstants.RANK_HOME;
		parseRanks(type, elements.get(1));
		type = SoccerConstants.RANK_CLIENT;
		parseRanks(type, elements.get(2));
		type = SoccerConstants.RANK_LATEST_6;
		parseRanks(type, elements.get(3));
		type = SoccerConstants.RANK_FIRST_HALF;
		parseRanks(type, elements.get(4));
		type = SoccerConstants.RANK_SECOND_HALF;
		parseRanks(type, elements.get(5));

		//设置当前排名的轮次
		setRoundInfo();
		
		return true;
	}
	
	/**
	 * 设置轮次信息
	 */
	protected void setRoundInfo()
	{
		int maxGameNum = 0;
		for (Rank rank : ranks)
		{
			if(rank.getGamenum() > maxGameNum)
			{
				maxGameNum = rank.getGamenum();
			}
		}
		round = Integer.toString(maxGameNum);
		
		for (Rank rank : ranks)
		{
			rank.setRound(round);
		}
	}

	/**
	 * 解析当前的比赛排名记录
	 * 
	 * @param lid
	 * @param type
	 * @param rankElement
	 */
	protected void parseRanks(String type, Element rankElement)
	{
		Elements rankRecords = rankElement.select(".zstab tbody tr");

		String updatetime = DateUtil.getCurTimeStr();

		for (Element element : rankRecords)
		{
			Rank rank = new Rank();
			rank.setLid(lid);
			rank.setType(type);
			// rank.setLeaguename(leaguename);
			rank.setUpdatetime(updatetime);

			parseRank(rank, element);

			// 加入列表
			ranks.add(rank);
		}
	}

	/**
	 * 解析排名记录
	 * 
	 * @param rank
	 * @param element
	 */
	protected void parseRank(Rank rank, Element element)
	{
		Elements recs = element.select("td");
		String no = recs.get(0).text();

		Element nameEl = recs.get(1).selectFirst("a");
		String name = nameEl.text();
		String tid = getTeamId(nameEl.attr("href"));

		String num = recs.get(2).text();
		String winnum = recs.get(3).text();
		String drawnum = recs.get(4).text();
		String losenum = recs.get(5).text();
		String goal = recs.get(6).text();
		String losegoal = recs.get(7).text();
		String diffgogal = recs.get(8).text();
		/*
		 * String avggoal = recs.get(9).text(); String avglosegoal =
		 * recs.get(10).text(); String winprob = recs.get(11).text(); String
		 * drawprob = recs.get(12).text(); String loseprob =
		 * recs.get(13).text();
		 */
		String score = recs.get(14).text();

		rank.setTid(tid);
		rank.setName(name);
		rank.setSeason(seasonInfo);
		rank.setRank(NumberUtil.parseInt(no));
		rank.setGamenum(NumberUtil.parseInt(num));
		rank.setWinnum(NumberUtil.parseInt(winnum));
		rank.setDrawnum(NumberUtil.parseInt(drawnum));
		rank.setLosenum(NumberUtil.parseInt(losenum));
		rank.setGoal(NumberUtil.parseInt(goal));
		rank.setLosegoal(NumberUtil.parseInt(losegoal));
		rank.setDiffgoal(NumberUtil.parseInt(diffgogal));
		rank.setScore(NumberUtil.parseInt(score));

	}

	/**
	 * Get the TeamId value.
	 * 
	 * @param url
	 * @return
	 */
	protected String getTeamId(String url)
	{
		String[] values = url.split(RITHG_SLASH.pattern());
		int size = values.length;
		String tid = values[size - 1];
		return tid;
	}

	/**
	 * Get the Ranks.
	 * 
	 * @return
	 */
	public List<Rank> getRanks()
	{
		return ranks;
	}
	
	/**
	 * Get the Round.
	 * 
	 * @return
	 */
	public String getRound()
	{
		return round;
	}
}
