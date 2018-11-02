package com.loris.stock.web.downloader;

import com.loris.base.context.LorisContext;
import com.loris.base.web.manager.AbstractDownloader;
import com.loris.stock.repository.StockManager;
import com.loris.stock.web.repository.StockPageManager;

/**
 * 股票数据下载管理器
 * 
 * @author jiean
 *
 */
public abstract class StockDownloader  extends AbstractDownloader
{
	/** The StockManager instance. */
	protected StockManager stockManager;
	
	/** The StockPageManager instance. */
	protected StockPageManager stockPageManager;
	
	/** The StockWebPageCreator. */
	protected StockWebPageCreator creator;
	
	/**
	 * Create a new instance of StockDownloader.
	 */
	public StockDownloader()
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

		//初始化
		if(context != null && stockManager == null)
		{
			stockManager = context.getApplicationContext().getBean(StockManager.class);
		}
		
		//初始化
		if(context != null && stockPageManager == null)
		{
			stockPageManager = context.getApplicationContext().getBean(StockPageManager.class);
		}
	}
}
