package com.loris.soccer.analysis.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.analysis.model.Freqs;
import com.loris.base.analysis.stat.Statistics;
import com.loris.base.data.Variance;
import com.loris.base.util.ArraysUtil;
import com.loris.base.util.DateUtil;
import com.loris.base.util.NumberUtil;
import com.loris.soccer.analysis.checker.OddsChecker;
import com.loris.soccer.analysis.data.MatchOpVariance;
import com.loris.soccer.analysis.data.OpVariance;
import com.loris.soccer.analysis.data.OddsVariance;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.data.table.odds.Op;
import com.loris.soccer.bean.data.table.odds.Yp;
import com.loris.soccer.bean.element.OddsElement;
import com.loris.soccer.bean.item.IssueMatch;
import com.loris.soccer.bean.item.OddsItem;
import com.loris.soccer.bean.item.YpValue;
import com.loris.soccer.bean.type.OddsValueType;

/**
 * 足球比赛的欧赔数据池
 * 
 * @author jiean
 *
 */
public class OddsUtil
{
	protected static Logger logger = Logger.getLogger(OddsUtil.class);
	
	/**
	 * 赔率转换表
	 * 
	 * @author jiean
	 *
	 */
	public static class OddsMappings
	{
		List<OddsMapping> list = new ArrayList<>();
		String name;
		double min = 100.0;
		double max = -100.0;

		public OddsMappings(String name)
		{
			this.name = name;
		}

		public void add(OddsMapping odds)
		{
			if (!name.equals(odds.getHandicap()))
			{
				return;
			}
			list.add(odds);
			if (odds.getOpodds() < min)
			{
				min = odds.getOpodds();
			}
			if (odds.getOpodds() > max)
			{
				max = odds.getOpodds();
			}
		}

		public int size()
		{
			return list.size();
		}

		public OddsMapping get(int index)
		{
			return list.get(index);
		}

		public boolean isEqualName(String name)
		{
			return this.name.equalsIgnoreCase(name);
		}

		/**
		 * 检测是否包含在该列表中
		 * 
		 * @param odds
		 *            赔率值
		 * @return 是否包含的标志
		 */
		public boolean contains(double odds)
		{
			return min < odds && odds < max;
		}

		/**
		 * 通过某一个欧赔值获得欧亚转换数据列表
		 * 
		 * @param op
		 *            欧赔值
		 * @return 列表
		 */
		public List<OddsMapping> getOddsByOp(double op)
		{
			double minDist = 100.0;
			for (OddsMapping odds : list)
			{
				double d = Math.abs(op - odds.getOpodds());
				if (d < minDist)
				{
					minDist = d;
				}
			}

			List<OddsMapping> l = new ArrayList<>();
			for (OddsMapping odds : list)
			{
				double d = Math.abs(op - odds.getOpodds());
				if (isEqualOrLessthan(d, minDist))
				{
					l.add(odds);
				}
			}
			return l;
		}

		/**
		 * 通过亚盘让球数据与盘口计算欧赔赔率值
		 * 
		 * @param handicap
		 *            让球数
		 * @param yp
		 *            亚盘盘口数据
		 * @return 赔率值
		 */
		public OddsMapping getOddsByYp(double yp)
		{
			double minDist = 100.0;
			OddsMapping odds = null;
			for (OddsMapping o : list)
			{
				double d = Math.abs(yp - o.getYpodds());
				if (d < minDist)
				{
					minDist = d;
					odds = o;
				}
			}
			return odds;
		}

		@Override
		public String toString()
		{
			return "OddsList [name=" + name + ", list=" + list + "]";
		}

	}

	/**
	 * 此为欧赔与亚盘转换对应表
	 * 
	 * @author jiean
	 *
	 */
	static public class OddsMapping
	{
		private String handicap; // 亚盘的让球数
		private double ypodds; // 亚盘的主赔率
		private double opodds; // 欧赔的主赔率

		public OddsMapping()
		{
		}

		public OddsMapping(double op)
		{
			this.opodds = op;
		}

		public OddsMapping(String handicap, double yp)
		{
			this.handicap = handicap;
			this.ypodds = yp;
		}

		public OddsMapping(String handicap, double yp, double op)
		{
			this.handicap = handicap;
			this.ypodds = yp;
			this.opodds = op;
		}

		public String getHandicap()
		{
			return handicap;
		}

		public void setHandicap(String handicap)
		{
			this.handicap = handicap;
		}

		public double getYpodds()
		{
			return ypodds;
		}

		public void setYpodds(double ypodds)
		{
			this.ypodds = ypodds;
		}

		public double getOpodds()
		{
			return opodds;
		}

		public void setOpodds(double opodds)
		{
			this.opodds = opodds;
		}

		@Override
		public String toString()
		{
			return "欧亚转换: (让球:" + handicap + ", 胜赔:" + ypodds + ") <=> 欧赔:" + opodds + "";
		}
	}

	public static final double EPS = 0.0001;

