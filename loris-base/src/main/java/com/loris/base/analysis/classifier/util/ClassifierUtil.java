package com.loris.base.analysis.classifier.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.loris.base.analysis.base.Attribute;
import com.loris.base.analysis.base.Element;
import com.loris.base.analysis.base.Factor;
import com.loris.base.analysis.base.FactorType;
import com.loris.base.analysis.base.AttributeFreqs;
import com.loris.base.data.NumberValue;
import com.loris.base.data.PairValue;

public class ClassifierUtil
{
	protected static Logger logger = Logger.getLogger(ClassifierUtil.class);
	
	/**
	 * 对于某个特定的属性计算信息熵.
	 * 
	 * @param attributeName
	 *            特定的属性(例如: 温度)
	 * @param elements
	 *            待分类的属性集
	 * @return 某个特定的属性的信息熵
	 */
	public static Factor<String> getDiscreteAttributeFactor(String attributeName, List<Element> elements, FactorType type)
	{
		Map<String, List<Element>> attributes = new HashMap<String, List<Element>>();
		for (Element as : elements)
		{
			Attribute<?> attr = as.getAttribute(attributeName);
			if (attributes.containsKey(attr.getAttributeValue()))
			{
				List<Element> l = attributes.get(attr);
				l.add(as);
			}
			else
			{
				List<Element> l = new ArrayList<>();
				l.add(as);
				attributes.put((String)attr.getAttributeValue(), l);
			}
		}
		int totalCount = elements.size();
		Factor<String> factor = new Factor<>(attributeName);
		double entropy = 0;
		for (Map.Entry<String, List<Element>> e : attributes.entrySet())
		{
			factor.addCategory(e.getKey());
			List<Element> l = e.getValue();
			
			AttributeFreqs freqs = ClassifierUtil.getFreqs(l);
			entropy += (l.size() * 1.0 / totalCount) * getFactor(freqs, type);
		}
		factor.setTotal(totalCount);
		factor.setFactor(entropy);
		
		return factor;
	}
	
	/**
	 * Find all split factor.
	 * 
	 * @param items
	 * @param splitSize
	 * @return
	 */
	public static List<Factor<NumberValue<Double>>> findAllSplitFactor(List<Element> items, double interval,
			FactorType type)
	{
		List<Factor<NumberValue<Double>>> factors = new ArrayList<>();
		List<String> attributes = items.get(0).getClassificationAttributeNames();
		long start, end;
		
		start = System.currentTimeMillis();
		AttributeFreqs freqs = getFreqs(items);
		end = System.currentTimeMillis();
		logger.info("Find " + freqs + " best split point spend " + (end - start) + " ms.");
		
		for (String attribute : attributes)
		{			
			start = System.currentTimeMillis();
			Factor<NumberValue<Double>> factor = findBestSplitFactor(attribute, items, type, interval);
			
			end = System.currentTimeMillis();
			logger.info("Find " + attribute + " best split point spend " + (end - start) + " ms.");
			factors.add(factor);
		}
		return factors;
	}
	
	/**
	 * 将当前的数据集按照结果类别进行统计
	 * 
	 * @param items
	 * @return
	 */
	public static AttributeFreqs getFreqs(List<Element> items)
	{
		AttributeFreqs freqs = new AttributeFreqs(items.get(0).getAllDecisionAttributes());
		Attribute<?> attribute;
		for (Element element : items)
		{
			attribute = element.getDecisionAttribute();
			freqs.addAttribute(attribute);
		}
		return freqs;
	}
	
	
	/**
	 * Split the attribute value by max entropy.
	 * 
	 * @param attribute
	 * @param items
	 */
	public static Factor<NumberValue<Double>> findBestSplitFactor(String attribute, List<Element> items, 
			FactorType type, double interval)
	{
		int total = items.size();
		Element item = null;
		Attribute<?> value;
		Attribute<?> result;
		float d;
		
		double min = Float.MAX_VALUE;
		double max = -Float.MAX_VALUE;
		
		if(!(items.get(0).getAttribute(attribute).getAttributeValue() instanceof Number))
		{
			throw new UnsupportedOperationException(attribute + " is not a number attribute.");
		}
		
		// 取最大的数据
		List<PairValue<Attribute<?>, Float>> pairs = new ArrayList<>();
		for (int i = 0; i < total; i++)
		{
			item = items.get(i);
			try
			{
				value = item.getAttribute(attribute);
				result = item.getDecisionAttribute();
				
				Number number = (Number)value.getAttributeValue();
				d = number.floatValue();
				if (d > max)
				{
					max = d;
				}
				if (d < min)
				{
					min = d;
				}

				PairValue<Attribute<?>, Float> pair = new PairValue<>(result, d);
				pairs.add(pair);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				break;
			}
		}
		
		total = pairs.size();
		Collections.sort(pairs, new Comparator<PairValue<Attribute<?>, Float>>()
			{
				@Override
				public int compare(PairValue<Attribute<?>, Float> o1, PairValue<Attribute<?>, Float> o2)
				{
					return o1.getValue().compareTo(o2.getValue());
				}
			}
		);
		//end = System.currentTimeMillis();
		//logger.info("Sort " + total + " items spend time is " + (end - start) + " ms.");
		//start = System.currentTimeMillis();
		
		Factor<NumberValue<Double>> factor = findBestSplitValue(attribute, items.get(0).getAllDecisionAttributes(), 
				pairs,
				type, min, max, interval);
		
		//end = System.currentTimeMillis();
		//logger.info("Find " + attribute + " best split point spend " + (end - start) + " ms.");
		return factor;
	}
	
