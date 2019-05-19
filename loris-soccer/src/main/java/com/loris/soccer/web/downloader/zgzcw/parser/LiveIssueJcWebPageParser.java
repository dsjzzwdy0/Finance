package com.loris.soccer.web.downloader.zgzcw.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.loris.base.web.page.WebPage;
import com.loris.soccer.web.downloader.zgzcw.page.LiveWebPage;

public class LiveIssueJcWebPageParser extends LiveJcWebPageParser
{
	private static Logger logger = Logger.getLogger(LiveIssueJcWebPageParser.class);

	@Override
	public boolean parseWebPage(WebPage page)
	{
		List<Class<?>> clazzes = new ArrayList<>();
		clazzes.add(LiveWebPage.class);
		if (!checkBasicInfo(page, clazzes))
		{
			return false;
		}

		String content = "<html><body><table>" + page.getContent() + "</table></body></html>";
		page.setContent(content);
		Document document = parseHtml(page);
		if (document == null)
		{
			logger.info("The parse result is null, exit.");
			return false;
		}
		
		Elements elements = document.select("tr");
		for (Element element2 : elements)
		{
			//logger.info("Tr: ");
			parseMatch(element2);
		}
		
		return true;
	}
}
