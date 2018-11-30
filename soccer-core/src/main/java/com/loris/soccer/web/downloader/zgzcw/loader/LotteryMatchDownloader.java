package com.loris.soccer.web.downloader.zgzcw.loader;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.data.PairValue;
import com.loris.base.util.DateUtil;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.bean.data.table.lottery.BdMatch;
import com.loris.soccer.bean.data.table.lottery.JcMatch;
import com.loris.soccer.bean.data.table.lottery.LotteryCalendar;
import com.loris.soccer.bean.data.table.lottery.ZcMatch;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwSoccerDownloader;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwWebPageCreator;
import com.loris.soccer.web.downloader.zgzcw.page.LotteryWebPage;
import com.loris.soccer.web.downloader.zgzcw.parser.LotteryBdWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.LotteryJcWebPageParser;
import com.loris.soccer.web.downloader.zgzcw.parser.LotteryZcWebPageParser;

/**
 * 足彩数据、竞彩数据、北单数据下载器
 * 
 * @author jiean
 *
 */
public class LotteryMatchDownloader extends ZgzcwSoccerDownloader
{	
	private static Logger logger = Logger.getLogger(LotteryMatchDownloader.class);
	
	private static final String bd = "bd";
	private static final String jc = "jc";
	private static final String zc = "zc";
	
	/**
	 * 初始化数据
	 */
	@Override
	public boolean prepare()
	{
		if (soccerManager == null || soccerWebPageManager == null)
		{
			logger.info("TeamDownloader is not initialized, stop.");
			return false;
		}
		
		if(StringUtils.isEmpty(start))
		{
			logger.info("RoundDownloader start time is not set, please set the date first. stop.");
			return false;
		}
		
		if(StringUtils.isEmpty(end))
		{
			end = DateUtil.getCurDayStr();
		}
		
		List<LotteryCalendar> calendars = soccerManager.getLotteryCalendars(start, end);
		List<PairValue<String, String>> candidates = new ArrayList<>();
		int num = 0;
		
		for (LotteryCalendar cal : calendars)
		{
			if(StringUtils.isNotEmpty(cal.getBdissue()) && cal.getBd() > 0)
			{
				num ++;
				addOrNotExistInCandidates(candidates, bd, cal.getBdissue());
			}
			
			if(cal.getJc() > 0)
			{
				num ++;				
				addOrNotExistInCandidates(candidates, jc, cal.getJcissue());
			}
			
			if(StringUtils.isNotEmpty(cal.getZc()))
			{
				num ++;				
				addOrNotExistInCandidates(candidates, zc, cal.getZc());
			}
		}
		
		List<LotteryWebPage> downPages = soccerWebPageManager.getDownloadedLotteryWebPages(null);
		for (PairValue<String, String> issue : candidates)
		{
			if(!isDownloaded(downPages, issue))
			{
				switch (issue.getKey()) {
				case bd:
					pages.put(ZgzcwWebPageCreator.createBdWebPage(issue.getValue()));
					break;
				case jc:
					pages.put(ZgzcwWebPageCreator.createJcWebPage(issue.getValue()));
					break;
				case zc:
					pages.put(ZgzcwWebPageCreator.createZcWebPage(issue.getValue()));
					break;
				default:
					break;
				}
			}
		}
		totalSize = num;
		logger.info("There are " + totalSize + " pages and there are " + leftSize() + " pages to be downloaded.");
		return true;		
	}
	
	/**
	 * After download the page.
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
		
		if(!(page instanceof LotteryWebPage))
		{
			return;
		}
		
		LotteryWebPage page2 = (LotteryWebPage) page;
		String issueType = page2.getIssuetype();
		boolean success = false;
		
		synchronized (soccerManager)
		{
			switch (issueType) {
			case bd:
				success = processBdWebPage(page2);
				break;
			case jc:
				success = processJcWebPage(page2);
				break;
			case zc:
				success = processZcWebPage(page2);
				break;
			default:
				break;
			}			
		}

		if(!success)
		{
			page2.setCompleted(false);
		}
		
		synchronized (soccerWebPageManager)
		{
			soccerWebPageManager.addOrUpdateLotteryWebPage(page2);
		}
		
		super.afterDownload(page, flag);
	}
	
	/**
	 * 处理BD页面数据
	 * 
	 * @param page
	 * @return
	 */
	protected boolean processBdWebPage(LotteryWebPage page)
	{
		LotteryBdWebPageParser parser = new LotteryBdWebPageParser();
		if(parser.parseWebPage(page))
		{
			List<BdMatch> matchs = parser.getMatches();
			return soccerManager.addOrUpdateBdMatches(matchs);
		}
		return false;
	}
	
	/**
	 * 处理Jc页面数据
	 * 
	 * @param page
	 * @return
	 */
	protected boolean processJcWebPage(LotteryWebPage page)
	{
		LotteryJcWebPageParser parser = new LotteryJcWebPageParser();
		if(parser.parseWebPage(page))
		{
			List<JcMatch> matchs = parser.getJcMatches();
			if(matchs == null || matchs.size() <= 0)
			{
				return false;
			}
			return soccerManager.addOrUpdateJcMatches(matchs);
		}
		return false;
	}
	
	/**
	 * 处理Zc页面数据
	 * 
	 * @param page
	 * @return
	 */
	protected boolean processZcWebPage(LotteryWebPage page)
	{
		LotteryZcWebPageParser parser = new LotteryZcWebPageParser();
		if(parser.parseWebPage(page))
		{
			List<ZcMatch> matchs = parser.getMatches();
			return soccerManager.addOrUpdateZcMatches(matchs);
		}
		return false;
	}
	
	/**
	 * 检测是否已经在下载列表中。
	 * 
	 * @param candidates
	 * @param type
	 * @param issue
	 * @return
	 */
	protected void addOrNotExistInCandidates(List<PairValue<String, String>> candidates, String type, String issue)
	{
		for (PairValue<String, String> pairValue : candidates)
		{
			if(type.equals(pairValue.getKey()) && issue.equals(pairValue.getValue()))
			{
				return;
			}
		}
		candidates.add(new PairValue<>(type, issue));
	}
	
	/**
	 * 检测是否已经在下载的数据表中
	 * 
	 * @param downPages
	 * @param issue
	 * @return
	 */
	protected boolean isDownloaded(List<LotteryWebPage> downPages, PairValue<String, String> issue)
	{
		for (LotteryWebPage page : downPages)
		{
			if(issue.getKey().equalsIgnoreCase(page.getIssuetype()) && issue.getValue().equalsIgnoreCase(page.getIssue()))
			{
				return true;
			}
		}
		return false;
	}
}
