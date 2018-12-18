package com.loris.soccer.web.task;

import org.apache.log4j.Logger;

import com.loris.base.web.task.event.TaskEvent;
import com.loris.base.web.task.event.TaskEvent.TaskEventType;
import com.loris.soccer.bean.item.MatchItem;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwDataDownloader;

public class MatchWebTask extends SoccerTask implements Comparable<MatchWebTask>
{
	private static Logger logger = Logger.getLogger(MatchWebTask.class);
	
	/**
	 * MatchWebTaskType.
	 * @author deng
	 *
	 */
	public static enum MatchWebTaskType
	{
		Op,
		Yp
	}
	
	/** 比赛数据 */
	protected MatchItem match;
	
	/** 任务类型 */
	protected MatchWebTaskType type;
	
	/** 
	 * Create a new instance of MatchWebTask.
	 */
	public MatchWebTask()
	{
		this.waitTime = 4500;
		this.name = "MatchWebTask.";
	}
	
	/**
	 * 建立一个数据下载任务
	 * @param match
	 * @param type
	 */
	public MatchWebTask(MatchItem match, MatchWebTaskType type)
	{
		this();
		this.match = match;
		this.type = type;
	}

	@Override
	public void run()
	{
		notify(new TaskEvent(this, TaskEventType.Start));
		try
		{
			ZgzcwDataDownloader.downloadMatchTask(this);
		}
		catch(Exception e)
		{
			logger.info("Error when run MatchWebTask(" + match.getMid() + ")...");
		}

		notify(new TaskEvent(this, TaskEventType.Finished));
	}

	public MatchItem getMatch()
	{
		return match;
	}

	public void setMatch(MatchItem match)
	{
		this.match = match;
	}

	public MatchWebTaskType getType()
	{
		return type;
	}

	public void setType(MatchWebTaskType type)
	{
		this.type = type;
	}

	/**
	 * 优先的数据
	 */
	@Override
	public int compareTo(MatchWebTask o)
	{
		long t0 = match.getMatchtimevalue();
		long t1 = o.getMatch().getMatchtimevalue();
		return (t0 < t1) ? -1 : (t0 == t1) ? 0 : 1;
	}

	@Override
	public String toString()
	{
		return "MatchWebTask [type=" + type + ", match(" + match.getLid() + ", " + match.getMid() + ", " + match.getMatchtime() + ")]";
	}
}
