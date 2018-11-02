package com.loris.soccer.web.downloader.zgzcw.page;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.web.WebPage;

@TableName("soccer_web_round_cup_content")
public class RoundCupWebPage extends WebPage
{
	/***/
	private static final long serialVersionUID = 1L;

	protected String racetype;             //类型：联赛league、杯赛cup
	protected String lid;
	protected String season;
	
	/**
	 * Create a new instance of RoundCupWebPage.
	 */
	public RoundCupWebPage()
	{
		this.racetype = "cup";
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


	public String getRacetype()
	{
		return racetype;
	}
	public void setRacetype(String racetype)
	{
		this.racetype = racetype;
	}


	@Override
	public String toString()
	{
		return "RoundCupWebPage [racetype=" + racetype + ", lid=" + lid + ", season=" + season + ", url=" + url
				+ ", type=" + type + ", completed=" + completed + ", loadtime=" + loadtime
				+ ", createtime=" + createtime + ", encoding=" + encoding + ", method=" + method + ", httpstatus="
				+ httpstatus + ", tablename=" + tablename + ", isNew=" + isNew
				+ ", id=" + id + "]";
	}	
		
}
