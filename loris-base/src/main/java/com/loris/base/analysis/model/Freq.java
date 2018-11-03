package com.loris.base.analysis.model;

import com.loris.base.data.PairValue;

public class Freq extends PairValue<String, Integer>
{
	/**
	 * Create a new instance of Counter.
	 */
	public Freq()
	{
		this("");
	}
	
	public Freq(String key)
	{
		this(key, 0);
	}
	
	public Freq(String key, int num)
	{
		this.key = key;
		this.value = num;
	}
	
	public void add(int num)
	{
		value += num;
	}

	@Override
	public String toString()
	{
		return "['" + key + "', " + value + "]";
	}
}
