package com.loris.soccer.web.downloader.eastsoccer;

import com.loris.base.util.DateUtil;
import com.loris.base.web.manager.Downloader;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.web.downloader.eastsoccer.page.EastSoccerWebPage;

public class EastSoccerWebPageCreator
{
	public static String host = "http://106.12.205.220/";
	
	/** 通用的页面编码 */
	protected static String encoding = Downloader.ENCODING_UTF8;
	
	public static String PAGE_TYPES[] = {
		"op"
	};
	
	/**
	 * 创建数据页面
	 * @param mid
	 * @return
	 */
	public static WebPage createMatchOddsPage(String mid)
	{
		int pageTye = 0;
		EastSoccerWebPage page = new EastSoccerWebPage();
		setBasicParams(page, pageTye);
		String url = host + "loris/soccerdata/getMatchOps?mid=" + mid;
		page.setUrl(url);
		return page;
	}
	
	
	
	/**
	 * 基本信息，共同具有的特征
	 * 
	 * @param page
	 */
	protected static void setBasicParams(WebPage page, int typeIndex)
	{
		page.setEncoding(encoding);
		page.setType(PAGE_TYPES[typeIndex]);
		page.setCreatetime(DateUtil.getCurTimeStr());
	}
}
