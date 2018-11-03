package com.loris.base.analysis.classifier;

import java.util.List;

import com.loris.base.analysis.base.Attribute;
import com.loris.base.analysis.base.Element;

/**
 * CART Tree: Classification And Regression Tree
 * 它采用一种二分递归分割的技术，分割方法采用基于最小距离的基尼指数估计函数，
 * 将当前的样本集分为两个子样本集，使得生成的的每个非叶子节点都有两个分支。
 * 因此，CART算法生成的决策树是结构简洁的二叉树。
 * 
 * @author dsj
 *
 */
public class CARTTree implements Classifier
{

	@Override
	public Attribute<?> bestClassify(Element element)
	{
		return null;
	}

	@Override
	public List<Attribute<?>> classify(Element element)
	{
		return null;
	}

	@Override
	public boolean train(List<Element> list)
	{
		return false;
	}

	@Override
	public void test(List<Element> list)
	{
		
	}

}
