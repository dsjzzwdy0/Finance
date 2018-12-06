package com.loris.soccer.bean.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.loris.base.util.NumberUtil;
import com.loris.soccer.bean.data.table.Op;

public class OpList extends ArrayList<Op>
{
	public static enum OpListType{
		General,			//通用的类型
		GidUnique,			//Gid唯一类型，就是家公司的只能有一个值
		MidUnique			//Mid唯一类型，就是一场比赛的只能有一个值
	}
	
	/***/
	private static final long serialVersionUID = 1L;

	OpListType type;

	public OpList()
	{
		this(OpListType.General);
	}

	public OpList(OpListType type)
	{
		this.type = type;
	}

	/**
	 * 
	 * @param ops
	 */
	public OpList(OpListType type, List<Op> ops)
	{
		this(type);
		this.addAll(ops);
	}

	/**
	 * 添加数据
	 * 
	 * @param ops
	 * @return
	 */
	@Override
	public boolean addAll(Collection<? extends Op> ops)
	{
		if(type == OpListType.GidUnique)
		{
			for (Op op : ops)
			{
				Op op1 = getOpByGid(op.getGid());
				if (op1 == null)
				{
					add(op);
				}
				else
				{
					long t1 = NumberUtil.parseLong(op1.getLasttime());
					long t2 = NumberUtil.parseLong(op.getLasttime());
	
					if (t1 < t2)
					{
						this.remove(op1);
						this.add(op);
					}
				}
			}
		}
		else if(type == OpListType.MidUnique)
		{
			for (Op op : ops)
			{
				Op op1 = getOpByMid(op.getGid());
				if (op1 == null)
				{
					add(op);
				}
				else
				{
					long t1 = NumberUtil.parseLong(op1.getLasttime());
					long t2 = NumberUtil.parseLong(op.getLasttime());
	
					if (t1 < t2)
					{
						this.remove(op1);
						this.add(op);
					}
				}
			}
		}
		else
		{
			for (Op op : ops)
			{
				this.add(op);
			}
		}
		return true;
	}

	/**
	 * 查找某一个欧赔公司的欧赔值
	 * 
	 * @param gid
	 * @return
	 */
	public Op getOpByGid(String gid)
	{
		for (Op op : this)
		{
			if (gid.equals(op.getGid()))
			{
				return op;
			}
		}
		return null;
	}
	
	/**
	 * 查找某一个
	 * @param mid
	 * @return
	 */
	public Op getOpByMid(String mid)
	{
		for (Op op : this)
		{
			if (mid.equals(op.getMid()))
			{
				return op;
			}
		}
		return null;
	}

	/**
	 * 获得平均欧赔值
	 * 
	 * @return
	 */
	public Op getAvgOp()
	{
		return getOpByGid("0");
	}

	public OpListType getType()
	{
		return type;
	}

	public void setType(OpListType type)
	{
		this.type = type;
	}
}
