package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.okooo.OkoooJcMatch;
import com.loris.soccer.repository.mapper.OkoooJcMatchMapper;
import com.loris.soccer.repository.service.OkoooJcMatchService;

@Service("okoooJcMatchService")
public class OkoooJcMatchServiceImpl extends ServiceImpl<OkoooJcMatchMapper, OkoooJcMatch>
	implements OkoooJcMatchService
{

}
