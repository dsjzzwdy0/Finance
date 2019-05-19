package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.okooo.OkoooBdMatch;
import com.loris.soccer.repository.mapper.OkoooBdMatchMapper;
import com.loris.soccer.repository.service.OkoooBdMatchService;

@Service("okoooBdMatchService")
public class OkoooBdMatchServiceImpl extends ServiceImpl<OkoooBdMatchMapper, OkoooBdMatch>
	implements OkoooBdMatchService
{

}
