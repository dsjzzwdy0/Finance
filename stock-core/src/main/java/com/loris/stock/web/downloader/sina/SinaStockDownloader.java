package com.loris.stock.web.downloader.sina;

import com.loris.base.context.LorisContext;
import com.loris.stock.web.downloader.StockDownloader;

public class SinaStockDownloader extends StockDownloader
{
	/**
	 * Create a new instance of StockDownloader.
	 */
	public SinaStockDownloader()
	{
		setEncoding(ENCODING_GBK);
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
		if(creator == null)
		{
			creator = new SinaWebPageCreator();
		}
		
		/*
		if(processor == null)
		{
			processor = new ZgzcwWebPageProcessor(soccerManager, soccerWebPageManager);
		}*/
	}
}
