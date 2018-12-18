package com.loris.soccer.repository;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.entity.Entity;
import com.loris.base.bean.wrapper.TableRecordList;
import com.loris.base.web.http.UrlFetchException;
import com.loris.base.web.http.UrlFetcher;
import com.loris.base.web.page.WebPage;

public class RemoteSoccerManager
{
	private static Logger logger = Logger.getLogger(RemoteSoccerManager.class);

	protected String host;
	protected String uri;
	protected String port;
	protected String encoding = "utf-8";

	/**
	 * 保存到远程数据
	 * 
	 * @param entities
	 * @return
	 * @throws UrlFetchException 
	 */
	public String saveEntities(List<? extends Entity> entities) throws UrlFetchException, UnsupportedEncodingException
	{
		if(entities == null || entities.isEmpty())
		{
			logger.info("Error, there are no entities to save to remote.");
		}
		String clazzname = entities.get(0).getClass().getName();
		logger.info("Saving " + entities.size() + " " + clazzname + "...");
		
		WebPage page = createWebPage(clazzname, entities);
		if(UrlFetcher.fetch(page))
		{
			return page.getContent();
		}
		else
		{
			return "Error when post to: " + page.getUrl();
		}
	}
	
	/**
	 * 创建数据页面
	 * @param clazzname
	 * @param entities
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	protected WebPage createWebPage(String clazzname, List<? extends Entity> entities) throws UnsupportedEncodingException
	{
		WebPage page = new WebPage();
		page.setUrl(getBaseUrl());
		page.setEncoding(encoding);
		page.setMethod(WebPage.HTTP_METHOD_POST);
		String json = toJson(clazzname, entities);
		//json = URLEncoder.encode(json, encoding);
		//logger.info("json: " + json);
		page.addParam("json", json);
		return page;
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	protected String toJson(String clazzname, List<? extends Entity> entities)
	{
		TableRecordList list = new TableRecordList();
		list.setClazzname(clazzname);
		list.setRecords(entities);
		return JSON.toJSONString(list);
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public String getUri()
	{
		return uri;
	}

	public void setUri(String uri)
	{
		this.uri = uri;
	}

	public String getPort()
	{
		return port;
	}

	public void setPort(String port)
	{
		this.port = port;
	}

	public String getEncoding()
	{
		return encoding;
	}

	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}
	
	public String getBaseUrl()
	{
		return "http://" + host + (StringUtils.isEmpty(port) ? "" : ":" + port) + uri;
	}
	
	public void setRemoteSoccerManagerInfo(RemoteSoccerManager manager)
	{
		if(StringUtils.isNotEmpty(manager.getHost()))
		{
			this.host = manager.getHost();
		}
		if(StringUtils.isNotEmpty(manager.getEncoding()))
		{
			this.encoding = manager.getEncoding();
		}
		if(StringUtils.isNotEmpty(manager.getUri()))
		{
			this.uri = manager.getUri();
		}
		if(StringUtils.isNotEmpty(manager.getPort()))
		{
			this.port = manager.getPort();
		}
	}

	@Override
	public String toString()
	{
		return "RemoteSoccerManager [" + getBaseUrl() + "]";
	}
}
