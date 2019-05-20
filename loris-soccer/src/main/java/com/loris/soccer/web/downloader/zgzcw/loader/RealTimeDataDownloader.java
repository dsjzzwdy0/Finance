/**  
 * All rights Reserved, Designed By www.loris.com
 * @Title:  RealTimeDataDownloader.java   
 * @Package com.loris.soccer.web.downloader.zgzcw.loader   
 * @Description: 本项目用于天津东方足彩数据的存储、共享、处理等   
 * @author: 东方足彩    
 * @date:   2019年1月28日 下午8:59:32   
 * @version V1.0.0
 * @Copyright: 2019 www.loris.com Inc. All rights reserved. 
 * 注意：本内容仅限于天津东方足彩有限公司传阅，禁止外泄以及用于其他的商业目
 */
package com.loris.soccer.web.downloader.zgzcw.loader;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.ArraysUtil;
import com.loris.base.util.DateUtil;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.item.IssueMatch;
import com.loris.soccer.bean.table.BdMatch;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwWebPageCreator;
import com.loris.soccer.web.downloader.zgzcw.page.LotteryWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.MatchHistoryWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsOpWebPage;
import com.loris.soccer.web.downloader.zgzcw.page.OddsYpWebPage;

/**
 * @ClassName: RealTimeDataDownloader
 * @Description: 最新数据下载管理器
 * @author: 东方足彩
 * @date: 2019年1月28日 下午8:59:32
 * 
 * @Copyright: 2019 www.loris.com Inc. All rights reserved.
 *             注意：本内容仅限于天津东方足彩有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class RealTimeDataDownloader extends IssueMatchDownloader
{
	private static Logger logger = Logger.getLogger(IssueMatchDownloader.class);

	/**
	 * 数据下载准备
	 */
	@Override
	public boolean prepare()
	{
		// 下载今天的数据
		if (StringUtils.isEmpty(date))
		{
			date = DateUtil.getCurDayStr();
		}

		if (!checkexist || !isIssueMatchDownloaded(SoccerConstants.LOTTERY_BD, date))
		{
			LotteryWebPage bdWebPage = ZgzcwWebPageCreator.createBdWebPage("");
			downloadLotteryPage(bdWebPage);
		}
		else
		{
			List<BdMatch> m2 = soccerManager.getBdMatches(date);
			issueMatchs.addAll(m2);
			logger.info("Get BdMatchs from database, and get " + issueMatchs.size() + " matchs.");
		}

		if (!checkexist || !isIssueMatchDownloaded(SoccerConstants.LOTTERY_JC, date))
		{
			LotteryWebPage jcWebPage = ZgzcwWebPageCreator.createJcWebPage(date);
			downloadLotteryPage(jcWebPage);
		}

		logger.info("There are " + issueMatchs.size() + " matches to be downloaded. ");

		if (issueMatchs != null && issueMatchs.size() > 0)
		{
			List<String> mids = new ArrayList<>();
			ArraysUtil.getObjectFieldValue(issueMatchs, mids, BdMatch.class, "mid");

			List<OddsOpWebPage> downOpPages = soccerWebPageManager.getDownloadedOddsOpWebPages(mids);
			List<OddsYpWebPage> downYpPages = soccerWebPageManager.getDownloadedOddsYpWebPages(mids);
			// List<MatchHistoryWebPage> historyWebPages =
			// soccerWebPageManager.getDownloadedMatchHistoryPages(mids);
			opChecker.setTime(System.currentTimeMillis());
			ypChecker.setTime(opChecker.getTime());

			for (IssueMatch match : issueMatchs)
			{
				String mid = match.getMid();
				opChecker.setMid(mid);
				ypChecker.setMid(mid);
				historyChecker.setMid(mid);

				// 该数据需要下多次
				if (!ArraysUtil.hasSameObject(downOpPages, opChecker))
				{
					OddsOpWebPage opWebPage = ZgzcwWebPageCreator.createOddsOpWebPage(mid);
					pages.put(opWebPage);
				}

				if (!ArraysUtil.hasSameObject(downYpPages, ypChecker))
				{
					OddsYpWebPage ypWebPage = ZgzcwWebPageCreator.createOddsYpWebPage(mid);
					pages.put(ypWebPage);
				}
			}
		}

		totalSize = issueMatchs.size() * 4;
		/*
		 * if(!checkexist || !isIssueMatchDownloaded(SoccerConstants.LOTTERY_JC,
		 * issue)) { LotteryWebPage zcWebPage = creator.createZcWebPage(issue);
		 * }
		 */

		return true;
	}
	
	/**
	 * 后处理下载的数据页面
	 * 
	 * @param page
	 *            下载的数据页面
	 * @param flag
	 *            下载是否成功的标识
	 */
	@Override
	public void afterDownload(WebPage page, boolean flag)
	{
		if(!flag)
		{
			return;
		}
		
		//进行分发处理
		if(page instanceof OddsOpWebPage)
		{
			processOddsOpWebPage((OddsOpWebPage)page);
		}
		else if(page instanceof OddsYpWebPage)
		{
			processOddsYpWebPage((OddsYpWebPage)page);
		}
		if(page instanceof MatchHistoryWebPage)
		{
			processMatchHistoryPage((MatchHistoryWebPage)page);
		}
		
		super.afterDownload(page, flag);
	}
}
