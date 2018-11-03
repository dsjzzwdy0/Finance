package com.loris.soccer.web.downloader.zgzcw.page;

import com.loris.base.bean.web.WebPage;

/**
 * 比赛的数据页面
 * 
 * @author Administrator
 *
 */
public class MatchWebPage extends WebPage
{
	/***/
	private static final long serialVersionUID = 1L;
	
	protected String mid;

	public String getMid()
	{
		return mid;
	}

	public void setMid(String mid)
	{
		this.mid = mid;
	}
}
