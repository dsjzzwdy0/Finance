package com.loris.stock.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.stock.bean.data.table.StockInfo;
import com.loris.stock.repository.mapper.StockInfoMapper;
import com.loris.stock.repository.service.StockInfoService;

@Service("stockInfoService")
public class StockInfoServiceImpl extends ServiceImpl<StockInfoMapper, StockInfo> implements StockInfoService
{

}
