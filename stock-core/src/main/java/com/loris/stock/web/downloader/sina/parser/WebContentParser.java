package com.loris.stock.web.downloader.sina.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

// import org.codehaus.jackson.map.ObjectMapper;

import com.loris.base.util.CsvReader;
import com.loris.base.util.NumberUtil;
import com.loris.base.util.DateUtil;
import com.loris.stock.bean.data.table.DailyRecord;
import com.loris.stock.bean.item.DetailedItem;
import com.loris.stock.bean.model.StockDayDetailList;
import com.loris.stock.web.downloader.sina.loader.StockInfoWebPageDownloader;
import com.loris.stock.web.page.StockDailyWebPage;
import com.loris.stock.web.page.StockDetailWebPage;

/**
 * 
 * @author usr
 *
 */
public class WebContentParser
{
	private static ObjectMapper objectMapper = new ObjectMapper();
	/**
	 * Parse Daily WebPage
	 * 
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static DailyRecordList parseDailyWebPage(StockDailyWebPage page)
	{
		if (page == null || !page.isCompleted())
		{
			return null;
		}
		try
		{
			List<?> objs = objectMapper.readValue(page.getContent(), List.class);
			if (objs.size() < 0)
				return null;

			DailyRecordList bean = new DailyRecordList();
			Map<String, ?> map = (LinkedHashMap<String, ?>) objs.get(0);
			for (String key : map.keySet())
			{
				Object obj = map.get(key);
				key = key.toLowerCase();
				switch (key)
				{
				case "code":
					bean.setCode(obj.toString());
					break;
				case "day":
					bean.setDay(obj.toString());
					break;
				case "count":
					bean.setCount((Integer) obj);
					break;
				case "fields":
					processFields(bean, (List<String>) obj);
					break;
				case "items":
					processItems(bean, (List<Object>) obj);
					break;
				default:
					break;
				}
			}

			return bean;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	protected static void processFields(DailyRecordList bean, List<String> fields)
	{

	}

	/**
	 * Process the items.
	 * 
	 * @param bean
	 * @param items
	 */
	@SuppressWarnings("unchecked")
	protected static void processItems(DailyRecordList bean, List<?> items)
	{
		for (Object object : items)
		{
			if (object instanceof List)
			{
				List<Object> values = (List<Object>) object;
				DailyRecord record = new DailyRecord();

				record.setDay(bean.getDay());
				record.setSymbol(values.get(0).toString());
				record.setCode(values.get(1).toString());
				record.setName(values.get(2).toString());
				record.setTrade(NumberUtil.parseDouble(values.get(3)));
				record.setPricechange(NumberUtil.parseDouble(values.get(4)));
				record.setChangepercent(NumberUtil.parseDouble(values.get(5)));
				record.setBuy(NumberUtil.parseDouble(values.get(6)));
				record.setSell(NumberUtil.parseDouble(values.get(7)));
				record.setSettlement(NumberUtil.parseDouble(values.get(8)));
				record.setOpen(NumberUtil.parseDouble(values.get(9)));
				record.setHigh(NumberUtil.parseDouble(values.get(10)));
				record.setLow(NumberUtil.parseDouble(values.get(11)));
				record.setVolume(NumberUtil.parseInt(values.get(12)));
				record.setAmount(NumberUtil.parseInt(values.get(13)));
				record.setTicktime(values.get(14).toString());
				record.setPer(NumberUtil.parseDouble(values.get(15)));
				record.setPer_d(NumberUtil.parseDouble(values.get(16)));
				record.setNta(NumberUtil.parseDouble(values.get(17)));
				record.setPb(NumberUtil.parseDouble(values.get(18)));
				record.setMktcap(NumberUtil.parseDouble(values.get(19)));
				record.setNmc(NumberUtil.parseDouble(values.get(20)));
				record.setTurnoverratio(NumberUtil.parseDouble(values.get(21)));

				bean.items.add(record);
			}
		}
	}

	/**
	 * 
	 * @param content
	 * @return
	 */
	public static StockDayDetailList parseDetailWebPage(StockDetailWebPage page)
	{
		if (page == null || !StockInfoWebPageDownloader.PAGE_TYPE_DETAIL.equals(page.getType()))
		{
			return null;
		}

		try
		{
			StockDayDetailList dataset = new StockDayDetailList();
			dataset.setSymbol(page.getSymbol());
			dataset.setDate(page.getCreatetime());
			parseContent(dataset, page);

			return dataset;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * 
	 * @param strs
	 * @return
	 */
	protected static DetailedItem parseItem(String symbol, String date, String[] strs)
	{
		if (!checkIsDetailedItem(strs))
		{
			return null;
		}

		DetailedItem item = new DetailedItem(date);

		item.setDate(date);
		item.setSecond(strs[0]);
		item.setPrice(NumberUtil.parseDouble(strs[1]));
		item.setVariation(NumberUtil.parseDouble(strs[2]));
		item.setVolume(NumberUtil.parseInt(strs[3]));
		item.setAmount(NumberUtil.parseInt(strs[4]));
		item.setType(strs[5]);

		return item;
	}

	/**
	 * Check is detailed item.
	 */
	protected static boolean checkIsDetailedItem(String[] strs)
	{
		if (strs == null || strs.length < 6)
			return false;

		try
		{
			DateUtil.HOUR_FORMAT.parse(strs[0]);
			return true;
		}
		catch (ParseException e)
		{
			return false;
		}
	}

	/**
	 * Parse the content value.
	 * 
	 * @param symbol
	 * @param date
	 * @param content
	 * @throws IOException
	 */
	protected static void parseContent(StockDayDetailList dateset, StockDetailWebPage page) throws IOException
	{
		// System.out.println("Start to tokenizer...");

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new ByteArrayInputStream(page.getContent().getBytes(Charset.forName("utf8"))),
						Charset.forName("utf8"))))
		{
			CsvReader reader = new CsvReader(br, '\t');
			{
				// System.out.println(reader.readHeaders());
				// System.out.println(reader.getHeaders());

				while (reader.readRecord())
				{
					String[] strs = reader.getValues();
					DetailedItem item = parseItem(page.getSymbol(), page.getCreatetime(), strs);
					if (item == null)
						continue;
					dateset.addItem(item);
				}

			}
			reader.close();
		}
	}
}
