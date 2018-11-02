package com.loris.learning.ant;

import java.util.Random;

import org.apache.log4j.Logger;

import com.loris.learning.graph.Edge;
import com.loris.learning.graph.Graph;
import com.loris.learning.graph.Node;

public class AntCreator
{
	private static Logger logger = Logger.getLogger(AntCreator.class);
	
	//protected int nodeNum;
	protected Edge[] edges;
	protected Node[] nodes;
	
	protected Graph graph;
	
	//信息素
	protected float pheromones[][]; 		//信息素
	
	/** 蚂蚁对象 */
	protected Ant[] ants;
	
	/** 蚂蚁数量 */
	protected int antNum;
	
	/** 节点数量 */
	protected int nodeSize;
	
	//三个参数
	protected double alpha;
	protected double beta;
	protected double rho;
	
	/** 最大迭代代数 */
	protected int maxGernerator;
	
	/** 最佳路径 */
	protected Node[] bestTour;
	
	/** 最佳路径长度 */
	protected double bestLength;
	
	/** 随机数据 */
	Random random = new Random();
	
	/**
	 * Create a new instance of AntCreator.
	 */
	public AntCreator()
	{
	}
	
	/**
	 * Create a new instance of AntCreator.
	 * @param antNum
	 * @param nodeSize
	 * @param maxGen
	 * @param alpha
	 * @param beta
	 * @param rho
	 */
	public AntCreator(int antNum, int nodeSize, int maxGen, double alpha, double beta, double rho)
	{
		this.antNum = antNum;
		this.nodeSize = nodeSize;
		this.maxGernerator = maxGen;
		this.alpha = alpha;
		this.beta = beta;
		this.rho = rho;
	}
	
	/**
	 * 初始化
	 */
	public void init()
	{
		int size = nodeSize;
		pheromones = new float[size][size];
		for(int i = 0; i < size; i ++)
		{
			for(int j = 0; j < size; j ++)
			{
				pheromones[i][j] = 0.1f;
			}
		}
		
		bestLength = Integer.MAX_VALUE;
		bestTour = new Node[size + 1];
		
		ants = new Ant[antNum];
		for(int i = 0; i < antNum; i ++)
		{
			ants[i] = new Ant(graph);
		}
	}
	
	/**
	 * 重置蚂蚁 
	 */
	protected void resetAnt()
	{				
		Node node;
		
		//重置蚂蚁
		for(int i = 0; i < antNum; i ++)
		{
			ants[i].setParams((float)alpha, (float)beta);			
			int index = random.nextInt(nodeSize);			
			node = graph.getNodeByIndex(index);
			ants[i].addStartNode(node);
		}
	}
	
	
	public void solve()
	{
		for(int g = 0; g < maxGernerator; g ++)
		{
			//重置蚂蚁
			resetAnt();
			
			for (int i = 0; i < antNum; i++) 
			{
				for(int j = 0; j < nodeSize; j ++)
				{
					ants[i].selectNextNode(pheromones);
				}
				
				ants[i].addVisitedNode(ants[i].getFirstNode());
				float len = ants[i].getSumLen();
				if(len < bestLength)
				{
					bestLength = len;
					bestTour = ants[i].getVisitedNodes().toArray(bestTour);
				}
				
				//计算值
				for (int j = 0; j < nodeSize; j++)
				{
					ants[i].getDelta()[ants[i].getNode(j).getIndex()][ants[i].getNode(j+1).getIndex()] = 
							(float) (1./len);
					ants[i].getDelta()[ants[i].getNode(j + 1).getIndex()][ants[i].getNode(j).getIndex()] = 
							(float) (1./len);
				}
				
				//logger.info(len);
			}
			
			logger.info("Gen" + (g + 1) + ": " + getBestTourInfo());
			
			//更新信息素
			updatePheromone();
		}
	}
	
	public String getBestTourInfo()
	{
		String string = "";
		for (Node node : bestTour)
		{
			string += node.getName() + " -> ";
		}
		
		string += ", BestLength = " + bestLength;
		
		return string;
	}
	
	/**
	 * 更新信息素
	 */
	private void updatePheromone()
	{
		//信息素挥发
		for(int i = 0; i < nodeSize; i ++)
			for(int j = 0; j < nodeSize; j ++)
				pheromones[i][j] = (float)(pheromones[i][j]*(1.0 - rho));
		
		//信息素更新
		for(int i = 0; i < nodeSize; i ++)
			for(int j = 0; j < nodeSize; j ++)
				for (int k = 0; k < antNum; k++)
					pheromones[i][j] += ants[k].getDelta()[i][j];
	}
	
	public Graph getGraph()
	{
		return graph;
	}
	public void setGraph(Graph graph)
	{
		this.graph = graph;
	}
	public float[][] getPheromones()
	{
		return pheromones;
	}
	public void setPheromones(float[][] pheromones)
	{
		this.pheromones = pheromones;
	}
	public Ant[] getAnts()
	{
		return ants;
	}
	public void setAnts(Ant[] ants)
	{
		this.ants = ants;
	}
	public int getAntNum()
	{
		return antNum;
	}
	public void setAntNum(int antNum)
	{
		this.antNum = antNum;
	}
	public int getNodeSize()
	{
		return nodeSize;
	}
	public void setNodeSize(int nodeSize)
	{
		this.nodeSize = nodeSize;
	}
	public double getAlpha()
	{
		return alpha;
	}
	public void setAlpha(double alpha)
	{
		this.alpha = alpha;
	}
	public double getBeta()
	{
		return beta;
	}
	public void setBeta(double beta)
	{
		this.beta = beta;
	}
	public double getRho()
	{
		return rho;
	}
	public void setRho(double rho)
	{
		this.rho = rho;
	}

	public int getMaxGernerator()
	{
		return maxGernerator;
	}

	public void setMaxGernerator(int maxGernerator)
	{
		this.maxGernerator = maxGernerator;
	}
	
	public void setPheromone(int row, int col, float pheromone)
	{
		pheromones[row][col] = pheromone;
	}
	
	public float getPheromone(int row, int col)
	{
		return pheromones[row][col];
	}
}
