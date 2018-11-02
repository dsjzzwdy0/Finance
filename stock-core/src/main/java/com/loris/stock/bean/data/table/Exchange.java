package com.loris.stock.bean.data.table;

import java.io.Serializable;
import java.util.ArrayList;

import com.loris.base.util.SerialVersion;

/**
 * 交易所, 有几个属性数据：
 * 1、交易所名称:如上证A股、深圳A股、中小板、创业板；也可以是虚拟交易所：沪深A股等。
 * 2、交易所代码:唯一标识码
 * 3、交易所前缀:
 * 4、交易所的股票: sh600001等
 * @author viorel.gheba
 */
public class Exchange implements Serializable
{

	private static final long serialVersionUID = SerialVersion.APPVERSION;

	private String name;
	private String code;
	private String sufix;
	
	private ArrayList<StockInfo> stocks;

	public Exchange()
	{
		this("", "", "");
	}

	public Exchange(String exchange)
	{
		this(exchange, "", "");
	}
	
	public Exchange(String exchange, String code)
	{
		this(exchange, code, "");
	}

	public Exchange(String exchange, String code, String sufix)
	{
		this.name = exchange;
		this.code = code;
		this.sufix = sufix;
		stocks = new ArrayList<StockInfo>();
	}
		
	public Exchange(String exchange, String code, String sufix, int stockSize)
	{
		this.name = exchange;
		this.code = code;
		this.sufix = sufix;
		stocks = new ArrayList<StockInfo>(stockSize);
	}

	public void setName(String exchange)
	{
		this.name = exchange;
	}

	public String getName()
	{
		return name;
	}
	
    public boolean hasStock(StockInfo stock)
    {
        return stocks.contains(stock);
    }
	
	public String getCode()
	{
		return code;
	}
	
	public void setCode(String code)
	{
		this.code = code;
	}

	public void setSufix(String sufix)
	{
		this.sufix = sufix;
	}

	public String getSufix()
	{
		return sufix;
	}
	
	public void addStock(StockInfo stock)
	{
		if(!stocks.contains(stock))
			stocks.add(stock);
		
	}

	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		if (!(obj instanceof Exchange))
		{
			return false;
		}

		Exchange that = (Exchange) obj;
		if (!getName().equals(that.getName()))
		{
			return false;
		}
		if(!getCode().equals(that.getCode()))
		{
			return false;
		}
		if (!getSufix().equals(that.getSufix()))
		{
			return true;
		}		
		return true;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getName());
		sb.append(": ");
		sb.append(getSufix());
		return sb.toString();
	}

}
