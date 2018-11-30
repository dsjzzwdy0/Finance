package com.loris.soccer.web.downloader.zgzcw.loader;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.loris.soccer.bean.item.IssueMatch;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwSoccerDownloader;

/**
 * 创建动态数据下载管理器
 * @author jiean
 *
 */
public class LiveMatchDownloader extends ZgzcwSoccerDownloader
{
	private static Logger logger = Logger.getLogger(LiveMatchDownloader.class);
	
	/** 竞彩比赛列表 */
	private List<IssueMatch> issueMatchs = new ArrayList<>();
	
	/**
	 * 数据下载准备
	 */
	@Override
	public boolean prepare()
	{
		logger.info("Starting to preparing LiveMatchDownloader...");
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<IssueMatch> getIssueMatchs()
	{
		return issueMatchs;
	}
}
