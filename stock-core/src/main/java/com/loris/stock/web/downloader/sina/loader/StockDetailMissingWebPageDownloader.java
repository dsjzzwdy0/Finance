package com.loris.stock.web.downloader.sina.loader;

import org.apache.log4j.Logger;

import com.loris.stock.web.util.DetailWebPagerChecker;

public class StockDetailMissingWebPageDownloader extends StockDetailWebPageDownloader
{
	private static Logger log = Logger.getLogger(StockDetailMissingWebPageDownloader.class);
	
	/**
	 * Create a new instance of StockDetailMissingWebPageManager.
	 * 
	 * @param context
	 */
	public StockDetailMissingWebPageDownloader()
	{
		super();
	}

	/**
	 * prepare for the Manager.
	 */
	@Override
	public boolean prepare()
	{
		try
		{
			DetailWebPagerChecker.fastCheckStockDetailPages(context, pages);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.warn("Error in check pages: " + e.toString());
		}
		return false;
	}

	@Override
	public String toString()
	{
		return "StockDetailMissingWebPageManager [stocks=" + stocks.size()
				+ ", baseURL=" + baseURL + ", name=" + name
				+ ", tableName=" + tablename + ", type=" + type + ", enable=" + enable + ", encoding=" + encoding
				+ ", pages=" + pages + ", interval=" + interval + "]";
	}
	
	
}
