package com.loris.soccer.web.downloader.zgzcw.loader;

import java.util.List;

import org.apache.log4j.Logger;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.web.WebPage;
import com.loris.base.util.DateUtil;
import com.loris.soccer.bean.data.table.Logo;
import com.loris.soccer.bean.data.table.league.Team;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwSoccerDownloader;

/**
 * 球队标志图像下载器
 * 
 * @author jiean
 *
 */
public class LogoDownloader extends ZgzcwSoccerDownloader
{
	private static Logger logger = Logger.getLogger(TeamDownloader.class);
	
	

	/**
	 * 数据下载准备
	 */
	@Override
	public boolean prepare()
	{
		if (soccerManager == null || soccerWebPageManager == null)
		{
			logger.info("TeamLogoDownloader is not initialized, stop.");
			return false;
		}

		List<Team> teams = soccerManager.getAllTeams();
		List<Logo> logos = soccerManager.getAllLogos();

		String timeStr = DateUtil.getCurTimeStr();
		for (Team team : teams)
		{
			if (isNotNeedToDownload(team.getLogo()))
			{
				// 不需要进行下载
				continue;
			}

			if (isDownloaded(logos, Logo.LOGO_TYPE_TEAM, team.getTid()))
			{
				continue;
			}

			WebPage page = new WebPage();
			page.setBytevalue(true);
			page.setUrl(team.getLogo());
			page.setBytevalue(true);
			page.setCreatetime(timeStr);
			page.setParam(team);
			pages.put(page);
		}
		
		return true;
		//totalSize = teams.size();
		//logger.info("There are " + totalSize + " pages and there are " + leftSize() + " pages to be downloaded.");
	}

	/**
	 * 下载后处理
	 */
	@Override
	public void afterDownload(WebPage page, boolean flag)
	{
		if (!flag)
		{
			// this will do nothing.
			return;
		}

		if (page.getBytes() == null)
		{
			return;
		}
		Object source = page.getParam();
		if (source == null)
		{
			return;
		}

		if(source instanceof Team)
		{
			Team team = (Team)source;
			Logo logo = new Logo();
			logo.setType(Logo.LOGO_TYPE_TEAM);
			logo.setImages(page.getBytes());
			logo.setTid(team.getTid());
			logo.setMediatype(getMediaType(team.getLogo()));
			soccerManager.addOrUpdateLogo(logo);
		}

		super.afterDownload(page, flag);
	}

	/**
	 * 检测是否需要去进行下载
	 * 
	 * @param type
	 *            类型
	 * @param id
	 *            ID值
	 * @return 是否已经下载的标志
	 */
	protected boolean isDownloaded(List<Logo> logos, String type, String id)
	{
		for (Logo logo : logos)
		{
			if (type.equals(logo.getType()) && id.equals(logo.getTid()))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查是否需要去下载，如nopic.gif就不需要下载
	 * 
	 * @param url
	 *            URL值
	 * @return 是否需要下载的标志
	 */
	protected boolean isNotNeedToDownload(String url)
	{
		if(StringUtils.isEmpty(url) || url.toLowerCase().endsWith("nopic.gif"))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 媒体类型
	 * @param url
	 * @return
	 */
	protected String getMediaType(String url)
	{
		if(url.endsWith("gif"))
		{
			return "image/gif";
		}
		else if(url.endsWith("jpg"))
		{
			return "image/jpeg";
		}
		else if(url.endsWith("png"))
		{
			return "image/png";
		}
		else
		{
			return "image/jpg";			
		}
	}
}
