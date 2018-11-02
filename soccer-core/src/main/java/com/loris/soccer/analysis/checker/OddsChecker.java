package com.loris.soccer.analysis.checker;

import com.loris.base.util.ArraysUtil;
import com.loris.soccer.bean.item.OddsItem;

public class OddsChecker<T extends OddsItem> implements ArraysUtil.EqualChecker<T>
{
	String mid;
	String gid;
	
	public OddsChecker()
	{
	}
	public OddsChecker(String mid)
	{
		this.mid = mid;
	}	
	public String getMid()
	{
		return mid;
	}
	public void setMid(String mid)
	{
		this.mid = mid;
	}
	public String getGid()
	{
		return gid;
	}
	public void setGid(String gid)
	{
		this.gid = gid;
	}

	@Override
	public boolean isSameObject(T obj)
	{
		return mid.equals(obj.getMid()) && gid.equals(obj.getGid());
	}
}
