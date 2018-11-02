package com.loris.soccer.analysis.util;

import java.util.List;

import com.loris.base.analysis.graph.Graph;
import com.loris.base.analysis.graph.GraphEdge;
import com.loris.base.analysis.graph.GraphNode;
import com.loris.base.util.DateUtil;
import com.loris.soccer.analysis.data.LeagueMatchDoc;
import com.loris.soccer.analysis.data.MatchData;
import com.loris.soccer.bean.data.table.odds.Op;
import com.loris.soccer.bean.item.MatchInfoItem;


/**
 * 联赛比赛图数据
 * 
 * @author jiean
 *
 */
public class MatchGraph
{
	protected static String creator = "Loris";
	
	protected static float diffThreshold = 0.05f;	
	protected static float coefficeintThreshold = 0.35f;
	
	/**
	 * 创建Gexf图谱
	 * 
	 * @return Gexf图谱
	 
	public static Gexf createGexf(String issue, List<LeagueMatchVector> leagues)
	{
		Gexf gexf = new GexfImpl();
		gexf.getMetadata().setCreator(creator);
		String description = "竞彩足球比赛" + issue + "赛事分析";
		gexf.getMetadata().setDescription(description).setLastModified(new Date());
		gexf.setVisualization(true);
		
		Graph graph = gexf.getGraph();
		graph.setDefaultEdgeType(EdgeType.DIRECTED);
		graph.setMode(Mode.STATIC);
		
		//创建图
		int i = 0;
		for (LeagueMatchVector league : leagues)
		{
			createGraph(graph, i ++, league);
		}
		
		return gexf;
	}*/
	
	/**
	 * 创建图谱
	 * 
	 * @param issue 比赛期号
	 * @param leagues 联赛数据
	 * @return 图谱
	 */
	public static Graph createGraph(String issue, List<LeagueMatchDoc> leagues)
	{
		Graph graph = new Graph();
		graph.setCreatetime(DateUtil.getCurTimeStr());
		graph.setCreator(creator);
		for (LeagueMatchDoc league : leagues)
		{
			createGraph(graph, league);
		}
		return graph;
	}
	
	/**
	 * 创建联赛比赛关联图谱数据
	 * 
	 * @param graph 图形数据
	 * @param league 联赛比赛数据
	 */
	public static void createGraph(Graph graph, LeagueMatchDoc league)
	{
		//String lid = league.getLid();
		//String lname = league.getLeaguename();
		String mid, mid2;
		MatchInfoItem baseMatch;
		GraphNode node;
		GraphEdge edge;
		String homename;
		String clientname;
		Op averageOp, averageOp2;
		String averageCorpId = "0";
		
		MatchData result, result2;
		String opValue1 = "", opValue2 = "";
		float coefficient;
		
		List<MatchData> matchs = league.getMatchDatas();
		int size = matchs.size();
		
		//GraphNode node = graph.createNode(lid);
		//node.setLabel(lname);
		
		for (int i = 0; i < size; i ++)
		{
			result = matchs.get(i);
			mid = result.getMid();			
			node = graph.createNode(mid);
			
			//设置节点基本信息
			baseMatch = result;
			homename = baseMatch.getHomename();
			clientname = baseMatch.getClientname();
			node.setName(homename + " vs " + clientname);
			
			averageOp = result.getOp(averageCorpId);
			
			if(averageOp != null)
			{
				opValue1 = formatAverageOp(averageOp);
				node.addAttribute(opValue1);
				node.setSize(computeNodeSize(averageOp));
			}
			
			for(int j = i + 1; j < size; j ++)
			{
				result2 = matchs.get(j);
				mid2 = result2.getMid();
				averageOp2 = result2.getOp(averageCorpId);
				if(averageOp2 == null)
				{
					continue;
				}
				
				coefficient = computeCorrelation(averageOp, averageOp2);
				if(coefficient >= coefficeintThreshold)
				{
					edge = graph.createEdge(mid + ":" + mid2);
					edge.setSource(mid);
					edge.setTarget(mid2);
					
					opValue2 = formatAverageOp(averageOp2);
					edge.addAttribute(mid + ": " + opValue1);
					edge.addAttribute(mid2 + ": " + opValue2);
					edge.setValue(coefficient);					
					edge.setSize((int)(20.0 * (coefficient * coefficient)));
				}
			}
		}
	}
	
	/**
	 * 格式化平均欧值
	 * 
	 * @param averageOp 平均欧赔值
	 * @return 格式化后的值
	 */
	protected static String formatAverageOp(Op averageOp)
	{
		String st = averageOp.getGname() + ": " + averageOp.getFirstwinodds() + ", " 
				+ averageOp.getFirstdrawodds() + ", " + averageOp.getFirstloseodds();
		return st;
	}
	
	/**
	 * 计算两个欧赔值的关联系数
	 * 
	 * @param op1 欧赔1
	 * @param op2 欧赔2
	 * @return 相关系数
	 */
	protected static float computeCorrelation(Op op1, Op op2)
	{
		double diff = getMinDiff(op1, op2);	
		diff /= diffThreshold;
		return (float) (1/ Math.pow(Math.E, diff));
	}
	
	/**
	 * 计算最小的差值，这里仅以胜、负值进行计算与比较
	 * 
	 * @param op1 欧赔1
	 * @param op2 欧赔2
	 * @return 相关值
	 */
	protected static float getMinDiff(Op op1, Op op2)
	{
		double diff = getDiffValue(op1.getFirstwinodds(), op2.getFirstwinodds());
		double diff2 = getDiffValue(op1.getFirstwinodds(), op2.getFirstloseodds());
		if(diff2 < diff)
		{
			diff = diff2;
		}
		diff2 = getDiffValue(op1.getFirstloseodds(), op2.getFirstwinodds());
		if(diff2 < diff)
		{
			diff = diff2;
		}
		diff2 = getDiffValue(op1.getFirstloseodds(), op2.getFirstloseodds());
		if(diff2 < diff)
		{
			diff = diff2;
		}
		return (float)diff;
	}
	
	/**
	 * 计算两个数据的差值
	 * 
	 * @param d1 值1
	 * @param d2 值2
	 * @return 差值
	 */
	protected static double getDiffValue(double d1, double d2)
	{
		double diff = Math.abs(d1 - d2);
		if(diff <= diffThreshold)
		{
			diff = 0.0f;
		}
		else
		{
			diff = diff - diffThreshold;
		}
		return diff;
	}
	
	/**
	 * 计算节点的大小值
	 * 
	 * @param op 欧赔值
	 * @return 节点大小
	 */
	protected static int computeNodeSize(Op op)
	{
		return (int)(100.0 / op.getFirstwinodds());
	}
}
