package com.loris.soccer.bean.data.table.odds;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.item.YpItem;
import com.loris.soccer.bean.item.YpValue;

@TableName("soccer_odds_yp")
public class Yp extends YpItem
{
	/***/
	private static final long serialVersionUID = 1L;
	
	private String ordinary;             	//公司序号
	private String oddstype;             	//类型：亚盘、欧盘
	private String firsttime;            	//赔率时间
	private float firstwinodds;         	//初盘获胜
	private String firsthandicap;        	//初盘盘口
	private float firstloseodds;        	//初盘负赔率

	
	public Yp()
	{
		this.source = SoccerConstants.DATA_SOURCE_ZGZCW;
	}
	
	public String getOrdinary()
	{
		return ordinary;
	}
	public void setOrdinary(String ordinary)
	{
		this.ordinary = ordinary;
	}
	public String getOddstype()
	{
		return oddstype;
	}
	public void setOddstype(String oddstype)
	{
		this.oddstype = oddstype;
	}
	public String getFirsttime()
	{
		return firsttime;
	}
	public void setFirsttime(String firsttime)
	{
		this.firsttime = firsttime;
	}
	public float getFirstwinodds()
	{
		return firstwinodds;
	}
	public void setFirstwinodds(float firstwinodds)
	{
		this.firstwinodds = firstwinodds;
	}
	public String getFirsthandicap()
	{
		return firsthandicap;
	}
	public void setFirsthandicap(String firsthandicap)
	{
		this.firsthandicap = firsthandicap;
	}
	public float getFirstloseodds()
	{
		return firstloseodds;
	}
	public void setFirstloseodds(float firstloseodds)
	{
		this.firstloseodds = firstloseodds;
	}
	
	public YpValue getYpValue()
	{
		return new YpValue(handicap, winodds, loseodds);
	}
	
	public YpValue getFirstYpValue()
	{
		return new YpValue(firsthandicap, firstwinodds, firstloseodds);
	}
	
	/**
	 * 两个数据是否一致
	 * 相等的条件是：比赛编号、欧赔公司编号、赔率开出时间三个属性相等。
	 * 
	 * @param yp 待比赛的欧赔数据
	 * @return 是否相等
	 */
	public boolean equals(Yp yp)
	{
		if(mid.equalsIgnoreCase(yp.getMid()) && gid.equalsIgnoreCase(yp.getGid())
				&& lasttime.equalsIgnoreCase(yp.getLasttime()))
		{
			if(StringUtils.isNotEmpty(firsttime) && firsttime.equalsIgnoreCase(yp.getFirsttime()))
			{
				return true;
			}
			else if(StringUtils.isEmpty(firsttime) && StringUtils.isEmpty(yp.getFirsttime()))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return "Yp [ordinary=" + ordinary + ", mid=" + mid + ", gid=" + gid + ", gname=" + gname + ", oddstype="
				+ oddstype + ", firsttime=" + firsttime + ", firstwinodds=" + firstwinodds + ", firsthandicap="
				+ firsthandicap + ", firstloseodds=" + firstloseodds + ", lasttime=" + lasttime + ", winodds=" + winodds
				+ ", handicap=" + handicap + ", loseodds=" + loseodds + ", winprob=" + winprob + ", loseprob="
				+ loseprob + ", winkelly=" + winkelly + ", losekelly=" + losekelly + ", lossratio=" + lossratio + "]";
	}
}
