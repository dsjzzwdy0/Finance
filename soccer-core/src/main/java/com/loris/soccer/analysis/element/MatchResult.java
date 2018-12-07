package com.loris.soccer.analysis.element;

import java.util.ArrayList;

public class MatchResult extends ArrayList<MatchCorpProb>
{
	/***/
	private static final long serialVersionUID = 1L;

	protected String mid;
	
	public MatchResult()
	{
	}
	
	public MatchResult(String mid)
	{
		this.mid = mid;
	}
	
	public int getWinNum()
	{
		return getNum(3);
	}
	
	public int getDrawNum()
	{
		return getNum(1);
	}
	public int getLoseNum()
	{
		return getNum(0);
	}
	
	/**
	 * 统计数据
	 * @param result
	 * @return
	 */
	public int getNum(int result)
	{
		int num = 0;
		for (MatchCorpProb prob : this)
		{
			if(prob.getResult() == result)
			{
				num ++;
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
