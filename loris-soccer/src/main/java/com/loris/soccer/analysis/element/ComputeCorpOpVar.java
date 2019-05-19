package com.loris.soccer.analysis.element;

import com.loris.soccer.bean.item.CorpOpVar;

public class ComputeCorpOpVar extends CorpOpVar
{
	public void addNum()
	{
		this.num++;
	}

	/**
	 * 计算平均值
	 */
	public void computeMeanValue()
	{
		if (num <= 0)
		{
			return;
		}
		int size = vars.length;
		for (int i = 0; i < size; i++)
		{
			int k = (i / 3) % 2;
			if (k == 0)
			{
				vars[i] /= num;
			}
		}
	}

	/**
	 * 计算方差值
	 */
	public void computeStdErr()
	{
		if (num <= 0)
		{
			return;
		}
		int size = vars.length;
		for (int i = 0; i < size; i++)
		{
			int k = (i / 3) % 2;
			if (k == 1)
			{
				vars[i] = (float) Math.sqrt(vars[i] / num);
			}
		}
	}

	/**
	 * 加入一个数据记录
	 * @param firstwindiff
	 * @param firstdrawdiff
	 * @param firstlosediff
	 * @param firstwinstd
	 * @param firstdrawstd
	 * @param firstlosestd
	 * @param windiff
	 * @param drawdiff
	 * @param losediff
	 * @param winstd
	 * @param drawstd
	 * @param losestd
	 */
	public void add(float firstwindiff, float firstdrawdiff, float firstlosediff, float firstwinstd, float firstdrawstd,
			float firstlosestd, float windiff, float drawdiff, float losediff, float winstd, float drawstd,
			float losestd)
	{
		vars[0] += firstwindiff;
		vars[1] += firstdrawdiff;
		vars[2] += firstlosediff;
		vars[3] += firstwinstd;
		vars[4] += firstdrawstd;
		vars[5] += firstlosestd;
		vars[6] += windiff;
		vars[7] += drawdiff;
		vars[8] += losediff;
		vars[9] += winstd;
		vars[10] += drawstd;
		vars[11] += losestd;
	}

	/**
	 * 加入数据记录
	 * @param vars
	 */
	public void add(float[] vars)
	{
		if (vars.length != 12)
		{
			throw new IllegalArgumentException("The vars length is not 12, error.");
		}
		for (int i = 0; i < vars.length; i++)
		{
			this.vars[i] += vars[i];
		}
	}
}
