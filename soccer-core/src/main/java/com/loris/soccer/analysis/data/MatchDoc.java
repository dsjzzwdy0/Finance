package com.loris.soccer.analysis.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.data.table.league.Match;
import com.loris.soccer.bean.data.table.odds.Op;
import com.loris.soccer.bean.data.table.odds.Yp;
import com.loris.soccer.bean.item.IssueMatch;
import com.loris.soccer.bean.model.IssueMatchMapping;


/**
 * 竞彩比赛数据池，包括：<br/>
 * 1、竞彩比赛数据列表<br/>
 * 2、竞彩比赛数据综合分析结果类<br/>
 * 
 * @author dsj
 *
 */
public class MatchDoc
{
	/** 比赛数据数组列表 */
	protected Map<String, MatchData> matchDatas = new HashMap<>();
	
	/** 主数据源的比赛数据 */
	protected List<IssueMatch> matches = new ArrayList<>();
	
	/** 数据映射表 */
	protected Map<String, IssueMatchMapping> mappings = new HashMap<>();
	
	/** 默认的数据 */
	protected final String defaultSource;

	/** 赔率公司置 */
	//protected CorpSetting corpSetting;
	
	/**
	 * Create a new instance of MatchDataVector.
	 */
	public MatchDoc()
	{
		this(SoccerConstants.DATA_SOURCE_ZGZCW, null);
	}
	
	/**
	 * 构造函数
	 * @param matchs 比赛列表数据
	 */
	public MatchDoc(List<? extends IssueMatch> matchs)
	{
		this(SoccerConstants.DATA_SOURCE_ZGZCW, matchs);		
	}
	
	/**
	 * Create a new instance of MatchDataVector
	 * @param source
	 * @param matchs
	 */
	public MatchDoc(String source, List<? extends IssueMatch> matchs)
	{
		defaultSource = source;
		if(matchs == null)
		{
			return;
		}
		for (IssueMatch IssueMatch : matchs)
		{
			addIssueMatch(IssueMatch);
		}
	}

	/**
	 * 添加竞彩比赛数据
	 * @param match 比赛数据
	 */
	public void addIssueMatch(IssueMatch match)
	{
		this.matches.add(match);
		MatchData matchData = new MatchData(match);
		this.addMatchData(matchData);
	}
	
	/**
	 * 添加比赛数据
	 * @param data 比赛数据
	 */
	protected void addMatchData(MatchData data)
	{
		if(matchDatas.containsKey(data.getMid()))
		{
			return;
		}
		matchDatas.put(data.getMid(), data);
	}
	
	/**
	 * 按照联赛编号获得比赛数据
	 * @param lid 联赛编号
	 * @return 数据列表
	 */
	public LeagueMatchDoc getMatchDataList(String lid)
	{
		LeagueMatchDoc doc = new LeagueMatchDoc();
		doc.setLid(lid);
		for (MatchData matchData : matchDatas.values())
		{
			if(lid.equals(matchData.getLid()))
			{
				//datas.add(matchData);				
				doc.addMatchData(matchData);
			}
		}
		return doc;
	}
	
	/**
	 * 获得比赛数据列表
	 * @return 比赛数组
	 */
	public List<MatchData> getMatchDatas()
	{
		List<MatchData> datas = new ArrayList<>();
		datas.addAll(matchDatas.values());
		return datas;
	}
	
	/**
	 * 获得某一联赛的数据
	 * @param lid 联赛编号
	 * @return 联赛的数据数组
	 */
	public List<MatchData> getMatchDatas(String lid)
	{
		List<MatchData> datas = new ArrayList<>();
		for (MatchData match : matchDatas.values())
		{
			if(match.getLid().equals(lid))
			{
				datas.add(match);
			}
		}
		return datas;
	}
	
	/**
	 * 获得比赛数据列表
	 * @param lid
	 * @return
	 
	public IssueMatchDocs getMatchDataList(String lid)
	{
		IssueMatchDocs list = new IssueMatchDocs();
		for (MatchDoc match : matchDatas.values())
		{
			if(match.getLid().equals(lid))
			{
				list.addIssueMatchData(match);
			}
		}
		return list;
	}
	
	public CorpSetting getCorpSetting()
	{
		return corpSetting;
	}

	public void setCorpSetting(CorpSetting configure)
	{
		this.corpSetting = configure;
	}*/

	/**
	 * 添加历史比赛数据
	 * @param matchs 比赛数据
	 */
	public void addHistoryMatches(List<Match> matchs)
	{
		for (Match baseMatch : matchs)
		{
			addHistoryMatch(baseMatch);
		}
	}
	
	/**
	 * 添加历史比赛数据
	 * 
	 * @param match 比赛数据
	 */
	public void addHistoryMatch(Match match)
	{
		for (MatchData result : matchDatas.values())
		{
			result.addHistoryMatch(match);
		}
	}
	
	/**
	 * 添加相关比赛的亚盘数据
	 * @param source 数据来源
	 * @param yps 亚盘数据
	 */
	public void addYpList(String source, List<? extends Yp> yps)
	{
		if(source.equals(defaultSource))
		{
			addYpList(yps);
		}
		else
		{
			IssueMatchMapping mapping = mappings.get(source);
			addYpList(mapping, yps);
		}
	}
	
