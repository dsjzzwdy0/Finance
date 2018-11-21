package com.loris.stock.web.page;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.web.page.WebPage;

@TableName("stock_web_capital_content")
public class StockDetailWebPage extends WebPage
{
	private static final long serialVersionUID = 1L;

	/** The symbol of the page. */
	protected String symbol = "";
	protected String day;
	
	@TableField(exist=false)
	protected String content;

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

	@Override
	public String toString()
	{
		return "StockDetailWebPage [symbol=" + symbol + ", content=" + content + ", url=" + url + ", completed="
				+ completed + ", loadtime=" + loadtime + ", createtime=" + createtime + ", encoding=" + encoding + "]";
	}	
}
