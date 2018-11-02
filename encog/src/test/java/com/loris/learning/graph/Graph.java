package com.loris.learning.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph
{
	/** 创建者 */
	protected String creator;
	
	/** 名称 */
	protected String name;
	
	/** 时间戳 */
	protected String timeStamp;
	
	/** Nodes values. */
	List<Node> nodes = new ArrayList<>();
	
	/** Edges values. */
	List<Edge> edges = new ArrayList<>();
	
	/** 距离矩阵 */
	protected float[][] distances;

	
	public void addNode(Node node)
	{
		for (Node n : nodes)
		{
			if(n.equals(node))
			{
				return;
			}
		}
		nodes.add(node);
	}
	
	public void addEdge(Edge edge)
	{
		edges.add(edge);
	}

	public List<Node> getNodes()
	{
		return nodes;
	}

	public void addNodes(List<Node> nodes)
	{
		this.nodes.addAll(nodes);
	}

	public List<Edge> getEdges()
	{
		return edges;
	}

	public void addEdges(List<Edge> edges)
	{
		this.edges.addAll(edges);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getTimeStamp()
	{
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp)
	{
		this.timeStamp = timeStamp;
	}

	public String getCreator()
	{
		return creator;
	}

	public void setCreator(String creator)
	{
		this.creator = creator;
	}
	
	public int nodeSize()
	{
		return nodes.size();
	}
	
	public int edgeSize()
	{
		return edges.size();
	}
	
	public float[][] getDistances()
	{
		return distances;
	}

	public void setDistances(float[][] distances)
	{
		this.distances = distances;
	}
	
	public float getDistance(int row, int col)
	{
		return distances[row][col];
	}
	
	
	public Node getNodeByIndex(int index)
	{
		for (Node n : nodes)
		{
			if(index == n.getIndex())
			{
				return n;
			}
		}
		return null;
	}

	/**
	 * 通过ID值获得节点
	 * @param id ID值
	 * @return 节点
	 */
	public Node getNodeById(String id)
	{
		for (Node n : nodes)
		{
			if(id.equals(n.getId()))
			{
				return n;
			}
		}
		return null;
	}
	
	/**
	 * 通过节点ID值获得与之相连通的所有的边
	 * @param node 节点ID值
	 * @return 连通向边集合
	 */
	public List<Edge> getEdges(Node node)
	{
		List<Edge> es = new ArrayList<>();
		for (Edge edge : edges)
		{
			if(edge.isDirected())
			{
				if(edge.getStart().equals(node))
				{
					es.add(edge);
				}
			}
			else
			{
				if(edge.getStart().equals(node) || edge.getEnd().equals(node))
				{
					es.add(edge);
				}
			}
		}
		return es;
	}
}
