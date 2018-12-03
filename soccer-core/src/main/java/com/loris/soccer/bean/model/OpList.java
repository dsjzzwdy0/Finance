package com.loris.soccer.bean.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.loris.base.util.NumberUtil;
import com.loris.soccer.bean.data.table.Op;

public class OpList extends ArrayList<Op>
{
	/***/
	private static final long serialVersionUID = 1L;
	
	public OpList()
	{
	}

	/**
	 * 
	 * @param ops
	 */
	public OpList(List<Op> ops)
	{
		this.addAll(ops);
	}
	
	/**
	 * 添加数据
	 * @param ops
	 * @return 
	 */
	@Override
	public boolean addAll(Collection<? extends Op> ops)
	{
		for (Op op : ops)
		{
			Op op1 = getOp(op.getGid());
			if(op1 == null)
			{
				add(op);
			}
			else
			{
				long t1 = NumberUtil.parseLong(op1.getLasttime());
				long t2 = NumberUtil.parseLong(op.getLasttime());
				
				if(t1 < t2)
				{
					this.remove(op1);
					this.add(op);
				}
			}
		}
		return true;
	}
	
	/**
	 * 查找某一个欧赔公司的欧赔值
	 * @param gid
	 * @return
	 */
	public Op getOp(String gid)
	{
		for (Op op : this)
		{
			if(gid.equals(op.getGid()))
			{
				return op;
			}
		}
		return null;
	}
	
	/**
	 * 获得平均欧赔值
	 * @return
	 */
	public Op getAvgOp()
	{
		return getOp("0");
	}
}
