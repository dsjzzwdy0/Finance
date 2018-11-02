package com.loris.base.util;

public class Assume
{
	public static void assume(boolean expression)
	{
		assume(expression, "");
	}

	public static void assume(boolean expression, String comment)
	{
		if (!expression)
		{
			throw new RuntimeException(comment);
		}
	}
}
