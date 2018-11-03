package com.loris.stock.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.stock.repository.mapper.BaseInfoPageMapper;
import com.loris.stock.repository.service.BaseInfoPageService;
import com.loris.stock.web.page.StockBaseInfoWebPage;

@Service("baseInfoPageService")
public class BaseInfoPageServiceImpl extends ServiceImpl<BaseInfoPageMapper, StockBaseInfoWebPage> 
	implements BaseInfoPageService
{

}
