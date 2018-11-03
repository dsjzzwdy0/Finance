package com.loris.stock.web.downloader.sina.parser;

import java.util.ArrayList;
import java.util.List;

import com.loris.stock.bean.data.table.DailyRecord;

public class DailyRecordList
{
	private String code;
	private String day;
	private int count;
	List<String> fields;
	List<DailyRecord> items;
	
	public DailyRecordList()
	{
		fields = new ArrayList<String>();
		items = new ArrayList<DailyRecord>();
	}
	
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code = code;
	}
	public String getDay()
	{
		return day;
	}
	public void setDay(String day)
	{
		this.day = day;
	}
	public int getCount()
	{
		return count;
	}
	public void setCount(int count)
	{
		this.count = count;
	}
	public List<String> getFields()
	{
		return fields;
	}
	public void setFields(List<String> fields)
	{
		this.fields = fields;
	}
	public List<DailyRecord> getItems()
	{
		return items;
	}
	public void setItems(List<DailyRecord> items)
	{
		this.items = items;
	}

	@Override
	public String toString()
	{
		return "DailyRecordList [code=" + code + ", day=" + day + ", count=" + count + ", fields=" + fields + ", items="
				+ items + "]";
	}
}
