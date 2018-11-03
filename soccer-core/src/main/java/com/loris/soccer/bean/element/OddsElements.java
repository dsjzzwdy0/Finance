package com.loris.soccer.bean.element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author dsj
 *
 * @param <T>
 */
public class OddsElements implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/** 数据列表 */
	protected List<OddsElement> items = new ArrayList<>();
	
	/**
	 * 添加数据元素
	 * @param key
	 * @param item
	 */
	public void add(OddsElement item)
	{
		items.add(item);
	}
	
	
	public List<OddsElement> getItems()
	{
		return items;
	}
	
	/*
	public Collection<OddsItem> values()
	{
		return items.values();
	}*/
	
	/**
	 * Clear the value.
	 */
	public void clear()
	{
		items.clear();
	}
}
