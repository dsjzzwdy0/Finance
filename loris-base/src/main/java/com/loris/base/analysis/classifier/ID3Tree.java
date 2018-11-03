package com.loris.base.analysis.classifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.log4j.Logger;

import com.loris.base.analysis.base.Attribute;
import com.loris.base.analysis.base.Element;
import com.loris.base.analysis.base.Factor;
import com.loris.base.analysis.base.FactorType;
import com.loris.base.analysis.base.AttributeFreqs;
import com.loris.base.analysis.base.TreeNode;
import com.loris.base.analysis.classifier.util.ClassifierUtil;
import com.loris.base.data.PairValue;

/**
 * ID3算法是一种贪心算法，用来构造决策树。ID3算法起源于概念学习系统（CLS），以信息熵的
 * 下降速度为选取测试属性的标准，即在每个节点选取还尚未被用来划分的具有最高信息增益的属
 * 性作为划分标准，然后继续这个过程，直到生成的决策树能完美分类训练样例。
 * 
 * @author dsj
 *
 */
public class ID3Tree implements Classifier
{
	protected static Logger logger = Logger.getLogger(ID3Tree.class);

	/** 根节点 */
	private TreeNode root;

	/**
	 * Create a new instance of ID3Tree.
	 */
	public ID3Tree()
	{
		root = new TreeNode("root");
	}

	/**
	 * Get the classify.
	 * 
	 */
	@Override
	public Attribute<?> bestClassify(Element element)
	{
		if (root == null)
		{
			return null;
		}
		TreeNode current = root;
		while (current != null)
		{
			String attributeName = current.getAttributeName();

			Attribute<?> attr = element.getAttribute(attributeName);
			current = current.getChildTreeNode(attr);

			if (current == null)
			{
				return null;
			}
			if(current.isLeaf())
			{
				return current.getClassifyAttribute();
			}
		}
		return null;
	}

	/**
	 * 没有实现
	 */
	@Override
	public List<Attribute<?>> classify(Element element)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 训练数据
	 */
	@Override
	public boolean train(List<Element> list)
	{
		// 处理队列
		Queue<PairValue<TreeNode, List<Element>>> nodes = new LinkedList<>();

		// 构造根节点的数据对
		PairValue<TreeNode, List<Element>> pair = new PairValue<>(root, list);
		nodes.offer(pair);

		do
		{
			// 当前的树点节与树节点所代表的元素
			PairValue<TreeNode, List<Element>> p = nodes.poll();
			TreeNode node = p.getKey();
			List<Element> currentElements = p.getValue();

			// 计算最佳属性值
		
			logger.info("Finding '" + node.getAttributeName() + "' not in " + node.getUsedAttributeNames().size()
					+ " best Attribute Name.");
			long start = System.currentTimeMillis();
			Factor<?> factor = getAttributeWithMaxInfoGain(currentElements, node.getUsedAttributeNames(),
					FactorType.ENTROPY);
			
			//没有可用的属性了
			if(factor == null)
				break;
			
			long end = System.currentTimeMillis();
			logger.info("Find " + factor.getAttribute() + " for root '" + node.getAttributeName() 
				+ "' spend " + (end - start) + " ms and the InfoGain = " + factor.getDeltaFactor());	

			// String attributeName =
			// getAttributeNameWithMaxInformationGain(currentElements,
			// node.getUsedAttributeNames());

			String attributeName = factor.getAttribute();
			node.setAttributeName(attributeName);
			node.addUsedAttributeName(attributeName);

			// 加入子节点
			//node.addChilds(getTreeNodeUsingAttributeName(node.getAttributeName(), currentElements,
			//		node.getUsedAttributeNames()));			
			node.addChilds(getTreeNodeUsingAttributeName(attributeName, factor, node.getUsedAttributeNames()));

			// 对于最佳分类属性建立孩子节点
			for (Map.Entry<Attribute<?>, TreeNode> e : node.getChildren().entrySet())
			{
				
				List<Element> restAttributeSet = getAttributeSets(e.getKey(), currentElements);

				//logger.info(e.getKey() + " find child elements: " + restAttributeSet.size());
				
				// 检查是否所有属性集都已被全部分类
				if (getEntropy(restAttributeSet) == 0.0 || restAttributeSet.size() < 20)
				{
					TreeNode dtln = new TreeNode(attributeName);
					Attribute<?> decisionAttribute = null;
					if (restAttributeSet.size() != 0)
					{
						decisionAttribute = restAttributeSet.get(0).getDecisionAttribute();
						dtln.setClassifyAttribute(decisionAttribute);
						dtln.setLeaf(true);
					}
					e.setValue(dtln);
				}
				else
				{
					pair = new PairValue<>(e.getValue(), restAttributeSet);
					nodes.offer(pair);
					// nodes.offer(e.getValue());
					// filterElements.offer(restAttributeSet);
				}
			}
		} while (!nodes.isEmpty());

		return false;
	}

