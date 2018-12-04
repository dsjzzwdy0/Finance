package com.loris.soccer.bean.item;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.entity.AutoIdEntity;
import com.loris.soccer.analysis.stat.CorpOpVar;
import com.loris.soccer.bean.data.table.Corporate;

@TableName("soccer_corp_distribution")
public class CorpStatItem extends AutoIdEntity
{
	/***/
	private static final long serialVersionUID = 1L;
	protected String gid;					//博彩公司编号
	protected String name;					//博彩公司名称
	
	//总数据量及偏离值
	protected int num;
	protected float firstwindiff;
	protected float firstdrawdiff;
	protected float firstlosediff;
	protected float firstwinstd;
	protected float firstdrawstd;
	protected float firstlosestd;
	protected float windiff;
	protected float drawdiff;
	protected float losediff;
	protected float winstd;
	protected float drawstd;
	protected float losestd;
	
	//胜值的统计
	protected int winnum;
	protected float winfirstwindiff;
	protected float winfirstdrawdiff;
	protected float winfirstlosediff;
	protected float winfirstwinstd;
	protected float winfirstdrawstd;
	protected float winfirstlosestd;
	protected float winwindiff;
	protected float windrawdiff;
	protected float winlosediff;
	protected float winwinstd;
	protected float windrawstd;
	protected float winlosestd;
	
	//平值的统计
	protected int drawnum;
	protected float drawfirstwindiff;
	protected float drawfirstdrawdiff;
	protected float drawfirstlosediff;
	protected float drawfirstwinstd;
	protected float drawfirstdrawstd;
	protected float drawfirstlosestd;
	protected float drawwindiff;
	protected float drawdrawdiff;
	protected float drawlosediff;
	protected float drawwinstd;
	protected float drawdrawstd;
	protected float drawlosestd;
	
	//负值的统计
	protected int losenum;
	protected float losefirstwindiff;
	protected float losefirstdrawdiff;
	protected float losefirstlosediff;
	protected float losefirstwinstd;
	protected float losefirstdrawstd;
	protected float losefirstlosestd;
	protected float losewindiff;
	protected float losedrawdiff;
	protected float loselosediff;
	protected float losewinstd;
	protected float losedrawstd;
	protected float loselosestd;
	
	public CorpStatItem()
	{		
	}
	
	public CorpStatItem(String gid, String name)
	{
		this.gid = gid;
		this.name = name;
	}
	
	public CorpStatItem(Corporate corporate)
	{
		this.gid = corporate.getGid();
		this.name = corporate.getName();
	}

	public String getGid()
	{
		return gid;
	}

