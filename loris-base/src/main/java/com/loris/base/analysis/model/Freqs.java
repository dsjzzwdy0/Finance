package com.loris.base.analysis.model;

import java.util.List;
import java.util.ArrayList;

public class Freqs
{
	/** 计数器*/
	private List<Freq> freqs = new ArrayList<>();
	
	/**
	 * 给某一个关键字计数器加上数值
	 * @param key
	 * @param num
	 */
	public void add(String key)
	{
		add(key, 1);
	}
	
	/**
	 * 给某一个关键字计数器加上数值
	 * @param key
	 * @param num
	 */
	public void add(String key, int num)
	{
		Freq counter = getFreq(key);
		if(counter == null)
		{
			counter = new Freq(key, num);
			freqs.add(counter);
		}
		else
		{
			counter.add(num);
		}
	}
	
	/**
	 * 获得计数器
	 * @param key 关键字
	 * @return 计算器
	 */
	public Freq getFreq(String key)
	{
		for (Freq counter : freqs)
		{
			if(key.equals(counter.getKey()))
			{
				return counter;
			}
		}
		return null;
	}
	
	/**
	 * 获得计数器
	 * @return
	 */
	public List<Freq> getFreqs()
	{
		return freqs;
	}

	@Override
	public String toString()
	{
		return "[counters=" + freqs + "]";
	}
}
