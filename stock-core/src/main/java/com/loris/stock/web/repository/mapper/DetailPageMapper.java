package com.loris.stock.web.repository.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.loris.stock.web.page.StockDetailWebPage;

public interface DetailPageMapper extends BaseMapper<StockDetailWebPage>
{
	/**
	 * Get the DetailPageInfos.
	 * 
	 * @param day
	 * @return
	 */
	List<StockDetailWebPage> selectDetailPageInfo(String day);
	
	/**
	 * Get the download days list.
	 * 
	 * @return
	 */
	List<String> getDownloadDays();
}
