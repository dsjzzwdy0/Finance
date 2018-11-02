package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.setting.CorpSetting;
import com.loris.soccer.repository.mapper.CorpSettingMapper;
import com.loris.soccer.repository.service.CorpSettingService;

@Service("corpSettingService")
public class CorpSettingServiceImpl extends ServiceImpl<CorpSettingMapper, CorpSetting> implements CorpSettingService
{

}
