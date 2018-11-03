package com.loris.base.analysis.stat;

public class Possion
{
	/**
	 * 计算泊松分布的值
	 * @param lamda 期望值
	 * @param k K的值
	 * @return 分布概率
	 */
	public static double computePossion(double lamda, int k)
	{
		double c = Math.exp(-lamda), sigma = 1;
		for (int i = 1; i <= k; i++) {
			sigma *= lamda / i;
		}
		return sigma * c;
	}
	
	/**
	 * 计算期望Lamda期望值的情况下，分布值的数值
	 * @param lamda
	 * @return
	 */
	public static int getPossionVariable(double lamda, double prob) {
		int x = 0;
		double cdf = computePossion(lamda, 0);
		while (cdf < prob) {
			x++;
			cdf += computePossion(lamda, 0);
		}
		return x;
	}
<<<<<<< HEAD
=======
	
	public static void getPossionData()
	{
		return;
	}
>>>>>>> 9b450c39b7c085402877e394d4583d6f2ceaf855
}
