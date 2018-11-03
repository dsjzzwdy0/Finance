package com.loris.lottery.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.wrapper.PageWrapper;
import com.loris.base.bean.wrapper.Rest;
import com.loris.base.util.DateUtil;
import com.loris.base.util.NumberUtil;
import com.loris.stock.bean.data.table.DailyRecord;
import com.loris.stock.bean.data.table.TradeDate;
import com.loris.stock.bean.data.table.capital.DefaultCapitalFlow;
import com.loris.stock.bean.data.view.StockDetailPageInfo;
import com.loris.stock.repository.service.DailyRecordService;
import com.loris.stock.repository.service.DefaultCapitalFlowService;
import com.loris.stock.repository.service.StockDetailPageInfoService;
import com.loris.stock.repository.service.TradeDateService;
import com.loris.stock.web.page.StockDetailWebPage;
import com.loris.stock.web.repository.service.DetailPageService;

@Controller
@RequestMapping("/stockdata")
public class StockDataController
{
	/** 每页的默认记录数 */
	public static int DEFAULT_PAGE_SIZE = 20;
	
	@Autowired
	private TradeDateService tradeDateService;
	
	@Autowired
	private DailyRecordService dailyRecordService;
	
	@Autowired
	private DetailPageService detailPageService;
	
	@Autowired
	private DefaultCapitalFlowService defaultCapitalFlowService;
	
	@Autowired
	private StockDetailPageInfoService stockDetailPageInfoService;
	
	
	
	@ResponseBody
	@RequestMapping("/tradedates")
	public Rest getTradeDates(String year, String month, String flag)
	{
		EntityWrapper<TradeDate> ew = new EntityWrapper<>();
		if(StringUtils.isNotEmpty(year))
		{
			ew.eq("year", year);
		}
		if(StringUtils.isNotEmpty(month))
		{
			if(month.startsWith("0"))
			{
				month = month.replaceFirst("0", "");
			}
			ew.eq("month", month);
		}
		if(StringUtils.isNotEmpty(flag))
		{
			if("true".equalsIgnoreCase(flag) || "".equalsIgnoreCase(flag) || "1".equals(flag))
			{
				ew.eq("flag", 1);
			}
			else
			{
				ew.eq("flag", 0);
			}
		}
		
		List<TradeDate> dates = tradeDateService.selectList(ew);
		
		return Rest.okList(dates, dates.size());
	}
	
	@ResponseBody
	@RequestMapping("/updatedates")
	public Rest updateTradeDates(String queryStr)
	{
		//System.out.println(queryStr);		
		JSONObject object = JSON.parseObject(queryStr);
		Date date;
		boolean flag;
		TradeDate tradeDate;
		
		List<TradeDate> tradeDates = new ArrayList<>();
		List<String> keys = new ArrayList<>();
		for (String key : object.keySet())
		{
			date = DateUtil.tryToParseDate(key);
			if(date == null)
			{
				continue;
			}
			flag = object.getBooleanValue(key);
			tradeDate = new TradeDate(date);
			tradeDate.setFlag(flag);
			tradeDates.add(tradeDate);
			keys.add(key);
		}
		
		if(keys.size() > 0)
		{
			tradeDateService.deleteBatchIds(keys);
		}
		
		if(tradeDates.size() > 0)
		{
			tradeDateService.insertBatch(tradeDates);
		}
		return Rest.ok();
	}
	
	/**
	 * 按照页码列出详细的已经下载的数据
	 * 
	 * @param symbol 编号
	 * @param pageNumber 页码
	 * @param pageSize 每页的数量
	 * @return 页码
	 */
	@ResponseBody
	@RequestMapping("/pagelist")
	public Rest getDetailWebPageInfo(String symbol, String pageNumber, String pageSize)
	{		
		int pNum = NumberUtil.parseInt(pageNumber);
        int pSize = NumberUtil.parseInt(pageSize);
        pSize = pSize == 0 ? DEFAULT_PAGE_SIZE : pSize;
        
        Page<StockDetailWebPage> page = new Page<>(pNum, pSize);
        EntityWrapper<StockDetailWebPage> ew = new EntityWrapper<>();
        if(StringUtils.isNotEmpty(symbol))
        {
        	ew.eq("symbol", symbol);
        }
        Page<StockDetailWebPage> selectePage = detailPageService.selectPage(page, ew);
        
		return Rest.okData(selectePage);
	}
	
