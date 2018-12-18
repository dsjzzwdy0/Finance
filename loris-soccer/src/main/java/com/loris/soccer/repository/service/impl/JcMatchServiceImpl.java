package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.table.JcMatch;
import com.loris.soccer.repository.mapper.JcMatchMapper;
import com.loris.soccer.repository.service.JcMatchService;

@Service("jcMatchService")
public class JcMatchServiceImpl extends ServiceImpl<JcMatchMapper, JcMatch> implements JcMatchService
{

}
