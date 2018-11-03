package com.loris.soccer.web.repository.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.loris.soccer.web.downloader.zgzcw.page.RankWebPage;

public interface RankWebPageMapper extends BaseMapper<RankWebPage>
{
	List<RankWebPage> getDownloadedRanks();
}