	public void setGid(String gid)
	{
		this.gid = gid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getNum()
	{
		return num;
	}

	public void setNum(int num)
	{
		this.num = num;
	}

	public float getFirstwindiff()
	{
		return firstwindiff;
	}

	public void setFirstwindiff(float firstwindiff)
	{
		this.firstwindiff = firstwindiff;
	}

	public float getFirstdrawdiff()
	{
		return firstdrawdiff;
	}

	public void setFirstdrawdiff(float firstdrawdiff)
	{
		this.firstdrawdiff = firstdrawdiff;
	}

	public float getFirstlosediff()
	{
		return firstlosediff;
	}

	public void setFirstlosediff(float firstlosediff)
	{
		this.firstlosediff = firstlosediff;
	}

	public float getFirstwinstd()
	{
		return firstwinstd;
	}

	public void setFirstwinstd(float firstwinstd)
	{
		this.firstwinstd = firstwinstd;
	}

	public float getFirstdrawstd()
	{
		return firstdrawstd;
	}

	public void setFirstdrawstd(float firstdrawstd)
	{
		this.firstdrawstd = firstdrawstd;
	}

	public float getFirstlosestd()
	{
		return firstlosestd;
	}

	public void setFirstlosestd(float firstlosestd)
	{
		this.firstlosestd = firstlosestd;
	}

	public float getWindiff()
	{
		return windiff;
	}

	public void setWindiff(float windiff)
	{
		this.windiff = windiff;
	}

	public float getDrawdiff()
	{
		return drawdiff;
	}

	public void setDrawdiff(float drawdiff)
	{
		this.drawdiff = drawdiff;
	}

	public float getLosediff()
	{
		return losediff;
	}

	public void setLosediff(float losediff)
	{
		this.losediff = losediff;
	}

	public float getWinstd()
	{
		return winstd;
	}

	public void setWinstd(float winstd)
	{
		this.winstd = winstd;
	}

	public float getDrawstd()
	{
		return drawstd;
	}

	public void setDrawstd(float drawstd)
	{
		this.drawstd = drawstd;
	}

	public float getLosestd()
	{
		return losestd;
	}

	public void setLosestd(float losestd)
	{
		this.losestd = losestd;
	}

	public int getWinnum()
	{
		return winnum;
	}

	public void setWinnum(int winnum)
	{
		this.winnum = winnum;
	}

	public float getWinfirstwindiff()
	{
		return winfirstwindiff;
	}

	public void setWinfirstwindiff(float winfirstwindiff)
	{
		this.winfirstwindiff = winfirstwindiff;
	}

	public float getWinfirstdrawdiff()
	{
		return winfirstdrawdiff;
	}

	public void setWinfirstdrawdiff(float winfirstdrawdiff)
	{
		this.winfirstdrawdiff = winfirstdrawdiff;
	}

	public float getWinfirstlosediff()
	{
		return winfirstlosediff;
	}

	public void setWinfirstlosediff(float winfirstlosediff)
	{
		this.winfirstlosediff = winfirstlosediff;
	}

	public float getWinfirstwinstd()
	{
		return winfirstwinstd;
	}

	public void setWinfirstwinstd(float winfirstwinstd)
	{
		this.winfirstwinstd = winfirstwinstd;
	}

	public float getWinfirstdrawstd()
	{
		return winfirstdrawstd;
	}

	public void setWinfirstdrawstd(float winfirstdrawstd)
	{
		this.winfirstdrawstd = winfirstdrawstd;
	}

	public float getWinfirstlosestd()
	{
		return winfirstlosestd;
	}

	public void setWinfirstlosestd(float winfirstlosestd)
	{
		this.winfirstlosestd = winfirstlosestd;
	}

	public float getWinwindiff()
	{
		return winwindiff;
	}

	public void setWinwindiff(float winwindiff)
	{
		this.winwindiff = winwindiff;
	}

	public float getWindrawdiff()
	{
		return windrawdiff;
	}

	public void setWindrawdiff(float windrawdiff)
	{
		this.windrawdiff = windrawdiff;
	}

	public float getWinlosediff()
	{
		return winlosediff;
	}

	public void setWinlosediff(float winlosediff)
	{
		this.winlosediff = winlosediff;
	}

	public float getWinwinstd()
	{
		return winwinstd;
	}

	public void setWinwinstd(float winwinstd)
	{
		this.winwinstd = winwinstd;
	}

	public float getWindrawstd()
	{
		return windrawstd;
	}

	public void setWindrawstd(float windrawstd)
	{
		this.windrawstd = windrawstd;
	}

	public float getWinlosestd()
	{
		return winlosestd;
	}

	public void setWinlosestd(float winlosestd)
	{
		this.winlosestd = winlosestd;
	}

	public int getDrawnum()
	{
		return drawnum;
	}

	public void setDrawnum(int drawnum)
	{
		this.drawnum = drawnum;
	}

	public float getDrawfirstwindiff()
	{
		return drawfirstwindiff;
	}

	public void setDrawfirstwindiff(float drawfirstwindiff)
	{
		this.drawfirstwindiff = drawfirstwindiff;
	}

	public float getDrawfirstdrawdiff()
	{
		return drawfirstdrawdiff;
	}

	public void setDrawfirstdrawdiff(float drawfirstdrawdiff)
	{
		this.drawfirstdrawdiff = drawfirstdrawdiff;
	}

	public float getDrawfirstlosediff()
	{
		return drawfirstlosediff;
	}

	public void setDrawfirstlosediff(float drawfirstlosediff)
	{
		this.drawfirstlosediff = drawfirstlosediff;
	}

	public float getDrawfirstwinstd()
	{
		return drawfirstwinstd;
	}

	public void setDrawfirstwinstd(float drawfirstwinstd)
	{
		this.drawfirstwinstd = drawfirstwinstd;
	}

	public float getDrawfirstdrawstd()
	{
		return drawfirstdrawstd;
	}

	public void setDrawfirstdrawstd(float drawfirstdrawstd)
	{
		this.drawfirstdrawstd = drawfirstdrawstd;
	}

	public float getDrawfirstlosestd()
	{
		return drawfirstlosestd;
	}

	public void setDrawfirstlosestd(float drawfirstlosestd)
	{
		this.drawfirstlosestd = drawfirstlosestd;
	}

	public float getDrawwindiff()
	{
		return drawwindiff;
	}

	public void setDrawwindiff(float drawwindiff)
	{
		this.drawwindiff = drawwindiff;
	}

	public float getDrawdrawdiff()
	{
		return drawdrawdiff;
	}

	public void setDrawdrawdiff(float drawdrawdiff)
	{
		this.drawdrawdiff = drawdrawdiff;
	}

	public float getDrawlosediff()
	{
		return drawlosediff;
	}

	public void setDrawlosediff(float drawlosediff)
	{
		this.drawlosediff = drawlosediff;
	}

	public float getDrawwinstd()
	{
		return drawwinstd;
	}

	public void setDrawwinstd(float drawwinstd)
	{
		this.drawwinstd = drawwinstd;
	}

	public float getDrawdrawstd()
	{
		return drawdrawstd;
	}

	public void setDrawdrawstd(float drawdrawstd)
	{
		this.drawdrawstd = drawdrawstd;
	}

	public float getDrawlosestd()
	{
		return drawlosestd;
	}

	public void setDrawlosestd(float drawlosestd)
	{
		this.drawlosestd = drawlosestd;
	}

	public int getLosenum()
	{
		return losenum;
	}

	public void setLosenum(int losenum)
	{
		this.losenum = losenum;
	}

	public float getLosefirstwindiff()
	{
		return losefirstwindiff;
	}

	public void setLosefirstwindiff(float losefirstwindiff)
	{
		this.losefirstwindiff = losefirstwindiff;
	}

	public float getLosefirstdrawdiff()
	{
		return losefirstdrawdiff;
	}

	public void setLosefirstdrawdiff(float losefirstdrawdiff)
	{
		this.losefirstdrawdiff = losefirstdrawdiff;
	}

	public float getLosefirstlosediff()
	{
		return losefirstlosediff;
	}

	public void setLosefirstlosediff(float losefirstlosediff)
	{
		this.losefirstlosediff = losefirstlosediff;
	}

	public float getLosefirstwinstd()
	{
		return losefirstwinstd;
	}

	public void setLosefirstwinstd(float losefirstwinstd)
	{
		this.losefirstwinstd = losefirstwinstd;
	}

	public float getLosefirstdrawstd()
	{
		return losefirstdrawstd;
	}

	public void setLosefirstdrawstd(float losefirstdrawstd)
	{
		this.losefirstdrawstd = losefirstdrawstd;
	}

	public float getLosefirstlosestd()
	{
		return losefirstlosestd;
	}

	public void setLosefirstlosestd(float losefirstlosestd)
	{
		this.losefirstlosestd = losefirstlosestd;
	}

	public float getLosewindiff()
	{
		return losewindiff;
	}

	public void setLosewindiff(float losewindiff)
	{
		this.losewindiff = losewindiff;
	}

	public float getLosedrawdiff()
	{
		return losedrawdiff;
	}

	public void setLosedrawdiff(float losedrawdiff)
	{
		this.losedrawdiff = losedrawdiff;
	}

	public float getLoselosediff()
	{
		return loselosediff;
	}

	public void setLoselosediff(float loselosediff)
	{
		this.loselosediff = loselosediff;
	}

	public float getLosewinstd()
	{
		return losewinstd;
	}

	public void setLosewinstd(float losewinstd)
	{
		this.losewinstd = losewinstd;
	}

	public float getLosedrawstd()
	{
		return losedrawstd;
	}

	public void setLosedrawstd(float losedrawstd)
	{
		this.losedrawstd = losedrawstd;
	}

	public float getLoselosestd()
	{
		return loselosestd;
	}

	public void setLoselosestd(float loselosestd)
	{
		this.loselosestd = loselosestd;
	}
	
	public void setBaseOpVar(CorpOpVar var)
	{
		this.num = var.getNum();
		this.firstwindiff = var.getFirstwindiff();
		this.firstdrawdiff = var.getFirstdrawdiff();
		this.firstlosediff = var.getFirstlosediff();
		this.firstwinstd = var.getFirstwinstd();
		this.firstdrawstd = var.getFirstlosestd();
		this.firstlosestd =var.getFirstlosestd();
		this.windiff = var.getWindiff();
		this.drawdiff = var.getDrawdiff();
		this.losediff = var.getLosediff();
		this.winstd = var.getWinstd();
		this.drawstd = var.getDrawstd();
		this.losestd = var.getLosestd();
	}
	
	public CorpOpVar getBaseOpVar()
	{
		float[] vars = new float[12];
		vars[0] = this.firstwindiff;
		vars[1] = this.firstdrawdiff;
		vars[2] = this.firstlosediff;
		vars[3] = this.firstwinstd;
		vars[4] = this.firstdrawstd;
		vars[5] = this.firstlosestd;
		vars[6] = this.windiff;
		vars[7] = this.drawdiff;
		vars[8] = this.losediff;
		vars[9] = this.winstd;
		vars[10] = this.drawstd;
		vars[11] = this.losestd;
		return new CorpOpVar(num, vars);
	}
	
	public void setWinOpVar(CorpOpVar var)
	{
		this.winnum = var.getNum();
		this.winfirstwindiff = var.getFirstwindiff();
		this.winfirstdrawdiff = var.getFirstdrawdiff();
		this.winfirstlosediff = var.getFirstlosediff();
		this.winfirstwinstd = var.getFirstwinstd();
		this.winfirstdrawstd = var.getFirstlosestd();
		this.winfirstlosestd =var.getFirstlosestd();
		this.winwindiff = var.getWindiff();
		this.windrawdiff = var.getDrawdiff();
		this.winlosediff = var.getLosediff();
		this.winwinstd = var.getWinstd();
		this.windrawstd = var.getDrawstd();
		this.winlosestd = var.getLosestd();
	}
	
	public CorpOpVar getWinOpVar()
	{
		float[] vars = new float[12];
		vars[0] = this.winfirstwindiff;
		vars[1] = this.winfirstdrawdiff;
		vars[2] = this.winfirstlosediff;
		vars[3] = this.winfirstwinstd;
		vars[4] = this.winfirstdrawstd;
		vars[5] = this.winfirstlosestd;
		vars[6] = this.winwindiff;
		vars[7] = this.windrawdiff;
		vars[8] = this.winlosediff;
		vars[9] = this.winwinstd;
		vars[10] = this.windrawstd;
		vars[11] = this.winlosestd;
		return new CorpOpVar(winnum, vars);
	}
	
	public void setDrawOpVar(CorpOpVar var)
	{
		this.drawnum = var.getNum();
		this.drawfirstwindiff = var.getFirstwindiff();
		this.drawfirstdrawdiff = var.getFirstdrawdiff();
		this.drawfirstlosediff = var.getFirstlosediff();
		this.drawfirstwinstd = var.getFirstwinstd();
		this.drawfirstdrawstd = var.getFirstlosestd();
		this.drawfirstlosestd =var.getFirstlosestd();
		this.drawwindiff = var.getWindiff();
		this.drawdrawdiff = var.getDrawdiff();
		this.drawlosediff = var.getLosediff();
		this.drawwinstd = var.getWinstd();
		this.drawdrawstd = var.getDrawstd();
		this.drawlosestd = var.getLosestd();
	}
	
	public CorpOpVar getDrawOpVar()
	{
		float[] vars = new float[12];
		vars[0] = this.drawfirstwindiff;
		vars[1] = this.drawfirstdrawdiff;
		vars[2] = this.drawfirstlosediff;
		vars[3] = this.drawfirstwinstd;
		vars[4] = this.drawfirstdrawstd;
		vars[5] = this.drawfirstlosestd;
		vars[6] = this.drawwindiff;
		vars[7] = this.drawdrawdiff;
		vars[8] = this.drawlosediff;
		vars[9] = this.drawwinstd;
		vars[10] = this.drawdrawstd;
		vars[11] = this.drawlosestd;
		return new CorpOpVar(drawnum, vars);
	}
	
	public void setLoseOpVar(CorpOpVar var)
	{
		this.losenum = var.getNum();
		this.losefirstwindiff = var.getFirstwindiff();
		this.losefirstdrawdiff = var.getFirstdrawdiff();
		this.losefirstlosediff = var.getFirstlosediff();
		this.losefirstwinstd = var.getFirstwinstd();
		this.losefirstdrawstd = var.getFirstlosestd();
		this.losefirstlosestd =var.getFirstlosestd();
		this.losewindiff = var.getWindiff();
		this.losedrawdiff = var.getDrawdiff();
		this.loselosediff = var.getLosediff();
		this.losewinstd = var.getWinstd();
		this.losedrawstd = var.getDrawstd();
		this.loselosestd = var.getLosestd();
	}
	
	public CorpOpVar getLoseOpVar()
	{
		float[] vars = new float[12];
		vars[0] = this.losefirstwindiff;
		vars[1] = this.losefirstdrawdiff;
		vars[2] = this.losefirstlosediff;
		vars[3] = this.losefirstwinstd;
		vars[4] = this.losefirstdrawstd;
		vars[5] = this.losefirstlosestd;
		vars[6] = this.losewindiff;
		vars[7] = this.losedrawdiff;
		vars[8] = this.loselosediff;
		vars[9] = this.losewinstd;
		vars[10] = this.losedrawstd;
		vars[11] = this.loselosestd;
		return new CorpOpVar(losenum, vars);
	}

	@Override
	public String toString()
	{
		return "CorpStatItem [" + gid + ", " + name + ", BaseVar=" + getBaseOpVar() + ", WinVar="
				+ getWinOpVar() + ", DrawVar=" + getDrawOpVar() + ", LoseVar=" + getLoseOpVar() + "]";
	}
}
