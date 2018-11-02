package com.loris.base.analysis.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph
{
	private String creator;
	private String createtime;
	private String title;
	
	private List<GraphNode> nodes = new ArrayList<>();
	private List<GraphEdge> edges = new ArrayList<>();
	public List<GraphNode> getNodes()
	{
		return nodes;
	}

	public List<GraphEdge> getLinks()
	{
		return edges;
	}

	public String getCreator()
	{
		return creator;
	}
	public void setCreator(String creator)
	{
		this.creator = creator;
	}
	public String getCreatetime()
	{
		return createtime;
	}
	public void setCreatetime(String createtime)
	{
		this.createtime = createtime;
	}
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * 添加节点
	 * 
	 * @param node 节点
	 */
	public void addNode(GraphNode node)
	{
		nodes.add(node);
	}
	
	/**
	 * 添加连接
	 * 
	 * @param edge
	 */
	public void addEdge(GraphEdge edge)
	{
		edges.add(edge);
	}
	
	/**
	 * 创建一个节点
	 * 
	 * @param id Id值
	 * @return 节点
	 */
	public GraphNode createNode(String id)
	{
		for (GraphNode node : nodes)
		{
			if(id.equals(node.getId()))
			{
				return null;
			}
		}
		GraphNode node = new GraphNode(id);
		nodes.add(node);
		return node;
	}
	
	/**
	 * 创建一个连接边
	 * 
	 * @param id ID的值
	 * @return 连接边
	 */
	public GraphEdge createEdge(String id)
	{
		for (GraphEdge edge : edges)
		{
			if(id.equals(edge.getId()))
			{
				return null;
			}
		}
		GraphEdge edge = new GraphEdge(id);
		edges.add(edge);
		return edge;
	}
}
