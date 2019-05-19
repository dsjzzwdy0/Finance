package com.loris.soccer.analysis.checker;

import com.loris.base.util.ArraysUtil.EqualChecker;
import com.loris.soccer.bean.item.MatchItem;

public class MatchChecker<T extends MatchItem> implements EqualChecker<T>
{
	String mid;
	
	public String getMid()
	{
		return mid;
	}

	public void setMid(String mid)
	{
		this.mid = mid;
	}

	@Override
	public boolean isSameObject(T obj)
	{
		return obj.getMid().equals(mid);
	}

}
