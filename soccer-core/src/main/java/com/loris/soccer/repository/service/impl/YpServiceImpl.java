package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.data.table.Yp;
import com.loris.soccer.repository.mapper.YpMapper;
import com.loris.soccer.repository.service.YpService;

@Service("ypService")
public class YpServiceImpl extends ServiceImpl<YpMapper, Yp> implements YpService
{

}
