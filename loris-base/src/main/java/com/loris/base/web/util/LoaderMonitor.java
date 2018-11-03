package com.loris.base.web.util;

import com.loris.base.web.manager.Downloader;

public class LoaderMonitor
{
	/** The Downloader. */
	private final Downloader loader;
	
	/** The active ThreadNum */
	private volatile int activeThreadNum = 0;
	
	/** The time to start. */
	private static long startTime;
	
	/** The watch. */
	public final Object watch = new Integer(0);
	
	/**
	 * Create a new instance of LoaderCrawler.
	 * @param downloader The Downloader.
	 */
	public LoaderMonitor(final Downloader downloader)
	{
		this.loader = downloader;
	}
	
	/** Check if is the downloader */
	public boolean isDownloader(Downloader downloader)
	{
		return loader == downloader;
	}

	/**
	 * 获得当前活跃下载线程
	 * @return
	 */
	public int getActiveThreadNum()
	{
		return activeThreadNum;
	}
	
	/**
	 * 加一个当前的活跃下载线程
	 */
	public void incActiveThreadNum()
	{
		activeThreadNum ++;
	}
	
	public static long getStartTime()
	{
		return startTime;
	}

	public static void setStartTime(long startTime)
	{
		LoaderMonitor.startTime = startTime;
	}
	
	/**
	 * 检测是否与这个名称相同
	 * @param name 名称
	 * @return 是否相符的标识
	 */
	public boolean isSameName(String name)
	{
		return name.equals(loader.getName());
	}

	/**
	 * 减少当前活跃下载线程
	 */
	public void decActiveThreaNum()
	{
		if(activeThreadNum > 0)
			activeThreadNum --;		
	}

	public Downloader getLoader()
	{
		return loader;
	}
}
