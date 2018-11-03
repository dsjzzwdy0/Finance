package com.loris.soccer.bean.okooo;

import com.loris.soccer.bean.data.table.odds.Op;

public class OkoooOp extends Op
{
	/***/
	private static final long serialVersionUID = 1L;

	private String time;
	private boolean isfirst = false;		//是否初赔
	private boolean isend = false;			//是否终赔
	public String getTime()
	{
		return time;
	}
	public void setTime(String time)
	{
		this.time = time;
	}
	public boolean isIsfirst()
	{
		return isfirst;
	}
	public void setIsfirst(boolean isfirst)
	{
		this.isfirst = isfirst;
	}
	public boolean isIsend()
	{
		return isend;
	}
	public void setIsend(boolean isend)
	{
		this.isend = isend;
	}
}
