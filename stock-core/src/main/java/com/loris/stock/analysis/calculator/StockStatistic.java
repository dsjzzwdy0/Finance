package com.loris.stock.analysis.calculator;

import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.loris.stock.bean.model.StockDataset;

/**
 * 股票相关性分析
 * @author usr
 *
 */
public class StockStatistic
{
	/**
	 * 内部变量
	 */
	private StockDataset std1, std2;
	
	/**
	 * 构建
	 */
	public StockStatistic()
	{
		
	}
	
	/**
	 * 构建两个股票的统计分析类
	 * @param std1
	 * @param std2
	 */
	public StockStatistic(StockDataset std1, StockDataset std2)
	{
		this.std1 = std1;
		this.std1 = std2;		
	}
	
	/**
	 * 
	 * @param i
	 * @return
	 */
	public StockDataset getStockDataset(int i)
	{
		if(i == 0)
		{
			return std1;
		}
		else
		{
			return std2;
		}
	}
	
	/**
	 * 设置股票数据
	 * @param index
	 * @param std
	 */
	public void setStockDataset(int index, StockDataset std)
	{
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
	 * 计算值
	 * @return
	 */
	public boolean compuateStatistic()
	{
		return true;
	}
	
	/**
	 * 两支股票的描述性统计数据
	 * @return
	 */
	public double getCorariance()
	{
		int itemCount1 = std1.getItemsCount();
		int itemCount2 = std2.getItemsCount();
		
		int count = itemCount1 > itemCount2 ? itemCount2 : itemCount1;
		if(count > 100) count = 100;
		
		double[][] data = new double[2][count];
		
		for(int i = 0; i < count; i ++)
		{
			data[0][i] = std1.getCloseAt(itemCount1 - count + i);
			data[1][i] = std2.getCloseAt(itemCount2 - count + i);
		}
		
		Covariance covariance = new Covariance();	
		
		return covariance.covariance(data[0], data[1]);
	}
	
	/**
	 * 相关系数
	 * @return
	 */
	public double pearsonCorrelation()
	{
		int itemCount1 = std1.getItemsCount();
		int itemCount2 = std2.getItemsCount();
		
		int count = itemCount1 > itemCount2 ? itemCount2 : itemCount1;
		if(count > 100) count = 100;
		
		double[][] data = new double[2][count];
		
		for(int i = 0; i < count; i ++)
		{
			data[0][i] = std1.getCloseAt(itemCount1 - count + i);
			data[1][i] = std2.getCloseAt(itemCount2 - count + i);
		}
		
		PearsonsCorrelation covariance = new PearsonsCorrelation();	
		
		return covariance.correlation(data[0], data[1]);
	}
	
	/**
	 * 计算相关统计数据
	 * @param index
	 * @param count
	 */
	public void statData(int index, int count)
	{
		StockDataset std = getStockDataset(index);
		int itemCount = std.getItemsCount();
		if(itemCount < count) count = itemCount;
		
		SummaryStatistics stat = new SummaryStatistics();		
		//double[] values = new double[count];
		for(int i = 0; i < count; i ++)
		{
			//values[i] = std1.getCloseAt(itemCount - count + i);
			stat.addValue(std.getCloseAt(itemCount - count + i));
		}
		
		System.out.println(std.getStock().getKey());
		System.out.println("Mean：" + stat.getMean());
		System.out.println("Min：" + stat.getMin());
		System.out.println("Max：" + stat.getMax());
		System.out.println("Std Dev: " + stat.getStandardDeviation());
		System.out.println("Variance: " + stat.getVariance());
	}
}
