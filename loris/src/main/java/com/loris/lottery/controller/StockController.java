package com.loris.lottery.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.wrapper.PageWrapper;
import com.loris.base.util.DateUtil;
import com.loris.stock.bean.data.table.capital.DefaultCapitalFlow;
import com.loris.stock.bean.item.DetailedItem;
import com.loris.stock.bean.model.StockDayDetailList;
import com.loris.stock.repository.service.DefaultCapitalFlowService;
import com.loris.stock.web.downloader.sina.parser.WebContentParser;
import com.loris.stock.web.page.StockDetailWebPage;
import com.loris.stock.web.repository.service.DetailPageService;

@Controller
@RequestMapping("/stock")
public class StockController
{
	@Autowired
	private DetailPageService detailPageService;
	
	@Autowired
	private DefaultCapitalFlowService defaultCapitalFlowService;
	
	/**
	 * Get the page Record.
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param symbol
	 * @param day
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/detail")
	public PageWrapper<DetailedItem> getDetailRecord(String pageNumber, String pageSize, String symbol, String day)
	{
		if(!StringUtils.isNotEmpty(pageNumber) & !StringUtils.isNotEmpty(pageSize))
		{
            pageNumber= "1";
            pageSize= "100";
        }
        //分页 pageNumber--》页数    pageSize--》每页显示数据的条数
        int pNum = Integer.parseInt(pageNumber);
        int pSize = Integer.parseInt(pageSize);
        
        EntityWrapper<StockDetailWebPage> ew = new EntityWrapper<>();
        ew.eq("symbol", symbol);
        ew.eq("createtime", day);
        List<StockDetailWebPage> pages = detailPageService.selectList(ew);
        
        Page<DetailedItem> pageWrapper = new Page<DetailedItem>(pNum, pSize);
        
        if(pages.size() == 0)
        {
        	pageWrapper.setTotal(0);
        	pageWrapper.setRecords(null);
        }
        else
        {
        	StockDetailWebPage page = pages.get(0);
        	StockDayDetailList detailList = WebContentParser.parseDetailWebPage(page);
        	if(detailList != null && detailList.getRecords() != null)
        	{
        		List<DetailedItem> records = detailList.getRecords();
        		int total = records.size();
        		pageWrapper.setTotal(total);
        		List<DetailedItem> rs = new ArrayList<DetailedItem>();
        		int start = (pNum - 1) * pSize;
        		int end = Math.min(total, start + pSize);
        		for(int i = start; i < end; i ++)
        		{
        			rs.add(records.get(i));
        		}
        		pageWrapper.setRecords(rs);
        	}
        	else
        	{
        		pageWrapper.setTotal(0);
            	pageWrapper.setRecords(null);
        	}
        }
		
		return new PageWrapper<DetailedItem>(pageWrapper);
	}
	
	
	/**
	 * 获得信息
	 * 
	 * @param symbol
	 * @param start
	 * @param end
	 * @return
	 */
	@RequestMapping("/page")
	public ModelAndView getCapitalsView(String symbol, String start, String end)
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
		ModelAndView view = new ModelAndView("stock/page");
		view.addObject("page", flows);
		
		return view;
	}
}
