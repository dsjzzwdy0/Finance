package com.loris.base.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public interface NumberUtil
{
	public static final String REGULAR_FLOAT = "\\d+(\\.\\d+)?$";
	public static final String REGULAR_INTEGER_POSITIVE = "\\d+";
	
	public static final double EPS = 0.00000000001;
	
	/**
	 * 保留小数位数
	 * 
	 * @param len
	 * @param value
	 * @return
	 */
	public static double setScale(int len, double value)
	{
		if(Double.isFinite(value) || (Double.isNaN(value)))
		{
			return 0;
		}
		/*
		long x = (long)Math.pow(10, len);
		return ((long)(value * x ))/ (double)x;*/
		
		BigDecimal bigDecimal = new BigDecimal(value);
		return bigDecimal.setScale(len, RoundingMode.HALF_UP).doubleValue();
	}
	
	/*
	public static double formatDouble(int len, double value)
	{
		BigDecimal bg = new BigDecimal(value).setScale(len, RoundingMode.HALF_UP);
		return bg.doubleValue();
	}*/
	
	/**
	 * 判断两个双精度型值是否相等
	 * 
	 * @param value1 第一个值
	 * @param value2 第二个值
	 * @param eps 最小限值误差
	 * @return 是否相等的标志
	 */
	public static boolean isEqual(double value1, double value2, double eps)
	{
		return Math.abs(value1 - value2) < eps;
	}
	
	/**
	 * 判断两个双精度型值是否相等
	 * 
	 * @param value1 第一个值
	 * @param value2 第二个值
	 * @return 是否相等的标志
	 */
	public static boolean isEqual(double value1, double value2)
	{
		return isEqual(value1, value2, EPS);
	}
	
	/**
	 * 不小于1个数
	 * @param value1
	 * @param value2
	 * @return 是否不小于的标志
	 */
	public static boolean isNotLessThan(double value1, double value2)
	{
		return (value1 - value2) > -EPS;
	}
	
	/**
	 * 从一个字符串解析出一个浮点数
	 * 
	 * @param string 含有浮点数值的字符串
	 * @return 第一个符合的值
	 */
	public static double parseDoubleFromString(String string)
	{
		Pattern p = Pattern.compile("\\d+(\\.\\d+)?");
		Matcher m = p.matcher(string);
		//System.out.println("是否匹配：" + m.matches());
		if(m.find())
		{
			//System.out.println("" + m.group());
			return parseDouble(m.group());
		}
		return 0.0;
	}
	
	/**
	 * 从一个字符串中解析出第一个整数值
	 * 
	 * @param string 含有整型数字的字符串
	 * @return
	 */
	public static int parseIntegerFromString(String string)
	{
		Pattern p = Pattern.compile(REGULAR_INTEGER_POSITIVE);
		Matcher m = p.matcher(string);
		//System.out.println("是否匹配：" + m.matches());
		if(m.find())
		{
			//System.out.println("" + m.group());
			return parseInt(m.group());
		}
		return 0;
	}
	
	/**
	 * 解析整形字符串数据
	 * @param string
	 * @return
	 */
	public static String parseFirstIntegerString(String string)
	{
		Pattern p = Pattern.compile(REGULAR_INTEGER_POSITIVE);
		Matcher m = p.matcher(string);
		//System.out.println("是否匹配：" + m.matches());
		if(m.find())
		{
			//System.out.println("" + m.group());
			return m.group();
		}
		return "";
	}
	
	/**
	 * 解析得到多个整数值
	 * 
	 * @param string 含有多个整数值的字符串
	 * @return 整数值数组
	 */
	public static int[] parseAllIntegerFromString(String string)
	{
		Pattern p = Pattern.compile(REGULAR_INTEGER_POSITIVE);
		Matcher m = p.matcher(string);
		List<Integer> values = new ArrayList<>();
		while(m.find())
		{
			int i = parseInt(m.group(0));
			values.add(i);
		}
		int size = values.size();
		if(size == 0)
		{
			return null;
		}
		int[] rs = new int[size];
		for(int i = 0; i < size;i ++)
		{
			rs[i] = values.get(i);
		}
		return rs;
	}
	
	/**
	 * 从一个字符串解析出一个浮点数
	 * 
	 * @param string 含有浮点数值的字符串
	 * @return 第一个符合的值
	 */
	public static float parseFloatFromString(String string)
	{
		Pattern p = Pattern.compile("\\d+(\\.\\d+)?");
		Matcher m = p.matcher(string);
		//System.out.println("是否匹配：" + m.matches());
		if(m.find())
		{
			//System.out.println("" + m.group());
			return parseFloat(m.group());
		}
		return 0.0f;
	}
	
	/**
	 * 格式化浮点型数字
	 * 
	 * @param len 长度
	 * @param value 数值
	 * @return 格式化后的字符串
	 */
	public static String formatDouble(int len, double value)
	{
		return String.format("%." + len + "f", value);
	}
	
	/**
	 * 保留小数位数
	 * 
	 * @param len
	 * @param value
	 * @return
	 */
	public static float setFloatScale(int len, double value)
	{
		int x = (int)Math.pow(10, len);
		return (Math.round(value * x ))/ (float)x;
	}
	
	/**
	 * Parse the double.
	 * @param obj
	 * @return
	 */
	public static double parseDouble(Object obj)
	{
		if(obj == null)
			return 0.0;
		return parseDouble(obj.toString());
	}
	
	
	/**
	 * Parse double value.
	 * @param str
	 * @return
	 */
	public static double parseDouble(String str)
	{
		try
		{
			str = str.replace(",", "");
			return Double.parseDouble(str);
		}
		catch(Exception e)
		{
			return 0.0;
		}
	}
	
	/**
	 * Parse the long value from the string value.
	 * @param str String value.
	 * @return long value.
	 */
	public static long parseLong(String str)
	{
		try
		{
			return Long.parseLong(str);
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	
	/**
	 * Parse the double.
	 * @param obj
	 * @return
	 */
	public static float parseFloat(Object obj)
	{
		if(obj == null)
			return 0.0f;
		return parseFloat(obj.toString());
	}
	
	/**
	 * Parse double value.
	 * @param str
	 * @return
	 */
	public static float parseFloat(String str)
	{
		try
		{
			str = str.replace(",", "");
			return Float.parseFloat(str);
		}
		catch(Exception e)
		{
			return 0.0f;
		}
	}
	
	/**
	 * Parse the double.
	 * @param obj
	 * @return
	 */
	public static int parseInt(Object obj)
	{
		if(obj == null)
			return 0;
		return parseInt(obj.toString());
	}
	
	/**
	 * Parse the integer value.
	 * 
	 * @param str
	 * @return
	 */
	public static int parseInt(String str)
	{
		try
		{
			str = str.replace(",", "");
			return Integer.parseInt(str);
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	
	/**
	 * 转换short为byte
	 *
	 * @param b
	 * @param s
	 *            需要转换的short
	 * @param index
	 */
	public static void shortToByte(short s, byte b[], int index)
	{
		b[index + 1] = (byte) (s >> 8);
		b[index + 0] = (byte) (s >> 0);
	}

	/**
	 * 通过byte数组取到short
	 *
	 * @param b
	 * @param index
	 *            第几位开始取
	 * @return
	 */
	public static short byteToShort(byte[] b, int index)
	{
		return (short) (((b[index + 1] << 8) | b[index + 0] & 0xff));
	}

	/**
	 * byte数组转换为int整数
	 *
	 * @param bytes
	 *            byte数组
	 * @param off
	 *            开始位置
	 * @return int整数
	 */
	public static int byte4ToIntRev(byte[] bytes, int off)
	{
		int b0 = bytes[off] & 0xFF;
		int b1 = bytes[off + 1] & 0xFF;
		int b2 = bytes[off + 2] & 0xFF;
		int b3 = bytes[off + 3] & 0xFF;
		return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
	}

	/**
	 * byte数组转换为int整数
	 *
	 * @param bytes
	 *            byte数组
	 * @param off
	 *            开始位置
	 * @return int整数
	 */
	public static int byte4ToInt(byte[] bytes, int off)
	{
		int b0 = bytes[off] & 0xFF;
		int b1 = bytes[off + 1] & 0xFF;
		int b2 = bytes[off + 2] & 0xFF;
		int b3 = bytes[off + 3] & 0xFF;
		return (b3 << 24) | (b2 << 16) | (b1 << 8) | b0;
	}

	/**
	 * 字节转换为浮点
	 *
	 * @param b
	 *            字节（至少4个字节）
	 * @param index
	 *            开始位置
	 * @return
	 */
	public static float byte4Tofloat(byte[] b, int index)
	{
		int l;
		l = b[index + 0];
		l &= 0xff;
		l |= ((long) b[index + 1] << 8);
		l &= 0xffff;
		l |= ((long) b[index + 2] << 16);
		l &= 0xffffff;
		l |= ((long) b[index + 3] << 24);
		return Float.intBitsToFloat(l);
	}

	/**
	 * 字节转换为浮点
	 *
	 * @param b
	 *            字节（至少4个字节）
	 * @param index
	 *            开始位置
	 * @return
	 */
	public static float byte2floatRev(byte[] b, int index)
	{
		int l;
		l = b[index + 3];
		l &= 0xff;
		l |= ((long) b[index + 2] << 8);
		l &= 0xffff;
		l |= ((long) b[index + 1] << 16);
		l &= 0xffffff;
		l |= ((long) b[index + 0] << 24);
		return Float.intBitsToFloat(l);
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] intTo4Byte(int i)
	{
		byte[] targets = new byte[4];
		targets[3] = (byte) (i & 0xFF);
		targets[2] = (byte) (i >> 8 & 0xFF);
		targets[1] = (byte) (i >> 16 & 0xFF);
		targets[0] = (byte) (i >> 24 & 0xFF);
		return targets;
	}
	
	/**
	 * 
	 * @param i
	 * @param bytes
	 * @param offset
	 */
	public static void intTo4Byte(int i, byte[] bytes, int offset)
	{
		bytes[offset + 0] = (byte) (i & 0xFF);
		bytes[offset + 1] = (byte) (i >> 8 & 0xFF);
		bytes[offset + 2] = (byte) (i >> 16 & 0xFF);
		bytes[offset + 3] = (byte) (i >> 24 & 0xFF);
	}

	/**
	 * 浮点转换为字节
	 * 
	 * @param f
	 * @return
	 */
	public static void floatTo4byte(float f, byte[] bytes, int offset)
	{
		// 把float转换为byte[]
		int fbit = Float.floatToIntBits(f);
		bytes[offset] = (byte) (fbit >> 0);
		bytes[offset + 1] = (byte) (fbit >> 8);
		bytes[offset + 2] = (byte) (fbit >> 16);
		bytes[offset + 3] = (byte) (fbit >> 24);
	}
	
	/**
	 * 检测数据是否为数字
	 * 
	 * @param value 数字
	 * @return 是否为数字
	 */
	public static boolean isNumber(String value)
	{
		try
		{
			new BigDecimal(value);
			//Double.parseDouble(value);
			return true;
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * @param object
	 * @return
	 */
	public static boolean parseBoolean(String str)
	{
		try
		{
			if("true".equalsIgnoreCase(str) || "是".equals(str))
			{
				return true;
			}
		}
		catch(Exception e)
		{			
		}
		return false;
	}
}
