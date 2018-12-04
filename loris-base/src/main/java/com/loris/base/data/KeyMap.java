package com.loris.base.data;

import java.util.HashMap;

public class KeyMap extends HashMap<String, Integer>
{
	/***/
	private static final long serialVersionUID = 1L;
	
	public void add(String key)
	{
		Integer num = get(key);
		if(num == null)
		{
			num = 1;
		}
		else
		{
			num ++;
		}
		put(key, num);
	}

}
