package com.loris.stock.repository.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.loris.base.util.NumberUtil;
import com.loris.base.util.SerialVersion;
import com.loris.stock.bean.data.table.StockInfo;
import com.loris.stock.bean.item.DataItem;
import com.loris.stock.bean.item.StockDataItem;
import com.loris.stock.bean.item.interval.Interval;
import com.loris.stock.bean.model.StockDataset;
import com.loris.stock.exception.StockNotFoundException;

/**
 * 
 * @author usr
 *
 */
public class LorisDataProvider extends AbstractDataProvider
{
	private static final long serialVersionUID = SerialVersion.APPVERSION;
	private static final String NAME = "Loris Data Provider";
	
	
	private String baseDir;
	
	
	/**
	 * Create a new LorisDataProvider.
	 * @param baseDir
	 */
	public LorisDataProvider(String baseDir) throws IOException
	{
		super(NAME);
		this.baseDir = baseDir;
		//initialize();
	}
	
	/**
	 * 目前，仅仅支持日线数据
	 */
	@Override
	public Interval[] getSupportedIntervals()
	{
		return new Interval[]
		{ Interval.DAILY };
	}

	/**
	 * Get Dataset by the interval defined from the DataProvider.
	 * 
	 * @param stock
	 * @param interval
	 * @return
	 * @throws IOException
	 * @throws StockNotFoundException
	 * @throws ParseException
	 */
	@Override
	public StockDataset getDataset(StockInfo stock, Interval interval) throws IOException, StockNotFoundException, ParseException
	{
		return readDataset(stock, interval);
	}
	
	/**
	 * Get Dataset by the interval defined from the DataProvider.
	 * 
	 * @param stock
	 * @param dataset
	 * @return
	 * @throws IOException
	 */
	@Override
	public boolean updateDataset(StockInfo stock, StockDataset dataset) throws IOException
	{
		String path = getStockPath(stock, dataset.getInterval());
		String name = getStockKey(stock, dataset.getInterval());
		
		File file = new File(baseDir + File.separator + path + File.separator + name);
		StockDataset ds = dataset;
		if(file.exists())
		{
			//Read dataset.
			//process the dataset.
		}
		
		File tmp = new File(file.getAbsolutePath() + ".tmp");
		//System.out.println("Write to tmpFile : " + tmp.getAbsolutePath());
		int recLen = 24;
		byte[] bytes = new byte[recLen];
		float open, high, low, close, volume;
		int time;
		try(OutputStream out = new FileOutputStream(tmp))
		{
			int recNum = 0;
			for(DataItem item0 : ds.getDataItems())
			{
				StockDataItem item = (StockDataItem)item0;
				open = (float)item.getOpen();
				high = (float)item.getHigh();
				low = (float)item.getLow();
				close = (float)item.getClose();
				volume = (float)item.getVolume();
				time = (int)(item.getTime() / 1000);
				
				NumberUtil.intTo4Byte(time, bytes, 0);
				NumberUtil.floatTo4byte(open, bytes, 4);
				NumberUtil.floatTo4byte(high, bytes, 8);
				NumberUtil.floatTo4byte(low, bytes, 12);
				NumberUtil.floatTo4byte(close, bytes, 16);
				NumberUtil.floatTo4byte(volume, bytes, 20);
				
				out.write(bytes, 0, recLen);
				recNum ++;
				
				if(recNum % 100 == 0)
				{
					out.flush();
				}
			}
			
			//System.out.println("Write rec number = " + recNum);
			
			out.flush();
			out.close();
		}
		if(file.exists())
			file.delete();
		tmp.renameTo(file);
		
		return true;
	}
	
	/**
	 * 
	 * @param stock
	 * @param interval
	 * @return
	 * @throws IOException
	 * @throws StockNotFoundException
	 * @throws ParseException
	 */
	protected StockDataset readDataset(StockInfo stock, Interval interval) throws IOException, StockNotFoundException, ParseException
	{
		String path = getStockPath(stock, interval);
		String name = getStockKey(stock, interval);		
		File file = new File(baseDir + File.separator + path + File.separator + name);
		//System.out.println(file.getAbsolutePath());
		if (!file.exists())
		{
			throw new StockNotFoundException("Can't find the " + stock + "");
		}
		
		int byteNum = 2400;
		int readNum, currentNum = 24;
		byte[] bytes = new byte[byteNum];
		float open, high, low, close, volume;
		long time;
		
		List<DataItem> items = new ArrayList<DataItem>();
		
		try(InputStream in = new FileInputStream(file))
		{
			while((readNum = in.read(bytes)) > 23)
			{
				for(; currentNum < readNum; )
				{
					time = NumberUtil.byte4ToInt(bytes, currentNum + 0);
					open = NumberUtil.byte4Tofloat(bytes, currentNum + 4);
					high = NumberUtil.byte4Tofloat(bytes, currentNum + 8);
					low = NumberUtil.byte4Tofloat(bytes, currentNum + 12);
					close = NumberUtil.byte4Tofloat(bytes, currentNum + 16);
					volume = NumberUtil.byte4Tofloat(bytes, currentNum + 20);
					currentNum += 24;
					
					time = time * 1000L;
				
					StockDataItem item = new StockDataItem(time, open, high, low, close, volume, 0.0);
					items.add(item);
				}
				
				currentNum = 0;
			}
			
			return new StockDataset(items, stock, interval,  true);
			
		}
	}
	
	/**
	 * 
	 * @param stock
	 * @param interval
	 * @return
	 */
	private String getStockPath(StockInfo stock, Interval interval)
	{
		String str = "";
		str += stock.getExchange();
		
		return str;
	}
	
	/**
	 * 
	 * @param stock
	 * @param interval
	 * @return
	 */
	private String getStockKey(StockInfo stock, Interval interval)
	{
		return stock.getSymbol();
	}
	
	/**
	 * 
	 */
	@Override
	public boolean supportUpdate()
	{
		return true;
	}
	
	/**
	 * Close the DataProvider.
	 */
	@Override
	public void close()
	{
	}
}
