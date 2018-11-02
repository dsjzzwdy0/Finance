package com.loris.soccer.web.downloader.okooo.page;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.web.WebPage;

/**
 * 澳客网的数据下载页面
 * 
 * @author jiean
 *
 */
@TableName("soccer_web_okooo_content")
public class OkoooWebPage extends WebPage
{
	/***/
	private static final long serialVersionUID = 1L;
	
	protected String issue;
	protected String mid;
	protected String gid;
	
	@TableField(exist=false)
	protected String domain;

	public String getDomain()
	{
		return domain;
	}

	public void setDomain(String domain)
	{
		this.domain = domain;
	}

	public String getIssue()
	{
		return issue;
	}

	public void setIssue(String issue)
	{
		this.issue = issue;
	}

	public String getMid()
	{
		return mid;
	}

	public void setMid(String mid)
	{
		this.mid = mid;
	}

	public String getGid()
	{
		return gid;
	}

	public void setGid(String gid)
	{
		this.gid = gid;
	}

	@Override
	public String toString()
	{
		return "OkoooWebPage [domain=" + domain + ", url=" + url + ", type=" + type + ", completed=" + completed
				+ ", createtime=" + createtime + "]";
	}
}
