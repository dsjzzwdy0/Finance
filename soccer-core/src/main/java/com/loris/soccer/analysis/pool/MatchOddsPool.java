package com.loris.soccer.analysis.pool;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.ArraysUtil;
import com.loris.soccer.analysis.data.MatchOpVariance;
import com.loris.soccer.analysis.data.MatchOdds;
import com.loris.soccer.analysis.util.IssueMatchUtil;
import com.loris.soccer.analysis.util.OddsUtil;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.data.table.odds.Op;
import com.loris.soccer.bean.item.IssueMatch;
import com.loris.soccer.bean.item.MatchItem;
import com.loris.soccer.repository.SoccerManager;

public class MatchOddsPool
{	
	protected static Logger logger = Logger.getLogger(MatchOddsPool.class);
	
	/** 比赛检测器Checker class */
	static class MatchEqualChecker implements ArraysUtil.EqualChecker<MatchItem>
	{
		String mid;
		
		public void setMid(String mid)
		{
			this.mid = mid;
		}
		
		@Override
		public boolean isSameObject(MatchItem obj)
		{
			return mid.equals(obj.getMid());
		}
		
	}
	
	/** 公司检测器 */
	static class GidEqualChecker implements ArraysUtil.EqualChecker<Op>
	{
		protected String gid;
		
		public void setGid(String gid)
		{
			this.gid = gid;
		}
		
		@Override
		public boolean isSameObject(Op obj)
		{
			if(obj.getGid().equals(gid))
				return true;
			return false;
		}		
	}
	
	/** The checker. */
	static MatchEqualChecker checker = new MatchEqualChecker();
	
	/**
	 * 计算竞彩比赛的欧赔方差值
	 * @param issue 比赛期号
	 * @return 方差值数据
	 */
	public static List<MatchOpVariance> computeJcMatchsOpVariance(String issue)
	{
		String start = IssueMatchUtil.getStartTime(issue);
		String end = IssueMatchUtil.getEndTime(issue);
		return computeOpVariance(start, end, SoccerConstants.LOTTERY_JC);
	}
	
	/**
	 * 计算竞彩比赛的欧赔方差值
	 * @param issue 比赛期号
	 * @return 方差值数据
	 */
	public static List<MatchOpVariance> computeBdMatchsOpVariance(String issue)
	{
		String start = IssueMatchUtil.getStartTime(issue);
		String end = IssueMatchUtil.getEndTime(issue);
		return computeOpVariance(start, end, SoccerConstants.LOTTERY_BD);
	}
	
	/**
	 * 计算某一场比赛的欧赔数据值
	 * @param mid 比赛编号
	 * @return 方差计算值
	 */
	public static MatchOpVariance computeMatchOpVariance(String mid)
	{
		if(StringUtils.isEmpty(mid))
		{
			return null;
		}
		SoccerManager soccerManager = SoccerManager.getInstance();
		IssueMatch match = soccerManager.getIssueMatch(mid);
		if(match == null)
		{
			return null;
		}
		List<Op> ops = soccerManager.getOpListOrderByTime(mid, true);
		List<Op> cleanOps = new ArrayList<>();
		cleanSameComporateOps(ops, cleanOps);
		
		//被清理干净后，不进行计算
		if(cleanOps.size() == 0)
		{
			return null;
		}
		return OddsUtil.computeIssueMatchOpVariance(match, cleanOps);
	}
	
	/**
	 * 清除同一公司的多个数据记录值
	 * @param source
	 * @param dest
	 */
	public static void cleanSameComporateOps(List<Op> source, List<Op> dest)
	{
		GidEqualChecker checker = new GidEqualChecker();
		for (Op op : source)
		{
			checker.gid = op.getGid();
			Op op1 = ArraysUtil.getSameObject(dest, checker);
			if(op1 == null)
			{
				dest.add(op);
			}
			else
			{
				if(op.getLastTimeValue() > op1.getLastTimeValue())
				{
					dest.remove(op1);
					dest.add(op);
				}
			}
		}
	}
	
	/**
	 * 计算欧赔数据的方差值
	 * @param start 开始日期
	 * @param end 结束日期
	 * @return 方差值列表
	 */
	public static List<MatchOpVariance> computeOpVariance(String start, String end, String type)
	{
		SoccerManager soccerManager = SoccerManager.getInstance();		
		List<? extends IssueMatch> issueMatchs;
		
		if(type.equalsIgnoreCase(SoccerConstants.LOTTERY_JC))
		{
			issueMatchs = soccerManager.getJcMatchesByDate(start, end);
		}
		else
		{
			issueMatchs = soccerManager.getBdMatchByMatchtime(start, end);
		}
		//logger.info("JcMatchs size is " + jcMatchs.size());
		return computeIssueMatchsOpVariance(issueMatchs, start, end);
	}
	
	/**
	 * 计算竞彩比赛的方差数据值
	 * @param matches 竞彩比赛
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return 比赛方差数据
	 */
	protected static List<MatchOpVariance> computeIssueMatchsOpVariance(List<? extends IssueMatch> matches, String start, String end)
	{
		SoccerManager soccerManager = SoccerManager.getInstance();
		
		/*List<MatchInfo> infos = soccerManager.getMatchInfoByDate(start, end);
		if(infos == null || infos.size() <= 0)
		{
			logger.info("There are no MatchInfo in the database of the time from '" + 
					start + "' to '" + end + "'");
			return null;
		}*/
		
		List<String> mids = new ArrayList<>();		
		ArraysUtil.getObjectFieldValue(matches, mids, IssueMatch.class, "mid");		
		//logger.info("match size: " + mids.size());
		
		List<MatchOdds> matchOps = new ArrayList<>();
		for (IssueMatch match : matches)
		{
			//checker.setMid(match.getMid());
			//MatchInfo matchInfo = ArraysUtil.getSameObject(infos, checker);
			MatchOdds ops = new MatchOdds(match);
			matchOps.add(ops);
		}

		logger.info("There are " + matchOps.size() + " op variances in the database.");		
		List<Op> ops = soccerManager.getOddsOp(mids, true);	
		//logger.info("There are total " + ops.size() + " op records.");
		
		assignOpValues(matchOps, ops);		
		List<MatchOpVariance> variances = new ArrayList<>();
		
		for (IssueMatch match : matches)
		{
			checker.setMid(match.getMid());
			MatchOdds ops2 =(MatchOdds) ArraysUtil.getSameObject(matchOps, checker);
			if(ops2 == null)
			{
				continue;
			}
			MatchOpVariance item = OddsUtil.computeIssueMatchOpVariance(match, ops2.getOps());			
			variances.add(item);
		}
		
		return variances;
	}
	
	/**
	 * 按照比赛的编号分配欧赔数据
	 * @param vars 欧赔数据的方差值
	 * @param ops 欧赔数据
	 */
	public static void assignOpValues(List<MatchOdds> matchOps, List<Op> ops)
	{
		//int i = 0;
		for (Op op : ops)
		{
			for (MatchOdds var : matchOps)
			{
				if(var.getMid().equals(op.getMid()))
				{
					var.addOp(op);
				}
			}
		}
		
		//logger.info("There are total " + i + " op records has been assigned.");
	}
}


