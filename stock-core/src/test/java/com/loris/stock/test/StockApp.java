package com.loris.stock.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loris.base.context.LorisContext;
import com.loris.stock.bean.data.table.TradeDate;
import com.loris.stock.bean.data.table.capital.DetailCapitalFlow;
import com.loris.stock.repository.StockContext;
import com.loris.stock.repository.StockManager;
import com.loris.stock.web.page.StockDetailWebPage;
import com.loris.stock.web.repository.StockPageManager;
import com.loris.stock.web.repository.io.StockWebPageContentIO;

public class StockApp
{
	private static Logger logger = Logger.getLogger(StockApp.class);

	/** The Application Context. */
	static ClassPathXmlApplicationContext context;

	/**
	 * 
	 * @return
	 */
	public static LorisContext getLorisContext()
	{
		/** The Application Context. */
		context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		LorisContext appContext = new StockContext(context);
		return appContext;
	}

	public static void close()
	{
		try
		{
			context.close();
		}
		catch (Exception e)
		{
		}
	}

	public static void main(String[] args)
	{
		System.out.println("Test for Test.");
		try
		{
			LorisContext context = getLorisContext();
			// test();
			//testCapitalFlow(context);
			//testDetailPage(context);			
			//testTradeDate(context);
			// testFastJson(context);			
			//testStockPageManager(context);
			
			testDetailContent(context);

			close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 测试数据下载与写出
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testDetailContent(LorisContext context) throws Exception
	{
		StockPageManager stockPageManager = context.getApplicationContext().getBean(StockPageManager.class);
		List<String> downDates = stockPageManager.getDetailDownloadDays();
		
		int i = 1;
		for (String date : downDates)
		{
			logger.info(i +++ ": " + date);
			
			long st = System.currentTimeMillis();
			List<StockDetailWebPage> pages = stockPageManager.getDetailPages(date);
			long en = System.currentTimeMillis();
			logger.info("Total spend time is " + (en - st) + "ms to get " + pages.size() + " pages.");
			
			st = System.currentTimeMillis();
			for (StockDetailWebPage page : pages)
			{
				StockWebPageContentIO.writeWebPageContent(page);
			}
			en = System.currentTimeMillis();
			logger.info("Total spend time is " + (en - st) + "ms to write " + pages.size() + " pages.");
			//break;
		}
		
	}
	
	/**
	 * 测试
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testStockPageManager(LorisContext context) throws Exception 
	{
		//StockPageManager stockManager = context.getApplicationContext().getBean(StockPageManager.class);
		StockManager manager = context.getApplicationContext().getBean(StockManager.class);
		
		logger.info("BaseDir: " + manager.getBaseDir());		
		StockWebPageContentIO.BASE_PATH = manager.getBaseDir();
		
		String date = "2018-01-29";
		String symbol = "sh600345";
		StockDetailWebPage page = new StockDetailWebPage();
		page.setCreatetime(date);
		page.setSymbol(symbol);
		
		StockWebPageContentIO.writeWebPageContent(page);
		
		long st = System.currentTimeMillis();
		StockWebPageContentIO.readWebPageContent(page);
		long en = System.currentTimeMillis();
		
		logger.info("Total spend " + (en - st) + "ms to read page.");
		
		logger.info(page.getContent().length());
	}
	
	/**
	 * Test FastJson.
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testFastJson(LorisContext context) throws Exception
	{
		String str = "{\"2018-03-01\":1,\"2018-03-02\":1,\"2018-03-03\":0,\"2018-03-04\":0,\"2018-03-05\":1,\"2018-03-06\":1," 
				+ "\"2018-03-07\":1,\"2018-03-08\":1,\"2018-03-09\":1,\"2018-03-10\":0,\"2018-03-11\":0,\"2018-03-12\":1,"
				+ "\"2018-03-13\":1,\"2018-03-14\":1,\"2018-03-15\":1,\"2018-03-16\":1,\"2018-03-17\":0,\"2018-03-18\":0," 
				+ "\"2018-03-19\":1,\"2018-03-20\":1,\"2018-03-21\":1,\"2018-03-22\":1,\"2018-03-23\":1,\"2018-03-24\":0,"
				+ "\"2018-03-25\":0,\"2018-03-26\":1,\"2018-03-27\":1,\"2018-03-28\":1,\"2018-03-29\":1,\"2018-03-30\":1,\"2018-03-31\":0}";
		JSONObject object = JSON.parseObject(str);
		for (String key : object.keySet())
		{
			logger.info(key + ": " + object.getBoolean(key));
		}
	}
	

	
	/**
	 * Test Trade Date.
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testTradeDate(LorisContext context) throws Exception
	{
		StockManager stockManager = context.getApplicationContext().getBean(StockManager.class);
		
		//Date date = new Date();
		//List<Date> dates = DateUtil.getDates(date, 400);
		
		List<TradeDate> tradeDates = new ArrayList<>();
		
		/*
		tradeDates.add(new TradeDate(date));
		for (Date date2 : dates)
		{
			TradeDate tradeDate = new TradeDate(date2);
			tradeDates.add(tradeDate);
			logger.info(tradeDate);
		}*/
		
		String start = "2018-03-02";
		String end = "2018-03-08";
		
		tradeDates = stockManager.geTradeDates(start, end);
		
		for (TradeDate date : tradeDates)
		{
			logger.info(date);
		}
		
		logger.info(UUID.randomUUID().toString());
		//stockManager.insertTradeDates(tradeDates);
	}
	
	/**
	 * Test Detail Page.
	 * 
	 * @param context
	 * @throws Exception
	 */
	public static void testDetailPage(LorisContext context) throws Exception
	{
		StockPageManager stockManager = context.getApplicationContext().getBean(StockPageManager.class);
		List<StockDetailWebPage> pages = stockManager.getDetailPageInfos("2018-01-26");
		for (StockDetailWebPage stockDetailWebPage : pages)
		{
			System.out.println(stockDetailWebPage);
		}
	}

	/**
	 * Test
	 * 
	 * @throws Exception
	 */
	public static void test(LorisContext context) throws Exception
	{
		StockPageManager stockManager = context.getApplicationContext().getBean(StockPageManager.class);
		List<String> days = stockManager.getDetailDownloadDays();

		for (String string : days)
		{
			logger.info(string);
		}
		System.out.println("Size : " + days.size());
	}

	/**
	 * test capital flow.
	 * 
	 * @throws Exception
	 */
	public static void testCapitalFlow(LorisContext context) throws Exception
	{
		StockManager stockManager = context.getApplicationContext().getBean(StockManager.class);
		Map<String, Object> params = new HashMap<>();
		params.put("symbol", "sh600018");
		List<DetailCapitalFlow> flows = stockManager.getCapitalFlow(params);
		for (DetailCapitalFlow detailCapitalFlow : flows)
		{
			System.out.println(detailCapitalFlow);
		}
	}
}
