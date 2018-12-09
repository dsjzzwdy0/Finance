package com.loris.soccer.analysis.element;

import java.util.ArrayList;

import com.loris.soccer.bean.stat.MatchCorpProb;

public class MatchResult extends ArrayList<MatchCorpProb>
{
	/***/
	private static final long serialVersionUID = 1L;
	
	public static double SIGNIFICANCE_THRESHOLD = 0.03;

	protected String mid;

	public MatchResult()
	{
	}

	public MatchResult(String mid)
	{
		this.mid = mid;
	}

	/**
	 * 获得统计数据
	 * 
	 * @return
	 */
	public int[] getStatNum()
	{
		int[] nums = new int[3];
		int r;
		for (MatchCorpProb prob : this)
		{
			r = prob.getResult();
			if (r == 3)
			{
				nums[0]++;
			}
			else if (r == 0)
			{
				nums[1]++;
			}
			else
			{
				nums[2]++;
			}
		}
		return nums;
	}

	/**
	 * 计算加权数据
	 * 
	 * @return
	 */
	public double[] getWeightStatProb()
	{
		double[] probs = new double[3];
		double weight = 0;
		for (MatchCorpProb prob : this)
		{
			probs[0] += prob.getWinprob() * prob.getWeight();
			probs[1] += prob.getDrawprob() * prob.getWeight();
			probs[2] += prob.getLoseprob() * prob.getWeight();
			weight += prob.getWeight();
		}
		for (int i = 0; i < 3; i++)
		{
			probs[i] /= weight;
		}
		return probs;
	}
	
	/**
	 * 计算加权数据
	 * 
	 * @return
	 */
	public double[] getAverageStatProb()
	{
		double[] probs = new double[3];
		double weight = 0;
		for (MatchCorpProb prob : this)
		{
			probs[0] += prob.getWinprob();
			probs[1] += prob.getDrawprob();
			probs[2] += prob.getLoseprob();
			weight ++;
		}
		for (int i = 0; i < 3; i++)
		{
			probs[i] /= weight;
		}
		return probs;
	}
	
	/**
	 * 是否显著
	 * @return
	 */
	public boolean isSignificance()
	{
		for (MatchCorpProb prob : this)
		{
			if(prob.isSignificance(SIGNIFICANCE_THRESHOLD))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 统计数据
	 * 
	 * @param result
	 * @return
	 */
	public int getNum(int result)
	{
		int num = 0;
		for (MatchCorpProb prob : this)
		{
			if (prob.getResult() == result)
			{
				num++;
			}
		}
		return num;
	}

	public String getMid()
	{
		return mid;
	}

	public void setMid(String mid)
	{
		this.mid = mid;
	}
}
