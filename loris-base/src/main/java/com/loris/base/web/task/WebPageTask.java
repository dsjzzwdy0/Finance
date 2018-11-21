package com.loris.base.web.task;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;

import com.loris.base.web.http.UrlFetchException;
import com.loris.base.web.manager.Downloader;
import com.loris.base.web.page.WebPage;
import com.loris.base.web.util.LoaderMonitor;
import com.loris.base.web.util.URLUtil;

public class WebPageTask extends AbstractTask
{
	private static Logger log = Logger.getLogger(WebPageTask.class);
	
	/** The manager. */
	protected Downloader downloader;
	
	/** The Monitor. */
	protected LoaderMonitor monitor;

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
	
	/**
	 * Create the WebPageManager instance.
	 */
	public WebPageTask()
	{
	}

	

	/**
	 * Create the instance of WebPageTask.
	 * @param type
	 * @param manager
	 */
	public WebPageTask(Downloader manager)
	{
		this.setDownloader(manager);
	}


	/**
	 * Run the task.
	 */
	@Override
	public void run()
	{
		log.debug("Starting FetcherTask");
		//Monitor.numOfActiveThreads++;
		monitor.incActiveThreadNum();

		String pageInfo = "";
		// If there is anything to process, do process
		if (downloader != null & downloader.hasNextWebPage())
		{			
			try
			{
				WebPage page = downloader.popWebPage();
				downloader.incCounter();
				
				pageInfo = page.toString();				
				log.info("Begin processing page: " + pageInfo);
				boolean flag = downloader.download(page);
				if(page.getHttpstatus() == HttpStatus.SC_FORBIDDEN)
				{
					log.info("The site[" + URLUtil.getHost(page.getFullURL()) + 
							"] has Forbiden downloading "
							+ page + ". This will stop the Downloader thread.");
					downloader.stopDownloader();
				}
				else
				{
					downloader.afterDownload(page, flag);
				}
			}
			catch (UrlFetchException ex1)
			{
				ex1.printStackTrace();				
				log.warn("Could not fetch page: " + pageInfo + "->" + ex1.getMessage());
			}	
			catch(Throwable e)
			{
				e.printStackTrace();				
				log.warn("Could not fetch page: " + pageInfo + "->" + e.getMessage());
			}
		}
		else
		{			
			log.debug("No Page left in crawler");
		}
		
		log.info("Stopping " + name + ".");
		monitor.decActiveThreaNum();
	}
}
