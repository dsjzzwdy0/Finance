package com.loris.soccer.web.downloader.zgzcw.parser;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.loris.base.util.DateUtil;
import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.WebPageParser;
import com.loris.soccer.bean.data.table.LotteryCalendar;
import com.loris.soccer.web.downloader.zgzcw.page.LotteryCalendarWebPage;

/**
 * 解析足彩日历数据
 * 
 * @author jiean
 *
 */
public class LotteryCalendarWebPageParser implements WebPageParser
{
	/** 日历 */
	private List<LotteryCalendar> calendars;

	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The WebPage is not completed or Content is null. ");
		}

		if(!(page instanceof LotteryCalendarWebPage))
		{
			return false;
		}
		
		try
		{
			calendars = JSON.parseArray(page.getContent(), LotteryCalendar.class);
			if(calendars != null)
			{
				String time = DateUtil.getCurTimeStr();
				for (LotteryCalendar lotteryCalendar : calendars)
				{
					lotteryCalendar.setId(lotteryCalendar.getDate());
					lotteryCalendar.setJcissue(lotteryCalendar.getDate());
					lotteryCalendar.setUpdatetime(time);
				}
			}
			
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Get the Calendars.
	 * 
	 * @return
	 */
	public List<LotteryCalendar> getCalendars()
	{
		return calendars;
	}

}
