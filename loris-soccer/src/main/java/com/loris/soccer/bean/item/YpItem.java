package com.loris.soccer.bean.item;

public class YpItem extends OddsItem
{
	/***/
	private static final long serialVersionUID = 1L;

	protected String handicap;             //最新盘口

	public String getHandicap()
	{
		return handicap;
	}
	public void setHandicap(String handicap)
	{
		this.handicap = handicap;
	}
	public void setYpItem(YpItem item)
	{
		this.mid = item.mid;
		this.gid = item.gid;
		this.gname = item.gname;
		this.lasttime = item.lasttime;
		this.handicap = item.handicap;
		this.winodds = item.winodds;
		this.loseodds = item.loseodds;
		this.winkelly = item.winkelly;
		this.losekelly = item.losekelly;
		this.winprob = item.winprob;
		this.loseprob = item.loseprob;
		this.lossratio = item.lossratio;
		this.source = item.source;
	}
	
	@Override
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch(CloneNotSupportedException exception)
		{			
		}
		return null;
	}
}
