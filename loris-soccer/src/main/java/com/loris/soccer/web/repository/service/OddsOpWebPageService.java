package com.loris.soccer.web.repository.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.loris.soccer.web.downloader.zgzcw.page.OddsOpWebPage;

public interface OddsOpWebPageService extends IService<OddsOpWebPage>
{
	/**
	 * 查找已经下载的数据
	 * 
	 * @return
	 */
	List<OddsOpWebPage> getDownloadedPages();
}
