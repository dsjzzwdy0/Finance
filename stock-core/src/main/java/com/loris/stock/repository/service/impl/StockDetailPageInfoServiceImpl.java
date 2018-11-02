package com.loris.stock.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.stock.bean.data.view.StockDetailPageInfo;
import com.loris.stock.repository.mapper.StockDetailPageInfoMapper;
import com.loris.stock.repository.service.StockDetailPageInfoService;

@Service("stockDetailPageInfoService")
public class StockDetailPageInfoServiceImpl extends ServiceImpl<StockDetailPageInfoMapper, StockDetailPageInfo>
	implements StockDetailPageInfoService
{

}
