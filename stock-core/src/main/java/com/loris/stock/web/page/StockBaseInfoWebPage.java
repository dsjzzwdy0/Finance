package com.loris.stock.web.page;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.web.WebPage;

@TableName("stock_web_info_content")
public class StockBaseInfoWebPage extends WebPage
{
	private static final long serialVersionUID = 1L;

	protected String symbol;

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}	
}
