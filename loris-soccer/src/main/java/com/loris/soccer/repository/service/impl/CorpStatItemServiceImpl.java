package com.loris.soccer.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.bean.stat.CorpStatItem;
import com.loris.soccer.repository.mapper.CorpstatItemMapper;
import com.loris.soccer.repository.service.CorpStatItemService;

@Service("corpStatItemService")
public class CorpStatItemServiceImpl extends ServiceImpl<CorpstatItemMapper, CorpStatItem> implements CorpStatItemService
{

}
