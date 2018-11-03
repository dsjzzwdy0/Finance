package com.loris.base.data;

import java.util.HashMap;
import java.util.Map;

public class StatDataset<T, V>
{
	/** 数据记录*/
	Map<String, PairValueList<T, V>> records = new HashMap<>();
	
	/**
	 * 添加数据记录值
	 * @param category
	 * @param name
	 * @param value
	 */
	public void addValue(String category, T name, V value)
	{
		PairValue<T, V> p = new PairValue<T, V>(name, value);
		addPairValue(category, p);
	}
	
	/**
	 * 加入数据记录值
	 * @param category
	 * @param p
	 */
	protected void addPairValue(String category, PairValue<T, V> p)
	{
		PairValueList<T, V> list = records.get(category);
		if(list == null)
		{
			list = new PairValueList<>();
			records.put(category, list);
		}
		list.add(p);
	}
	
	/**
	 * 获得数据记录值
	 * @return
	 */
	public Map<String, PairValueList<T, V>> getRecords()
	{
		return records;
	}
}
