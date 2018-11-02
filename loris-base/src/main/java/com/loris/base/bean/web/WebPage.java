package com.loris.base.bean.web;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.loris.base.util.DateUtil;

public class WebPage extends SimpleWebPage
{
	public final static String HTTP_METHOD_POST = "post";
	public final static String HTTP_METHOD_GET = "get";

	/** Serial version uid. */
	private static final long serialVersionUID = 1L;
	
	/** 是否需要保存页面内容 */
	@TableField(exist=false)
	protected boolean savecontent = false;
	
	/** The Zip type value. */
	@TableField(exist=false)
	protected String ziptype = "";
	
	/** The source object to download. */
	@TableField(exist=false)
	@JsonIgnore
	protected Object param;
	
	/** Is the result is byte. */
	@TableField(exist=false)
	@JsonIgnore
	protected boolean bytevalue;

	/** The table name to store the page. */
	@TableField(exist=false)
	protected String tablename;

	/** Is new from the Data. */
	@TableField(exist=false)
	protected boolean isNew = true;

	/** The byte array. */
	@TableField(exist=false)
	@JsonIgnore
	protected byte[] bytes;
	
	/** The headers to be post before request. */
	@TableField(exist=false)
	protected Map<String, String> headers;
	
	/** check if there are more header parameter. */
	@TableField(exist=false)
	protected boolean hasMoreHeader = false;

	public WebPage()
	{
		method = HTTP_METHOD_GET;
		bytevalue = false;
	}

	public String getZiptype()
	{
		return ziptype;
	}

	public void setZiptype(String ziptype)
	{
		this.ziptype = ziptype;
	}

	public String getTablename()
	{
		return tablename;
	}

	public void setTablename(String tableName)
	{
		this.tablename = tableName;
	}

	public boolean isNew()
	{
		return isNew;
	}

	public void setNew(boolean isNew)
	{
		this.isNew = isNew;
	}

	public byte[] getBytes()
	{
		return bytes;
	}

	public void setBytes(byte[] bytes)
	{
		this.bytes = bytes;
	}

	public boolean isBytevalue()
	{
		return bytevalue;
	}

	public void setBytevalue(boolean bytevalue)
	{
		this.bytevalue = bytevalue;
	}

	public Object getParam()
	{
		return param;
	}

	public void setParam(Object param)
	{
		this.param = param;
	}
	
	//请求数据示例
	public Map<String, String> getParams()
	{
		return null;
	}

	public Map<String, String> getHeaders()
	{
		return headers;
	}

	public void setHeaders(Map<String, String> headers)
	{
		this.headers = headers;
	}

	public boolean isHasMoreHeader()
	{
		return hasMoreHeader;
	}

	public void setHasMoreHeader(boolean hasMoreHeader)
	{
		this.hasMoreHeader = hasMoreHeader;
	}	

	public boolean isSavecontent()
	{
		return savecontent;
	}

	public void setSavecontent(boolean savecontent)
	{
		this.savecontent = savecontent;
	}

	/**
	 * 获得存储内容的唯一名字，实现的子类需要继承
	 * @return
	 */
	public String getContentFileName()
	{
		if(StringUtils.isEmpty(loadtime))
		{
			return System.currentTimeMillis() + "";
		}
		Date date = DateUtil.tryToParseDate(loadtime);
		if(date == null)
			throw new UnsupportedOperationException("The loadtime is null or invalid value.");
		return "" + date.getTime();
	}
	
	/**
	 * 获得目录地址：默认的按照年、月来组织
	 * @return 目录地址
	 */
	public String getPathName()
	{
		Date date = DateUtil.tryToParseDate(createtime);
		if(date == null)
		{
			throw new UnsupportedOperationException("The createtime is null or invalid value.");
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return "" + calendar.get(Calendar.YEAR) + File.separator + calendar.get(Calendar.MONTH) + File.separator;		
	}
	
	/**
	 * Add the header parameter.
	 * @param name The key name
	 * @param value The value.
	 */
	public void addHeader(String name, String value)
	{
		if(!hasMoreHeader)
		{
			throw new UnsupportedOperationException("Error, this page doesn't support speclial header parameter.");
		}
		
		if(headers == null)
		{
			headers = new HashMap<>();
		}
		headers.put(name, value);
	}

	@Override
	public String toString()
	{
		return "WebPage [url=" + url + ", type=" + type + ", completed=" + completed + ", loadtime=" + loadtime
				+ ", createtime=" + createtime + ", encoding=" + encoding + ", method=" + method + ", httpstatus="
				+ httpstatus + ", source=" + param + ", bytevalue=" + bytevalue + ", tablename=" + tablename
				+ ", isNew=" + isNew + "]";
	}
}
