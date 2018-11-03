package com.loris.stock.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.stock.bean.data.table.capital.DetailCapitalFlow;
import com.loris.stock.repository.mapper.DetailCapitalMapper;
import com.loris.stock.repository.service.DetailCapitalService;

@Service("detailCapitalService")
public class DetailCapitalServiceImpl extends ServiceImpl<DetailCapitalMapper, DetailCapitalFlow> implements DetailCapitalService
{

}
