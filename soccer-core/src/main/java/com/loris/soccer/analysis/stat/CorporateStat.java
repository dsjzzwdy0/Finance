package com.loris.soccer.analysis.stat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.context.LorisContext;
import com.loris.base.util.NumberUtil;
import com.loris.soccer.bean.data.table.league.Match;
import com.loris.soccer.bean.data.table.lottery.Corporate;
import com.loris.soccer.bean.data.table.odds.Op;
import com.loris.soccer.bean.item.ScoreItem;
import com.loris.soccer.bean.model.OpList;
import com.loris.soccer.repository.SoccerManager;

/**
 * 博彩公司数据统计类
 *
 * @author jiean
 *
 */
public class CorporateStat
{
	private static Logger logger = Logger.getLogger(CorporateStat.class);
	
	static SoccerManager soccerManager;
	
	/**
	 * 设置管理器
	 * @param manager
	 */
	public static void initialize(LorisContext context) 
	{
		soccerManager = context.getBean(SoccerManager.class);
	}
	
	/**
	 * 设置管理器
	 * @param manager
	 */
	public static void initialize(SoccerManager manager)
	{
		soccerManager = manager;
	}
	
	/**
	 * 计算统计值
	 * @param start
	 * @param end
	 */
	public static void computeStat(String start, String end) throws IOException
	{
		CorpStatList corpStatList = new CorpStatList();
		
		List<Match> matches = soccerManager.getMatches(start, end);
		logger.info("Total match is : " + matches.size());
		int i = 1;
		for (Match match : matches)
		{
			logger.info("Compute " + (i++) + " of " + matches.size() + " Match: " + match);
			ScoreItem item = match.getScoreResult();
			if(item.getResult() < 0)
			{
				logger.info("");
			}

			List<Op> ops = soccerManager.getOpList(match.getMid(), true);
			if(ops == null || ops.isEmpty())
			{
				logger.info("There are no op record of '" + match.getMid() + " in database, next.");
				continue;
			}
			
			OpList list = new OpList(ops);
			Op avgOp = list.getAvgOp();
			if(avgOp == null)
			{
				logger.info("There are no average op record of '" + match.getMid() + " in database, next.");
				continue;
			}
			for (Op op : list)
			{
				//平均欧赔值
				if("0".equals(op.getGid()))
				{
					continue;
				}
				CorpStat stat = corpStatList.getCorporateStat(op.getCorporate());
				if(stat == null)
				{
					continue;
				}
								
				stat.add(item, op, avgOp);
			}
		}
		
		File file = new File("d:/index/stat.txt");
		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file))))
		{
			i = 1;
			for (CorpStat corpStat : corpStatList)
			{
				corpStat.computeMean();
				logger.info(i +++ ": " + corpStat);
				writer.write(i + ": " + corpStat + "\r\n");
			}
			writer.flush();
		}		
	}
}

class CorpStatList extends ArrayList<CorpStat>
{
	/***/
	private static final long serialVersionUID = 1L;

	public CorpStat getCorporateStat(Corporate corp)
	{
		for (CorpStat stat : this)
		{
			if(stat.getCorp().getGid().equals(corp.getGid()))
			{
				return stat;
			}
		}
		CorpStat corporate = new CorpStat(corp);
		add(corporate);
		return corporate;
	}
}

class CorpStat
{
	Corporate corp;
	
	int matchNum = 0;
	Deviation deviation = new Deviation();
	Deviation firstDeviation = new Deviation();
	
	ResultStat winResult = new ResultStat(3);
	ResultStat drawResult = new ResultStat(1);
	ResultStat loseResult = new ResultStat(0);
	
	public CorpStat()
	{
	}
	
	public CorpStat(Corporate corp)
	{
		this.corp = corp;
	}
	
	/**
	 * 添加一个数据记录
	 * @param match
	 * @param op
	 * @param avgOp
	 */
	public void add(ScoreItem item, Op op, Op avgOp)
	{
		float winDiff;
		float drawDiff;
		float loseDiff;
		float fwinDiff;
		float fdrawDiff;
		float floseDiff;
		
		winDiff = op.getWinodds() - avgOp.getWinodds();
		drawDiff = op.getDrawodds() - avgOp.getDrawodds();
		loseDiff = op.getLoseodds() - avgOp.getLoseodds();
		fwinDiff = op.getFirstwinodds() - avgOp.getFirstwinodds();
		fdrawDiff =	op.getFirstdrawodds() - avgOp.getFirstdrawodds();
		floseDiff = op.getFirstloseodds() - avgOp.getFirstloseodds();
		
		deviation.add(winDiff, drawDiff, loseDiff);
		firstDeviation.add(fwinDiff, fdrawDiff, floseDiff);
		
		int result = item.getResult();
		
		ResultStat stat;
		switch (result) {
		case 3:
			stat = winResult;
			break;
		case 1:
			stat = drawResult;
			break;
		default:
			stat = loseResult;
			break;
		}
		
		stat.add(winDiff, drawDiff, loseDiff, fwinDiff, fdrawDiff, floseDiff);		
		matchNum ++;
	}
	
