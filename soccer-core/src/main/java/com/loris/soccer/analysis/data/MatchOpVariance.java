package com.loris.soccer.analysis.data;

import java.util.ArrayList;
import java.util.List;

import com.loris.soccer.bean.item.IssueMatch;

public class MatchOpVariance extends IssueMatch
{
	/***/
	private static final long serialVersionUID = 1L;
	
	List<OpVariance> vars = new ArrayList<>();

	/**
	 * 创建一个比赛的方差数据
	 * @param match
	 */
	public MatchOpVariance(IssueMatch match)
	{
		setIssueMatch(match);
	}
	
	public MatchOpVariance(IssueMatch match, List<OpVariance> vars)
	{
		setIssueMatch(match);
		this.vars.addAll(vars);
	}
	
	public void addOpVariance(OpVariance var)
	{
		vars.add(var);
	}

	public List<OpVariance> getVars()
	{
		return vars;
	}
}
