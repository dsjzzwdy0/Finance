package com.loris.soccer.web.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.loris.base.util.ArraysUtil;
import com.loris.base.web.task.Task;
import com.loris.base.web.task.event.TaskEvent;
import com.loris.base.web.task.event.TaskEvent.TaskEventType;
import com.loris.soccer.analysis.checker.MatchChecker;
import com.loris.soccer.bean.item.IssueMatch;
import com.loris.soccer.bean.item.MatchItem;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwDataDownloader;
import com.loris.soccer.web.task.DownloadRecord;
import com.loris.soccer.web.task.MatchWebTask;
import com.loris.soccer.web.task.MatchWebTask.MatchWebTaskType;
import com.loris.soccer.web.task.SoccerTask;

public class OddsTaskProduceScheduler extends TaskProduceScheduler<SoccerTask>
{
	private static Logger logger = Logger.getLogger(OddsTaskProduceScheduler.class);
	
	/** The matches. */
	List<MatchItem> matches = null;
	
	/** 数据下载的记录 */
	protected Map<String, DownloadRecord> downRecords = new HashMap<>();
	
	/** 数据清洗阈值,默认为三分钟(毫秒) */
	protected long timeToDiscard = 1000 * 180;
	
	/** 重新开始的时间 */
	protected double timeToReproduce = 4.0;
	
	/** 重新初始化的时间　*/
	protected double timeToReinitialize = 12.0;
	
	/** Task的序列编号*/
	protected int taskIndex = 1;
	
	/** 竞彩数据初始化 */
	protected boolean isJcInitialized = false;

	/** 北单数据初始化*/
	protected boolean isBdInitialized = false;
	
	/** 比赛检测器 */
	protected MatchChecker<MatchItem> matchChecker = new MatchChecker<>();
	
	/**
	 * Create a new instance ofOddsTaskProduceScheduler.
	 */
	public OddsTaskProduceScheduler()
	{
		this.name = "OddsTaskProduceScheduler";
	}
	
	/**
	 * Run the Produce Scheduler.
	 */
	@Override
	public boolean produce()
	{
		if(taskQueue == null)
		{
			throw new IllegalArgumentException("The TaskQueue object is null, please set the taskQueue value first.");
		}
		
		if(!initialized || matches == null || matches.isEmpty())
		{
			logger.info("Initialize error, no task will be produced.");
			return false;
		}
		
		cleanMatchs();
		if(matches == null || matches.isEmpty())
		{
			logger.info("There are no match to be downloaded.");
			return false;
		}
		
		taskIndex = 1;		
		Date date = new Date();
		for (MatchItem match : matches)
		{
			//检测是否需要下载
			if(needToDownload(date, match))
			{

				MatchWebTask optask = new MatchWebTask(match, MatchWebTaskType.Op);
				MatchWebTask yptask = new MatchWebTask(match, MatchWebTaskType.Yp);
				
				addSoccerTask(optask);
				addSoccerTask(yptask);
			}
		}
		//logger.info("Total queue size is " + taskQueue.size());
		return true;
	}
	
	/**
	 * 添加任务处理器
	 * @param task
	 */
	protected void addSoccerTask(SoccerTask task)
	{
		task.setBatchNo(batchNo);
		task.setTaskIndex(taskIndex ++);
		task.addTaskEventListener(this);
		
		taskList.add(task);
	}
	
