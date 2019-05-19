package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.table.UserCorporate;
import com.loris.soccer.repository.mapper.UserCorporateMapper;
import com.loris.soccer.repository.service.UserCorporateService;

@Service("userCoporateService")
public class UserCorporateServiceImpl extends ServiceImpl<UserCorporateMapper, UserCorporate> implements UserCorporateService
{

}