	/**
	 * 获得单只股票资金交易信息
	 * 
	 * @param symbol 股票代码
	 * @param start 开始日期
	 * @param end 截止日期
	 * @return 交易的资金数据列表
	 */
	@ResponseBody
	@RequestMapping("/capital")
	public Object getCapitalInfo(String symbol, String start, String end)
	{
		EntityWrapper<DefaultCapitalFlow> ew = new EntityWrapper<>();
		if(StringUtils.isNotEmpty(symbol))
		{
			ew.eq("symbol", symbol);
		}
		start = DateUtil.getDayString(start);
		end = DateUtil.getDayString(end);
		if(StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end))
		{
			ew.between("day", start, end);
		}
		ew.orderBy("day");
		List<DefaultCapitalFlow> flows = defaultCapitalFlowService.selectList(ew);
		
		return flows;
	}
	
	
	/**
	 * 获得所有股票资金交易信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/capitals")
	public Object getCapitalInfos(String symbol, String start, String end)
	{
		EntityWrapper<DefaultCapitalFlow> ew = new EntityWrapper<>();
		if(StringUtils.isNotEmpty(symbol))
		{
			ew.eq("symbol", symbol);
		}
		start = DateUtil.getDayString(start);
		end = DateUtil.getDayString(end);
		if(StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end))
		{
			ew.between("day", start, end);
		}
		ew.orderBy("day");
		List<DefaultCapitalFlow> flows = defaultCapitalFlowService.selectList(ew);
		System.out.println("Local System: " + flows.size());
		
		return flows;
	}
	
	/**
	 * Get the DailyRecord.
	 * 
	 * @param date 日期
	 * @param pagination 分页参数
	 * @return 分页后的数据结果
	 */
	@ResponseBody
	@RequestMapping("/dailyrecs")
	public PageWrapper<DailyRecord> getDailyRecord(String date, Pagination pagination)
	{
		int current = 1;
		int size = DEFAULT_PAGE_SIZE;
		
		if(pagination != null)
		{
			current = pagination.getCurrent();
			size = pagination.getSize();
			size = size == 0 ? DEFAULT_PAGE_SIZE : size;
		}
		
		//System.out.println("Pagination: " + pagination);
		//System.out.println("Descs:" + pagination.getDescs());
		//System.out.println("Ascs: " + pagination.getAscs());
		
		EntityWrapper<DailyRecord> ew = new EntityWrapper<>();
		if(StringUtils.isNotEmpty(date))
		{
			ew.eq("day", date);
		}
		
		if(pagination.getAscs() != null && pagination.getAscs().size() > 0)
		{
			ew.orderAsc(pagination.getAscs());
		}
		
		if(pagination.getDescs() != null && pagination.getDescs().size() > 0)
		{
			ew.orderDesc(pagination.getDescs());
		}
		
		
	    Page<DailyRecord> page = new Page<>(current, size);
	    Page<DailyRecord> selectPage = dailyRecordService.selectPage(page, ew);
		
		return new PageWrapper<>(selectPage);
	}
	
	/**
	 * 获得已经下载的日交易详细数据页列表
	 * 
	 * @param pagination 分页器
	 * @return 已经分页的数据
	 */
	@ResponseBody
	@RequestMapping("/detailpages")
	public PageWrapper<StockDetailPageInfo> getDetailPageInfos(Pagination pagination)
	{
		int current = 1;
		int size = DEFAULT_PAGE_SIZE;
		
		if(pagination != null)
		{
			current = pagination.getCurrent();
			size = pagination.getSize();
			size = size == 0 ? DEFAULT_PAGE_SIZE : size;
		}
		EntityWrapper<StockDetailPageInfo> ew = new EntityWrapper<>();
		if(pagination.getAscs() != null && pagination.getAscs().size() > 0)
		{
			ew.orderAsc(pagination.getAscs());
		}
		
		if(pagination.getDescs() != null && pagination.getDescs().size() > 0)
		{
			ew.orderDesc(pagination.getDescs());
		}
		
		Page<StockDetailPageInfo> page = new Page<>(current, size);
	    Page<StockDetailPageInfo> selectPage = stockDetailPageInfoService.selectPage(page, ew);
		
		return new PageWrapper<>(selectPage);
	}
	
}
