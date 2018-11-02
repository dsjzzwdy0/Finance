package com.loris.base.web.manager.event;

import java.util.EventObject;

import com.loris.base.web.manager.Downloader;

public class WebPageStatusEvent extends EventObject
{
	/** Serial Event. */
	private static final long serialVersionUID = 1L;

	/** The Status flag. */
	private int status;
	
	/**
	 * Create a new instance of WebPageEvent.
	 * 
	 * @param source
	 */
	public WebPageStatusEvent(Downloader source, int status)
	{
		super(source);
		this.status = status;
	}
	
	/**
	 * Get the WebPageEvent.
	 * 
	 * @return
	 */
	public int getStatus()
	{
		return status;
	}
	
	/**
	 * Get the WebPageManager.
	 * 
	 * @return
	 */
	public Downloader getWebPageDownloader()
	{
		return (Downloader)source;
	}
}
