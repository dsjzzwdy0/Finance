package com.loris.base.analysis.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 统计频率与出现的次数
 * 
 * @author jiean
 *
 */
public class AttributeFreqs
{
	/** The Count value. */
	private int[] counts;
	
	/** 分类的数量 */
	private int num;
	
	/** The Destination Attribute */
	private List<Attribute<?>> attributes = new ArrayList<>();
	
	/**
	 * Create the Freqs.
	 * 
	 * @param num
	 */
	public AttributeFreqs(int num)
	{
		this.num = num;
		counts = new int[num];
	}
	
	/**
	 * Create a new Freqs.
	 * 
	 * @param attrs 属于分类的类别列表
	 */
	public AttributeFreqs(List<Attribute<?>> attrs)
	{
		this.attributes.addAll(attrs);
		this.num = attrs.size();
		counts = new int[num];
	}
	
	/**
	 * Clone a Freqs.
	 * 
	 * @return
	 */
	public AttributeFreqs cloneFreqs()
	{
		AttributeFreqs freqs = new AttributeFreqs(attributes);
		freqs.setFreqs(this);
		return freqs;
	}

	/**
	 * Get the Counts value.
	 * 
	 * @return
	 */
	public int[] getCounts()
	{
		return counts;
	}
	
	/**
	 * reset the counts.
	 */
	public void reset()
	{
		for(int i = 0; i < num; i ++)
		{
			counts[i] = 0;
		}
	}

	/**
	 * Get the Total value.
	 * 
	 * @return
	 */
	public int getTotal()
	{
		int t = 0;
		for (int n : counts)
		{
			t += n;
		}
		return t;
	}
	
	/**
	 * Set the MatchOddsSize.
	 * 
	 * @param size
	 */
	public void setFreqs(AttributeFreqs size)
	{
		if(num != size.getNum())
		{
			num = size.num;
			counts = new int[size.num];
		}
		for(int i = 0; i < num; i ++)
		{
			counts[i] = size.counts[i];
		}
		this.attributes = size.attributes;
	}

	/**
	 * Get the num value.
	 * 
	 * @return
	 */
	public int getNum()
	{
		return num;
	}
	
	/**
	 * 加入属性项
	 * @param attribute
	 */
	public void addAttribute(Attribute<?> attribute)
	{
		for(int i = 0; i < num; i ++)
		{
			if(attribute.equals(attributes.get(i)))
			{
				increase(i);
				break;
			}
		}
	}
	
	/**
	 * Increase the Value.
	 * 
	 * @param index
	 */
	public void increase(int index)
	{
		counts[index] ++;
	}
	
	/**
	 * Add the Attribute Value.
	 * 
	 * @param value
	 */
	public void addAttributeValue(Object value)
	{
		for(int i = 0; i < num; i ++)
		{
			if(attributes.get(i).getAttributeValue().equals(value))
			{
				increase(i);
				break;
			}
		}
	}
	
	/**
	 * Get the Gini Value.
	 * 
	 * @return
	 */
	public double getGini()
	{
		return getGini(counts);
	}
	
	/**
	 * 求取信息熵
	 * 
	 * @return 信息熵的值
	 */
	public double getEntropy()
	{
		return getEntropy(counts);
	}
	
	/**
	 * 计算信息熵
	 * 
	 * @param counts
	 *            分类后每组的属性值的实例数量，数组。
	 * @return  该分类结果的信息熵
	 */
	public static double getEntropy(int[] counts)
	{
		int total = 0;
		for (int i : counts)
		{
			total += i;
		}
		return getEntropy(counts, total);
	}
	
	/**
	 * 计算信息熵.
	 * 
	 * @param counts
	 *            分类后每类属性值的实例数量，数组
	 * @param totalCount
	 *            分类前该属性的实例数量
	 * @return 该属性值的信息熵
	 */
	public static double getEntropy(int[] counts, int totalCount)
	{
		double p = 0.0;
		for (int i : counts)
		{
			p += getEntropy(i, totalCount);
		}
		return p;
	}

	/**
	 * 计算信息熵.
	 * 
	 * @param currentCount
	 *            分类后该属性值的实例数量
	 * @param totalCount
	 *            分类前该属性的实例数量
	 * @return 该属性值的信息熵
	 */
	public static double getEntropy(int currentCount, int totalCount)
	{
		if (currentCount == 0 || totalCount == 0)
		{
			return 0;
		}
		double p = currentCount * 1.0 / totalCount;
		return -p * Math.log(p) / Math.log(2);
	}
	
	/**
	 * Compute the Gini value.
	 * 
	 * @param probs
	 * @return
	 */
	public static double getGini(double [] probs)
	{
		double gini = 1.0;
		for (double d : probs)
		{
			gini -= Math.pow(d, 2);
		}
		return gini;
	}
	
	/**
	 * Compute the Gini value.
	 * 
	 * @param counts
	 * @return
	 */
	public static double getGini(int[] counts)
	{
		int sum = sum(counts);
		if(sum == 0)
		{
			return 0.0;
		}
		
		double gini = 1.0;
		
		for (int i : counts)
		{
			gini -= Math.pow(i * 1.0 / sum, 2);
		}
		return gini;
	}
	
	/**
	 * 求和
	 * @param counts
	 * @return
	 */
	public static int sum(int[] counts)
	{
		int sum = 0;
		for (int i : counts)
		{
			sum += i;
		}
		return sum;
	}

	@Override
	public String toString()
	{
		return "Freqs [counts=(" + Arrays.toString(counts) + ")]";
	}
}
