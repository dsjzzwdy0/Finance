package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.data.table.league.SeasonTeam;
import com.loris.soccer.repository.mapper.SeasonTeamMapper;
import com.loris.soccer.repository.service.SeasonTeamService;

@Service("seasonTeamService")
public class SeasonTeamServiceImpl extends ServiceImpl<SeasonTeamMapper, SeasonTeam> implements SeasonTeamService
{

}
