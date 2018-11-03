package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.data.table.league.Team;
import com.loris.soccer.repository.mapper.TeamMapper;
import com.loris.soccer.repository.service.TeamService;

@Service("teamService")
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService
{

}
