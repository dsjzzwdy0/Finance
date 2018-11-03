package com.loris.soccer.web.downloader.zgzcw.page;

import com.loris.base.bean.web.WebPage;

/**
 * 足彩、竞彩、北单计划日历表
 * 
 * @author jiean
 *
 */
public class LotteryCalendarWebPage extends WebPage
{
	/***/
	private static final long serialVersionUID = 1L;

	private String startdate;
	private int num;
	
	public String getStartdate()
	{
		return startdate;
	}
	public void setStartdate(String startdate)
	{
		this.startdate = startdate;
	}
	public int getNum()
	{
		return num;
	}
	public void setNum(int num)
	{
		this.num = num;
	}
	@Override
	public String toString()
	{
		return "LotteryCalendarWebPage [startdate=" + startdate + ", num=" + num + "]";
	}
}
