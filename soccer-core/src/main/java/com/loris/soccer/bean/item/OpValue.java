package com.loris.soccer.bean.item;

public class OpValue
{
	float winodds;
	float drawodds;
	float loseodds;
	
	public OpValue()
	{
	}
	
	public OpValue(float winodds, float drawodds, float loseodds)
	{
		this.winodds = winodds;
		this.drawodds = drawodds;
		this.loseodds = loseodds;
	}
	
	public float getWinodds()
	{
		return winodds;
	}
	public void setWinodds(float winodds)
	{
		this.winodds = winodds;
	}
	public float getDrawodds()
	{
		return drawodds;
	}
	public void setDrawodds(float drawodds)
	{
		this.drawodds = drawodds;
	}
	public float getLoseodds()
	{
		return loseodds;
	}
	public void setLoseodds(float loseodds)
	{
		this.loseodds = loseodds;
	}
	
	
}
