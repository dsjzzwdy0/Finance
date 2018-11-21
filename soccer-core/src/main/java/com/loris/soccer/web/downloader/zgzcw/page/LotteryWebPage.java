package com.loris.soccer.web.downloader.zgzcw.page;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.web.page.WebPage;

/**
 * 足彩信息页面
 * 
 * @author jiean
 *
 */
@TableName("soccer_web_lottery_content")
public class LotteryWebPage extends WebPage
{
	/***/
	private static final long serialVersionUID = 1L;

	protected String issue;				//期号
	protected String issuetype; 		//类型
	
	
	public String getIssue()
	{
		return issue;
	}
	public void setIssue(String issue)
	{
		this.issue = issue;
	}

	public String getIssuetype()
	{
		return issuetype;
	}
	public void setIssuetype(String issuetype)
	{
		this.issuetype = issuetype;
	}
	@Override
	public String toString()
	{
		return "LotteryWebPage [issue=" + issue + ", type=" + type + ", url=" + url
				+ ", completed=" + completed + ", loadtime=" + loadtime + ", createtime=" + createtime + ", encoding="
				+ encoding + ", method=" + method + ", httpstatus=" + httpstatus + ", tablename=" + tablename
				+ ", isNew=" + isNew + ", id=" + id + "]";
	}
}
