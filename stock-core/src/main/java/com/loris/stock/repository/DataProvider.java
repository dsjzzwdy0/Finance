package com.loris.stock.repository;

import java.io.Closeable;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.loris.stock.bean.data.table.Exchange;
import com.loris.stock.bean.data.table.StockInfo;
import com.loris.stock.bean.item.StockDataItem;
import com.loris.stock.bean.item.interval.Interval;
import com.loris.stock.bean.model.StockDataset;
import com.loris.stock.exception.InvalidStockException;
import com.loris.stock.exception.RegistrationException;
import com.loris.stock.exception.StockNotFoundException;

/**
 * This will provide stock data.
 * 
 * @author dsj
 *
 */
public interface DataProvider extends Closeable
{
	/**
	 * Get the DataProvider name defined by this.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Get the Exchanges that the DataProvider supported.
	 * 
	 * @return
	 */
	List<Exchange> getExchanges();
		
	/**
	 * 
	 * @param exchange
	 * @return
	 */
	Exchange getExchange(String exchange);
	
	/**
	 * Add the Exchange into the DataProvider.
	 * @param exchange
	 */
	void addSupportExchange(Exchange exchange);
	
	/**
	 * Get the RefreshInterval interval in seconds.
	 * @return
	 */
	int getRefreshInterval();
	
	/**
	 * Get the DatasetKey value.
	 * @param stock
	 * @param interval
	 * @return
	 */
	String getDatasetKey(StockInfo stock, Interval interval);
	
	/**
	 * Get the Stock Key Value.	
	 * @param symbol
	 * @return
	 */
	String getStockKey(StockInfo stock);

	/**
	 * Check if the stock exists that defined by the symbol.
	 * 
	 * @param symbol
	 * @return
	 */
	boolean stockExists(StockInfo stock);

	/**
	 * Check the dataset defined by stock and interval exists.
	 * 
	 * @param stock
	 * @param interval
	 * @return
	 * @throws IOException
	 */
	boolean datasetExists(StockInfo stock, Interval interval) throws IOException;

	/**
	 * Get Dataset by the interval defined from the DataProvider.
	 * @param stock
	 * @param interval
	 * @return
	 * @throws IOException
	 * @throws StockNotFoundException
	 * @throws ParseException
	 */
	StockDataset getDataset(StockInfo stock, Interval interval) throws IOException, StockNotFoundException, ParseException;

	/**
	 * Get the Last DataItem of the stock defined by the interval.
	 * 
	 * @param stock
	 * @param interval
	 * @return
	 */
	StockDataItem getLastDataItem(StockInfo stock, Interval interval) throws IOException, ParseException;
	
	/**
	 * Get the last DataItem list from the DataProvider.
	 * @param stock
	 * @param interval
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	List<StockDataItem> getLastDataItems(StockInfo stock, Interval interval) throws IOException, ParseException;

	/**
	 * Fetch all the data of the stock from DataProvider.
	 * 
	 * @param symbol
	 * @throws InvalidStockException
	 * @throws StockNotFoundException
	 * @throws RegistrationException
	 * @throws IOException
	 */
	void fetchStock(StockInfo stock)
			throws InvalidStockException, StockNotFoundException, RegistrationException, IOException;

	/**
	 * Update the dataset into the cache.
	 * 
	 * @param stock
	 * @param dataset
	 * @return
	 * @throws IOException
	 */
	boolean updateDataset(StockInfo stock, StockDataset dataset) throws IOException;
	
	/**
	 * Update the intraDay Data.
	 * @param key
	 * @param dataItems
	 * @return
	 * @throws IOException
	 */
	boolean updateIntraDay(String key, List<StockDataItem> dataItems) throws IOException;

	/**
	 * Get the intervals that the DataProvider supported.
	 * Such as Daily, Weekly, Monthly.
	 * 
	 * @return
	 */
	Interval[] getSupportedIntervals();

	/**
	 * Check this DataProvider support providing minute data line, such as.
	 * 
	 * @return
	 */
	boolean supportsIntraday();

	/**
	 * Check if the provider support any interval.
	 * 
	 * @return
	 */
	boolean supportsAnyInterval();

	/**
	 * boolean If the provider need to be registered.
	 * 
	 * @return
	 */
	boolean needsRegistration();

	/**
	 * Check if the DataProvider is registered.
	 * 
	 * @return
	 */
	boolean isRegistred();
	
	/**
	 * Check if the DataProvider can update the stock data.
	 * @return
	 */
	boolean supportUpdate();
}
