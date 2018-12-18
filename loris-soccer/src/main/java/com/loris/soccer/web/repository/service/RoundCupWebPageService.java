package com.loris.soccer.web.repository.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.loris.soccer.web.downloader.zgzcw.page.RoundCupWebPage;

public interface RoundCupWebPageService extends IService<RoundCupWebPage>
{
	/**
	 * 获得已经下载的数据
	 * 
	 * @return
	 */
	List<RoundCupWebPage> getDownloadedRounds();
}
