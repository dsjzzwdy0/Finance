package com.loris.soccer.web.repository.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.web.downloader.zgzcw.page.OddsYpWebPage;
import com.loris.soccer.web.repository.mapper.OddsYpWebPageMapper;
import com.loris.soccer.web.repository.service.OddsYpWebPageService;

@Service("oddsYpWebPageService")
public class OddsYpWebPageServiceImpl extends ServiceImpl<OddsYpWebPageMapper, OddsYpWebPage> 
	implements OddsYpWebPageService
{
	/**
	 * 查找已经下载的数据
	 * 
	 * @return
	 */
	public List<OddsYpWebPage> getDownloadedPages()
	{
		return baseMapper.getDownloadedPages();
	}
}
