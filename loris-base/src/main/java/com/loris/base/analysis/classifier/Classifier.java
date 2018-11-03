package com.loris.base.analysis.classifier;

import java.util.List;

import com.loris.base.analysis.base.Attribute;
import com.loris.base.analysis.base.Element;

public interface Classifier
{
	/**
	 * 确定该元素属性某一个类别
	 * 
	 * @param element
	 * @return
	 */
	Attribute<?> bestClassify(Element element);
	
	/**
	 * 按照类别进行分类，返回每一类的可能比
	 * @param element
	 * @return
	 */
	List<Attribute<?>> classify(Element element);
	
	/**
	 * 训练模型
	 * 
	 * @param list
	 */
	boolean train(List<Element> list);
	
	/**
	 * 评价与测试模型
	 * 
	 * @param list
	 */
	void test(List<Element> list);
}
