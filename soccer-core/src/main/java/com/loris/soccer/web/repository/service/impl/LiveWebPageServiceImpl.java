package com.loris.soccer.web.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.web.downloader.zgzcw.page.LiveWebPage;
import com.loris.soccer.web.repository.mapper.LiveWebPageMapper;
import com.loris.soccer.web.repository.service.LiveWebPageService;

@Service("liveWebPageService")
public class LiveWebPageServiceImpl extends ServiceImpl<LiveWebPageMapper, LiveWebPage> implements LiveWebPageService
{

}
