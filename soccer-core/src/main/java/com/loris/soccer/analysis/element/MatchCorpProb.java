package com.loris.soccer.analysis.element;

public class MatchCorpProb
{
	protected String mid;
	protected String gid;
	protected String name;
	protected String type;
	protected int weight;
	protected double winProb;
	protected double drawProb;
	protected double loseProb;
	
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
	public double getWinProb()
	{
		return winProb;
	}
	public void setWinProb(double winProb)
	{
		this.winProb = winProb;
	}
	public double getDrawProb()
	{
		return drawProb;
	}
	public void setDrawProb(double drawProb)
	{
		this.drawProb = drawProb;
	}
	public double getLoseProb()
	{
		return loseProb;
	}
	public void setLoseProb(double loseProb)
	{
		this.loseProb = loseProb;
	}
	
	public void setProb(double winprob, double drawprob, double loseprob)
	{
		this.winProb = winprob;
		this.drawProb = drawprob;
		this.loseProb = loseprob;
	}
	
	public int getResult()
	{
		if(winProb > drawProb && winProb > loseProb)
		{
			return 3;
		}
		else if(drawProb > loseProb)
		{
			return 1;
		}
		else {
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

	@Override
	public String toString()
	{
		return "MatchCorpProb [result=" + getResult() + ", mid=" + mid + ", gid=" + gid 
				+ ", name=" + name + ", weight=" + weight + ", winProb=" + winProb 
				+ ", drawProb=" + drawProb + ", loseProb=" + loseProb + "]";
	}
}
