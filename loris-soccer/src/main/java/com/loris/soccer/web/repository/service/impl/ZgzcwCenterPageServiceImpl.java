package com.loris.soccer.web.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.web.downloader.zgzcw.page.ZgzcwCenterPage;
import com.loris.soccer.web.repository.mapper.ZgzcwCenterPageMapper;
import com.loris.soccer.web.repository.service.ZgzcwCenterPageService;

@Service("zgzcwCenterPageService")
public class ZgzcwCenterPageServiceImpl extends ServiceImpl<ZgzcwCenterPageMapper, ZgzcwCenterPage>
	implements ZgzcwCenterPageService
{

}
