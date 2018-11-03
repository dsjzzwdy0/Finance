package com.loris.soccer.web.repository.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.web.downloader.zgzcw.page.TeamWebPage;
import com.loris.soccer.web.repository.mapper.TeamWebPageMapper;
import com.loris.soccer.web.repository.service.TeamWebPageService;

@Service("teamWebPageService")
public class TeamWebPageServiceImpl extends ServiceImpl<TeamWebPageMapper, TeamWebPage> implements TeamWebPageService
{
	/**
	 * Get the DownloadedTeams.
	 * 
	 * @return
	 */
	public List<String> getDownloadedTeams()
	{
		return baseMapper.getDownloadedTeams();
	}
}
