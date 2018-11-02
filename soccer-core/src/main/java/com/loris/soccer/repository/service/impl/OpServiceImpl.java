package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.data.table.odds.Op;
import com.loris.soccer.repository.mapper.OpMapper;
import com.loris.soccer.repository.service.OpService;

@Service("opService")
public class OpServiceImpl extends ServiceImpl<OpMapper, Op> implements OpService
{

}
