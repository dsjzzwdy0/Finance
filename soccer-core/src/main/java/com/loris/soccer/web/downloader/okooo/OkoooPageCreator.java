package com.loris.soccer.web.downloader.okooo;

import com.loris.base.util.DateUtil;
import com.loris.base.web.manager.Downloader;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.web.downloader.okooo.page.OkoooRequestHeaderWebPage;
import com.loris.soccer.web.downloader.okooo.page.OkoooWebPage;

public class OkoooPageCreator
{
	/** The Header Referer name. */
	public static final String OKOOO_HEADER_REFERER = "Referer";
	
	
	public static final String[] PAGE_URLS = {
		"http://www.okooo.com/jingcai/shengpingfu/",
		"http://www.okooo.com/soccer/match/",
		"http://www.okooo.com/soccer/match/",
		"http://www.okooo.com/soccer/match/",
		"http://www.okooo.com/soccer/match/",
		"http://www.okooo.com/soccer/match/", //1023935/ah/ajax/?page=2&trnum=60&companytype=BaijiaBooks
		"http://www.okooo.com/danchang/",
	};
	
	public static final String[] PAGE_TYPES = {
		"jc",
		"op",
		"yp",
		"opchange",
		"ypchange",
		"yppage",
		"bd"
	};
	
	
	protected static String encoding = Downloader.ENCODING_UTF8;
	
	/**
	 * Create a new instance of OkoooWebPageCreator.
	 */
	public OkoooPageCreator()
	{
		encoding = Downloader.ENCODING_UTF8;
	}
	
	/**
	 * 创建主页面
	 * 
	 * @return 页面
	 */
	public static OkoooWebPage createJcWebPage()
	{
		int typeIndex = 0;
		OkoooWebPage page = new OkoooWebPage();
		setBasicParams(page, typeIndex);
		page.setUrl(PAGE_URLS[typeIndex]);
		page.setIssue(DateUtil.getCurDayStr());

		return page;
	}
	
	/**
	 * 创建主页面
	 * 
	 * @return
	 */
	public static OkoooWebPage createBaseWebPage()
	{
		return createBdWebPage();
	}
	
	/**
	 * 创建北单主页
	 * @return 北单主页
	 */
	public static OkoooWebPage createBdWebPage()
	{
		int typeIndex = 6;
		OkoooWebPage page = new OkoooWebPage();
		setBasicParams(page, typeIndex);
		page.setUrl(PAGE_URLS[typeIndex]);
		page.setIssue(DateUtil.getCurDayStr());

		return page;
	}
	
	/**
	 * 创建亚盘数据下载页面
	 * 
	 * @param mid 比赛编号
	 * @return 返回页面
	 */
	public static OkoooWebPage createYpWebPage(String mid)
	{
		int typeIndex = 2;
		OkoooWebPage page = new OkoooWebPage();
		setBasicParams(page, typeIndex);
		page.setUrl(PAGE_URLS[typeIndex] + mid + "/ah/");
		page.setMid(mid);
		
		page.setHasMoreHeader(true);
		page.addHeader(OKOOO_HEADER_REFERER, PAGE_URLS[typeIndex] + mid + "/odds/");
		
		return page;
	}
	
	/**
	 * 创建澳客欧赔数据下载页面
	 * @param mid
	 * @return
	 */
	public static OkoooWebPage createOpWebPage(String mid)
	{
		int typeIndex = 1;
		OkoooWebPage page = new OkoooWebPage();
		setBasicParams(page, typeIndex);
		page.setUrl(PAGE_URLS[typeIndex] + mid + "/odds");
		page.setMid(mid);
		page.setHasMoreHeader(true);
		page.addHeader(OKOOO_HEADER_REFERER, "http://www.okooo.com/danchang/");
		return page;
	}
	
	/**
	 * 创建亚盘数据详细下载页面
	 * @param mid 比赛编号
	 * @param gid 公司编号
	 * @return 返回页面
	 */
	public static OkoooWebPage createYpChangeWebPage(String mid, String gid)
	{
		int typeIndex = 4;
		OkoooWebPage page = new OkoooWebPage();
		setBasicParams(page, typeIndex);
		page.setMid(mid);
		page.setGid(gid);
		String baseURL = getBaseYpURL(typeIndex, mid);
		page.setUrl(baseURL + "change/" + gid + "/");
		page.setHasMoreHeader(true);
		page.addHeader(OKOOO_HEADER_REFERER, baseURL);
		return page;
	}
	
	/**
	 * 创建OKOOO亚盘主要盘面数据页面
	 * @param mid 比赛编号
	 * @param pageIndex 页面序号
	 * @return 创建的页面
	 */
	public static OkoooRequestHeaderWebPage createYpPageWebPage(String mid, int pageIndex)
	{
		int typeIndex = 5;
		
		int trnum = pageIndex * 30;
		OkoooRequestHeaderWebPage page = new OkoooRequestHeaderWebPage();
		setBasicParams(page, typeIndex);
		page.setMid(mid);
		String baseURL = getBaseYpURL(typeIndex, mid);
		String url = baseURL + "ajax/?page=" + pageIndex 
				+ "&trnum=" + trnum + "&companytype=BaijiaBooks";
		page.setUrl(url);
		page.addHeader(OKOOO_HEADER_REFERER, baseURL);				
		return page;
	}
	
	/**
	 * 
	 * @param mid
	 * @param pageIndex
	 * @return
	 */
	public static OkoooRequestHeaderWebPage createOpPageWebPage(String mid, int pageIndex)
	{
		int typeIndex = 5;
		int trnum = pageIndex * 30;
		OkoooRequestHeaderWebPage page = new OkoooRequestHeaderWebPage();
		setBasicParams(page, typeIndex);
		page.setMid(mid);
		String baseURL = getBaseOpURL(typeIndex, mid);
		String url = baseURL + "ajax/?page=" + pageIndex 
				+ "&trnum=" + trnum + "&companytype=BaijiaBooks&type=1";
		page.setUrl(url);
		page.addHeader(OKOOO_HEADER_REFERER, baseURL);				
		return page;
		//ajax/?page=1&trnum=30&companytype=BaijiaBooks&type=1
	}
	
	
	/**
	 * 创建亚盘基础网络URL地址
	 * @param typeIndex 地址类型
	 * @param mid 比赛编号
	 * @return 基础网络地址
	 */
	protected static String getBaseYpURL(int typeIndex, String mid)
	{
		return PAGE_URLS[typeIndex] + mid + "/ah/";
	}
	
	/**
	 * 创建欧盘基础网络URL地址
	 * @param typeIndex 地址类型
	 * @param mid 比赛编号
	 * @return 基础网络地址
	 */
	protected static String getBaseOpURL(int typeIndex, String mid)
	{
		return PAGE_URLS[typeIndex] + mid + "/odds/";
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
