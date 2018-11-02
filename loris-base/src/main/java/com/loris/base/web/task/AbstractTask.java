package com.loris.base.web.task;

import com.loris.base.web.manager.Downloader;
import com.loris.base.web.util.LoaderMonitor;

public abstract class AbstractTask implements Task
{
	/** The manager. */
	protected Downloader downloader;
	
	/** The Monitor. */
	protected LoaderMonitor monitor;
	
	/** The task name. */
	protected String name;

	/**
	 * Get the Downloader.
	 */
	public Downloader getDownloader()
	{
		return downloader;
	}

	public void setDownloader(Downloader manager)
	{
		this.downloader = manager;
		setName(manager.getName());
	}
	
	/**
	 * Set the LoaderMonitor
	 * @param monitor The LoaderMonitor.
	 */
	public void setLoaderMonitor(LoaderMonitor monitor)
	{
		this.monitor = monitor;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	
	@Override
	public abstract void run();
}
