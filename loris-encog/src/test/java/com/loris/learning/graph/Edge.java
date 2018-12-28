package com.loris.learning.graph;

/**
 * 
 * Create a new instance of Edge.
 * @author dsj
 *
 */
public class Edge
{
	protected Node start;
	protected Node end;
	protected double weight;		//道路长度
	
	protected double pheromone; 	//信息素
	protected EdgeType type;
	
	/** 连通类型 */
	public static enum EdgeType
	{
		DIRECTED,			//有向图
		NODIRECTED			//无向图
	}
	
	public Edge()
	{
		this.type = EdgeType.DIRECTED;
	}
	
	public Edge(Node start, Node end, double weight)
	{
		this(start, end, weight, EdgeType.DIRECTED);
	}
	
	/**
	 * Create a new instance of Edge.
	 * @param start 起始节点
	 * @param end 终止节点
	 * @param weight 节点间的权重
	 * @param type 节点类型
	 */
	public Edge(Node start, Node end, double weight, EdgeType type)
	{
		this.type = type;
		this.start = start;
		this.end = end;
		this.weight = weight;
	}

	public Node getStart()
	{
		return start;
	}

	public void setStart(Node start)
	{
		this.start = start;
	}

	public Node getEnd()
	{
		return end;
	}

	public void setEnd(Node end)
	{
		this.end = end;
	}

	public double getWeight()
	{
		return weight;
	}

	public void setWeight(double weight)
	{
		this.weight = weight;
	}

	public EdgeType getType()
	{
		return type;
	}

	public void setType(EdgeType type)
	{
		this.type = type;
	}
	
	/**
	 * 是否是有向边
	 * @return
	 */
	public boolean isDirected()
	{
		return type == EdgeType.DIRECTED;
	}
}
