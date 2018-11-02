package com.loris.stock.bean.data.table;

import java.util.Calendar;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.entity.UserIDEntity;
import com.loris.base.util.DateUtil;

/**
 * 交易日期，是代表哪些日期有交易、哪些日期是非交易
 * 
 * @author dsj
 *
 */
@TableName("stock_trade_date")
public class TradeDate extends UserIDEntity
{
	private static final long serialVersionUID = 1L;
	private String date;				//日期字符串
	private int year;             		//年
	private int month;					//月
	private int day;					//日
	private int week;					//星期
	private boolean flag;				//是否交易日
	
	public TradeDate()
	{
	}
	
	public TradeDate(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH) + 1;
		day = calendar.get(Calendar.DAY_OF_MONTH);
		week = calendar.get(Calendar.DAY_OF_WEEK);
		this.date = DateUtil.formatDay(date);
		this.id = this.date;
		if(week == 6 || week == 0)
		{
			flag = false;
		}
		else
		{
			flag = true;
		}
	}
	
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public int getYear()
	{
		return year;
	}
	public void setYear(int year)
	{
		this.year = year;
	}
	public int getMonth()
	{
		return month;
	}
	public void setMonth(int month)
	{
		this.month = month;
	}
	public int getDay()
	{
		return day;
	}
	public void setDay(int day)
	{
		this.day = day;
	}
	public int getWeek()
	{
		return week;
	}
	public void setWeek(int week)
	{
		this.week = week;
	}
	public String getDate()
	{
		return date;
	}
	public void setDate(String date)
	{
		this.date = date;
	}
	public boolean isFlag()
	{
		return flag;
	}
	public void setFlag(boolean flag)
	{
		this.flag = flag;
	}
	@Override
	public String toString()
	{
		return "TradeDate [" + date	+ ", week = " + week +  ", flag=" + flag + "]";
	}
}
