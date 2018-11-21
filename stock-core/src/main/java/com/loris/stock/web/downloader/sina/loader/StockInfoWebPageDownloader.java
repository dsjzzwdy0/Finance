package com.loris.stock.web.downloader.sina.loader;

import java.util.List;

import com.loris.base.context.LorisContext;
import com.loris.base.web.page.WebPage;
import com.loris.stock.bean.data.table.StockInfo;
import com.loris.stock.repository.StockManager;
import com.loris.stock.web.downloader.sina.SinaStockDownloader;
import com.loris.stock.web.repository.StockPageManager;

public abstract class StockInfoWebPageDownloader extends SinaStockDownloader
{
	//private static Logger log = Logger.getLogger(StockInfoWebPageManager.class);
	/** The Table name defined for Web page. */
	public static final String TABLE_NAME_DETAIL_WEB = "stock_web_capital_content";
	public static final String TABLE_NAME_DAILY_WEB = "stock_web_daily_content";
	public static final String TABLE_NAME_BASIC_INFO = "stock_web_info_content";
	

	/** The page type. */
	public static final String PAGE_TYPE_DETAIL = "detail";
	public static final String PAGE_TYPE_BASE_INFO = "gdhs";                //股东户数

	/** The detailed url information. */
	public static final String URL_DETAIL_WEB_PAGE = "http://market.finance.sina.com.cn/downxls.php?date=%s&symbol=%s";
	public static final String URL_BASE_INFO_PAGE = "http://stock.finance.qq.com/corp1/stk_holder_count.php?zqdm=%s";       //股东户数
	/** The daily web url. */
	public static final String URL_DAILY_WEB_PAGE = "http://money.finance.sina.com.cn/d/api/openapi_proxy.php/?__s=";
	
	/** The stock list. */
	protected List<StockInfo> stocks;
	
	/** The StockList. */
	protected static StockManager stockManager = null;
	
	/** The StockPageManager. */
	protected static StockPageManager stockPageManager;
	
	/**
	 * Create a new instance of StockInfoWebManager.
	 */
	public StockInfoWebPageDownloader()
	{
		super();
		
		/*
		session = StockManager.getInstance().getSqlSessionFactory().openSession();		
		pageMapper = session.getMapper(PageMapper.class);*/
	}
	
	public void setLorisContext(LorisContext context)
	{
		super.setLorisContext(context);
		if(context != null)
		{
			stockManager = context.getApplicationContext().getBean(StockManager.class);
			stockPageManager = context.getApplicationContext().getBean(StockPageManager.class);
			stocks = stockManager.getStockList();
		}
	}
	
	/**
	 * Set the stocklist.
	 * @param stocks
	 */
	public void setStockList(List<StockInfo> stocks )
	{
		this.stocks = stocks;
	}
	
	/**
	 * Get the stock list.
	 * @return
	 */
	public List<StockInfo> getStockList()
	{
		return stocks;
	}

	/**
	 * Prepare for the StockWebPageManager.
	 */
	@Override
	public boolean prepare()
	{
		return true;
	}

	@Override
	public void beforeDownload(WebPage page)
	{
		super.beforeDownload(page);
	}

	@Override
	public void afterDownload(WebPage page, boolean flag)
	{
		super.afterDownload(page, flag);
	}
	
	/**
	 * Check if the symbol exist in the stock list.
	 * 
	 * @param symbol
	 * @return
	 */
	public boolean isExistStock(String symbol)
	{
		if(stocks == null)
		{
			return false;
		}
		for (StockInfo stockInfo : stocks)
		{
			if(symbol.equals(stockInfo.getSymbol()))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isDownloaded(WebPage page)
	{
		return false;
	}

	@Override
	public void close()
	{
	}

	/**
	 * format the url.
	 * @param stock
	 * @param params
	 * @return
	 */
	public abstract String formatURL(StockInfo stock, String...params); 
}
