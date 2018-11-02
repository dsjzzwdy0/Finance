package com.loris.stock.bean.item;

import java.io.Serializable;
import java.util.Date;

import com.loris.base.util.DateUtil;
import com.loris.stock.bean.item.util.DataHeader;

public class DataItem implements Serializable, Comparable<DataItem>
{
	private static final long serialVersionUID = 1L;

	/** The time. */
	protected long time;

	/** The Value of the item. */
	protected double[] values = null;
	
	/** The Header. */
	protected DataHeader header = null;

	/**
	 * 
	 * @param fieldnum
	 */
	public DataItem(DataHeader header)
	{
		this.header = header;
		values = new double[header.getFieldNum()];
	}
	
	/**
	 * 
	 * @param fieldnum
	 */
	public DataItem()
	{
		this(DataHeader.getStockDataHeader());
	}
	
	
	/**
	 * 
	 * @param time
	 * @param value
	 */
	public DataItem(long time, double value)
	{
		this.time = time;
		values = new double[] { value};
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
	 * Set the time.
	 * 
	 * @param time
	 */
	public void setTime(long time)
	{
		this.time = time;
	}

	/**
	 * Get the time.
	 * 
	 * @return
	 */
	public long getTime()
	{
		return time;
	}
	
	/**
	 * 
	 * @return
	 */
    public Date getDate()
    {
        return new Date(time);
    }

   
	/**
	 * 
	 * @param index The value index type.
	 * @return
	 */
	public double getValue(int index)
	{
		return values[index];
	}

	/**
	 * set value.
	 * 
	 * @param index
	 * @param value
	 */
	public void setValue(int index, double value)
	{
		values[index] = value;
	}

	/**
	 * Calculate the hashcode.
	 * 
	 * @return Hashcode
	 */
	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 53 * hash + (int) (this.time ^ (this.time >>> 32));

		int len = values.length;
		for (int i = 0; i < len; i++)
		{
			hash = 53 * hash + (int) (Double.doubleToLongBits(values[i]) ^ (Double.doubleToLongBits(values[i]) >>> 32));
		}
		return hash;
	}
	
	/**
	 * To String value.
	 * 
	 * @param To String.
	 */
	@Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append(DateUtil.DAY_FORMAT.format(new Date(time)) + " (");
        //sb.append(Long.toString(time)).append(",");
        
        int len = values.length;
        for(int i = 0; i < len; i ++)
        {
        	//sb.append("DataItem [open=");
        	sb.append(Double.toString(values[i]));
        	if(i < len - 1)
        		sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }

	/**
	 * Compare two DataItem.
	 * 
	 * @param o
	 * @return
	 */
	@Override
	public int compareTo(DataItem item)
	{
		return getDate().compareTo(item.getDate());
		//return Long.compare(time, o.getTime());
	}

}
