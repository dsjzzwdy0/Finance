package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.data.table.Corporate;
import com.loris.soccer.repository.mapper.CorporateMapper;
import com.loris.soccer.repository.service.CorporateService;

@Service("corporateService")
public class CorporateServiceImpl extends ServiceImpl<CorporateMapper, Corporate> implements CorporateService
{

}
