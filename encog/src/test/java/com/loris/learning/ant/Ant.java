package com.loris.learning.ant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.loris.learning.graph.Graph;
import com.loris.learning.graph.Node;

public class Ant
{
	/** 走过路径的总长度 */
	protected float sumLen = 0;
	
	//已经访问过的节点路径
	protected List<Node> visitedNodes = new ArrayList<>(); 
	
	/** 候选的数据 */
	protected List<Node> probNodes = new ArrayList<>();
	
	/** 图 */
	protected Graph graph;
	
	/** 信息数变化矩阵 */
	protected float[][] delta;
	
	/** Alpha值*/
	protected float alpha;
	
	/** Beta值 */
	protected float beta;
	
	/** 当前的节点 */
	protected Node currentNode;
	
	/**
	 * 构建一个蚂蚁
	 * @param graph 图
	 */
	public Ant(Graph graph)
	{
		this.graph = graph;
	}
	
	/**
	 * 以一个起始点构造蚂蚁
	 * @param graph 图
	 * @param start 起始节点
	 */
	public Ant(Graph graph, Node start)
	{
		this(graph);
		addStartNode(start);
	}
	
	/**
	 * 初始化蚂蚁数据，
	 * @param alpha
	 * @param beta
	 */
	public void setParams(float alpha, float beta)
	{
		//init();
		this.alpha = alpha;
		this.beta = beta;
	}
	
	private void init()
	{
		sumLen = 0;
		visitedNodes.clear();
		int nodeSize = graph.nodeSize();
		delta = new float[nodeSize][nodeSize];
		for(int i = 0; i < nodeSize; i ++)
		{
			for(int j = 0; j < nodeSize; j ++)
			{
				delta[i][j] = 0.f;
			}
		}
	}
	
	/**
	 * 获得第一个访问节点
	 * @return 获得第一个节点
	 */
	public Node getFirstNode()
	{
		return visitedNodes.get(0);
	}

	public List<Node> getVisitedNodes()
	{
		return visitedNodes;
	}
		
	protected void addVisitedNode(Node node)
	{
		visitedNodes.add(node);
	}
	
	public Node getNode(int index)
	{
		return visitedNodes.get(index);
	}
	
	protected void addSumLen(double len)
	{
		sumLen += len;
	}
	
	public void addVisitedNode(Node node, double len)
	{
		visitedNodes.add(node);
		sumLen += len;		
	}
	
	/**
	 * 选择下一个节点
	 * @return 选择的节点
	 */
	public Node selectNextNode(float[][] pheromone)
	{
		int nodeSize = probNodes.size();
		if(nodeSize <= 0)
		{
			return null;
		}
		float[] p = new float[nodeSize];
		float sum = 0.0f;
		
		for (Node n : probNodes)
		{
			sum += Math.pow(pheromone[currentNode.getIndex()][n.getIndex()], alpha) * 
					Math.pow(1.0/graph.getDistance(currentNode.getIndex(), n.getIndex()), beta);
		}
		
		for (int i = 0; i < nodeSize; i++)
		{
			boolean flag = false;
			for (Node n : probNodes)
			{
				if(i == n.getIndex())
				{
					p[i] = (float) (Math.pow(pheromone[currentNode.getIndex()][i], alpha) * 
							Math.pow(1.0/graph.getDistance(currentNode.getIndex(), n.getIndex()), beta))/sum;
					flag = true;
					break;
				}
			}
			
			if (flag == false) 
			{
				p[i] = 0.f;
			}
		}
		
		//轮盘赌选择下一个城市
		Random random = new Random(System.currentTimeMillis());
		float selectP = random.nextFloat();
		int selectNodeIndex = 0;
		float sum1 = 0.f;
		
		for (int i = 0; i < nodeSize; i++) 
		{
			sum1 += p[i];
			if (sum1 >= selectP)
			{
				selectNodeIndex = i;
				break;
			}
		}
		
		Node node = probNodes.get(selectNodeIndex);
		currentNode = node;
		probNodes.remove(node);
		visitedNodes.add(node);
		
		return currentNode;
	}
	
	/**
	 * 设置起始点的节点
	 * @param node 节点
	 */
	public void addStartNode(Node node)
	{
		init();
		currentNode = node;
		visitedNodes.add(node);
		probNodes.addAll(graph.getNodes());
		probNodes.remove(node);
	}
	
	/**
	 * 计算旅行长度
	 * @return 旅行的总长度
	 */
	private float calculateTourLength()
	{
		float len = 0.0f;
		int nodeSize = graph.nodeSize();
		Node n1, n2;
		n1 = visitedNodes.get(0);
		
		for (int i = 0; i < nodeSize - 1; i ++)
		{
			n2 = visitedNodes.get(i + 1);
			len += graph.getDistance(n1.getIndex(), n2.getIndex());
			n1 = n2;
		}
		return len;
	}

	public Graph getGraph()
	{
		return graph;
	}

	public void setGraph(Graph graph)
	{
		this.graph = graph;
	}

	public float[][] getDelta()
	{
		return delta;
	}

	public void setDelta(float[][] delta)
	{
		this.delta = delta;
	}
	
	public float getSumLen()
	{
		sumLen = calculateTourLength();
		return sumLen;
	}
}
