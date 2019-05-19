package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.okooo.OkoooOp;
import com.loris.soccer.repository.mapper.OkoooOpMapper;
import com.loris.soccer.repository.service.OkoooOpService;

@Service("okoooOpService")
public class OkoooOpServiceImpl extends ServiceImpl<OkoooOpMapper, OkoooOp> implements OkoooOpService
{

}
