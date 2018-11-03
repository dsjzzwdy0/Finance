package com.loris.base.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.context.DefaultLorisContext;
import com.loris.base.context.LorisContext;
import com.loris.base.web.config.ConfigParser;
import com.loris.base.web.config.setting.DownSetting;
import com.loris.base.web.http.Settings;
import com.loris.base.web.manager.Downloader;
import com.loris.base.web.manager.TaskMode;
import com.loris.base.web.scheduler.MultiTaskSchedulerThread;
import com.loris.base.web.scheduler.SingleTaskSchedulerThread;
import com.loris.base.web.scheduler.MonitorThread;
import com.loris.base.web.task.WebPageTask;
import com.loris.base.web.util.Monitor;

/**
 * WebCrawler.
 * 
 * @author usr
 *
 */
public class WebCrawler implements Runnable
{
	private static Logger logger = Logger.getLogger(WebCrawler.class);

	/** The DownLoaders managers. */
	private List<Downloader> downloaders = new ArrayList<Downloader>();
	
	/** WebPageInfos. */
	//protected List<WebPageSetting> basicSettings = new ArrayList<>();
	/** The DownLoaders settings. */
	protected static Map<String, List<DownSetting>> settingMap = new HashMap<>();

	/** The default rules file. */
	private static String ruleFileaName = "StockRules.xml";

	/** The context value. */
	private static ClassPathXmlApplicationContext context;
	
	/** 唯一实例管理器*/
	private static WebCrawler instance;
	
	/**
	 * 获得唯一实例
	 * @return
	 */
	public static WebCrawler getInstance()
	{
		if(instance == null)
		{
			instance = new WebCrawler();
		}
		return instance;
	}
	
	/**
	 * Get the WebPageSettingMap.
	 * @return Map object.
	 */
	public static Map<String, List<DownSetting>> getWebPageSettingMap()
	{
		return settingMap;
	}
	
	/**
	 * 注册下载管理器
	 * @param key
	 * @param settings
	 */
	public static void addDownSettings(String key, List<DownSetting> settings)
	{
		settingMap.put(key, settings);
	}
	
	/**
	 * 注册下载管理器信息
	 * @param info
	 */
	public static void registWebPageSetting(DownSetting info)
	{
		//basicSettings.add(info);
		String category = info.getCategory();
		List<DownSetting> list = settingMap.get(category);
		if(list == null)
		{
			list = new ArrayList<>();
			//list.add(info);
			settingMap.put(category, list);
		}

		list.add(info);		
	}

	/**
	 * 获得下载管理器信息
	 * @return
	 */
	public static List<DownSetting> getWebPageSettings()
	{
		List<DownSetting> list = new ArrayList<>();
		for (List<DownSetting> l : settingMap.values())
		{
			list.addAll(l);
		}
		return list;
	}

	/**
	 * Create a new instance of WebCrawler.
	 */
	private WebCrawler()
	{
	}
	
	/**
	 * Run the program.
	 */
	@Override
	public void run()
	{
		logger.info("LOADING CONFIGURATION. Please, stand by.");
		Settings sets = ConfigParser.getSettings();
		logger.info("CONFIGURATION LOADED." + sets.toString());

		testConfigparser();
		System.out.println("Permission setting: " + ConfigParser.getSettings().getCrawlPermission());

		for (Downloader webPageManager : downloaders)
		{
			if (webPageManager.getEnable())
			{
				startMainSchedulerThread(webPageManager);
			}
		}
		startMonitorThread();
	}

	/**
	 * Add the WebPageManager.
	 * 
	 * @param webPageDownloader
	 */
	public void addWebPageDownloader(Downloader webPageDownloader)
	{
		for (Downloader downloader : downloaders)
		{
			if(downloader == webPageDownloader)
			{
				return;
			}
		}
		downloaders.add(webPageDownloader);
	}

	/**
	 * 正在下载的数据大小
	 * 
	 * @return
	 */
	public int size()
	{
		return downloaders.size();
	}

