package com.loris.stock.analysis.calculator;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import com.loris.stock.bean.model.StockDataset;

/**
 * 数据集内部自分析函数
 * @author usr
 *
 */
public class OneDatasetAnalyzer
{
	private int itemCount;
	
	/**
	 * 起始的时间点
	 * 截止的时间点
	 */
	private long start, end;
	
	/**
	 * 相关系统计算
	 */
	private PearsonsCorrelation covariance;
	
	/**
	 * 数据集
	 */
	private StockDataset dataset;
	
	/**
	 * Create a new instance of OneDatasetAnalyzer.
	 */
	public OneDatasetAnalyzer()
	{
		covariance = new PearsonsCorrelation();	
	}
	
	/**
	 * 设置数据集
	 * @param dataset
	 */
	public void setStockDataset(StockDataset dataset)
	{
		this.dataset = dataset;
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
	 * 数据
	 * @return
	 */
	public int getItemCount()
	{
		return itemCount;
	}
	
	/**
	 * 设置计算的数据量
	 * @param count
	 */
	public void setItemCount(int count)
	{
		this.itemCount = count;
	}
	
	/**
	 * 收盘价和成交量协相关系数计算
	 * @return
	 */
	public double corelationCloseVolume()
	{
		int totalCount = dataset.getItemsCount();
		int count = itemCount;
		if(count > totalCount)
			count = totalCount;
		if(count < 10)
			return 0.0;
		
		double[][] data = new double[2][count];
		for(int i = 0; i < count; i ++)
		{
			data[0][i] = dataset.getCloseAt(totalCount - count + i);
			data[1][i] = dataset.getVolumeAt(totalCount - count + i);
		}
		return covariance.correlation(data[0], data[1]);
	}
	
	/**
	 * 最高价、最低价之差与成交量的相关性系数测试
	 * @return
	 */
	public double corelationDiffVolume()
	{
		int totalCount = dataset.getItemsCount();
		int count = itemCount;
		if(count > totalCount)
			count = totalCount;
		if(count < 10)
			return 0.0;
		
		double[][] data = new double[2][count];
		for(int i = 0; i < count; i ++)
		{
			data[0][i] = dataset.getHighAt(totalCount - count + i) - dataset.getLowAt(totalCount - count + i);
			data[1][i] = dataset.getVolumeAt(totalCount - count + i);
		}
		return covariance.correlation(data[0], data[1]);
	}
	
	/**
	 * 成交量、收盘价的增量之间的关系
	 * @return
	 */
	public double corelationDeviationValue()
	{
		int totalCount = dataset.getItemsCount();
		int count = itemCount;
		if(count > totalCount)
			count = totalCount;
		if(count < 10)
			return 0.0;
		
		double[][] data = new double[2][count];
		for(int i = 0; i < count; i ++)
		{
			if(totalCount - count + i == 0)
			{
				data[0][i] = dataset.getCloseAt(totalCount - count + i) - dataset.getCloseAt(totalCount - count + i);
				data[1][i] = dataset.getVolumeAt(totalCount - count + i) - dataset.getVolumeAt(totalCount - count + i);
			}
			else
			{
				data[0][i] = dataset.getCloseAt(totalCount - count + i) - dataset.getCloseAt(totalCount - count + i - 1);
				data[1][i] = dataset.getVolumeAt(totalCount - count + i) - dataset.getVolumeAt(totalCount - count + i - 1);
			}
		}
		
		return covariance.correlation(data[0], data[1]);
	}
}
