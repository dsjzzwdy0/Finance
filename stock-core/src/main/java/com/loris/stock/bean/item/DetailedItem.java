package com.loris.stock.bean.item;

import java.io.Serializable;


public class DetailedItem implements Serializable, Comparable<DetailedItem>
{		
	/** 序列化号 */
	private static final long serialVersionUID = 1L;
		

	/** 日期  */
	private String date;
	
	/** 成交时间 */
	private String time;
	
	/** 成交价格 */
	private double price;
	
	/** 变动量 */
	private double variation;
	
	/** 成交量 */
	private int volume;
	
	/** 成交总价 */
	private int total;
	
	/** 性质 :买盘0、卖盘1、中性盘2 */
	private String typeStr;
	
	/** 买卖盘性质 */
	private Type type;
	
	/**
	 * 买卖盘性质
	 * @author usr
	 *
	 */
	public enum Type
	{
		BUY,
		SELL, 
		MID
	}
	
	/**
	 * Create a new instance of DetailedItem.
	 */
	public DetailedItem()
	{		
	}
	
	/**
	 * Create a new instance of DetailedItem.
	 * @param date
	 */
	public DetailedItem(String date)
	{
		this.date = date;
	}
	
	/**
	 * Create a new instance of DetailedItem.
	 * @param date
	 * @param second
	 * @param price
	 * @param variable
	 * @param volume
	 * @param total
	 * @param type
	 */
	public DetailedItem(String date, String time, double price, double variation, int volume, int total, String type)
	{
		this.date = date;
		this.time = time;
		this.price = price;
		this.variation = variation;		
		this.volume = volume;
		this.total = total;
		this.typeStr = type;
	}
	
	/**
	 * Get the total value.
	 * 
	 * @return
	 */
	public int getAmount()
	{
		return total;
	}
	
	/**
	 * Set tht total value.
	 * 
	 * @param total
	 */
	public void setAmount(int total)
	{
		this.total = total;
	}
	
	/**
	 * Get the volume.
	 * 
	 * @return
	 */
	public int getVolume()
	{
		return volume;
	}
	
	/**
	 * Set the volume.
	 * 
	 * @param volume
	 */
	public void setVolume(int volume)
	{
		this.volume = volume;
	}
	
	/**
	 * Get the type.
	 * @return
	 */
	public String getTypeName()
	{
		return typeStr;
	}
	
	/**
	 * Set the Type value.
	 */
	public void setType(String type)
	{
		this.typeStr = type;
		this.type = getType(type);
	}
	
	/**
	 * Get the type.
	 * @return
	 */
	public Type getType()
	{
		return type;
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
	
	/**
	 * Get the variation.
	 * 
	 * @return
	 */
	public double getVariation()
	{
		return variation;
	}
	
	/**
	 * Set the price.
	 * @param price
	 */
	public void setPrice(double price)
	{
		this.price = price;
	}
	
	/**
	 * Get the price of the item.
	 * 
	 * @return
	 */
	public double getPrice()
	{
		return price;
	}
	
	/**
	 * Get the Date of the Item.
	 * 
	 * @return
	 */
	public String getDate()
	{
		return date;
	}
	
	/**
	 * Set the Date value of the item.
	 * 
	 * @param date
	 */
	public void setDate(String date)
	{
		this.date = date;
	}
	
	/**
	 * Get the second.
	 * 
	 * @return
	 */
	public String getTime()
	{
		return time;
	}
	
	/**
	 * Set the Second.
	 * 
	 * @param second
	 */
	public void setSecond(String time)
	{
		this.time = time;
	}
		
	/**
	 * Compare the two data item.
	 * @param o
	 * @return
	 */
	@Override
	public int compareTo(DetailedItem o)
	{
		return date.compareTo(o.date) & (time.equals(o.time) ? 0 : 1);
	}

	/**
	 * toString method.
	 */
	@Override
	public String toString()
	{
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(this.date + " ");
		stringBuffer.append(time);
		stringBuffer.append(" " + price);
		stringBuffer.append(" " + variation);
		stringBuffer.append(" " + volume);
		stringBuffer.append(" " + total);
		stringBuffer.append(" " + typeStr);
		return stringBuffer.toString();
	}
	
	/**
	 * Get the type of the String.
	 * @param type
	 * @return
	 */
	public static Type getType(String type)
	{
		if(type.contains("买"))
		{
			return Type.BUY;
		}
		else if(type.contains("卖"))
		{
			return Type.SELL;
		}
		else 
		{			
			return Type.MID;
		}
	}
}
