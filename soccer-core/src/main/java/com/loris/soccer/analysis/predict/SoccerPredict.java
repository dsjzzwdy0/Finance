package com.loris.soccer.analysis.predict;

import java.util.ArrayList;
import java.util.List;

import com.loris.soccer.analysis.element.CorpRegionStatElement;
import com.loris.soccer.analysis.element.CorpStatElement;
import com.loris.soccer.analysis.element.MatchCorpProb;
import com.loris.soccer.analysis.element.MatchResult;
import com.loris.soccer.bean.data.table.Corporate;
import com.loris.soccer.bean.data.table.Op;
import com.loris.soccer.bean.item.CorpStatItem;
import com.loris.soccer.bean.item.MatchItem;
import com.loris.soccer.bean.model.OpList;
import com.loris.soccer.bean.model.OpList.OpListType;
import com.loris.soccer.repository.SoccerManager;

public class SoccerPredict
{
	//private static Logger logger = Logger.getLogger(SoccerPredict.class);

	/** The SoccerManager. */
	SoccerManager soccerManager;

	/** CorpStatElement */
	List<CorpStatElement> elements = new ArrayList<>();

	/** 最小的比赛数据 */
	protected int minMatchNum = 200;
	
	/**
	 * 预测数据
	 * @param matchItem
	 * @return
	 */
	public MatchResult predict(MatchItem matchItem)
	{
		List<Op> ops = soccerManager.getOddsOp(matchItem.getMid());		
		OpList list = new OpList(OpListType.GidUnique);
		Op avgOp = null;
		for (Op op : ops)
		{
			if("0".equals(op.getGid()))
			{
				avgOp = op;
			}
			else
			{
				list.add(op);
			}			
		}
		
		if(avgOp == null || list.isEmpty())
		{
			return null;
		}
		
		return predict(avgOp, list);
	}

	/**
	 * 计算比赛的数据
	 * 
	 * @param ops
	 */
	public MatchResult predict(Op avgOp, List<Op> ops)
	{
		MatchResult result = new MatchResult(avgOp.getMid());
		MatchCorpProb prob = null;
		for (Op op : ops)
		{
			CorpStatElement element = getCorpStatElement(op.getGid());
			if (element == null)
			{
				continue;
			}

			// 计算数据
			prob = computeCorpProb(element, avgOp, op);
			result.add(prob);
		}
		return result;
	}

	/**
	 * 计算某一公司的概率值
	 * 
	 * @param element
	 * @param avgOp
	 * @param op
	 */
	protected MatchCorpProb computeCorpProb(CorpStatElement element, Op avgOp, Op op)
	{
		double winProb = 0.0;
		double drawProb = 0.0;
		double loseProb = 0.0;
		float[] diff = computeOpDiff(op, avgOp);
		float winodds = op.getFirstwinodds();
		int weight = 0;
		for (CorpRegionStatElement regionElement : element)
		{
			// Do something.
			if (regionElement.contains(winodds))
			{
				float[] winvars = regionElement.getWinVar().getVars();
				float[] drawvars = regionElement.getDrawVar().getVars();
				float[] losevars = regionElement.getLoseVar().getVars();

				winProb += computeRelationValue(diff, winvars);
				drawProb += computeRelationValue(diff, drawvars);
				loseProb += computeRelationValue(diff, losevars);
				
				if(weight < regionElement.getMatchNum())
				{
					weight = regionElement.getMatchNum();
				}
			}
		}
		MatchCorpProb prob = new MatchCorpProb(op.getMid());
		prob.setGid(op.getGid());
		prob.setName(op.getGname());
		prob.setProb(winProb,  drawProb,  loseProb);
		prob.setWeight(weight);
		return prob;
		//logger.info(op);
		//logger.info("WinProb: " + winProb + ", DrawProb: " + drawProb + ", LoseProb: " + loseProb);
	}

	protected float[] computeOpDiff(Op op, Op avgOp)
	{
		// 计算数据差值
		float[] diff = new float[6];
		diff[0] = op.getFirstwinodds() - avgOp.getFirstwinodds();
		diff[1] = op.getFirstdrawodds() - avgOp.getFirstdrawodds();
		diff[2] = op.getFirstloseodds() - avgOp.getFirstloseodds();
		diff[3] = op.getWinodds() - avgOp.getWinodds();
		diff[4] = op.getDrawodds() - avgOp.getDrawodds();
		diff[5] = op.getLoseodds() - avgOp.getLoseodds();
		return diff;
	}

	/**
	 * 计算相关系数
	 * 
	 * @param diffVals
	 *            与平均数的差值，这里是一个6位长度的数组
	 * @param vars
	 *            预先计算的方差值，这里是一个12位长度的数组
	 * @return
	 */
	protected double computeRelationValue(float[] diffVals, float[] vars)
	{
		double p = 0.0;
		for (int i = 0; i < diffVals.length; i++)
		{
			int st = i < 3 ? 0 : 3;
			p += diffVals[i] * vars[i + st];
		}
		return p;
	}

	/**
	 * 初始化数据预测器
	 * 
	 * @param soccerManager
	 */
	public void initialize(SoccerManager soccerManager)
	{
		this.soccerManager = soccerManager;

		List<CorpStatItem> items = soccerManager.getCorpStatItems();
		for (CorpStatItem corpStatItem : items)
		{
			CorpStatElement element = getCorpStatElement(corpStatItem.getGid());
			if (element == null)
			{
				Corporate corp = new Corporate();
				corp.setGid(corpStatItem.getGid());
				corp.setName(corpStatItem.getName());

				element = new CorpStatElement();
				element.setCorp(corp);
				elements.add(element);
			}

			element.add(new CorpRegionStatElement(corpStatItem));
		}

		// 清洗掉不需要的数据
		shuffle();
	}

	protected void shuffle()
	{
		int len = elements.size();
		for (int i = len - 1; i >= 0; i--)
		{
			CorpStatElement element = elements.get(i);
			if (!checkCorpStatElement(element))
			{
				elements.remove(i);
			}
		}
	}

	/**
	 * 检测数据是否符合要求
	 * 
	 * @param element
	 * @return
	 */
	private boolean checkCorpStatElement(CorpStatElement element)
	{
		for (CorpRegionStatElement statElement : element)
		{
			if (statElement.getMin() < 0.5 && statElement.getMax() > 20.0)
			{
				return statElement.getMatchNum() > minMatchNum;
			}
		}
		return false;
	}

	/**
	 * 获得博彩公司的统计数据
	 * 
	 * @param gid
	 * @return
	 */
	protected CorpStatElement getCorpStatElement(String gid)
	{
		for (CorpStatElement corpStatElement : elements)
		{
			if (gid.equals(corpStatElement.getCorp().getGid()))
			{
				return corpStatElement;
			}
		}

		return null;
	}

	public int getMinMatchNum()
	{
		return minMatchNum;
	}

	public void setMinMatchNum(int minMatchNum)
	{
		this.minMatchNum = minMatchNum;
	}
}
