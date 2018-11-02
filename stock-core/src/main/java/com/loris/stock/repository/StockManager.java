package com.loris.stock.repository;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.stock.bean.data.table.DailyRecord;
import com.loris.stock.bean.data.table.StockInfo;
import com.loris.stock.bean.data.table.TradeDate;
import com.loris.stock.bean.data.table.capital.DetailCapitalFlow;
import com.loris.stock.repository.service.BaseInfoPageService;
import com.loris.stock.repository.service.DailyRecordService;
import com.loris.stock.repository.service.DetailCapitalService;
import com.loris.stock.repository.service.StockInfoService;
import com.loris.stock.repository.service.TradeDateService;
import com.loris.stock.web.page.StockBaseInfoWebPage;
import com.loris.stock.web.repository.io.StockWebPageContentIO;

@Component
public class StockManager
{
	/** 根目录字段名称*/
	public static final String KEY_BASE_DIR = "basedir";
	
	@Autowired
	private DailyRecordService dailyRecordService;

	@Autowired
	private StockInfoService stockInfoService;
	
	@Autowired
	private DetailCapitalService detailCapitalService;
	
	@Autowired
	private BaseInfoPageService baseInfoPageService;
	
	@Autowired
	private TradeDateService tradeDateService;
	
	/** The Single instance. */
	private static StockManager singleton = null;
	
	/** 数据根目录 */
	protected String baseDir;
	
	
	/**
	 * Get the Instance.
	 * @return The StockManager instance
	 */
	public static StockManager getInstance()
	{
		return singleton;
	}
	
	/**
	 * Create a new instance of StockManager
	 */
	protected StockManager()
	{
		singleton = this;
		initialize();
	}
	
	/**
	 * 初始化基本数据
	 */
	private void initialize()
	{
		try
		{
			InputStream in = this.getClass().getResourceAsStream("/stock.properties");
			Properties prop = new Properties();
			prop.load(in);
			
			String tmpDir = prop.getProperty(KEY_BASE_DIR);
			if(StringUtils.isNotEmpty(tmpDir))
			{
				tmpDir = tmpDir.trim();
				baseDir = tmpDir;
			}
			
			StockWebPageContentIO.BASE_PATH = baseDir;
			in.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 数据根目录 
	 * 
	 * @return 数据目标
	 */
	public String getBaseDir()
	{
		return baseDir;
	}

	/**
	 * 设置数据根目录
	 * 
	 * @param baseDir 数据目录
	 */
	public void setBaseDir(String baseDir)
	{
		this.baseDir = baseDir;
	}
	
	/**
	 * Add DailyRecords.
	 * 
	 * @param records
	 * @return
	 */
	public boolean addDailyRecords(List<DailyRecord> records)
	{
		return dailyRecordService.insertBatch(records);
	}
	
	/**
	 * Add DetailCapitalFlows.
	 * 
	 * @param flows
	 * @return
	 */
	public boolean addDetailCapitalFlows(List<DetailCapitalFlow> flows)
	{
		return detailCapitalService.insertBatch(flows);
	}
	
	/**
	 * AddDetailCapitalFlow.
	 * 
	 * @param flow
	 * @return
	 */
	public boolean addDetailCapitalFlow(DetailCapitalFlow flow)
	{
		return detailCapitalService.insert(flow);
	}
	
	
	
	/**
	 * Stock 
	 * @param value
	 * @return
	 */
	public StockInfo getStock(String value)
	{
		EntityWrapper<StockInfo> ew = new EntityWrapper<>();
		ew.eq("symbol", value);
		return stockInfoService.selectOne(ew);
	}
	
	/**
	 * Get the stock list.
	 * 
	 * @return
	 */
	public List<StockInfo> getStockList()
	{
		EntityWrapper<StockInfo> ew = new EntityWrapper<>();
		//ew.orderBy("symbol", true);
		return stockInfoService.selectList(ew);
	}
	
	/**
	 * Add the stock info list.
	 * 
	 * @param stockInfos
	 * @return
	 */
	public boolean addStock(List<StockInfo> stockInfos)
	{
		return stockInfoService.insertBatch(stockInfos);
	}
	
	/**
	 * Add the stock.
	 * 
	 * @param stock
	 * @return
	 */
	public boolean addStock(StockInfo stock)
	{
		return stockInfoService.insert(stock);
	}
	
	
	/**
	 * Upate or add base info web.
	 * 
	 * @param page
	 * @return
	 */
	public boolean updateOrAddBaseInfoWebPage(StockBaseInfoWebPage page)
	{
		if(StringUtils.isEmpty(page.getId()))
		{
			return baseInfoPageService.insert(page);
		}
		else
		{
			EntityWrapper<StockBaseInfoWebPage> ew = new EntityWrapper<>();
			ew.eq("id", page.getId());
			return baseInfoPageService.update(page, ew);
		}
	}
	
	/**
	 * 加入数据
	 * 
	 * @param dates
	 * @return
	 */
	public boolean insertTradeDates(List<TradeDate> dates)
	{
		return tradeDateService.insertBatch(dates);
	}
	
	/**
	 * 查询两个日期之间的交易日期
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<TradeDate> geTradeDates(String start, String end)
	{
		EntityWrapper<TradeDate> ew = new EntityWrapper<TradeDate>();
		ew.between("date", start, end);
		return tradeDateService.selectList(ew);
	}
	
	
	/**
	 * Get the capital flow.
	 * 
	 * @param params
	 * @return
	 */
	public List<DetailCapitalFlow> getCapitalFlow(Map<String, Object> params)
	{
		EntityWrapper<DetailCapitalFlow> ew = new EntityWrapper<>();
		for (String key : params.keySet())
		{
			ew.eq(key, params.get(key));
		}
		return detailCapitalService.selectList(ew);
	}
}
