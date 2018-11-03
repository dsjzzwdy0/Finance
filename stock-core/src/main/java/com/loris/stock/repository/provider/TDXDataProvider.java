package com.loris.stock.repository.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.loris.base.util.NumberUtil;
import com.loris.base.util.SerialVersion;
import com.loris.stock.bean.data.table.Exchange;
import com.loris.stock.bean.data.table.StockInfo;
import com.loris.stock.bean.item.DataItem;
import com.loris.stock.bean.item.StockDataItem;
import com.loris.stock.bean.item.interval.DailyInterval;
import com.loris.stock.bean.item.interval.Interval;
import com.loris.stock.bean.item.interval.OneMinuteInterval;
import com.loris.stock.bean.model.StockDataset;
import com.loris.stock.exception.StockNotFoundException;

/**
 * 
 * @author dsj
 *
 */
public class TDXDataProvider extends AbstractDataProvider
{
	private static final long serialVersionUID = SerialVersion.APPVERSION;
	
	public static String DIR_DIALY = "lday";
	public static String DIR_SH = "sh";
	public static String DIR_SZ = "sz";

	private String baseURL = "";
	private static DateFormat df;

	/**
	 * 
	 * @param baseURL
	 */
	public TDXDataProvider(String baseURL)
	{
		super("TDX Data Provider");
		this.baseURL = baseURL;
		if (df == null)
		{
			df = new java.text.SimpleDateFormat("yyyyMMdd");
		}
	}
	
	
	/**
	 * 扫描数据存储位置
	 */
	public void scanURL()
	{
		/*System.out.println("BaseURL: " + baseURL);
		Path shPath = Paths.get(baseURL, "/sh/lday");
		for (Path path : shPath)
		{
			System.out.println(path.toUri());
		}*/
		List<Exchange> exchanges = getExchanges();
		for (Exchange exchange : exchanges)
		{
			System.out.println(exchange.getName());
		}		
		
		Path dir = Paths.get(baseURL + "/sh/lday");
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir))
        {
            for(Path e : stream)
            {
                System.out.println(e.toAbsolutePath());
            }
        }
        catch(IOException e)
        {
        	e.printStackTrace();
        }
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
	 * Check the DataProvider exists the Dataset.
	 * 
	 * @param stock
	 * @param interval
	 * @throws IOException
	 */
	@Override
	public boolean datasetExists(StockInfo stock, Interval interval) throws IOException
	{
		String path = "";
		try
		{
			path = getDataURL(stock, interval);
		}
		catch (UnsupportedEncodingException e)
		{
			return false;
		}
		
		File file = new File(path);
		return file.exists();
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
		String path = "";
		try
		{
			path = getDataURL(stock, interval);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new StockNotFoundException();
		}
		File file = new File(path);
		if (!file.exists())
		{
			System.out.println(file.getAbsolutePath());
			throw new StockNotFoundException();
		}

		return this.parseTDXDayLine(stock, file, interval);
	}

	/**
	 *
	 * @param filePath
	 * @return
	 */
	private StockDataset parseTDXDayLine(StockInfo stock, File filePath, Interval interval) throws IOException
	{
		try (InputStream in = new FileInputStream(filePath))
		{
			int num;
			List<DataItem> items = new ArrayList<DataItem>();
			byte[] bytes = new byte[32];
			
			long start = interval.startTime();
			//end = start + interval.getLengthInSeconds() * 1000;

			long time;
			double open, high, low, close, amount, volume;
			Date date;

			while (true)
			{
				try
				{
					num = in.read(bytes);
					if (num < 32)
					{
						// 没有32个字节的数据，终止数据解析与获取
						break;
					}

					// 解析日期数据
					time = NumberUtil.byte4ToInt(bytes, 0);
					date = df.parse("" + time);
					time = date.getTime();
					
					//截断时间
					if(time < start)
						continue;

					open = NumberUtil.byte4ToInt(bytes, 4) / 100.0f;
					high = NumberUtil.byte4ToInt(bytes, 8) / 100.0f;
					low = NumberUtil.byte4ToInt(bytes, 12) / 100.0f;
					close = NumberUtil.byte4ToInt(bytes, 16) / 100.0f;
					amount = NumberUtil.byte4Tofloat(bytes, 20) / 10000.0f;
					volume = NumberUtil.byte4ToInt(bytes, 24) / 100.0f;

					StockDataItem item = new StockDataItem(time, open, high, low, close, volume, amount);
					items.add(item);
				}
				catch (Exception ex)
				{
					break;
				}
			}
			return new StockDataset(items, stock, interval);
		}

	}

	/**
	 * 
	 * @param stock
	 * @param interval
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String getDataURL(StockInfo stock, Interval interval) throws UnsupportedEncodingException
	{
		String exchange = stock.getExchange().startsWith("sh") ? "sh" : "sz";
		String symbol = stock.getSymbol();
		StringBuffer url = new StringBuffer();
		url.append(baseURL + File.separator);
		url.append(exchange);

		if (interval instanceof DailyInterval)
			url.append("/" + getIntervalDir(interval) + "/" + symbol + ".day");
		else if (interval instanceof OneMinuteInterval)
			url.append("/minline/" + symbol + ".lc1");
		else
			throw new UnsupportedEncodingException("Not supported '" + interval.toString() + "' data.");

		return url.toString();
	}
	
	/**
	 * Get the Exchange Child directory.
	 * @param exchange
	 * @return
	 */
	public String getExchangeDir(Exchange exchange) throws UnsupportedEncodingException
	{
		if(exchange.getName().contains("sh"))
		{
			return "";
		}
		else if(exchange.getName().contains("sz"))
		{
			return "";
		}
		else {
			throw new UnsupportedEncodingException("No such Exchange exists. " + exchange.toString());			
		}
	}
	
	/**
	 * Get the Interval dir.
	 * @param interval
	 * @return
	 * @throws IOException
	 */
	public String getIntervalDir(Interval interval) throws UnsupportedEncodingException
	{
		if(interval instanceof DailyInterval)
		{
			return "lday";
		}
		else {
			throw new UnsupportedEncodingException("No such Exchange exists. " + interval.toString());			
		}
	}
}
