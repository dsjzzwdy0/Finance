package com.loris.soccer.web.downloader.zgzcw.page;

import com.baomidou.mybatisplus.annotations.TableName;

@TableName("soccer_web_yp_zhishu")
public class OddsYpZhishuWebPage extends MatchZhishuWebPage
{
	/***/
	private static final long serialVersionUID = 1L;

	@Override
	public String toString()
	{
		return "OddsYpZhishuWebPage [mid=" + mid + ", gid=" + gid + ", gname=" + gname + ", url=" + url + ", type="
				+ type + ", createtime=" + createtime + "]";
	}
}
