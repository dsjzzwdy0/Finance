package com.loris.soccer.analysis.element;

import com.loris.base.data.Region;
import com.loris.soccer.bean.item.CorpOpVar;
import com.loris.soccer.bean.item.ScoreItem;
import com.loris.soccer.bean.stat.CorpStatItem;
import com.loris.soccer.bean.table.Corporate;
import com.loris.soccer.bean.table.Op;

/**
 * CorpStatElement.
 * 
 * @author jiean
 *
 */
public class CorpRegionStatElement
{
	Corporate corp;
	int matchNum = 0;
	float min;
	float max;
	CorpOpVar baseVar = null;
	CorpOpVar winVar = null;
	CorpOpVar drawVar = null;
	CorpOpVar loseVar = null;
	
	/**
	 * Create a new CorpRegionStatElement width compute method.
	 * @param corp
	 * @param region
	 */
	public CorpRegionStatElement(Corporate corp, Region<Float> region)
	{
		this.corp = corp;
		this.min = region.getMin();
		this.max = region.getMax();
		baseVar = new ComputeCorpOpVar();
		winVar = new ComputeCorpOpVar();
		drawVar = new ComputeCorpOpVar();
		loseVar = new ComputeCorpOpVar();
	}
	
	public CorpRegionStatElement(CorpStatItem item)
	{
		corp = new Corporate();
		corp.setGid(item.getGid());
		corp.setName(item.getName());
		min = item.getOddsmin();
		max = item.getOddsmax();
		this.baseVar = item.getBaseOpVar();
		this.winVar = item.getWinOpVar();
		this.drawVar = item.getDrawOpVar();
		this.loseVar = item.getLoseOpVar();
		this.matchNum = baseVar.getNum();
	}
	
	/**
	 * 是否包含该区间的值
	 * @param odds
	 * @return
	 */
	public boolean contains(float odds)
	{
		return min <= odds && odds <= max;
	}

	/*
	 * Deviation deviation = new Deviation(); Deviation firstDeviation = new
	 * Deviation();
	 * 
	 * ResultStat winResult = new ResultStat(3); ResultStat drawResult = new
	 * ResultStat(1); ResultStat loseResult = new ResultStat(0);
	 */

	/**
	 * 加入一个数据值，计算方差值所用
	 * @param item
	 * @param op
	 * @param avgOp
	 */
	public void addVarValue(ScoreItem item, Op op, Op avgOp)
	{
		float winDiff;
		float drawDiff;
		float loseDiff;
		float fwinDiff;
		float fdrawDiff;
		float floseDiff;

		fwinDiff = op.getFirstwinodds() - avgOp.getFirstwinodds();
		fdrawDiff = op.getFirstdrawodds() - avgOp.getFirstdrawodds();
		floseDiff = op.getFirstloseodds() - avgOp.getFirstloseodds();

		winDiff = op.getWinodds() - avgOp.getWinodds();
		drawDiff = op.getDrawodds() - avgOp.getDrawodds();
		loseDiff = op.getLoseodds() - avgOp.getLoseodds();

		fwinDiff = fwinDiff - baseVar.getFirstwindiff();
		fdrawDiff = fdrawDiff - baseVar.getFirstdrawdiff();
		floseDiff = floseDiff - baseVar.getFirstlosediff();
		winDiff = winDiff - baseVar.getWindiff();
		drawDiff = drawDiff - baseVar.getDrawdiff();
		loseDiff = loseDiff - baseVar.getLosediff();

		((ComputeCorpOpVar)baseVar).add(0, 0, 0, fwinDiff * fwinDiff, fdrawDiff * fdrawDiff, floseDiff * floseDiff, 0, 0, 0, winDiff * winDiff,
				drawDiff * drawDiff, loseDiff * loseDiff);
		int result = item.getResult();
		ComputeCorpOpVar var;
		switch (result) {
		case 3:
			var = (ComputeCorpOpVar)winVar;
			break;
		case 1:
			var = (ComputeCorpOpVar)drawVar;
			break;
		default:
			var = (ComputeCorpOpVar)loseVar;
			break;
		}

		var.add(0, 0, 0, fwinDiff * fwinDiff, fdrawDiff * fdrawDiff, floseDiff * floseDiff, 0, 0, 0, winDiff * winDiff,
				drawDiff * drawDiff, loseDiff * loseDiff);
	}

