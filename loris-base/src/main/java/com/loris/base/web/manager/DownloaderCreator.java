package com.loris.base.web.manager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import org.apache.log4j.Logger;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.DateUtil;
import com.loris.base.util.ReflectUtil;
import com.loris.base.web.config.setting.DownSetting;
import com.loris.base.web.manager.event.WebPageStatusListener;

public interface DownloaderCreator
{
	final static Logger log = Logger.getLogger(DownloaderCreator.class);

	/**
	 * Create a WebPageManager.
	 * 
	 * @param webPageSetting
	 * @return Downloader
	 */
	public static Downloader createDownloader(DownSetting webPageSetting, WebPageStatusListener listener)
	{
		String className = webPageSetting.getClassname();
		if (StringUtils.isEmpty(className))
		{
			log.info("The WebPageManager '" + webPageSetting + "' is not regiestered.");
			return null;
		}
		try
		{
			Class<?> clazz = Class.forName(className);
			Downloader downloader = (Downloader)clazz.newInstance();
			
			setDownloader(downloader, webPageSetting);
			/*
			List<Method> methods = new ArrayList<Method>();
			ClassUtil.getAllMethods(methods, clazz);
			Field[] infoFields = webPageSetting.getClass().getDeclaredFields();
			
			String name;
			Object value;
			for (Field f : infoFields)
			{
				f.setAccessible(true);
				name = "set" + f.getName();
				for (Method method : methods)
				{
					if (name.equalsIgnoreCase(method.getName()))
					{
						method.setAccessible(true);
						value = f.get(webPageSetting);						
						log.info(method.getName() + "(" + value + ")");
						if(value == null)
							continue;
						method.invoke(downloader, value);
					}
				}
			}*/
			
			
			//Downloader downloader = (Downloader) obj;
			downloader.setWebPageSetting(webPageSetting);
			downloader.addWebPageStatusListener(listener);
			downloader.setStatus(DownloaderStatus.STATUS_INIT);
			webPageSetting.setCreatetime(DateUtil.getCurTimeStr());
			return downloader;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.info("Error in creating the " + webPageSetting + ".\r\n" + e.toString());
		}
		return null;
	}
	
	/**
	 * Set the Downloader Setting.
	 * @param downloader
	 * @param webPageSetting
	 */
	public static void setDownloader(Downloader downloader, DownSetting webPageSetting)
	{
		try
		{
			List<Method> methods = ReflectUtil.getAllMethods(downloader.getClass(), false);
			Field[] infoFields = webPageSetting.getClass().getDeclaredFields();
			
			String name;
			Object value;
			for (Field f : infoFields)
			{
				f.setAccessible(true);
				name = "set" + f.getName();
				for (Method method : methods)
				{
					if (name.equalsIgnoreCase(method.getName()))
					{
						method.setAccessible(true);
						value = f.get(webPageSetting);						
						log.info(method.getName() + "(" + value + ")");
						if(value == null)
							continue;
						method.invoke(downloader, value);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.info("Error in creating the " + webPageSetting + ".\r\n" + e.toString());
		}
	}
}
