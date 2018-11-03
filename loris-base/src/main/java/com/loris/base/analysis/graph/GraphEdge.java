package com.loris.base.analysis.graph;

import java.util.ArrayList;
import java.util.List;

public class GraphEdge
{
	private String id;
	private String source;
	private String target;
	private int size;
	private float value;
	private List<String> attrs = new ArrayList<>();
	
	public GraphEdge()
	{
	}
	
	public GraphEdge(String id)
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
	public String getSource()
	{
		return source;
	}
	public void setSource(String source)
	{
		this.source = source;
	}
	public String getTarget()
	{
		return target;
	}
	public void setTarget(String target)
	{
		this.target = target;
	}
	public int getSize()
	{
		return size;
	}
	public void setSize(int size)
	{
		this.size = size;
	}
	public float getValue()
	{
		return value;
	}
	public void setValue(float value)
	{
		this.value = value;
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

	@Override
	public String toString()
	{
		return "GraphEdge [id=" + id + ", source=" + source + ", target=" + target + ", size=" + size + ", value="
				+ value + ", attrs=" + attrs + "]";
	}
	
}
