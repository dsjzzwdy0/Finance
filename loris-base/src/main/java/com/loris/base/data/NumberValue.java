package com.loris.base.data;

public class NumberValue<T extends Number>
{
	/** 用于浮点数据的比较. */
	private static double EPS = 0.000000000001;

	T lowerValue;
	T upperValue;
	T value;
	boolean isRegion;

	/**
	 * create a new instance of NumberValue.
	 */
	public NumberValue()
	{
		isRegion = false;
	}

	/**
	 * Create a new instance of NumberValue.
	 * 
	 * @param value
	 */
	public NumberValue(T value)
	{
		isRegion = false;
		this.value = value;
	}

	/**
	 * Create a new instance of NumberValue with two element.
	 * 
	 * @param lower
	 * @param upper
	 */
	public NumberValue(T lower, T upper)
	{
		isRegion = true;
		this.lowerValue = lower;
		this.upperValue = upper;
	}

	public T getValue()
	{
		return value;
	}

	public void setValue(T value)
	{
		this.value = value;
	}

	public T getLowerValue()
	{
		return lowerValue;
	}

	public void setLowerValue(T lowerValue)
	{
		this.lowerValue = lowerValue;
	}

	public T getUpperValue()
	{
		return upperValue;
	}

	public void setUpperValue(T upperValue)
	{
		this.upperValue = upperValue;
	}

	public boolean isRegion()
	{
		return isRegion;
	}

	public void setRegion(boolean isRegion)
	{
		this.isRegion = isRegion;
	}

	@Override
	public int hashCode()
	{
		if (isRegion)
		{
			return lowerValue.hashCode() + upperValue.hashCode();
		}
		else
		{
			return value.hashCode();
		}
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof NumberValue)
		{
			return o.hashCode() == this.hashCode();
		}
		return false;
	}

	/**
	 * Check if the value contains the other.
	 * 
	 * @param other
	 * @return
	 */
	public boolean contains(NumberValue<?> other)
	{
		if(other.isRegion)
		{
			return contains(other.getLowerValue(), other.getUpperValue());
		}
		else
		{
			return contains(other.getValue());
		}
	}

	/**
	 * Check if the NumberValue contains the value.
	 * @param n
	 * @return
	 */
	public boolean contains(Number n)
	{
		if (isRegion)
		{
			return lowerValue.doubleValue() - EPS <= n.doubleValue()
					&& upperValue.doubleValue() + EPS >= n.doubleValue();
		}
		else
		{
			if ((value instanceof Integer) && (n instanceof Integer))
			{
				return value.intValue() == n.intValue();
			}
			return value.doubleValue() - EPS <= n.doubleValue() && value.doubleValue() + EPS >= n.doubleValue();
		}
	}

	/**
	 * Check if the NumberValue contains the region value.
	 * @param lower
	 * @param upper
	 * @return
	 */
	public boolean contains(Number lower, Number upper)
	{
		if (isRegion)
		{
			return lowerValue.doubleValue() - EPS <= lower.doubleValue()
					&& upperValue.doubleValue() + EPS >= upper.doubleValue();
		}
		else
		{
			return false;
		}
	}

	@Override
	public String toString()
	{
		if(isRegion)
		{
			return "NumberValue Region [" + lowerValue + ", " + upperValue + "]";
		}
		else
		{
			return "NumberValue Point [" + value + "]";
		}
	}
}
