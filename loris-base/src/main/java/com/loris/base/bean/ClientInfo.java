package com.loris.base.bean;

import java.util.Date;

/**
 * 
 * @author deng
 *
 */
public class ClientInfo
{
	protected String addr;
	protected String host;
	protected String user;
	protected int port;
	protected Date connecttime;
	
	public String getAddr()
	{
		return addr;
	}
	public void setAddr(String addr)
	{
		this.addr = addr;
	}
	public String getHost()
	{
		return host;
	}
	public void setHost(String host)
	{
		this.host = host;
	}
	public String getUser()
	{
		return user;
	}
	public void setUser(String user)
	{
		this.user = user;
	}
	
	public Date getConnecttime()
	{
		return connecttime;
	}
	public void setConnecttime(Date connecttime)
	{
		this.connecttime = connecttime;
	}
	public int getPort()
	{
		return port;
	}
	public void setPort(int port)
	{
		this.port = port;
	}
	@Override
	public String toString()
	{
		return "ClientInfo [addr=" + addr + ", host=" + host + ", user=" + user + ", port=" + port + ", updatetime="
				+ connecttime + "]";
	}
}
