/*
 * @Author George Kvizhinadze
 * CVS-ID: $Id: ConfigParser.java,v 1.1 2004/11/30 22:47:42 idumali Exp $
 *
 * Copyright (c) 2004 Development Gateway Foundation, Inc. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Common Public License v1.0 which accompanies this
 * distribution, and is available at:
 * http://www.opensource.org/licenses/cpl.php
 *
 *****************************************************************************/

package com.loris.base.web.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.WebCrawler;
import com.loris.base.web.config.setting.DownSetting;
import com.loris.base.web.config.setting.PageSetting;
import com.loris.base.web.http.ParamMapEntry;
import com.loris.base.web.http.Settings;
import com.loris.base.web.repository.WebPageManager;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.apache.log4j.Logger;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.SAXException;

public class ConfigParser
{
	private static Logger logger = Logger.getLogger(ConfigParser.class);
	private static Settings crawlerSettings = null;

	/**
	 * Get the Settings.
	 * @return Settings
	 */
	public static Settings getSettings()
	{
		if(crawlerSettings == null)
		{
			initialize();
		}
		return crawlerSettings;
	}
	
	/**
	 * Initialize the data settings. 
	 */
	private static void initialize()
	{
		try
		{
			InputStream input = ConfigParser.class.getResourceAsStream("crawlerConfig.xml");
			crawlerSettings = parseConfiguration(input);
			// -- Compile regexpes to improve performance.
			compileUrlPatterns();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		catch (SAXException ex)
		{
			ex.printStackTrace();
		}
	}
		
	/**
	 * 解析默认的下载配置管理器,解析之后的数据存储在WebCrawler中
	 * @throws IOException
	 * @throws SAXException
	 * @throws JDOMException
	 */
	public static void parseWebDownloaders() throws IOException, SAXException, JDOMException
	{
		parseWebPageSettings(ConfigParser.class.getResourceAsStream("/web-downloads.xml"));
	}
	
	/**
	 * 
	 * @throws IOException
	 * @throws SAXException
	 * @throws JDOMException
	 */
	public static void parseTaskProducers() throws IOException, SAXException, JDOMException
	{
		
	}
	
	/**
	 * Parse the web-downloads.xml file setting value.
	 * 
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static void parseWebPageSettings(InputStream inputStream) throws IOException, SAXException, JDOMException
	{
		//List<DownSetting> webPageSettings = new ArrayList<>();
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(inputStream);
		Element rootElement = document.getRootElement();
		
		List<Element> elements = rootElement.getChildren();
		for (Element element : elements)
		{
			if("downloaders".equalsIgnoreCase(element.getName()))
			{
				parseDownloaders(element);				
			}
			else if("webpages".equalsIgnoreCase(element.getName()))
			{
				parsePageSettings(element);
			}
		}
	}
	
	/**
	 * 解析设置
	 * @param webPageSettings
	 * @param element
	 */
	private static void parseDownloaders(Element element)
	{
		List<Element> elements = element.getChildren();
		for (Element ele : elements)
		{
			if("category".equalsIgnoreCase(ele.getName()))
			{
				parseCategory(ele);
			}
		}
	}	
	
	/**
	 * Compile the URL Patterns.
	 */
	private static void compileUrlPatterns()
	{
		// -- Compile regexpes to improve performance.
		Set<String> urlPatterns = ConfigParser.getSettings().getUrlPatterns();
		Iterator<String> itPatterns = urlPatterns.iterator();
		final int flags = Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE
				| Pattern.CANON_EQ;
		while (itPatterns.hasNext())
		{
			String currRegexp = (String) itPatterns.next();
			Pattern pattern = Pattern.compile(currRegexp, flags);
			ConfigParser.getSettings().getUrlPatternsCompiled().add(pattern);
		}

	}
	
	/**
	 * 解析下载管理器的配置信息
	 * @param settings 配置信息
	 * @param element XML元素
	 */
	protected static void parseCategory(Element element)
	{
		//处理属性数据
		String cate = null;
		String cateid = null;
		List<Attribute> attributes = element.getAttributes();
		for (Attribute attribute : attributes)
		{
			if("name".equalsIgnoreCase(attribute.getName()))
			{
				cate = attribute.getValue();
			}
			
			if("id".equalsIgnoreCase(attribute.getName()))
			{
				cateid = attribute.getValue();
			}
		}		
		if(StringUtils.isEmpty(cate))
		{
			return;
		}
		
		//处理每一个子节点数据
		List<Element> elements = element.getChildren();
		for (Element e : elements)
		{
			DownSetting setting = parseWebPageSetting(cateid, cate, e);
			if(setting != null)
			{
				WebCrawler.registWebPageSetting(setting);
			}
		}		
	}
	
	/**
	 * Parse the WebPageSetting
	 * @param cateid The Category id value.
	 * @param category The Category name.
	 * @param element The element.
	 */
	protected static DownSetting parseWebPageSetting(String cateid, String category, Element element)
	{
		String id = null, encoding = null;
		String name = null; 
		String clazz = null;
		String desc = null;
		String interval = null;
		List<Attribute> attributes = element.getAttributes();
		for (Attribute attribute : attributes)
		{
			if("id".equalsIgnoreCase(attribute.getName()))
			{
				id = attribute.getValue();
			}
			else if("encoding".equalsIgnoreCase(attribute.getName()))
			{
				encoding = attribute.getValue();
			}
		}		
		//logger.info("Id: " + id + ", encoding: " + encoding);
		
		List<Element> elements = element.getChildren();
		for (Element e : elements)
		{
			if("name".equalsIgnoreCase(e.getName()))
			{
				name = e.getValue();
			}
			else if("description".equalsIgnoreCase(e.getName()))
			{
				desc = e.getValue();
			}
			else if("clazz".equalsIgnoreCase(e.getName()))
			{
				clazz = e.getValue();
			}
			else if("interval".equalsIgnoreCase(e.getName()))
			{
				interval = e.getValue();
			}
			else
			{
				//Default.
			}
		}
		
		//检测是否是合法的
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(clazz) || StringUtils.isEmpty(id))
		{
			return null;
		}
		
		DownSetting setting = new DownSetting();
		setting.setWid(id);
		setting.setName(name);
		setting.setEncoding(encoding);
		setting.setClassname(clazz);
		setting.setCateid(cateid);
		setting.setCategory(category);
		setting.setDescription(desc);
		setting.setInterval(NumberUtil.parseInt(interval));
		//logger.info(setting);
		return setting;
	}
	
