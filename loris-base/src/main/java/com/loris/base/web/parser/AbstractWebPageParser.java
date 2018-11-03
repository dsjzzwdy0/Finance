package com.loris.base.web.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.loris.base.bean.web.WebPage;
import com.loris.base.util.NumberUtil;

/**
 * 父类数据
 * 
 * @author Administrator
 *
 */
public abstract class AbstractWebPageParser implements WebPageParser
{
	/**
	 * Parse the html string to document.
	 * 
	 * @param page
	 * @return
	 */
	public Document parseHtml(WebPage page)
	{		
		return Jsoup.parse(page.getContent());
	}
	
	/**
	 * Get the Lid value.
	 * 
	 * @param url
	 * @return
	 */
	public String getLastNumberValue(String url)
	{
		String[] values = url.split(RITHG_SLASH.pattern());
		int size = values.length;
		//String tid = values[size - 1];
		for(int i = size - 1; i >= 0; i --)
		{
			String tid = values[i];
			if(NumberUtil.isNumber(tid))
			{
				return tid;
			}
		}
		return "";
	}
	
	/**
	 * 页面数据基本检查
	 * @param page 数据页面
	 * @param type 页面类型
	 * @return 是否检查通过
	 */
	public boolean checkBase(WebPage page, String type)
	{
		if(page == null || !page.isCompleted())
		{
			return false;
		}
		
		if(type != null && !type.equalsIgnoreCase(page.getType()))
		{
			return false;
		}
		return true;
	}
}
