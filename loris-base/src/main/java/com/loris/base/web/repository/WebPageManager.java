package com.loris.base.web.repository;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.loris.base.bean.web.WebPage;
import com.loris.base.util.DateUtil;
import com.loris.base.web.config.setting.PageSetting;
import com.loris.base.web.repository.io.WebPageContentIO;

/**
 * 网络页面管理器
 * @author deng
 *
 */
public class WebPageManager
{
	private static Logger logger = Logger.getLogger(WebPageManager.class);
	
	/** 注册的页面设置类 */
	private static Map<String, PageSetting> pageSettings = new HashMap<>();
	
	/** 页面设置类的公共方法 */
	private static Method[] settingMethods = PageSetting.class.getMethods();
	
	/** 存储的路径*/
	private static String DEFAULT_DIR = "";
	
	/**
	 * 设置默认的路径
	 * @param dir
	 */
	public static void setDefaultDir(String dir)
	{
		DEFAULT_DIR = dir;
	}
	
	public static String getDefaultDir()
	{
		return DEFAULT_DIR;
	}
	
	/**
	 * 获得所有的页面设置器
	 * @return
	 */
	public static List<PageSetting> getPageSettings()
	{
		Collection<PageSetting> settings = pageSettings.values();
		List<PageSetting> list = new ArrayList<>();
		list.addAll(settings);
		return list;
	}
	
	/**
	 * 获得页面设置的内容
	 * @param name 名称
	 * @return 页面设置器
	 */
	public static PageSetting getPageSetting(String name)
	{
		return pageSettings.get(name);
	}
	
	/**
	 * 创建网络页面
	 * @param classname 类型
	 * @return 数据页面
	 */
	public static WebPage createWebPage(String classname)
	{
		try
		{
			Class<?> clazz = Class.forName(classname);			
			WebPage page = (WebPage)clazz.newInstance();
			
			//设置不为空的时候，给予默认的值赋值
			PageSetting setting = pageSettings.get(classname);
			if(setting != null)
			{
				logger.info("Create: " + setting);
				Method[] methods = clazz.getMethods();
				Method m;
				Object value;
				for (Method method : methods)
				{
					String name = method.getName();
					
					//需要setMethodname与getMethodname的Methodname相同时才可进行匹配
					if(name.startsWith("set"))
					{
						m = getPageSettingGetterMethod("get" + name.substring(3, name.length()));
						if(m != null)
						{
							value = m.invoke(setting);
							method.invoke(page, value);
						}
					}
				}
			}
			page.setCreatetime(DateUtil.getCurTimeStr());
			//page.setLoadtime("2018-09-01");
			//page.setLoadtime(page.getCreatetime());
			return page;
		}
		catch(Exception exception)
		{
			logger.info(exception.toString());
		}
		return null;
	}
	
	/**
	 * 获得PageSetting的方法类
	 * @param name 名称
	 * @return 方法
	 */
	private static Method getPageSettingGetterMethod(String name)
	{
		for (Method method : settingMethods)
		{
			if(name.equals(method.getName()))
			{
				return method;
			}
		}
		return null;
	}
	
	/**
	 * 注册页面配置器
	 * @param setting
	 */
	public static void registPageSetting(PageSetting setting)
	{
		pageSettings.put(setting.getName(), setting);
	}
	
	/**
	 * 保存数据页面
	 * @param page 页面数据
	 * @return 是否成功的标志
	 */
	public boolean saveWebPage(WebPage page)
	{
		try
		{
			if(page.isSavecontent())
				return WebPageContentIO.writeWebPageContent(page);
			else
				return false;
		}
		catch(Exception e)
		{
		}
		return false;
	}
}
