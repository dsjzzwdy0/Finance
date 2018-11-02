package com.loris.base.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.base.bean.Log;
import com.loris.base.repository.mapper.LogMapper;
import com.loris.base.repository.service.LogService;

@Service("logService")
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService
{

}
