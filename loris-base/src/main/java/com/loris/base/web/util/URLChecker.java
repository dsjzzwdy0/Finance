package com.loris.base.web.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLChecker
{
	/** Pattern. */
	private static Pattern datePattern = Pattern.compile("(\\d{1,4}-\\d{1,2}-\\d{1,2})");
	private static Pattern symbolPattern = Pattern.compile("([sz|sh]\\d{6})");
	private static String name1 = "symbol";
	private static String name2 = "date";
	
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static String checkDetailURL(String url)
	{
		try
		{
			URLParser parser = URLParser.fromURL(url).compile();
			
			
			String value1 = parser.getParameter(name1);
			String value2 = parser.getParameter(name2);

			Matcher matcher2 = datePattern.matcher(value1);

			if (matcher2.find())
			{
				parser.setParameter("symbol", value2);
				parser.setParameter("date", value1);
				
				return parser.toURL();
			}
		}
		catch(Exception e)
		{
		}
		return url;
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isRightDetailURL(String url)
	{
		try
		{
			URLParser parser = URLParser.fromURL(url).compile();
			String value1 = parser.getParameter(name1);
			String value2 = parser.getParameter(name2);
	
			Matcher dMatcher = datePattern.matcher(value2);
			Matcher sMatcher = symbolPattern.matcher(value1);

			if (dMatcher.find() && sMatcher.find())
			{
				return true;
			}
		}
		catch(Exception e)
		{
			
		}
		return false;
	}
	
	
	public static void main(String[] args)
	{
		String url = "http://market.finance.sina.com.cn/downxls.php?date=sz000007&symbol=2017-10-24";
		System.out.println(url + ": " + isRightDetailURL(url));
	}
}
