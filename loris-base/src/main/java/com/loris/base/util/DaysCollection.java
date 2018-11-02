package com.loris.base.util;

import java.util.ArrayList;
import java.util.List;

public class DaysCollection
{
	/** 成交天 */
	private List<String> days = new ArrayList<String>();

	/** 标志 */
	private short flags[];

	/**
	 * 重设标志
	 */
	public void resetFlag()
	{
		if (days.size() <= 0)
		{
			throw new IllegalArgumentException("There are no days data.");
		}
		if (flags == null || flags.length != days.size())
			flags = new short[days.size()];

		for (int i = 0; i < days.size(); i++)
		{
			flags[i] = 0;
		}
	}

	/**
	 * Add the day.
	 * 
	 * @param str
	 */
	public void addDay(String str)
	{
		days.add(str);
	}
	
	/**
	 * Set the Flag.
	 * @param str
	 * @param flag
	 */
	public void setFlag(String str, short flag)
	{
		for (int i = 0; i < days.size(); i++)
		{
			if (str.equals(days.get(i)))
			{
				flags[i] = flag;
			}
		}
	}
	
	/**
	 * Set the flag exist.
	 * @param str
	 */
	public void setFlag(String str)
	{
		setFlag(str, (short)1);
	}

	/**
	 * 获取当前的状态
	 * 
	 * @param str
	 * @return
	 */
	public int getFlag(String str)
	{
		for (int i = 0; i < days.size(); i++)
		{
			if (str.equals(days.get(i)))
			{
				return flags[i];
			}
		}
		// 表示不存在
		return -1;
	}
	
	/**
	 * Get the size of the days.
	 * @return
	 */
	public int size()
	{
		return days.size();
	}
	
	/**
	 * Get the day string.
	 * @param index
	 * @return
	 */
	public String getDateString(int index)
	{
		return days.get(index);
	}
	
	/**
	 * Get the day flag.
	 * @param index
	 * @return
	 */
	public short getFlag(int index)
	{
		return flags[index];
	}
	
	/**
	 * To String
	 */
	@Override
	public String toString()
	{
		return "[" + days.size() + "] " + days.toString();
	}
}
