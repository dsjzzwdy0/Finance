package com.loris.soccer.analysis.predict;

import java.util.ArrayList;
import java.util.List;

import com.loris.soccer.analysis.element.CorpRegionStatElement;
import com.loris.soccer.analysis.element.CorpStatElement;
import com.loris.soccer.bean.data.table.Corporate;
import com.loris.soccer.bean.data.table.Op;
import com.loris.soccer.bean.item.CorpStatItem;
import com.loris.soccer.repository.SoccerManager;

public class SoccerPredict
{
	/** The SoccerManager. */
	SoccerManager soccerManager;
	
	/** CorpStatElement */
	List<CorpStatElement> elements = new ArrayList<>();
	
	/** 最小的比赛数据 */
	protected int minMatchNum = 200;
	
	/**
	 * 计算比赛的数据
	 * @param ops
	 */
	public void predict(Op avgOp, List<Op> ops)
	{
		double winProb = 0.0;
		double drawProb = 0.0;
		double loseProb = 0.0;
		
		for (Op op : ops)
		{
			CorpStatElement element = getCorpStatElement(op.getGid());
			if(element == null)
			{
				continue;
			}
			
			computeCorpProb(element, avgOp, avgOp);
		}
	}
	
	/**
	 * 计算某一公司的概率值
	 * @param element
	 * @param avgOp
	 * @param op
	 */
	protected void computeCorpProb(CorpStatElement element, Op avgOp, Op op)
	{
		
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
			CorpStatElement element =getCorpStatElement(corpStatItem.getGid());
			if(element == null)
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
		
		//清洗掉不需要的数据
		shuffle();
	}
	
	protected void shuffle()
	{
		int len = elements.size();
		for(int i = len - 1; i >= 0; i ++)
		{
			CorpStatElement element = elements.get(i);
			if(checkCorpStatElement(element))
			{
				elements.remove(i);
			}
		}
	}
	
	/**
	 * 检测数据是否符合要求
	 * @param element
	 * @return
	 */
	private boolean checkCorpStatElement(CorpStatElement element)
	{
		for (CorpRegionStatElement statElement : element)
		{
			if(statElement.getMin() < 0.5 && statElement.getMax() > 20.0)
			{
				return statElement.getMatchNum() > minMatchNum;
			}
		}
		return false;
	}
	
	/**
	 * 获得博彩公司的统计数据
	 * @param gid
	 * @return
	 */
	protected CorpStatElement getCorpStatElement(String gid)
	{
		for (CorpStatElement corpStatElement : elements)
		{
			if(gid.equals(corpStatElement.getCorp().getGid()))
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
