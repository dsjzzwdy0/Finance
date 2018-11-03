package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.data.table.lottery.LotteryCalendar;
import com.loris.soccer.repository.mapper.LotteryCalendarMapper;
import com.loris.soccer.repository.service.LotteryCalendarService;

@Service("lotteryCalendarService")
public class LotteryCalendarServiceImpl extends ServiceImpl<LotteryCalendarMapper, LotteryCalendar> implements LotteryCalendarService
{

}
