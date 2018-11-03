package com.loris.stock.bean.data.table;

import com.loris.base.bean.entity.AutoIdEntity;

/**
 * 股东类
 * @author usr
 *
 */
public class StockHolder extends AutoIdEntity
{
	private static final long serialVersionUID = 1L;

	/** 姓名 */
	private String name;
	
	/** 股东性质 */
	private String type;
	
	/** 股票 */
	private String symbol;
	
	/** 占股比例 */
	private float ratio;
	
	/** 持股数 */
	private double shares;
	
	/** 较上期增减变化量  */
	private double variation;
	
	/**
	 * Create a new instance of StockHolder.
	 */
	public StockHolder()
	{		
	}
	
	/**
	 * Set the symbol of the holder.
	 * 
	 * @param symbol
	 */
	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}
	
	/**
	 * Get the symbol of the holder.
	 * @return
	 */
	public String getSymbol()
	{
		return symbol;
	}
	
	/**
	 * Set the name of the holder.
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Get the name of the holder.
	 * @return
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Get the type of the holder.
	 * @return
	 */
	public String getType()
	{
		return type;
	}
	
	/**
	 * Set the type of the holder.
	 * @param type
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	
	/**
	 * Get the ratio of the holder.
	 * 
	 * @return
	 */
	public float getRatio()
	{
		return ratio;
	}
	
	/**
	 * Set the ratio of the holder.
	 * 
	 * @param ratio
	 */
	public void setRatio(float ratio)
	{
		this.ratio = ratio;
	}
	
	/**
	 * num of shares.
	 * @return
	 */
	public double getShares()
	{
		return shares;
	}
	
	/**
	 * Set the num of the share.
	 * @param num
	 */
	public void setShare(double num)
	{
		this.shares = num;
	}
	
	/**
	 * Get the variation
	 * 
	 * @return
	 */
	public double getVariation()
	{
		return variation;
	}
	
	/**
	 * Set the variation.
	 * 
	 * @param variation
	 */
	public void setVariation(double variation)
	{
		this.variation = variation;
	}
}
