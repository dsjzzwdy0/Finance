/*
 * @Author Irakli Nadareishvili
 * CVS-ID: $Id: Settings.java,v 1.1 2004/11/30 22:47:42 idumali Exp $
 *
 * Copyright (c) 2004 Development Gateway Foundation, Inc. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Common Public License v1.0 which accompanies this
 * distribution, and is available at:
 * http://www.opensource.org/licenses/cpl.php
 *
 *****************************************************************************/

package com.loris.base.web.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.HashSet;
import java.util.Collections;

public class Settings
{

	public static final boolean CRAWL_ALLOWED = true;
	public static final boolean CRAWL_DENIED = false;

	private int interval;
	private int monitorInterval;
	private int connectionTimeout;
	private Map<String, String> headers;
	private Set<String> crawlUrls;

	private boolean crawlPermission;
	private Set<String> urlPatterns;
	private Set<Pattern> urlPatternsCompiled;

	public Settings()
	{
		// crawlPermission = Settings.CRAWL_DENIED;

		this.headers = new HashMap<String, String>();
		this.urlPatterns = new HashSet<String>();
		this.urlPatternsCompiled = Collections.synchronizedSet(new HashSet<Pattern>());
		this.crawlUrls = new HashSet<String>();

	}

	public boolean getCrawlPermission()
	{
		return crawlPermission;
	}

	/**
	 * This method exists solely for unit-testing purposes.
	 * 
	 * @param permission
	 *            boolean
	 */
	public void setCrawlPermission(boolean permission)
	{
		crawlPermission = permission;
	}

	public void addCrawlUrl(ParamMapEntry url)
	{
		this.crawlUrls.add(url.getKey());
	}

	public void addHeader(ParamMapEntry entry)
	{
		this.headers.put(entry.getKey(), entry.getValue());
	}

	public void addUrlPattern(ParamMapEntry pattern)
	{
		this.urlPatterns.add(pattern.getKey());
	}
	
	

	public int getConnectionTimeout()
	{
		return connectionTimeout;
	}

	public Set<String> getCrawlUrls()
	{
		return crawlUrls;
	}

	public Map<String, String> getHeaders()
	{
		return headers;
	}

	public int getInterval()
	{
		return interval;
	}

	public int getMonitorInterval()
	{
		return monitorInterval;
	}

	public Set<String> getUrlPatterns()
	{
		return urlPatterns;
	}

	public Set<Pattern> getUrlPatternsCompiled()
	{
		return urlPatternsCompiled;
	}

	public void setConnectionTimeout(int connectionTimeout)
	{
		this.connectionTimeout = connectionTimeout;
	}

	public void setCrawlUrls(Set<String> crawlUrls)
	{
		this.crawlUrls = crawlUrls;
	}

	public void setHeaders(Map<String, String> headers)
	{
		this.headers = headers;
	}

	public void setInterval(int interval)
	{
		this.interval = interval;
	}

	public void setMonitorInterval(int interval)
	{
		this.monitorInterval = interval;
	}

	public void setUrlPatterns(Set<String> urlPatterns)
	{
		this.urlPatterns = urlPatterns;
	}
}
