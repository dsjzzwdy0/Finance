package com.loris.soccer.web.repository.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.web.downloader.zgzcw.page.LotteryWebPage;
import com.loris.soccer.web.repository.mapper.LotteryWebPageMapper;
import com.loris.soccer.web.repository.service.LotteryWebPageService;

@Service("lotteryWebPageService")
public class LotteryWebPageServiceImpl extends ServiceImpl<LotteryWebPageMapper, LotteryWebPage> 
	implements LotteryWebPageService
{

	/**
	 * 获取已经下载的页面
	 * 
	 * @return 下载的数据列表
	 */
	public List<LotteryWebPage> getDownloadedPages(String sqlwhere)
	{
		return baseMapper.getDownloadedPages(sqlwhere);
	}
}
