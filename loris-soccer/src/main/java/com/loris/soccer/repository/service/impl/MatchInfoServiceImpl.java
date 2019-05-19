package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.view.MatchInfo;
import com.loris.soccer.repository.mapper.MatchInfoMapper;
import com.loris.soccer.repository.service.MatchInfoService;

@Service("matchInfoService")
public class MatchInfoServiceImpl extends ServiceImpl<MatchInfoMapper, MatchInfo> implements MatchInfoService
{

}
