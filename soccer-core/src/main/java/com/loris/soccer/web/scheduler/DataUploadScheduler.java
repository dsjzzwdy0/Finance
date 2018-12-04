package com.loris.soccer.web.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.entity.Entity;
import com.loris.base.util.DateUtil;
import com.loris.soccer.bean.data.table.BdMatch;
import com.loris.soccer.bean.data.table.JcMatch;
import com.loris.soccer.bean.data.table.Match;
import com.loris.soccer.bean.data.table.Op;
import com.loris.soccer.bean.data.table.Yp;
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

	/** 开始的日期 */
	protected String start;

	/** 结束的日期 */
	protected String end;

	/** 默认的系统配置路径 */
	protected final String defaultContextFile = "classpath:soccerApplicationContext.xml";

	/** 用户自定义的配置路径 */
	protected String userContextFile = "file:userApplicationContext.xml";

	/**
	 * Create a new instance of DataUploadScheduler.
	 */
	public DataUploadScheduler()
	{
	}

	/**
	 * 数据初始化
	 * 
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
			catch (Exception e)
			{
				logger.info("Error occured when create SoccerManager.");
				return false;
			}
			try
			{
				remoteManager = (RemoteSoccerManager) context.getBean("remoteSoccerManager");
				logger.info("RemoteSoccerManager " + remoteManager);
			}
			catch (Exception e)
			{
				logger.info("Error occured when create RemoteSoccerManager.");
			}

			try
			{
				ApplicationContext userContext = getUserApplicationContext();
				RemoteSoccerManager remoteSoccerManager = (RemoteSoccerManager) userContext
						.getBean("remoteSoccerManager");
				logger.info("Get user defined RemoteSoccerManager: " + remoteSoccerManager);

				if (remoteManager == null)
				{
					remoteManager = remoteSoccerManager;
				}
				else
				{
					remoteManager.setRemoteSoccerManagerInfo(remoteSoccerManager);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				logger.info("Error when get user defined remoteManager.");
			}
		}
		catch (Exception e)
		{
			logger.info("Error when initialized.");
		}

		if (soccerManager != null && remoteManager != null)
		{
			logger.info("Success to initialize the system.");
			return true;
		}
		return false;
	}

	/**
	 * 获得默认的配置信息
	 * 
	 * @return
	 */
	protected ApplicationContext getDefaultApplicationContext()
	{
		ApplicationContext context = ContextLoader.getClassPathXmlApplicationContext(defaultContextFile);
		return context;
	}

	/**
	 * 获得用户的配置信息文件
	 * 
	 * @return
	 */
	protected ApplicationContext getUserApplicationContext()
	{
		ApplicationContext context = ContextLoader.getFileSystemXmlApplicationContext(userContextFile);
		return context;
	}

	/**
	 * 保存数据到远程服务器
	 * 
	 * @param entities
	 * @return
	 */
	protected String saveEntities(List<? extends Entity> entities)
	{
		if(entities == null || entities.isEmpty())
		{
			logger.info("There are no Entities data to be uploaded.");
			return "error";
		}
		try
		{
			logger.info("Upload " + entities.get(0).getClass().getName() + " with [" + entities.size() + "] entities.");
			return remoteManager.saveEntities(entities);
		}
		catch (Exception e)
		{
			logger.info(
					"Error when save " + entities.get(0).getClass().getName() + " with [" + entities.size() + "] entities."
			);
			return "error";
		}
	}

	protected void saveMatches(List<Match> matchs)
	{
		int size = matchs.size();
		logger.info("Save matches [" + size + "].");

		int pernum = 100;
		int st = 0;
		for (; st < size;)
		{
			List<Match> temp = new ArrayList<>();
			for (int j = 0; j < pernum; j++)
			{
				if (j + st >= size)
				{
					break;
				}
				else
				{
					temp.add(matchs.get(j + st));
				}
			}
			st += pernum;
			saveEntities(temp);
			sleep(100);
		}
	}

	/**
	 * 运行线程
	 */
	@Override
	public void run()
	{
		if (StringUtils.isEmpty(start))
		{
			start = DateUtil.getCurDayStr();
		}
		List<Match> matchs = soccerManager.getMatches(start, end);
		saveMatches(matchs);

		List<JcMatch> jcMatchs = soccerManager.getJcMatchesByDate(start, end);
		String result = saveEntities(jcMatchs);
		logger.info("Save " + jcMatchs.size() + " JcMatches result: " + result);
		sleep(100);

		List<BdMatch> bdMatchs = soccerManager.getBdMatchByMatchtime(start, end);
		result = saveEntities(bdMatchs);
		logger.info("Save " + bdMatchs.size() + " BdMatches result: " + result);
		sleep(100);

		int i = 1;
		for (BdMatch match : bdMatchs)
		{
			logger.info(i++ + ": " + match);
			List<Op> ops = soccerManager.getOddsOp(match.getMid());
			if(ops != null && !ops.isEmpty())
			{
				logger.info("First: " + ops.get(0));
				result = saveEntities(ops);
				logger.info("Save result: " + result);
				sleep(100);
			}

			List<Yp> yps = soccerManager.getYpList(match.getMid());
			if(yps != null && !yps.isEmpty())
			{
				logger.info("First: " + yps.get(0));
				result = saveEntities(yps);
				logger.info("Save result: " + result);
				sleep(100);
			}
		}
	}

	public String getStart()
	{
		return start;
	}

	public void setStart(String start)
	{
		this.start = start;
	}

	public String getEnd()
	{
		return end;
	}

	public void setEnd(String end)
	{
		this.end = end;
	}

	public String getUserContextFile()
	{
		return userContextFile;
	}

	public void setUserContextFile(String userContextFile)
	{
		this.userContextFile = userContextFile;
	}
}
