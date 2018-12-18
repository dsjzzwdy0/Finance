package com.loris.soccer.web.repository.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.loris.soccer.web.downloader.zgzcw.page.SeasonWebPage;

public interface SeasonWebPageMapper extends BaseMapper<SeasonWebPage>
{
	List<SeasonWebPage> getDownloadedSeasons();
}
