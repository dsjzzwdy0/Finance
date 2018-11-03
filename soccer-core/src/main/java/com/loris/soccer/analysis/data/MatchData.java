package com.loris.soccer.analysis.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.loris.soccer.analysis.util.PerformanceUtil;
import com.loris.soccer.bean.data.table.league.Match;
import com.loris.soccer.bean.data.table.odds.Op;
import com.loris.soccer.bean.data.table.odds.Yp;
import com.loris.soccer.bean.element.MatchSynthElement;
import com.loris.soccer.bean.item.IssueMatch;
import com.loris.soccer.bean.item.PerformItem;
import com.loris.soccer.bean.type.MatchTeamType;

/**
 * 竞彩比赛数据集，以竞彩数据为基础，包含的数据有：<br>
 * 1、平均欧赔<br>
 * 2、平均亚盘<br>
 * 3、欧赔数据<br>
 * 4、亚盘数据<br>
 * 5、主队战绩<br>
 * 6、客队战绩<br>
 * 
 * @author jiean
 *
 */
public class MatchData extends IssueMatch
{
	private static Logger logger = Logger.getLogger(MatchData.class);
	
	/***/
	private static final long serialVersionUID = 1L;

	//protected Performance homeState;			//比赛状态分析
	//protected Performance clientSate;			//客队状态分析
	protected List<Op> opList;					//欧赔数据
	protected List<Yp> ypList;					//亚盘数据
	protected List<String> mids;				//相关联的比赛数据
	
	protected List<Match> homeHistories;		//主队历史战绩
	protected List<Match> clientHistories; 		//客队历史战绩
	
	/**
	 * Create a new instance of MatchResult.
	 */
	protected MatchData()
	{
		opList = new ArrayList<>();
		ypList = new ArrayList<>();
		homeHistories = new ArrayList<>();
		clientHistories = new ArrayList<>();
		mids = new ArrayList<>();
	}
	
	/**
	 * 
	 * @param match
	 */
	public MatchData(IssueMatch match)
	{
		this();
		setIssueMatch(match);
	}
	

	/**
	 * 数据列表
	 * @return
	 */
	public MatchSynthElement createIssueMatchSynthElement()
	{
		MatchOdds odds = new MatchOdds(this);	
		
		//添加OP记录数据
		for (Op op : opList)
		{
			//OddsElement opItem = OddsUtil.createOpItem(op, 0);
			//odds.addOpItem(opItem);
			odds.addOp(op);
		}
		
		for (Yp yp : ypList)
		{
			odds.addYp(yp);
			//OddsElement ypItem = OddsUtil.createYpItem(yp, 0);
			//odds.addYpItem(ypItem);
		}
		

		MatchSynthElement item = new MatchSynthElement(odds);	
		//战绩数据
		try
		{
			PerformItem hperf = getHomeTeamPerformance(MatchTeamType.ALL, 6);
			item.setHomePerf(hperf);
		}
		catch(Exception e)
		{
			logger.info(e.toString());
		}
		
		try
		{
			PerformItem cperf = getClientTeamPerformance(MatchTeamType.ALL, 6);
			item.setClientPerf(cperf);
		}
		catch(Exception e)
		{
			logger.info(e.toString());
		}
		
		item.setLastMatch(getTowTeamLostMatch());		
		return item;
	}
	
	/**
	 * 获得两支球队最近的一场比赛数据
	 * 
	 * @return 球队比赛数据
	 */
	public Match getTowTeamLostMatch()
	{
		List<Match> ms = new ArrayList<>();
		ms.addAll(homeHistories);
		
		Collections.sort(ms, new Comparator<Match>()
		{
			@Override
			public int compare(Match o1, Match o2)
			{
				return o2.getMatchtime().compareTo(o1.getMatchtime());
			}
		});
		
		for (Match match : ms)
		{
			if(match.isTwoTeam(getHomeid(), getClientid()))
			{
				return match;
			}
		}
		return null;
	}
	
	/**
	 * 获得历史比赛战绩数据
	 * @return 历史比赛数据
	 */
	public List<Match> getHistoryMatch()
	{
		List<Match> matchs = new ArrayList<>();
		
		if(homeHistories != null)
		{
			for (Match match : homeHistories)
			{
				if(match.isTwoTeam(getHomeid(), getClientid()))
				{
					matchs.add(match);
				}
			}
		}
		
		if(clientHistories != null)
		{
			for (Match match : clientHistories)
			{
				if(match.isTwoTeam(getHomeid(), getClientid()))
				{
					matchs.add(match);
				}
			}
		}
		
		return matchs;
	}

