package com.loris.soccer.web.downloader.zgzcw.page;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.web.WebPage;

/**
 * 联赛信息页面
 * 
 * @author jiean
 *
 */
@TableName("soccer_web_season_content")
public class SeasonWebPage extends WebPage
{
	/***/
	private static final long serialVersionUID = 1L;
	
	protected String lid;
	protected String season;

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
		/*
		if(StringUtils.isEmpty(season))
		{
			season = getSeasonInfo();
		}*/
		return season;
	}

	public void setSeason(String season)
	{
		this.season = season;
	}
	
	/**
	 * Get the season info.
	 * 
	 * @param url
	 * @return
	 */
	public String getSeasonInfo()
	{
		String[] strings = url.split("/");
		int size = strings.length;
		for (int i = size - 1; i >= 0; i--)
		{
			if (!StringUtils.isEmpty(strings[i]))
			{
				return strings[i];
			}
		}
		return "2017-2018";
	}

	@Override
	public String toString()
	{
		return "SeasonWebPage [lid=" + lid + ", season=" + season + ", url=" + url + ", type=" + type + ", createtime="
				+ createtime + "]";
	}
}
