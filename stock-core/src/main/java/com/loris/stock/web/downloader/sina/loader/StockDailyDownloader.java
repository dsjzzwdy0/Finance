package com.loris.stock.web.downloader.sina.loader;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.web.http.UrlFetcher;
import com.loris.base.web.page.WebPage;
import com.loris.stock.analysis.calculator.CapitalCalculator;
import com.loris.stock.bean.data.table.DailyRecord;
import com.loris.stock.bean.data.table.StockInfo;
import com.loris.stock.bean.data.table.capital.DetailCapitalFlow;
import com.loris.stock.bean.model.StockDayDetailList;
import com.loris.stock.web.downloader.sina.parser.DailyRecordList;
import com.loris.stock.web.downloader.sina.parser.WebContentParser;
import com.loris.stock.web.page.StockDailyWebPage;
import com.loris.stock.web.page.StockDetailWebPage;
import com.loris.stock.web.util.DetailWebPagerChecker;

/**
 * 新浪网站日交易数据下载管理器
 * 
 * @author jiean
 *
 */
public class StockDailyDownloader extends StockInfoWebPageDownloader
{
	private static Logger logger = Logger.getLogger(StockDailyDownloader.class);

	/** The stock total number. This will be replaced by downloaded data. */
	int stockTotalNum = 2000;

	/** The num of per page to be fetched. */
	int numOfPerPage = 40;

	/** The downloaded web pages. */
	private List<StockDailyWebPage> downloadedDailyPages;

	/** 已经下载的股票详细数据 */
	List<StockDetailWebPage> detailWebPages;

	/** 是否第一次下载 */
	boolean isFirst = true;

	/**
	 * 准备下载数据
	 */
	@Override
	public boolean prepare()
	{
		stocks = stockManager.getStockList();
		// 如果没有下载，则先行下载第一页数据
		int dailySize = (int) Math.ceil(stockTotalNum * 1.0 / numOfPerPage);
		totalSize = dailySize + stocks.size();

		StockDailyWebPage dailyWebPage = creator.createStockDailyWebPage(1, numOfPerPage, stockTotalNum);
		try
		{
			if(UrlFetcher.fetch(dailyWebPage))
			{
				afterDownload(dailyWebPage, true);
				logger.info("Download: " + dailyWebPage);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}
	
	/*
	@Override
	public void afterPrepared()
	{
		logger.info("There are " + totalSize + " pages and there are " + leftSize() + " pages to be downloaded.");
	}*/

	/**
	 * 更新下载页面
	 * 
	 * @param bean
	 *            已经下载的数据列表
	 * @param isFirst
	 *            是否第一次下载
	 */
	protected void updateDownloadDailyPages(DailyRecordList bean, boolean isFirst)
	{
		logger.info("Update download daily page. " + bean.getCount());
		if (bean != null)
		{
			// 在已经下载了数据的情况下
			String date = bean.getDay();
			stockTotalNum = bean.getCount();
			int dailySize = (int) Math.ceil(stockTotalNum * 1.0 / numOfPerPage);
			downloadedDailyPages = stockPageManager.getStockDailyPages(date);

			for (int i = 2; i <= dailySize; i++)
			{
				StockDailyWebPage dailyWebPage = creator.createStockDailyWebPage(i, numOfPerPage, stockTotalNum);
				if (!isDownloaded(dailyWebPage))
				{
					pages.put(dailyWebPage);
				}
			}

			//处理详细数据下载页面
			detailWebPages = stockPageManager.getDetailPageInfos(date);
			for (StockInfo stockInfo : stocks)
			{
				addStockDetailWebPage(date, stockInfo.getSymbol());
			}

			totalSize = dailySize + stocks.size();
			logger.info("Download page is " + totalSize);
			return;
		}
	}
	
	/**
	 * 添加日常详细数据下载页面
	 * 
	 * @param date 日期
	 * @param symbol 股票编号
	 */
	protected void addStockDetailWebPage(String date, String symbol)
	{
		StockDetailWebPage detailWebPage = getOrCreateDetailWebPage(date, symbol);
		if (detailWebPage != null)
		{
			pages.put(detailWebPage);
		}
	}

	/**
	 * 后处理
	 * @param page 下载的页面
	 * @param flag 下载成功的标志
	 */
	@Override
	public void afterDownload(WebPage page, boolean flag)
	{
		// 下载不成功
		if (!flag)
		{
			return;
		}

		if (page instanceof StockDailyWebPage)
		{
			StockDailyWebPage page2 = (StockDailyWebPage) page;
			processDailyWebPage(page2);
		}
		else if(page instanceof StockDetailWebPage)
		{
			StockDetailWebPage page2 = (StockDetailWebPage) page;
			processDetailWebPage(page2);
		}
		super.afterDownload(page, flag);
	}

	/**
	 * 处理日常交易数据下载管理器
	 * 
	 * @param page 数据页面
	 */
	protected void processDailyWebPage(StockDailyWebPage page)
	{		
		DailyRecordList bean = WebContentParser.parseDailyWebPage(page);
		if(isFirst && bean != null)
		{
			//logger.info("Update");
			updateDownloadDailyPages(bean, isFirst);
			isFirst = false;
		}

		//logger.info("IsFirst: " + isFirst + ": bean: " + (bean == null));
		//logger.info("Processing page: " + page);
		if (bean != null && bean.getItems().size() > 0)
		{
			List<DailyRecord> items = bean.getItems();
			DailyRecord r = items.get(0);
			page.setCreatetime(r.getDay());

			// 添加数据库中
			stockManager.addDailyRecords(items);
			stockPageManager.updateOrAddDailyWebPage(page);

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
					
					//如果不在列表中， 则加入下载列表
					addStockDetailWebPage(bean.getDay(), record.getSymbol());
					// stockManager.addStock(stockInfo);
				}
			}

			// 若有新股，则添加到数据库中
			if (newStocks.size() > 0)
			{
				stockManager.addStock(newStocks);
			}

			String info = "Total = " + stockTotalNum + " PageNum = " + totalSize + " CurPage = " + page.getOrdinary();
			logger.info(info);
		}
	}
	
