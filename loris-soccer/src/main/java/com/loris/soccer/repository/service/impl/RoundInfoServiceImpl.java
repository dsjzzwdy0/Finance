package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.view.RoundInfo;
import com.loris.soccer.repository.mapper.RoundInfoMapper;
import com.loris.soccer.repository.service.RoundInfoService;

@Service("roundInfoService")
public class RoundInfoServiceImpl extends ServiceImpl<RoundInfoMapper, RoundInfo> implements RoundInfoService
{

}
