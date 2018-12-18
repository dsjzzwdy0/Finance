package com.loris.soccer.web.repository.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.loris.soccer.web.downloader.zgzcw.page.MatchHistoryWebPage;

public interface MatchHistoryWebPageMapper extends BaseMapper<MatchHistoryWebPage>
{
	/**
	 * 获得已经下载的页面
	 * 
	 * @return 已经下载的页面数据
	 */
	List<MatchHistoryWebPage> getDownloadedPages();
}
