package com.loris.soccer.web.downloader.okooo;

import com.loris.base.context.LorisContext;
import com.loris.base.web.http.WebClientFetcher;
import com.loris.soccer.repository.SoccerManager;
import com.loris.soccer.web.downloader.SoccerDownloader;
import com.loris.soccer.web.repository.SoccerWebPageManager;

/**
 * 下载澳客网的数据
 * 
 * @author jiean
 *
 */
public class OkoooDownloader extends SoccerDownloader
{
	public static final String SOURCE_OKOOO = "okooo";
	
	/** 管理器 */
	protected SoccerManager soccerManager;
	
	/** 页面管理器 */
	protected SoccerWebPageManager soccerWebManager;
	
	/** 页面创建器 */
	protected OkoooPageCreator creator = null;
	
	/** 数据下载器 */
	protected WebClientFetcher fetcher;
	
	/**
	 * Set the LorisContext.
	 * 
	 * @param context
	 */
	@Override
	public void setLorisContext(LorisContext context)
	{
		super.setLorisContext(context);

		//初始化
		if(context != null && soccerManager == null)
		{
			soccerManager = context.getApplicationContext().getBean(SoccerManager.class);
		}
		
		//初始化
		if(context != null && soccerWebManager == null)
		{
			soccerWebManager = context.getApplicationContext().getBean(SoccerWebPageManager.class);
		}
		
		if(creator == null)
		{
			creator = new OkoooPageCreator();
		}
	}
}
