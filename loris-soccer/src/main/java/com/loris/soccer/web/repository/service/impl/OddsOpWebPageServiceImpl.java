package com.loris.soccer.web.repository.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.web.downloader.zgzcw.page.OddsOpWebPage;
import com.loris.soccer.web.repository.mapper.OddsOpWebPageMapper;
import com.loris.soccer.web.repository.service.OddsOpWebPageService;

@Service("oddsOpWebPageService")
public class OddsOpWebPageServiceImpl extends ServiceImpl<OddsOpWebPageMapper, OddsOpWebPage>
	implements OddsOpWebPageService
{
	/**
	 * 查找已经下载的数据
	 * 
	 * @return
	 */
	public List<OddsOpWebPage> getDownloadedPages()
	{
		return baseMapper.getDownloadedPages();
	}
}
