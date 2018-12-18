package com.loris.soccer.web.repository.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.loris.soccer.web.downloader.zgzcw.page.TeamWebPage;

public interface TeamWebPageService extends IService<TeamWebPage>
{
	/**
	 * Get the DownloadedTeams.
	 * 
	 * @return
	 */
	List<String> getDownloadedTeams();
}
