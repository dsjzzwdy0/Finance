package com.loris.base.web.parser;

import java.util.regex.Pattern;

import com.loris.base.bean.web.WebPage;

public interface WebPageParser
{
	public static Pattern RITHG_SLASH = Pattern.compile("/");
	
	/**
	 * Parse the Web page to StockInfo.
	 * 
	 * @param page
	 * @return
	 */
	boolean parseWebPage(WebPage page);
}