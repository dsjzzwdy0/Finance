package com.loris.soccer.bean.stat;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.entity.AutoIdEntity;

@TableName("soccer_corp_match_result")
public class CorpMatchResult extends AutoIdEntity
{
	/***/
	private static final long serialVersionUID = 1L;
	protected String gid;
	protected String gname;
	protected int num;
	protected float min;
	protected float max;
	protected int winnum;
	protected int winpredictwin;
	protected int winpredictdraw;
	protected int winpredictlose;
	
	protected int drawnum;
	protected int drawpredictdraw;
	protected int drawpredictwin;
	protected int drawpredictlose;
	
	protected int losenum;
	protected int losepredictwin;
	protected int losepredictdraw;
	protected int losepredictlose;
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
	public void addNum()
	{
		this.num ++;
	}
	public int getNum()
	{
		return num;
	}
	public void setNum(int num)
	{
		this.num = num;
	}
	public float getMin()
	{
		return min;
	}
	public void setMin(float min)
	{
		this.min = min;
	}
	public float getMax()
	{
		return max;
	}
	public void setMax(float max)
	{
		this.max = max;
	}
	public int getWinnum()
	{
		return winnum;
	}
	public void setWinnum(int winnum)
	{
		this.winnum = winnum;
	}
	public void addWinNum()
	{
		this.winnum ++;
	}
	public int getWinpredictwin()
	{
		return winpredictwin;
	}
	public void setWinpredictwin(int winpredictwin)
	{
		this.winpredictwin = winpredictwin;
	}
	public void addWinpredictwin()
	{
		winpredictwin ++;
	}
	public int getWinpredictdraw()
	{
		return winpredictdraw;
	}
	public void setWinpredictdraw(int winpredictdraw)
	{
		this.winpredictdraw = winpredictdraw;
	}
	public void addWinpredictdraw()
	{
		winpredictdraw ++;
	}
	public int getWinpredictlose()
	{
		return winpredictlose;
	}
	public void setWinpredictlose(int winpredictlose)
	{
		this.winpredictlose = winpredictlose;
	}
	public void addWinpredictlose()
	{
		winpredictlose ++;
	}
	public int getDrawnum()
	{
		return drawnum;
	}
	public void setDrawnum(int drawnum)
	{
		this.drawnum = drawnum;
	}
	public void addDrawnum()
	{
		drawnum ++;
	}
	public int getDrawpredictdraw()
	{
		return drawpredictdraw;
	}
	public void setDrawpredictdraw(int drawpredictdraw)
	{
		this.drawpredictdraw = drawpredictdraw;
	}
	public void addDrawpredictdraw()
	{
		drawpredictdraw ++;
	}
	public int getDrawpredictwin()
	{
		return drawpredictwin;
	}
	public void setDrawpredictwin(int drawpredictwin)
	{
		this.drawpredictwin = drawpredictwin;
	}
	public void addDrawpredictwin()
	{
		drawpredictwin ++;
	}
	public int getDrawpredictlose()
	{
		return drawpredictlose;
	}
	public void setDrawpredictlose(int drawpredictlose)
	{
		this.drawpredictlose = drawpredictlose;
	}
	public void addDrawpredictlose()
	{
		drawpredictlose ++;
	}
	public int getLosenum()
	{
		return losenum;
	}
	public void setLosenum(int losenum)
	{
		this.losenum = losenum;
	}
	public void addLosenum()
	{
		losenum ++;
	}
	public int getLosepredictwin()
	{
		return losepredictwin;
	}
	public void setLosepredictwin(int losepredictwin)
	{
		this.losepredictwin = losepredictwin;
	}
	public void addLosepredictwin()
	{
		losepredictwin ++;
	}
	public int getLosepredictdraw()
	{
		return losepredictdraw;
	}
	public void setLosepredictdraw(int losepredictdraw)
	{
		this.losepredictdraw = losepredictdraw;
	}
	public void addLosepredictdraw()
	{
		losepredictdraw ++;
	}
	public int getLosepredictlose()
	{
		return losepredictlose;
	}
	public void setLosepredictlose(int losepredictlose)
	{
		this.losepredictlose = losepredictlose;
	}
	public void addLosepredictlose()
	{
		losepredictlose ++;
	}
	
	public boolean contains(float odds)
	{
		return min <= odds && max >= odds;
	}
	@Override
	public String toString()
	{
		return "CorpMatchResult [gid=" + gid + ", gname=" + gname + ", num=" + num + ", min=" + min + ", max=" + max
				+ ", winnum=" + winnum + ", winpredictwin=" + winpredictwin + ", winpredictdraw=" + winpredictdraw
				+ ", winpredictlose=" + winpredictlose + ", drawnum=" + drawnum + ", drawpredictdraw=" + drawpredictdraw
				+ ", drawpredictwin=" + drawpredictwin + ", drawpredictlose=" + drawpredictlose + ", losenum=" + losenum
				+ ", losepredictwin=" + losepredictwin + ", losepredictdraw=" + losepredictdraw + ", losepredictlose="
				+ losepredictlose + "]";
	}
}
