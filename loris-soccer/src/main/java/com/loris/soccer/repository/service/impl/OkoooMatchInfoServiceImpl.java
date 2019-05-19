package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.okooo.OkoooMatchInfo;
import com.loris.soccer.repository.mapper.OkoooMatchInfoMapper;
import com.loris.soccer.repository.service.OkoooMatchInfoService;

@Service("okoooMatchInfoService")
public class OkoooMatchInfoServiceImpl extends ServiceImpl<OkoooMatchInfoMapper, OkoooMatchInfo> implements OkoooMatchInfoService
{

}
