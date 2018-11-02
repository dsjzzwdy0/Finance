package com.loris.base.web.util;

import java.util.Random;

public class RandomUtil
{
	private static Random random = new Random();
	
	/**
	 * Get the Random Integer value.
	 * @param seed The max random value.
	 * @return The Integer.
	 */
	public static int getRandom(int seed)
	{
		return random.nextInt(seed);
	}
}
