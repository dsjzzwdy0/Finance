package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.table.Logo;
import com.loris.soccer.repository.mapper.LogoMapper;
import com.loris.soccer.repository.service.LogoService;

@Service("logoService")
public class LogoServiceImpl extends ServiceImpl<LogoMapper, Logo> implements LogoService
{

}
