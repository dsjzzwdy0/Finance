package com.loris.soccer.web.task;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.web.task.TaskQueue;
import com.loris.soccer.bean.item.IssueMatch;
import com.loris.soccer.bean.item.MatchItem;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwDataDownloader;
import com.loris.soccer.web.task.MatchWebTask.MatchWebTaskType;

/**
 * 足球比赛的任务生成器工具
 * @author deng
 *
 */
public class SoccerMatchTaskProducer extends SoccerTaskProducer
{
	private static Logger logger = Logger.getLogger(SoccerMatchTaskProducer.class);
	
	/** The matches. */
	List<? extends IssueMatch> matches = null;
	
	/** 初始化的时间 */
	protected Date initTimeStamp;
	
	/** 数据清洗阈值,默认为三分钟(毫秒) */
	protected long timeToDiscard = 1000 * 60 * 3;
	
	protected static long ONE_DAY = 1000 * 3600 * 24;
	
	public SoccerMatchTaskProducer()
	{
	}
	
	/**
	 * 
	 * @param queue
	 */
	public SoccerMatchTaskProducer(TaskQueue<MatchWebTask> queue)
	{
		this.taskQueue = queue;
	}
	

	@Override
	public boolean needToStart()
	{
		//数据没有了,则需要重新加载数据
		if(matches == null || matches.isEmpty())
		{
			reset();
			return true;
		}
		
		//没有初始化,则需要开始
		if(!initialized)
		{
			return true;
		}
		
		long current = System.currentTimeMillis();
		long last = lastTimeStamp == null ? current : lastTimeStamp.getTime();
		long diff = current - last;
		
		//超过一天,则需要重新加载数据
		if(diff > ONE_DAY)
		{
			reset();
			return true;
		}
		
		return diff > interval;
	}
	
	/**
	 * 生成器的初始化
	 * @return 是否成功的标志
	 */
	protected boolean initialize()
	{
		logger.info("Start to initialize the SoccerMatchTaskProducer...");
		//String date = DateUtil.getCurDayStr();
		
		try
		{
			ZgzcwDataDownloader.downloadLatestMatch("jc");
		}
		catch(Exception exception)
		{
			logger.info("Error when downloading JcMatch datas.");
		}
		
		try
		{
			Thread.sleep(1000);
		}
		catch(Exception e)
		{
		}
		try
		{
			matches = ZgzcwDataDownloader.downloadLatestMatch("bd");
			if(matches == null || matches.size() <= 0)
			{
				return false;
			}
		}
		catch(Exception e)
		{
			logger.info("Error when downloading JcMatch datas.");
			return false;
		}
		
		initialized = true;
		initTimeStamp = new Date();
		return true;
	}
	
	/**
	 * 线程运行
	 */
	@Override
	public void run()
	{
		if(taskQueue == null)
		{
			throw new IllegalArgumentException("The TaskQueue object is null, please set the taskQueue value first.");
		}
		
		if(!isInitialized() || matches == null)
		{
			if(!initialize())
			{
				logger.info("Initialize error, producer exit.");
				return ;
			}
		}
		
		cleanMatchs();
		if(matches == null || matches.isEmpty())
		{
			logger.info("There are no match to be downloaded.");
			return;
		}
		
		Date date = new Date();
		
		for (IssueMatch match : matches)
		{
			//检测是否需要下载
			if(needToDownload(date, match))
			{

				MatchWebTask optask = new MatchWebTask(match, MatchWebTaskType.Op);
				MatchWebTask yptask = new MatchWebTask(match, MatchWebTaskType.Yp);
				
				optask.addTaskEventListener(this);
				yptask.addTaskEventListener(this);
				
				taskQueue.pushTask(optask);
				taskQueue.pushTask(yptask);
			}
		}
		
		lastTimeStamp = date;
		logger.info("Total queue size is " + taskQueue.size());
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

		IssueMatch match;
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

	public Date getInitTimeStamp()
	{
		return initTimeStamp;
	}

	public void setInitTimeStamp(Date initTimeStamp)
	{
		this.initTimeStamp = initTimeStamp;
	}
}
