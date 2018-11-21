package com.loris.base.web.repository.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.web.page.WebPage;

public class WebPageContentIO
{
	private static Logger logger = Logger.getLogger(WebPageContentIO.class);
	
	/** 默认的数据存储位置 */
	public static String DEFAULT_DIR = "d:/loris/data/";
	
	/** 默认的数据编码. */
	public static String DEFAULT_CONTENT_ENCODING = "UTF-8";
	
	/** 存在的数据路径*/
	protected static Map<String, String> baseDirs = new HashMap<>();
	
	/**
	 * 加入数据路径
	 * @param type 类型
	 * @param path 路径
	 */
	public static void addBaseDir(String type, String path)
	{
		baseDirs.put(type, path);
	}
	
	/**
	 * 读取详细内容数据
	 * 
	 * @param page 数据页面
	 * @return 是否写入成功的标志
	 */
	public static boolean readWebPageContent(WebPage page)
	{		
		try
		{
			String path = getFilePath(page);
			File file = new File(path);
			if(!file.exists())
			{
				return false;
			}
			
			try(FileInputStream in = new FileInputStream(file))
			{
				// size 为字串的长度 ，这里一次性读完
				int size = in.available();
				byte[] buffer = new byte[size];
				in.read(buffer);
				String content = new String(buffer, StringUtils.isNotEmpty(page.getEncoding()) ? page.getEncoding() : DEFAULT_CONTENT_ENCODING);
				page.setContent(content);
				return true;
			}
		}
		catch (Exception e)
		{
			logger.info(e.toString());
		}
		return false;
	}
	
	/**
	 * 写入详细数据内容
	 * @param page 数据页面
	 * @return 是否写入成功的标志
	 */
	public static boolean writeWebPageContent(WebPage page)
	{
		if(StringUtils.isEmpty(page.getContent()))
		{
			return false;
		}
		try
		{
			String path = getFilePath(page);
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
		}
		catch (Exception e)
		{
			logger.info(e.toString());
		}
		return false;
	}
	
	/**
	 * 获得路径
	 * 
	 * @param page
	 * @return
	 */
	public static String getFilePath(WebPage page)
	{
		return getBasePath(page.getType()) + page.getPathName() + page.getContentFileName();
	}
	
	/**
	 * 获得基础路径
	 * @param type
	 * @return
	 */
	public static String getBasePath(String type)
	{
		String p = baseDirs.get(type);
		return StringUtils.isEmpty(p) ? DEFAULT_DIR : p;
	}
}
