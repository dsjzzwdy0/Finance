package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.view.RankInfo;
import com.loris.soccer.repository.mapper.RankInfoMapper;
import com.loris.soccer.repository.service.RankInfoService;

@Service("rankInfoService")
public class RankInfoServiceImpl extends ServiceImpl<RankInfoMapper, RankInfo> implements RankInfoService
{

}
