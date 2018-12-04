package com.loris.soccer.analysis.stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.context.LorisContext;
import com.loris.soccer.bean.data.table.Corporate;
import com.loris.soccer.bean.data.table.Match;
import com.loris.soccer.bean.data.table.Op;
import com.loris.soccer.bean.item.CorpStatItem;
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
		
		List<Match> existOpMatches = new ArrayList<>();
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
			
			existOpMatches.add(match);
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
		
		//计算平均值
		for (CorpStat corpStat : corpStatList)
		{
			corpStat.computeMean();
		}
		
		//第二次计算，计算方差值
		int len = existOpMatches.size();
		for (int j = 0; j < len; j ++)
		{
			Match match = existOpMatches.get(j);
			logger.info("Compute " + (j++) + " of " + existOpMatches.size() + " Match: " + match);
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
								
				stat.addVar(match.getScoreResult(), op, avgOp);
			}
		}
		
		//File file = new File("d:/index/stat.txt");
		//try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file))))
		{
			i = 1;
			for (CorpStat corpStat : corpStatList)
			{
				corpStat.computeStdErr();
				CorpStatItem item = corpStat.createCorpStatItem();
				
				logger.info(i ++ + ": " + item);
				//writer.write(i++ + ": " + item + "\r\n");
				soccerManager.addOrUpdateCorpStatItem(item);
			}
			//writer.flush();
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
	
	CorpOpVar baseVar = new CorpOpVar();
	CorpOpVar winVar = new CorpOpVar();
	CorpOpVar drawVar = new CorpOpVar();
	CorpOpVar loseVar = new CorpOpVar();
	
	/*
	Deviation deviation = new Deviation();
	Deviation firstDeviation = new Deviation();
	
	ResultStat winResult = new ResultStat(3);
	ResultStat drawResult = new ResultStat(1);
	ResultStat loseResult = new ResultStat(0);*/
	
	public CorpStat()
	{
	}
	
	public CorpStat(Corporate corp)
	{
		this.corp = corp;
	}
	
	public void addVar(ScoreItem item, Op op, Op avgOp)
	{
		float winDiff;
		float drawDiff;
		float loseDiff;
		float fwinDiff;
		float fdrawDiff;
		float floseDiff;
		
		fwinDiff = op.getFirstwinodds() - avgOp.getFirstwinodds();
		fdrawDiff =	op.getFirstdrawodds() - avgOp.getFirstdrawodds();
		floseDiff = op.getFirstloseodds() - avgOp.getFirstloseodds();
		
		winDiff = op.getWinodds() - avgOp.getWinodds();
		drawDiff = op.getDrawodds() - avgOp.getDrawodds();
		loseDiff = op.getLoseodds() - avgOp.getLoseodds();
		
		fwinDiff = fwinDiff - baseVar.getFirstwindiff();
		fdrawDiff = fdrawDiff - baseVar.getFirstdrawdiff();
		floseDiff = floseDiff - baseVar.getFirstlosediff();
		winDiff = winDiff - baseVar.getWindiff();
		drawDiff = drawDiff - baseVar.getDrawdiff();
		loseDiff = loseDiff - baseVar.getLosediff();
		
		baseVar.add(0, 0, 0, fwinDiff * fwinDiff, fdrawDiff * fdrawDiff, floseDiff * floseDiff,
				0, 0, 0, winDiff * winDiff, drawDiff * drawDiff, loseDiff * loseDiff);
		int result = item.getResult();		
		CorpOpVar var;
		switch (result) {
		case 3:
			var = winVar;
			break;
		case 1:
			var = drawVar;
			break;
		default:
			var = loseVar;
			break;
		}
		
		var.add(0, 0, 0, fwinDiff * fwinDiff, fdrawDiff * fdrawDiff, floseDiff * floseDiff,
				0, 0, 0, winDiff * winDiff, drawDiff * drawDiff, loseDiff * loseDiff);
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
		
		fwinDiff = op.getFirstwinodds() - avgOp.getFirstwinodds();
		fdrawDiff =	op.getFirstdrawodds() - avgOp.getFirstdrawodds();
		floseDiff = op.getFirstloseodds() - avgOp.getFirstloseodds();
		
		winDiff = op.getWinodds() - avgOp.getWinodds();
		drawDiff = op.getDrawodds() - avgOp.getDrawodds();
		loseDiff = op.getLoseodds() - avgOp.getLoseodds();
		
		baseVar.add(fwinDiff, fdrawDiff, floseDiff, 0, 0, 0,
				winDiff, drawDiff, loseDiff, 0, 0, 0);
		baseVar.addNum();
		
		int result = item.getResult();		
		CorpOpVar var;
		switch (result) {
		case 3:
			var = winVar;
			break;
		case 1:
			var = drawVar;
			break;
		default:
			var = loseVar;
			break;
		}
		
		var.add(fwinDiff, fdrawDiff, floseDiff, 0, 0, 0,
				winDiff, drawDiff, loseDiff, 0, 0, 0);
		var.addNum();
		//var.add(fwinDiff, fdrawDiff, floseDiff, fwinDiff * fwinDiff, fdrawDiff * fdrawDiff, floseDiff * floseDiff,
		//winDiff, drawDiff, loseDiff, winDiff * winDiff, drawDiff * drawDiff, loseDiff * loseDiff);

		matchNum ++;
	}
	
	public void computeMean()
	{
		baseVar.computeMean();
		winVar.computeMean();
		drawVar.computeMean();
		loseVar.computeMean();
	}
	
	public void computeStdErr()
	{
		baseVar.computeStdErr();
		winVar.computeStdErr();
		drawVar.computeStdErr();
		loseVar.computeStdErr();
	}
	
	public Corporate getCorp()
	{
		return corp;
	}
	public void setCorp(Corporate corp)
	{
		this.corp = corp;
	}
	
	public CorpStatItem createCorpStatItem()
	{
		CorpStatItem item = new CorpStatItem(corp.getGid(), corp.getName());
		item.setBaseOpVar(baseVar);
		item.setWinOpVar(winVar);
		item.setDrawOpVar(drawVar);
		item.setLoseOpVar(loseVar);
		return item;
	}
}