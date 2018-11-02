package com.loris.base.analysis.logistics;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author deng
 *
 */
public interface Element extends Serializable
{
	/**
	 * 数据的标签
	 * @return
	 */
	public double getLabel();
	
	/**
	 * Set the label value.
	 * @param label
	 */
	public void setLabel(double label);
	
	/**
	 * Set the label name.
	 * @param name
	 */
	public void setLabelname(String name);
	
	/**
	 * 数据标签名称
	 * @return
	 */
	public String getLabelname();
	
	/**
	 * 属性数据
	 * @return
	 */
	public List<Double> getAttributes();
	
	/**
	 * Set the Attributes.
	 * @param attributes
	 */
	public void setAttributes(List<Double> attributes);
	
	/**
	 * 属性数据的个数
	 * @return
	 */
	public int attributeSize();
}
