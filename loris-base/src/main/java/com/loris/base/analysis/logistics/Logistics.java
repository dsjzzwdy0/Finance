package com.loris.base.analysis.logistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

/**
 * 
 * @author deng
 *
 */
public class Logistics
{
	private static Logger logger = Logger.getLogger(Logistics.class);

	/** 学习速率 */
	protected double rate;

	/** 随机数 */
	protected Random random = new Random();

	/**
	 * 分类测试
	 * 
	 * @param attributes
	 * @param trainWeights
	 * @return
	 */
	public double classifyVector(List<Double> attributes, List<Double> trainWeights)
	{
		double prob = sigmoid(multipleVec(attributes, trainWeights));
		if (prob > 0.5)
		{
			return 1.0;
		}
		return 0.0;
	}

	/**
	 * 计算权重值
	 * 
	 * @param attributes
	 * @param weights
	 * @return
	 */
	public double compute(List<Double> attributes, List<Double> weights)
	{
		double prob = sigmoid(multipleVec(attributes, weights));
		return prob;
	}

	/**
	 * @paramdataSet
	 * @paramclassLabels
	 * @paramnumberIter
	 * @return
	 * @author haolidong
	 * @Description: [改进的随机梯度上升算法]
	 */
	public ArrayList<Double> gradAscent1(List<Element> dataSet, int numberIter)
	{
		int m = dataSet.size();
		int n = dataSet.get(0).attributeSize();
		
		double alpha = 0.0;
		int randIndex = 0;
		
		ArrayList<Double> weights = new ArrayList<Double>();
		//ArrayList<Double> weightstmp = new ArrayList<Double>();
		double h;
		ArrayList<Integer> dataIndex = new ArrayList<Integer>();
		ArrayList<Double> dataMatrixMulweights = new ArrayList<Double>();
		
		for (int i = 0; i < n; i++)
		{
			weights.add(1.0);
			//weightstmp.add(1.0);
		}
		dataMatrixMulweights.add(0.0);
		double error = 0.0;
		for (int j = 0; j < numberIter; j++)
		{
			// 产生0到99的数组
			for (int p = 0; p < m; p++)
			{
				dataIndex.add(p);
			}
			// 进行每一次的训练
			for (int i = 0; i < m; i++)
			{
				alpha = 4 / (1.0 + i + j) + 0.0001;
				randIndex = (int) (Math.random() * dataIndex.size());
				dataIndex.remove(randIndex);
				double temp = 0.0;
				for (int k = 0; k < n; k++)
				{
					temp = temp + dataSet.get(randIndex).getAttributes().get(k) * weights.get(k);
				}
				dataMatrixMulweights.set(0, temp);
				h = sigmoid(temp);
				error = dataSet.get(randIndex).getLabel() - h;
				double tempweight = 0.0;
				for (int p = 0; p < n; p++)
				{
					tempweight = alpha * dataSet.get(randIndex).getAttributes().get(p) * error;
					weights.set(p, weights.get(p) + tempweight);
				}
			}
		}
		return weights;
	}

	/**
	 * 改进的随机梯度上升算法
	 * 
	 * @param trainDataSet
	 *            训练集
	 * @param numIter
	 *            迭代次数
	 * @return 权重向量
	 */
	public List<Double> stocGradAscent(List<? extends Element> trainDataSet, int numIter)
	{
		// 初始化回归系数
		List<Double> weights = new ArrayList<Double>();
		int attrSize = trainDataSet.get(0).getAttributes().size();
		initWeights(weights, attrSize);

		logger.info("Weights: " + Arrays.toString(weights.toArray()));

		int elemSize = trainDataSet.size();
		List<Element> datasets = new ArrayList<>();

		for (int i = 0; i < numIter; i++)
		{
			datasets.clear();
			datasets.addAll(trainDataSet);

			for (int j = 0; j < elemSize; j++)
			{
				double alpha = 4.0 / (1.0 + i + j) + 0.01;
				int randIndex = random.nextInt(datasets.size());
				double h = sigmoid(multipleVec(datasets.get(randIndex).getAttributes(), weights));
				double error = datasets.get(randIndex).getLabel() - h;
				weights = addVec(weights, alpha, error, datasets.get(randIndex).getAttributes());

				datasets.remove(randIndex);
			}

			logger.info((i + 1) + " times, Weights: " + Arrays.toString(weights.toArray()));
		}

		return weights;
	}

	/**
	 * 初始化权重值
	 * 
	 * @param weights
	 */
	protected void initWeights(List<Double> weights, int weightSize)
	{
		weights.clear();
		for (int i = 0; i < weightSize; i++)
		{
			weights.add(random.nextDouble());
		}
	}

	/**
	 * 向量加法计算
	 * 
	 * @param weights
	 * @param alpha
	 * @param error
	 * @param attributes
	 * @return
	 */
	protected List<Double> addVec(List<Double> weights, double alpha, double error, List<Double> attributes)
	{
		List<Double> list = new ArrayList<Double>();

		for (int i = 0; i < weights.size(); i++)
		{
			list.add(weights.get(i) + alpha * error * attributes.get(i));
		}
		return list;
	}

	/**
	 * 计算向量的内积
	 * 
	 * @param attributes
	 * @param weights
	 * @return
	 */
	protected double multipleVec(List<Double> attributes, List<Double> weights)
	{
		double sum = 0.0;
		for (int i = 0; i < attributes.size(); i++)
		{
			sum += attributes.get(i) * weights.get(i);
		}
		return sum;
	}

	/**
	 * @param x
	 * @return
	 */
	protected double sigmoid(double x)
	{
		return 1.0 / (1 + Math.exp(-x));
	}
}
