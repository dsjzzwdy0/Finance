package com.loris.soccer.web.repository.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.loris.soccer.web.downloader.zgzcw.page.TeamWebPage;

public interface TeamWebPageMapper extends BaseMapper<TeamWebPage>
{
	/**
	 * Get the DownloadedTeams.
	 * 
	 * @return
	 */
	List<String> getDownloadedTeams();
}