	@Override
	public void test(List<Element> list)
	{
	}

	/**
	 * 查找最佳的分类数据属性。
	 * 
	 * @param items
	 *            待分类的数据集
	 * @param usedAttributeNames
	 *            已经使用的分类的数据集
	 * @param type
	 *            拟使用的信息离散度计算方法
	 * @return
	 */
	private Factor<?> getAttributeWithMaxInfoGain(List<Element> items, List<String> usedAttributeNames, FactorType type)
	{
		List<Factor<?>> factors = computeBestAttributeInfoFactor(items, usedAttributeNames, type);

		// 查找获得的最佳 数据
		if (factors != null && factors.size() > 0)
		{
			Collections.sort(factors, new Comparator<Factor<?>>()
			{
				@Override
				public int compare(Factor<?> o1, Factor<?> o2)
				{
					return Double.compare(o2.getDeltaFactor(), o1.getDeltaFactor());
				}
			});
			return factors.get(0);
		}
		return null;
	}

	/**
	 * 
	 * @param elements
	 * @param usedAttributeNames
	 * @return
	 */
	private List<Factor<?>> computeBestAttributeInfoFactor(List<Element> elements, List<String> usedAttributeNames,
			FactorType type)
	{
		List<Factor<?>> factors = new ArrayList<>();
		if (elements.size() == 0)
		{
			return factors;
		}

		//long start, end;
		List<String> classificationAttributeNames = elements.get(0).getClassificationAttributeNames();
		for (String attributeName : classificationAttributeNames)
		{
			if (usedAttributeNames == null || !usedAttributeNames.contains(attributeName))
			{
				//start = System.currentTimeMillis();
				Factor<?> factor = findBestSplitFactor(attributeName, elements, type);
				factors.add(factor);

				//end = System.currentTimeMillis();
				//logger.info("Spend " + (end - start) + " ms to find '" + attributeName + "' best split factor : " + factor);
			}
		}

		return factors;
	}

	/**
	 * 寻找最优的划分方法: 1、对于离散值，只计算当前的信息熵值即可； 2、对于连续值，需要寻找最好的划分点
	 * 
	 * @param attributeName
	 * @param items
	 * @return
	 */
	private Factor<?> findBestSplitFactor(String attributeName, List<Element> items, FactorType type)
	{
		Attribute<?> attribute = items.get(0).getAttribute(attributeName);
		Factor<?> factor = null;
		if (attribute.getAttributeValue() instanceof Number) // 连续数值类型的属性值
		{
			int interval = items.size() / 20;
			factor = ClassifierUtil.findBestSplitFactor(attributeName, items, type, interval);
		}
		else // 离散值类型
		{
			// 计算离散值类型的数据
			factor = ClassifierUtil.getDiscreteAttributeFactor(attributeName, items, type);
		}
		return factor;
	}

	/**
	 * 获取满足某个属性值的属性集.
	 * 
	 * @param classificationAttribute
	 *            分类属性
	 * @param attributeSets
	 *            待分类的属性集
	 * @return 满足某个属性值的属性集
	 */
	private List<Element> getAttributeSets(Attribute<?> classificationAttribute, List<Element> attributeSets)
	{
		List<Element> filteredAttributeSets = new ArrayList<Element>();
		String attributeName = classificationAttribute.getAttributeName();
		for (Element as : attributeSets)
		{
			Attribute<?> attr = as.getAttribute(attributeName);
			if(classificationAttribute.contains(attr))
			{
				filteredAttributeSets.add(as);
			}
			
			/*
			if (attr.getAttributeName().equals(classificationAttribute.getAttributeName()))
			{
				filteredAttributeSets.add(as);
			}*/
		}
		return filteredAttributeSets;
	}

