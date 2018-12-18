package com.loris.soccer.analysis;

import java.util.Calendar;
import java.util.Date;

/**
 * 竞彩比赛数据分析
 * 
 * @author jiean
 *
 */
public class AnalyzerFactory
{
	// private static Logger logger = Logger.getLogger(AnalyzerFactory.class);

	/**
	 * 计算起始时间，这里将以yearToInterval年为单位，获得该年之前的起始日期
	 * 
	 * @param d
	 * @param yearToInterval
	 * @return
	 */
	protected Date getFirstDateBefore(Date d, int yearToInterval)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.YEAR, -yearToInterval);
		// calendar.set(calendar.get(Calendar.YEAR), 0, 1, 0, 0, 0);
		return calendar.getTime();
	}
}
