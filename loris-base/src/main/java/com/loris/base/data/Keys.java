package com.loris.base.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author deng
 *
 */
public class Keys extends ArrayList<String>
{
	/***/
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a new instance of Keys.
	 */
	public Keys()
	{
	}
	
	/**
	 * Create a new instance of Keys.
	 * @param keys
	 */
	public Keys(List<String> keys)
	{
		addAll(keys);
	}
}
