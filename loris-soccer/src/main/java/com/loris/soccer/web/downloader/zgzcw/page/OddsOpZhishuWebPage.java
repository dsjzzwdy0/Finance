package com.loris.soccer.web.downloader.zgzcw.page;

import com.baomidou.mybatisplus.annotations.TableName;

@TableName("soccer_web_op_zhishu")
public class OddsOpZhishuWebPage extends MatchZhishuWebPage
{
	/***/
	private static final long serialVersionUID = 1L;

	@Override
	public String toString()
	{
		return "OddsOpZhishuWebPage [gid=" + gid + ", gname=" + gname + ", mid=" + mid + ", url=" + url
				+ ", createtime=" + createtime + "]";
	}
}
