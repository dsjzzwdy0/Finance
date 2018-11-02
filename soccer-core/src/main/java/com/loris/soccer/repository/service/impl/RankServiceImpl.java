package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.data.table.league.Rank;
import com.loris.soccer.repository.mapper.RankMapper;
import com.loris.soccer.repository.service.RankService;

@Service("rankService")
public class RankServiceImpl extends ServiceImpl<RankMapper, Rank> implements RankService
{

}
