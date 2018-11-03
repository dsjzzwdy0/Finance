package com.loris.base.analysis.base;

import java.util.List;

public interface Element
{
	/**
	 * 获得决定属性，也称为分类目标
	 * @return
	 */
	Attribute<? extends Object> getDecisionAttribute();
	
	/**
	 * 得到属性值
	 * @param attributeName
	 * @return
	 */
	Attribute<? extends Object> getAttribute(String attributeName);
	
	/**
	 * Get classification attribute name.
	 * 
	 * @return
	 */
	List<String> getClassificationAttributeNames();
	
	/**
	 * Get all DecisionAttribute. 目标分类的种类
	 * 
	 * @return
	 */
	List<Attribute<?>> getAllDecisionAttributes();
}
