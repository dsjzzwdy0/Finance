package com.loris.soccer.web.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.web.downloader.zgzcw.page.LeagueWebPage;
import com.loris.soccer.web.repository.mapper.LeagueWebPageMapper;
import com.loris.soccer.web.repository.service.LeagueWebPageService;

@Service("leagueWebPageService")
public class LeagueWebPageServiceImpl extends ServiceImpl<LeagueWebPageMapper, LeagueWebPage>
	implements LeagueWebPageService
{

}
