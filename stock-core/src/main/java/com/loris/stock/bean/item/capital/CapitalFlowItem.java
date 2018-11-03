package com.loris.stock.bean.item.capital;

public class CapitalFlowItem implements Comparable<CapitalFlowItem>
{
	/** The error value. */
	public static double EPS = 0.0000000001;
	
	/** The name of the CapitalFlow. */
	private String name;
	
	/** The amount of the total flow. */
	//private double totalamount = 0.0;
	
	/** the amount of sell. */
	private double sellamount = 0.0;
	
	/** the amount of sell. */
	private double buyamount = 0.0;
	
	/** The parameter of first. */
	private int minparam = 0;
	
	/** The parameter of second. */
	private int maxparam = 0;
	
	/**
	 * Create a new instance of CapitalFlowItem.
	 */
	public CapitalFlowItem()
	{		
	}
	
	/**
	 * Create a new instance of CapitalFlowItem.
	 * @param name
	 */
	public CapitalFlowItem(String name)
	{
		this.name = name;
	}
	
	/**
	 * Create a new instance of CapitalFlowItem.
	 * @param name
	 * @param minParam
	 * @param maxParam
	 */
	public CapitalFlowItem(String name, int minParam, int maxParam)
	{
		this.name = name;
		this.minparam = minParam;
		this.maxparam = maxParam;
	}
	
	/**
	 * Add the Flow item value.
	 * @param item
	 */
	public void addFlowItemValue(CapitalFlowItem item)
	{
		addBuyamount(item.getBuyamount());
		addSellamount(item.getSellamount());
	}
	
	public int getMinparam()
	{
		return minparam;
	}

	public void setMinparam(int minparam)
	{
		this.minparam = minparam;
	}

	public int getMaxparam()
	{
		return maxparam;
	}

	public void setMaxparam(int maxparam)
	{
		this.maxparam = maxparam;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public double getDiff()
	{
		return buyamount - sellamount;
	}

	public double getTotalamount()
	{
		return buyamount + sellamount;
	}

	/*
	public void setTotalamount(double totalamount)
	{
		this.totalamount = totalamount;
	}*/

	@Override
	public String toString()
	{
		return "CapitalFlow [name=" + name+ ", minparam=" + minparam + ", maxparam=" + maxparam 
				+ ", buyamount=" + buyamount + ", sellamount=" + sellamount 
				+ ", totalamount=" + getTotalamount() + ", diff=" + getDiff() + "]";
	}

	public double getSellamount()
	{
		return sellamount;
	}

	public void setSellamount(double sellamount)
	{
		this.sellamount = sellamount;
	}

	public double getBuyamount()
	{
		return buyamount;
	}

	public void setBuyamount(double buyamount)
	{
		this.buyamount = buyamount;
	}
	
	public void addSellamount(double amount)
	{
		this.sellamount += amount;
		//this.totalamount += amount;
	}
	
	public void addBuyamount(double amount)
	{
		this.buyamount += amount;
		//this.totalamount += amount;
	}
	
	public void setParams(int min, int max)
	{
		this.minparam = min;
		this.maxparam = max;
	}
	
	/**
	 * check if contains the value.
	 * @param value
	 * @return
	 */
	public boolean contains(int value)
	{
		return (minparam <= value && value <= maxparam);
	}
	
	/**
	 * Check if this object contains the item.
	 * @param item
	 * @return
	 */
	public boolean contains(CapitalFlowItem item)
	{
		return (minparam <= item.getMinparam() && item.getMaxparam() <= maxparam);
	}
	
	/**
	 * Check if this object intersect with the item.
	 * @param item
	 * @return
	 */
	public boolean intersect(CapitalFlowItem item)
	{
		return !((minparam >= item.getMaxparam()) || maxparam <= item.getMinparam());
	}
	
	/**
	 * Reset the flow item.
	 */
	public void reset()
	{
		this.sellamount = 0.0;
		this.buyamount = 0.0;
		//this.totalamount = 0.0;
	}

	/**
	 * Compare two CapitalFlow.
	 */
	@Override
	public int compareTo(CapitalFlowItem o)
	{
		return Double.compare(minparam, o.getMinparam());
	}
}
