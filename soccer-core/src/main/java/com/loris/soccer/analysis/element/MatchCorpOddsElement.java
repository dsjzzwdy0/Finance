package com.loris.soccer.analysis.element;

import java.util.ArrayList;
import java.util.List;

import com.loris.soccer.bean.data.table.Corporate;
import com.loris.soccer.bean.item.MatchInfoItem;
import com.loris.soccer.bean.item.OpItem;
import com.loris.soccer.bean.item.YpItem;

/**
 * 一场比赛同一公司开出的欧赔与亚盘数据记录，该
 * @author jiean
 *
 */
public class MatchCorpOddsElement extends MatchInfoItem
{
	/***/
	private static final long serialVersionUID = 1L;
	
	private Corporate opCorp;
	private Corporate ypCorp;
	
	private List<OpItem> opItems;
	private List<YpItem> ypItems;
	
	/**
	 * Create a new instance of MatchCorpOddsElement
	 */
	public MatchCorpOddsElement()
	{
		opItems = new ArrayList<>();
		ypItems = new ArrayList<>();
	}
	
	/**
	 * Create a new instance of MatchCorpOddsElement
	 * @param match Match Info
	 */
	public MatchCorpOddsElement(MatchInfoItem match)
	{
		this();
		setMatchInfoItem(match);		
	}
	
	/**
	 * Add the OpItem
	 * @param op OpItem
	 */
	public void addOpItem(OpItem op)
	{
		opItems.add(op);
	}
	
	/**
	 * 加入欧赔数据列表
	 * @param ops 欧赔数据列表
	 */
	public void addOpList(List<? extends OpItem> ops)
	{
		for (OpItem opItem : ops)
		{
			OpItem item = (OpItem)opItem.clone();
			opItems.add(item);
		}
	}
	
	/**
	 * Add the YpItem
	 * @param yp YpItem
	 */
	public void addYpItem(YpItem yp)
	{
		ypItems.add(yp);
	}
	
	/**
	 * 加入亚盘列表
	 * @param yps 亚盘数据
	 */
	public void addYpList(List<? extends YpItem> yps)
	{
		for (YpItem ypItem : yps)
		{
			YpItem item = (YpItem)ypItem.clone();
			ypItems.add(item);
		}
	}
	
	public List<OpItem> getOpItems()
	{
		return opItems;
	}

	public List<YpItem> getYpItems()
	{
		return ypItems;
	}

	public Corporate getOpCorp()
	{
		return opCorp;
	}

	public void setOpCorp(Corporate opCorp)
	{
		this.opCorp = opCorp;
	}

	public Corporate getYpCorp()
	{
		return ypCorp;
	}

	public void setYpCorp(Corporate ypCorp)
	{
		this.ypCorp = ypCorp;
	}
}
