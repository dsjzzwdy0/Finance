package com.loris.stock.bean.data.table;

public class CompanyChangeInfo extends StockInfo
{
	private static final long serialVersionUID = 1L;

	private String type;
	private String changetime;
	private String pubtime;
	private String precontent;
	private String destcontent;

	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getChangetime()
	{
		return changetime;
	}
	public void setChangetime(String changetime)
	{
		this.changetime = changetime;
	}
	public String getPubtime()
	{
		return pubtime;
	}
	public void setPubtime(String pubtime)
	{
		this.pubtime = pubtime;
	}
	public String getPrecontent()
	{
		return precontent;
	}
	public void setPrecontent(String precontent)
	{
		this.precontent = precontent;
	}
	public String getDestcontent()
	{
		return destcontent;
	}
	public void setDestcontent(String destcontent)
	{
		this.destcontent = destcontent;
	}
	@Override
	public String toString()
	{
		return "CompanyChangeInfo [type=" + type + ", symbol=" + symbol + ", changetime=" + changetime + ", pubtime=" + pubtime
				+ ", precontent=" + precontent + ", destcontent=" + destcontent  + "]";
	}
}
