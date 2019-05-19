package com.loris.base.data;

public class Region<T extends Number>
{
	T min;
	T max;
	
	public Region()
	{
	}
	
	public Region(T min, T max)
	{
		this.min = min;
		this.max = max;
	}
	
	public T getMin()
	{
		return min;
	}
	public void setMin(T min)
	{
		this.min = min;
	}
	public T getMax()
	{
		return max;
	}
	public void setMax(T max)
	{
		this.max = max;
	}
}
