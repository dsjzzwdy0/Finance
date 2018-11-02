package com.loris.base.bean.entity;

import java.io.Serializable;
import java.util.UUID;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

public class UUIDEntity implements Entity, Serializable
{
	/** Searial. */
	private static final long serialVersionUID = 1L;
	
	@TableId(type=IdType.UUID)
	protected String id;
	
	/**
	 * Create the UUID value.
	 * 
	 */
	public void create()
	{
		this.id = UUID.randomUUID().toString();
	}

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
