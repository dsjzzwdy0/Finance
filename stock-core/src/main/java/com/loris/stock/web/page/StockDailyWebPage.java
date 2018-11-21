package com.loris.stock.web.page;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.web.page.WebPage;

@TableName("stock_web_daily_content")
public class StockDailyWebPage extends WebPage
{
	private static final long serialVersionUID = 1L;
	
	private int ordinary;
	private int pagenum;
	private int total;
	public int getOrdinary()
	{
		return ordinary;
	}
	public void setOrdinary(int ordinary)
	{
		this.ordinary = ordinary;
	}
	public int getPagenum()
	{
		return pagenum;
	}
	public void setPagenum(int pagenum)
	{
		this.pagenum = pagenum;
	}
	public int getTotal()
	{
		return total;
	}
	public void setTotal(int total)
	{
		this.total = total;
	}
}
