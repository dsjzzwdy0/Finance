package com.loris.stock.web.downloader.sina.loader;

import java.util.List;

import org.apache.log4j.Logger;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.web.WebPage;
import com.loris.base.util.DateUtil;
import com.loris.base.util.DaysCollection;
import com.loris.base.util.StringUtil;
import com.loris.stock.analysis.calculator.CapitalCalculator;
import com.loris.stock.bean.data.table.StockInfo;
import com.loris.stock.bean.data.table.TradeDate;
import com.loris.stock.bean.data.table.capital.DetailCapitalFlow;
import com.loris.stock.bean.model.StockDayDetailList;
import com.loris.stock.web.downloader.sina.parser.WebContentParser;
import com.loris.stock.web.page.StockDetailWebPage;
import com.loris.stock.web.util.DetailWebPagerChecker;

/**
 * 
 * @author usr
 *
 */
public class StockDetailWebPageDownloader extends StockInfoWebPageDownloader
{
	private static Logger logger = Logger.getLogger(StockDetailWebPageDownloader.class);

	/** Days to be download. */
	protected DaysCollection dayList = new DaysCollection();

	/**
	 * Create a new instance of StockDetailWebPageManager.
	 */
	public StockDetailWebPageDownloader()
	{
		super();
		setType(PAGE_TYPE_DETAIL);
		setBaseURL(URL_DETAIL_WEB_PAGE);
		setTablename(TABLE_NAME_DETAIL_WEB);
	}
	
	/**
	 * 
	 * @param pages
	 * @param stock
	 * @param day
	 */
	protected void addOrIgnoreStockWebPage(List<StockDetailWebPage> pages, StockInfo stock, String day)
	{
		for (StockDetailWebPage page : pages)
		{
			if(page.getSymbol().equals(stock.getSymbol()))
			{
				if(!page.isCompleted())
				{
					page.setNew(false);
					page.setTablename(tablename);
					page.setEncoding(encoding);
					addPage(page);
				}
				return;
			}
		}
		
		String url = formatURL(stock, day);
		StockDetailWebPage page = new StockDetailWebPage();
		page.setNew(true);
		page.setCompleted(false);
		page.setUrl(url);
		page.setTablename(tablename);
		page.setCreatetime(day);
		page.setSymbol(stock.getSymbol());
		page.setType(type);
		page.setEncoding(encoding);
		page.setDay(day);
		addPage(page);
	}

	/**
	 * Prepare the Detail Manager.
	 */
	@Override
	public boolean prepare()
	{
		super.prepare();
		
		//起止时间不为空
		if(StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end))
		{
			getTradeDates(start, end);
		}
		
		if(stocks == null)
		{
			stocks = stockManager.getStockList();
		}
		
		if (stocks == null )
		{
			logger.warn("Stock list is empty.");
			// There is nothing to download.
			return false;
		}
		
		if(dayList.size() <= 0)
		{
			logger.warn("Download date is not defined.");
		}

		logger.info("DetailWebManager Days: " + dayList);
		int daySize = dayList.size();
		String day;
		for(int i = 0; i < daySize; i ++)
		{
			day = dayList.getDateString(i);
			List<StockDetailWebPage> ps = stockPageManager.getDetailPageInfos(day);
			for (StockInfo stock : stocks)
			{
				addOrIgnoreStockWebPage(ps, stock, day);
			}
		}
		
		totalSize = daySize * stocks.size();
		logger.info("There are " + totalSize() + " pages and left = " + leftSize() + " pages to be downloaded.");
		return true;
	}

	/**
	 * After download the page.
	 */
	@Override
	public void afterDownload(WebPage page, boolean flag)
	{
		if (!flag)
		{
			// this will do nothing.
			return;
		}

		StockDetailWebPage spage = (StockDetailWebPage) page;

		StockDayDetailList dataset = WebContentParser.parseDetailWebPage(spage);
		DetailCapitalFlow flow = null;
		if (dataset != null && dataset.size() > 0)
		{
			page.setCompleted(true);
			flow = new DetailCapitalFlow();
			CapitalCalculator.calculateCapitalFlow(dataset, flow);
			flow.synchronize();
		}
		else if (DetailWebPagerChecker.hasNoValidateContent(spage))
		{
			page.setCompleted(true);
		}

		//StockManager manager = StockManager.getInstance();
		//synchronized (stockManager)
		{
			if (page.isNew())
			{
				spage.setId("");
			}
			stockPageManager.updateOrAddDetailWebPage(spage);

			// 添加分类数据
			if (flow != null)
			{
				stockManager.addDetailCapitalFlow(flow);
			}
		}

		flow = null;
		page = null;
		
		super.afterDownload(spage, flag);
	}

	/**
	 * Add the day string.
	 * 
	 * @param day
	 */
	public void addDay(String day)
	{
		dayList.addDay(day);
	}

	/**
	 * Add the Days.
	 * 
	 * @param days
	 */
	public void setDays(String days)
	{
		logger.info("Detail Page manager: " + days);
		String[] strings = days.split(",");
		for (String string : strings)
		{
			if (!StringUtil.isNullOrEmpty(string))
			{
				string = string.trim().toLowerCase();
				if ("today".equals(string) || "current".equals(string))
				{
					addDay(DateUtil.getCurDayStr());
				}
				else
				{
					string = DateUtil.getDayString(string);
					if (!StringUtil.isNullOrEmpty(string))
					{
						addDay(string);
					}
				}
			}
		}
	}
	
	/**
	 * Get the Trade dates.
	 * 
	 * @param start
	 * @param end
	 */
	protected void getTradeDates(String start, String end)
	{
		start = start.trim();
		end = end.trim();
		
		List<TradeDate> tradeDates = stockManager.geTradeDates(start, end);
		for (TradeDate tradeDate : tradeDates)
		{
			if(tradeDate.isFlag())
			{
				dayList.addDay(tradeDate.getDate());
			}
		}
	}

	/**
	 * Format the URL String.
	 * 
	 * @param stock
	 *            StockInfo
	 * @param params
	 */
	@Override
	public String formatURL(StockInfo stock, String... params)
	{
		if (params == null || params.length > 1)
			throw new IllegalArgumentException("Params length is not correct. ");

		return String.format(baseURL, params[0], stock.getSymbol());
	}

	/**
	 * ToString
	 */
	@Override
	public String toString()
	{
		return "StockDetailWebPageManager [days=" + dayList + ", stocks=" + stocks.size()
				+ ", baseURL=" + baseURL + ", name=" + name + ", tableName=" + tablename
				+ ", type=" + type + ", enable=" + enable + ", encoding=" + encoding + ", pages=" + pages
				+ ", interval=" + interval + "]";
	}

	/**
	 * Format the URL.
	 * 
	 * @param stock
	 * @param day
	 * @return
	 */
	public static String getURL(String symbol, String day)
	{
		return String.format(URL_DETAIL_WEB_PAGE, day, symbol);
	}
}
