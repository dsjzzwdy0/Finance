package com.loris.soccer.bean.item;

public class ScoreItem
{
	private String score;
	private int wingoal;
	private int losegoal;
	private int result;
	
	/**
	 * 比赛结果
	 * 
	 * @param score
	 */
	public ScoreItem(String score)
	{
		this.score = score;
		String[] str = score.split(":");
		
		if(str.length != 2)
		{
			result = -1;
			return;
		}
		try
		{
			wingoal = Integer.parseInt(str[0]);
			losegoal = Integer.parseInt(str[1]);
			if(wingoal > losegoal)
			{
				result = 3;
			}
			else if(wingoal == losegoal)
			{
				result = 1;
			}
			else
			{
				result = 0;
			}
		}
		catch(Exception e)
		{
			result = -1;
		}
	}
	
	public String getScore()
	{
		return score;
	}
	public void setScore(String score)
	{
		this.score = score;
	}
	public int getWingoal()
	{
		return wingoal;
	}
	public void setWingoal(int wingoal)
	{
		this.wingoal = wingoal;
	}
	public int getLosegoal()
	{
		return losegoal;
	}
	public void setLosegoal(int losegoal)
	{
		this.losegoal = losegoal;
	}
	public int getResult()
	{
		return result;
	}
	public void setResult(int result)
	{
		this.result = result;
	}
	@Override
	public String toString()
	{
		return "MatchResult [score=" + score + ", wingoal=" + wingoal + ", losegoal=" + losegoal + ", result=" + result
				+ "]";
	}
}
