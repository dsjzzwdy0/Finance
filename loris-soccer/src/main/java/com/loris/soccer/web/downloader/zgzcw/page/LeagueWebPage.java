package com.loris.soccer.web.downloader.zgzcw.page;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.util.StringUtil;
import com.loris.base.web.page.WebPage;

@TableName("soccer_web_league_content")
public class LeagueWebPage extends WebPage
{
	/** serial version uid. */
	private static final long serialVersionUID = 1L;

	protected String lid;
	protected String leaguetype;
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
		if(StringUtil.isNullOrEmpty(season))
		{
			season = getSeasonInfo();
		}
		return season;
	}

	public void setSeason(String season)
	{
		this.season = season;
	}
	
	public String getLeaguetype()
	{
		return leaguetype;
	}

	public void setLeaguetype(String leaguetype)
	{
		this.leaguetype = leaguetype;
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
			if (!StringUtil.isNullOrEmpty(strings[i]))
			{
				return strings[i];
			}
		}
		return "2017-2018";
	}

	@Override
	public String toString()
	{
		return "LeagueWebPage [lid=" + lid + ", id=" + id + "]";
	}
}
