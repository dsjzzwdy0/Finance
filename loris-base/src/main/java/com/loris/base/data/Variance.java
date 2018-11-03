package com.loris.base.data;

import com.loris.base.util.NumberUtil;

/**
 * 方差统计值数据
 * @author deng
 *
 */
public class Variance
{
	protected String name;
	protected int size;
	protected double min;
	protected double max;
	protected double avg;
	protected double totalerr;
	protected double stderr;
	protected int overnum;

	public Variance()
	{
	}
	public Variance(String name)
	{
		this.name = name;
	}
	
	public Variance(String name, int size, double min, double max, double avg, double totalerr, double stderr, int overnum)
	{
		this.name = name;
		this.size = size;
		this.min = min;
		this.max = max;
		this.avg = avg;
		this.totalerr = totalerr;
		this.stderr = stderr;
		this.overnum = overnum;
	}
	
	/**
	 * 设置方差值
	 * @param var
	 */
	public void setVariance(Variance var)
	{
		this.name = var.name;
		this.size = var.size;
		this.min = var.min;
		this.max = var.max;
		this.avg = var.avg;
		this.totalerr = var.totalerr;
		this.stderr = var.stderr;
		this.overnum = var.overnum;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getSize()
	{
		return size;
	}
	public void setSize(int size)
	{
		this.size = size;
	}
	public double getMin()
	{
		return min;
	}
	public void setMin(double min)
	{
		this.min = min;
	}
	public double getMax()
	{
		return max;
	}
	public void setMax(double max)
	{
		this.max = max;
	}
	public double getAvg()
	{
		return avg;
	}
	public void setAvg(double avg)
	{
		this.avg = avg;
	}
	public double getTotalerr()
	{
		return totalerr;
	}
	public void setTotalerr(double totalerr)
	{
		this.totalerr = totalerr;
	}
	public double getStderr()
	{
		return stderr;
	}
	public void setStderr(double stderr)
	{
		this.stderr = stderr;
	}
	
	public void setScale()
	{
		this.setScale(2);
	}
	
	public void setScale(int len )
	{
		min = NumberUtil.setScale(len, min);
		max = NumberUtil.setScale(len, max);
		avg = NumberUtil.setScale(len, avg);
		totalerr = NumberUtil.setScale(len, totalerr);
		stderr = NumberUtil.setScale(len, stderr);
	}
	/*
	public Map<String, Double> getStats()
	{
		return stats;
	}
	public void setStats(Map<String, Double> stats)
	{
		for (String key : stats.keySet())
		{
			this.stats.put(key, stats.get(key));
		}
	}
	public void add(String key, Double value)
	{
		stats.put(key, value);
	}
	
	public void addOneRecord(String key)
	{
		Double d = stats.get(key);
		if(d == null)
		{
			stats.put(key, 1.0);
		}
		else
		{
			stats.put(key, d + 1.0);
		}
	}*/
	public int getOvernum()
	{
		return overnum;
	}
	public void setOvernum(int overnum)
	{
		this.overnum = overnum;
	}
	@Override
	public String toString()
	{
		return "Variance [name=" + name + ", size=" + size + ", min=" + min + ", max=" + max + ", avg=" + avg
				+ ", totalerr=" + totalerr + ", stderr=" + stderr + "]";
	}	
}
