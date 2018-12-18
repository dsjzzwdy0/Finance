package com.loris.soccer.web.repository.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.loris.soccer.web.downloader.zgzcw.page.OddsOpWebPage;

public interface OddsOpWebPageMapper extends BaseMapper<OddsOpWebPage>
{
	/**
	 * 查找已经下载的数据
	 * 
	 * @return
	 */
	List<OddsOpWebPage> getDownloadedPages();
}
