package com.loris.base.bean.wrapper;

import java.util.HashMap;

public class Result extends HashMap<String, Object>
{
	/***/
	private static final long serialVersionUID = 1L;

	public Result()
	{
	}
	
	public Result(String key, Object value)
	{
		put(key, value);
	}
}
