package com.loris.soccer.web.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ContextLoader
{
	/**
	 * 获得配置文件内容
	 * @param xmlFile
	 * @return
	 */
	public static ApplicationContext getClassPathXmlApplicationContext(String xmlFile)
		throws BeansException
	{
		ApplicationContext context = new ClassPathXmlApplicationContext(xmlFile);
		return context;
	}
	
	/**
	 * 获得文件系统的配置内容
	 * @param xmlFile
	 * @return
	 */
	public static ApplicationContext getFileSystemXmlApplicationContext(String xmlFile)
	{
		ApplicationContext context = new FileSystemXmlApplicationContext(xmlFile);
		return context;
	}
}
