package com.loris.base.data;

import java.util.ArrayList;
import java.util.List;

public class Record<T, V>
{
	protected T key;
	protected List<V> list;
	
	public Record()
	{
		list = new ArrayList<>();
	}

	public T getKey()
	{
		return key;
	}

	public void setKey(T key)
	{
		this.key = key;
	}

	public List<V> getList()
	{
		return list;
	}

	public void setList(List<V> list)
	{
		this.list = list;
	}
	
	public void addRecord(V rec)
	{
		list.add(rec);
	}
	
	public int size()
	{
		return list.size();
	}
	
	public void clear()
	{
		list.clear();
	}
	
	public V getRecord(int index)
	{
		return list.get(index);
	}
	
	public V getLastRecord()
	{
		int size = size();
		if(size == 0)
		{
			throw new UnsupportedOperationException("There are no record.");
		}
		return list.get(size - 1);
	}
}