	/**
	 * 获得未分类时的信息熵.
	 * 
	 * @param attributeSets
	 *            待分类的属性集
	 * @return 未分类时的信息熵
	 */
	private double getEntropy(List<Element> attributeSets)
	{
		Map<Attribute<?>, Integer> classCounter = new HashMap<Attribute<?>, Integer>();

		for (Element as : attributeSets)
		{
			Attribute<?> attr = as.getDecisionAttribute();
			if (classCounter.containsKey(attr))
			{
				int count = classCounter.get(attr);
				classCounter.put(attr, count + 1);
			}
			else
			{
				classCounter.put(attr, 1);
			}
		}
		int totalCount = attributeSets.size();
		double entropy = 0;
		for (Map.Entry<Attribute<?>, Integer> e : classCounter.entrySet())
		{
			entropy += AttributeFreqs.getEntropy(e.getValue(), totalCount);
		}
		return entropy;
	}

	/**
	 * 获取决策树的孩子节点.
	 * 
	 * @param attributeName
	 *            将父节点的属性集分裂的属性名称
	 * @param attributeSets
	 *            父节点的属性集
	 * @param usedAttributeNames
	 *            已经使用的属性名称
	 * @return
	 */
	private Map<Attribute<?>, TreeNode> getTreeNodeUsingAttributeName(String attributeName, Factor<?> factor,
			List<String> usedAttributeNames)
	{
		Map<Attribute<?>, TreeNode> decisionTreeNodeChildren = new HashMap<>();
		List<?> categories = factor.getCategories();

		for (Object object : categories)
		{
			Attribute<?> attribute = new Attribute<>(attributeName, object);
			TreeNode dtn = new TreeNode(attributeName);
			dtn.addUsedAttributeNames(usedAttributeNames);
			decisionTreeNodeChildren.put(attribute, dtn);
		}

		return decisionTreeNodeChildren;
	}

	/**
	 * 获取决策树的孩子节点.
	 * 
	 * @param attributeName
	 *            将父节点的属性集分裂的属性名称
	 * @param attributeSets
	 *            父节点的属性集
	 * @param usedAttributeNames
	 *            已经使用的属性名称
	 * @return
	
	private Map<Attribute<?>, TreeNode> getTreeNodeUsingAttributeName(String attributeName, List<Element> attributeSets,
			List<String> usedAttributeNames)
	{

		Map<Attribute<?>, TreeNode> decisionTreeNodeChildren = new HashMap<>();
		if (attributeSets.size() < 2)
		{
			return decisionTreeNodeChildren;
		}

		Attribute<?> attr = attributeSets.get(0).getAttribute(attributeName);
		if (attr.getAttributeValue() instanceof Number) // 数值分析
		{
			// 找到最合适的分割点
			// Factor<NumberValue<Double>> factor =
			// ClassifierUtil.findBestSplitFactor(attributeName, attributeSets,
			// 1000);

			// 两个节点，左节点和右节点
			/*
			 * TreeNode dtn = new TreeNode(attributeName);
			 * dtn.addUsedAttributeNames(usedAttributeNames); NumberValue<Float>
			 * numberValue = new NumberValue<>((float)factor.getMin(),
			 * (float)factor.getPoint()); Attribute<NumberValue<Float>>
			 * attribute = new Attribute<>(attributeName, numberValue);
			 * decisionTreeNodeChildren.put(attribute, dtn);
			 * 
			 * 
			 * dtn = new TreeNode(attributeName);
			 * dtn.addUsedAttributeNames(usedAttributeNames); numberValue = new
			 * NumberValue<>((float)factor.getPoint(), (float)factor.getMax());
			 * attribute = new Attribute<>(attributeName, numberValue);
			 * decisionTreeNodeChildren.put(attribute, dtn);


			// logger.info(factor);
			logger.info(decisionTreeNodeChildren);
			return decisionTreeNodeChildren;
		}
		else // 离散值分析
		{
			for (Element as : attributeSets)
			{
				attr = as.getAttribute(attributeName);
				if (!decisionTreeNodeChildren.containsKey(attr))
				{
					TreeNode dtn = new TreeNode(attributeName);
					dtn.addUsedAttributeNames(usedAttributeNames);
					decisionTreeNodeChildren.put(attr, dtn);
				}
			}
			return decisionTreeNodeChildren;
		}

	} */

