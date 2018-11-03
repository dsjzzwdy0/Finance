package com.loris.stock.web.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.context.LorisContext;
import com.loris.base.util.DaysCollection;
import com.loris.base.web.util.BoundedFIFO;
import com.loris.base.web.util.URLChecker;
import com.loris.stock.bean.data.table.StockInfo;
import com.loris.stock.bean.model.StockDayDetailList;
import com.loris.stock.repository.StockManager;
import com.loris.stock.web.downloader.sina.loader.StockDetailWebPageDownloader;
import com.loris.stock.web.downloader.sina.loader.StockInfoWebPageDownloader;
import com.loris.stock.web.downloader.sina.parser.WebContentParser;
import com.loris.stock.web.page.StockDetailWebPage;

public class DetailWebPagerChecker
{
	private static Logger log = Logger.getLogger(DetailWebPagerChecker.class);
	
	/**
	 * Check stock detail pages by day.
	 * @param pages
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public static int checkStockDetailPagesByDay(LorisContext context, BoundedFIFO pages) throws IOException, SQLException
	{
		DaysCollection days = new DaysCollection();
		StockFlag stockFlag = new StockFlag();
		StockManager stockManager = context.getApplicationContext().getBean(StockManager.class);
		
		try(Connection connection = context.getConnection())
		{
			log.info("Check days data...");
			
			Statement st = connection.createStatement();
			String sql = "select createtime from STOCK_WEB_CAPITAL_CONTENT group by createtime";
			try (ResultSet rs = st.executeQuery(sql))
			{
				while (rs.next())
				{
					days.addDay(rs.getString(1));
				}
			}
			
			int daySize = days.size();
			log.info("Download: " + days);
			
			log.info("Start to check stocks...");
			
			/**
			 * Get the stocks.
			 */
			List<StockInfo> stocks = stockManager.getStockList();
			stockFlag.setStocks(stocks);
			int stockSize = stockFlag.size();
			int rowNum = 0;
	
			for (int i = 0; i < daySize; i ++)
			{
				String day = days.getDateString(i);
				long start = System.currentTimeMillis();
				
				sql = "select symbol, url, type, createtime, content from STOCK_WEB_CAPITAL_CONTENT " + "where createtime = '"
						+ day + "' order by symbol";
				
				log.info("Start to check day = '" + day + "'...");
				stockFlag.resetFlag();
				try (ResultSet rs = st.executeQuery(sql))
				{
					String str = "";
					while (rs.next())
					{
						StockDetailWebPage page = new StockDetailWebPage();
	
						page.setSymbol(rs.getString(1));
						page.setUrl(rs.getString(2));
						page.setType(rs.getString(3));
						page.setCreatetime(rs.getString(4));
						page.setContent(rs.getString(5));
						
						StockDayDetailList dataset = WebContentParser.parseDetailWebPage(page);	
						stockFlag.setFlag(page.getSymbol());
						
						if (dataset == null || dataset.size() <= 1)
						{
							//This will check the URL value.
							if(isValidateURL(page) && hasNoValidateContent(page))
								continue;
							//System.out.println(page + ": " + page.getType());
							page.setUrl(StockDetailWebPageDownloader.getURL(page.getSymbol(),
									page.getCreatetime()));
							page.setCompleted(false);
							page.setNew(false);
							page.setTablename("STOCK_WEB_CAPITAL_CONTENT");
							page.setEncoding(StockDetailWebPageDownloader.ENCODING_GBK);
							
							pages.put(page);
							continue;
						}
					}
					
					for(int j = 0; j < stockSize; j ++)
					{
						StockInfo stockInfo = stockFlag.getStockInfo(j);
						if(stockFlag.getFlag(i) == StockFlag.FLAG_INIT)
						{						
							StockDetailWebPage page = new StockDetailWebPage();
							page.setUrl(StockDetailWebPageDownloader.getURL(stockInfo.getSymbol(), day));
							page.setType(StockInfoWebPageDownloader.PAGE_TYPE_DETAIL);
							page.setSymbol(stockInfo.getSymbol());
							page.setCreatetime(day);
							page.setCompleted(false);
							page.setNew(true);
							page.setTablename("STOCK_WEB_CAPITAL_CONTENT");
							page.setEncoding(StockDetailWebPageDownloader.ENCODING_GBK);
							
							pages.put(page);
						}
					}
					
					long end = System.currentTimeMillis();
					
					//if(rowNum % 2 == 0)
					{
						log.info("Processing " + (rowNum + 1) + " ["+ day + "]: " + str + " " + (end - start) + " ms.");
					}
					
					rowNum ++;
				}
			}
			
