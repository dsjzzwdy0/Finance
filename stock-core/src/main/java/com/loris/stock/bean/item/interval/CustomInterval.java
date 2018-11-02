package com.loris.stock.bean.item.interval;

import com.loris.base.util.SerialVersion;

/**
 *
 * @author Viorel
 */
public class CustomInterval extends Interval
{

	private static final long serialVersionUID = SerialVersion.APPVERSION;

	private long startTime;
	private int lengthInSeconds;

	public CustomInterval(String name, boolean isIntraDay,
		long startTime, String timeParam, int lengthInSeconds)
	{
		super(name, isIntraDay);
		this.timeParam = timeParam;
		this.startTime = startTime;
		this.lengthInSeconds = lengthInSeconds;
	}

	@Override
	public long startTime()
	{
		return startTime;
	}

	@Override
	public String getTimeParam()
	{
		return this.timeParam;
	}

	@Override
	public int getLengthInSeconds()
	{
		return lengthInSeconds;
	}

}