	/*
	 * public static final String[] NAME_YPS_TEXT = { "平手", "平手/半球", "半球",
	 * "半球/一球", "一球", "一球/球半", "球半", "球半/两球", "两球", "两球/两球半", "两球半", "两球半/三球",
	 * "三球", "受平手/半球", "受半球", "受半球/一球", "受一球", "受一球/球半", "受球半", "受球半/两球", "受两球",
	 * "受两球/两球半", "受两球半", "受两球半/三球", "受三球" };
	 */

	public static final String[] NAME_YPS_HOME =
	{ "平手", "平/半", "半球", "半/一", "一球", "一/球半", "球半", "球半/两", "两球", "两/两半", "两半", "两半/三", "三球" };

	public static final String[] NAME_YPS =
	{ "平手", "平/半", "半球", "半/一", "一球", "一/球半", "球半", "球半/两", "两球", "两/两半", "两半", "两半/三", "三球", 
	  "受平/半", "受半球", "受半/一","受一球", "受一/球半", "受球半", "受球半/两", "受两球", "受两/两半", "受两半", "受两半/三", "受三球" };

	public static final float[] NAME_YPS_VALUES =
	{ 0.0f, 0.25f, 0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 1.75f, 2.0f, 2.25f, 2.5f, 2.75f, 3.0f, -0.25f, -0.5f, -0.75f, -1.0f,
			-1.25f, -1.5f, -1.75f, -2.0f, -2.25f, -2.5f, -2.75f, -3.0f };

	public static final String[][] HANDICAP_STANDRARS =
	{
			{"平", "平手"},
			{ "平手/半球", "平/半" },
			{ "半球/一球", "半/一" },
			{ "一球/球半", "一/球半" },
			{ "球半/两球", "球半/两" },
			{ "两球/两球半", "两/两半" },
			{ "两球半/三球", "两半/三"},
			{ "两球半/三", "两半/三"},
			{ "球半/两球", "球半/两" },
			{ "受平手/半球", "受平/半" },
			{ "受半球/一球", "受半/一" },
			{ "受一球/球半", "受一/球半" },
			{ "受球半/两球", "受球半/两" },
			{ "受两球/两球半", "受两/两球半" },
			{ "受球半/两球", "受球半/两球" }, };

	public static final double[][] OP_YP_TABLE =
	{
			{ 2.500, 0.750, 2.550, 0.775, 2.600, 0.800, 2.650, 0.825, 2.700, 0.850, 2.750, 0.875, 2.800, 0.900, 2.850,
					0.925, 2.900, 0.950, 2.950, 0.975, 3.000, 1.000, 3.050, 1.025, 3.100, 1.050, 3.150, 1.075, 3.200,
					1.100 }, 		//平手
			{ 2.000, 0.750, 2.030, 0.775, 2.070, 0.800, 2.100, 0.825, 2.130, 0.850, 2.170, 0.875, 2.200, 0.900, 2.230,
					0.925, 2.270, 0.950, 2.300, 0.975, 2.330, 1.000, 2.370, 1.025, 2.400, 1.050, 2.430, 1.075, 2.460,
					1.100 },		//平半
			{ 1.750, 0.750, 1.780, 0.775, 1.800, 0.800, 1.830, 0.825, 1.850, 0.850, 1.880, 0.875, 1.900, 0.900, 1.930,
					0.925, 1.950, 0.950, 1.980, 0.975, 2.000, 1.000, 2.030, 1.025, 2.050, 1.050, 2.080, 1.075, 2.100,
					1.100 },		//半球
			{ 1.500, 0.750, 1.520, 0.775, 1.530, 0.800, 1.550, 0.825, 1.570, 0.850, 1.580, 0.875, 1.600, 0.900, 1.620,
					0.925, 1.630, 0.950, 1.650, 0.975, 1.670, 1.000, 1.680, 1.025, 1.700, 1.050, 1.720, 1.075, 1.730,
					1.100 },		//半/一
			{ 1.380, 0.750, 1.390, 0.775, 1.400, 0.800, 1.410, 0.825, 1.430, 0.850, 1.440, 0.875, 1.450, 0.900, 1.460,
					0.925, 1.480, 0.950, 1.490, 0.975, 1.500, 1.000, 1.510, 1.025, 1.530, 1.050, 1.540, 1.075, 1.550,
					1.100 },		//一球
			{ 1.300, 0.750, 1.310, 0.775, 1.320, 0.800, 1.330, 0.825, 1.340, 0.850, 1.350, 0.875, 1.360, 0.900, 1.370,
					0.925, 1.380, 0.950, 1.390, 0.975, 1.400, 1.000, 1.410, 1.025, 1.420, 1.050, 1.430, 1.075, 1.440,
					1.100 },		//一/球半
			{ 1.250, 0.750, 1.260, 0.775, 1.270, 0.800, 1.280, 0.825, 1.280, 0.850, 1.290, 0.875, 1.300, 0.900, 1.310,
					0.925, 1.320, 0.950, 1.330, 0.975, 1.330, 1.000, 1.340, 1.025, 1.350, 1.050, 1.360, 1.075, 1.370,
					1.100 },		//球半
			{ 1.210, 0.750, 1.220, 0.775, 1.220, 0.800, 1.240, 0.825, 1.240, 0.850, 1.250, 0.875, 1.260, 0.900, 1.260,
					0.925, 1.270, 0.950, 1.280, 0.975, 1.290, 1.000, 1.290, 1.025, 1.300, 1.050, 1.310, 1.075, 1.310,
					1.100 },		//球半/两
			{ 1.190, 0.750, 1.190, 0.775, 1.200, 0.800, 1.210, 0.825, 1.210, 0.850, 1.220, 0.875, 1.230, 0.900, 1.230,
					0.925, 1.240, 0.950, 1.240, 0.975, 1.250, 1.000, 1.260, 1.025, 1.260, 1.050, 1.270, 1.075, 1.280,
					1.100 },		//两球
			{ 1.170, 0.750, 1.170, 0.775, 1.180, 0.800, 1.180, 0.825, 1.190, 0.850, 1.190, 0.875, 1.200, 0.900, 1.210,
					0.925, 1.210, 0.950, 1.220, 0.975, 1.220, 1.000, 1.230, 1.025, 1.230, 1.050, 1.240, 1.075, 1.240,
					1.100 },		//两/两球半
			{ 1.150, 0.750, 1.150, 0.775, 1.160, 0.800, 1.160, 0.825, 1.170, 0.850, 1.170, 0.875, 1.180, 0.900, 1.190,
					0.925, 1.190, 0.950, 1.200, 0.975, 1.200, 1.000, 1.210, 1.025, 1.210, 1.050, 1.220, 1.075, 1.220,
					1.100 },		//两球半
			{ 1.140, 0.750, 1.140, 0.775, 1.150, 0.800, 1.150, 0.825, 1.160, 0.850, 1.160, 0.875, 1.160, 0.900, 1.170,
					0.925, 1.170, 0.950, 1.180, 0.975, 1.180, 1.000, 1.190, 1.025, 1.190, 1.050, 1.200, 1.075, 1.200,
					1.100 },		//两球半/三球
			{ 1.130, 0.750, 1.130, 0.775, 1.130, 0.800, 1.130, 0.825, 1.140, 0.850, 1.140, 0.875, 1.150, 0.900, 1.150,
					0.925, 1.150, 0.950, 1.160, 0.975, 1.170, 1.000, 1.170, 1.025, 1.170, 1.050, 1.180, 1.075, 1.180,
					1.100 }, };		//三球
	public static final int TYPE_NUMBER = NAME_YPS_HOME.length;
	public static final int CONVERSION_NUMBER = OP_YP_TABLE[0].length / 2;

