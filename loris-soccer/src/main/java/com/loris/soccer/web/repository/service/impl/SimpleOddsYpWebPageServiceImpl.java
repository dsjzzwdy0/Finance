package com.loris.soccer.web.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.web.page.SimpleOddsYpWebPage;
import com.loris.soccer.web.repository.mapper.SimpleOddsYpWebPageMapper;
import com.loris.soccer.web.repository.service.SimpleOddsYpWebPageService;

@Service("simpleOddsYpWebPageService")
public class SimpleOddsYpWebPageServiceImpl 
	extends	ServiceImpl<SimpleOddsYpWebPageMapper, SimpleOddsYpWebPage>
	implements SimpleOddsYpWebPageService
{

}
