package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.okooo.OkoooMatch;
import com.loris.soccer.repository.mapper.OkoooMatchMapper;
import com.loris.soccer.repository.service.OkoooMatchService;

@Service("okoooMatchService")
public class OkoooMatchServiceImpl extends ServiceImpl<OkoooMatchMapper, OkoooMatch> implements OkoooMatchService
{

}