	/**
	 * 获得当前正在下载的数据管理器列表
	 * 
	 * @return
	 */
	public List<Downloader> getCurrentDownloaders()
	{
		return downloaders;
	}

	/**
	 * 删除当前下载的数据
	 * 
	 * @param id
	 */
	public void removeDownloader(String id)
	{
		for (Downloader webPageManager : downloaders)
		{
			DownSetting pageInfo = webPageManager.getWebPageSetting();
			if (pageInfo == null)
			{
				continue;
			}
			if (id.equalsIgnoreCase(pageInfo.getWid()) || id.equalsIgnoreCase(pageInfo.getId()))
			{
				downloaders.remove(webPageManager);
			}
		}
	}
	
	/**
	 * 删除下载管理器
	 * 
	 * @param manager
	 */
	public void removeDownloader(Downloader manager)
	{
		downloaders.remove(manager);
	}

	/**
	 * Get the web page manager.
	 * 
	 * @param name
	 * @return
	 */
	public Downloader getWebPageDownloader(String name)
	{
		for (Downloader webPageManager : downloaders)
		{
			if (name.equals(webPageManager.getName()))
			{
				return webPageManager;
			}
			else
			{
				DownSetting pageInfo = webPageManager.getWebPageSetting();
				if (pageInfo != null)
				{
					if (name.equalsIgnoreCase(pageInfo.getWid()) || name.equalsIgnoreCase(pageInfo.getId()))
					{
						return webPageManager;
					}
				}
			}
		}

		return null;
	}

	/**
	 * Get the WebPageManager.
	 * 
	 * @param id
	 * @return
	 */
	public Downloader getWebPageDownloaderByID(String id)
	{
		// 检测是否空ID
		if (StringUtils.isEmpty(id))
			return null;

		// 查询
		for (Downloader webPageManager : downloaders)
		{
			if (id.equalsIgnoreCase(webPageManager.getWebPageSetting().getId()))
			{
				return webPageManager;
			}
		}
		return null;
	}

