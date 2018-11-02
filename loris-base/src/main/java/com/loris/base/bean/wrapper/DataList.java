package com.loris.base.bean.wrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataList
{
	private int status;
	private int size;
	private int total; 
	private Object data;
	private String msg;
	private List<String> header;
	
	public int getTotal()
	{
		return total;
	}
	public void setTotal(int total)
	{
		this.total = total;
	}
	public String getMsg()
	{
		return msg;
	}
	public void setMsg(String msg)
	{
		this.msg = msg;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public int getSize()
	{
		return size;
	}
	public void setSize(int size)
	{
		this.size = size;
	}
	public Object getData()
	{
		return data;
	}
	public void setData(Object data)
	{
		this.data = data;
	}
	public List<String> getHeader()
	{
		return header;
	}
	public void setHeader(List<String> header)
	{
		this.header = header;
	}
	
	public static DataList ok()
	{
		DataList rest = new DataList();
		rest.setStatus(200);
		rest.setMsg("操作成功");
		return rest;
	}

	public static DataList ok(String msg)
	{
		DataList rest = ok();
		rest.setMsg(msg);
		return rest;
	}
	
	public static DataList okList(List<String> header, List<?> list)
	{
		DataList rest = ok();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("header", header);
		map.put("list", list);
		rest.setSize(list.size());
		rest.setData(map);
		return rest;
	}
	
	public static DataList okList(List<String> header, List<?> list, int total)
	{
		DataList rest = ok();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("header", header);
		map.put("list", list);
		rest.setSize(list.size());
		rest.setTotal(total);
		rest.setData(map);
		return rest;
	}
}
