package com.loris.base.bean.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

public class UserIDEntity implements Entity, Serializable
{
	/** */
	private static final long serialVersionUID = 1L;

	/** The id value of the entity. */
	@TableId(type=IdType.INPUT)
	protected String id;
	
	@Override
	public String getId()
	{
		return id;
	}

	@Override
	public void setId(String id)
	{
		this.id = id;
	}
}
