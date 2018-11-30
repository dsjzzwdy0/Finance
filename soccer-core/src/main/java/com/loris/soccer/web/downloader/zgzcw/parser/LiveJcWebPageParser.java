package com.loris.soccer.web.downloader.zgzcw.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.page.LiveWebPage;

public class LiveJcWebPageParser extends AbstractWebPageParser
{
	private static Logger logger = Logger.getLogger(LiveJcWebPageParser.class);
	
	@Override
	public boolean parseWebPage(WebPage page)
	{
		List<Class<?>> clazzes = new ArrayList<>();
		clazzes.add(LiveWebPage.class);
		if(!checkBasicInfo(page, clazzes))
		{
			return false;
		}
		
		Document document = parseHtml(page);
		if(document == null)
		{
			logger.info("The parse result is null, exit.");
			return false;
		}
		
		return false;
	}

}