	/**
	 * 添加欧赔赔率数据
	 * 
	 * @param ops 欧赔赔率数据列表
	 */
	public void addOpList(List<? extends Op> ops)
	{
		for (Op op : ops)
		{
			addOp(op);
		}
	}
	
	/**
	 * 添加欧赔数据表
	 * 
	 * @param mappingTable 映射表
	 * @param ops 欧赔表
	 */
	public void addOpList(IssueMatchMapping mappingTable, List<? extends Op> ops)
	{
		for (Op op : ops)
		{
			IssueMatchMapping.Mapping mapping = mappingTable.getMappingByDestMID(op.getMid());
			if(mapping == null)
			{
				continue;
			}
			MatchData result = matchDatas.get(mapping.getSourceMid());
			if(result != null)
			{
				result.addOp(op);
			}
		}
	}
	
	/**
	 * 添加欧赔数据
	 * 
	 * @param op 欧赔数据
	 */
	public void addOp(Op op)
	{
		MatchData result = matchDatas.get(op.getMid());
		if(result != null)
		{
			result.addOp(op);
		}
	}
	
	/**
	 * 添加亚盘赔率数据
	 * 
	 * @param ops 亚盘赔率数据列表
	 */
	public void addYpList(List<? extends Yp> yps)
	{
		for (Yp yp : yps)
		{
			addYp(yp);
		}
	}
	
	/**
	 * 添加亚盘数据
	 * @param mappingTable 数据映射表
	 * @param yps 亚盘赔率表
	 */
	public void addYpList(IssueMatchMapping mappingTable, List<? extends Yp> yps)
	{
		for (Yp yp : yps)
		{
			IssueMatchMapping.Mapping mapping = mappingTable.getMappingByDestMID(yp.getMid());
			if(mapping == null)
			{
				continue;
			}
			MatchData result = matchDatas.get(mapping.getSourceMid());
			if(result != null)
			{
				result.addYp(yp);
			}
		}
	}
	
	/**
	 * 添加亚盘数据
	 * 
	 * @param yp 亚盘数据
	 */
	public void addYp(Yp yp)
	{
		MatchData result = matchDatas.get(yp.getMid());
		if(result != null)
		{
			result.addYp(yp);
		}
	}
	
	/**
	 * 获得数组的长度
	 * @return 长度数值
	 */
	public int size()
	{
		return matchDatas.size();
	}
	
	/**
	 * 获得比赛数据
	 * @param index 序号
	 * @return 比赛数据
	 */
	public MatchData getMatchData(int index)
	{
		if(index >= size() || index < 0)
		{
			return null;
		}
		return (MatchData)matchDatas.values().toArray()[index];
	}
	
	/**
	 * 获得关键词的数据列
	 * @return 数据列
	 */
	public Set<String> keySet()
	{
		return matchDatas.keySet();
	}
	
	/**
	 * 获得数据列表
	 * @return 数据列表
	 */
	public Collection<MatchData> values()
	{
		return matchDatas.values();
	}
	
	/**
	 * 查询比赛表
	 * @param mid 比赛编号
	 * @return 比赛数据
	 */
	public MatchData getMatchData(String mid)
	{
		return matchDatas.get(mid);
	}
	
	/**
	 * 获得比赛的数据文档。
	 * @param issue 期号
	 * @param ordinary 序号
	 * @return 数据文档
	 */
	public MatchData getMatchData(String issue, String ordinary)
	{
		for (MatchData match : matchDatas.values())
		{
			if(issue.equals(match.getIssue()) && 
					ordinary.equals(match.getOrdinary()))
			{
				return match;
			}
		}
		return null;
	}
	
	/**
	 * 获得球队的编号
	 * 
	 * @param bdMatchs 比赛数据列表
	 * @param tids 球队编号列表
	 * @return 球队号
	 */
	public List<String> getTeamIds()
	{
		List<String> tids = new ArrayList<>();
		for (MatchData match : matchDatas.values())
		{
			tids.add(match.getHomeid());
			tids.add(match.getClientid());
		}
		return tids;
	}
	
	/**
	 * 获得比赛的编号列表
	 * @param bdMatchs 比赛列表
	 * @return 列表数据
	 */
	public List<String> getMatchIds()
	{
		List<String> mids = new ArrayList<>();
		for (MatchData match : matchDatas.values())
		{
			mids.add(match.getMid());
		}
		return mids;
	}
	
	/**
	 * 添加数据映射表
	 * @param source 数据源
	 * @param mapping 映射表
	 */
	public void addIssueMatchMapping(String source, IssueMatchMapping mapping)
	{
		mappings.put(source, mapping);
	}
	
	/**
	 * 检测是否包含数据映射
	 * @param source 数据来源
	 * @return 是否包含的标志
	 */
	public boolean containsMapping(String source)
	{
		return mappings.containsKey(source);
	}
	
	
	/**
	 * 获得比赛列表
	 * @return 竞彩数据列表
	 */
	public List<IssueMatch> getMatches()
	{
		return matches;
	}
}
