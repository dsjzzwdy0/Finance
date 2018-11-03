package com.loris.base.data;

import java.util.ArrayList;
import java.util.List;

public class PairValueList<T, V>
{
	List<PairValue<T, V>> list = new ArrayList<>();
	
	public void add(PairValue<T, V> p)
	{
		list.add(p);
	}

	public List<PairValue<T, V>> getList()
	{
		return list;
	}
}
