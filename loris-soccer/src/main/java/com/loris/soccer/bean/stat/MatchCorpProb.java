package com.loris.soccer.bean.stat;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.entity.AutoIdEntity;

/**
 * 按照博彩公司统计的数据
 * 
 * @author jiean
 *
 */
@TableName("soccer_corp_match_prob")
public class MatchCorpProb extends AutoIdEntity
{
	/***/
	private static final long serialVersionUID = 1L;
	protected String mid;
	protected String gid;
	protected String name;
	protected String type;
	protected int weight;
	protected double winprob;
	protected double drawprob;
	protected double loseprob;

	public MatchCorpProb()
	{
	}

	public MatchCorpProb(String mid)
	{
		this.mid = mid;
	}

	public String getMid()
	{
		return mid;
	}

	public void setMid(String mid)
	{
		this.mid = mid;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public double getWinprob()
	{
		return winprob;
	}

	public void setWinprob(double winProb)
	{
		this.winprob = winProb;
	}

	public double getDrawprob()
	{
		return drawprob;
	}

	public void setDrawprob(double drawProb)
	{
		this.drawprob = drawProb;
	}

	public double getLoseprob()
	{
		return loseprob;
	}

	public void setLoseprob(double loseProb)
	{
		this.loseprob = loseProb;
	}

	public void setProb(double winprob, double drawprob, double loseprob)
	{
		this.winprob = winprob;
		this.drawprob = drawprob;
		this.loseprob = loseprob;
	}

	public int getResult()
	{
		if (winprob > drawprob && winprob > loseprob)
		{
			return 3;
		}
		else if (drawprob > loseprob)
		{
			return 1;
		}
		else
		{
			return 0;
		}
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

	public int getWeight()
	{
		return weight;
	}

	public void setWeight(int weight)
	{
		this.weight = weight;
	}
	
	public boolean isSignificance()
	{
		return isSignificance(0.03);
	}
	
	public boolean isSignificance(double threshold)
	{
		return Math.abs(winprob - drawprob) > threshold ||
				Math.abs(winprob - loseprob) > threshold ||
				Math.abs(drawprob - loseprob) > threshold;
	}

	@Override
	public String toString()
	{
		return "MatchCorpProb [result=" + getResult() + ", mid=" + mid + ", gid=" + gid + ", name=" + name + ", weight="
				+ weight + ", winProb=" + winprob + ", drawProb=" + drawprob + ", loseProb=" + loseprob + "]";
	}
}
