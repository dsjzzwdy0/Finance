package com.loris.soccer.bean.item;

import com.loris.base.bean.entity.AutoIdEntity;

/**
 * 球队最近几场比赛的状态分析，状态以胜、平、负的场次来进行统计
 * 
 * @author jiean
 *
 */
public class PerformItem extends AutoIdEntity
{
	/***/
	private static final long serialVersionUID = 1L;
	
	protected String tid;				//球队编号
	protected String name;				//球队名称	
	protected int gamenum;              //赛场数
	protected int winnum;               //胜场数
	protected int drawnum;  			//平场数
	protected int losenum; 				//负场数
	protected int score;                //得分
	protected int goal;					//总进球数
	protected int losegoal;             //总失球数
	protected int diffgoal; 			//净胜球数
	//private double point;				//综合评分
	//protected String description;		//状态说明
	
	/**
	 * 创建一个实体
	 */
	public PerformItem()
	{
	}
	
	/**
	 * 创建一个战绩实体
	 * @param tid
	 */
	public PerformItem(String tid)
	{
		this.tid = tid;
	}
	
	public String getTid()
	{
		return tid;
	}
	public void setTid(String tid)
	{
		this.tid = tid;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getGamenum()
	{
		return gamenum;
	}
	public void setGamenum(int gamenum)
	{
		this.gamenum = gamenum;
	}
	public int getWinnum()
	{
		return winnum;
	}
	public void setWinnum(int winnum)
	{
		this.winnum = winnum;
	}
	public int getDrawnum()
	{
		return drawnum;
	}
	public void setDrawnum(int drawnum)
	{
		this.drawnum = drawnum;
	}
	public int getLosenum()
	{
		return losenum;
	}
	public void setLosenum(int losenum)
	{
		this.losenum = losenum;
	}
	public int getScore()
	{
		return score;
	}
	public void setScore(int score)
	{
		this.score = score;
	}
	public int getGoal()
	{
		return goal;
	}
	public void setGoal(int goal)
	{
		this.goal = goal;
	}
	public int getLosegoal()
	{
		return losegoal;
	}
	public void setLosegoal(int losegoal)
	{
		this.losegoal = losegoal;
	}
	public int getDiffgoal()
	{
		return diffgoal;
	}
	public void setDiffgoal(int diffgoal)
	{
		this.diffgoal = diffgoal;
	}
	
	public void addGoal(int goal)
	{
		this.goal += goal;
	}
	
	public void addLoseGoal(int goal)
	{
		this.losegoal += goal;
	}
	
	public void incWinGame()
	{
		winnum ++;
		gamenum ++;
	}
	
	public void incDrawGame()
	{
		drawnum ++;
		gamenum ++;
	}
	
	public void incLoseGame()
	{
		losenum ++;
		gamenum ++;
	}
	
	public void addWinGame(int num)
	{
		winnum += num;
		gamenum += num;
	}
	
	public void addDrawGame(int num)
	{
		drawnum += num;
		gamenum += num;
	}
	
	public void addLoseGame(int num)
	{
		losenum += num;
		gamenum += num;
	}
	
	public int getGoalDiff()
	{
		return goal - losegoal;
	}
	
	public void addScore(int score)
	{
		this.score += score;
	}
	
	/**
	 * 检测是否某一个球队
	 * 
	 * @param team 球队
	 * @return 是否标志
	 */
	public boolean isTeam(String team)
	{
		return tid.equalsIgnoreCase(team);
	}

	@Override
	public String toString()
	{
		return "Performance [tid=" + tid + ", name=" + name + ", gamenum=" + gamenum + ", winnum=" + winnum
		        + ", drawnum=" + drawnum + ", losenum=" + losenum + ", score=" + score + ", goal=" + goal
		        + ", losegoal=" + losegoal + ", diffgoal=" + getGoalDiff() + "]";
	}
}
