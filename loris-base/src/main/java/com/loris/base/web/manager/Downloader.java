package com.loris.base.web.manager;

import java.io.Closeable;

import com.loris.base.bean.web.WebPage;
import com.loris.base.context.LorisContext;
import com.loris.base.web.config.setting.DownSetting;
import com.loris.base.web.http.UrlFetchException;
import com.loris.base.web.manager.event.WebPageStatusListener;


public interface Downloader extends Closeable
{
	/** The encoding of the Page manager. */
	public static final String ENCODING_GBK = "GBK";
	public static final String ENCODING_UTF8 = "UTF-8";
	public static final String ENCODING_GB2312 = "GB2312";
	
	/**
	 * Get the name of the manager.
	 * @return
	 */
	String getName();
	
	/**
	 * Set the name of the manager.
	 * @param name
	 */
	void setName(String name);
	
	/**
	 * Set the description value.
	 * 
	 * @param desc
	 * @return
	 */
	void setDescription(String desc);
	
	/**
	 * Get the Description.
	 * 
	 * @return
	 */
	String getDescription();
	
	/**
	 * Set the type of the manager.
	 * @param type
	 */
	void setType(String type);
	
	/**
	 * 设置运行时环境变量
	 * 
	 * @param context
	 */
	void setLorisContext(LorisContext context);
	
	/**
	 * Get the type of the manager.
	 * @return
	 */
	String getType();
	
	/**
	 * Set the interval value.
	 * 
	 * @param interval
	 */
	void setInterval(int interval);
	
	/**
	 * Check if stop the Manager.
	 * 
	 * @return
	 */
	boolean isStopped();
	
	/**
	 * Stop the manager.
	 */
	void stopDownloader();
	
	/**
	 * Restart the manager.
	 */
	void restartDownloader();
	
	/**
	 * Get the interval value. 
	 * 
	 * @return
	 */
	int getInterval();
	
	/**
	 * prepare for the WebPageManager.
	 */
	boolean prepare();
	
	/**
	 * After the prepared.
	 */
	void afterPrepared();
	
	/**
	 * Set the WebPageManager enable flag.
	 * @param enable
	 */
	void setEnable(boolean enable);
	
	/**
	 * Get ehe Enable flag.
	 * @return
	 */
	boolean getEnable();
	
	/**
	 * Do something before download.
	 * @param page
	 */
	void beforeDownload(WebPage page);
	
	/**
	 * This will process after download the page.
	 * @param page
	 * @param flag
	 */
	void afterDownload(WebPage page, boolean flag);
	
	/**
	 * 下载数据页面
	 * 
	 * @param page 网络页面
	 * @return 是否下载成功的标志
	 */
	boolean download(WebPage page) throws UrlFetchException;
	
	/**
	 * Get the current WebPage.
	 * @return
	 */
	WebPage popWebPage();
	
	/**
	 * Check there are next page to be downloaded.
	 * @return
	 */
	boolean hasNextWebPage();
	
	/**
	 * The size to be downloaded.
	 * @return
	 */
	int leftSize();
	
	/**
	 *  The total size.
	 *  
	 * @return
	 */
	int totalSize();
	
	/**
	 * The max number that download data at same time.
	 * @return The number value.
	 */
	int getMaxActiveThreadNum();
	
	/**
	 * The max number of download data thread at the same time.
	 * @param max The number value.
	 */
	void setMaxActiveThreadNum(int max);
	
	/**
	 * Set the status of manager.
	 * 
	 * @param status
	 */
	void setStatus(int status);
	
	/**
	 * Check if the Manager has been prepared.
	 */
	boolean isPrepared();
	
	/**
	 * Get the status value.
	 * 
	 * @return
	 */
	int getStatus();
	
	/**
	 * Get the WebPageInfo.
	 * 
	 * @return
	 */
	DownSetting getWebPageSetting();
	
	/**
	 * Set the WebPageInfo.
	 * 
	 * @param webPageInfo
	 */
	void setWebPageSetting(DownSetting webPageInfo);
		
	/**
	 * Increase the counter value.
	 */
	void incCounter();
	
	/**
	 * Check if the page has been downloaded.
	 * @param page
	 * @return
	 */
	boolean isDownloaded(WebPage page);
	
	/**
	 * Add the WebPageStatusListener.
	 * 
	 * @param listener
	 */
	void addWebPageStatusListener(WebPageStatusListener listener);
	
	/**
	 * Get the Task run mode.
	 * @return The TaskMode
	 */
	TaskMode getTaskMode();
	
	/**
	 * Set the TaskMode for the Downloader.
	 * @param mode The TaskMode
	 */
	void setTaskMode(TaskMode mode);
}
