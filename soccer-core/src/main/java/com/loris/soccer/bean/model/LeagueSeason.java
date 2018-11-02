package com.loris.soccer.bean.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.loris.base.util.DateUtil;
import com.loris.base.util.NumberUtil;
import com.loris.soccer.bean.data.table.league.Season;

/**
 * 赛事赛季数据管理类
 */
public class LeagueSeason
{
	/** The League Id value. */
	private String lid;
	
	/** The Seasons List. */
	protected List<Season> seasons = new ArrayList<>();
	
	/**
	 * Create a instance of LeagueSeason
	 */
	public LeagueSeason(String lid)
	{
		this.lid = lid;
	}
	
	public String getLid()
	{
		return lid;
	}

	public void setLid(String lid)
	{
		this.lid = lid;
	}
	
	public Season getSeason(int index)
	{
		return seasons.get(index);
	}

	/**
	 * Add the Season.
	 * @param season The Season value.
	 */
	public void add(Season season)
	{
		seasons.add(season);
	}
	
	/**
	 * The Size of the seasons.
	 * @return
	 */
	public int size()
	{
		return seasons.size();
	}
	
	/**
	 * Get the last season value.
	 * @return Season value.
	 */
	public Season getLastSeason()
	{
		if(seasons.size() == 0)
		{
			return null;
		}
			
		Collections.sort(seasons, new Comparator<Season>()
		{
			@Override
			public int compare(Season o1, Season o2)
			{
				return o2.getSeason().compareTo(o1.getSeason());
			}
		});
		
		return seasons.get(0);
	}
	
	/**
	 * 检测是否有最新的赛季的数据
	 * @param date 日期
	 * @return 是否最新的标志
	 */
	public boolean hasLastSeason(Date date)
	{
		Season season = getLastSeason();
		
		String value = season.getSeason();
		int[] years = NumberUtil.parseAllIntegerFromString(value);
		int year = DateUtil.getValue(date, Calendar.YEAR);
		int month = DateUtil.getValue(date, Calendar.MONTH);
		
		if(years == null)
		{
			return false;
		}
		else if(years.length == 1)  //单年赛事
		{
			if(years[0] >= year)
			{
				return false;
			}
			else if(year - years[0] >= 2)
			{
				//已经超过一个赛季没有赛事，则取消下载
				return false;
			}
			else 
			{
				return true;
			}
		}
		else if(years.length == 2) //跨年赛事
		{
			if(years[1] > year || years[0] > year)
			{
				return false;
			}
			else if(years[1] == year && years[0] < year)
			{
				//跨年的赛事，一般以8月份为赛季开始
				if(month < 8)
				{
					return false;
				}
				else
				{
					return true;
				}
			}
		}
		//System.out.println(season);
		
		return false;
	}
	
	/**
	 * To string value.
	 * @return String value.
	 */
	@Override
	public String toString()
	{
		return "LeagueSeason [seasons=" + seasons + "]";
	}
}
