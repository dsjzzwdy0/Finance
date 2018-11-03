package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.data.table.league.Round;
import com.loris.soccer.repository.mapper.RoundMapper;
import com.loris.soccer.repository.service.RoundService;

@Service("roundService")
public class RoundServiceImpl extends ServiceImpl<RoundMapper, Round> implements RoundService
{

}
