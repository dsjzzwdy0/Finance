package com.loris.soccer.analysis.data;


/**
 * 比赛欧赔数据的欧赔
 * @author deng
 *
 */
public class OpVariance
{
	String time;
	OddsVariance win;
	OddsVariance draw;
	OddsVariance lose;
	
	public String getTime()
	{
		return time;
	}
	public void setTime(String time)
	{
		this.time = time;
	}
	public OddsVariance getWin()
	{
		return win;
	}
	public void setWin(OddsVariance win)
	{
		this.win = win;
	}
	public OddsVariance getDraw()
	{
		return draw;
	}
	public void setDraw(OddsVariance draw)
	{
		this.draw = draw;
	}
	public OddsVariance getLose()
	{
		return lose;
	}
	public void setLose(OddsVariance lose)
	{
		this.lose = lose;
	}
}
