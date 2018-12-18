package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.stat.MatchCorpProb;
import com.loris.soccer.repository.mapper.MatchCorpProbMapper;
import com.loris.soccer.repository.service.MatchCorpProbService;

@Service("matchCorpProbService")
public class MatchCorpProbServiceImpl extends ServiceImpl<MatchCorpProbMapper, MatchCorpProb> implements MatchCorpProbService
{

}
