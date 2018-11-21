package com.loris.soccer.web.downloader.zgzcw.page;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.web.page.WebPage;
import com.loris.base.web.util.URLUtil;
import com.loris.soccer.bean.data.table.league.Round;

/**
 * 联赛每轮比赛数据页面
 * 
 * @author jiean
 *
 */
@TableName("soccer_web_round_league_content")
public class RoundLeagueWebPage extends WebPage
{
	/***/
	private static final long serialVersionUID = 1L;
	
	protected String racetype;             //类型：联赛league、杯赛cup
	protected String lid;
	protected String season;
	protected String round;                //轮次
	
	@TableField(exist=false)
	private Round roundInfo;
	
	@TableField(exist=false)
	protected Map<String, String> headers = new HashMap<>();
	
	/**
	 * Create a new instance of LeagueRoundWebPage
	 */
	public RoundLeagueWebPage()
	{
		setMethod(HTTP_METHOD_POST);
		this.racetype = "league";
	}

	/**
	 * Get the params.
	 * 
	 * @return
	 */
	@Override
	public Map<String, String> getParams()
	{
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("source_league_id", getLid());
		params.put("currentRound", getRound());
		params.put("season", getSeason());
		params.put("seasonType", "");
		return params;
	}
	
	/**
	 * Get the headers parameters.
	 * 
	 * @return Headers value.
	 */
	@Override
	public Map<String, String> getHeaders()
	{
		String baseUrl = "http://saishi.zgzcw.com";
		String host = URLUtil.getHost(baseUrl);
		String referer = baseUrl + "/soccer/league/" + getLid() + "/" + getSeason();
		headers.put("Host", host);
		headers.put("Origin", baseUrl);
		headers.put("Referer", referer);
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		return headers;
	}
	
	/**
	 * Get the Parent URL value.
	 * @return The URL value.
	 */
	public String getParentURL()
	{
		String baseUrl = "http://saishi.zgzcw.com";
		return baseUrl + "/soccer/league/" + getLid() + "/" + getSeason();
	}
	
	public String getRacetype()
	{
		return racetype;
	}

	public void setRacetype(String racetype)
	{
		this.racetype = racetype;
	}

	public String getLid()
	{
		return lid;
	}

	public void setLid(String lid)
	{
		this.lid = lid;
	}

	public String getSeason()
	{
		return season;
	}

	public void setSeason(String season)
	{
		this.season = season;
	}

	public Round getRoundInfo()
	{
		return roundInfo;
	}
	public void setRoundInfo(Round roundInfo)
	{
		this.roundInfo = roundInfo;
	}
	
	public String getRound()
	{
		return round;
	}
	public void setRound(String round)
	{
		this.round = round;
	}
	
	@Override
	public String toString()
	{
		return "RoundLeagueWebPage [round=" + round + ", racetype=" + racetype + ", roundInfo=" + roundInfo + ", lid=" + lid
				+ ", season=" + season + ", url=" + url + "]";
	}
}