	/**
	 * Find the best split value to satisfy the max gini value.
	 * 
	 * @param items
	 * @return
	 */
	protected static Factor<NumberValue<Double>> findBestSplitValue(String attribute, List<Attribute<?>> decideAttributes, 
			List<PairValue<Attribute<?>, Float>> items,
			FactorType type, double min, double max,
			double interval)
	{
		Factor<NumberValue<Double>> factor = new Factor<NumberValue<Double>>(attribute);
		int total = items.size();
	
		double pFactor = 0.0, factor1 = 0.0, factor2 = 0.0;
		double deltaFactor, tmpFactor;
		double maxDeltaGini = -Double.MAX_VALUE;
		String descript = "";
		
		interval = (max - min) / interval;
		int computeSize = (int)Math.ceil((max - min) / interval);
		
		//基本数据赋值		
		factor.setpFactor(pFactor);
		factor.setTotal(total);
		
		AttributeFreqs left = new AttributeFreqs(decideAttributes);
		AttributeFreqs right = new AttributeFreqs(decideAttributes);
		
		//最小控制数，当某一区间的变化值小于某一阈值时，不进行计算
		int eps =(int)( total * 0.0005);
		double preIndex = 0;
		double t0, t;
		int changeCount;
		
		for(int i = 1; i < computeSize; i ++)
		{
			t0 = min + preIndex * interval;
			t = min + i * interval;
			
			//查找变化的数据量
			changeCount = getElementCount(items, t0, t);
			if(changeCount < eps)
			{
				continue;
			}
			//累积的变化量
			preIndex = i;
			
			left.reset();
			right.reset();
			for (PairValue<Attribute<?>, Float> pair : items)
			{
				if(pair.getValue() <= t)
				{
					left.addAttribute(pair.getKey());
				}
				else
				{
					right.addAttribute(pair.getKey());
				}
			}
			factor1 = getFactor(left, type);
			factor2 = getFactor(right, type);
			
			int sum1 = left.getTotal();
			int sum2 = right.getTotal();
			
			//当前的信息离散值
			tmpFactor = (factor1 * sum1 +  factor2 * sum2) / total;
			deltaFactor = pFactor - tmpFactor;
			
			if(deltaFactor > maxDeltaGini)
			{
				maxDeltaGini = deltaFactor;				
				
				descript = String.format("Total=%d, t=%.3f, min(%d, %d, %d)=%d & g=%.3f, max(%d, %d, %d)=%d & g=%.3f, delta=%.3f",
						total, t,
						left.getCounts()[0], left.getCounts()[1], left.getCounts()[2], sum1, factor1,
						right.getCounts()[0], right.getCounts()[1], right.getCounts()[2], sum2, factor2, deltaFactor);
				
				factor.reset();
				
				factor.setTotal(total);
				factor.setPoint(new NumberValue<>(t));				
				factor.setFactor(tmpFactor);
				factor.addCategory(new NumberValue<>(min, t));
				factor.addCategory(new NumberValue<>(t, max));				
				factor.setDescription(descript);
			}
				
		}
		return factor;
	}
	
	/**
	 * 查找在两个数字中间的数据。
	 * 因为已经进行了排序，这里采用二分法方式加快查找的速度。
	 * 
	 * @param items 元素的个数
	 * @param m1  最小的值
	 * @param m2  最大的值
	 * @return 获得在此区间的元素个数
	 */
	public static int getElementCount(List<PairValue<Attribute<?>, Float>> items, double m1, double m2)
	{
		int total = items.size() - 1;
		int half = total / 2;
		int low = 0;
		float value;
		int num = 0;
		PairValue<?, Float> p;
		
		while(low < total)
		{
			half = (low + total) / 2;
			p = items.get(half);
			value = p.getValue();
			if(value >= m2)   //中间点的值比m1, m2中的最大值要大
			{
				total = half - 1;
				continue;
			}
			else if(value <= m1) //中间点的值比m2, m2中的最小值要小
			{
				low = half + 1;
			}
			else      //正好命中值
			{
				num ++;
				//logger.info("Find " + value);
				//计算查找个数，小于的方法，只需判断最小值的情况
				for(int i = half - 1; i > low; i --)
				{
					p = items.get(i);
					value = p.getValue();
					if(value >= m1)
					{
						//logger.info("Find min value " + value);
						num ++;
					}
				}
				for(int i = half + 1; i <total; i ++)
				{
					p = items.get(i);
					value = p.getValue();
					if(value <= m2)
					{
						//logger.info("Find max vlaue " + value);
						num ++;
					}
				}
				break;
			}
		}
		return num;
	}
	
	/**
	 * Get the Factor.
	 * 
	 * @param freqs
	 * @param type
	 * @return
	 */
	private static double getFactor(AttributeFreqs freqs, FactorType type)
	{
		if(type == FactorType.ENTROPY)
		{
			return freqs.getEntropy();
		}
		else
		{
			return freqs.getGini();
		}
	}
}
