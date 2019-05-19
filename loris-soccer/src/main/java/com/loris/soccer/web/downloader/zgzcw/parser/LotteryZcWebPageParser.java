package com.loris.soccer.web.downloader.zgzcw.parser;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.WebPageParser;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.table.ZcMatch;
import com.loris.soccer.web.downloader.zgzcw.page.LotteryWebPage;

/**
 * 解析足彩数据页面
 * 
 * @author jiean
 *
 */
public class LotteryZcWebPageParser implements WebPageParser
{
	public static final String RANK_SPLITTER = "\\|";  //5|-|-
	
	/** 足彩比赛列表*/
	private List<ZcMatch> zcMatchs = new ArrayList<>();
	
	/** The issue value. */
	private String issue;
	
	/**
	 * Get the ZcMatches.
	 * 
	 * @return
	 */
	public List<ZcMatch> getMatches()
	{
		return zcMatchs;
	}
	
	/**
	 * 解析竞彩足球数据
	 * 
	 * @param page 页面数据
	 * @return 返回是否解析成功
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
		
		String content = page.getContent();
		if (StringUtils.isEmpty(content))
		{
			return false;
		}
		
		issue = ((LotteryWebPage)page).getIssue();
		
		JSONObject jsonObject = (JSONObject)JSON.parse(content);
		
		if(jsonObject == null)
		{
			return false;
		}
		for (String key : jsonObject.keySet())
		{
			if("matchInfo".equalsIgnoreCase(key))
			{
				JSONArray array = (JSONArray)jsonObject.get(key);
				parseMatchInfos(array);
			}
		}
		
		return true;
	}
	
	/**
	 * 解析足彩数据
	 * 
	 * @param matches
	 */
	protected void parseMatchInfos(JSONArray matches)
	{
		int index = 1;
		for (Object object : matches)
		{
			parseMatchInfo((JSONObject)object, index);
			index ++;
		}
	}
	
	/**
	 * 解析足彩竞赛数据
	 * 
	 * @param object
	 */
	protected void parseMatchInfo(JSONObject object, int index)
	{
		ZcMatch zcMatch = new ZcMatch();
		Object value = null;
		for (String key : object.keySet())
		{
			value = object.get(key);
			switch (key) {
			case "hostName":
				zcMatch.setHomename(trimAll(value.toString()));
				break;
			case "hostId":
				zcMatch.setHomeid(value.toString());
				break;
			case "homePh":
				zcMatch.setHomerank(getRank(value.toString()));
				break;
			case "kj_time":
				zcMatch.setKjtime(value.toString());
				break;
			case "lotteryEndDate":
				zcMatch.setClosetime(value.toString());
				break;
			case "playId":
				zcMatch.setMid(value.toString());
				break;
			case "gameStartDate":
				zcMatch.setMatchtime(value.toString());
				break;
			case "legageId":
				zcMatch.setLid(value.toString());
				break;
			case "leageName":
				zcMatch.setLeaguename(trimAll(value.toString()));
				break;
			case "leageType":
				zcMatch.setLeaguetype(getLeagueType(value.toString()));
				break;
			case "guestId":
				zcMatch.setClientid(value.toString());
				break;
			case "guestName":
				zcMatch.setClientname(trimAll(value.toString()));
				break;
			case "guestPh":
				zcMatch.setClientrank(getRank(value.toString()));
				break;
			default:
				break;
			}
		}		
		zcMatch.setIssue(issue);
		zcMatch.setOrdinary(Integer.toString(index));
		zcMatchs.add(zcMatch);
		
		//System.out.println(zcMatch);
	}
	
	/**
	 * 去掉所有的空格
	 * 
	 * @param value
	 * @return
	 */
	protected String trimAll(String value)
	{
		return value.replaceAll("　", "");
	}

	/**
	 * 
	 * @param pm
	 * @return
	 */
	protected String getRank(String pm)
	{
		String[] strings = pm.split(RANK_SPLITTER);
		return strings[0];		
	}
	
	/**
	 * Get the LeagueType.
	 * 
	 * @param value
	 * @return
	 */
	protected String getLeagueType(String value)
	{
		if("1".equals(value))
		{
			return SoccerConstants.MATCH_TYPE_LEAGUE;
		}
		else
		{
			return SoccerConstants.MATCH_TYPE_CUP;
		}
	}
}
