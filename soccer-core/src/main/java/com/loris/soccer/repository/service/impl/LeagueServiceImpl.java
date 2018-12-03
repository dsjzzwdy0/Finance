package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.data.table.League;
import com.loris.soccer.repository.mapper.LeagueMapper;
import com.loris.soccer.repository.service.LeagueService;

@Service("leagueService")
public class LeagueServiceImpl extends ServiceImpl<LeagueMapper, League> implements LeagueService
{

}
