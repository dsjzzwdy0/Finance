package com.loris.base.web.util;

import java.net.MalformedURLException;
import java.net.URL;

public class URLUtil
{
	/**
	 * 获得URL地址的主机
	 * 
	 * @param url The URL String value.
	 * @return The host string value.
	 */
	public static String getHost(String url)
	{
		try
		{
			return new URL(url).getHost();		
		}
		catch(MalformedURLException e)
		{			
		}
		return "";
	}
}
