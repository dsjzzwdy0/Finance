package com.loris.soccer.web.repository.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.loris.soccer.web.downloader.zgzcw.page.SeasonWebPage;

public interface SeasonWebPageService extends IService<SeasonWebPage>
{
	List<SeasonWebPage> getDownloadedSeasons();
}
