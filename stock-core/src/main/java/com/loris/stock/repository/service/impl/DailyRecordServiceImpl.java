package com.loris.stock.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.stock.bean.data.table.DailyRecord;
import com.loris.stock.repository.mapper.DailyRecordMapper;
import com.loris.stock.repository.service.DailyRecordService;

@Service("dailyRecordService")
public class DailyRecordServiceImpl extends ServiceImpl<DailyRecordMapper, DailyRecord> implements DailyRecordService
{

}
