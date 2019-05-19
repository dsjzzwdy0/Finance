package com.loris.soccer.bean.item;

public class OpItem extends OddsItem
{
	/***/
	private static final long serialVersionUID = 1L;

	protected float drawodds;              		//最新平局赔率	
	protected float drawprob;				 	//最新概率：平，亚盘时没有该项指标	
	protected float drawkelly;                	//凯利指数：平，亚盘时没有该项指标
	
	public float getDrawodds()
	{
		return drawodds;
	}
	public void setDrawodds(float drawodds)
	{
		this.drawodds = drawodds;
	}

	public float getDrawprob()
	{
		return drawprob;
	}
	public void setDrawprob(float drawprob)
	{
		this.drawprob = drawprob;
	}
	
	public float getDrawkelly()
	{
		return drawkelly;
	}
	public void setDrawkelly(float drawkelly)
	{
		this.drawkelly = drawkelly;
	}
	public void setOpItem(OpItem item)
	{
		this.mid = item.mid;
		this.gid = item.gid;
		this.gname = item.gname;
		this.lasttime = item.lasttime;
		this.winodds = item.winodds;
		this.drawodds = item.drawodds;
		this.loseodds = item.loseodds;
		this.winkelly = item.winkelly;
		this.drawkelly = item.drawkelly;
		this.losekelly = item.losekelly;
		this.winprob = item.winprob;
		this.drawprob = item.drawprob;
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
