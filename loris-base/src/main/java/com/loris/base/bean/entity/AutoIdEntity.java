package com.loris.base.bean.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

public class AutoIdEntity implements Entity, Serializable
{
	/** Serial version uid. */
	private static final long serialVersionUID = 1L;

	/** The id value of the entity. */
	@TableId(type=IdType.AUTO)
	protected String id;

	/**
	 * Get the id.
	 * @return
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * Set the id.
	 * @param id
	 */
	public void setId(String id)
	{
		this.id = id;
	}
}