package com.loris.base.web.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.page.WebPage;

/**
 * 父类数据
 * 
 * @author Administrator
 *
 */
public abstract class AbstractWebPageParser implements WebPageParser
{
	private static Logger logger = Logger.getLogger(AbstractWebPageParser.class);

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
		// String tid = values[size - 1];
		for (int i = size - 1; i >= 0; i--)
		{
			String tid = values[i];
			if (NumberUtil.isNumber(tid))
			{
				return tid;
			}
		}
		return "";
	}

	/**
	 * 检测基本数据是否符合要求
	 * 
	 * @param page
	 * @param clazzes
	 * @return
	 */
	public boolean checkBasicInfo(WebPage page, List<Class<?>> clazzes)
	{
		if (!page.isCompleted())
		{
			logger.info("The WebPage: " + page + " is not null.");
			return false;
		}

		if (StringUtils.isEmpty(page.getContent()))
		{
			logger.info("The WebPage: " + page + " content is null.");
			return false;
		}

		if (clazzes == null)
		{
			return true;
		}
		for (Class<?> clazz : clazzes)
		{
			if (!clazz.isInstance(page))
			{
				logger.info("The WebPage: " + page + " is not a validate '" + clazz.getName() + "' object.");
				return false;
			}
		}

		return true;
	}

	/**
	 * 页面数据基本检查
	 * 
	 * @param page
	 *            数据页面
	 * @param type
	 *            页面类型
	 * @return 是否检查通过
	 */
	public boolean checkBase(WebPage page, String type)
	{
		if (page == null || !page.isCompleted())
		{
			return false;
		}

		if (type != null && !type.equalsIgnoreCase(page.getType()))
		{
			return false;
		}
		return true;
	}

	/**
	 * 获select元素的当前值
	 * 
	 * @param element
	 * @return
	 */
	public String getSelectElementValue(Element element)
	{
		Elements elements = element.children();
		if (elements == null || elements.size() == 0)
		{
			return "";
		}
		for (Element el : elements)
		{
			if (el.hasAttr("selected"))
			{
				return el.text();
			}
		}
		return elements.get(0).text();
	}

	/**
	 * 获得所有的元素值
	 * 
	 * @param element
	 * @return
	 */
	public List<String> getSelectElementValues(Element element)
	{
		List<String> values = new ArrayList<>();
		Elements elements = element.children();
		for (Element el : elements)
		{
			String txt = el.text();
			if (StringUtils.isNotEmpty(txt))
			{
				values.add(txt);
			}
		}
		return values;
	}
}
