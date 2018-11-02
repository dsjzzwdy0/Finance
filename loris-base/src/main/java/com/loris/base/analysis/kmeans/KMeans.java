package com.loris.base.analysis.kmeans;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.analysis.base.Point;

public class KMeans<T extends Point>
{
	protected static Logger logger = Logger.getLogger(KMeans.class);

	/** The threshold value. */
	public static double DEFAULT_THRESHOLD_ERROR = 0.001;
	public static int DEFAULT_MAX_TIMES = 100000;

	/** The cluster center point number. */
	private int clusterNum;

	/** 每一个簇的数据组. */
	private List<ArrayList<Point>> cluster;

	/** 中心点 */
	private Point[] center;

	/** 原始数据组 */
	private List<Point> dataset;

	/** The */
	private Class<T> clazz;

	/** The threshold value. */
	private double threshold;

	/** The maxTimes. */
	private int maxTimes;

	/*
	 * 构造函数
	 */
	public KMeans(int clusterNum, Class<T> clazz)
	{
		this.clazz = clazz;
		this.clusterNum = clusterNum;
		cluster = new ArrayList<ArrayList<Point>>();
		this.threshold = DEFAULT_THRESHOLD_ERROR;
		maxTimes = DEFAULT_MAX_TIMES;
	}

	/**
	 * Set the dataset
	 * 
	 * @param dataset
	 */
	public void setDateset(List<Point> dataset)
	{
		this.dataset = dataset;
	}

	/**
	 * Set the init center.
	 * 
	 * @param points
	 */
	public void setInitCenter(Point[] points)
	{
		this.center = points;
	}

	/**
	 * Get the Center point array.
	 * 
	 * @return
	 */
	public Point[] getCenter()
	{
		return center;
	}

	/*
	 * 主执行方法
	 */
	public void computeKmeans()
	{
		int num = 1;
		Point[] lastCenter;
		for (int i = 0; i < maxTimes; i++)
		{
			initCluster();
			allocateCluster();
			lastCenter = center;
			setNewCenter();
			num ++;
			if(!isCenterChanged(center, lastCenter))
			{
				break;
			}

		}
		logger.info("Total process times is " + num);
		for (int j = 0; j < clusterNum; j++)
		{
			logger.info(j + " point[" + center[j] + ", " + cluster.get(j).size() + "]");
		}
	}

	/*
	 * 获取簇
	 */
	public List<ArrayList<Point>> getCluster()
	{
		return cluster;
	}

	/*
	 * 判断簇中心点是否改变 作为算法结束条件
	 */
	private boolean isCenterChanged(Point[] center, Point[] lastCenter)
	{
		int size = center.length;
		for (int i = 0; i < size; i++)
		{
			if (center[i].getDistance(lastCenter[i]) > threshold)
			{
				return true;
			}
		}
		return false;
	}

	/*
	 * 初始化簇容器
	 */
	private void initCluster()
	{
		cluster.clear();
		for (int i = 0; i < clusterNum; i++)
		{
			cluster.add(new ArrayList<Point>());
		}
	}

	/*
	 * 获取这个节点属于哪个簇
	 */
	private int getClusterIndex(double[] distance)
	{
		double minDistance = distance[0];
		int clusterIndex = 0;
		for (int i = 0; i < distance.length; i++)
		{
			if (distance[i] < minDistance)
			{
				minDistance = distance[i];
				clusterIndex = i;
			}
		}

		return clusterIndex;
	}

	/*
	 * 分配簇
	 */
	private void allocateCluster()
	{
		double[] distance = new double[clusterNum];
		for (Point p : dataset)
		{
			for (int j = 0; j < clusterNum; j++)
			{
				distance[j] = p.getDistance(center[j]);
			}

			int clusterIndex = this.getClusterIndex(distance);

			/*
			 * 如果用ArrayList<double[][]>来描述簇也是可行的 但是在这里会很不好处理 不可能为每一个簇都保存一个索引值
			 */
			cluster.get(clusterIndex).add(p);
		}
	}

	/*
	 * 设置新的簇中心
	 */
	private void setNewCenter()
	{
		center = new Point[clusterNum];
		for (int i = 0; i < center.length; i++)
		{
			// center[i] += cluster.get(i).doubleValue()/cluster.size();
			center[i] = createNewPoint();
			ArrayList<Point> points = cluster.get(i);
			for (Point point : points)
			{
				center[i].addPoint(point);
			}
			center[i].multiply(1 / (double) points.size());
			// logger.info(i + ": " + center[i] + " " + points.size());
		}
	}

	/**
	 * Create a new instance of Point
	 * 
	 * @return
	 */
	protected Point createNewPoint()
	{
		try
		{
			return (Point) clazz.newInstance();
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public double getThreshold()
	{
		return threshold;
	}

	public void setThreshold(double threshold)
	{
		this.threshold = threshold;
	}

	public int getMaxTimes()
	{
		return maxTimes;
	}

	public void setMaxTimes(int maxTimes)
	{
		this.maxTimes = maxTimes;
	}
}
