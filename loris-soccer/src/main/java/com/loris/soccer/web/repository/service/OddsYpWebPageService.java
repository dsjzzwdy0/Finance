package com.loris.soccer.web.repository.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.loris.soccer.web.downloader.zgzcw.page.OddsYpWebPage;

public interface OddsYpWebPageService extends IService<OddsYpWebPage>
{
	/**
	 * 查找已经下载的数据
	 * 
	 * @return
	 */
	List<OddsYpWebPage> getDownloadedPages();
}