	/**
	 * 检测是否应该开始更新
	 */
	@Override
	public boolean shouldBegin()
	{
		if(!initialized || lastInitTimeStamp == null || shouldReinitialize())
		{
			reset();
			return true;
		}
		
		if(shouldReproduce())
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * 初始化数据
	 */
	@Override
	public void reset()
	{
		super.reset();
		isBdInitialized = false;
		isJcInitialized = false;
	}
	
	/**
	 * 重新初始化的标志
	 * @return
	 */
	protected boolean shouldReinitialize()
	{
		long current = System.currentTimeMillis();
		long lastInitTime = lastInitTimeStamp.getTime();
		
		double h = getHour(current, lastInitTime);
		return h > timeToReinitialize;
	}
	
	/**
	 * 是否应该重新开始
	 * @return
	 */
	public boolean shouldReproduce()
	{
		long current = System.currentTimeMillis();
		long lastProduceTime = lastProduceTimeStamp.getTime();
		
		double h = getHour(current, lastProduceTime);
		return h > timeToReproduce;
	}
	
	/**
	 * 检测是否需要下载数据
	 * @param current
	 * @param matchDate
	 * @return
	 */
	protected boolean needToDownload(Date current, MatchItem match)
	{
		Date matchDate = match.getMatchDate();
		if(matchDate == null)
		{
			return false;
		}
		
		long timeToMatch = matchDate.getTime() - current.getTime();
		int hour = (int)(timeToMatch / (1000 * 3600));
		
		if(hour <= 10)
		{
			return true;
		}
		else 
		{
			Date lastDown = getLastDownTime(match.getMid());
			if(lastDown == null)
			{
				return true;
			}
			hour = (int)((current.getTime() - lastDown.getTime())/(3600 * 1000));
			if(hour > 3)
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isInitialized()
	{
		return initialized && isBdInitialized && isJcInitialized;
	}
	
	/**
	 * 加入比赛数据
	 * @param match
	 */
	protected void addMatch(MatchItem match)
	{
		if(matches == null)
		{
			matches = new ArrayList<>();
		}
		
		matchChecker.setMid(match.getMid());
		if(!ArraysUtil.hasSameObject(matches, matchChecker))
		{
			matches.add(match);
		}
	}
	
	/**
	 * 生成器的初始化
	 * @return 是否成功的标志
	 */
	public boolean initialize()
	{
		logger.info("Start to initialize the SoccerMatchTaskProducer...");
		if(matches == null)
		{
			matches = new ArrayList<>();
		}
		try
		{
			if(!isJcInitialized)
			{			
				List<? extends IssueMatch> jcMatches = ZgzcwDataDownloader.downloadLatestMatch("jc");
				if(jcMatches != null && !jcMatches.isEmpty())
				{
					isJcInitialized = true;
					for (IssueMatch issueMatch : jcMatches)
					{
						addMatch(issueMatch);
					}
				}
				logger.info("There are total " + jcMatches.size() + " JcMatches.");
			}
		}
		catch(Exception exception)
		{
			logger.info("Error when downloading JcMatch datas.");
		}
		
		try
		{
			Thread.sleep(2000);
		}
		catch(Exception e)
		{
			logger.info("Error: " + e.toString());
		}
		try
		{
			if(!isBdInitialized)
			{
				List<? extends IssueMatch> bdMatches = ZgzcwDataDownloader.downloadLatestMatch("bd");
				if(bdMatches != null && !bdMatches.isEmpty())
				{
					isBdInitialized = true;
					for (IssueMatch issueMatch : bdMatches)
					{
						addMatch(issueMatch);
					}
				}				
				logger.info("There are total " + bdMatches.size() + " BdMatches.");
			}
		}
		catch(Exception e)
		{
			logger.info("Error when downloading JcMatch datas.");
			return false;
		}
		
		if(matches.isEmpty())
		{
			logger.info("The match size is 0, exit");
			return false;
		}
		
		initialized = true;
		setLastInitTimeStamp();
		return true;
	}
	
	/**
	 * 数据清洗
	 */
	protected void cleanMatchs()
	{
		if(matches == null || matches.isEmpty())
		{
			return;
		}
		long current = System.currentTimeMillis();
		long mtime;
		long diff;
		int size = matches.size();

		MatchItem match;
		Date matchTime;
		for(int i = size - 1; i >= 0; i --)
		{
			match = matches.get(i);
			matchTime = match.getMatchDate();
			if(matchTime == null)
			{
				matches.remove(i);
			}
			else
			{
				mtime = matchTime.getTime();
				diff = mtime - current;
				
				//把数据丢弃,不再加入下载列表
				//比赛时间与现在时间相比,小于数据阈值的,则删除
				if(diff <= timeToDiscard)
				{
					matches.remove(i);
				}
			}
		}
	}
	
	/**
	 * 清除当前的任务
	 */
	@Override
	protected void clearPreTasks()
	{
		super.clearPreTasks();
		downRecords.clear();
	}
	
	/**
	 * 获得最近的下载时间
	 * @param key
	 * @return
	 */
	public Date getLastDownTime(String key)
	{
		DownloadRecord record = downRecords.get(key);
		if(record == null)
		{
			return null;
		}
		else
		{
			return record.getLastDate();
		}
	}
	
	/**
	 * 设置最后完成的时间
	 * @param key
	 */
	public void setLastDownTime(String key)
	{
		DownloadRecord record = downRecords.get(key);
		if(record == null)
		{
			record = new DownloadRecord(key);
			downRecords.put(key, record);
		}
		record.addCurrentRecord();
	}
	
	/**
	 * 通知任务处理器
	 * @param event 任务处理事件
	 */
	@Override
	public void notify(TaskEvent event)
	{
		super.notify(event);
		
		Task task = event.getTask();
		TaskEventType type = event.getType();
		if (type == TaskEventType.Finished)
		{
			if(task instanceof MatchWebTask)
			{
				String key = ((MatchWebTask)task).getMatch().getMid();
				setLastDownTime(key);
			}
		}
	}

	public double getTimeToResume()
	{
		return timeToReproduce;
	}

	public void setTimeToResume(double timeToResume)
	{
		this.timeToReproduce = timeToResume;
	}
}