	public static Map<String, OddsMappings> OP_YP_LIST = new HashMap<>();

	static
	{
		for (int i = 0; i < TYPE_NUMBER; i++)
		{
			for (int j = 0; j < CONVERSION_NUMBER; j++)
			{
				OddsMapping odds = new OddsMapping(NAME_YPS_HOME[i], OP_YP_TABLE[i][j * 2 + 1], OP_YP_TABLE[i][j * 2]);
				addOrCreateNewOddsList(odds);
			}
		}
	};

	/*
	 * public static final double[][] OP_YP_TABLE = { { 2.50, 0.75, 2.00, 0.75,
	 * 1.75, 0.75, 1.50, 0.75, 1.38, 0.75, 1.30, 0.75, 1.25, 0.75, 1.21, 0.75,
	 * 1.19, 0.75, 1.17, 0.75, 1.15, 0.75, 1.14, 0.75, 1.13, 0.75 }, { 2.55,
	 * 0.775, 2.03, 0.775, 1.78, 0.775, 1.52, 0.775, 1.39, 0.775, 1.31, 0.775,
	 * 1.26, 0.775, 1.22, 0.775, 1.19, 0.775, 1.17, 0.775, 1.15, 0.775, 1.14,
	 * 0.775, 1.13, 0.775 }, { 2.60, 0.8, 2.07, 0.80, 1.80, 0.8, 1.53, 0.80,
	 * 1.40, 0.80, 1.32, 0.80, 1.27, 0.80, 1.22, 0.80, 1.20, 0.80, 1.18, 0.80,
	 * 1.16, 0.80, 1.15, 0.80, 1.13, 0.8 }, { 2.65, 0.825, 2.10, 0.825, 1.83,
	 * 0.825, 1.55, 0.825, 1.41, 0.825, 1.33, 0.825, 1.28, 0.825, 1.24, 0.825,
	 * 1.21, 0.825, 1.18, 0.825, 1.16, 0.825, 1.15, 0.825, 1.13, 0.825 }, {
	 * 2.70, 0.85, 2.13, 0.85, 1.85, 0.85, 1.57, 0.85, 1.43, 0.85, 1.34, 0.85,
	 * 1.28, 0.85, 1.24, 0.85, 1.21, 0.85, 1.19, 0.85, 1.17, 0.85, 1.16, 0.85,
	 * 1.14, 0.85 }, { 2.75, 0.875, 2.17, 0.875, 1.88, 0.875, 1.58, 0.875, 1.44,
	 * 0.875, 1.35, 0.875, 1.29, 0.875, 1.25, 0.875, 1.22, 0.875, 1.19, 0.875,
	 * 1.17, 0.875, 1.16, 0.875, 1.14, 0.875 }, { 2.80, 0.9, 2.20, 0.90, 1.90,
	 * 0.90, 1.60, 0.90, 1.45, 0.90, 1.36, 0.90, 1.30, 0.90, 1.26, 0.90, 1.23,
	 * 0.90, 1.20, 0.90, 1.18, 0.90, 1.16, 0.9, 1.15, 0.9 }, { 2.85, 0.925,
	 * 2.23, 0.925, 1.93, 0.925, 1.62, 0.925, 1.46, 0.925, 1.37, 0.925, 1.31,
	 * 0.925, 1.26, 0.925, 1.23, 0.925, 1.21, 0.925, 1.19, 0.925, 1.17, 0.925,
	 * 1.15, 0.925 }, { 2.90, 0.95, 2.27, 0.95, 1.95, 0.95, 1.63, 0.95, 1.48,
	 * 0.95, 1.38, 0.95, 1.32, 0.95, 1.27, 0.95, 1.24, 0.95, 1.21, 0.95, 1.19,
	 * 0.95, 1.17, 0.95, 1.15, 0.95 }, { 2.95, 0.975, 2.30, 0.975, 1.98, 0.975,
	 * 1.65, 0.975, 1.49, 0.975, 1.39, 0.975, 1.33, 0.975, 1.28, 0.975, 1.24,
	 * 0.975, 1.22, 0.975, 1.2, 0.975, 1.18, 0.975, 1.16, 0.975 }, { 3.00, 1.00,
	 * 2.33, 1.00, 2.00, 1.00, 1.67, 1.00, 1.5, 1.00, 1.4, 1.00, 1.33, 1.00,
	 * 1.29, 1.00, 1.25, 1.00, 1.22, 1.00, 1.20, 1.00, 1.18, 1.00, 1.17, 1.00 },
	 * { 3.05, 1.025, 2.37, 1.025, 2.03, 1.025, 1.68, 1.025, 1.51, 1.025, 1.41,
	 * 1.025, 1.34, 1.025, 1.29, 1.025, 1.26, 1.025, 1.23, 1.025, 1.21, 1.025,
	 * 1.19, 1.025, 1.17, 1.025 }, { 3.10, 1.05, 2.40, 1.05, 2.05, 1.05, 1.70,
	 * 1.05, 1.53, 1.05, 1.42, 1.05, 1.35, 1.05, 1.30, 1.05, 1.26, 1.05, 1.23,
	 * 1.05, 1.21, 1.05, 1.19, 1.05, 1.17, 1.05 }, { 3.15, 1.075, 2.43, 1.075,
	 * 2.08, 1.075, 1.72, 1.075, 1.54, 1.075, 1.43, 1.075, 1.36, 1.075, 1.31,
	 * 1.075, 1.27, 1.075, 1.24, 1.075, 1.22, 1.075, 1.2, 1.075, 1.18, 1.075 },
	 * { 3.20, 1.1, 2.46, 1.10, 2.10, 1.10, 1.73, 1.10, 1.55, 1.10, 1.44, 1.10,
	 * 1.37, 1.10, 1.31, 1.10, 1.28, 1.10, 1.24, 1.10, 1.22, 1.10, 1.20, 1.10,
	 * 1.18, 1.10 } };
	 */

