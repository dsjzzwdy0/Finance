package com.loris.soccer.web.repository.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.loris.soccer.web.downloader.zgzcw.page.MatchHistoryWebPage;

public interface MatchHistoryWebPageService extends IService<MatchHistoryWebPage>
{
	/**
	 * 获得已经下载的页面
	 * 
	 * @return 已经下载的页面数据
	 */
	List<MatchHistoryWebPage> getDownloadedPages();
}
