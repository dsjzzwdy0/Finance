package com.loris.soccer.web.repository.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.loris.soccer.web.downloader.zgzcw.page.OddsYpZhishuWebPage;

public interface OddsYpZhishuWebPageService extends IService<OddsYpZhishuWebPage>
{
	/**
	 * 获得已经下载的亚盘指数据页数据列表
	 * 
	 * @return 数据页列表
	 */
	List<OddsYpZhishuWebPage> getDownloadedPages();
}
