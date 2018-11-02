package com.loris.base.web.config.setting;

/**
 * 下载页面配置信息
 * @author deng
 *
 */
public class PageSetting
{
	private boolean savecontent;			//是否存储内容
	private String name;					//名称
	private String type;					//类型
	private String encoding;				//页面编号
	private String protocol;				//协议
	private String host;					//主机地址
	private String port;					//端口
	private String method;					//下载的方式
	private String basedir;					//存储路径
	
	public boolean isSavecontent()
	{
		return savecontent;
	}
	public void setSavecontent(boolean savecontent)
	{
		this.savecontent = savecontent;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getEncoding()
	{
		return encoding;
	}
	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}
	public String getMethod()
	{
		return method;
	}
	public void setMethod(String method)
	{
		this.method = method;
	}
	public String getBasedir()
	{
		return basedir;
	}
	public void setBasedir(String basedir)
	{
		this.basedir = basedir;
	}
	public String getProtocol()
	{
		return protocol;
	}
	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}
	public String getHost()
	{
		return host;
	}
	public void setHost(String host)
	{
		this.host = host;
	}
	public String getPort()
	{
		return port;
	}
	public void setPort(String port)
	{
		this.port = port;
	}
	@Override
	public String toString()
	{
		return "PageSetting [savecontent=" + savecontent + ", name=" + name + ", type="
				+ type + ", encoding=" + encoding + ", protocol=" + protocol + ", host=" + host + ", port=" + port
				+ ", method=" + method + ", basedir=" + basedir + "]";
	}
}
