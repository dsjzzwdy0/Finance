package com.loris.soccer.web.downloader.zgzcw;

import com.loris.base.bean.web.WebPage;
import com.loris.base.context.LorisContext;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.repository.SoccerManager;
import com.loris.soccer.web.downloader.SoccerDownloader;
import com.loris.soccer.web.downloader.SoccerWebPageProcessor;
import com.loris.soccer.web.repository.SoccerWebPageManager;

/**
 * 中国足彩数据下载管理器
 * 
 * @author jiean
 *
 */
public abstract class ZgzcwSoccerDownloader extends SoccerDownloader
{	
	public static final String SOURCE_ZGZCW = SoccerConstants.DATA_SOURCE_ZGZCW;
	
	/** The soccer manager. */
	protected static SoccerManager soccerManager;
	
	/** The Soccer WebPage Manager. */
	protected static SoccerWebPageManager soccerWebPageManager;
	
	/** The WebPageCreator */
	protected static ZgzcwWebPageCreator creator;
	
	/** The WebPageProcessor. */
	protected static SoccerWebPageProcessor processor;
	
	/**
	 * Zhucai main
	 */
	public ZgzcwSoccerDownloader()
	{
		setEncoding(ENCODING_UTF8);
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

		//初始化
		if(context != null && soccerManager == null)
		{
			soccerManager = context.getApplicationContext().getBean(SoccerManager.class);
		}
		
		//初始化
		if(context != null && soccerWebPageManager == null)
		{
			soccerWebPageManager = context.getApplicationContext().getBean(SoccerWebPageManager.class);
		}
		
		if(creator == null)
		{
			creator = new ZgzcwWebPageCreator();
		}
		
		if(processor == null)
		{
			processor = new ZgzcwWebPageProcessor(soccerManager, soccerWebPageManager);
		}
	}
	
	/**
	 * Check the web page content is validate.
	 * 
	 * @param page
	 * @return
	 */
	public boolean checkPageContent(WebPage page)
	{
		if(page.getContent().contains("<title>403 Forbidden</title>"))
		{
			return false;
		}
		return true;
	}
}
