package com.loris.soccer.web.repository.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.web.downloader.zgzcw.page.OddsOpZhishuWebPage;
import com.loris.soccer.web.repository.mapper.OddsOpZhishuWebPageMapper;
import com.loris.soccer.web.repository.service.OddsOpZhishuWebPageService;

@Service("oddsOpZhishuWebPageService")
public class OddsOpZhishuWebPageServiceImpl extends ServiceImpl<OddsOpZhishuWebPageMapper, OddsOpZhishuWebPage>
	implements OddsOpZhishuWebPageService
{
	/**
	 * 获得欧赔指数据已经下载的数据 
	 * 
	 * @return 欧赔指数据下载数据列表
	 */
	public List<OddsOpZhishuWebPage> getDownloadedPages()
	{
		return baseMapper.getDownloadedPages();
	}
}
