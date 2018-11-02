package com.loris.stock.web.downloader.sina.loader;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.bean.web.WebPage;
import com.loris.base.web.http.UrlFetcher;
import com.loris.stock.bean.data.table.DailyRecord;
import com.loris.stock.bean.data.table.StockInfo;
import com.loris.stock.web.downloader.sina.parser.DailyRecordList;
import com.loris.stock.web.downloader.sina.parser.WebContentParser;
import com.loris.stock.web.page.StockDailyWebPage;

public class StockDailyWebPageDownloader extends StockInfoWebPageDownloader
{
	private static Logger log = Logger.getLogger(StockInfoWebPageDownloader.class);

	public static String DAILY_PAGE_TYPE = "301";
	public static String DAILY_PAGE_ENCODING = "GBK";

	/** The parameter url string. */
	private String paramStr = "%22hq%22,%22hs_a%22,%22%22,0,"; // [[%22hq%22,%22hs_a%22,%22%22,0,%d,%d]]

	/** The stock total number. This will be replaced by downloaded data. */
	int stockTotalNum = 1000;

	/** The num of per page to be fetched. */
	int numOfPerPage = 40;

	
	/** The downloaded web pages. */
	private List<StockDailyWebPage> downloadPages;
	
	/** The day to be downloaded. */
	private String day = "";

	/**
	 * StockDailyWebPageManager.
	 */
	public StockDailyWebPageDownloader()
	{
		setBaseURL(URL_DAILY_WEB_PAGE);
		setType(DAILY_PAGE_TYPE);
		setTablename(TABLE_NAME_DAILY_WEB);
		setEncoding(DAILY_PAGE_ENCODING);
		totalSize = 80;
	}

	@Override
	public boolean prepare()
	{
		StockDailyWebPage page = getCurDailyPage();
		try
		{
			if(UrlFetcher.fetch(page))
			{
				curIndex ++;
				
				DailyRecordList bean = WebContentParser.parseDailyWebPage(page);
				if (bean != null && bean.getItems().size() > 0)
				{
					this.stockTotalNum = bean.getCount();			
					this.totalSize = (int) Math.ceil((double) stockTotalNum / numOfPerPage);
					this.day = bean.getDay();					
					page.setTotal(stockTotalNum);
					page.setCreatetime(day);
					
					downloadPages = stockPageManager.getStockDailyPages(day);
					
					//添加新的下载页至列表中
					for(; curIndex <= totalSize; )
					{
						StockDailyWebPage p = getCurDailyPage();
						curIndex ++;
						if(p != null && !isDownloaded(p))
							pages.put(p);
					}
					
					if(!isDownloaded(page))
					{
						afterDownload(page, true);
					}
					
					curIndex = 0;
					totalSize = pages.length();
				}				
				return true;
			}
		}
		catch(Exception e)
		{			
			log.info("Exception occur when prepare for the StockDailyWebPageManager, " + e.toString() + ".");
		}
		curIndex = 0;
		totalSize = 0;
		stockTotalNum = 0;
		return false;
	}


	/**
	 * Check there are next page to be downloaded.
	 * 
	 * @return
	 
	@Override
	public boolean hasNextWebPage()
	{
		return currentPage <= stockTotalPage;
	}*/

	/**
	 * Get the current WebPage.
	 * 
	 * @return
	 
	@Override
	public StockDailyWebPage popWebPage()
	{
		pages.get();
		StockDailyWebPage page = new StockDailyWebPage();
		try
		{
			String url = paramStr + currentPage + "," + numOfPerPage; 
			// String.format(paramStr, currentPage, numOfPerPage);
			// String url =
			// "http://money.finance.sina.com.cn/d/api/openapi_proxy.php/?__s=";
			url = baseURL + URLEncoder.encode("[[", "utf-8") + url + URLEncoder.encode("]]", "utf-8");

			page.setType(type);
			page.setEncoding(encoding);
			page.setURL(url);
			page.setTablename(tablename);
			page.setCreatetime(DateUtil.getCurDayStr());
			page.setOrdinary(currentPage);
			page.setPagenum(numOfPerPage);
			page.setTotal(stockTotalNum);

			currentPage++;
		}
		catch (Exception e)
		{
			log.warn("Error in popWebPage '" + e.toString() + "'");
		}
		return page;
	}*/

