package com.loris.soccer.analysis.data;

import java.util.ArrayList;
import java.util.List;

import com.loris.soccer.bean.item.IssueMatch;
import com.loris.soccer.bean.table.Op;
import com.loris.soccer.bean.table.Yp;
import com.loris.soccer.bean.view.MatchInfo;

public class MatchOdds extends IssueMatch
{
	/***/
	private static final long serialVersionUID = 1L;
	
	/** 欧赔数据 */
	protected List<Op> ops = new ArrayList<>();
	
	/** 亚盘数据*/
	protected List<Yp> yps = new ArrayList<>();
	
	/**
	 * 比赛的欧赔数据
	 */
	public MatchOdds()
	{
	}
	/**
	 * 比赛信息数据
	 * @param matchInfo
	 */
	public MatchOdds(MatchInfo matchInfo)
	{
		setMatchInfoItem(matchInfo);
	}
	
	/**
	 * 创建比赛
	 * @param issueMatch
	 */
	public MatchOdds(IssueMatch issueMatch)
	{
		setIssueMatch(issueMatch);
		setOrdinary(issueMatch.getOrdinary());
	}

	/**
	 * Add the Op value.
	 * @param op
	 */
	public void addOp(Op op)
	{
		ops.add(op);
	}
	
	public void addYp(Yp yp)
	{
		yps.add(yp);
	}
	
	/**
	 * 获得欧赔数据
	 * @return 欧赔数据列表
	 */
	public List<Op> getOps()
	{
		return ops;
	}
	
	public List<Yp> getYps()
	{
		return yps;
	}

	public void setYps(List<Yp> yps)
	{
		this.yps = yps;
	}

	/**
	 * 设置欧赔数据
	 * @param ops
	 */
	public void setOps(List<Op> ops)
	{
		this.ops.clear();
		this.ops.addAll(ops);
	}
}
