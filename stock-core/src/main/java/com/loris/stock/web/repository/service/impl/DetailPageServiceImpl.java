package com.loris.stock.web.repository.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.stock.web.page.StockDetailWebPage;
import com.loris.stock.web.repository.mapper.DetailPageMapper;
import com.loris.stock.web.repository.service.DetailPageService;

@Service("detailPageService")
public class DetailPageServiceImpl extends ServiceImpl<DetailPageMapper, StockDetailWebPage> implements DetailPageService
{
	/**
	 * Get the DetailPageInfo.
	 * @param day
	 * @return
	 */
	public List<StockDetailWebPage> getDetailPageInfoByDay(String day)
	{
		return baseMapper.selectDetailPageInfo(day);
	}
	
	/**
	 * Get the download days list.
	 * 
	 * @return
	 */
	public List<String> getDownloadDays()
	{
		return baseMapper.getDownloadDays();
	}
}
