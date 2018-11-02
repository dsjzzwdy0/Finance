package com.loris.stock.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.stock.bean.data.table.TradeDate;
import com.loris.stock.repository.mapper.TradeDateMapper;
import com.loris.stock.repository.service.TradeDateService;

@Service("tradeDateService")
public class TradeDateServiceImpl extends ServiceImpl<TradeDateMapper, TradeDate> implements TradeDateService
{

}
