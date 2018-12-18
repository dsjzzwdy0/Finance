package com.loris.soccer.web.downloader.zgzcw.page;

import com.baomidou.mybatisplus.annotations.TableName;

@TableName("soccer_web_history_content")
public class MatchHistoryWebPage extends MatchWebPage
{
	/***/
	private static final long serialVersionUID = 1L;
	
	private String homeid;
	private String clientid;

	public String getHomeid()
	{
		return homeid;
	}
	public void setHomeid(String homeid)
	{
		this.homeid = homeid;
	}
	public String getClientid()
	{
		return clientid;
	}
	public void setClientid(String clientid)
	{
		this.clientid = clientid;
	}
	@Override
	public String toString()
	{
		return "MatchHistoryWebPage [mid=" + mid + ", homeid=" + homeid + ", clientid=" + clientid + ", url=" + url
		        + ", type=" + type + "]";
	}
}
