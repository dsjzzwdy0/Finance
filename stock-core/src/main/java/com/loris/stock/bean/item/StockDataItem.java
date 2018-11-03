package com.loris.stock.bean.item;


import com.loris.base.util.SerialVersion;

/**
 *
 * @author viorel.gheba
 */
public class StockDataItem extends DataItem
{

	/** SerialVersion. */
    private static final long serialVersionUID = SerialVersion.APPVERSION;

    /*private long time;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;
    private double amount;*/

    public StockDataItem()
    {
        this(0, 0, 0, 0, 0, 0, 0);
    }
    
    public StockDataItem(long time, double close)
    {
        this(time, 0, 0, 0, close, 0, 0);
    }

    /**
     * Create a new StockDataItem.
     * 
     * @param time
     * @param open
     * @param high
     * @param low
     * @param close
     * @param volume
     * @param amount
     */
    public StockDataItem(long time, double open, double high, double low, double close, double volume, double amount)
    {
    	super();
    	this.time = time;
    	values[0] = open;
    	values[1] = high;
    	values[2] = low;
    	values[3] = close;
    	values[4] = volume;
    	values[5] = amount;
	
        /*this.time = time;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.amount = amount;*/
    }

    public StockDataItem getEmptyDataItem()
    {
        return null;
    }
    
    public double getOpen()
    {
    	return values[0];
    }

    public void setOpen(double open)
    {
    	values[0] = open;
    }

    public double getHigh()
    {
        return values[1];
    }

    public void setHigh(double high)
    {
    	values[1] = high;
    }

    public double getLow()
    {
        return values[2];
    }

    public void setLow(double low)
    {
    	values[2] = low;
    }

    public double getClose()
    {
        return values[3];
    }

    public void setClose(double close)
    {
    	values[3] = close;
    }

    public double getVolume()
    {
        return values[4];
    }

    public void setVolume(double volume)
    {
    	values[4] = volume;
    }
    
    public void setAmount(double amount)
    {
    	values[5] = amount;
    }
    
    public double getAmount()
    {
    	return values[5];
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }

        if (!(obj instanceof StockDataItem))
        {
            return false;
        }

        StockDataItem that = (StockDataItem) obj;
        if (getTime() != that.getTime())
        {
            return false;
        }

        if (getOpen() != that.getOpen())
        {
            return false;
        }

        if (getHigh() != that.getHigh())
        {
            return false;
        }

        if (getLow() != that.getLow())
        {
            return false;
        }

        if (getClose() != that.getClose())
        {
            return false;
        }

        if (getVolume() != that.getVolume())
        {
            return false;
        }

        return true;
    }

    /**
     * 
     * @param item
     * @return
     */
    public boolean updateClose(StockDataItem item)
    {
        return Double.compare(this.getClose(), item.getClose()) != 0;
    }

}