	public static final String[] WATER_LEVEL =
	{ "超低水", "低水", "中低水", "中水", "中高水", "高水", "超高水" };
	public static final double[][] WATER_LEVEL_THRESHOLD =
	{
			{ 0.000, 0.75999 },
			{ 0.760, 0.84999 },
			{ 0.850, 0.89999 },
			{ 0.900, 0.94999 },
			{ 0.950, 0.99999 },
			{ 1.000, 1.99999 },
			{ 2.000, 10000.0 } };

	/** 不进行统计的数据 */
	static List<String> excludeGids = new ArrayList<>();
	static
	{
		excludeGids.add("0");
		excludeGids.add("-2");
	}

	/** 默认的统计区间值 */
	public final static double DEFAUL_OP_INTERVAL = 0.05;
	public static int STAT_OP_KEY_LEN = 2;
	public final static int OVER_ERROR_TIMES = 2;

	/**
	 * 将列表中的赔率值，按照公司的编号获得唯一的值
	 * 
	 * @param odds
	 *            赔率列表值
	 * @return 数据列表
	 */
	public static <T extends OddsItem> List<T> getSingleCorpOddList(List<T> odds, long last)
	{
		OddsChecker<T> checker = new OddsChecker<>();

		List<T> list = new ArrayList<>();
		for (T t : odds)
		{
			long time = t.getLastTimeValue();
			if (time > last)
				continue;

			checker.setMid(t.getMid());
			checker.setGid(t.getGid());
			T t2 = ArraysUtil.getSameObject(list, checker);
			if (time > t2.getLastTimeValue())
			{
				list.remove(t2);
				list.add(t);
			}
		}
		return list;
	}

	/**
	 * 将列表中的赔率值，按照公司的编号获得唯一的值
	 * 
	 * @param odds
	 *            赔率列表值
	 * @return 数据列表
	 */
	public static <T extends OddsItem> List<T> getLatestSingleCorpOddList(List<T> odds)
	{
		long last = System.currentTimeMillis();
		return getSingleCorpOddList(odds, last);
	}

