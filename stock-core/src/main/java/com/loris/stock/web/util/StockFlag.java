package com.loris.stock.web.util;

import java.util.ArrayList;
import java.util.List;

import com.loris.stock.bean.data.table.StockInfo;

/**
 * 
 * @author usr
 *
 */
public class StockFlag
{
	public static final short FLAG_INIT = 0;
	public static final short FLAG_OK = 1;
	
	/** The stock symbol list. */
	private List<StockInfo> symbols = new ArrayList<StockInfo>();
	
	/** The flag. */
	private short flags[];
	
	/**
	 * Set the stocks list.
	 * @param stocks
	 */
	public void setStocks(List<StockInfo> stocks)
	{
		symbols.clear();
		flags = null;
		for (StockInfo stockInfo : stocks)
		{
			symbols.add(stockInfo);
		}
		flags = new short[symbols.size()];
	}
	
	/**
	 * Reset the flag.
	 */
	public void resetFlag()
	{
		if(flags == null)
			return;
		int size = flags.length;
		for(int i = 0; i < size; i ++)
		{
			flags[i] = FLAG_INIT;
		}
	}
	
	/**
	 * Get the size of the stocks.
	 * @return
	 */
	public int size()
	{
		return symbols.size();
	}
	
	/**
	 * Get the flag of the index value.
	 * @param index
	 * @return
	 */
	public int getFlag(int index)
	{
		return flags[index];
	}
	
	/**
	 * set the flag.
	 * @param symbol
	 */
	public void setFlag(String symbol)
	{
		int size = symbols.size();
		for(int i = 0; i < size; i ++)
		{
			if(symbols.get(i).getSymbol().equals(symbol))
			{
				flags[i] = FLAG_OK;
				return;
			}
		}
	}
	
	/**
	 * Set the flag value.
	 * @param index
	 * @param value
	 */
	public void setFlag(int index, short value)
	{
		flags[index] = value;
	}
	
	/**
	 * Get the stock info value.
	 * @param index
	 * @return
	 */
	public StockInfo getStockInfo(int index)
	{
		return symbols.get(index);
	}
}
