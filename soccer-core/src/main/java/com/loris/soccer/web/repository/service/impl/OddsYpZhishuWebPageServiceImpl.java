package com.loris.soccer.web.repository.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.web.downloader.zgzcw.page.OddsYpZhishuWebPage;
import com.loris.soccer.web.repository.mapper.OddsYpZhishuWebPageMapper;
import com.loris.soccer.web.repository.service.OddsYpZhishuWebPageService;

@Service("oddsYpZhishuWebPageService")
public class OddsYpZhishuWebPageServiceImpl extends ServiceImpl<OddsYpZhishuWebPageMapper, OddsYpZhishuWebPage>
	implements OddsYpZhishuWebPageService
{
	/**
	 * 获得已经下载的亚盘指数据页数据列表
	 * 
	 * @return 数据页列表
	 */
	public List<OddsYpZhishuWebPage> getDownloadedPages()
	{
		return baseMapper.getDownloadedPages();
	}
}
