package com.loris.stock.web.downloader;

import com.loris.stock.web.page.StockDailyWebPage;
import com.loris.stock.web.page.StockDetailWebPage;

public abstract class StockWebPageCreator
{
	
	/**
	 * 创建详细数据下载页
	 * 
	 * @param date 日期
	 * @param symbol 股票代码
	 * @return 创建的页面
	 */
	public abstract StockDetailWebPage createStockDetailWebPage(String date, String symbol);
	
	/**
	 * 创建投票日交易数据下载页面
	 * 
	 * @param curindex 当前页面序号
	 * @param numOfPerPage 每页下载的数据
	 * @param total 总数据量
	 * @return 页面数据
	 */
	public abstract StockDailyWebPage createStockDailyWebPage(int curindex, int numOfPerPage, int total);
}
