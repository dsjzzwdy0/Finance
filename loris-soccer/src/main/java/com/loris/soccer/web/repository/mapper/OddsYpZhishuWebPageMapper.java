package com.loris.soccer.web.repository.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.loris.soccer.web.downloader.zgzcw.page.OddsYpZhishuWebPage;

public interface OddsYpZhishuWebPageMapper extends BaseMapper<OddsYpZhishuWebPage>
{
	/**
	 * 获得已经下载的亚盘指数据页数据列表
	 * 
	 * @return 数据页列表
	 */
	List<OddsYpZhishuWebPage> getDownloadedPages();
}
