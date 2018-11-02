package com.loris.base.data;

public class PairValue<T, V>
{
	protected T key;
	protected V value;
	
	public PairValue()
	{
	}
	
	public PairValue(T key, V value)
	{
		this.key = key;
		this.value = value;
	}
	
	public T getKey()
	{
		return key;
	}
	public void setKey(T key)
	{
		this.key = key;
	}
	public V getValue()
	{
		return value;
	}
	public void setValue(V value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return "PairValue [key=" + key + ", value=" + value + "]";
	}
}
