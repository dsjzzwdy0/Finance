package com.loris.lottery.context;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.loris.base.context.DefaultLorisContext;
import com.loris.base.context.LorisContext;

/**
 * 基础运行环境配置与启动
 * 
 * @author dsj
 *
 */
public class ApplicationContextHelper implements ApplicationContextAware
{
	private static Logger log = Logger.getLogger(ApplicationContextHelper.class);
	
	/** The application context. */
	private static ApplicationContext applicationContext;

	/**
	 * Set the application context.
	 * 
	 */
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException
	{
		log.info("Set the ApplicationContext.");
		applicationContext = context;
	}
	
	/**
	 * Create a new instance of LorisWebApplicationContext.
	 */
	public ApplicationContextHelper()
	{
		log.info("Create a new instance of LorisWebApplicationContext.");
	}

	/**
	 * Get ApplicationContext.
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}
	
	/**
	 * Get the LorisContext.
	 * 
	 * @return
	 */
	public static LorisContext getLorisContext()
	{
		return new DefaultLorisContext(applicationContext);
	}

}
