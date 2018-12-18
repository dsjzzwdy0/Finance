package com.loris.soccer.web.repository.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.web.downloader.zgzcw.page.SeasonWebPage;
import com.loris.soccer.web.repository.mapper.SeasonWebPageMapper;
import com.loris.soccer.web.repository.service.SeasonWebPageService;

@Service("seasonWebPageService")
public class SeasonWebPageServiceImpl extends ServiceImpl<SeasonWebPageMapper, SeasonWebPage>
	implements SeasonWebPageService
{
	public List<SeasonWebPage> getDownloadedSeasons()
	{
		return baseMapper.getDownloadedSeasons();
	}
}
