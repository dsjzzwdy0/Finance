package com.loris.soccer.web.downloader.okooo.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.loris.base.context.LorisContext;
import com.loris.base.util.ArraysUtil;
import com.loris.base.util.DateUtil;
import com.loris.base.web.http.UrlFetchException;
import com.loris.base.web.http.WebClientFetcher;
import com.loris.base.web.manager.TaskMode;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.bean.item.MatchItem;
import com.loris.soccer.bean.okooo.OkoooBdMatch;
import com.loris.soccer.bean.okooo.OkoooJcMatch;
import com.loris.soccer.bean.okooo.OkoooMatch;
import com.loris.soccer.bean.okooo.OkoooOp;
import com.loris.soccer.bean.okooo.OkoooYp;
import com.loris.soccer.repository.OkoooSqlHelper;
import com.loris.soccer.web.downloader.okooo.OkoooDataDownloader;
import com.loris.soccer.web.downloader.okooo.OkoooDownloader;
import com.loris.soccer.web.downloader.okooo.OkoooPageCreator;
import com.loris.soccer.web.downloader.okooo.page.OkoooWebPage;

public class OkoooMatchDownloaer extends OkoooDownloader
{
	private static Logger logger = Logger.getLogger(OkoooMatchDownloaer.class);
	
	/** 下载次数 */
	Map<WebPage, Integer> counts = new HashMap<>();
	
	/** 将要下载的数据 */
	List<MatchItem> matches = new ArrayList<>();
	
	/** 数据页面 */
	WebClientFetcher fetcher = null;
	
	/** 同一页面最大下载次数 */
	int maxDownloadTimes = 3;
	
	/** Sql Helper*/
	OkoooSqlHelper sqlHelper;
	
	public OkoooMatchDownloaer()
	{
		this.interval = 5500;
	}
	
	/**
	 * 运行模式
	 * @return
	 */
	@Override
	public TaskMode getTaskMode()
	{
		return TaskMode.Single;
	}
	
	/**
	 * Set the LorisContext.
	 * 
	 * @param context
	 */
	@Override
	public void setLorisContext(LorisContext context)
	{
		super.setLorisContext(context);
		sqlHelper = context.getBean(OkoooSqlHelper.class);
	}
	
	/**
	 * 数据下载准备
	 */
	@Override
	public boolean prepare()
	{
		OkoooDataDownloader.initialize(context);
		try
		{
			List<OkoooBdMatch> bdMatchs = OkoooDataDownloader.downloadBaseBdMatchPage();
			String date = DateUtil.getCurDayStr();
			
			List<OkoooBdMatch> ms = new ArrayList<>();
			for (OkoooBdMatch okoooBdMatch : bdMatchs)
			{
				if(okoooBdMatch.getMatchtime().compareTo(date) >= 0)
				{
					ms.add(okoooBdMatch);
				}
			}
			
			if(ms.size() > 0)
			{
				sqlHelper.addOkoooBdMatches(ms);
			}			
			matches.addAll(ms);
			
			fetcher = OkoooDataDownloader.getWebClientFetcher();
			Thread.sleep(interval);
			
			List<OkoooJcMatch> jcMatchs = OkoooDataDownloader.downloadJcMainPage(fetcher);
			if(jcMatchs != null)
			{
				List<OkoooJcMatch> jms = new ArrayList<>();
				for (OkoooJcMatch okoooBdMatch : jcMatchs)
				{
					if(okoooBdMatch.getMatchtime().compareTo(date) >= 0)
					{
						jms.add(okoooBdMatch);
					}
				}
				
				logger.info("There are " + jms.size() + " JcMatches.");				
				if(jms.size() > 0)
				{
					sqlHelper.addOkoooJcMatches(jms);
				}
			}
			
			for (MatchItem m : ms)
			{
				addPage(OkoooPageCreator.createOpWebPage(m.getMid()));
				addPage(OkoooPageCreator.createYpWebPage(m.getMid()));
			}
			
			List<String> lids = ArraysUtil.getObjectFieldValue(ms, OkoooBdMatch.class, "lid");
			for (String lid : lids)
			{
				OkoooWebPage page = OkoooPageCreator.createLeaguePage(lid);
				addPage(page);
			}
			
			totalSize = ms.size() * 2 + lids.size();
			logger.info("OkoooMatchDownloader prepared successful, there are total " + totalSize + " pages to be downloaded.");
		}
		catch (Exception e) {
			logger.info("Failed to initialize.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 下载数据
	 * @param page 数据页面
	 * @return 下载是否完成的标志
	 */
	@Override
	public boolean download(WebPage page) throws UrlFetchException
	{
		String type = page.getType();
		if("op".equals(type))
		{
			String mid = ((OkoooWebPage)page).getMid();
			MatchItem match = getOkoooBdMatch(mid);
			if(match == null)
			{
				logger.info("The match[" + mid + "] is not exist");
				return false;
			}
			
			List<OkoooOp> ops = OkoooDataDownloader.downloadMatchMainOp(fetcher, match);
			if(ops != null && ops.size() > 0)
			{
				return sqlHelper.addNewOkoooOpList(mid, ops);
			}
		}
		else if("yp".equals(type))
		{
			String mid = ((OkoooWebPage)page).getMid();
			MatchItem match = getOkoooBdMatch(mid);
			if(match == null)
			{
				logger.info("The match[" + mid + "] is not exist");
				return false;
			}
			
			List<OkoooYp> yps = OkoooDataDownloader.downloadMatchMainYp(fetcher, match);
			if(yps != null && yps.size() > 0)
			{
				return sqlHelper.addNewOkoooYpList(mid, yps);
			}
		}
		else if(OkoooPageCreator.PAGE_TYPES[7].equalsIgnoreCase(type))
		{
			String lid = ((OkoooWebPage)page).getMid();
			List<OkoooMatch> matchs = OkoooDataDownloader.downloadLeagueCurrentRound(lid);
			if(matchs != null && matchs.size() > 0)
			{
				return sqlHelper.addOkoooMatch(matchs);
			}
		}
		
		if(getDownloadCount(page) < maxDownloadTimes)
		{
			addDownloadTime(page);
			addPage(page);
			totalSize ++;
		}
		
		return false;
	}
	
	/**
	 * 数据后处理
	 */
	@Override
	public void afterDownload(WebPage page, boolean flag)
	{
		super.afterDownload(page, flag);
	}
	
	/**
	 * 查找比赛对象
	 * @param mid
	 * @return
	 */
	public MatchItem getOkoooBdMatch(String mid)
	{
		for (MatchItem match : matches)
		{
			if(mid.equals(match.getMid()))
			{
				return match;
			}
		}
		return null;
	}
	
	protected void addDownloadTime(WebPage page)
	{
		Integer integer = counts.get(page);
		if(integer == null)
		{
			counts.put(page, 1);
		}
		else
		{
			counts.put(page, integer + 1);
		}
	}
	
	protected int getDownloadCount(WebPage page)
	{
		Integer integer = counts.get(page);
		return integer == null ? 0 : (int)integer;
	}
}
