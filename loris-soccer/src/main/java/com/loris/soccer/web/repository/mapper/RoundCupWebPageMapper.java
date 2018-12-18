package com.loris.soccer.web.repository.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.loris.soccer.web.downloader.zgzcw.page.RoundCupWebPage;

public interface RoundCupWebPageMapper extends BaseMapper<RoundCupWebPage>
{
	/**
	 * 获得已经下载的数据
	 * 
	 * @return
	 */
	List<RoundCupWebPage> getDownloadedRounds();
}
