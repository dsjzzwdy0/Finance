package com.loris.soccer.web.repository.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.web.downloader.zgzcw.page.RoundLeagueWebPage;
import com.loris.soccer.web.repository.mapper.RoundLeagueWebPageMapper;
import com.loris.soccer.web.repository.service.RoundLeagueWebPageService;

@Service("roundLeagueWebPageService")
public class RoundLeagueWebPageServiceImpl extends ServiceImpl<RoundLeagueWebPageMapper, RoundLeagueWebPage>
	implements RoundLeagueWebPageService
{
	/**
	 * Get the downloaded pages.
	 * 
	 * @return 已经下载的数据 
	 */
	public List<RoundLeagueWebPage> getDownloadedRounds()
	{
		return baseMapper.getDownloadedRounds();
	}
}
