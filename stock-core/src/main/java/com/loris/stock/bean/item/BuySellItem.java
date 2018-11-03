package com.loris.stock.bean.item;

public class BuySellItem
{
	/**
	 * Volume statistic Type.
	 * 1: 按照价格统计： minPrice, maxPrice之间的买盘、卖盘
	 * 2: 按照成交量统计：minVolume, maxVolume之间遥买盘
	 * 
	 * @author usr
	 *
	 */
	public enum Type
	{
		Volume,
		Price
	}
	
	/** 常量定义 */
	public static final double MAX_PRICE = 1000.0f;
	public static final double MIN_PRICE = 0.0f;

	/** 买盘数量 */
	private double buyVol = 0.0f;
	
	/** 买盘金额 */
	private double buyAmt = 0.0f;
	
	/** 买盘成交数 */
	private int buyRecs = 0;
	
	/** 卖盘数量 */
	private double sellVol = 0.0f;
	
	/** 卖盘金额 */
	private double sellAmt = 0.0f;
	
	/** 卖盘成交笔数 */
	private int sellRecs = 0;
	
	/** 最小的金额*/
	private double minPrice= 0.0f;
	
	/** 最大的金额 */
	private double maxPrice = 1000.0f;
	
	/** 最小成交量 */
	private int minVolume = 0;
	
	/** 最大成交量*/
	private int maxVolume = 0; 
	
	/** 开始时间 */
	private String start = "";
	
	/** 结束时间 */
	private String end = "";
	
	/** 说明 */
	private String desc = "";
	
	/** */
	private BuySellItem.Type type;
	
	/**
	 * create a new instanceo of type.
	 * @param type
	 */
	public BuySellItem(Type type)
	{
		this.type = type;
	}
	
	/**
	 * Get the start description.
	 * @return
	 */
	public String getStart()
	{
		return start;
	}
	
	/**
	 * Get the end description.
	 * @return
	 */
	public String getEnd()
	{
		return end;
	}
	
	/**
	 * Get the Description.
	 * 
	 * @return
	 */
	public String getDescription()
	{
		return desc;
	}
	
	/**
	 * Reset the value of this item.
	 */
	public void reset()
	{
		this.buyAmt = 0.0f;
		this.buyVol = 0.0f;
		this.sellAmt = 0.0f;
		this.sellVol = 0.0f;
		this.buyRecs = 0;
		this.sellRecs = 0;
		
		if(type == Type.Price)
		{
			this.minVolume = Integer.MAX_VALUE;
			this.maxVolume = 0;
		}
		else if(type == Type.Volume)
		{
			this.minPrice = MAX_PRICE;
			this.maxPrice = MIN_PRICE;
		}
	}
	
	/**
	 * Add the buy volume.
	 * @param vol
	 */
	public void addBuyVolume(double vol)
	{
		this.buyVol += vol;
	}
	
	/**
	 * Get the buy volume.
	 * @return
	 */
	public double getBuyVolume()
	{
		return buyVol;
	}
	
	/**
	 * Get the buyMoney.
	 * 
	 * @return
	 */
	public double getBuyAmount()
	{
		return buyAmt;
	}
	
	/**
	 * Add the buy money.
	 * @param mon
	 */
	public void addBuyAmount(double mon)
	{
		this.buyAmt += mon;
	}
	
	/**
	 * Add the sell money.
	 * 
	 * @param mon
	 */
	public void addSellAmount(double mon)
	{
		this.sellAmt += mon;
	}
	
	/**
	 * Get the sell money.
	 * 
	 * @return
	 */
	public double getSellAmount()
	{
		return sellAmt;
	}
	
	/**
	 * Add the sell volume.
	 * @param vol
	 */
	public void addSellVolume(double vol)
	{
		this.sellVol += vol;
	}
	
	/**
	 * Get the sell volume.
	 * 
	 * @return
	 */
	public double getSellVolume()
	{
		return sellVol;
	}
	
	/**
	 * Get the total volume.
	 * @return
	 */
	public double getTotalVolume()
	{
		return sellVol + buyVol;
	}
	