	/**
	 * 解析页面配置的数据
	 * @param element
	 */
	protected static void parsePageSettings(Element element)
	{
		String baseDir;
		List<Element> elements = element.getChildren();
		for (Element element2 : elements)
		{
			if("datapath".equalsIgnoreCase(element2.getName()))
			{
				baseDir = element2.getValue();
				WebPageManager.setDefaultDir(baseDir);
			}
			else if("page".equalsIgnoreCase(element2.getName()))
			{
				PageSetting setting = new PageSetting();
				if(parsePageSetting(setting, element2))
				{
					WebPageManager.registPageSetting(setting);					
				}
			}
		}		
	}
	
	/**
	 * 解析页面设置器
	 * @param setting 设置
	 * @param element XML元素
	 */
	protected static boolean parsePageSetting(PageSetting setting, Element element)
	{
		String eleName;
		String value;
		
		List<Attribute> attributes = element.getAttributes();
		for (Attribute attribute : attributes)
		{
			eleName = attribute.getName();
			if("classname".equalsIgnoreCase(eleName))
			{
				setting.setName(attribute.getValue());
			}
			else if("type".equalsIgnoreCase(eleName))
			{
				setting.setType(attribute.getValue());
			}
		}
		
		if(StringUtils.isEmpty(setting.getName()))
		{
			return false;
		}
		List<Element> elements = element.getChildren();
		for (Element element2 : elements)
		{
			eleName = element2.getName();
			if("encoding".equalsIgnoreCase(eleName))
			{
				setting.setEncoding(element2.getValue());
			}
			else if("host".equalsIgnoreCase(eleName))
			{
				setting.setHost(element2.getValue());		
				attributes = element2.getAttributes();
				for (Attribute attribute : attributes)
				{
					if("protocol".equalsIgnoreCase(attribute.getName()))
					{
						setting.setProtocol(attribute.getValue());
					}
					else if("port".equalsIgnoreCase(attribute.getName()))
					{
						setting.setPort(attribute.getValue());
					}
					else if("method".equalsIgnoreCase(attribute.getName()))
					{
						setting.setMethod(attribute.getValue());
					}
				}
			}			
			else if("storage".equalsIgnoreCase(eleName))
			{
				setting.setSavecontent(false);
				attributes = element2.getAttributes();
				for (Attribute attribute : attributes)
				{
					if("enable".equalsIgnoreCase(attribute.getName()))
					{
						value = attribute.getValue();
						if(StringUtils.isNotEmpty(value) && (("1".equals(value) || "true".equalsIgnoreCase(value))))
						{
							setting.setSavecontent(true);
						}
					}
					else if("basedir".equalsIgnoreCase(attribute.getName()))
					{
						setting.setBasedir(attribute.getValue());
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Parse the Configuration file.
	 * 
	 * @param input
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Settings parseConfiguration(InputStream input) throws SAXException, IOException
	{
		// Settings set = new Settings();
		// String configFileName = "." + File.separator + "conf" +
		// File.separator + "crawlerConfig.xml";
		// File configFile = new File(configFileName);
		// log.debug(configFile.getAbsolutePath());

		Digester digester = new Digester();
		digester.clear();
		digester.setValidating(false);
		digester.setUseContextClassLoader(true);

		digester.addObjectCreate("settings", Settings.class);

		digester.addBeanPropertySetter("settings/interval", "interval");
		digester.addBeanPropertySetter("settings/monitorInterval", "monitorInterval");

		digester.addBeanPropertySetter("settings/connectionTimeout", "connectionTimeout");

		digester.addSetProperties("settings/url-patterns", "permission", "crawlPermission");

		digester.addObjectCreate("settings/crawl-urls/url", ParamMapEntry.class);
		digester.addSetNext("settings/crawl-urls/url", "addCrawlUrl");
		digester.addBeanPropertySetter("settings/crawl-urls/url", "key");

		digester.addObjectCreate("settings/url-patterns/pattern", ParamMapEntry.class);
		digester.addSetNext("settings/url-patterns/pattern", "addUrlPattern");
		digester.addBeanPropertySetter("settings/url-patterns/pattern", "key");

		digester.addObjectCreate("settings/headers/header", ParamMapEntry.class);
		digester.addSetNext("settings/headers/header", "addHeader");
		digester.addSetProperties("settings/headers/header", "name", "key");
		digester.addBeanPropertySetter("settings/headers/header", "value");

		Settings set = (Settings) digester.parse(input);
		input.close();
		return set;
	}

	/**
	 * Parse the configure file.
	 * 
	 * @param confFile
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 */
	public static WebCrawler parseConf(String confFileName, String ruleFileName) throws IOException, SAXException
	{
		File conFile = new File("." + File.separator + "conf" + File.separator + confFileName);
		File ruleFile = new File("." + File.separator + "conf" + File.separator + ruleFileName); //"StockRules.xml"
		logger.info("Load configuration file : " + conFile.getPath());

		Digester digester = DigesterLoader.createDigester(ruleFile.toURI().toURL());

		/*
		DigesterLoader loader = DigesterLoader.newLoader(new FromXmlRulesModule()
		{
			@Override
			protected void loadRules()
			{
				loadXMLRules(ruleFile);
			}
		});
		Digester digester = loader.newDigester();*/
		
		WebCrawler settings = (WebCrawler) digester.parse(conFile);
		return settings;
	}
}
