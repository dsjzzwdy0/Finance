package com.loris.stock.web.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.web.repository.WebPageManager;
import com.loris.stock.web.page.StockDailyWebPage;
import com.loris.stock.web.page.StockDetailWebPage;
import com.loris.stock.web.repository.io.StockWebPageContentIO;
import com.loris.stock.web.repository.service.DailyPageService;
import com.loris.stock.web.repository.service.DetailPageService;

@Component
public class StockPageManager extends WebPageManager
{
	@Autowired
	private DetailPageService detailPageService;
	
	@Autowired
	private DailyPageService dailyPageService;
	
	/**
	 * Create a new instance of StockPageManager.
	 */
	public StockPageManager()
	{
	}

	/**
	 * Get the DetailWebPage List.
	 * 
	 * @param day
	 * @return
	 */
	public List<StockDetailWebPage> getDetailPageInfos(String day)
	{
		return detailPageService.getDetailPageInfoByDay(day);
	}
	
	/**
	 * 查找所有的页面。
	 * 
	 * @param day
	 * @return
	 */
	public List<StockDetailWebPage> getDetailPages(String day)
	{
		EntityWrapper<StockDetailWebPage> ew = new EntityWrapper<>();
		ew.eq("day", day);
		return detailPageService.selectList(ew);
	}
	
	/**
	 * Get the DetailDownload days.
	 * 
	 * @return
	 */
	public List<String> getDetailDownloadDays()
	{
		return detailPageService.getDownloadDays();
	}
	
	/**
	 * Read the StockDetailPage Content.
	 * 
	 * @param page DetailPage.
	 * @return Flag.
	 */
	public boolean getStockDetailPageContent(StockDetailWebPage page)
	{
		return StockWebPageContentIO.readWebPageContent(page);
	}
	
	/**
	 * add the DetailWebPage info.
	 * 
	 * @param page
	 * @return
	 */
	public boolean updateOrAddDetailWebPage(StockDetailWebPage page)
	{
		if(StringUtils.isEmpty(page.getId()))
		{
			try
			{
				StockWebPageContentIO.writeWebPageContent(page);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return detailPageService.insert(page);
		}
		else
		{
			try
			{
				StockWebPageContentIO.writeWebPageContent(page);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			EntityWrapper<StockDetailWebPage> ew = new EntityWrapper<>();
			ew.eq("id", page.getId());
			return detailPageService.update(page, ew);
		}
	}
	
	/**
	 * Update or add the daily web page.
	 * 
	 * @param page
	 * @return
	 */
	public boolean updateOrAddDailyWebPage(StockDailyWebPage page)
	{
		if(StringUtils.isEmpty(page.getId()))
		{
			return dailyPageService.insert(page);
		}
		else
		{
			EntityWrapper<StockDailyWebPage> ew = new EntityWrapper<>();
			ew.eq("id", page.getId());
			return dailyPageService.update(page, ew);
		}
	}
	
	/**
	 * Get the StockDailyPages.
	 * 
	 * @param day
	 * @return
	 */
	public List<StockDailyWebPage> getStockDailyPages(String day)
	{
		EntityWrapper<StockDailyWebPage> ew = new EntityWrapper<>();
		ew.eq("createtime", day);
		return dailyPageService.selectList(ew);
	}
	
}
