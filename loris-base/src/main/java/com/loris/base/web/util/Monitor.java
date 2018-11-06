/*
 * @Author Irakli Nadareishvili
 * CVS-ID: $Id: Crawler.java,v 1.4 2005/02/03 01:43:54 idumali Exp $
 *
 * Copyright (c) 2004 Development Gateway Foundation, Inc. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Common Public License v1.0 which accompanies this
 * distribution, and is available at:
 * http://www.opensource.org/licenses/cpl.php
 *
 *****************************************************************************/

package com.loris.base.web.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;

import com.loris.base.web.manager.Downloader;


/**
 *
 * <p>
 * Crawler settings class
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class Monitor
{
	private static Logger log = Logger.getLogger(Monitor.class);

	/**
	 * Number of processed URLs. The modulus of this is, actually, used
	 * separated by number of threads, to determine which thread should
	 * process the current url.
	 */
	private static volatile long counter = 0;
	
	/** The crawlers. */
	protected static List<LoaderMonitor> crawlers = new ArrayList<>();
	
	/** The start time.*/
	private static long startTime;

	/** The flag to stop crawler. */
	public static boolean stopCrawler = false;

	/** The fetcher counter. */
	public static volatile long fetchedCounter = 0;

	/** The num of active thread. */
	//public static volatile long numOfActiveThreads = 0;
	
	/** The num of main scheduler. */
	public static volatile long numOfMainSchedulerThread = 0;
		
	/** Date format. */
	private static DateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
	
	public static final Object watch = new Integer(0);
	public static final Object cookieWatch = new Integer(0);
	/**
	 * Ok, why do we need this property? To speed things up.
	 * The story is that we do not want a contains() method
	 * to be called on getUrls() while somebody is putting
	 * something in that Set. if we use synchronize(), however,
	 * to contains() calls will, also, wait for each-other
	 * which is absolutely unnecessary and as the size of the
	 * urls set grows - becomes a serious performance bottleneck.
	 */
	public static boolean checking = false;

	/**
	 * Counts number of iterations for which there are no URLs to process.
	 * After ten iterations, crawler will restart. Used by: restartIfNeeded()
	 * method.
	 */
	private static int idleCounter = 0;
	
	/** The settings value. */
	//private static Settings settings = null;
	
	/**
	 * Returns current time in a human-readable way with the precision of
	 * milliseconds
	 * 
	 * @return
	 */
	public static String getCurrentTime()
	{		
		Date date = new Date(System.currentTimeMillis());

		return sdf.format(date);
	}
	
	/**
	 * 添加一个下载监控器
	 * 
	 * @param crawler
	 */
	public static void addLoaderCrawler(LoaderMonitor crawler)
	{
		crawlers.add(crawler);
	}
	
	/**
	 * 删除一个下载监控器
	 * @param crawler 监控器
	 */
	public static void removeLoaderCrawler(LoaderMonitor crawler)
	{
		crawlers.remove(crawler);
	}
	
	/**
	 * 根据名称删除下载监控器
	 * @param name 名称
	 */
	public static void removeLoaderCrawler(String name)
	{
		int size = crawlers.size();
		for(int i = size - 1; i >= 0; i --)
		{
			if(crawlers.get(i).isSameName(name))
			{
				crawlers.remove(i);
			}
		}
	}
	
	/**
	 * 检测是否正在下载中
	 * @param downloader
	 * @return
	 */
	public static boolean checkInDownload(Downloader downloader)
	{
		for (LoaderMonitor loaderMonitor : crawlers)
		{
			if(loaderMonitor.isDownloader(downloader))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获得当前下载的下载器的个数
	 * @return 个数
	 */
	public static int getLoaderCounter()
	{
		return crawlers.size();
	}
	
	/**
	 * 获得当前下载的线程数
	 * @return个数
	 */
	public static int getThreadCounter()
	{
		int c = 0;
		for (LoaderMonitor monitor : crawlers)
		{
			c += monitor.getActiveThreadNum();
		}
		return c;
	}

	/**
	 * Get the Counter value.
	 * @return
	 */
	public static long getCounter()
	{
		return Monitor.counter;
	}

	/**
	 * Increase the counter - number of processed URLs
	 */
	public static void incCounter()
	{
		Monitor.counter++;
	}

	public static long getStartTime()
	{
		return Monitor.startTime;
	}
	
	public static void setStartTime(long startTime)
	{
		Monitor.startTime = startTime;
	}

	/**
	 * One of the "dangers" of unique crawling is that you will run
	 * out of URLs eventually - once all unique ones are crawled.
	 *
	 * Once we detect that all unique URLs have been parsed, we try
	 * to restart the process.
	 *
	 */
	public static void restartIfNeeded()
	{
		if (idleCounter != -1 && Monitor.fetchedCounter > 3)
		{
			idleCounter++;
		}
		else
		{
			idleCounter = 0;
		}

		if (idleCounter > 20)
		{
			log.info("Crawler Restart requested. \n " + "This means all the unique URLs have been "
					+ "accessed in this iteration and we are going " + "into the next one");

			idleCounter = -1; // -- Do not disturb, while restarting!
			restartCrawler();
			idleCounter = 0;

		}
	}

	public static void restartCrawler()
	{
	}

	/**
	 * Determines if the URL should be crawled down or not.
	 * 
	 * @param url
	 * @return
	
	public static boolean crawlOrNot(String url)
	{
		boolean flag = false;

		// -- Is this URL allowed from the configuration XML file settings?
		boolean matchFlag = false;
		Set<Pattern> urlPatterns = ConfigParser.getSettings().getUrlPatternsCompiled();
		if (urlPatterns != null)
		{
			Iterator<Pattern> itPatterns = urlPatterns.iterator();
			while (itPatterns.hasNext())
			{
				Pattern pattern = (Pattern) itPatterns.next();
				Matcher m = pattern.matcher(url);
				matchFlag = m.matches();

				if (matchFlag)
				{
					break;
				}

			}

		}

		// -- If permissionMode = Denied, crawl all but that URLS
		if (ConfigParser.getSettings().getCrawlPermission() == Settings.CRAWL_DENIED)
		{
			if (matchFlag)
			{
				flag = false;
				log.debug("DENIED URL " + url);
			}
			else
			{
				log.debug("NOT DENIED URL " + url);
				flag = true && flag;
			}
		}

		// -- If permissionMode = Allowed, crawl only that URLS
		if (ConfigParser.getSettings().getCrawlPermission() == Settings.CRAWL_ALLOWED)
		{
			if (matchFlag)
			{
				flag = true && flag;
				log.debug("ALLOWED URL " + url + "\r\n");
			}
			else
			{
				log.debug("NOT ALLOWED URL " + url + "\r\n");
				
				flag = false;
			}
		}

		// -- enf synch cond }

		return flag;
	} */
}
