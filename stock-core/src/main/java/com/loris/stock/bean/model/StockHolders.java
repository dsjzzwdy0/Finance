package com.loris.stock.bean.model;

import java.util.ArrayList;
import java.util.List;

import com.loris.stock.bean.data.table.StockHolder;

/**
 * 股东列表
 * @author usr
 *
 */
public class StockHolders
{
	/** The stock symbol. */
	private String symbol;
	
	/** The date of the holders. 是动态变化的。 */
	private String date;
	
	/** StockHolders. */
	private List<StockHolder> holders = new ArrayList<StockHolder>();
	
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
	 * Set the date of the holders.
	 * @param date
	 */
	public void setDate(String date)
	{
		this.date = date;
	}
	
	/**
	 * Get the date.
	 * @return
	 */
	public String getDate()
	{
		return date;
	}
	
	/**
	 * Add the holder. 
	 * @param holder
	 */
	public void addHolder(StockHolder holder)
	{
		holders.add(holder);
	}
	
	/**
	 * Get the holder by name.
	 * @param name
	 * @return
	 */
	public StockHolder getHolder(String name)
	{
		for (StockHolder stockHolder : holders)
		{
			if(name.equals(stockHolder.getName()))
			{
				return stockHolder;
			}
		}
		return null;
	}
	
	/**
	 * Get the size of the holders.
	 * 
	 * @return
	 */
	public int size()
	{
		return holders.size();
	}
	
	/**
	 * Get the holder by index.
	 * 
	 * @param index
	 * @return
	 */
	public StockHolder getHolder(int index)
	{
		return holders.get(index);
	}
}