	/**
	 * 计算比赛的欧赔数据方差等数据
	 * 
	 * @param mid
	 *            比赛编号
	 * @param ops
	 *            欧赔数据
	 * @return 比赛方差值数据
	 */
	public static List<OpVariance> computeMatchOpVariance(String mid, List<Op> ops)
	{
		return computeMatchOpVariance(mid, ops, DEFAUL_OP_INTERVAL, STAT_OP_KEY_LEN, true);
	}

	/**
	 * 计算比赛的欧赔数据方差等数据
	 * 
	 * @param mid
	 *            比赛编号
	 * @param ops
	 *            欧赔数据
	 * @return 比赛方差值数据
	 */
	public static List<OpVariance> computeMatchOpVariance(String mid, List<Op> ops, double interval)
	{
		return computeMatchOpVariance(mid, ops, interval, STAT_OP_KEY_LEN, true);
	}

	/**
	 * 计算比赛的欧赔数据方差等数据
	 * 
	 * @param mid
	 *            比赛编号
	 * @param ops
	 *            欧赔数据
	 * @return 比赛方差值数据
	 */
	public static MatchOpVariance computeIssueMatchOpVariance(IssueMatch match, List<Op> ops)
	{
		return computeIssueMatchOpVariance(match, ops, DEFAUL_OP_INTERVAL);
	}

	/**
	 * 计算比赛的欧赔数据方差等数据
	 * 
	 * @param mid
	 *            比赛编号
	 * @param ops
	 *            欧赔数据
	 * @return 比赛方差值数据
	 */
	public static MatchOpVariance computeIssueMatchOpVariance(IssueMatch match, List<Op> ops, double interval)
	{
		List<OpVariance> vars = computeMatchOpVariance(match.getMid(), ops, interval, STAT_OP_KEY_LEN, true);
		return new MatchOpVariance(match, vars);
	}

	/**
	 * 计算比赛数据的统计值
	 * 
	 * @param mid
	 *            比赛编号
	 * @param ops
	 *            比赛及欧赔数据
	 * @param interval
	 * @param keyLen
	 * @return
	 */
	public static List<OpVariance> computeMatchOpVariance(String mid, List<Op> ops, double interval, int keyLen,
			boolean fixAvg)
	{
		int size = ops.size();

		double[][] oddsValues = new double[6][size];
		for (int i = 0; i < size; i++)
		{
			Op op = ops.get(i);
			oddsValues[0][i] = op.getFirstwinodds();
			oddsValues[1][i] = op.getFirstdrawodds();
			oddsValues[2][i] = op.getFirstloseodds();

			oddsValues[3][i] = op.getWinodds();
			oddsValues[4][i] = op.getDrawodds();
			oddsValues[5][i] = op.getLoseodds();
		}

		List<OpVariance> variances = new ArrayList<>();

		OpVariance var = new OpVariance();
		var.setWin(computeVariance(SoccerConstants.ODDS_KEY_NAMES[0], OddsValueType.WIN, oddsValues[0], interval,
				keyLen, fixAvg));
		var.setDraw(computeVariance(SoccerConstants.ODDS_KEY_NAMES[1], OddsValueType.DRAW, oddsValues[1], interval,
				keyLen, fixAvg));
		var.setLose(computeVariance(SoccerConstants.ODDS_KEY_NAMES[2], OddsValueType.LOSE, oddsValues[2], interval,
				keyLen, fixAvg));

		variances.add(var);
		var = new OpVariance();
		var.setWin(computeVariance(SoccerConstants.ODDS_KEY_NAMES[3], OddsValueType.WIN, oddsValues[3], interval,
				keyLen, fixAvg));
		var.setDraw(computeVariance(SoccerConstants.ODDS_KEY_NAMES[4], OddsValueType.DRAW, oddsValues[4], interval,
				keyLen, fixAvg));
		var.setLose(computeVariance(SoccerConstants.ODDS_KEY_NAMES[5], OddsValueType.LOSE, oddsValues[5], interval,
				keyLen, fixAvg));
		variances.add(var);
		return variances;
	}

	/**
	 * 计算统计值数据
	 * 
	 * @param name
	 * @param type
	 * @param values
	 * @param interval
	 * @param keyLen
	 * @param fixAvg
	 * @return
	 */
	protected static OddsVariance computeVariance(String name, OddsValueType type, double[] values, double interval,
			int keyLen, boolean fixAvg)
	{
		OddsVariance opVariance = new OddsVariance(name, type);
		Variance variance = Statistics.statVar(name, values, interval, OVER_ERROR_TIMES);
		Freqs winCounter = Statistics.statValueInterval(variance, values, interval, keyLen, fixAvg);
		opVariance.setVariance(variance);
		opVariance.setCounter(winCounter);
		variance.setScale();
		return opVariance;
	}

	/**
	 * 计算欧赔数据的统计值
	 * 
	 * @param ops
	 */
	public static List<Variance> statOpValues(List<Op> ops)
	{
		return statOpValues(ops, DEFAUL_OP_INTERVAL);
	}

