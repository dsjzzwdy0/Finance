package com.loris.base.analysis.graph;

import java.util.ArrayList;
import java.util.List;

public class GraphNode
{
	private String id;
	private String name;	
	private String color;
	private float x;
	private float y;
	private int size;
	private String value;
	private String label;
	private List<String> attrs = new ArrayList<>();
	
	public GraphNode()
	{
	}
	
	/**
	 * Create a new instance of GraphNode with id.
	 * @param id Id value.
	 */
	public GraphNode(String id)
	{
		this.id = id;
	}
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getColor()
	{
		return color;
	}
	public void setColor(String color)
	{
		this.color = color;
	}
	public float getX()
	{
		return x;
	}
	public void setX(float x)
	{
		this.x = x;
	}
	public float getY()
	{
		return y;
	}
	public void setY(float y)
	{
		this.y = y;
	}
	public int getSize()
	{
		return size;
	}
	public void setSize(int size)
	{
		this.size = size;
	}
	public List<String> getAttrs()
	{
		return attrs;
	}
	public void setAttrs(List<String> attrs)
	{
		this.attrs = attrs;
	}
	public void addAttribute(String attr)
	{
		attrs.add(attr);
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	public String getLabel()
	{
		return label;
	}
	public void setLabel(String label)
	{
		this.label = label;
	}

	@Override
	public String toString()
	{
		return "GraphNode [id=" + id + ", name=" + name + ", color=" + color + ", x=" + x + ", y=" + y + ", size="
				+ size + ", value=" + value + ", label=" + label + ", attrs=" + attrs + "]";
	}
}
