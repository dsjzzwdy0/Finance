package com.loris.base.analysis.stat;

import com.loris.base.analysis.model.Freqs;
import com.loris.base.data.Variance;
import com.loris.base.util.NumberUtil;

public class Statistics
{
	/**
	 * 统计数据值
	 * @param values
	 * @param size
	 */
	public static Variance statVar(String name, double[] values, double interval, double overTimes)
	{
		double sum = 0.0f;
		double v;
		double avg = 0.0;
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		double terr = 0.0;
		double stderr = 0.0;
		int overnum = 0;
		
		int size = values.length;
		for(int i = 0; i < size; i ++)
		{
			v = values[i];
			if(min > v)
			{
				min = v;
			}			
			if(max < v)
			{
				max = v;
			}
			sum += v;
		}
		if(size != 0)
		{
			avg = sum / size;
		}
		
		for(int i = 0; i < size; i ++)
		{
			v = values[i];
			terr += Math.pow(v - avg, 2);
		}
		if(size != 0)
		{
			stderr = Math.sqrt(terr / size);
		}
		double stderr3 = stderr * overTimes;		
		for(int i = 0; i < size; i ++)
		{
			if(Math.abs(values[i] - avg) >  stderr3)
			{
				overnum ++;
			}
		}
		
		return new Variance(name, size, min, max, avg, terr, stderr, overnum);
	}
	
	/**
	 * 按照区间统计各区间的数据
	 * @param var
	 * @param values
	 * @param interval
	 * @param keyLen 关键字保留的位数长度
	 */
	public static Freqs statValueInterval(Variance var, double[] values, double interval, int keyLen, boolean fixedAvg)
	{
		Freqs counter = new Freqs();
		double avg = NumberUtil.setScale(keyLen, var.getAvg());
		if(fixedAvg)
		{
			avg = NumberUtil.setScale(keyLen - 1, avg);
		}
		double min = var.getMin();
		double max = var.getMax();
		
		int leftsize = (int)((avg - min) / interval);
		int rightsize = (int)((max - avg) / interval);
		
		for(int i = 0; i < leftsize; i ++)
		{
			String str = NumberUtil.formatDouble(keyLen, avg - interval * (leftsize - i));
			counter.add(str, 0);
		}
		counter.add(NumberUtil.formatDouble(keyLen, avg), 0);
		for(int i = 1; i <= rightsize; i ++)
		{
			String str = NumberUtil.formatDouble(keyLen, avg + interval * i);
			counter.add(str, 0);
		}
		
		int size = values.length;
		int index;
		String key;
		for(int i = 0; i < size;i ++)
		{
			index = (int)((values[i] - avg) / interval);
			key = NumberUtil.formatDouble(keyLen, avg + interval * index);
			counter.add(key);;			
		}
		return counter;
	}
}
