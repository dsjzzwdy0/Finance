package com.loris.soccer.web.repository.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.web.downloader.zgzcw.page.RoundCupWebPage;
import com.loris.soccer.web.repository.mapper.RoundCupWebPageMapper;
import com.loris.soccer.web.repository.service.RoundCupWebPageService;

@Service("roundCupWebPageService")
public class RoundCupWebPageServiceImpl extends ServiceImpl<RoundCupWebPageMapper, RoundCupWebPage> implements RoundCupWebPageService
{
	/**
	 * 获得已经下载的数据
	 * 
	 * @return
	 */
	public List<RoundCupWebPage> getDownloadedRounds()
	{
		return baseMapper.getDownloadedRounds();
	}
}
