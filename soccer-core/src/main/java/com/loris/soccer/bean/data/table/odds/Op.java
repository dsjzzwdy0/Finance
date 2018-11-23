package com.loris.soccer.bean.data.table.odds;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.DateUtil;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.item.OpItem;
import com.loris.soccer.bean.item.OpValue;

@TableName("soccer_odds_op")
public class Op extends OpItem
{
	/***/
	private static final long serialVersionUID = 1L;

	private boolean ismain; // 是否主流公司
	private String ordinary; // 公司序号
	private String oddstype; // 赔率类型
	private String firsttime; // 赔率时间
	private float firstwinodds; // 初盘获胜
	private float firstdrawodds; // 初盘平率
	private float firstloseodds; // 初盘负赔率

	public Op()
	{
		this.source = SoccerConstants.DATA_SOURCE_ZGZCW;
	}

	public long getFirstTimeValue()
	{
		Date date = DateUtil.tryToParseDate(firsttime);
		return date == null ? 0 : date.getTime();
	}

	public boolean isIsmain()
	{
		return ismain;
	}

	public void setIsmain(boolean ismain)
	{
		this.ismain = ismain;
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

	public float getFirstdrawodds()
	{
		return firstdrawodds;
	}

	public void setFirstdrawodds(float firstdrawodds)
	{
		this.firstdrawodds = firstdrawodds;
	}

	public float getFirstloseodds()
	{
		return firstloseodds;
	}

	public void setFirstloseodds(float firstloseodds)
	{
		this.firstloseodds = firstloseodds;
	}

	public boolean hasFirstValue()
	{
		return StringUtils.isNotEmpty(firsttime) && firstwinodds > 0 && firstdrawodds > 0 && firstloseodds > 0;
	}

	public OpValue getFirstOpValue()
	{
		return new OpValue(firstwinodds, firstdrawodds, firstloseodds);
	}

	public OpValue getOpValue()
	{
		return new OpValue(winodds, drawodds, loseodds);
	}

	/**
	 * 两个数据是否一致 相等的条件是：比赛编号、欧赔公司编号、赔率开出时间三个属性相等。
	 * 
	 * @param op
	 *            待比赛的欧赔数据
	 * @return 是否相等
	 */
	public boolean equals(Op op)
	{
		if (mid.equalsIgnoreCase(op.getMid()) && gid.equalsIgnoreCase(op.getGid())
				&& lasttime.equalsIgnoreCase(op.getLasttime()))
		{
			if (StringUtils.isNotEmpty(firsttime) && firsttime.equalsIgnoreCase(op.getFirsttime()))
			{
				return true;
			}
			else if (StringUtils.isEmpty(firsttime) && StringUtils.isEmpty(op.getFirsttime()))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString()
	{
		return "Op [mid=" + mid + ", gid=" + gid + ", gname=" + gname + ", ismain=" + ismain + ", ordinary=" + ordinary
				+ ", oddstype=" + oddstype + ", firsttime=" + firsttime + ", firstwinodds=" + firstwinodds
				+ ", firstdrawodds=" + firstdrawodds + ", firstloseodds=" + firstloseodds + ", lasttime=" + lasttime
				+ ", winodds=" + winodds + ", drawodds=" + drawodds + ", loseodds=" + loseodds + ", winprob=" + winprob
				+ ", drawprob=" + drawprob + ", loseprob=" + loseprob + ", winkelly=" + winkelly + ", drawkelly="
				+ drawkelly + ", losekelly=" + losekelly + ", lossratio=" + lossratio + "]";
	}

}
