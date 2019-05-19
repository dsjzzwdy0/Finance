package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.table.Season;
import com.loris.soccer.repository.mapper.SeasonMapper;
import com.loris.soccer.repository.service.SeasonService;

@Service("seasonService")
public class SeasonServiceImpl extends ServiceImpl<SeasonMapper, Season> implements SeasonService
{

}