	/**
	 * 获得最佳(信息增益最大)的分类属性.
	 * 
	 * @param attributeSets
	 *            待分类的属性集
	 * @param usedAttributeNames
	 *            已经使用过的分类属性
	 * @return 最佳分类属性的名称
	 * 
	 *         private String
	 *         getAttributeNameWithMaxInformationGain(List<Element> elements,
	 *         List<String> usedAttributeNames) { if (elements.size() == 0) {
	 *         return null; } String bestClassificationAttributeName = null;
	 *         double minEntropy = Double.MAX_VALUE; List<String>
	 *         classificationAttributeNames =
	 *         elements.get(0).getClassificationAttributeNames(); for (String
	 *         classificationAttributeName : classificationAttributeNames) { if
	 *         (usedAttributeNames == null ||
	 *         !usedAttributeNames.contains(classificationAttributeName)) {
	 *         double entropy = getEntropy(classificationAttributeName,
	 *         elements); if (entropy < minEntropy) { minEntropy = entropy;
	 *         bestClassificationAttributeName = classificationAttributeName; }
	 *         } } return bestClassificationAttributeName; }
	 */

	/**
	 * 对于某个特定的属性计算信息熵.
	 * 
	 * @param attributeName
	 *            特定的属性(例如: 温度)
	 * @param elements
	 *            待分类的属性集
	 * @return 某个特定的属性的信息熵
	 * 
	 *         private double getEntropy(String attributeName, List<Element>
	 *         elements) { Map<Attribute<?>, List<Element>> attributes = new
	 *         HashMap<Attribute<?>, List<Element>>(); for (Element as :
	 *         elements) { Attribute<?> attr = as.getAttribute(attributeName);
	 *         if (attributes.containsKey(attr)) { List<Element> l =
	 *         attributes.get(attr); l.add(as); } else { List<Element> l = new
	 *         ArrayList<>(); l.add(as); attributes.put(attr, l); } } int
	 *         totalCount = elements.size(); double entropy = 0; for
	 *         (Map.Entry<Attribute<?>, List<Element>> e :
	 *         attributes.entrySet()) { List<Element> l = e.getValue(); entropy
	 *         += (l.size() * 1.0 / totalCount) * getEntropy(e.getKey(), l); }
	 *         return entropy; }
	 */

	/**
	 * 对于某个特定的属性值计算信息熵.
	 * 
	 * @param classificationAttribute
	 *            特定的属性值(例如: 温度 = 高)
	 * @param attributeSets
	 *            待分类的属性集
	 * @return 某个特定的属性值的信息熵 private double getEntropy(Attribute<?>
	 *         classificationAttribute, List<Element> attributeSets) {
	 *         Map<Attribute<?>, Integer> classCounter = new
	 *         HashMap<Attribute<?>, Integer>();
	 * 
	 *         for (Element as : attributeSets) { Attribute<?> attr =
	 *         as.getAttribute(classificationAttribute.getAttributeName()); if
	 *         (attr.getAttributeName().equals(classificationAttribute.getAttributeName()))
	 *         { Attribute<?> decisionAttribute = as.getDecisionAttribute(); if
	 *         (classCounter.containsKey(decisionAttribute)) { int count =
	 *         classCounter.get(decisionAttribute);
	 *         classCounter.put(decisionAttribute, count + 1); } else {
	 *         classCounter.put(decisionAttribute, 1); } } } int totalCount =
	 *         attributeSets.size(); double entropy = 0; for
	 *         (Map.Entry<Attribute<?>, Integer> e : classCounter.entrySet()) {
	 *         entropy += Freqs.getEntropy(e.getValue(), totalCount); } return
	 *         entropy; }
	 */

}