	/**
	 * 处理日交易详细交易数据
	 * 
	 * @param page 下载的页面
	 */
	protected void processDetailWebPage(StockDetailWebPage page)
	{
		StockDayDetailList dataset = WebContentParser.parseDetailWebPage(page);
		DetailCapitalFlow flow = null;
		if (dataset != null && dataset.size() > 0)
		{
			page.setCompleted(true);
			flow = new DetailCapitalFlow();
			CapitalCalculator.calculateCapitalFlow(dataset, flow);
			flow.synchronize();
		}
		else if (DetailWebPagerChecker.hasNoValidateContent(page))
		{
			page.setCompleted(true);
		}

		synchronized (stockManager)
		{
			if (page.isNew())
			{
				page.setId("");
			}
			stockPageManager.updateOrAddDetailWebPage(page);

			// 添加分类数据
			if (flow != null)
			{
				stockManager.addDetailCapitalFlow(flow);
			}
		}

		flow = null;
		page = null;
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

	/**
	 * 检查是否已经下载
	 * 
	 * @page 下载的页面
	 */
	@Override
	public boolean isDownloaded(WebPage page)
	{
		if (downloadedDailyPages == null)
		{
			return false;
		}

		if (page instanceof StockDailyWebPage)// 日交易数据
		{
			StockDailyWebPage page2 = (StockDailyWebPage) page;

			for (StockDailyWebPage p : downloadedDailyPages)
			{
				if (!p.isCompleted())
					continue;
				if (p.getOrdinary() == page2.getOrdinary() && p.getPagenum() == page2.getPagenum())
					return true;
			}
		}
		else if (page instanceof StockDetailWebPage) // 日交易详细数据
		{
			StockDetailWebPage page2 = (StockDetailWebPage) page;
			for (StockDetailWebPage p : detailWebPages)
			{
				if (page2.getSymbol().equals(p.getSymbol()))
				{
					if (!p.isCompleted())
					{
						page2.setNew(false);
						page2.setTablename(tablename);
						page2.setEncoding(encoding);
						page2.setId(p.getId());
						// addPage(page);
						return false;
					}
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 创建一个下载页面，如果已经下载，则不创建
	 * 
	 * @param date
	 * @param symbol
	 * @return
	 */
	protected StockDetailWebPage getOrCreateDetailWebPage(String date, String symbol)
	{
		for (StockDetailWebPage p : detailWebPages)
		{
			if (symbol.equals(p.getSymbol()))
			{
				if (!p.isCompleted())
				{
					p.setNew(false);
					p.setTablename(tablename);
					p.setEncoding(encoding);
					return p;
				}
				return null;
			}
		}

		return creator.createStockDetailWebPage(date, symbol);
	}

	/**
	 * 不支持的操作
	 * 
	 */
	@Override
	public String formatURL(StockInfo stock, String... params)
	{
		throw new UnsupportedOperationException();
	}
}
