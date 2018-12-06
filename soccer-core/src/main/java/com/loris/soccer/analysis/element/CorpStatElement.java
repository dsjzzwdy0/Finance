package com.loris.soccer.analysis.element;

import java.util.ArrayList;
import java.util.List;

import com.loris.base.data.Region;
import com.loris.soccer.bean.data.table.Corporate;
import com.loris.soccer.bean.data.table.Op;
import com.loris.soccer.bean.item.CorpStatItem;
import com.loris.soccer.bean.item.ScoreItem;

public class CorpStatElement extends ArrayList<CorpRegionStatElement>
{
	/***/
	private static final long serialVersionUID = 1L;

	Corporate corp;

	/**
	 * Create a new instance of CorpStatElements.
	 */
	public CorpStatElement()
	{
	}

	public CorpStatElement(Corporate corp, List<Region<Float>> regions)
	{
		this.corp = corp;
		this.createCorpStatElements(regions);
	}

	public Corporate getCorp()
	{
		return corp;
	}

	public void setCorp(Corporate corp)
	{
		this.corp = corp;
	}

	/**
	 * 加入博彩公司的统计元素
	 * 
	 * @param regionElement
	 */
	public void addCorpRegionStatElement(CorpRegionStatElement regionElement)
	{
		add(regionElement);
	}

	/**
	 * 加入平均值
	 * 
	 * @param item
	 * @param op
	 * @param avgOp
	 */
	public void addMeanValue(ScoreItem item, Op op, Op avgOp)
	{
		for (CorpRegionStatElement element : this)
		{
			if (element.contains(op.getWinodds()))
			{
				element.addMeanValue(item, op, avgOp);
			}
		}
	}

	/**
	 * 加入平均值
	 * 
	 * @param item
	 * @param op
	 * @param avgOp
	 */
	public void addVarValue(ScoreItem item, Op op, Op avgOp)
	{
		for (CorpRegionStatElement element : this)
		{
			if (element.contains(op.getWinodds()))
			{
				element.addVarValue(item, op, avgOp);
			}
		}
	}

	public void computeMean()
	{
		for (CorpRegionStatElement element : this)
		{
			element.computeMean();
		}
	}

	public void computeStdErr()
	{
		for (CorpRegionStatElement element : this)
		{
			element.computeStdErr();
		}
	}

	public void createCorpStatElements(List<Region<Float>> regions)
	{
		for (Region<Float> region : regions)
		{
			CorpRegionStatElement element = new CorpRegionStatElement(corp, region);
			this.add(element);
		}
	}

	/**
	 * 获得所有的博彩公司元素
	 * 
	 * @return
	 */
	public List<CorpStatItem> getAllCorpStatItems()
	{
		List<CorpStatItem> items = new ArrayList<>();
		for (CorpRegionStatElement element : this)
		{
			items.add(element.createCorpStatItem());
		}
		return items;
	}
}
