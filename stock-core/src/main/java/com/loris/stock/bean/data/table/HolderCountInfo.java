package com.loris.stock.bean.data.table;

import com.baomidou.mybatisplus.annotations.TableName;

@TableName("stock_holder_count")
public class HolderCountInfo extends StockInfo
{
	private static final long serialVersionUID = 1L;
	
	private String pubtime;			//公告时间
	private double totalcount;		//总股数
	private double pershares;		//每股分权益数
	private double totalshares;		//总权益数
	private double circshares; 		//环比权益数

	public String getPubtime()
	{
		return pubtime;
	}
	public void setPubtime(String pubtime)
	{
		this.pubtime = pubtime;
	}
	public double getTotalcount()
	{
		return totalcount;
	}
	public void setTotalcount(double totalcount)
	{
		this.totalcount = totalcount;
	}
	public double getPershares()
	{
		return pershares;
	}
	public void setPershares(double pershares)
	{
		this.pershares = pershares;
	}
	public double getTotalshares()
	{
		return totalshares;
	}
	public void setTotalshares(double totalshares)
	{
		this.totalshares = totalshares;
	}
	public double getCircshares()
	{
		return circshares;
	}
	public void setCircshares(double circshares)
	{
		this.circshares = circshares;
	}
	@Override
	public String toString()
	{
		return "HolderCountInfo [pubtime=" + pubtime + ", totalcount=" + totalcount + ", pershares=" + pershares
				+ ", totalshares=" + totalshares + ", circshares=" + circshares + ", symbol=" + symbol + "]";
	}	
}
