package com.loris.stock.web.repository.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.loris.stock.web.page.StockDetailWebPage;

public interface DetailPageService extends IService<StockDetailWebPage>
{
	/**
	 * Get the DetailPageInfo.
	 * @param day
	 * @return
	 */
	List<StockDetailWebPage> getDetailPageInfoByDay(String day);
	
	/**
	 * Get the download days list.
	 * 
	 * @return
	 */
	List<String> getDownloadDays();
}
