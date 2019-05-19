package com.loris.soccer.analysis.element;

import java.util.ArrayList;
import java.util.List;

import com.loris.soccer.analysis.data.MatchOdds;
import com.loris.soccer.analysis.util.OddsUtil;
import com.loris.soccer.bean.item.IssueMatch;
import com.loris.soccer.bean.table.Op;
import com.loris.soccer.bean.table.Yp;

public class MatchOddsElement extends IssueMatch
{
	/** */
	private static final long serialVersionUID = 1L;

	/** 欧赔数据*/
	List<OddsElement> opitems = new ArrayList<>();
	
	/** 亚盘数据*/
	List<OddsElement> ypitems = new ArrayList<>();
	
	/**
	 * 创建欧赔数据集
	 * @param ops
	 */
	public MatchOddsElement(MatchOdds ops)
	{
		setIssueMatch(ops);
		setOrdinary(ops.getOrdinary());
		addOpList(ops.getOps());
		addYpList(ops.getYps());
	}

	/**
	 * 加入欧赔数据记录
	 * @param ops
	 */
	public void addOpList(List<Op> ops)
	{
		for (Op op : ops)
		{
			opitems.add(OddsUtil.createOpItem(op, 0));
		}
	}
	
	public void addYpList(List<Yp> yps)
	{
		for (Yp yp : yps)
		{
			ypitems.add(OddsUtil.createYpItem(yp, 0));
		}
	}

	public List<OddsElement> getOpItems()
	{
		return opitems;
	}
	
	public void addOpItem(OddsElement item)
	{
		opitems.add(item);
	}

	public List<OddsElement> getYpItems()
	{
		return ypitems;
	}
	
	public void addYpItem(OddsElement item)
	{
		ypitems.add(item);
	}
}
