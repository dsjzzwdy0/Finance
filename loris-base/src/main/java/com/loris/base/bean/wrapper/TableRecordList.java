package com.loris.base.bean.wrapper;

import java.util.List;

import com.loris.base.bean.entity.Entity;

public class TableRecordList
{
	private String clazzname;
	private List<Entity> records;
	
	public TableRecordList()
	{
	}
	
	public TableRecordList(String clazzName, List<Entity> entities)
	{
		this.clazzname = clazzName;
		this.records = entities;
	}
	
	public String getClazzname()
	{
		return clazzname;
	}
	public void setClazzname(String clazzname)
	{
		this.clazzname = clazzname;
	}
	public List<Entity> getRecords()
	{
		return records;
	}
	public void setRecords(List<Entity> records)
	{
		this.records = records;
	}
}
