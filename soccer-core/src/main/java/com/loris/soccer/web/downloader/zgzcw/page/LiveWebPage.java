package com.loris.soccer.web.downloader.zgzcw.page;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.web.page.WebPage;

@TableName("soccer_web_live_page")
public class LiveWebPage extends WebPage
{
	/***/
	private static final long serialVersionUID = 1L;

	protected String issue;	
	protected String lotterytype;

	public String getIssue()
	{
		return issue;
	}

	public void setIssue(String issue)
	{
		this.issue = issue;
	}

	
	public String getLotterytype()
	{
		return lotterytype;
	}

	public void setLotterytype(String lotterytype)
	{
		this.lotterytype = lotterytype;
	}

	@Override
	public String toString()
	{
		return "LiveWebPage [issue=" + issue + ", lotteryType=" + lotterytype + ", url: "  + url  + "]";
	}	
}
