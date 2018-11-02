package com.loris.soccer.web.repository.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.loris.soccer.web.downloader.zgzcw.page.RoundLeagueWebPage;

public interface RoundLeagueWebPageService extends IService<RoundLeagueWebPage>
{
	/**
	 * 获取已经下载的数据
	 * 
	 * @return
	 */
	List<RoundLeagueWebPage> getDownloadedRounds();
}
