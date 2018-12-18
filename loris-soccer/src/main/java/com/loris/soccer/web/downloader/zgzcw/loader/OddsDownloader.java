package com.loris.soccer.web.downloader.zgzcw.loader;

import java.util.List;

import org.apache.log4j.Logger;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.DateUtil;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.bean.table.Match;
import com.loris.soccer.bean.table.Op;
import com.loris.soccer.bean.table.Yp;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwSoccerDownloader;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwWebPageCreator;
import com.loris.soccer.web.downloader.zgzcw.page.OddsOpWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsYpWebPage;
import com.loris.soccer.web.downloader.zgzcw.parser.OddsOpWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.OddsYpWebPageParser;

/**
 * 亚盘、欧赔数据下载器
 * 
 * @author jiean
 *
 */
public class OddsDownloader extends ZgzcwSoccerDownloader
{
	private static Logger logger = Logger.getLogger(OddsDownloader.class);
	
	/**
	 * 初始化下载管理器
	 */
	@Override
	public boolean prepare()
	{
		// 检测是否正确设置数据下载的环境
		if (soccerManager == null || soccerWebPageManager == null)
		{
			logger.info("OddsDownloader is not initialized, stop.");
			return false;
		}
		
		//如果没有设置起始日期，
		if(StringUtils.isEmpty(start))
		{
			logger.info("OddsDownloader start or end time is not set, please set the date first. stop.");
			return false;
		}
		
		//检测截止日期，如果没有设置，则以今天作为截止日期
		if(StringUtils.isEmpty(end))
		{
			end = DateUtil.getCurDayStr();
		}
		
		List<Match> matchs = soccerManager.getMatches(start, end);
		WebPage page = null;
		
		List<OddsOpWebPage> downOddsOpPages = soccerWebPageManager.getDownloadedOddsOpWebPages();
		List<OddsYpWebPage> downOddsYpPages = soccerWebPageManager.getDownloadedOddsYpWebPages();
		
		for (Match match : matchs)
		{
			
			//如果已经下载，则不计算下载库中
			if(!isOpDownloaded(downOddsOpPages, match))
			{
				page = ZgzcwWebPageCreator.createOddsOpWebPage(match.getMid());
				pages.put(page);
			}
			
			if(!isYpDownloaded(downOddsYpPages, match))
			{
				page = ZgzcwWebPageCreator.createOddsYpWebPage(match.getMid());
				pages.put(page);
			}
		}
		
		totalSize = matchs.size() * 2;
		logger.info("There are " + totalSize + " pages and there are " + leftSize() + " pages to be downloaded.");
		return true;
	}
	
	
	/**
	 * Post process the WebPage.
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
		
		if(page instanceof OddsYpWebPage)
		{
			OddsYpWebPage page2 = (OddsYpWebPage)page;
			OddsYpWebPageParser parser = new OddsYpWebPageParser();
			if(parser.parseWebPage(page2))
			{
				List<Yp> ypList = parser.getOddsList();
				synchronized (soccerManager)
				{
					soccerManager.addYpList(ypList);
				}
			}
			else
			{
				page2.setCompleted(false);
			}
			
			synchronized (soccerWebPageManager)
			{
				soccerWebPageManager.addOrUpdateYpWebPage(page2);
			}
		}
		else if(page instanceof OddsOpWebPage)
		{
			OddsOpWebPage page2 = (OddsOpWebPage)page;
			OddsOpWebPageParser parser = new OddsOpWebPageParser();
			
			if(parser.parseWebPage(page2))
			{
				List<Op> opList = parser.getOddsList();
				if(opList.isEmpty())
				{
					logger.info("Error, no Op Data can be parsed: " + page2);
					return;
				}
				synchronized (soccerManager)
				{
					soccerManager.addOpList(opList);
				}
			}
			else
			{
				page2.setCompleted(false);
			}
			
			synchronized (soccerWebPageManager)
			{
				soccerWebPageManager.addOrUpdateOpWebPage(page2);
			}
		}

		
		super.afterDownload(page, flag);
	}
	
	
	/**
	 * 检测是否已经下载。
	 */
	protected boolean isOpDownloaded(List<OddsOpWebPage> list, Match match)
	{
		for (OddsOpWebPage page : list)
		{
			if(!page.isCompleted())
			{
				continue;
			}
			
			//已经下载了，不再下载
			if(match.getMid().equals(page.getMid()))
			{
				//if(DateUtil.compareDateString(match.get, dateString2))
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 检测是否已经下载。
	 */
	protected boolean isYpDownloaded(List<OddsYpWebPage> list, Match match)
	{
		for (OddsYpWebPage page : list)
		{
			if(!page.isCompleted())
			{
				continue;
			}
			
			//已经下载了，不再下载
			if(match.getMid().equals(page.getMid()))
			{
				//if(DateUtil.compareDateString(match.get, dateString2))
				return true;
			}
		}
		return false;
	}
}
