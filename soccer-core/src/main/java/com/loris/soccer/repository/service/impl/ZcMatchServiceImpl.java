package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.data.table.lottery.ZcMatch;
import com.loris.soccer.repository.mapper.ZcMatchMapper;
import com.loris.soccer.repository.service.ZcMatchService;

@Service("zcMatchService")
public class ZcMatchServiceImpl extends ServiceImpl<ZcMatchMapper, ZcMatch> implements ZcMatchService
{

}
