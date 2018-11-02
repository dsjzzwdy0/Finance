package com.loris.soccer.web.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.web.page.SimpleOddsOpWebPage;
import com.loris.soccer.web.repository.mapper.SimpleOddsOpWebPageMapper;
import com.loris.soccer.web.repository.service.SimpleOddsOpWebPageService;

@Service("simpleOddsOpWebPageService")
public class SimpleOddsOpWebPageServiceImpl extends ServiceImpl<SimpleOddsOpWebPageMapper, SimpleOddsOpWebPage>
	implements SimpleOddsOpWebPageService
{

}
