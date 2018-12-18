package com.loris.soccer.bean.item;

import com.loris.base.bean.entity.AutoIdEntity;
import com.loris.soccer.bean.table.Corporate;

public class OddsItem extends AutoIdEntity implements Cloneable
{
	/***/
	private static final long serialVersionUID = 1L;
	protected String mid;
	protected String gid;
	protected String gname;					//公司名称
	protected String lasttime;            	//最新时间	
	protected float winodds;              	//最新获胜赔率
	protected float loseodds;             	//最新负赔率
	protected float winprob;              	//最新概率：主
	protected float loseprob;             	//最新概率：客
	protected float winkelly;             	//凯利指数：主
	protected float losekelly;            	//凯利指数：客
	protected float lossratio;            	//赔付率
	protected String source;				//数据来源
	public String getMid()
	{
		return mid;
	}
	public void setMid(String mid)
	{
		this.mid = mid;
	}
	public String getGid()
	{
		return gid;
	}
	public void setGid(String gid)
	{
		this.gid = gid;
	}
	public String getGname()
	{
		return gname;
	}
	public void setGname(String gname)
	{
		this.gname = gname;
	}
	public String getLasttime()
	{
		return lasttime;
	}
	public void setLasttime(String lasttime)
	{
		this.lasttime = lasttime;
	}
	public long getLastTimeValue()
	{
		try{
			return Long.parseLong(lasttime);
		}
		catch (Exception e) {
			return Long.MIN_VALUE;
		}
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
	public float getWinprob()
	{
		return winprob;
	}
	public void setWinprob(float winprob)
	{
		this.winprob = winprob;
	}
	public float getLoseprob()
	{
		return loseprob;
	}
	public void setLoseprob(float loseprob)
	{
		this.loseprob = loseprob;
	}
	public float getWinkelly()
	{
		return winkelly;
	}
	public void setWinkelly(float winkelly)
	{
		this.winkelly = winkelly;
	}
	public float getLosekelly()
	{
		return losekelly;
	}
	public void setLosekelly(float losekelly)
	{
		this.losekelly = losekelly;
	}
	public float getLossratio()
	{
		return lossratio;
	}
	public void setLossratio(float lossratio)
	{
		this.lossratio = lossratio;
	}
	public String getSource()
	{
		return source;
	}
	public void setSource(String source)
	{
		this.source = source;
	}
	
	public Corporate getCorporate()
	{
		Corporate corp = new Corporate();
		corp.setGid(gid);
		corp.setName(gname);
		return corp;
	}
	@Override
	public String toString()
	{
		return "OddsItem [mid=" + mid + ", gid=" + gid + ", gname=" + gname + ", lasttime=" + lasttime + ", winodds="
				+ winodds + ", loseodds=" + loseodds + ", winprob=" + winprob + ", loseprob=" + loseprob + ", winkelly="
				+ winkelly + ", losekelly=" + losekelly + ", lossratio=" + lossratio + ", source=" + source + "]";
	}
}