	/**
	 * Stop the WebPageManager.
	 * 
	 * @param name
	 * @return
	 */
	public boolean stopWebPageDownloader(String name)
	{
		for (Downloader webPageManager : downloaders)
		{
			if (name.equals(webPageManager.getName()))
			{
				webPageManager.stopDownloader();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get the Downloader.
	 * @param index
	 * @return
	 */
	public Downloader getWebPageDownloader(int index)
	{
		return downloaders.get(index);
	}

	/**
	 * Start main scheduler thread.
	 * 
	 * @param downloader 下载管理器
	 */
	public void startMainSchedulerThread(Downloader downloader)
	{
		Thread thread = null;
		Monitor.setStartTime(System.currentTimeMillis());
		if(downloader.getTaskMode() == TaskMode.Single)
		{
			logger.debug("Starting SingleTaskScheduler...");
			thread = new SingleTaskSchedulerThread(downloader, WebPageTask.class, downloader.getInterval());
			thread.setName("SingleTaskScheduler Thread");			
		}
		else
		{		
			logger.debug("Starting MultiTaskScheduler...");				
			thread = new MultiTaskSchedulerThread(downloader, WebPageTask.class, downloader.getInterval());
			thread.setName("MultiTaskScheduler Thread");
		}
		
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.setDaemon(false);
		thread.start();
	}
	
	
	/**
	 * 开始主下载线程
	 * 
	 * @param downloader 下载管理器
	 * @param webClient	下载器
	 
	public void startMainSchedulerThread(Downloader downloader, WebClient webClient)
	{
		log.info("Starting scheduler: " + downloader.getName() + " width WebClient.");
		Crawler.setStartTime(System.currentTimeMillis());
		
		MainSchedulerThread schedulerThread = 
				new MainSchedulerThread(downloader, HtmlUnitWebPageTask.class, downloader.getInterval(), webClient);
		schedulerThread.setPriority(Thread.MIN_PRIORITY);
		schedulerThread.setName("Scheduler Thread");
		schedulerThread.setDaemon(false);
		schedulerThread.start();
	}*/

	/**
	 * Start monitor thread.
	 */
	public void startMonitorThread()
	{
		logger.debug("Starting scheduler");
		Monitor.setStartTime(System.currentTimeMillis());

		MonitorThread monitor = new MonitorThread(ConfigParser.getSettings().getMonitorInterval());

		monitor.setPriority(Thread.MIN_PRIORITY);
		monitor.setName("Monitor Thread");
		monitor.setDaemon(false);
		monitor.start();
	}

	/**
	 * Debug method to test if the config parser works properly
	 */
	public void testConfigparser()
	{
		logger.info("======= CRAWLER CONFIGURATION ======");
		logger.info("Connection Timout: " + ConfigParser.getSettings().getConnectionTimeout());
		logger.info("Threads Interval: " + ConfigParser.getSettings().getInterval());
		logger.info("Monitor Interval: " + ConfigParser.getSettings().getMonitorInterval());
		logger.info("------- HTTP Headers ----- ");
		printMap(ConfigParser.getSettings().getHeaders());
		logger.info("-------------------------- ");
		logger.info("------- Crawl URLS ------- ");
		logger.info(printSet(ConfigParser.getSettings().getCrawlUrls()));
		logger.info("-------------------------- ");
		logger.info("Pattern Permission: " + ConfigParser.getSettings().getCrawlPermission());
		logger.info("------- URL Patterns ------- ");
		logger.info(printSet(ConfigParser.getSettings().getUrlPatterns()));
		logger.info("-------------------------- ");
		logger.info("===== END CRAWLER CONFIGURATION =====");
	}

	/**
	 * Print the setting infomation.
	 * 
	 * @param set
	 * @return
	 */
	protected static String printSet(Set<String> set)
	{
		String ret = "";
		if (set != null)
		{
			Iterator<String> it = set.iterator();
			while (it.hasNext())
			{
				String element = (String) it.next();
				ret = ret + "   " + element;
				if (it.hasNext())
					ret = ret + "\n";
			}
		}
		return ret;
	}

	/**
	 * Get the LorisContext
	 * 
	 * @param path
	 * @return
	 */
	public static LorisContext getLorisContext(String path)
	{
		if (context == null)
		{
			context = new ClassPathXmlApplicationContext(path);
		}
		return new DefaultLorisContext(context);
	}

	/**
	 * Close the context value.
	 * 
	 */
	public static void close()
	{
		try
		{
			if (context != null)
			{
				context.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Print the information.
	 * 
	 * @param map
	 */
	protected static void printMap(Map<String, String> map)
	{
		if (map != null)
		{
			Set<String> keyset = map.keySet();
			if (keyset != null)
			{
				Iterator<String> it = keyset.iterator();
				String key, value;

				while (it.hasNext())
				{
					key = (String) it.next();
					value = (String) map.get(key);
					logger.info("   " + key + " = " + value);
				}
			}
		}
	}

	/**
	 * main
	 * 
	 * @param args
	 */
	public static void main(String args[])
	{
		try
		{
			if (args == null || args.length < 2)
			{
				logger.info("There are no conf file setted, System exit.");
				return;
			}

			WebCrawler crawler = null;
			LorisContext context = getLorisContext("classpath:applicationContext.xml");

			if ("-file".equalsIgnoreCase(args[0]))
			{
				String file = args[1];
				logger.info("Starting parse " + file);
				crawler = ConfigParser.parseConf(file, ruleFileaName);

				int size = crawler.size();
				for (int i = 0; i < size; i++)
				{
					Downloader manager = crawler.getWebPageDownloader(i);
					manager.setLorisContext(context);

					logger.info("Adding " + manager.toString());
					/*
					 * if (manager instanceof StockInfoWebPageManager) {
					 * List<StockInfo> stocks =
					 * StockManager.getInstance().getStockList();
					 * ((StockInfoWebPageManager) manager).setStockList(stocks);
					 * }
					 */

					//crawler.addWebPageDownloader(manager);
				}
			}
			else
			{
				logger.info("No validate WebManager to be set. System exit.");
				return;
			}

			logger.info("Start WebCrawler...");
			crawler.run();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
