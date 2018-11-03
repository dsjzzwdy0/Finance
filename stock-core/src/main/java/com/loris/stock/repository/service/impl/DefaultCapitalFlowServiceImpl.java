package com.loris.stock.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.stock.bean.data.table.capital.DefaultCapitalFlow;
import com.loris.stock.repository.mapper.DefaultCapitalFlowMapper;
import com.loris.stock.repository.service.DefaultCapitalFlowService;

@Service("defaultCapitalFlowService")
public class DefaultCapitalFlowServiceImpl extends ServiceImpl<DefaultCapitalFlowMapper, DefaultCapitalFlow>
	implements DefaultCapitalFlowService
{
}
