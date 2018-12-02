package com.loris.soccer.analysis.util;

import java.util.Date;
import java.util.Calendar;

import com.loris.base.util.DateUtil;
import com.loris.base.util.TextCalculator;
import com.loris.soccer.bean.item.IssueMatch;

/**
 * 竞彩比赛的实用处理类
 * @author dsj
 *
 */
public class IssueMatchUtil
{
	/**
	 * 获得当前比赛时间期号
	 * @return 比赛期号
	 */
	public static String getCurrentIssue()
	{
		return getIssueDay(new Date());
	}
	
	/**
	 * 获得比赛的投注日期
	 * @param matchtime
	 * @return
	 */
	public static String getIssueDay(String matchtime)
	{
		Date date = DateUtil.tryToParseDate(matchtime);
		if(date == null)
		{
			return "";
		}
		return getIssueDay(date);
	}
	
	public static String getIssueDay(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		if(hour < 11 || (hour == 11) && (minute < 30))
		{
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			Date d = calendar.getTime();
			return DateUtil.DAY_FORMAT.format(d);
		}
		else
		{
			return DateUtil.DAY_FORMAT.format(calendar.getTime());
		}
	}
	
	/**
	 * 获得竞彩比赛的结束时间
	 * @param issus 比赛的期号
	 * @return 结束时间
	 */
	public static String getEndTime(String issus)
	{
		Date date = DateUtil.tryToParseDate(issus);
		date = DateUtil.add(date, Calendar.DAY_OF_MONTH, 1);
		String string = DateUtil.formatDay(date) + " 11:30";
		return string;
	}
	
	/**
	 * 竞彩比赛数据的开始时间
	 * @param issue 竞彩比赛的编号
	 * @return 开始时间
	 */
	public static String getStartTime(String issue)
	{
		return issue + " 11:30";
	}
	
	/**
	 * JcMatch比赛数据的映射分析,通过比赛双方两个球队的名称进行分析,计算球队名称的相似度,
	 * 相似度大的则匹配。
	 * @param str1 字符串1
	 * @param str2 字符串2
	 * @return 相似度
	 */
	public static double computeSimilarity(IssueMatch match1, IssueMatch match2)
	{
		double s1 = TextCalculator.getSimilarity(match1.getHomename(), match2.getHomename());
		double s2 = TextCalculator.getSimilarity(match1.getClientname(), match2.getClientname());
		return s1 * s2;
	}
}
