package com.loris.stock.web.downloader.sina.loader;

import java.util.Date;

import org.apache.log4j.Logger;

import com.loris.base.util.DateUtil;
import com.loris.base.web.page.WebPage;
import com.loris.stock.bean.data.table.StockInfo;
import com.loris.stock.web.page.StockBaseInfoWebPage;

public class StockBaseInfoWebPageDownloader extends StockInfoWebPageDownloader
{
	private static Logger log = Logger.getLogger(StockBaseInfoWebPageDownloader.class);

	/**
	 * Create a new instance of StockBaseInfoWebPageManager
	 */
	public StockBaseInfoWebPageDownloader()
	{
		super();
		setBaseURL(URL_BASE_INFO_PAGE);
		setEncoding(ENCODING_GB2312);
		setType(PAGE_TYPE_BASE_INFO);
		setTablename(TABLE_NAME_BASIC_INFO);
	}

	/**
	 * Format the URL
	 * 
	 * @param base
	 * @param stock
	 * @return
	 */
	public String formatURL(String base, StockInfo stock)
	{
		return String.format(base, stock.getSymbol());
	}

	/**
	 * Prepare for the StockWebPageManager.
	 */
	@Override
	public boolean prepare()
	{
		log.info("Prepare StockInfoWebManager ");

		super.prepare();
		if (stocks == null)
		{
			// There is nothing to download.
			return false;
		}

		String day = DateUtil.DAY_FORMAT.format(new Date());

		for (StockInfo stock : stocks)
		{
			String url = formatURL(baseURL, stock);
			StockBaseInfoWebPage page = new StockBaseInfoWebPage();
			page.setNew(true);
			page.setCompleted(false);
			page.setUrl(url);
			page.setTablename(tablename);
			page.setCreatetime(day);
			page.setSymbol(stock.getSymbol());
			page.setType(type);
			page.setEncoding(encoding);

			// Add this to be download.
			addPage(page);

		}
		return true;
	}

	/**
	 * To String.
	 */
	@Override
	public String toString()
	{
		return "StockBaseInfoWebPageManager [baseURLs=" + baseURL + ", types=" + type + ", stocks=" + stocks.size()
				+ ", baseURL=" + baseURL + ", name=" + name + ", tableName=" + tablename + ", type=" + type
				+ ", enable=" + enable + ", encoding=" + encoding + ", pages=" + pages + ", interval=" + interval + "]";
	}

	@Override
	public void afterDownload(WebPage page, boolean flag)
	{
		page.setCreatetime(DateUtil.DAY_FORMAT.format(new Date()));
		synchronized (stockManager)
		{
			stockManager.updateOrAddBaseInfoWebPage((StockBaseInfoWebPage) page);
		}
		
		super.afterDownload(page, flag);
	}

	/**
	 * Format URL.
	 */
	@Override
	public String formatURL(StockInfo stock, String... params)
	{
		return "";
	}
}
