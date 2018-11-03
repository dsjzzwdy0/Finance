package com.loris.base.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.analysis.logistics.BaseElement;
import com.loris.base.analysis.logistics.Element;

/**
 * 
 * @author deng
 *
 */
public class IrisData
{
	/**
	 * 读取数据文件
	 * @param filename
	 * @param attrSize
	 * @return
	 * @throws IOException
	 */
	public static List<Element> getDataset(String filename, int attrSize) throws IOException
	{
		return getDataset(filename, attrSize, ",");
	}
	
	/**
	 * 读取数据集参数
	 * @param filename
	 * @param attrSize
	 * @param delimiter
	 * @return
	 * @throws IOException
	 */
	public static List<Element> getDataset(String filename, int attrSize, String delimiter) throws IOException
	{		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename))))
		{
			String line;
			List<Element> dataset = new ArrayList<>();
			Map<String, Double> labels = new HashMap<>();
			String labelName;
			double label;
			
			while(StringUtils.isNotEmpty(line = reader.readLine()))
			{
				BaseElement element = (BaseElement)parseRecord(line, attrSize, delimiter);
				if(element != null)
				{
					labelName = element.getLabelname();
					label = getOrCreateLabel(labels, labelName);
					element.setLabel(label);
					dataset.add(element);					
				}
			}
						
			return dataset;
		}
	}
	
	/**
	 * 获得最新的标志
	 * @param labels
	 * @param name
	 * @return
	 */
	protected static double getOrCreateLabel(Map<String, Double> labels, String name)
	{
		Double d = labels.get(name);
		if(d == null)
		{
			d = new Double(labels.size());
			labels.put(name, d);
		}
		return d;
	}
	
	/**
	 * 解析数据
	 * @param line
	 * @param attrSize
	 * @param delimiter
	 * @return
	 */
	protected static Element parseRecord(String line, int attrSize, String delimiter)
	{
		String[] strings = line.split(delimiter);
		if(strings.length != attrSize + 1)
		{
			return null;
		}
		
		double[] attrs = new double[attrSize];
		for (int i = 0; i < attrSize; i ++)
		{
			try
			{
				attrs[i] = Double.parseDouble(strings[i]);
			}
			catch (Exception e) {
				return null;
			}
		}
		BaseElement element = new BaseElement();
		element.setAttributes(attrs);
		element.setLabelname(strings[attrSize]);
		return element;
	}
}