	/**
	 * 计算欧赔数据的统计值
	 * 
	 * @param ops
	 */
	public static List<Variance> statOpValues(List<Op> ops, double interval)
	{
		int size = ops.size();
		double[] firstwinodds = new double[size];
		double[] firstdrawodds = new double[size];
		double[] firstloseodds = new double[size];

		double[] winodds = new double[size];
		double[] drawodds = new double[size];
		double[] loseodds = new double[size];

		for (int i = 0; i < size; i++)
		{
			Op op = ops.get(i);
			firstwinodds[i] = op.getFirstwinodds();
			firstdrawodds[i] = op.getFirstdrawodds();
			firstloseodds[i] = op.getFirstloseodds();

			winodds[i] = op.getWinodds();
			drawodds[i] = op.getDrawodds();
			loseodds[i] = op.getLoseodds();
		}

		Variance fwinVar = Statistics.statVar(SoccerConstants.ODDS_KEY_NAMES[0], firstwinodds, interval,
				OVER_ERROR_TIMES);
		Variance fdrawVar = Statistics.statVar(SoccerConstants.ODDS_KEY_NAMES[1], firstdrawodds, interval,
				OVER_ERROR_TIMES);
		Variance floseVar = Statistics.statVar(SoccerConstants.ODDS_KEY_NAMES[2], firstloseodds, interval,
				OVER_ERROR_TIMES);

		Variance winVar = Statistics.statVar(SoccerConstants.ODDS_KEY_NAMES[3], winodds, interval, OVER_ERROR_TIMES);
		Variance drawVar = Statistics.statVar(SoccerConstants.ODDS_KEY_NAMES[4], drawodds, interval, OVER_ERROR_TIMES);
		Variance loseVar = Statistics.statVar(SoccerConstants.ODDS_KEY_NAMES[5], loseodds, interval, OVER_ERROR_TIMES);

		List<Variance> variances = new ArrayList<>();
		variances.add(fwinVar);
		variances.add(fdrawVar);
		variances.add(floseVar);
		variances.add(winVar);
		variances.add(drawVar);
		variances.add(loseVar);

		return variances;
	}
	
	/**
	 * 通过概率创建欧赔数据
	 * @param probs
	 * @return
	 */
	public static OddsElement createOpItem(double[] probs)
	{
		float[] p = {(float)probs[0], (float)probs[1], (float)probs[2]};
		return createOpItem(p);
	}
	
	/**
	 * 通过概率创建欧赔数据
	 * @param probs
	 * @return
	 */
	public static OddsElement createOpItem(float[] probs)
	{
		OddsElement item = new OddsElement(SoccerConstants.ODDS_TYPE_OP);
		item.setGname("理论值");
		item.setGid("-100");
		item.setSource("rank");
		
		float ratio = 0.94f;
		float win = ratio / probs[0];
		float draw = ratio / probs[1];
		float lose = ratio / probs[2];
		item.setValue(0, win);
		item.setValue(1, draw);
		item.setValue(2, lose);
		item.setValue(3, win);
		item.setValue(4, draw);
		item.setValue(5, lose);
		item.setValue(6, probs[0]);
		item.setValue(7, probs[1]);
		item.setValue(8, probs[2]);
		item.setValue(9, 0);
		item.setValue(10, 0);
		item.setValue(11, 0);
		item.setValue(12, ratio);
		return item;
	}

	/**
	 * 创建一个欧赔数据元素,在这里主要是用于网络上传输，减少数据量
	 * 
	 * @param op
	 *            欧赔原始数据
	 * @param first
	 *            是否用初始记录
	 * @return 欧赔数据元素
	 */
	public static OddsElement createOpItem(Op op, int index)
	{
		OddsElement item = new OddsElement(SoccerConstants.ODDS_TYPE_OP);
		item.setGname(op.getGname());
		item.setGid(op.getGid());
		item.setSource(op.getSource());
		item.setIndex(index);
		item.setFirsttime(op.getFirsttime());

		long t = NumberUtil.parseLong(op.getLasttime());
		if (t > 0)
		{
			item.setTime(DateUtil.formatDate(t));
		}
		item.setValue(0, op.getFirstwinodds());
		item.setValue(1, op.getFirstdrawodds());
		item.setValue(2, op.getFirstloseodds());
		item.setValue(3, op.getWinodds());
		item.setValue(4, op.getDrawodds());
		item.setValue(5, op.getLoseodds());
		item.setValue(6, op.getWinprob());
		item.setValue(7, op.getDrawprob());
		item.setValue(8, op.getLoseprob());
		item.setValue(9, op.getWinkelly());
		item.setValue(10, op.getDrawkelly());
		item.setValue(11, op.getLosekelly());
		item.setValue(12, op.getLossratio());

		return item;
	}

