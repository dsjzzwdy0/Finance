package com.loris.soccer.web.repository.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.loris.soccer.web.downloader.okooo.page.OkoooWebPage;
import com.loris.soccer.web.repository.mapper.OkoooWebPageMapper;
import com.loris.soccer.web.repository.service.OkoooWebPageService;

@Service("okoooWebPageService")
public class OkoooWebPageServiceImpl extends ServiceImpl<OkoooWebPageMapper, OkoooWebPage>
	implements OkoooWebPageService
{

}
