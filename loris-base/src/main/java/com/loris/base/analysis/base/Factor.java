package com.loris.base.analysis.base;

import java.util.ArrayList;
import java.util.List;

/**
 * 该类用于连续变量的域值划分, 这里仅考虑单点划分的情况
 * 
 * @author dsj
 *
 */
public class Factor<T>
{	
	private String attribute;     //特征属性名称
	private double pFactor;       //父节点的熵值（或GINI值），未划分时的统计结果
	private double factor;        //划分为子节点之后的增益值
	private int total;            //当前总元素个数
	private T point;              //划分点, 当为连续值时，划分点才有意义
	private List<T> categories = new ArrayList<>();   //类别
	private String description;   //描述信息
	
	/**
	 * Create a new instance of Factor.
	 * 
	 * @param attribute 特征属性名称
	 */
	public Factor(String attribute)
	{
		this.attribute = attribute;
	}
	
	public String getAttribute()
	{
		return attribute;
	}
	public void setAttribute(String attribute)
	{
		this.attribute = attribute;
	}
	public double getpFactor()
	{
		return pFactor;
	}
	public void setpFactor(double pFactor)
	{
		this.pFactor = pFactor;
	}

	public double getFactor()
	{
		return factor;
	}

	public void setFactor(double factor)
	{
		this.factor = factor;
	}

	public T getPoint()
	{
		return point;
	}
	public void setPoint(T point)
	{
		this.point = point;
	}
	
	public double getDeltaFactor()
	{
		return pFactor - factor;
	}
	
	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}

	/**
	 * 增加分类的类别
	 * @param category
	 */
	public void addCategory(T category)
	{
		categories.add(category);
	}
	
	/**
	 * 获得分类的类别
	 * @return
	 */
	public List<T> getCategories()
	{
		return categories;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * 清理数据分类
	 */
	public void reset()
	{
		point = null;
		total = 0;
		pFactor = 0.0;
		factor = 0.0;
		description = "";
		categories.clear();
	}

	@Override
	public String toString()
	{
		return "Factor [pFactor=" + pFactor + ", factor=" + factor + ", total=" + total
				+ ", point=" + point + ", attribute=" + attribute + ", deltaFact = " + getDeltaFactor() + ", description=" + description + "]";
	}
}
