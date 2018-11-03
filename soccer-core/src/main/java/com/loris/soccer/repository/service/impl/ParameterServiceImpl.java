package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.setting.Parameter;
import com.loris.soccer.repository.mapper.ParameterMapper;
import com.loris.soccer.repository.service.ParameterService;

@Service("parameterService")
public class ParameterServiceImpl extends ServiceImpl<ParameterMapper, Parameter> implements ParameterService
{

}