	/**
	 * 创建一个亚盘元素
	 * 
	 * @param yp
	 *            亚盘数据
	 * @param index
	 *            序号
	 * @return 亚盘元素
	 */
	public static OddsElement createYpItem(Yp yp, int index)
	{
		OddsElement item = new OddsElement(SoccerConstants.ODDS_TYPE_YP);
		item.setGid(yp.getGid());
		item.setGname(yp.getGname());
		item.setSource(yp.getSource());
		item.setIndex(index);
		item.setFirsttime(yp.getFirsttime());
		long t = NumberUtil.parseLong(yp.getLasttime());
		if (t > 0)
		{
			item.setTime(DateUtil.formatDate(t));
		}

		//item.setFirsthandicap(formatHandicapName(yp.getFirsthandicap()));
		item.setValue(0, yp.getFirstwinodds());
		item.setValue(1, getHandicapValue(formatHandicapName(yp.getFirsthandicap())));
		item.setValue(2, yp.getFirstloseodds());

		item.setValue(3, yp.getWinodds());
		item.setValue(4, getHandicapValue(formatHandicapName(yp.getHandicap())));
		item.setValue(5, yp.getLoseodds());
		
		item.setValue(6, yp.getWinprob());
		item.setValue(7, yp.getLoseprob());
		item.setValue(8, yp.getWinkelly());
		item.setValue(9, yp.getLosekelly());
		item.setValue(10, yp.getLossratio());

		return item;
	}

	/**
	 * 让球中文名的标准化处理
	 * 
	 * @param name
	 *            让球名称
	 * @return 标准名称
	 */
	public static String formatHandicapName(String name)
	{
		if (StringUtils.isEmpty(name))
		{
			return "";
		}
		for (String[] strings : HANDICAP_STANDRARS)
		{
			if (name.equalsIgnoreCase(strings[0]))
			{
				return strings[1];
			}
		}

		for (String string : NAME_YPS)
		{
			if (name.equalsIgnoreCase(string))
			{
				return name;
			}
		}

		return name;
		// throw new IllegalArgumentException("The name '" + name + "' is not a
		// standard Handicap name.");
	}

	/**
	 * 计算亚盘的水位值
	 * 
	 * @param oddsYp
	 *            亚盘值
	 * @return 水位水平
	 */
	public static String getWaterLevel(double oddsYp)
	{
		int l = WATER_LEVEL_THRESHOLD.length;
		for (int i = 0; i < l; i++)
		{
			if (WATER_LEVEL_THRESHOLD[i][0] <= oddsYp && WATER_LEVEL_THRESHOLD[i][1] >= oddsYp)
			{
				return WATER_LEVEL[i];
			}

		}
		return "Unknown";
	}

	/**
	 * 通过欧赔赔率来计算亚赔正常对应的数据
	 * 
	 * @param op
	 *            亚赔数据
	 * @return 赔率值
	 */
	public static List<OddsMapping> getOddsFromOp(double op)
	{

		List<OddsMapping> list = new ArrayList<>();
		if (op > 3.200)
		{
			String name = NAME_YPS[0];
			OddsMappings l = OP_YP_LIST.get(name);
			int size = l.size();
			list.add(l.get(size - 1));
		}
		else if (op < 1.13)
		{
			String name = NAME_YPS[TYPE_NUMBER - 1];
			OddsMappings l = OP_YP_LIST.get(name);
			list.add(l.get(0));
		}
		else
		{
			for (String key : OP_YP_LIST.keySet())
			{
				OddsMappings l = OP_YP_LIST.get(key);
				if (!l.contains(op))
				{
					continue;
				}

				List<OddsMapping> list2 = l.getOddsByOp(op);
				if (list2.size() > 0)
				{
					list.addAll(list2);
				}
			}
		}
		return list;
	}

	/**
	 * 通过亚盘数据计算正常对应的欧赔赔率
	 * 
	 * @param handicap
	 *            让球数
	 * @param yp
	 *            亚盘
	 * @return 赔率对应值
	 */
	public static OddsMapping getOddsFromYp(String handicap, double yp)
	{
		for (String key : OP_YP_LIST.keySet())
		{
			OddsMappings l = OP_YP_LIST.get(key);
			if (l.isEqualName(handicap))
			{
				return l.getOddsByYp(yp);
			}
		}
		return null;
	}

	/**
	 * 计算亚盘让球的价值
	 * 
	 * @param handicap
	 *            让球名称
	 * @return 让球价值，当不存在该让球名称时，则返回－1.0.
	 */
	public static float getHandicapValue(String handicap)
	{
		String stdName = formatHandicapName(handicap);
		if(StringUtils.isEmpty(stdName))
		{
			return -100.0f;
		}
		for (int i = 0; i < TYPE_NUMBER; i++)
		{
			if (stdName.equalsIgnoreCase(NAME_YPS[i]))
			{
				return NAME_YPS_VALUES[i];
			}
		}
		return -1.0f;
	}

	/**
	 * 添加或创建一个数据列表
	 * 
	 * @param odds
	 *            数据转换元素
	 */
	protected static void addOrCreateNewOddsList(OddsMapping odds)
	{
		for (String name : OP_YP_LIST.keySet())
		{
			if (name.equalsIgnoreCase(odds.getHandicap()))
			{
				OddsMappings list = OP_YP_LIST.get(name);
				list.add(odds);
				return;
			}
		}

		OddsMappings list = new OddsMappings(odds.getHandicap());
		list.add(odds);
		OP_YP_LIST.put(odds.getHandicap(), list);
	}

