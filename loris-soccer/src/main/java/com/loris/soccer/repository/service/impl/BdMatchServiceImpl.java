package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.table.BdMatch;
import com.loris.soccer.repository.mapper.BdMatchMapper;
import com.loris.soccer.repository.service.BdMatchService;

@Service("bdMatchService")
public class BdMatchServiceImpl extends ServiceImpl<BdMatchMapper, BdMatch> implements BdMatchService
{

}
