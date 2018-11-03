package com.loris.stock.web.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.stock.web.page.StockDailyWebPage;
import com.loris.stock.web.repository.mapper.DailyPageMapper;
import com.loris.stock.web.repository.service.DailyPageService;

@Service("dailyPageService")
public class DailyPageServiceImpl extends ServiceImpl<DailyPageMapper, StockDailyWebPage> implements DailyPageService
{

}