			st.close();
		}

		return pages.length();
	}
	
	/**
	 * Fast check whether there are some page need to be loaded.
	 * @param pages
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public static int fastCheckStockDetailPages(LorisContext context, BoundedFIFO pages) throws IOException, SQLException
	{
		StockManager stockManager = context.getApplicationContext().getBean(StockManager.class);
		
		DaysCollection days = new DaysCollection();
		Connection connection = context.getConnection();

		log.info("Check days data...");
		
		Statement st = connection.createStatement();
		String sql = "select createtime from STOCK_WEB_CAPITAL_CONTENT group by createtime";
		try (ResultSet rs = st.executeQuery(sql))
		{
			while (rs.next())
			{
				days.addDay(rs.getString(1));
			}
		}
		
		int daySize = days.size();
		log.info("Download: " + days);
			
		/**
		 * Get the stocks.
		 */
		List<StockInfo> stocks = stockManager.getStockList();
		int rowNum = 0;
		
		for (StockInfo stockInfo : stocks)
		{
			long start = System.currentTimeMillis();
			
			sql = "select symbol, url, type, createtime, completed from STOCK_WEB_CAPITAL_CONTENT " + "where symbol = '"
					+ stockInfo.getSymbol() + "' order by createtime";

			days.resetFlag();
			
			try (ResultSet rs = st.executeQuery(sql))
			{
				String str = "";
				while (rs.next())
				{
					StockDetailWebPage page = new StockDetailWebPage();

					page.setSymbol(rs.getString(1));
					page.setUrl(rs.getString(2));
					page.setType(rs.getString(3));
					page.setCreatetime(rs.getString(4));
					page.setCompleted(rs.getBoolean(5));
					page.setTablename("STOCK_WEB_CAPITAL_CONTENT");

					//设置标记
					days.setFlag(page.getCreatetime());
					if(!page.isCompleted())
					{
						page.setNew(false);
						page.setEncoding(StockDetailWebPageDownloader.ENCODING_GBK);
						pages.put(page);
					}					
				}
				
				//解决库中没有的数据
				for(int i = 0; i < daySize; i ++)
				{
					if(days.getFlag(i) == (short)0)
					{
						String day = days.getDateString(i);
						StockDetailWebPage page = new StockDetailWebPage();
						page.setUrl(StockDetailWebPageDownloader.getURL(stockInfo.getSymbol(), day));
						page.setType(StockInfoWebPageDownloader.PAGE_TYPE_DETAIL);
						page.setSymbol(stockInfo.getSymbol());
						page.setCreatetime(day);
						page.setCompleted(false);
						page.setNew(true);
						page.setTablename("STOCK_WEB_CAPITAL_CONTENT");
						page.setEncoding(StockDetailWebPageDownloader.ENCODING_GBK);
						
						pages.put(page);
					}
				}
				
				long end = System.currentTimeMillis();
				
				//if(rowNum % 2 == 0)
				{
					log.info("Processing " + (rowNum + 1) + " ["+ stockInfo + "]: " + str + " " + (end - start) + " ms.");
				}
				
				rowNum ++;
			}
		}
		
		st.close();
		connection.close();

		return pages.length();
	}
	
	/**
	 * Check there are some page are not in the database.
	 * 
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public static int checkStockDetailPages(LorisContext context, BoundedFIFO pages) throws IOException, SQLException
	{
		StockManager stockManager = context.getApplicationContext().getBean(StockManager.class);
		
		DaysCollection days = new DaysCollection();
		Connection connection = context.getConnection();

		System.out.println("Check days data...");
		
		Statement st = connection.createStatement();
		String sql = "select createtime from STOCK_WEB_CAPITAL_CONTENT group by createtime";
		try (ResultSet rs = st.executeQuery(sql))
		{
			while (rs.next())
			{
				days.addDay(rs.getString(1));
			}
		}
		
		int daySize = days.size();
		log.info("Download: " + days);
			
		/**
		 * Get the stocks.
		 */
		List<StockInfo> stocks = stockManager.getStockList();
		int rowNum = 0;
		
		for (StockInfo stockInfo : stocks)
		{
			long start = System.currentTimeMillis();
			
			sql = "select symbol, url, type, createtime, content from STOCK_WEB_CAPITAL_CONTENT " + "where symbol = '"
					+ stockInfo.getSymbol() + "' order by createtime";

			days.resetFlag();
			
			try (ResultSet rs = st.executeQuery(sql))
			{
				String str = "";
				while (rs.next())
				{
					StockDetailWebPage page = new StockDetailWebPage();

					page.setSymbol(rs.getString(1));
					page.setUrl(rs.getString(2));
					page.setType(rs.getString(3));
					page.setCreatetime(rs.getString(4));
					page.setContent(rs.getString(5));

					//设置标记
					days.setFlag(page.getCreatetime());
					
					StockDayDetailList dataset = WebContentParser.parseDetailWebPage(page);					
					
					if (dataset == null || dataset.size() <= 1)
					{
						//This will check the URL value.
						if(isValidateURL(page) && hasNoValidateContent(page))
							continue;
					
						//System.out.println(page + ": " + page.getType());
						page.setUrl(StockDetailWebPageDownloader.getURL(page.getSymbol(),
								page.getCreatetime()));
						page.setCompleted(false);
						page.setNew(false);
						page.setTablename("STOCK_WEB_CAPITAL_CONTENT");
						page.setEncoding(StockDetailWebPageDownloader.ENCODING_GBK);
						
						pages.put(page);
						continue;
					}
				}
				
				for(int i = 0; i < daySize; i ++)
				{
					if(days.getFlag(i) == (short)0)
					{
						String day = days.getDateString(i);
						StockDetailWebPage page = new StockDetailWebPage();
						page.setUrl(StockDetailWebPageDownloader.getURL(stockInfo.getSymbol(), day));
						page.setType(StockInfoWebPageDownloader.PAGE_TYPE_DETAIL);
						page.setSymbol(stockInfo.getSymbol());
						page.setCreatetime(day);
						page.setCompleted(false);
						page.setNew(true);
						page.setTablename("STOCK_WEB_CAPITAL_CONTENT");
						page.setEncoding(StockDetailWebPageDownloader.ENCODING_GBK);
						
						pages.put(page);
						
						//System.out.println(page);
					}
				}
				
				long end = System.currentTimeMillis();
				
				//if(rowNum % 2 == 0)
				{
					log.info("Processing " + (rowNum + 1) + " ["+ stockInfo + "]: " + str + " " + (end - start) + " ms.");
				}
				
				rowNum ++;
			}
		}
		
		st.close();
		connection.close();

		return pages.length();
	}
	
	/**
	 * Is 
	 * @param page
	 * @return
	 */
	public static boolean isValidateWebPage(StockDetailWebPage page)
	{
		StockDayDetailList dataset = WebContentParser.parseDetailWebPage(page);	
		if (dataset == null || dataset.size() < 1)
		{
			if(!isValidateURL(page) || !hasNoValidateContent(page))
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check exist the data.
	 * @param page
	 * @return
	 */
	public static boolean hasNoValidateContent(StockDetailWebPage page)
	{
		if(page.getContent() != null && page.getContent().contains("当天没有数据"))
			return true;
		return false;
	}
	
	/**
	 * Check if is the validate url string.
	 * @param page
	 * @return
	 */
	private static boolean isValidateURL(StockDetailWebPage page)
	{
		return URLChecker.isRightDetailURL(page.getFullURL());
	}
	
	/*
	public static void checkExist() throws IOException, SQLException
	{
		List<String> days = new ArrayList<String>();

		Connection connection = DataManager.getInstance().getSQLiteConnection();

		Statement st = connection.createStatement();

		String sql = "select createtime from web_content2 group by createtime";
		try (ResultSet rs = st.executeQuery(sql))
		{
			while (rs.next())
			{
				days.add(rs.getString(1));
			}
		}
		
		System.out.println("Start to check stocks...");
		
		List<StockInfo> stocks = StockManager.getDefault().getStockList();
		//int rowNum = 0;
		String dateStr = "2017-10-23";
		
		for (StockInfo stockInfo : stocks)
		{
			long start = System.currentTimeMillis();
			boolean exist = false;
			sql = "select symbol from WEB_CONTENT2 where symbol='"
					+ stockInfo.getSymbol() + "' and createtime='" + dateStr + "'";
			try (ResultSet rs = st.executeQuery(sql))
			{
				while(rs.next())
				{
					exist = true;
					break;
				}
			}
			
			long end = System.currentTimeMillis();
			System.out.println("Find " + stockInfo.getSymbol() + " " + dateStr + " = " + exist + " " + (end - start) + " ms.");
		}
		st.close();
		connection.close();
	}
	
	public static void main(String[] args)
	{
		try
		{
			long start = System.currentTimeMillis();
			BoundedFIFO pages = new BoundedFIFO(2000);
			checkStockDetailPagesByDay(pages);
			System.out.println("Total need to reload page is " + pages.length());
			long end = System.currentTimeMillis();
			System.out.println("Total process time is " + (end - start) + " ms.");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}*/
}
