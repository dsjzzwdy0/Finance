package com.loris.soccer.bean.element;

import java.io.Serializable;

import com.loris.soccer.bean.data.table.league.Match;
import com.loris.soccer.bean.item.IssueMatch;
import com.loris.soccer.bean.item.PerformItem;

/**
 * 竞彩综合数据元素，包括有：<br/>
 * 1、竞彩基本信息<br/>
 * 2、亚盘数据<br/>
 * 3、欧赔数据<br/>
 * 4、主队成绩<br/>
 * 5、客队成绩<br/>
 * 6、最后一场比赛信息<br/>
 * @author jiean
 *
 */
public class MatchSynthElement extends IssueMatch implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected OddsElements opItems;
	protected OddsElements ypItems;
	
	protected PerformItem homePerf;
	protected PerformItem clientPerf;
	protected Match lastMatch;
	
	public MatchSynthElement()
	{
		opItems = new OddsElements();
		ypItems = new OddsElements();
	}
	
	public MatchSynthElement(IssueMatch match)
	{
		this();
		setIssueMatch(match);
	}

	public OddsElements getOpItems()
	{
		return opItems;
	}
	
	public void addOpItem(OddsElement item)
	{
		opItems.add(item);
	}
	
	public OddsElements getYpItems()
	{
		return ypItems;
	}
	
	public void addYpItem(OddsElement item)
	{
		ypItems.add(item);
	}

	public PerformItem getHomePerf()
	{
		return homePerf;
	}

	public void setHomePerf(PerformItem homePerf)
	{
		this.homePerf = homePerf;
	}

	public PerformItem getClientPerf()
	{
		return clientPerf;
	}

	public void setClientPerf(PerformItem clientPerf)
	{
		this.clientPerf = clientPerf;
	}

	public Match getLastMatch()
	{
		return lastMatch;
	}

	public void setLastMatch(Match lastMatch)
	{
		this.lastMatch = lastMatch;
	}
}
