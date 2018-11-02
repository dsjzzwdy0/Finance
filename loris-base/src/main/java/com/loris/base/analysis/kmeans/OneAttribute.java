package com.loris.base.analysis.kmeans;

import com.loris.base.analysis.base.Point;

public class OneAttribute implements Point
{
	/** The value of the instance. */
	private double value;
	
	/**
	 * One Attribute
	 */
	public OneAttribute()
	{
		value = 0.0;
	}
	
	/**
	 * Create a new instance of OneAttribute.
	 * 
	 * @param value
	 */
	public OneAttribute(double value)
	{
		this.value = value;
	}

	@Override
	public double getDistance(Point p)
	{
		if(!(p instanceof OneAttribute))
		{
			throw new UnsupportedOperationException();
		}
		OneAttribute o = (OneAttribute)p;
		return Math.abs(value - o.value);
	}

	@Override
	public void addPoint(Point p)
	{
		if(!(p instanceof OneAttribute))
		{
			throw new UnsupportedOperationException();
		}
		OneAttribute o = (OneAttribute)p;
		value += o.value;
	}

	@Override
	public void multiply(double k)
	{
		value *= k;		
	}

	public double getValue()
	{
		return value;
	}

	public void setValue(double value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return "OneAttribute [value=" + value + "]";
	}
}
