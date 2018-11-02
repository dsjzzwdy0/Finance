package com.loris.stock.bean.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.loris.stock.bean.data.table.StockInfo;
import com.loris.stock.bean.item.DataItem;
import com.loris.stock.bean.item.interval.Interval;
import com.loris.stock.bean.item.util.DataHeader;
import com.loris.stock.bean.item.util.Range;

public class Dataset implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	
	public static float RANGE_SPACE = 0.1f;

	/** The Dataset Header. */
	protected DataHeader header = null;

	/** The DataItem value. */
	protected List<DataItem> data = null;
	
	/** The interval value. */
	protected Interval interval = null;
	
	/** The Stock value. */
	protected StockInfo stock = null;
	
	/** The Data whether is from the stock source data. */
	protected boolean isSourceValue = true;
	
	/** The parent dataset. */
	protected StockDataset parent = null;
	
	/**
	 * Create a new instance of Dataset.
	 * 
	 * @param header
	 */
	public Dataset(DataHeader header)
	{
		this(header, null);
	}

	/**
	 * 
	 * @param stock
	 * @param interval
	 * @param list
	 */
	public Dataset(DataHeader header, List<DataItem> list)
	{
		this(header, list, null, null , true);
	}
	
	/**
	 * 
	 * @param stock
	 * @param interval
	 * @param list
	 * @param isSource
	 */
	public Dataset(DataHeader header, List<DataItem> list, StockInfo stock, Interval interval)
	{
		this(header, list, stock, interval, true);
	}
	
	/**
	 * 
	 * @param stock
	 * @param interval
	 * @param list
	 * @param isSource
	 */
	public Dataset(DataHeader header, List<DataItem> list, StockInfo stock, Interval interval, boolean isSource)
	{
		this.stock = stock;
		this.interval = interval;
		this.data = list == null ? new ArrayList<DataItem>() : list;
		this.header = header;
		this.isSourceValue = isSource;
		this.parent = null;
	}
	
	/**
	 * The Parent of the dataset, default is null.
	 * 
	 * @return
	 */
	public StockDataset getParent()
	{
		return parent;
	}
	
	/**
	 * Set the parent.
	 * @param parent
	 */
	public void setParent(StockDataset parent)
	{
		this.parent = parent;
	}
	
	
	/**
	 * Check this is the source value.
	 * @return
	 */
	public boolean isSource()
	{
		return isSourceValue;
	}
	
	/**
	 * Get the DataHeader.
	 * @return
	 */
	public DataHeader getDataHeader()
	{
		return header;
	}
	
	/**
	 * Set the Data Header.
	 * @param header
	 */
	public void setDataHeader(DataHeader header)
	{
		this.header = header;
	}

	/**
	 * The interval of the Dataset.
	 * 
	 * @param interval
	 */
	public void setInterval(Interval interval)
	{
		this.interval = interval;
	}

	/**
	 * 
	 * @return
	 */
	public Interval getInterval()
	{
		return interval;
	}

	/**
	 * This is the stock represented.
	 * 
	 * @param stock
	 */
	public void setStock(StockInfo stock)
	{
		this.stock = stock;
	}

	/**
	 * 
	 * @return
	 */
	public StockInfo getStock()
	{
		return stock;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isNull()
	{
		return (data == null);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEmpty()
	{
		return data.isEmpty();
	}

	/**
	 * 
	 */
	public void sort()
	{
		Collections.sort(data);
	}

	/**
	 * 
	 * @return
	 */
	public int getItemsCount()
	{
		return data.size();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getLastIndex()
	{
		int index = data.size() - 1;
		return index < 0 ? 0 : index;
	}

	/**
	 * 
	 * @return
	 */
	public List<DataItem> getDataItems()
	{
		return data;
	}
	

	
	/**
	 * Get the Field num.
	 * @return
	 */
	public int getFieldNum()
	{
		return header.getFieldNum();
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public String getFieldName(int index)
	{
		return header.getDataField(index).getName();
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	public DataItem getDataItem(int index)
	{
		if (index < 0 || index >= data.size())
		{
			return null;
		}
		return data.get(index);
	}

	/**
	 * 
	 * @param index
	 * @param item
	 */
	public void setDataItem(int index, DataItem item)
	{
		if (index < 0 || index >= data.size())
		{
			return;
		}
		data.set(index, item);
	}

	/**
	 * 
	 * @param item
	 */
	public void addDataItem(DataItem item)
	{
		data.add(item);
	}
	
	/**
	 * 
	 * @return
	 */
	public DataItem getLastDataItem()
	{
		return data.get(getLastIndex());
	}

	/**
	 * 
	 * @param index
	 * @param indValue
	 * @return
	 */
	public boolean isRising(int index, int indValue)
	{
		DataItem item = data.get(index);
		if(index == 0)
		{
			if(item.getValue(indValue) > 0)
			{
				return true;
			}
			return false;
		}
		DataItem item1 = data.get(index - 1);
		if(item1.getValue(indValue) <= item.getValue(indValue))
		{
			return true;
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public Date getDateAt(int index)
	{
		if (index < 0 || index >= data.size())
		{
			return null;
		}
		return data.get(index).getDate();
	}

	/**
	 * 
	 * @return
	 */
	public long[] getTimeValues()
	{
		long[] values = new long[data.size()];
		for (int i = 0; i < data.size(); i++)
		{
			if (data.get(i) != null)
			{
				values[i] = data.get(i).getTime();
			}
		}
		return values;
	}
	
	public double getPriceAt(int index, String price)
	{
		int p = header.getFieldIndex(price);
		if(p < 0 || p >= header.getFieldNum())
			return 0.0;
		return getPriceAt(index, p);
	}

	/**
	 * 
	 * @param index
	 * @param price
	 * @return
	 */
	public double getPriceAt(int index, int price)
	{
		if (index < 0 || index >= data.size() || price < 0 || price >= header.getFieldNum())
		{
			return 0.0;
		}
		return data.get(index).getValue(price);
	}
	
	/**
	 * 
	 * @param price
	 * @param index
	 * @return
	 
	public double getOpenAt(int price, int index)
	{
		if (index < 0 || index > data.size())
		{
			return 0.0;
		}
		return data.get(index).getValue(price);
	}*/
	
	/**
	 * 
	 * @param price
	 * @return
	 */
	public double[] getValues(int price)
	{
		double[] values = new double[data.size()];
		for (int i = 0; i < data.size(); i++)
		{
			if (data.get(i) != null)
			{
				values[i] = data.get(i).getValue(price);
			}
		}
		return values;
	}

	/**
	 * 
	 * @return
	 */
	public Date[] getDateValues()
	{
		Date[] values = new Date[data.size()];
		for (int i = 0; i < data.size(); i++)
		{
			if (data.get(i) != null)
			{
				values[i] = data.get(i).getDate();
			}
		}
		return values;
	}
	
	/**
	 * Get the Time at index value.
	 * @param index
	 * @return
	 */
	public long getTimeAt(int index)
	{
		if (index < 0 || index >= data.size())
		{
			return 0;
		}
		return data.get(index) != null ? data.get(index).getTime() : 0;
	}
	
	/**
	 * 
	 * @param index
	 * @param value
	 */
	public void setTimeAt(int index, long value)
	{
		if (index < 0 || index >= data.size())
		{
			return;
		}
		if (data.get(index) == null)
		{
			return;
		}
		data.get(index).setTime(value);
	}
	
	/**
	 * 
	 * @return
	 */
	public long getLastTime()
	{
		return data.get(getLastIndex()) != null ? data.get(getLastIndex()).getTime() : 0;
	}

	/**
	 * 
	 * @return
	 */
	public Date getLastDate()
	{
		return data.get(getLastIndex()) != null ? data.get(getLastIndex()).getDate() : null;
	}
	
	/**
	 * 
	 * @param price
	 * @return
	 */
	public double getMin(String price)
	{
		int p = header.getFieldIndex(price);
		return getMin(p);
	}

	/**
	 * 
	 * @param price
	 * @return
	 */
	public double getMin(int price)
	{
		List<DataItem> list = getDataItems();
		double value = Double.MAX_VALUE;
		for (DataItem item : list)
		{
			if (item != null && value > item.getValue(price))
			{
				value = item.getValue(price);
			}
		}
		return value;
	}
	
	/**
	 * 
	 * @param price
	 * @return
	 */
	public double getMax(String price)
	{
		int p =  header.getFieldIndex(price);
		return getMax(p);
	}

	/**
	 * Get the max value of the price value.
	 * @param price
	 * @return
	 */
	public double getMax(int price)
	{
		List<DataItem> list = getDataItems();
		double value = Double.MIN_VALUE;
		
		for (DataItem item : list)
		{
			if (item != null && value < item.getValue(price))
			{
				value = item.getValue(price);
			}
		}
		return value;
	}
	
	/**
	 * 
	 * @param price
	 * @return
	 */
	public double getMinNotZero(String price)
	{
		int p = header.getFieldIndex(price);
		return getMinNotZero(p);
	}

	/**
	 * 
	 * @param price
	 * @return
	 */
	public double getMinNotZero(int price)
	{
		List<DataItem> list = getDataItems();
		double value = Double.MAX_VALUE;
		
		for (DataItem item : list)
		{
			if (item != null && item.getValue(price) != 0 && value > item.getValue(price))
			{
				value = item.getValue(price);
			}
		}
		return value;
	}

	
	/**
	 * 
	 * @param price
	 * @return
	 */
	public double getMaxNotZero(String price)
	{
		int p = header.getFieldIndex(price);
		return getMaxNotZero(p);
	}

	/**
	 * 
	 * @param price
	 * @return
	 */
	public double getMaxNotZero(int price)
	{
		List<DataItem> list = getDataItems();
		double value = Double.MIN_VALUE;
		
		for (DataItem item : list)
		{
			if (item != null && item.getValue(price) != 0 && value < item.getValue(price))
			{
				value = item.getValue(price);
			}
		}
		return value;
	}

	/**
	 * 
	 * @param period
	 * @param end
	 * @return
	 */
	public List<DataItem> getVisibleItems(int period, int end)
	{
		List<DataItem> list = new ArrayList<DataItem>();
		for (int i = 0; i < period; i++)
		{
			int j = end - period + i;
			if (j < getItemsCount() && j >= 0)
			{
				list.add(data.get(j));
			}
		}
		return list;
	}
	
	/**
	 * 
	 * @param price
	 * @return
	 */
	public Range getVisibleRange(int price)
	{
		double min = getMinNotZero(price);
		double max = getMaxNotZero(price);
		Range range = new Range(min - (max - min) * RANGE_SPACE, max + (max - min) * RANGE_SPACE);
		return range;
	}
	
	/**
	 * 
	 * @param period
	 * @param end
	 * @return
	 */
	public Dataset getVisibleDataset(int period, int end)
	{
		return new Dataset(header, getVisibleItems(period, end));
	}

	/**
	 * 
	 * @param period
	 * @param end
	 * @return
	 */
	public DataItem[] getVisibleItemsArray(int period, int end)
	{
		DataItem[] list = new DataItem[period];
		for (int i = 0; i < period; i++)
		{
			int j = end - period + i;
			if (j < getItemsCount() && j >= 0)
			{
				list[i] = data.get(j);
			}
		}
		return list;
	}
}
