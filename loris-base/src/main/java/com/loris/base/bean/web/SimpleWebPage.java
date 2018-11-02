package com.loris.base.bean.web;

import com.baomidou.mybatisplus.annotations.TableField;
import com.loris.base.bean.entity.AutoIdEntity;

public class SimpleWebPage extends AutoIdEntity
{
	/***/
	private static final long serialVersionUID = 1L;
	
	/** 名称,在整个系统中应该唯一 */
	protected String name;

	/** The type of the page. */
	protected String type;
	
	/** The encoding of the webpage. */
	protected String encoding;

	/** The http method */
	protected String method;
	
	/** 使用的协议: 如http、ftp等 */
	protected String protocol = "http";
	
	/** 主机地址, 如www.sina.com.cn */
	protected String host;
	
	/** 端口 */
	protected String port;

	/** URL String value. */
	protected String url;

	/** Is loaded from web. */
	protected boolean completed;

	/** The time to load the page. */
	protected String loadtime;

	/** The time to create the page. */
	protected String createtime;
	
	/** The http status. */
	protected int httpstatus = 0;
	
	/** The web content. */
	@TableField(exist=false)
	protected String content;

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
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

	public boolean isCompleted()
	{
		return completed;
	}

	public void setCompleted(boolean completed)
	{
		this.completed = completed;
	}

	public String getLoadtime()
	{
		return loadtime;
	}

	public void setLoadtime(String loadtime)
	{
		this.loadtime = loadtime;
	}

	public String getCreatetime()
	{
		return createtime;
	}

	public void setCreatetime(String createtime)
	{
		this.createtime = createtime;
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

	public int getHttpstatus()
	{
		return httpstatus;
	}

	public void setHttpstatus(int httpstatus)
	{
		this.httpstatus = httpstatus;
	}
	
	public String getFullURL()
	{
		return getUrl();
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

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	@Override
	public String toString()
	{
		return "SimpleWebPage [name=" + name + ", type=" + type + ", encoding=" + encoding + ", method=" + method
				+ ", protocol=" + protocol + ", host=" + host + ", port=" + port + ", url=" + url + ", completed="
				+ completed + ", loadtime=" + loadtime + ", createtime=" + createtime + ", httpstatus=" + httpstatus
				+ "]";
	}
}
