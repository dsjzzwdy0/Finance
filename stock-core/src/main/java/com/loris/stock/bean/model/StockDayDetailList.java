package com.loris.stock.bean.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.loris.base.util.DateUtil;
import com.loris.stock.bean.item.BuySellItem;
import com.loris.stock.bean.item.DetailedItem;
import com.loris.stock.bean.item.StockDataItem;


public class StockDayDetailList
{
	/** The records */
	private List<DetailedItem> records = new ArrayList<DetailedItem>();
	
	/** The Date .*/
	private String date;
	
	/** The stock symbol. */
	private String symbol;
	
	/** The Open time. */
	final public static String openTime = "09:25";
	
	/** The close time. */
	final public static String closeTime = "15:00";
	
	/**
	 * Crate a new instance of DailyDataset.
	 */
	public StockDayDetailList()
	{
	}
	
	/**
	 * Create a new instance of DailyDataset.
	 * 
	 * @param date
	 */
	public StockDayDetailList(String symbol, String date)
	{
		this.symbol = symbol; 
		this.date = date;
	}
	
	/**
	 * Set the Date.
	 * 
	 * @param date
	 */
	public void setDate(String date)
	{
		this.date = date;
	}
	
	/**
	 * Set the Symbol.
	 * 
	 * @param symbol
	 */
	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}
	
	/**
	 * Get the Symbol.
	 * 
	 * @return
	 */
	public String getSymbol()
	{
		return symbol;
	}
	
	/**
	 * Add the item.
	 * 
	 * @param item
	 */
	public void addItem(DetailedItem item)
	{
		records.add(item);
	}
	
	/**
	 * is null
	 * @return
	 */
	public boolean isNull()
	{
		return records == null || records.size() <= 5;
	}
	
	/**
	 * Get the records.
	 * 
	 * @return
	 */
	public List<DetailedItem> getRecords()
	{
		return records;
	}
	
	/**
	 * The size of the dataset.
	 * @return
	 */
	public int size()
	{
		return records.size();
	}
	
	/**
	 * Get the Date.
	 * @return
	 */
	public String getDate()
	{
		return date;
	}
	
	/**
	 * Clear the record.
	 */
	public void clear()
	{
		records.clear();
	}
	
	/**
	 * 统计为左闭环，右开口:[minPrice, maxPrice)
	 * @param minPrice
	 * @param maxPrice
	 * @return
	 */
	public BuySellItem getBuySellItemByPrice(double minPrice, double maxPrice)
	{
		BuySellItem item = new BuySellItem(BuySellItem.Type.Price);
		item.reset();
		item.setMaxPrice(maxPrice);
		item.setMinPrice(minPrice);
		addToItemByPrice(item);
		return item;
	}
	
	/**
	 * Get the item value.
	 * @param minVolue
	 * @param maxVolume
	 * @return
	 */
	public BuySellItem getBuySellItemByVolume(int minVolue, int maxVolume)
	{
		BuySellItem item = new BuySellItem(BuySellItem.Type.Volume);
		item.reset();
		item.setMinVolume(minVolue);
		item.setMaxVolume(maxVolume);
		addToItemByVolume(item);
		return item;
	}
	
	/**
	 * add to item buy volume.
	 * @param item
	 */
	public void addToItemByVolume(BuySellItem item)
	{
		int minVol = Integer.MAX_VALUE, maxVol = 0;
		double minPrice = BuySellItem.MAX_PRICE;
		double maxPrice = BuySellItem.MIN_PRICE;
		
		for (DetailedItem detailedItem : records)
		{
			if(item.containVolume(detailedItem.getVolume()))
			{
				if(minVol > detailedItem.getVolume())
				{
					minVol = detailedItem.getVolume();
				}
				if(maxVol < detailedItem.getVolume())
				{
					maxVol = detailedItem.getVolume();
				}
				
				if(minPrice > detailedItem.getPrice())
				{
					minPrice = detailedItem.getPrice();
				}
				if(maxPrice < detailedItem.getPrice())
				{
					maxPrice = detailedItem.getPrice();
				}
				if(detailedItem.getType() == DetailedItem.Type.BUY)
				{
					item.addBuyAmount(detailedItem.getAmount());
					item.addBuyVolume(detailedItem.getVolume());
					item.addBuyRecNum();
				}
				if(detailedItem.getType() == DetailedItem.Type.SELL)
				{
					item.addSellAmount(detailedItem.getAmount());
					item.addSellVolume(detailedItem.getVolume());
					item.addSellRecNum();
				}
			}
		}
		/*
		if(minVol < item.getMinVolume())
		{
			item.setMinVolume(minVol);
		}
		if(maxVol > item.getMaxVolume())
		{
			item.setMaxVolume(maxVol);
		}*/
		
		if(minPrice < item.getMinPrice())
		{
			item.setMinPrice(minPrice);
		}
		if(maxPrice > item.getMaxPrice())
		{
			item.setMaxPrice(maxPrice);
		}
	}
	
	/**
	 * Add the statistic to the item.
	 * @param item
	 */
	public void addToItemByPrice(BuySellItem item)
	{		
		int minVol = Integer.MAX_VALUE, maxVol = 0;
		for (DetailedItem detailedItem : records)
		{
			if(item.containPrice(detailedItem.getPrice()))
			{
				if(minVol > detailedItem.getVolume())
				{
					minVol = detailedItem.getVolume();
				}
				if(maxVol < detailedItem.getVolume())
				{
					maxVol = detailedItem.getVolume();
				}
				if(detailedItem.getType() == DetailedItem.Type.BUY)
				{
					item.addBuyAmount(detailedItem.getAmount());
					item.addBuyVolume(detailedItem.getVolume());
					item.addBuyRecNum();
				}
				if(detailedItem.getType() == DetailedItem.Type.SELL)
				{
					item.addSellAmount(detailedItem.getAmount());
					item.addSellVolume(detailedItem.getVolume());
					item.addSellRecNum();
				}
			}
		}
		
		if(minVol < item.getMinVolume())
		{
			item.setMinVolume(minVol);
		}
		if(maxVol > item.getMaxVolume())
		{
			item.setMaxVolume(maxVol);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public StockDataItem getDataItem()
	{
		Date d = DateUtil.parseDay(date);
		if(d == null)
		{
			return null;
		}
				
		double open = 0.0;
		double close = 0.0;
		double high = Double.MIN_VALUE;
		double low = Double.MAX_VALUE;
		int volume = 0;
		int amount = 0;
		
		for (DetailedItem item : records)
		{
			if(item.getTime().startsWith(openTime))
			{
				open = item.getPrice();
			}
			if(item.getTime().startsWith(closeTime))
			{
				close = item.getPrice();
			}
			
			if(high < item.getPrice())
			{
				high = item.getPrice();
			}
			if(low > item.getPrice())
			{
				low = item.getPrice();
			}
			
			volume += item.getVolume();
			amount += item.getAmount();
		}		
		
		return new StockDataItem(d.getTime(), open, high, low, close, volume, amount);
	}

	/**
	 * Check if the same record.
	 * @param d
	 * @return
	 */
	public boolean isSameDataset(StockDayDetailList d)
	{
		return (symbol.equals(d.symbol) && date.equals(d.date));
	}
}
