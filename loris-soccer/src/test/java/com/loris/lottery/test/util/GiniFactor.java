package com.loris.lottery.test.util;

import java.util.List;

import com.loris.base.analysis.base.Attribute;
import com.loris.base.analysis.base.AttributeFreqs;

public class GiniFactor implements Comparable<GiniFactor>
{
	private String attribute;
	private int total;
	private double point;
	private double parentFactor;
	private double deltaFactor;
	private AttributeFreqs left;
	private AttributeFreqs right;
	private double min;
	private double max;
	private String description;
	
	private GiniFactor(int num)
	{
		left = new AttributeFreqs(num);
		right = new AttributeFreqs(num);
	}
	
	/**
	 * Factor.
	 * 
	 * @param attribute
	 */
	public GiniFactor(String attribute, int num)
	{
		this(num);
		this.attribute = attribute;
	}
	
	/**
	 * Create the Attribute.
	 * 
	 * @param attribute
	 * @param attributes
	 */
	public GiniFactor(String attribute, List<Attribute<?>> attributes)
	{
		left = new AttributeFreqs(attributes);
		right = new AttributeFreqs(attributes);
		this.attribute = attribute;
	}
	
	public String getAttribute()
	{
		return attribute;
	}
	public void setAttribute(String attribute)
	{
		this.attribute = attribute;
	}
	public int getTotal()
	{
		return total;
	}
	public void setTotal(int total)
	{
		this.total = total;
	}
	public double getPoint()
	{
		return point;
	}
	public void setPoint(double point)
	{
		this.point = point;
	}

	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public AttributeFreqs getLeft()
	{
		return left;
	}
	public void setLeft(AttributeFreqs left)
	{
		this.left.setFreqs(left);
	}
	public AttributeFreqs getRight()
	{
		return right;
	}
	public void setRight(AttributeFreqs right)
	{
		this.right.setFreqs(right);
	}
	
	public double getParentFactor()
	{
		return parentFactor;
	}

	public void setParentFactor(double parentGini)
	{
		this.parentFactor = parentGini;
	}

	public double getDeltaFactor()
	{
		return deltaFactor;
	}

	public void setDeltaFactor(double deltaGini)
	{
		this.deltaFactor = deltaGini;
	}
	
	public double getMin()
	{
		return min;
	}

	public void setMin(double min)
	{
		this.min = min;
	}

	public double getMax()
	{
		return max;
	}

	public void setMax(double max)
	{
		this.max = max;
	}

	@Override
	public int compareTo(GiniFactor o)
	{
		return Double.compare(deltaFactor, o.deltaFactor);
	}
	
	@Override
	public String toString()
	{
		return "GiniFactor [attribute=" + attribute + ", total=" + total + ", point=" + point + ", parentGini="
				+ parentFactor + ", left=" + left + ", right=" + right + ", deltaGini=" + deltaFactor + ", description=("
				+ description + ")]";
	}
}
