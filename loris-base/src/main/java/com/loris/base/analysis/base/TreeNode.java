package com.loris.base.analysis.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树节点类，树节点的元素有几类：
 *    1、attributeName： 表示树节点的属性名称，表名当前节点所使用的属性类型
 *    2、children: 子树节点，是一个hash表，标识节点的类型
 *    3、isLeaf：说明是否是叶节点，即没有根节点
 *    4、classifyAttribute： 如果是叶节点，则用该属性标识类来表示分类的最终结果
 * 
 * @author dsj
 *
 */
public class TreeNode
{
	/** 当前节点的属性名称. */
	private String attributeName;
	
	/** The children node. */
	private Map<Attribute<?>, TreeNode> children = new HashMap<>();
	
	/** 是否叶节点 */
	private boolean isLeaf;
	
	/** 结果判别节点 */
	private Attribute<?> classifyAttribute = null;
	
	/**
	 * 已使用的属性值.
	 */
	protected List<String> usedAttributeNames = new ArrayList<String>();
	
	/**
	 * 树节点定义
	 */
	public TreeNode(String attributeName)
	{
		isLeaf = false;
		this.attributeName = attributeName;
	}
	
	/**
	 * Create a Leaf Node.
	 * 
	 * @param classifyAttribute
	 */
	public TreeNode(String attributeName, Attribute<?> classifyAttribute)
	{
		isLeaf = true;
		this.attributeName = attributeName;
		this.classifyAttribute = classifyAttribute;
	}
	

	/**
	 * Set the ClassifyAttribute.
	 * 
	 * @param classifyAttribute
	 */
	public void setClassifyAttribute(Attribute<?> classifyAttribute)
	{
		isLeaf = true;
		this.classifyAttribute = classifyAttribute;
	}

	/**
	 * Get the Attribute name.
	 * 
	 * @return
	 */
	public String getAttributeName()
	{
		return attributeName;
	}
	
	/**
	 * Set the attribute name.
	 * @param attributeName
	 */
	public void setAttributeName(String attributeName)
	{
		this.attributeName = attributeName;
	}
	
	/**
	 * Get the children.
	 * @return
	 */
	public Map<Attribute<?>, TreeNode> getChildren()
	{
		return children;
	}
	
	/**
	 * Add the child.
	 * 
	 * @param attribute
	 * @param node
	 */
	public void addChild(Attribute<?> attribute, TreeNode node)
	{
		children.put(attribute, node);
	}
	
	/**
	 * Add all the child node.
	 * 
	 * @param childNodes
	 */
	public void addChilds(Map<Attribute<?>, TreeNode> childNodes)
	{
		children.putAll(childNodes);
	}
	
	/**
	 * Get the ChildTreeNode.
	 * 
	 * @param attribute
	 * @return
	 */
	public TreeNode getChildTreeNode(Attribute<?> attribute)
	{
		if(isLeaf)
		{
			return null;
		}
		for (Attribute<?> attr : children.keySet())
		{
			if(attr.equals(attribute) || attr.contains(attribute))
			{
				return children.get(attr);
			}
		}
		return null;
	}

	/**
	 * Get the ClassifyAttribute.
	 * @return
	 */
	public Attribute<?> getClassifyAttribute()
	{
		return classifyAttribute;
	}

	public boolean isLeaf()
	{
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf)
	{
		this.isLeaf = isLeaf;
	}

	public List<String> getUsedAttributeNames()
	{
		return usedAttributeNames;
	}

	/**
	public void setUsedAttributeNames(List<String> usedAttributeNames)
	{
		this.usedAttributeNames = usedAttributeNames;
	}*/
	
	/**
	 * Add Used AttributeNames.
	 * 
	 * @param usedAttributeNames
	 */
	public void addUsedAttributeNames(List<String> usedAttributeNames)
	{
		this.usedAttributeNames.addAll(usedAttributeNames);
	}
	
	/**
	 * Add the usedNttributeName.
	 * 
	 * @param usedAttributeName
	 */
	public void addUsedAttributeName(String usedAttributeName)
	{
		usedAttributeNames.add(usedAttributeName);
	}

	@Override
	public String toString()
	{
		return "TreeNode [attributeName=" + attributeName + ", children=" + children + ", isLeaf=" + isLeaf
				+ ", classifyAttribute=" + classifyAttribute + ", usedAttributeNames=" + usedAttributeNames + "]";
	}
}
