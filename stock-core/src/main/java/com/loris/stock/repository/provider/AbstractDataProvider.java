package com.loris.stock.repository.provider;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.loris.base.util.SerialVersion;
import com.loris.stock.bean.data.table.Exchange;
import com.loris.stock.bean.data.table.StockInfo;
import com.loris.stock.bean.item.StockDataItem;
import com.loris.stock.bean.item.interval.Interval;
import com.loris.stock.bean.model.StockDataset;
import com.loris.stock.exception.InvalidStockException;
import com.loris.stock.exception.RegistrationException;
import com.loris.stock.exception.StockNotFoundException;
import com.loris.stock.repository.DataProvider;

/**
 *
 * @author viorel.gheba
 */
public abstract class AbstractDataProvider implements DataProvider, Serializable
{
	private static final long serialVersionUID = SerialVersion.APPVERSION;

	protected String name;
	protected List<Exchange> exchanges;

	protected boolean supportsIntraDay = false;
	protected boolean supportsCustomInterval = false;
	protected boolean initializeFlag = true;
	protected boolean needsRegistration = false;
	protected boolean isRegistered = false;

	/**
	 * Construct a new AbstractDataProvider.
	 */
	protected AbstractDataProvider(String name)
	{
		this.name = name;
		this.supportsIntraDay = false;
		this.supportsCustomInterval = false;
		exchanges = new ArrayList<Exchange>();
	}

	/**
	 * Get the DataProvider name.
	 * 
	 * @return
	 */
	public String getName()
	{
		return this.name;
	}

	public List<Exchange> getExchanges()
	{
		return this.exchanges;
	}

	/**
	 * Add the Exchange into the DataProvider.
	 * 
	 * @param exchange
	 */
	@Override
	public void addSupportExchange(Exchange exchange)
	{
		exchanges.add(exchange);
	}

	/**
	 * 支持的数据类型
	 */
	public Interval[] getSupportedIntervals()
	{
		return new Interval[]
		{};
	}

	/**
	 * @param stock
	 */
	@Override
	public boolean stockExists(StockInfo stock)
	{
		for (Exchange exchange : exchanges)
		{
			if (exchange.hasStock(stock))
				return true;
		}
		return false;
	}

	public boolean supportsIntraday()
	{
		return supportsIntraDay;
	}

	public boolean supportsCustomInterval()
	{
		return supportsCustomInterval;
	}

	public boolean supportsAnyInterval()
	{
		return false;
	}

	public List<StockDataItem> getLastDataItems(StockInfo stock, Interval interval)
	{
		return new ArrayList<StockDataItem>();
	}

	public boolean needsRegistration()
	{
		return needsRegistration;
	}

	public boolean isRegistred()
	{
		return isRegistered;
	}

	public String getRegistrationMessage()
	{
		return "";
	}

	public String getRegistrationURL()
	{
		return "";
	}

	@Override
	public void close() throws IOException
	{
		// do nothing.
	}

	@Override
	public int getRefreshInterval()
	{
		return 0;
	}
	
	/**
	 * @param name
	 */
	@Override
	public Exchange getExchange(String exchange)
	{
		for(Exchange exh: exchanges)
		{
			if(exchange.equals(exh.getName()))
			{
				return exh;
			}
		}
		return null;
	}
	

	@Override
	public String getDatasetKey(StockInfo stock, Interval interval)
	{
		String key = getName() + '_' + stock.getSymbol() + '_' + interval.getTimeParam();
		return key;
	}
	
	/**
	 * Get the Stock Key Value.	
	 * @param symbol
	 * @return
	 */
	public String getStockKey(StockInfo stock)
	{
		String key = getName() + '-' + stock.getSymbol();
		return key;
	}

	@Override
	public boolean datasetExists(StockInfo stock, Interval interval) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean updateDataset(StockInfo stock, StockDataset dataset) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean updateIntraDay(String key, List<StockDataItem> dataItems) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	public void fetchStock(StockInfo stock)
			throws InvalidStockException, StockNotFoundException, RegistrationException, IOException
	{
		throw new UnsupportedOperationException();
	}

	public StockDataItem getLastDataItem(StockInfo stock, Interval interval)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 
	 */
	@Override
	public boolean supportUpdate()
	{
		return false;
	}
}
