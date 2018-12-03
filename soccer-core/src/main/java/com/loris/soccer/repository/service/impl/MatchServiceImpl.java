package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.data.table.Match;
import com.loris.soccer.repository.mapper.MatchMapper;
import com.loris.soccer.repository.service.MatchService;

@Service("matchService")
public class MatchServiceImpl extends ServiceImpl<MatchMapper, Match> implements MatchService
{

}
