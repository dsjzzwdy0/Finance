package com.loris.soccer.web.scheduler;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.loris.soccer.repository.RemoteSoccerManager;
import com.loris.soccer.repository.SoccerManager;
import com.loris.soccer.web.config.ContextLoader;

public class DataUploadScheduler extends AbstractScheduler
{
	private static Logger logger = Logger.getLogger(DataUploadScheduler.class);
	
	/** 远程数据的访问器 */
	protected RemoteSoccerManager remoteManager;
	
	/** 本地数据管理器 */
	protected SoccerManager soccerManager;
	
	/** 默认的系统配置路径 */
	protected final String defaultContextFile = "classpath:soccerApplicationContext.xml";
	
	/** 用户自定义的配置路径 */
	protected final String userContextFile = "classpath:userApplicationContext.xml";
	
	/**
	 * Create a new instance of DataUploadScheduler.
	 */
	public DataUploadScheduler()
	{
	}
	
	/**
	 * 数据初始化
	 * @return
	 */
	public boolean initialize()
	{
		try
		{
			ApplicationContext context = getDefaultApplicationContext();
			try
			{
				soccerManager = context.getBean(SoccerManager.class);
			}
			catch(Exception e)
			{
				logger.info("Error occured when create SoccerManager.");
				return false;
			}
			try
			{
				remoteManager = (RemoteSoccerManager)context.getBean("remoteSoccerManager");
				logger.info("RemoteSoccerManager " + remoteManager);
			}
			catch(Exception e)
			{
				logger.info("Error occured when create RemoteSoccerManager.");
			}
			
			try
			{
				ApplicationContext userContext = getUserApplicationContext();
				RemoteSoccerManager remoteSoccerManager = (RemoteSoccerManager)userContext.getBean("remoteSoccerManager");
				logger.info("Get user defined RemoteSoccerManager: " + remoteSoccerManager);
				
				if(remoteManager == null)
				{
					remoteManager = remoteSoccerManager;
				}
				else
				{
					remoteManager.setRemoteSoccerManagerInfo(remoteSoccerManager);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				logger.info("Error when get user defined remoteManager.");
			}
		}
		catch(Exception e)
		{
			logger.info("Error when initialized.");
		}
		return false;
	}
	
	/**
	 * 获得默认的配置信息
	 * @return
	 */
	protected ApplicationContext getDefaultApplicationContext()
	{
		ApplicationContext context = ContextLoader.getClassPathXmlApplicationContext(defaultContextFile);
		return context;
	}
	
	/**
	 * 获得用户的配置信息文件
	 * @return
	 */
	protected ApplicationContext getUserApplicationContext()
	{
		ApplicationContext context = ContextLoader.getClassPathXmlApplicationContext(userContextFile);
		return context;
		
	}

	/**
	 * 
	 */
	@Override
	public void run()
	{
	}
}
