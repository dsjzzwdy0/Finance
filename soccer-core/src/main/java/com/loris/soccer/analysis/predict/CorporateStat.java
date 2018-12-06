package com.loris.soccer.analysis.predict;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.context.LorisContext;
import com.loris.base.data.Region;
import com.loris.soccer.analysis.element.CorpStatElement;
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

	/** 数据管理器 */
	static SoccerManager soccerManager;
	
	static List<Region<Float>> regions = new ArrayList<>();
	
	static {
		addRegion(0.0f, 30.0f);
		addRegion(1.0f, 1.15f);
		addRegion(1.15f, 1.30f);
		addRegion(1.30f, 1.50f);
		addRegion(1.50f, 1.75f);
		addRegion(1.75f, 2.00f);
		addRegion(2.0f, 2.5f);
		addRegion(2.5f, 30.0f);
	}
	
	static class CorpStatList extends ArrayList<CorpStatElement>
	{
		/***/
		private static final long serialVersionUID = 1L;

		/**
		 * 创建统计值
		 * @param corp
		 * @return
		 */
		public CorpStatElement getCorporateStat(Corporate corp)
		{
			for (CorpStatElement stat : this)
			{
				if (stat.getCorp().getGid().equals(corp.getGid()))
				{
					return stat;
				}
			}
			return null;
		}
		
		/**
		 * 如果存在，则直接返回数据统计值；如果不存在，则创建数据统计值之后返回
		 * @param corp
		 * @param regions
		 * @return
		 */
		public CorpStatElement getOrCreateCorpStat(Corporate corp)
		{
			CorpStatElement stat = getCorporateStat(corp);
			if(stat == null)
			{
				stat = new CorpStatElement(corp, regions);
				stat.createCorpStatElements(regions);
				this.add(stat);
			}
			return stat;
		}
	}

	/**
	 * 设置管理器
	 * 
	 * @param manager
	 */
	public static void initialize(LorisContext context)
	{
		soccerManager = context.getBean(SoccerManager.class);
	}

	/**
	 * 设置管理器
	 * 
	 * @param manager
	 */
	public static void initialize(SoccerManager manager)
	{
		soccerManager = manager;
	}

	/**
	 * 计算平均值
	 * 
	 * @param matches
	 * @param corpStatList
	 * @param type
	 *            平均值：0, 方差值：1
	 * @return
	 */
	protected static List<Match> computeStat(List<Match> matches, CorpStatList corpStatList, int type)
	{
		List<Match> existOpMatches = new ArrayList<>();
		int i = 1;
		for (Match match : matches)
		{
			logger.info("Compute " + (i++) + " of " + matches.size() + " Match: " + match);
			ScoreItem item = match.getScoreResult();
			if (item.getResult() < 0)
			{
				logger.info("");
			}

			List<Op> ops = soccerManager.getOpList(match.getMid(), true);
			if (ops == null || ops.isEmpty())
			{
				logger.info("There are no op record of '" + match.getMid() + " in database, next.");
				continue;
			}

			OpList list = new OpList(OpList.OpListType.GidUnique, ops);
			Op avgOp = list.getAvgOp();
			if (avgOp == null)
			{
				logger.info("There are no average op record of '" + match.getMid() + " in database, next.");
				continue;
			}

			existOpMatches.add(match);
			for (Op op : list)
			{
				// 平均欧赔值
				if ("0".equals(op.getGid()))
				{
					continue;
				}
				CorpStatElement stat = null;
				if(type == 0)
					stat = corpStatList.getOrCreateCorpStat(op.getCorporate());
				else
					stat = corpStatList.getCorporateStat(op.getCorporate());
				
				if (stat == null)
				{
					continue;
				}

				if (type == 0)
				{
					stat.addMeanValue(item, op, avgOp);
				}
				else
				{
					stat.addVarValue(item, op, avgOp);
				}
			}
		}
		return existOpMatches;
	}

	/**
	 * 计算统计值
	 * 
	 * @param start
	 * @param end
	 */
	public static void computeStat(String start, String end) throws IOException
	{
		CorpStatList corpStatList = new CorpStatList();
		List<Match> matches = soccerManager.getMatches(start, end);
		logger.info("Total match is : " + matches.size());

		List<Match> existOpMatches = new ArrayList<>();

		// 首先计算平均值
		existOpMatches = computeStat(matches, corpStatList, 0);

		// 计算平均值
		for (CorpStatElement corpStat : corpStatList)
		{
			corpStat.computeMean();
		}

		if (!existOpMatches.isEmpty())
		{
			computeStat(existOpMatches, corpStatList, 1);
		}

		// File file = new File("d:/index/stat.txt");
		// try(BufferedWriter writer = new BufferedWriter(new
		// OutputStreamWriter(new FileOutputStream(file))))

		int i = 1;
		for (CorpStatElement corpStat : corpStatList)
		{
			corpStat.computeStdErr();
			List<CorpStatItem> items = corpStat.getAllCorpStatItems();

			logger.info(i++ + ": " + items);			
			for (CorpStatItem item : items)
			{
				// writer.write(i++ + ": " + item + "\r\n");
				soccerManager.addOrUpdateCorpStatItem(item);
			}
		}
	}
	
	public static void addRegion(float min, float max)
	{
		regions.add(new Region<>(min, max));
	}
}

