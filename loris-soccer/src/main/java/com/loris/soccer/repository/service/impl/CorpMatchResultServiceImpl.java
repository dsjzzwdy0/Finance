package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.stat.CorpMatchResult;
import com.loris.soccer.repository.mapper.CorpMatchResultMapper;
import com.loris.soccer.repository.service.CorpMatchResultService;

@Service("corpMatchResultService")
public class CorpMatchResultServiceImpl extends ServiceImpl<CorpMatchResultMapper, CorpMatchResult> implements CorpMatchResultService
{

}
