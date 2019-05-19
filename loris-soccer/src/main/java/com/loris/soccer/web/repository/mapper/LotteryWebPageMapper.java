package com.loris.soccer.web.repository.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.loris.soccer.web.downloader.zgzcw.page.LotteryWebPage;

public interface LotteryWebPageMapper extends BaseMapper<LotteryWebPage>
{
	/**
	 * 获取已经下载的页面
	 * 
	 * @return 下载的数据列表
	 */
	List<LotteryWebPage> getDownloadedPages(@Param("type") String type);
}
