package com.loris.base.web.task;

import com.loris.base.web.manager.Downloader;
import com.loris.base.web.util.LoaderMonitor;

public interface Task extends Runnable
{
	/**
	 * Get webPagemanager. 
	 * @return
	 */
	Downloader getDownloader();

	/**
	 * Set the web page manager.
	 * @param manager
	 */
	void setDownloader(Downloader manager);
	
	/**
	 * Set the LoaderMonitor.
	 * @param monitor The monitor
	 */
	void setLoaderMonitor(LoaderMonitor monitor);
	
	/**
	 * Set the name of the task.
	 * @param name
	 */
	void setName(String name);
	
	/**
	 * Get the task name.
	 * @return
	 */
	String getName();
	
}
