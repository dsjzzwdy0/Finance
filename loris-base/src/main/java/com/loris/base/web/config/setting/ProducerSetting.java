package com.loris.base.web.config.setting;

public class ProducerSetting
{
	protected String name;
	protected String clazz;
	protected int waitTime;
	protected boolean isOneTime;
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getClazz()
	{
		return clazz;
	}
	public void setClazz(String clazz)
	{
		this.clazz = clazz;
	}
	public int getWaitTime()
	{
		return waitTime;
	}
	public void setWaitTime(int waitTime)
	{
		this.waitTime = waitTime;
	}
	public boolean isOneTime()
	{
		return isOneTime;
	}
	public void setOneTime(boolean isOneTime)
	{
		this.isOneTime = isOneTime;
	}
	@Override
	public String toString()
	{
		return "ProducerSetting [name=" + name + ", clazz=" + clazz + ", waitTime=" + waitTime + ", isOneTime="
				+ isOneTime + "]";
	}
}
