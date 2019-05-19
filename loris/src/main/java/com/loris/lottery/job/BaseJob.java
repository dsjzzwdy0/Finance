/**  
 * All rights Reserved, Designed By www.loris.com
 * @Title:  BaseJob.java   
 * @Package com.loris.lottery.job   
 * @Description: 本项目用于天津东方足彩数据的存储、共享、处理等   
 * @author: 东方足彩    
 * @date:   2019年1月28日 下午8:59:32   
 * @version V1.0.0
 * @Copyright: 2019 www.loris.com Inc. All rights reserved. 
 * 注意：本内容仅限于天津东方足彩有限公司传阅，禁止外泄以及用于其他的商业目
 */
package com.loris.lottery.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.repository.BasicManager;
import com.loris.base.web.WebCrawler;
import com.loris.base.web.config.ConfigParser;
import com.loris.base.web.config.setting.DownSetting;
import com.loris.base.web.manager.Downloader;
import com.loris.base.web.manager.DownloaderCreator;
import com.loris.base.web.manager.event.WebPageStatusEvent;
import com.loris.base.web.manager.event.WebPageStatusListener;
import com.loris.base.web.util.Monitor;
import com.loris.lottery.context.ApplicationContextHelper;
import com.loris.lottery.task.WebPageRunnable;

/**   
 * @ClassName: BaseJob   
 * @Description: 数据下载基础任务类
 * @author: 东方足彩
 * @date:   2019年1月28日 下午8:59:32   
 * @Copyright: 2019 www.tydic.com Inc. All rights reserved. 
 * 注意：本内容仅限于天津东方足彩有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
public abstract class BaseJob implements Job, WebPageStatusListener
{
	private static Logger logger = Logger.getLogger(IssueDataDownloadJob.class);
	
	private BasicManager basicManager;
	
	/** WebCrawler. */
	protected WebCrawler crawler;
	
	protected String sid;
	
	/**
	 * Create the new instance of IssueDataDownloadJob
	 */
	public BaseJob()
	{
		crawler = WebCrawler.getInstance();
		ConfigParser.getSettings();
		basicManager = ApplicationContextHelper.getApplicationContext().getBean(BasicManager.class);
		sid = null;
	}
	
	/**
	 *  (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		if (StringUtils.isEmpty(sid))
		{
			logger.info("The Job '" + getClass().getName() + "' attribute sid is null and not set");
			return;
		}
		
		logger.info("Start Download Thread: " + sid);		
		DownSetting defaulSetting = crawler.getDefaultDownSetting(sid);
		if(defaulSetting == null)
		{
			String info = "Error, downloader " + sid + " is not exist.";
			logger.info(info);
			return;
		}
		
		DownSetting setting = new DownSetting();		
		setting = defaulSetting.cloneAndSetDownSetting(setting);
		Downloader downloader = getOrCreateWebPageDownloader(setting);
		if (downloader == null)
		{
			String info = "Create download manager failure.";
			logger.info(info);
			return;
		}
		
		if(Monitor.checkInDownload(downloader))
		{
			String info = "Error, downloader " + downloader.getWebPageSetting().getId() + " is running.";
			logger.info(info);
			return ;
		}

		// 启动线程
		new Thread(new WebPageRunnable(downloader)
		{
			@Override
			public void run()
			{
				crawler.startMainSchedulerThread(downloader);
			}
		}).start();
	}
	
	/**
	 * Create a new WebPageManager.
	 * 
	 * @param setting
	 * @return
	 */
	protected Downloader getOrCreateWebPageDownloader(DownSetting setting)
	{
		Downloader downloader = null;
		if (StringUtils.isNotEmpty(setting.getId()))
		{
			downloader = crawler.getWebPageDownloaderByID(setting.getId());
			//如果是已经停止，则需要重新启动
			if(downloader != null && downloader.isStopped())
			{
				downloader.restartDownloader();
			}
		}
		if(downloader == null)
		{
			basicManager.addOrUpdateDownSettingById(setting);
			downloader = DownloaderCreator.createDownloader(setting, this);
		}
		
		if (downloader != null)
		{			
			downloader.setLorisContext(ApplicationContextHelper.getLorisContext());
			crawler.addWebPageDownloader(downloader);
		}
		return downloader;
	}

	/* (non-Javadoc)
	 * @see com.loris.base.web.manager.event.WebPageStatusListener#webPageStatusChanged(com.loris.base.web.manager.event.WebPageStatusEvent)
	 */
	@Override
	public void webPageStatusChanged(WebPageStatusEvent evt)
	{	
	}
}
