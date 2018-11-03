package com.loris.soccer.analysis.util;

import com.loris.base.analysis.stat.Possion;

/**
 * 盘口数据计算器
 * @author deng
 *
 */
public class HandicapUtil
{
	public static final int POSSION_K = 7;
	
	/**
	 * 计算各种结果的比率
	 * @param lamda1 期望值1
	 * @param lamda2 期望值2
	 * @param k K值
	 * @return 结果
	 */
	public static double[] computeOddsProb(double lamda1, double lamda2)
	{
		return computeOddsProb(lamda1, lamda2, POSSION_K);
	}
	
	/**
	 * 计算各种结果的比率
	 * @param lamda1 期望值1
	 * @param lamda2 期望值2
	 * @param k K值
	 * @return 结果
	 */
	public static double[] computeOddsProb(double lamda1, double lamda2, int k)
	{
		double[][] p = computeProb(lamda1, lamda2, k);
		double[] prob = new double[3];
		for(int i = 0; i < k; i ++)
		{
			for(int j = 0; j < k; j ++)
			{
				if(i > j)
    			{
    				prob[0] += p[i][j];
    			}
    			if(i == j)
    			{
    				prob[1] += p[i][j];
    			}
    			if(i < j)
    			{
    				prob[2] += p[i][j];
    			}
			}
		}
		return prob;
	}
	
	/**
	 * 计算各种可能的概率值
	 * @param lamda1 期望值1
	 * @param lamda2 期望值2
	 * @param k K值
	 * @return 概率分布
	 */
	public static double[][] computeProb(double lamda1, double lamda2, int k)
	{
		double[][] p = new double[k][k];
		
		double[] p1 = getPossoinProb(lamda1, k);
		double[] p2 = getPossoinProb(lamda2, k);
		for(int i = 0; i < k; i ++)
    	{
    		for(int j = 0; j < k; j ++)
    		{
    			p[i][j] = p1[i] * p2[j];
    		}
    	}
		return p;
	}
	
	/**
	 * 计算泊松分布概率数据
	 * @param lamda 期望值
	 * @return 概率分布数据
	 */
	public static double[] getPossoinProb(double lamda)
	{
		return getPossoinProb(lamda, POSSION_K);
	}
	
	/**
	 * 计算泊松分布概率数据
	 * @param lamda 期望值
	 * @param k 最大个数
	 * @return 概率分布数据
	 */
	public static double[] getPossoinProb(double lamda, int k)
	{
		double[] p = new double[k];
		double total = 0.0;
		for (int i = 0; i < k - 1; i ++)
		{
			p[i] = Possion.computePossion(lamda, i);
			total += p[i];
		}
		p[k - 1] = 1.0 - total;		
		return p;
	}
}