	/**
	 * Get tht total amount.
	 * @return
	 */
	public double getTotalAmount()
	{
		return sellAmt + buyAmt;
	}
	
	/**
	 * Set the minimize price.
	 * @param price
	 */
	public void setMinPrice(double price)
	{
		this.minPrice = price;
	}
	
	/**
	 * Set the max price.
	 * @param price
	 */
	public void setMaxPrice(double price)
	{
		this.maxPrice = price;
	}
	
	/**
	 * Get the minimize price value.
	 * @return
	 */
	public double getMinPrice()
	{
		return minPrice;
	}
	
	/**
	 * Get the maxPrice.
	 * @return
	 */
	public double getMaxPrice()
	{
		return maxPrice;
	}
	
	/**
	 * Get the type of the item.
	 * @return
	 */
	public Type getType()
	{
		return type;
	}
	
	/**
	 * Set the type of the item.
	 * @param type
	 */
	public void setType(Type type)
	{
		this.type = type;
	}
	
	/**
	 * Set the max volume value.
	 * @param vol
	 */
	public void setMaxVolume(int vol)
	{
		this.maxVolume = vol;
	}
	
	/**
	 * Get the max volume.
	 * @return
	 */
	public int getMaxVolume()
	{
		return maxVolume;
	}
	
	/**
	 * Get the minimize volume.
	 * @return
	 */
	public int getMinVolume()
	{
		return minVolume;
	}
	
	/**
	 * Set the minimize volume.
	 * 
	 * @param vol
	 */
	public void setMinVolume(int vol)
	{
		this.minVolume = vol;
	}
	
	/**
	 * Set the sell rec number.
	 * @param rec
	 */
	public void setSellRecNum(int rec)
	{
		this.sellRecs = rec;
	}
	
	/**
	 * Get the sell rec number.
	 * @return
	 */
	public int getSellRecNum()
	{
		return sellRecs;
	}
	
	/**
	 * Add the sell record number
	 * @param rec
	 */
	public void addSellRecNum(int rec)
	{
		this.buyRecs += rec;
	}
	
	/**
	 * Add sell rec to 1;
	 */
	public void addSellRecNum()
	{
		this.sellRecs ++;
	}
	
	/**
	 * Add buy Rec.
	 */
	public void addBuyRecNum()
	{
		this.buyRecs ++;
	}
	
	/**
	 * Get the buy rec number.
	 * 
	 * @return
	 */
	public int getBuyRecNum()
	{
		return buyRecs;
	}
	
	/**
	 * Set the buy rec number.
	 * 
	 * @param rec
	 */
	public void setBuyRecNum(int rec)
	{
		this.buyRecs = rec;
	}
	
	/**
	 * Add the buyRecnum.
	 * @param rec
	 */
	public void addBuyRecNum(int rec)
	{
		this.buyRecs += rec;
	}
	
	/**
	 * Check if the price is included in this item.
	 * @param price
	 * @return
	 */
	public boolean containPrice(double price)
	{
		return minPrice <= price && maxPrice > price;
	}

	/**
	 * Check if the vol value is included in this item.
	 * @param vol
	 * @return
	 */
	public boolean containVolume(int vol)
	{
		return minVolume <= vol && maxVolume > vol;
	}
	
	/**
	 * Get the absolute amount value.
	 * @return
	 */
	public double getAbsoluteAmount()
	{
		return (buyAmt - sellAmt);
	}
	
	/**
	 * Format to the String value.
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		if(type == Type.Volume)
		{
			sb.append("成交量 (" + minVolume + "->" + maxVolume + ") ");
		}
		else if(type == Type.Price)
		{
			sb.append("成交价 (" + minPrice + "->" + maxPrice + ") ");
		}
		sb.append("买盘 (" + buyVol + ", " + buyAmt + ", " + buyRecs + ") - ");
		sb.append("卖盘 (" + sellVol + ", " + sellAmt + ", " + sellRecs + ") ");
		sb.append("差额 (" + (buyVol - sellVol) + ", " + (buyAmt - sellAmt) + ") ");
		return sb.toString();
	}
}
