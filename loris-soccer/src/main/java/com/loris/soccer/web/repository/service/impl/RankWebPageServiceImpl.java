package com.loris.soccer.web.repository.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.web.downloader.zgzcw.page.RankWebPage;
import com.loris.soccer.web.repository.mapper.RankWebPageMapper;
import com.loris.soccer.web.repository.service.RankWebPageService;

@Service("rankWebPageService")
public class RankWebPageServiceImpl extends ServiceImpl<RankWebPageMapper, RankWebPage> implements RankWebPageService
{
	public List<RankWebPage> getDownloadedRanks()
	{
		return baseMapper.getDownloadedRanks();
	}
}
