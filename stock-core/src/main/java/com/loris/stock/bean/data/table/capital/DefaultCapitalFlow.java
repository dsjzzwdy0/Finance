package com.loris.stock.bean.data.table.capital;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.entity.AutoIdEntity;

@TableName("stock_capital_default")
public class DefaultCapitalFlow extends AutoIdEntity
{
	private static final long serialVersionUID = 1L;
	private String symbol;
	private String day;
	private String name;

	private double smalltotal;    	// 0~300手	
	private double smalldiff;
	
	private double middletotal;    	// 300~600手
	private double middlediff;
	
	private double bigtotal;     	// 700~4000手
	private double bigdiff;
	
	private double supertotal;      // 4000手以上
	private double superdiff;
	
	/*
	@TableField(exist = false)
	private double total;
	@TableField(exist = false)
	private double totalsell;
	@TableField(exist = false)
	private double totalbuy;
	@TableField(exist = false)
	private double totaldiff;*/
	
	public String getSymbol()
	{
		return symbol;
	}
	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}
	public String getDay()
	{
		return day;
	}
	public void setDay(String day)
	{
		this.day = day;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public double getSmalltotal()
	{
		return smalltotal;
	}
	public void setSmalltotal(double smalltotal)
	{
		this.smalltotal = smalltotal;
	}
	public double getSmallsell()
	{
		return (smalltotal - smalldiff) / 2.0f;
	}

	public double getSmallbuy()
	{
		return (smalltotal + smalldiff) / 2.0f;
	}

	public double getSmalldiff()
	{
		return smalldiff;
	}
	public void setSmalldiff(double smalldiff)
	{
		this.smalldiff = smalldiff;
	}
	
	public double getMiddletotal()
	{
		return middletotal;
	}
	public void setMiddletotal(double middletotal)
	{
		this.middletotal = middletotal;
	}
	public double getMiddlesell()
	{
		return (middletotal - middlediff) / 2.0f;
	}

	public double getMiddlebuy()
	{
		return (middletotal + middlediff) / 2.0f;
	}

	public double getMiddlediff()
	{
		return middlediff;
	}
	public void setMiddlediff(double middlediff)
	{
		this.middlediff = middlediff;
	}
	public double getBigtotal()
	{
		return bigtotal;
	}
	public void setBigtotal(double bigtotal)
	{
		this.bigtotal = bigtotal;
	}
	public double getBigsell()
	{
		return (bigtotal - bigdiff) / 2.0f;
	}

	public double getBigbuy()
	{
		return (bigtotal + bigdiff) / 2.0f;
	}

	public double getBigdiff()
	{
		return bigdiff;
	}
	public void setBigdiff(double bigdiff)
	{
		this.bigdiff = bigdiff;
	}
	public double getSupertotal()
	{
		return supertotal;
	}
	public void setSupertotal(double supertotal)
	{
		this.supertotal = supertotal;
	}
	public double getSupersell()
	{
		return (supertotal - superdiff) / 2.0f;
	}
	public double getSuperbuy()
	{
		return (supertotal + superdiff) / 2.0f;
	}
	public double getSuperdiff()
	{
		return superdiff;
	}
	public void setSuperdiff(double superdiff)
	{
		this.superdiff = superdiff;
	}
	public double getTotal()
	{
		return smalltotal + middletotal + bigtotal + supertotal;
	}

	public double getTotalsell()
	{
		return getSmallsell() + getMiddlesell() + getBigsell() + getSupersell();
	}

	public double getTotalbuy()
	{
		return getSmallbuy() + getMiddlebuy() + getBigbuy() + getSuperbuy();
	}

	public double getTotaldiff()
	{
		return smalldiff + middlediff + bigdiff + superdiff;
	}
	@Override
	public String toString()
	{
		return "DefaultCapitalFlow [symbol=" + symbol + ", day=" + day + ", name=" + name + ", smalltotal=" + smalltotal
				+ ", smalldiff=" + smalldiff + ", middletotal=" + middletotal + ", middlediff=" + middlediff
				+ ", bigtotal=" + bigtotal + ", bigdiff=" + bigdiff + ", supertotal=" + supertotal + ", superdiff="
				+ superdiff + "]";
	}
}
