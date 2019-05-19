package com.loris.soccer.bean.item;

import com.loris.soccer.analysis.util.OddsUtil;

public class YpValue
{
	protected String handicap;
	protected float winodds;
	protected float loseodds;
	
	public YpValue()
	{
	}
	
	public YpValue(String handicap, float winodds, float loseodds)
	{
		this.handicap = handicap;
		this.winodds = winodds;
		this.loseodds = loseodds;
	}
	
	public String getHandicap()
	{
		return handicap;
	}
	public void setHandicap(String handicap)
	{
		this.handicap = handicap;
	}
	public float getWinodds()
	{
		return winodds;
	}
	public void setWinodds(float winodds)
	{
		this.winodds = winodds;
	}
	public float getLoseodds()
	{
		return loseodds;
	}
	public void setLoseodds(float loseodds)
	{
		this.loseodds = loseodds;
	}
	
	public boolean isSameHandicap(YpValue value)
	{
		return OddsUtil.isSameHandicap(handicap, value.getHandicap());
	}
	
	/**
	 * 计算赔付率
	 * @return
	 */
	public double getLossratio()
	{
		return 1.0 / (1.0/ winodds + 1.0 / loseodds);
	}
	

	@Override
	public String toString()
	{
		return "YpValue [handicap=" + handicap + ", winodds=" + winodds + ", loseodds=" + loseodds + "]";
	}
}
