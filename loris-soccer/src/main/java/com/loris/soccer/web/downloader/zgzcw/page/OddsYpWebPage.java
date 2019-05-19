package com.loris.soccer.web.downloader.zgzcw.page;

import com.baomidou.mybatisplus.annotations.TableName;

@TableName("soccer_web_yp_content")
public class OddsYpWebPage extends MatchWebPage
{
	/***/
	private static final long serialVersionUID = 1L;

	
	@Override
	public String toString()
	{
		return "YpWebPage [mid=" + mid + ", url=" + url + ", type=" + type + ", completed="
				+ completed + ", loadtime=" + loadtime + ", createtime=" + createtime + ", encoding=" + encoding
				+ ", method=" + method + ", httpstatus=" + httpstatus + ", tablename=" + tablename + ", isNew=" + isNew
				+ ", id=" + id + "]";
	}
}
