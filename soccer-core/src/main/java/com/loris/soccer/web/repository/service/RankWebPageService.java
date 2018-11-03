package com.loris.soccer.web.repository.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.loris.soccer.web.downloader.zgzcw.page.RankWebPage;

public interface RankWebPageService extends IService<RankWebPage>
{
	List<RankWebPage> getDownloadedRanks();
}
