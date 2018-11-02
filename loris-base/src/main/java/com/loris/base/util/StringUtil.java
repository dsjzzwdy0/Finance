package com.loris.base.util;

public class StringUtil
{
	/**
	 * Check the String is null or empty.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str)
	{
		if(str == null || "".equals(str.trim()))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 压缩掉所有空格符号
	 * 
	 * @param str
	 * @return
	 */
	public static String trimAllSpace(String str)
	{
		if(!isNullOrEmpty(str))
		{
			str = str.replaceAll(" ", "");
		}
		return str;
	}
	
	/**
	 * 按照最大的数据截断字符串
	 * @param str 字符串
	 * @param maxLen 最大长度
	 * @return 截断后的字符串
	 */
	public static String getMaxStringValue(String str, int maxLen)
	{
		if(str.length() > maxLen)
		{
			return str.substring(0, maxLen);
		}
		return str;
	}
}
