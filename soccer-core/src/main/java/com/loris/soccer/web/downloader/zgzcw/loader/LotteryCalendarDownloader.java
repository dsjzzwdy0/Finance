package com.loris.soccer.web.downloader.zgzcw.loader;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.DateUtil;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.bean.data.table.lottery.LotteryCalendar;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwSoccerDownloader;
import com.loris.soccer.web.downloader.zgzcw.page.LotteryCalendarWebPage;
import com.loris.soccer.web.downloader.zgzcw.parser.LotteryCalendarWebPageParser;

/**
 * 足彩、竞彩、北单日历下载器
 * 
 * @author jiean
 *
 */
public class LotteryCalendarDownloader extends ZgzcwSoccerDownloader
{
	private static Logger logger = Logger.getLogger(LotteryCalendarDownloader.class);

	/**
	 * 初始化下载器
	 */
	@Override
	public boolean prepare()
	{
		if (soccerManager == null || soccerWebPageManager == null)
		{
			logger.info("TeamDownloader is not initialized, stop.");
			return false;
		}

		Date stDate = DateUtil.tryToParseDate(start);
		Date edDate = DateUtil.tryToParseDate(end);

		// 如果开始时间设置为空,则不进行计算
		if (stDate == null)
		{
			logger.info("The Start Date is not set, stop.");
			return false;
		}

		// 如果结束日期没有设置，则取明天作为结束日期
		if (edDate == null)
		{
			edDate = DateUtil.add(new Date(), Calendar.DAY_OF_YEAR, 1);
		}

		// 日期
		List<Date> days = DateUtil.getBetweenDates(stDate, edDate);
		// logger.info("DayList: " + days);

		// 去除已经下载的数据
		List<LotteryCalendar> calendars = soccerManager.getLotteryCalendars(start, DateUtil.formatDay(edDate));
		for (LotteryCalendar cal : calendars)
		{
			// 空的数据
			if (StringUtils.isEmpty(cal.getJcissue()) || 0 == cal.getJc() || 0 == cal.getBd())
			{
				continue;
			}
			// logger.info("Calendar: " + cal);

			int size = days.size();
			for (int i = 0; i < size; i++)
			{
				Date date = days.get(i);
				if (DateUtil.formatDay(date).equals(cal.getDate()))
				{
					days.remove(i);
					break;
				}
			}
		}

		if (days.size() <= 0)
		{
			logger.info("There are no page need to be downloaded.");
			return false;
		}

		stDate = days.get(0);
		edDate = days.get(days.size() - 1);
		int diffNum = DateUtil.getDiscrepantDays(stDate, edDate) + 1;

		start = DateUtil.formatDay(stDate);
		LotteryCalendarWebPage page = creator.createLotteryCalendarWebPage(start, diffNum);
		pages.put(page);
		
		return true;
		//totalSize = 1;
		//logger.info("There are " + totalSize + " to be downloaded.");
	}

	/**
	 * After download the page.
	 * 
	 * @param page
	 *            Page Value.
	 * @param flag
	 *            The flag value.
	 */
	@Override
	public void afterDownload(WebPage page, boolean flag)
	{
		if (!flag)
		{
			// this will do nothing.
			return;
		}

		LotteryCalendarWebPage page2 = (LotteryCalendarWebPage) page;
		LotteryCalendarWebPageParser parser = new LotteryCalendarWebPageParser();

		if (parser.parseWebPage(page2))
		{
			List<LotteryCalendar> calendars = parser.getCalendars();
			synchronized (soccerManager)
			{
				soccerManager.addOrUpdateLotteryCalendars(calendars);
			}
		}

		super.afterDownload(page2, flag);

	}
}
