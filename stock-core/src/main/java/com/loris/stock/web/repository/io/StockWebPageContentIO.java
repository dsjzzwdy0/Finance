package com.loris.stock.web.repository.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.DateUtil;
import com.loris.stock.web.page.StockDetailWebPage;

/**
 * 数据页面输入&输出
 * 
 * @author dsj
 *
 */
public class StockWebPageContentIO
{
	/** 数据基础路径. */
	public static String BASE_PATH = "";

	/** 默认的数据编码. */
	public static String DEFAULT_CONTENT_ENCODING = "GBK";

	/**
	 * 读取详细内容数据
	 * 
	 * @param page
	 *            数据页面
	 * @return 是否写入成功的标志
	 */
	public static boolean readWebPageContent(StockDetailWebPage page)
	{
		try
		{
			String filePath = getFilePath(page);
			File f = new File(filePath);
			if (!f.exists() && !f.isFile())
			{
				return false;
			}
			try(FileInputStream in = new FileInputStream(f))
			{
				// size 为字串的长度 ，这里一次性读完
				int size = in.available();
				byte[] buffer = new byte[size];
				in.read(buffer);
				String content = new String(buffer,
						StringUtils.isNotEmpty(page.getEncoding()) ? page.getEncoding() : DEFAULT_CONTENT_ENCODING);
				page.setContent(content);
				return true;
			}
		} 
		catch (Exception e)
		{
			return false;
		}
	}
	
	/**
	 * 读取数据
	 * 
	 * @param path 路径
	 * @param encoding 编码
	 * @return 读取的内容
	 */
	public static String readWebPageContent(String path, String encoding)
	{
		try
		{
			File f = new File(path);
			if (!f.exists() && !f.isFile())
			{
				return null;
			}
			try(FileInputStream in = new FileInputStream(f))
			{
				// size 为字串的长度 ，这里一次性读完
				int size = in.available();
				byte[] buffer = new byte[size];
				in.read(buffer);
				String content = new String(buffer,
						StringUtils.isNotEmpty(encoding) ? encoding : DEFAULT_CONTENT_ENCODING);
				return content;
			}
		} 
		catch (IOException e)
		{
			return null;
		}
	}

	/**
	 * 写入详细数据内容
	 * 
	 * @param page
	 *            数据页面
	 * @return 是否写入成功的标志
	 */
	public static boolean writeWebPageContent(StockDetailWebPage page) throws IOException
	{
		if(StringUtils.isEmpty(page.getContent()))
		{
			return false;
		}
		
		String path = getFilePath(page);
		//System.out.println("Path: " + path);
		File file = new File(path);
		File parentDirectory = new File(file.getParent());
		if(!parentDirectory.isDirectory() || !parentDirectory.exists())
		{
			parentDirectory.mkdirs();
		}
		
		try(FileOutputStream out = new FileOutputStream(getFilePath(page)))
		{
			out.write(page.getContent().getBytes(StringUtils.isEmpty(page.getEncoding()) ?
					DEFAULT_CONTENT_ENCODING : page.getEncoding()));
			out.flush();
		}
		
		//System.out.println(path);
		//System.out.println(parentDirectory.getAbsolutePath());
		return true;
	}

	/**
	 * 获得路径
	 * 
	 * @param page
	 * @return
	 */
	public static String getFilePath(StockDetailWebPage page)
	{
		String date = page.getDay();
		String year = getYear(date);
		
		if(StringUtils.isEmpty(year))
		{
			throw new UnsupportedOperationException("The '" + date + "' is not a validate Date String value. ");
		}
		String string = getBasePath(year);
		
		return string + date + File.separator + page.getSymbol() + "_" + date + ".csv";
	}

	/**
	 * 获得数据根目录
	 * 
	 * @param year
	 *            年份
	 * @return 路径
	 */
	public static String getBasePath(String year)
	{
		return BASE_PATH + year + File.separator;
	}
	
	/**
	 * 获得YEAR值
	 * 
	 * @param d 日期
	 * @return
	 */
	protected static String getYear(String d)
	{
		Date date = DateUtil.tryToParseDate(d);
		
		if(date == null)
		{
			return "";
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return Integer.toString(calendar.get(Calendar.YEAR));
	}
}