	/*
	public Performance getHomeState()
	{
		return homeState;
	}
	public void setHomeState(Performance homeState)
	{
		this.homeState = homeState;
	}
	public Performance getClientSate()
	{
		return clientSate;
	}
	public void setClientSate(Performance clientSate)
	{
		this.clientSate = clientSate;
	}
	public Op getAverageOp()
	{
		return averageOp;
	}
	public void setAverageOp(Op averageOp)
	{
		this.averageOp = averageOp;
	}
	public Yp getAverageYp()
	{
		return averageYp;
	}
	public void setAverageYp(Yp averageYp)
	{
		this.averageYp = averageYp;
	}*/
	public List<Op> getOpList()
	{
		return opList;
	}
	public void setOpList(List<Op> opList)
	{
		this.opList = opList;
	}
	public List<Yp> getYpList()
	{
		return ypList;
	}
	public void setYpList(List<Yp> ypList)
	{
		this.ypList = ypList;
	}
	/*
	public Op getLastAverageOp()
	{
		return lastAverageOp;
	}
	public void setLastAverageOp(Op lastAverageOp)
	{
		this.lastAverageOp = lastAverageOp;
	}
	public Yp getLastAverageYp()
	{
		return lastAverageYp;
	}
	public void setLastAverageYp(Yp lastAverageYp)
	{
		this.lastAverageYp = lastAverageYp;
	}
	public List<String> getMids()
	{
		return mids;
	}
	public void setMids(List<String> mids)
	{
		this.mids = mids;
	}*/
	
	/**
	 * 查询欧赔数值
	 * 
	 * @param gid 公司编号
	 * @return 符合条件的欧赔值，若没有，则返回NULL值
	 */
	public Op getOp(String gid)
	{
		for (Op op : opList)
		{
			if(gid.equalsIgnoreCase(op.getGid()))
			{
				return op;
			}
		}
		return null;
	}

	/**
	 * 加入欧赔数据
	 * 
	 * @param op 欧赔数据
	 */
	public void addOp(Op op)
	{
		//if(getMid().equals(op.getMid()))
		opList.add(op);
	}
	
	/**
	 * 查询亚盘数值
	 * 
	 * @param gid 公司编号
	 * @return 符合条件的亚盘值，若没有，则返回NULL值
	 */
	public Yp getYp(String gid)
	{
		for (Yp yp : ypList)
		{
			if(gid.equalsIgnoreCase(yp.getGid()))
			{
				return yp;
			}
		}
		return null;
	}
	
	/**
	 * 加入亚盘数据
	 * 
	 * @param yp 亚盘数据
	 */
	public void addYp(Yp yp)
	{
		//if(getMid().equals(yp.getMid()))
		ypList.add(yp);
	}
	
	/**
	 * 添加关联比赛数据
	 * 
	 * @param mid 关联比赛编号
	 */
	public void addCorrealtionMatch(String mid)
	{
		for (String string : mids)
		{
			if(mid.equalsIgnoreCase(string))
			{
				return;
			}
		}
		mids.add(mid);
	}
	
	/**
	 * 添加历史比赛数据
	 * 
	 * @param m 比赛数据
	 */
	public void addHistoryMatch(Match m)
	{
		if(isHomeTeamHistoryMatch(m))
		{
			homeHistories.add(m);
		}
		if(isClientHistoryMatch(m))
		{
			clientHistories.add(m);
		}
	}
	
	/**
	 * 获得主队的历史比赛数据
	 * 
	 * @return 历史比赛数据列表
	 */
	public List<Match> getHomeHistories()
	{
		return homeHistories;
	}
	
	/**
	 * 获得客队的历史比赛数据
	 * @return
	 */
	public List<Match> getClientHistories()
	{
		return clientHistories;
	}
	
	/**
	 * 获得主队的历史比赛数据列表
	 * 
	 * @param type 类型：主场、客场、全部
	 * @param lastMatchSize 数据场次
	 * @return 符合条件的比赛场次
	 */
	public List<Match> getHomeHistories(MatchTeamType type, int lastMatchSize)
	{
		return PerformanceUtil.getMatchList(homeHistories, getHomeid(),
				lastMatchSize, type);
	}
	
	/**
	 * 获得客队的比赛历史数据列表
	 * 
	 * @param type 类型：主场、客场、全部
	 * @param lastMatchSize 数据场次
	 * @return 符合条件的比赛场次
	 */
	public List<Match> getClientHistories(MatchTeamType type, int lastMatchSize)
	{
		return PerformanceUtil.getMatchList(clientHistories, getClientid(), 
				lastMatchSize, type);
	}

	/**
	 * 计算主队的最近战绩
	 * 
	 * @param type 比赛类型：主场、客场、全部
	 * @param lastMatchSize 比赛场次
	 * @return
	 */
	public PerformItem getHomeTeamPerformance(MatchTeamType type, int lastMatchSize)
	{
		return PerformanceUtil.computeTeamPerformance(getHomeid(), type, 
				homeHistories, lastMatchSize);
	}
	
	/**
	 * 计算客队的最近战绩
	 * 
	 * @param type 比赛类型：主场、客场、全部
	 * @param lastMatchSize 比赛场次
	 * @return
	 */
	public PerformItem getClientTeamPerformance(MatchTeamType type, int lastMatchSize)
	{
		return PerformanceUtil.computeTeamPerformance(getClientid(), type, 
				clientHistories, lastMatchSize);
	}

	@Override
	public String toString()
	{
		return "MatchData [match"  + ":" + getMid() /*+ ", homeState=" + homeState + ", clientSate=" + clientSate*/
				+ ", opsize=" + opList.size() + ", ypsize=" + ypList.size() 
				+ ", homeHistory=" + homeHistories.size() 
				+ ", clientHistory=" + clientHistories.size() + "]";
	}
}
