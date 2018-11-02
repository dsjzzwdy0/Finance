package com.loris.stock.web.downloader.sina;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.loris.base.bean.web.WebPage;
import com.loris.base.util.DateUtil;
import com.loris.base.web.manager.Downloader;
import com.loris.stock.web.downloader.StockWebPageCreator;
import com.loris.stock.web.downloader.sina.loader.StockBaseInfoWebPageDownloader;
import com.loris.stock.web.page.StockDailyWebPage;
import com.loris.stock.web.page.StockDetailWebPage;

/**
 * 创建新浪下载数据页面。
 * 
 * @author jiean
 *
 */
public class SinaWebPageCreator extends StockWebPageCreator
{
	/** The parameter url string. */
	private String DAILY_PARAM_STR = "%22hq%22,%22hs_a%22,%22%22,0,"; // [[%22hq%22,%22hs_a%22,%22%22,0,%d,%d]]
	
	public static final String[] PAGE_URLS =
	{
		"http://market.finance.sina.com.cn/downxls.php?date=%s&symbol=%s",
		"http://money.finance.sina.com.cn/d/api/openapi_proxy.php/?__s="
	};
	
	public static final String[] PAGE_TYPES = 
	{
		StockBaseInfoWebPageDownloader.PAGE_TYPE_DETAIL,
		"daily"
	};
	
	public static String DAILY_PAGE_ENCODING = "GBK";
	
	/** 数据的编码 */
	private String encoding;
	
	/**
	 * Create a new instance of SinaWebPageCreator.
	 */
	public SinaWebPageCreator()
	{
		encoding = Downloader.ENCODING_UTF8;
	}
	
	/**
	 * 创建详细数据下载页
	 * 
	 * @param date 日期
	 * @param symbol 股票代码
	 * @return 创建的页面
	 */
	@Override
	public StockDetailWebPage createStockDetailWebPage(String date, String symbol)
	{
		int pageType = 0;
		StockDetailWebPage page = new StockDetailWebPage();
		setBasicParams(page, pageType);
		String url = String.format(PAGE_URLS[pageType], date, symbol);
		page.setUrl(url);
		page.setSymbol(symbol);
		page.setNew(true);
		page.setCompleted(false);
		page.setDay(date);
		page.setCreatetime(date);
		
		return page;
	}
	
	/**
	 * 创建投票日交易数据下载页面
	 * 
	 * @param curindex 当前页面序号
	 * @param numOfPerPage 每页下载的数据
	 * @param total 总数据量
	 * @return 页面数据
	 */
	public StockDailyWebPage createStockDailyWebPage(int curIndex, int numOfPerPage, int total)
	{
		int pageType = 1;
		StockDailyWebPage page = new StockDailyWebPage();
		this.setBasicParams(page, pageType);
		try
		{
			String url = formatURL(pageType, curIndex, numOfPerPage);
			page.setUrl(url);
		}
		catch(Exception e)
		{			
		}
		
		//page.setEncoding(DAILY_PAGE_ENCODING);		
		page.setOrdinary(curIndex);
		page.setPagenum((int)Math.ceil(total / numOfPerPage));
		page.setTotal(total);
		
		return page;
	}

	/**
	 * 基本信息，共同具有的特征
	 * 
	 * @param page
	 */
	protected void setBasicParams(WebPage page, int typeIndex)
	{
		page.setEncoding(encoding);
		page.setType(PAGE_TYPES[typeIndex]);
		page.setCreatetime(DateUtil.getCurDayStr());
	}
	
	/**
	 * Format the URL string value.
	 * 
	 * @param currentPage
	 * @param numOfPerPage
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	protected String formatURL(int pageType, int currentPage, int numOfPerPage) throws UnsupportedEncodingException
	{
		String url = DAILY_PARAM_STR + currentPage + "," + numOfPerPage; 
		// String.format(paramStr, currentPage, numOfPerPage);
		// String url =
		// "http://money.finance.sina.com.cn/d/api/openapi_proxy.php/?__s=";
		url = PAGE_URLS[pageType] + URLEncoder.encode("[[", "utf-8") + url + URLEncoder.encode("]]", "utf-8");
		return url;
	}
}
