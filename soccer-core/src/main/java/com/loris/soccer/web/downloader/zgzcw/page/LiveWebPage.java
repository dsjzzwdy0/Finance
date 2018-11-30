package com.loris.soccer.web.downloader.zgzcw.page;

import com.loris.base.web.page.WebPage;

public class LiveWebPage extends WebPage
{
	/***/
	private static final long serialVersionUID = 1L;

	protected String issue;	
	protected String lotteryType;

	public String getIssue()
	{
		return issue;
	}

	public void setIssue(String issue)
	{
		this.issue = issue;
	}

	public String getLotteryType()
	{
		return lotteryType;
	}

	public void setLotteryType(String lotteryType)
	{
		this.lotteryType = lotteryType;
	}

	@Override
	public String toString()
	{
		return "LiveWebPage [issue=" + issue + ", lotteryType=" + lotteryType + ", url: "  + url  + "]";
	}	
}
