package com.loris.base.analysis.base;

import com.loris.base.data.NumberValue;

public class Attribute<T>
{
	/**The Attribute Name. */
	protected String attributeName;
	
	/** The Attribute value. */
	protected T value;
	
	/** Flag of attribute value type. */
	protected boolean isContinuous;
	
	/** The Attribute. */
	public Attribute()
	{
	}
	
	/**
	 * Create a new instance of Attribute.
	 * @param attributeName
	 * @param value
	 */
	public Attribute(String attributeName, T value)
	{
		this.attributeName = attributeName;
		this.value = value;
		if(value instanceof NumberValue)
		{
			isContinuous = ((NumberValue<?>)value).isRegion();
		}
		else
		{
			isContinuous = false;
		}
	}
	
	/**
	 * Get the Attribute Name.
	 * 
	 * @return
	 */
	public String getAttributeName()
	{
		return attributeName;
	}
	
	/**
	 * Get the Attribute Value.
	 * 
	 * @return
	 */
	public T getAttributeValue()
	{
		return value;
	}
	
	/**
	 * Is continuous value.
	 * 
	 * @return
	 */
	public boolean isContinuous()
	{
		return isContinuous;
	}
	
	public void setContinuous(boolean isContinuous)
	{
		this.isContinuous = isContinuous;
	}

	public T getValue()
	{
		return value;
	}

	public void setValue(T value)
	{
		this.value = value;
	}

	public void setAttributeName(String attributeName)
	{
		this.attributeName = attributeName;
	}

	/**
	 * Check if the Attribute contains the other attribute.
	 * 
	 * @param attribute
	 * @return
	 */
	public boolean contains(Attribute<?> other)
	{
		if(!attributeName.equals(other.getAttributeName()))
		{
			return false;
		}
		if(value instanceof NumberValue)
		{
			if(other.getAttributeValue() instanceof NumberValue )
			{
				NumberValue<?> o = (NumberValue<?>)other.getAttributeValue();
				return ((NumberValue<?>)value).contains(o);
			}
			else if(other.getAttributeValue() instanceof Number)
			{
				Number o = (Number)other.getAttributeValue();
				return ((NumberValue<?>)value).contains(o);
			}
		}
		return value.equals(other.getAttributeValue());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		int hashCode = attributeName.hashCode();
		hashCode += value.hashCode();
		return hashCode;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (o instanceof Attribute)
		{
			return o.hashCode() == this.hashCode();
		}
		return false;
	}

	@Override
	public String toString()
	{
		return "Attribute [attributeName=" + attributeName + ", value=" + value + ", isConinuous=" + isContinuous + "]";
	}
}