	/**
	 * 添加一个数据记录，计算平均值所有
	 * 
	 * @param match
	 * @param op
	 * @param avgOp
	 */
	public void addMeanValue(ScoreItem item, Op op, Op avgOp)
	{
		float winDiff;
		float drawDiff;
		float loseDiff;
		float fwinDiff;
		float fdrawDiff;
		float floseDiff;

		fwinDiff = op.getFirstwinodds() - avgOp.getFirstwinodds();
		fdrawDiff = op.getFirstdrawodds() - avgOp.getFirstdrawodds();
		floseDiff = op.getFirstloseodds() - avgOp.getFirstloseodds();

		winDiff = op.getWinodds() - avgOp.getWinodds();
		drawDiff = op.getDrawodds() - avgOp.getDrawodds();
		loseDiff = op.getLoseodds() - avgOp.getLoseodds();

		((ComputeCorpOpVar)baseVar).add(fwinDiff, fdrawDiff, floseDiff, 0, 0, 0, winDiff, drawDiff, loseDiff, 0, 0, 0);
		((ComputeCorpOpVar)baseVar).addNum();

		int result = item.getResult();
		ComputeCorpOpVar var;
		switch (result) {
		case 3:
			var = (ComputeCorpOpVar)winVar;
			break;
		case 1:
			var = (ComputeCorpOpVar)drawVar;
			break;
		default:
			var = (ComputeCorpOpVar)loseVar;
			break;
		}

		var.add(fwinDiff, fdrawDiff, floseDiff, 0, 0, 0, winDiff, drawDiff, loseDiff, 0, 0, 0);
		var.addNum();

		matchNum++;
	}

	public void computeMean()
	{
		((ComputeCorpOpVar)baseVar).computeMeanValue();
		((ComputeCorpOpVar)winVar).computeMeanValue();
		((ComputeCorpOpVar)drawVar).computeMeanValue();
		((ComputeCorpOpVar)loseVar).computeMeanValue();
	}

	public void computeStdErr()
	{
		((ComputeCorpOpVar)baseVar).computeStdErr();
		((ComputeCorpOpVar)winVar).computeStdErr();
		((ComputeCorpOpVar)drawVar).computeStdErr();
		((ComputeCorpOpVar)loseVar).computeStdErr();
	}

	public Corporate getCorp()
	{
		return corp;
	}

	public void setCorp(Corporate corp)
	{
		this.corp = corp;
	}

	public CorpStatItem createCorpStatItem()
	{
		CorpStatItem item = new CorpStatItem(corp.getGid(), corp.getName());
		item.setOddsmin(min);
		item.setOddsmax(max);
		item.setBaseOpVar(baseVar);
		item.setWinOpVar(winVar);
		item.setDrawOpVar(drawVar);
		item.setLoseOpVar(loseVar);
		return item;
	}

	public float getMin()
	{
		return min;
	}

	public void setMin(float min)
	{
		this.min = min;
	}

	public float getMax()
	{
		return max;
	}

	public void setMax(float max)
	{
		this.max = max;
	}

	public int getMatchNum()
	{
		return matchNum;
	}

	public void setMatchNum(int matchNum)
	{
		this.matchNum = matchNum;
	}

	public CorpOpVar getBaseVar()
	{
		return baseVar;
	}

	public void setBaseVar(CorpOpVar baseVar)
	{
		this.baseVar = baseVar;
	}

	public CorpOpVar getWinVar()
	{
		return winVar;
	}

	public void setWinVar(CorpOpVar winVar)
	{
		this.winVar = winVar;
	}

	public CorpOpVar getDrawVar()
	{
		return drawVar;
	}

	public void setDrawVar(CorpOpVar drawVar)
	{
		this.drawVar = drawVar;
	}

	public CorpOpVar getLoseVar()
	{
		return loseVar;
	}

	public void setLoseVar(CorpOpVar loseVar)
	{
		this.loseVar = loseVar;
	}

	@Override
	public String toString()
	{
		return "CorpRegionStatElement [corp=" + corp + ", matchNum=" + matchNum + ", min=" + min + ", max=" + max + "]";
	}
}
