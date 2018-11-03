package com.loris.soccer.web.repository.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.loris.soccer.web.downloader.zgzcw.page.LotteryWebPage;

public interface LotteryWebPageService extends IService<LotteryWebPage>
{
	/**
	 * 获取已经下载的页面
	 * 
	 * @return 下载的数据列表
	 */
	List<LotteryWebPage> getDownloadedPages(String type);
}
