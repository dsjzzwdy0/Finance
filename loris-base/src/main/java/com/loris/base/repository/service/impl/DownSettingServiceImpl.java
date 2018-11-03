package com.loris.base.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.base.repository.mapper.DownSettingMapper;
import com.loris.base.repository.service.DownSettingService;
import com.loris.base.web.config.setting.DownSetting;

@Service("downSettingService")
public class DownSettingServiceImpl extends ServiceImpl<DownSettingMapper, DownSetting> implements DownSettingService
{

}
