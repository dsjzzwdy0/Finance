package com.loris.soccer.analysis.stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.context.LorisContext;
import com.loris.base.data.Region;
import com.loris.soccer.bean.data.table.Corporate;
import com.loris.soccer.bean.data.table.Match;
import com.loris.soccer.bean.data.table.Op;
import com.loris.soccer.bean.item.CorpOpVar;
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
	
	static class CorpStatList extends ArrayList<CorpStat>
	{
		/***/
		private static final long serialVersionUID = 1L;

		/**
		 * 创建统计值
		 * @param corp
		 * @return
		 */
		public CorpStat getCorporateStat(Corporate corp)
		{
			for (CorpStat stat : this)
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
		public CorpStat getOrCreateCorpStat(Corporate corp)
		{
			CorpStat stat = getCorporateStat(corp);
			if(stat == null)
			{
				stat = new CorpStat(corp);
				stat.createCorpStatElements();
				this.add(stat);
			}
			return stat;
		}
	}
	
	static class CorpStat
	{
		Corporate corp;
		
		List<CorpStatElement> elements = new ArrayList<>();
		
		public CorpStat()
		{
		}

		public CorpStat(Corporate corp)
		{
			this.corp = corp;
		}

		public Corporate getCorp()
		{
			return corp;
		}

		public void setCorp(Corporate corp)
		{
			this.corp = corp;
		}
		
		/**
		 * 加入平均值
		 * @param item
		 * @param op
		 * @param avgOp
		 */
		public void addMeanValue(ScoreItem item, Op op, Op avgOp)
		{
			for (CorpStatElement element : elements)
			{
				if(element.contains(op.getWinodds()))
				{
					element.addMeanValue(item, op, avgOp);
				}
			}
		}
		
		/**
		 * 加入平均值
		 * @param item
		 * @param op
		 * @param avgOp
		 */
		public void addVarValue(ScoreItem item, Op op, Op avgOp)
		{
			for (CorpStatElement element : elements)
			{
				if(element.contains(op.getWinodds()))
				{
					element.addVarValue(item, op, avgOp);
				}
			}
		}
		
		public void computeMean()
		{
			for (CorpStatElement element : elements)
			{
				element.computeMean();
			}
		}
		
		public void computeStdErr()
		{
			for (CorpStatElement element : elements)
			{
				element.computeStdErr();
			}
		}
		
		public void createCorpStatElements()
		{
			for (Region<Float> region : regions)
			{
				CorpStatElement element = new CorpStatElement(corp, region);
				elements.add(element);
			}
		}
		
		public List<CorpStatItem> createCorpStatItems()
		{
			List<CorpStatItem> items = new ArrayList<>();
			for (CorpStatElement element : elements)
			{
				items.add(element.createCorpStatItem());
			}
			return items;
		}
	}

	static class CorpStatElement
	{
		Corporate corp;
		int matchNum = 0;
		float min;
		float max;
		ComputeCorpOpVar baseVar = new ComputeCorpOpVar();
		ComputeCorpOpVar winVar = new ComputeCorpOpVar();
		ComputeCorpOpVar drawVar = new ComputeCorpOpVar();
		ComputeCorpOpVar loseVar = new ComputeCorpOpVar();
		
		/**
		 * 
		 * @param corp
		 * @param region
		 */
		public CorpStatElement(Corporate corp, Region<Float> region)
		{
			this.corp = corp;
			this.min = region.getMin();
			this.max = region.getMax();
		}
		
		/**
		 * 是否包含该区间的值
		 * @param odds
		 * @return
		 */
		public boolean contains(float odds)
		{
			return min <= odds && odds <= max;
		}

		/*
		 * Deviation deviation = new Deviation(); Deviation firstDeviation = new
		 * Deviation();
		 * 
		 * ResultStat winResult = new ResultStat(3); ResultStat drawResult = new
		 * ResultStat(1); ResultStat loseResult = new ResultStat(0);
		 */

		/**
		 * 加入一个数据值，计算方差值所用
		 * @param item
		 * @param op
		 * @param avgOp
		 */
		public void addVarValue(ScoreItem item, Op op, Op avgOp)
		{
			float winDiff;
			float drawDiff;
			float loseDiff;
			float fwinDiff;
			float fdrawDiff;
			float floseDiff;

			fwinDiff = op.getFirstwinodds() - avgOp.getFirstwinodds();
			fdrawDiff = op.getFirstdrawodds() - avgOp.getFirstdrawodds();
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

			baseVar.add(0, 0, 0, fwinDiff * fwinDiff, fdrawDiff * fdrawDiff, floseDiff * floseDiff, 0, 0, 0, winDiff * winDiff,
					drawDiff * drawDiff, loseDiff * loseDiff);
			int result = item.getResult();
			ComputeCorpOpVar var;
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

			var.add(0, 0, 0, fwinDiff * fwinDiff, fdrawDiff * fdrawDiff, floseDiff * floseDiff, 0, 0, 0, winDiff * winDiff,
					drawDiff * drawDiff, loseDiff * loseDiff);
		}

		/**
		 * 添加一个数据记录，计算平均值所有
		 * 
		 * @param match
		 * @param op
		 * @param avgOp
		 */
		public void addMeanValue(ScoreItem item, Op op, Op avgOp)
		{
			float winDiff;
			float drawDiff;
			float loseDiff;
			float fwinDiff;
			float fdrawDiff;
			float floseDiff;

			fwinDiff = op.getFirstwinodds() - avgOp.getFirstwinodds();
			fdrawDiff = op.getFirstdrawodds() - avgOp.getFirstdrawodds();
			floseDiff = op.getFirstloseodds() - avgOp.getFirstloseodds();

			winDiff = op.getWinodds() - avgOp.getWinodds();
			drawDiff = op.getDrawodds() - avgOp.getDrawodds();
			loseDiff = op.getLoseodds() - avgOp.getLoseodds();

			baseVar.add(fwinDiff, fdrawDiff, floseDiff, 0, 0, 0, winDiff, drawDiff, loseDiff, 0, 0, 0);
			baseVar.addNum();

			int result = item.getResult();
			ComputeCorpOpVar var;
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

			var.add(fwinDiff, fdrawDiff, floseDiff, 0, 0, 0, winDiff, drawDiff, loseDiff, 0, 0, 0);
			var.addNum();

			matchNum++;
		}

		public void computeMean()
		{
			baseVar.computeMeanValue();
			winVar.computeMeanValue();
			drawVar.computeMeanValue();
			loseVar.computeMeanValue();
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
			item.setOddsmin(min);
			item.setOddsmax(max);
			item.setBaseOpVar(baseVar);
			item.setWinOpVar(winVar);
			item.setDrawOpVar(drawVar);
			item.setLoseOpVar(loseVar);
			return item;
		}

		public float getMin()
		{
			return min;
		}

		public void setMin(float min)
		{
			this.min = min;
		}

		public float getMax()
		{
			return max;
		}

		public void setMax(float max)
		{
			this.max = max;
		}
	}

	/**
	 * 用于计算所有的公司方差值
	 * 
	 * @author jiean
	 *
	 */
	static class ComputeCorpOpVar extends CorpOpVar
	{
		public void addNum()
		{
			this.num++;
		}

		/**
		 * 计算平均值
		 */
		public void computeMeanValue()
		{
			if (num <= 0)
			{
				return;
			}
			int size = vars.length;
			for (int i = 0; i < size; i++)
			{
				int k = (i / 3) % 2;
				if (k == 0)
				{
					vars[i] /= num;
				}
			}
		}

		/**
		 * 计算方差值
		 */
		public void computeStdErr()
		{
			if (num <= 0)
			{
				return;
			}
			int size = vars.length;
			for (int i = 0; i < size; i++)
			{
				int k = (i / 3) % 2;
				if (k == 1)
				{
					vars[i] = (float) Math.sqrt(vars[i] / num);
				}
			}
		}

		/**
		 * 加入一个数据记录
		 * @param firstwindiff
		 * @param firstdrawdiff
		 * @param firstlosediff
		 * @param firstwinstd
		 * @param firstdrawstd
		 * @param firstlosestd
		 * @param windiff
		 * @param drawdiff
		 * @param losediff
		 * @param winstd
		 * @param drawstd
		 * @param losestd
		 */
		public void add(float firstwindiff, float firstdrawdiff, float firstlosediff, float firstwinstd, float firstdrawstd,
				float firstlosestd, float windiff, float drawdiff, float losediff, float winstd, float drawstd,
				float losestd)
		{
			vars[0] += firstwindiff;
			vars[1] += firstdrawdiff;
			vars[2] += firstlosediff;
			vars[3] += firstwinstd;
			vars[4] += firstdrawstd;
			vars[5] += firstlosestd;
			vars[6] += windiff;
			vars[7] += drawdiff;
			vars[8] += losediff;
			vars[9] += winstd;
			vars[10] += drawstd;
			vars[11] += losestd;
		}

		/**
		 * 加入数据记录
		 * @param vars
		 */
		public void add(float[] vars)
		{
			if (vars.length != 12)
			{
				throw new IllegalArgumentException("The vars length is not 12, error.");
			}
			for (int i = 0; i < vars.length; i++)
			{
				this.vars[i] += vars[i];
			}
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
				CorpStat stat = null;
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
		for (CorpStat corpStat : corpStatList)
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
		for (CorpStat corpStat : corpStatList)
		{
			corpStat.computeStdErr();
			List<CorpStatItem> items = corpStat.createCorpStatItems();

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

