package com.loris.soccer.analysis.checker;

import com.loris.base.util.ArraysUtil;
import com.loris.soccer.bean.data.table.Corporate;

public class CorpChecker<T extends Corporate> implements ArraysUtil.EqualChecker<T>
{
	String gid;
	
	
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
		return gid.equals(obj.getGid());
	}

}