	/**
	 * After
	 */
	@Override
	public void afterDownload(WebPage page, boolean flag)
	{
		// 下载不成功
		if (!flag)
		{
			return;
		}

		if (!(page instanceof StockDailyWebPage))
		{
			return;
		}

		StockDailyWebPage page2 = (StockDailyWebPage) page;

		// System.out.println(page.getContent());
		//page.setCreatetime(DateUtil.DAY_FORMAT.format(new Date()));

		DailyRecordList bean = WebContentParser.parseDailyWebPage(page2);

		if (bean != null && bean.getItems().size() > 0)
		{
			List<DailyRecord> items = bean.getItems();
			DailyRecord r = items.get(0);
			page2.setCreatetime(r.getDay());

			//添加数据库中
			stockManager.addDailyRecords(items);
			stockPageManager.updateOrAddDailyWebPage(page2);

			// 检测是否有新股发行
			List<StockInfo> newStocks = new ArrayList<StockInfo>();
			for (DailyRecord record : items)
			{
				if (!isExistStock(record.getSymbol()))
				{
					StockInfo stockInfo = new StockInfo(record.getSymbol());
					stockInfo.setName(record.getName());
					stockInfo.setCode(record.getCode());
					newStocks.add(stockInfo);
					// stockManager.addStock(stockInfo);
				}
			}

			// 若有新股，则添加到数据库中
			if (newStocks.size() > 0)
			{
				stockManager.addStock(newStocks);
			}

			String info = "Total = " + stockTotalNum + " PageNum = " + totalSize + " CurPage = " + page2.getOrdinary();
			log.info(info);
		}
		
		super.afterDownload(page, flag);
	}
	
	/**
	 * Check if the page has been downloaded.
	 * 
	 * @param page
	 * @return
	 
	protected boolean isDownloaded(StockDailyWebPage page)
	{
		if(downloadPages == null)
		{
			return false;
		}
		for (StockDailyWebPage p : downloadPages)
		{
			if(!p.isCompleted())
				continue;
			if(p.getOrdinary() == page.getOrdinary() && p.getPagenum() == page.getPagenum())
				return true;
		}
		return false;
	}*/
	

	@Override
	public boolean isDownloaded(WebPage page)
	{
		if(downloadPages == null)
		{
			return false;
		}
		
		StockDailyWebPage page2 = (StockDailyWebPage)page;
		
		for (StockDailyWebPage p : downloadPages)
		{
			if(!p.isCompleted())
				continue;
			if(p.getOrdinary() == page2.getOrdinary() && p.getPagenum() == page2.getPagenum())
				return true;
		}
		return false;
	}


	@Override
	public String toString()
	{
		return "StockDailyWebPageManager [stockTotalNum=" + stockTotalNum + ", stockTotalPage=" + totalSize
				+ ", numOfPerPage=" + numOfPerPage + ", baseURL=" + baseURL + ", name=" + name + ", tableName="
				+ tablename + ", type=" + type + ", enable=" + enable + ", encoding=" + encoding + ", pages=" + downloadPages
				+ ", interval=" + interval + "]";
	}
	
	
	/**
	 * Format the URL string value.
	 * 
	 * @param currentPage
	 * @param numOfPerPage
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	protected String formatURL(int currentPage, int numOfPerPage) throws UnsupportedEncodingException
	{
		String url = paramStr + currentPage + "," + numOfPerPage; 
		// String.format(paramStr, currentPage, numOfPerPage);
		// String url =
		// "http://money.finance.sina.com.cn/d/api/openapi_proxy.php/?__s=";
		url = baseURL + URLEncoder.encode("[[", "utf-8") + url + URLEncoder.encode("]]", "utf-8");
		return url;
	}
	
	/**
	 * Get the CurrentDailyPage.
	 * @return
	 */
	protected StockDailyWebPage getCurDailyPage()
	{
		try
		{
			StockDailyWebPage page = new StockDailyWebPage();
			String url = formatURL(curIndex, numOfPerPage);
			page.setType(type);
			page.setEncoding(encoding);
			page.setUrl(url);
			page.setTablename(tablename);
			page.setCreatetime(day);
			page.setOrdinary(curIndex);
			page.setPagenum(numOfPerPage);
			page.setTotal(stockTotalNum);
			
			return page;
		}
		catch(Exception e)
		{
			log.warn("Error in popWebPage '" + e.toString() + "'");
		}
		return null;
	}

	/**
	 * Format URL.
	 * 
	 */
	@Override
	public String formatURL(StockInfo stock, String... params)
	{
		throw new UnsupportedOperationException();
	}
}
