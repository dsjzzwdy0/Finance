package com.loris.soccer.web.downloader.zgzcw.page;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.web.WebPage;

@TableName("soccer_web_team_content")
public class TeamWebPage extends WebPage
{
	/***/
	private static final long serialVersionUID = 1L;

	private String tid;

	public String getTid()
	{
		return tid;
	}

	public void setTid(String tid)
	{
		this.tid = tid;
	}

	@Override
	public String toString()
	{
		return "TeamWebPage [tid=" + tid + ", url=" + url + ", createtime=" + createtime + "]";
	}
}
