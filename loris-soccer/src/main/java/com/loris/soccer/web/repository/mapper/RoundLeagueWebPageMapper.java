package com.loris.soccer.web.repository.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.loris.soccer.web.downloader.zgzcw.page.RoundLeagueWebPage;

public interface RoundLeagueWebPageMapper extends BaseMapper<RoundLeagueWebPage>
{
	/**
	 * 获取已经下载的数据
	 * 
	 * @return
	 */
	List<RoundLeagueWebPage> getDownloadedRounds();
}
