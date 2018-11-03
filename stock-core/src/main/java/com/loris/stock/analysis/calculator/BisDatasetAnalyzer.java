package com.loris.stock.analysis.calculator;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import com.loris.stock.bean.model.StockDataset;

/**
 * Stock数据分析模型
 * 1、股票相关性分析(两个股票之间)
 * @author dsj
 *
 */
public class BisDatasetAnalyzer
{
	/**
	 * 内部变量
	 */
	private StockDataset std1, std2;
	
	/**
	 * 起始的时间点
	 * 截止的时间点
	 */
	private long start, end;
	
	/**
	 * 使用的个数
	 */
	private int itemCount = 0;
	
	/**
	 * 相关系统计算
	 */
	private PearsonsCorrelation covariance;
	
	/**
	 * 构造函数
	 */
	public BisDatasetAnalyzer()
	{	
		covariance = new PearsonsCorrelation();	
	}
	
	/**
	 * 设置用于计算的数据
	 * @param index
	 * @param std
	 */
	public void setStockDataset(int index, StockDataset std)
	{
		if(index > 1)
			throw new UnknownError();
		
		if(index == 0)
		{
			std1 = std;
		}
		else
		{
			std2 = std;
		}
	}
	
	/**
	 * 获得数据
	 * @param index
	 * @return
	 */
	public StockDataset getStockDataset(int index)
	{
		if(index > 1)
			throw new UnknownError();
		
		if(index == 0)
			return std1;
		else
			return std2;
	}
	
	/**
	 * 设置起始时间
	 * @param t
	 */
	public void setStartTime(long t)
	{
		this.start = t;
	}
	
	/**
	 * 设置结束时间
	 * @param t
	 */
	public void setEndTime(long t)
	{
		this.end = t;
	}
	
	/**
	 * 获得起始时间
	 * @return
	 */
	public long getStartTime()
	{
		return start;
	}
	
	/**
	 * 获得结束时间
	 * @return
	 */
	public long getEndTime()
	{
		return end;
	}
	
	/**
	 * 设置计算的个数
	 * @param count
	 */
	public void setItemCount(int count)
	{
		this.itemCount = count;
	}
	
	/**
	 * 获得参与计算的数据个数
	 * @return
	 */
	public int getItemCount()
	{
		int itemCount1 = std1.getItemsCount();
		int itemCount2 = std2.getItemsCount();
		
		int count = itemCount1 > itemCount2 ? itemCount2 : itemCount1;
		if(count > itemCount)
			count = itemCount;
		
		return count;
	}
	
	
	/**
	 * Pearson相关系数计算
	 * @return
	 */
	public double corelationCloseValue()
	{
		int itemCount1 = std1.getItemsCount();
		int itemCount2 = std2.getItemsCount();
		
		int count = itemCount1 > itemCount2 ? itemCount2 : itemCount1;
		if(count > itemCount) count = itemCount;
		
		if(count < 10)
			return 0.0;
		
		double[][] data = new double[2][count];
		
		for(int i = 0; i < count; i ++)
		{
			data[0][i] = std1.getCloseAt(itemCount1 - count + i);
			data[1][i] = std2.getCloseAt(itemCount2 - count + i);
		}	
		return covariance.correlation(data[0], data[1]);
	}
	
	/**
	 * 成交量相关性
	 * @return
	 */
	public double corelationVolume()
	{
		int itemCount1 = std1.getItemsCount();
		int itemCount2 = std2.getItemsCount();
		
		int count = itemCount1 > itemCount2 ? itemCount2 : itemCount1;
		if(count > itemCount) count = itemCount;
		
		if(count < 10)
			return 0.0;
		
		double[][] data = new double[2][count];
		
		for(int i = 0; i < count; i ++)
		{
			data[0][i] = std1.getVolumeAt(itemCount1 - count + i);
			data[1][i] = std2.getVolumeAt(itemCount2 - count + i);
		}	
		return covariance.correlation(data[0], data[1]);
	}
	
	/**
	 * 涨幅相关性
	 * @return
	 */
	public double corelationDiffValue()
	{
		int itemCount1 = std1.getItemsCount();
		int itemCount2 = std2.getItemsCount();
		
		int count = itemCount1 > itemCount2 ? itemCount2 : itemCount1;
		if(count > itemCount) count = itemCount;
		
		if(count < 10)
			return 0.0;
		
		double[][] data = new double[2][count];
		
		for(int i = 0; i < count; i ++)
		{
			data[0][i] = std1.getDiffValue(itemCount1 - count + i);
			data[1][i] = std2.getDiffValue(itemCount2 - count + i);
		}	
		return covariance.correlation(data[0], data[1]);
	}
}
