package com.loris.soccer.web.scheduler;

import java.util.Random;

public abstract class AbstractScheduler implements Scheduler
{
	/** The interval */
	protected int interval = 1000;
	
	/** 随机数　*/
	protected static Random random = new Random();
	
	/** 调度器的名称　*/
	protected String name;
	
	@Override
	public void reset()
	{
	}

	/**
	 * 线程暂停
	 */
	public void sleep()
	{
		sleep(interval);
	}
	
	/**
	 * 计算等待时间
	 * @param waitTime
	 * @return
	 */
	public int computeWaitTime(int waitTime)
	{
		int rand = random.nextInt((int)(waitTime * 0.2));
		waitTime = (rand % 2 == 0) ? (waitTime + rand) : (waitTime + rand);
		return waitTime;
	}
	
	/**
	 * 线程暂停
	 * @param millis
	 */
	public void sleep(long millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch(Exception e)
		{
		}
	}

	/**
	 * 线程默认暂停的时间
	 * @return
	 */
	public int getInterval()
	{
		return interval;
	}

	/**
	 * 设置线程暂停的时间
	 * @param interval
	 */
	public void setInterval(int interval)
	{
		this.interval = interval;
	}
	
	@Override
	public boolean shouldBegin()
	{
		return false;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