	public void computeMean()
	{
		deviation.computeMean();
		firstDeviation.computeMean();
		winResult.computeMean();
		drawResult.computeMean();
		loseResult.computeMean();
	}
	
	public Corporate getCorp()
	{
		return corp;
	}
	public void setCorp(Corporate corp)
	{
		this.corp = corp;
	}
	public Deviation getDeviation()
	{
		return deviation;
	}
	public void setDeviation(Deviation deviation)
	{
		this.deviation = deviation;
	}
	public ResultStat getWinResult()
	{
		return winResult;
	}
	public void setWinResult(ResultStat winResult)
	{
		this.winResult = winResult;
	}
	public ResultStat getDrawResult()
	{
		return drawResult;
	}
	public void setDrawResult(ResultStat drawResult)
	{
		this.drawResult = drawResult;
	}
	public ResultStat getLoseResult()
	{
		return loseResult;
	}
	public void setLoseResult(ResultStat loseResult)
	{
		this.loseResult = loseResult;
	}
	@Override
	public String toString()
	{
		return "CorpStat [初=" + firstDeviation + ", 即=" + deviation 
			+ winResult + ", " + drawResult + ", " + loseResult 
			+ "corp=" + corp.getGid() + ": " + corp.getName() + "]";
	}
}

/**
 * 与百家欧赔平均值的偏离值
 * @author jiean
 *
 */
class Deviation
{
	int num;	
	float win;
	float draw;
	float lose;
	
	public int getNum()
	{
		return num;
	}
	public void setNum(int num)
	{
		this.num = num;
	}
	public float getWin()
	{
		return win;
	}
	public void setWin(float win)
	{
		this.win = win;
	}
	public float getDraw()
	{
		return draw;
	}
	public void setDraw(float draw)
	{
		this.draw = draw;
	}
	public float getLose()
	{
		return lose;
	}
	public void setLose(float lose)
	{
		this.lose = lose;
	}
	
	public void add(float win, float draw, float lose)
	{
		this.win += win;
		this.draw += draw;
		this.lose += lose;
		num ++;
	}
	
	public void computeMean()
	{
		if(num == 0)
		{
			return;
		}
		win /= num;
		draw /= num;
		lose /= num;
	}
	
	@Override
	public String toString()
	{
		return "Dev[" + num + ", " + NumberUtil.formatDouble(2, win) + "," 
				+  NumberUtil.formatDouble(2, draw) + ", " 
				+ NumberUtil.formatDouble(2, lose) + "]";
	}
	
}

class ResultStat
{
	int result;
	int num;
	Deviation deviation = new Deviation();
	Deviation firstDeviation = new Deviation();
	
	public ResultStat()
	{
		num = 0;
	}
	
	public ResultStat(int result)
	{
		this();
		this.result = result;
	}
	
	public void add(float win, float draw, float lose, float fwin, float fdraw, float flose)
	{
		num ++;
		deviation.add(win, draw, lose);
		firstDeviation.add(fwin, fdraw, flose);
	}
	
	public int getNum()
	{
		return num;
	}

	public void setNum(int num)
	{
		this.num = num;
	}

	public int getResult()
	{
		return result;
	}
	public void setResult(int result)
	{
		this.result = result;
	}
	public Deviation getDeviation()
	{
		return deviation;
	}
	public void setDeviation(Deviation deviation)
	{
		this.deviation = deviation;
	}
	public Deviation getFirstDeviation()
	{
		return firstDeviation;
	}
	public void setFirstDeviation(Deviation firstDeviation)
	{
		this.firstDeviation = firstDeviation;
	}
	
	public void computeMean()
	{
		deviation.computeMean();
		firstDeviation.computeMean();
	}
	@Override
	public String toString()
	{
		String r = result == 3 ? "胜" : result == 1 ? "平" : "负";
		return r + " Stat[" + num + ", 初=" + firstDeviation + ", 即=" + deviation + "]";
	}
}