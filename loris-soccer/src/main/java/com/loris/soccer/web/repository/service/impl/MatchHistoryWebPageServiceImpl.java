package com.loris.soccer.web.repository.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.web.downloader.zgzcw.page.MatchHistoryWebPage;
import com.loris.soccer.web.repository.mapper.MatchHistoryWebPageMapper;
import com.loris.soccer.web.repository.service.MatchHistoryWebPageService;

@Service("matchHistoryWebPageService")
public class MatchHistoryWebPageServiceImpl 
	extends ServiceImpl<MatchHistoryWebPageMapper, MatchHistoryWebPage>
	implements MatchHistoryWebPageService
{
	/**
	 * 获得已经下载的页面
	 * 
	 * @return 已经下载的页面数据
	 */
	public List<MatchHistoryWebPage> getDownloadedPages()
	{
		return baseMapper.getDownloadedPages();
	}
}