	/**
	 * 判断两个赔率值是否相等
	 * 
	 * @param value1
	 *            赔率1
	 * @param value2
	 *            赔率2
	 * @return 是否相等的标志
	 */
	protected static boolean isEqual(double value1, double value2)
	{
		return NumberUtil.isEqual(value1, value2, EPS);
	}

	/**
	 * 判断一个值是否等于或小于某一个值
	 * 
	 * @param value1
	 *            值1
	 * @param value2
	 *            值2
	 * @return 是否小于或等于的标志
	 */
	protected static boolean isEqualOrLessthan(double value1, double value2)
	{
		return NumberUtil.isEqual(value1, value2, EPS) || Math.abs(value1) < Math.abs(value2);
	}
	
	/**
	 * 计算欧赔与亚盘之间的差异数据
	 * @param ops
	 * @param yps
	 */
	public static void computeOddsError(List<Op> ops, List<Yp> yps)
	{
		
	}
	
	/**
	 * 判断两个让球名称是否相等
	 * @param handicap1
	 * @param handicap2
	 * @return
	 */
	public static boolean isSameHandicap(String handicap1, String handicap2)
	{
		handicap1 = formatHandicapName(handicap1);
		handicap2 = formatHandicapName(handicap2);
		return handicap1.equals(handicap2);
	}
	
	/**
	 * 同盘的数据
	 * @param value
	 * @param handicap
	 * @return
	 */
	public static YpValue formatYpValue(YpValue value, String handicap)
	{
		//如果相等，则不用转换
		if(isSameHandicap(handicap, value.getHandicap()))
		{
			return value;
		}
		
		float v = getHandicapValue(value.getHandicap());	
		float handicapValue = getHandicapValue(handicap);		
		
		boolean isNegValue = handicapValue < 0.0;
		
		float upperValue = value.getWinodds();
		if(v < 0.0)
		{
			v = -v;
			upperValue = value.getLoseodds();
		}
		
		float op = (float)mappingOpValue(upperValue, v);
		float upper = (float)mappingYpValue(op, Math.abs(handicapValue));
		upper = NumberUtil.setFloatScale(2, upper);
		
		float lower = (float)(1.0 * value.getLossratio() / (1- 1.0 * value.getLossratio() / upper ));
		lower = NumberUtil.setFloatScale(2, lower);
		
		if(isNegValue)
		{
			return new YpValue(handicap, lower, upper);
		}
		else
		{
			return new YpValue(handicap, upper, lower);
		}
	}
	
	/**
	 * 计算两个让球盘口的差值等级
	 * @param handicap1
	 * @param handicap2
	 * @return
	 */
	public static int computeHandicapError(String handicap1, String handicap2)
	{
		float v1 = getHandicapValue(handicap1);
		float v2 = getHandicapValue(handicap2);
		return (int) Math.abs((v1 - v2) / 0.25);
	}
	
	/**
	 * 获得亚盘转换数据
	 * @param opUpperValue
	 * @param handicapValue
	 * @return
	 */
	public static double mappingYpValue(float opUpperValue, float handicapValue)
	{
		int index = (int)Math.abs((handicapValue / 0.25));
		if(index >= OP_YP_TABLE.length)
		{
			throw new UnsupportedOperationException("The handivap " + handicapValue + " is not in the list values.");
		}
		return getYpValue(OP_YP_TABLE[index], opUpperValue);
	}

	/**
	 * 获得亚盘上盘数据
	 * @param tables 转换表
	 * @param upper 上盘数据
	 * @return
	 */
	protected static double getYpValue(double[] tables, float upper)
	{
		int len = tables.length / 2;
		/*if(upper < tables[0] || upper > tables[len * 2 - 2])
		{
			throw new UnsupportedOperationException("The handivap " + upper + " is not in the tables values.");
		}*/		
		double opValueInterval = tables[len * 2 - 2] - tables[0];
		double ypValueInterval = tables[len * 2 - 1] - tables[1];
		return tables[1] + (upper - tables[0]) * ypValueInterval / opValueInterval;
	}
	
	/**
	 * 获得转换表
	 * @param ypUpperValue 亚盘上盘数据
	 * @param handicapValue 让球数据
	 * @return
	 */
	protected static double mappingOpValue(float ypUpperValue, float handicapValue)
	{
		int index = (int)Math.abs((handicapValue / 0.25));
		if(index >= OP_YP_TABLE.length)
		{
			throw new UnsupportedOperationException("The handivap " + handicapValue + " is not in the list values.");
		}		
		return getOpValue(OP_YP_TABLE[index], ypUpperValue);
	}
	
	
	/**
	 * 获得对应的OP赔率数据
	 * @param tables 转换表格
	 * @param upper 数据值
	 * @return
	 */
	protected static double getOpValue(double[] tables, float upper)
	{
		int len = tables.length / 2;
		/*if(upper < tables[1] || upper > tables[len * 2 - 1])
		{
			throw new UnsupportedOperationException("The handivap " + upper + " is not in the tables values.");
		}*/
		double opValueInterval = tables[len * 2 - 2] - tables[0];
		double ypValueInterval = tables[len * 2 - 1] - tables[1];
		
		return tables[0] + (upper - tables[1]) * opValueInterval / ypValueInterval;
	}
}
