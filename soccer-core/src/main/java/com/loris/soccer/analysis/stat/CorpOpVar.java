package com.loris.soccer.analysis.stat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.loris.base.util.NumberUtil;

public class CorpOpVar
{
	protected int num;
	protected float[] vars = new float[12];
	
	public CorpOpVar()
	{
	}
	
	public CorpOpVar(int num, float vars[])
	{
		if(vars.length != 12)
		{
			throw new IllegalArgumentException("The vars length is not 12, error.");
		}
		this.num = num;
		this.vars = vars;
	}
	public int getNum()
	{
		return num;
	}
	public void setNum(int num)
	{
		this.num = num;
	}
	public void addNum()
	{
		this.num ++;
	}
	public float[] getVars()
	{
		return vars;
	}
	public void setVars(float[] vars)
	{
		this.vars = vars;
	}
	@JsonIgnore
	public float getFirstwindiff()
	{
		return vars[0];
	}

	public void setFirstwindiff(float firstwindiff)
	{
		this.vars[0] = firstwindiff;
	}
	@JsonIgnore
	public float getFirstdrawdiff()
	{
		return vars[1];
	}

	public void setFirstdrawdiff(float firstdrawdiff)
	{
		this.vars[1] = firstdrawdiff;
	}
	@JsonIgnore
	public float getFirstlosediff()
	{
		return vars[2];
	}

	public void setFirstlosediff(float firstlosediff)
	{
		this.vars[2] = firstlosediff;
	}
	@JsonIgnore
	public float getFirstwinstd()
	{
		return vars[3];
	}

	public void setFirstwinstd(float firstwinstd)
	{
		this.vars[3] = firstwinstd;
	}
	@JsonIgnore
	public float getFirstdrawstd()
	{
		return vars[4];
	}

	public void setFirstdrawstd(float firstdrawstd)
	{
		this.vars[4] = firstdrawstd;
	}
	@JsonIgnore
	public float getFirstlosestd()
	{
		return vars[5];
	}

	public void setFirstlosestd(float firstlosestd)
	{
		this.vars[5] = firstlosestd;
	}
	@JsonIgnore
	public float getWindiff()
	{
		return vars[6];
	}

	public void setWindiff(float windiff)
	{
		this.vars[6] = windiff;
	}
	@JsonIgnore
	public float getDrawdiff()
	{
		return vars[7];
	}

	public void setDrawdiff(float drawdiff)
	{
		this.vars[7] = drawdiff;
	}
	@JsonIgnore
	public float getLosediff()
	{
		return vars[8];
	}

	public void setLosediff(float losediff)
	{
		this.vars[8] = losediff;
	}
	@JsonIgnore
	public float getWinstd()
	{
		return vars[9];
	}

	public void setWinstd(float winstd)
	{
		this.vars[9] = winstd;
	}
	@JsonIgnore
	public float getDrawstd()
	{
		return vars[10];
	}

	public void setDrawstd(float drawstd)
	{
		this.vars[10] = drawstd;
	}
	@JsonIgnore
	public float getLosestd()
	{
		return vars[11];
	}

	public void setLosestd(float losestd)
	{
		this.vars[11] = losestd;
	}
	public void computeMean()
	{
		if(num <= 0)
		{
			return;
		}
		int size = vars.length;
		for (int i = 0; i < size; i ++)
		{
			int k = (i / 3) % 2;
			if(k == 0)
			{
				vars[i] /= num;
			}
		}
	}
	
	public void computeStdErr()
	{
		if(num <= 0)
		{
			return;
		}
		int size = vars.length;
		for (int i = 0; i < size; i ++)
		{
			int k = (i / 3) % 2;
			if(k == 1)
			{
				vars[i] = (float)Math.sqrt(vars[i] /num);
			}
		}
	}
	
	public void add(float firstwindiff, float firstdrawdiff, float firstlosediff, 
			float firstwinstd, float firstdrawstd, float firstlosestd, 
			float windiff, float drawdiff, float losediff,
			float winstd, float drawstd, float losestd)
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
	
	public void add(float[] vars)
	{
		if(vars.length != 12)
		{
			throw new IllegalArgumentException("The vars length is not 12, error.");
		}
		for(int i = 0; i < vars.length; i ++)
		{
			this.vars[i] += vars[i];
		}
	}
	
	@Override
	public String toString()
	{
		int max = vars.length - 1;
		StringBuffer sb = new StringBuffer();
		sb.append("OpVar [" + num + ", ");
		for (int i = 0; i < max; i ++)
		{
			sb.append(NumberUtil.formatDouble(2, vars[i]) + ", ");
		}
		sb.append(NumberUtil.formatDouble(2, vars[max]) + "]");
		return sb.toString();
	}
}
