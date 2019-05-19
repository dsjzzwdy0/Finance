package com.loris.soccer.web.downloader.okooo.page;

import java.util.HashMap;

import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 在某些数据下载的时候，需要使用特殊的请求头信息
 * @author dsj
 *
 */
@TableName("soccer_web_okooo_child")
public class OkoooRequestHeaderWebPage extends OkoooWebPage
{
	private static final long serialVersionUID = 1L;
	
	/** 第几页数据*/
	protected int pageindex;
	
	public OkoooRequestHeaderWebPage()
	{
		hasMoreHeader = true;
		headers = new HashMap<>();
	}
	
	public int getPageindex()
	{
		return pageindex;
	}

	public void setPageindex(int pageindex)
	{
		this.pageindex = pageindex;
	}
}
