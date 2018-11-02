package com.loris.soccer.web.repository.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.loris.soccer.web.downloader.zgzcw.page.OddsOpZhishuWebPage;

public interface OddsOpZhishuWebPageMapper extends BaseMapper<OddsOpZhishuWebPage>
{
	/**
	 * 获得欧赔指数据已经下载的数据 
	 * 
	 * @return 欧赔指数据下载数据列表
	 */
	List<OddsOpZhishuWebPage> getDownloadedPages();
}
