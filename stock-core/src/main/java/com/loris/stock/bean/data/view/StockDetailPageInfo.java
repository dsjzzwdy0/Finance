package com.loris.stock.bean.data.view;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.entity.AutoIdEntity;

@TableName("stock_web_capital_num")
public class StockDetailPageInfo extends AutoIdEntity
{
	/***/
	private static final long serialVersionUID = 1L;

	private String symbol;
	private String name;
	private String code;
	private String mindate;
	private String maxdate;
	private int num;
	public String getSymbol()
	{
		return symbol;
	}
	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code = code;
	}
	public String getMindate()
	{
		return mindate;
	}
	public void setMindate(String mindate)
	{
		this.mindate = mindate;
	}
	public String getMaxdate()
	{
		return maxdate;
	}
	public void setMaxdate(String maxdate)
	{
		this.maxdate = maxdate;
	}
	public int getNum()
	{
		return num;
	}
	public void setNum(int num)
	{
		this.num = num;
	}
	@Override
	public String toString()
	{
		return "StockDetailPageInfo [symbol=" + symbol + ", name=" + name + ", code=" + code + ", mindate=" + mindate
				+ ", maxdate=" + maxdate + ", num=" + num + "]";
	}
}
