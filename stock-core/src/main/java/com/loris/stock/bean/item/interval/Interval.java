package com.loris.stock.bean.item.interval;

import java.io.Serializable;
import java.util.Calendar;

import com.loris.base.util.SerialVersion;

/**
 *
 * @author viorel.gheba
 */
public abstract class Interval implements Serializable
{
	private static final long serialVersionUID = SerialVersion.APPVERSION;
	
	
	public static final Interval ONE_MINUTE = new OneMinuteInterval();
	public static final Interval FIVE_MINUTE = new FiveMinuteInterval();
	public static final Interval FIFTEEN_MINUTE = new FifteenMinuteInterval();
	public static final Interval THIRTY_MINUTE = new ThirtyMinuteInterval();
	public static final Interval SIXTY_MINUTE = new SixtyMinuteInterval();
	public static final Interval DAILY = new DailyInterval();
	public static final Interval WEEKLY = new WeeklyInterval();
	public static final Interval MONTHLY = new MonthlyInterval();
	
	
	public static final Interval[] INTRA_DAY_INTERVALS =
	{
			ONE_MINUTE, FIVE_MINUTE, FIFTEEN_MINUTE, THIRTY_MINUTE, SIXTY_MINUTE
	};
	public static final Interval[] INTERVALS =
	{
			DAILY, WEEKLY, MONTHLY
	};
	
	
	public static Interval[] getIntraDayIntervals()
	{
		return INTRA_DAY_INTERVALS;
	}

	public static Interval[] getIntervals()
	{
		return INTERVALS;
	}
	
	public static Interval getIntervalFromKey(String key)
	{
		for (Interval i : INTERVALS)
		{
			if (key.endsWith(i.getTimeParam()))
			{
				return i;
			}
		}
		for (Interval i : INTRA_DAY_INTERVALS)
		{
			if (key.endsWith(i.getTimeParam()))
			{
				return i;
			}
		}
		return null;
	}

	public static Interval getInterval(int intervalHash)
	{
		for (Interval interval : INTERVALS)
		{
			if (interval.hashCode() == intervalHash)
			{
				return interval;
			}
		}
		for (Interval interval : INTRA_DAY_INTERVALS)
		{
			if (interval.hashCode() == intervalHash)
			{
				return interval;
			}
		}
		return null;
	}

	
	
	
	protected String name = "";
	protected String timeParam = "";
	protected boolean intraDay = false;

	public Interval(String name)
	{
		this.name = name;
		this.intraDay = false;
	}

	public Interval(String name, boolean intraDay)
	{
		this.name = name;
		this.intraDay = intraDay;
	}

	public String getName()
	{
		return name;
	}

	public boolean isIntraDay()
	{
		return intraDay;
	}

	public abstract long startTime();

	public abstract String getTimeParam();

	public abstract int getLengthInSeconds();

	public String getMarkerString(long time)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);

		StringBuilder sb = new StringBuilder();
		if (!isIntraDay())
		{
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int month = cal.get(Calendar.MONTH) + 1;
			int year = cal.get(Calendar.YEAR);

			sb.append(Integer.toString(year));
			//sb.append("-");
			
			if (month < 10)
			{
				sb.append("0");
			}
			sb.append(Integer.toString(month));
			//sb.append("-");

			if (day < 10)
			{
				sb.append("0");
			}
			sb.append(Integer.toString(day));

			return sb.toString();
		}
		else
		{
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int month = cal.get(Calendar.MONTH) + 1;
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int minute = cal.get(Calendar.MINUTE);
			int seconds = cal.get(Calendar.SECOND);

			if (month < 10)
			{
				sb.append("0");
			}
			sb.append(Integer.toString(month));
			sb.append("/");

			if (day < 10)
			{
				sb.append("0");
			}
			sb.append(Integer.toString(day));
			sb.append(" ");

			if (hour < 10)
			{
				sb.append("0");
			}
			sb.append(Integer.toString(hour));
			sb.append(":");

			if (minute < 10)
			{
				sb.append("0");
			}
			sb.append(Integer.toString(minute));

			if (getLengthInSeconds() < 60)
			{
				sb.append(":");
				if (seconds < 10)
					sb.append("0");
				sb.append(Integer.toString(seconds));
			}

			return sb.toString();
		}
	}

	public @Override String toString()
	{
		return name;
	}

	public @Override boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}

		if (!(obj instanceof Interval))
		{
			return false;
		}
		Interval that = (Interval) obj;

		if (!that.getName().equals(getName()))
		{
			return false;
		}

		if (!that.getTimeParam().equals(getTimeParam()))
		{
			return false;
		}

		return true;
	}

	public @Override int hashCode()
	{
		int hash = 5;
		hash = 13 * hash + (this.name != null ? this.name.hashCode() : 0);
		hash = 13 * hash + (this.timeParam != null ? this.timeParam.hashCode() : 0);
		hash = 13 * hash + (this.intraDay ? 1 : 0);
		return hash;
	}

}
