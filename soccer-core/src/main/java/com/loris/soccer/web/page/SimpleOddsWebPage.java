package com.loris.soccer.web.page;

import com.loris.base.web.page.SimpleWebPage;

public class SimpleOddsWebPage extends SimpleWebPage
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

	@Override
	public String toString()
	{
		return "SimpleOddsWebPage [mid=" + mid + ", url=" + url + ", type=" + type + ", completed=" + completed
				+ ", loadtime=" + loadtime + ", createtime=" + createtime + ", encoding=" + encoding + ", method="
				+ method + ", httpstatus=" + httpstatus + ", id=" + id + "]";
	}
}
