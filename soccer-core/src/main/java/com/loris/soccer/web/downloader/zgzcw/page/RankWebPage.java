package com.loris.soccer.web.downloader.zgzcw.page;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.web.page.WebPage;

@TableName("soccer_web_rank_content")
public class RankWebPage extends WebPage
{
	/***/
	private static final long serialVersionUID = 1L;

	private String lid;					//联赛编号

	public String getLid()
	{
		return lid;
	}

	public void setLid(String lid)
	{
		this.lid = lid;
	}
}
