package com.loris.base.web.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.loris.base.context.LorisContext;
import com.loris.base.util.DateUtil;
import com.loris.base.web.config.setting.DownSetting;
import com.loris.base.web.http.UrlFetchException;
import com.loris.base.web.http.UrlFetcher;
import com.loris.base.web.manager.event.WebPageStatusEvent;
import com.loris.base.web.manager.event.WebPageStatusListener;
import com.loris.base.web.page.WebPage;
import com.loris.base.web.util.BoundedFIFO;

/**
 * 下载页面管理类
 * 
 * @author dsj
 *
 */
public abstract class AbstractDownloader implements Downloader
{
	private static Logger logger = Logger.getLogger(AbstractDownloader.class);
	
	public static int MAX_SIZE = 500;
	public static int MAX_ACTIVE_THREAD_NUM = 10;
	
	/** The base url string. */
	protected String baseURL;
	
	/** The name of the manager. */
	protected String name;
	
	/** The Description. */
	protected String description;
	
	/** The table name to store the page. */
	protected String tablename;

	/** The type of the page. */
	protected String type = "0";
	
	/** The flag of enable. */
	protected boolean enable = true;
	
	/** The char encoding value. */
	protected String encoding;
	
	/** The pages that to download information. */
	protected BoundedFIFO pages = new BoundedFIFO(MAX_SIZE);
	
	/** The interval value of the webmanager. */
	protected int interval = 2000;
	
	/** The Flag to stop the manager. */
	protected boolean stopManager = false;
	
	/** The LorisContext. */
	protected LorisContext context;
	
	/** The status of the manager. */
	protected int state;
	
	/** Check if the Manager has been prepared. */
	protected boolean prepared = false;
	
	/** The current index value. */
	protected int curIndex;
	
	/** The total number of page to be downloaded. */
	protected int totalSize;
	
	/** The max thread number. */
	protected int maxActiveThreadNum;
	
	/** 开始日期 */
	protected String start;
	
	/** 结束日期 */
	protected String end;
	
	/** The WebPageInfo. */
	protected DownSetting setting;
	
	/** The TaskMode */
	protected TaskMode taskMode;
	
	/** The listeners. */
	protected List<WebPageStatusListener> listeners = new ArrayList<>();
	
	/**
	 * Create a new instance of AbstractWebPageManger.
	 * 
	 * @param context
	 */
	protected AbstractDownloader()
	{
		taskMode = TaskMode.Multi;
		maxActiveThreadNum = MAX_ACTIVE_THREAD_NUM;
		setStatus(DownloaderStatus.STATUS_INIT);
	}
	
	/**
	 * Set the LorisContext.
	 * 
	 * @param context
	 */
	public void setLorisContext(LorisContext context)
	{
		this.context = context;
	}

	/**
	 * Set the base url string value.
	 * @param url
	 */
	public void setBaseURL(String url)
	{
		this.baseURL = url;
	}
	
	/**
	 * Get the Base url string value.
	 * 
	 * @return
	 */
	public String getBaseURL()
	{
		return baseURL;
	}
	
	/**
	 * Add the page to be process.
	 * 
	 * @param page
	 */
	public void addPage(WebPage page)
	{
		pages.put(page);
	}
	
	/**
	 * Get the table name.
	 * @return
	 */
	public String getTablename()
	{
		return tablename;
	}
	
	/**
	 * Set the Table name.
	 * @param name
	 */
	public void setTablename(String name)
	{
		this.tablename = name;
	}
	
	/**
	 * Get the WebPage and delete from the collection.
	 * @return
	 */
	public WebPage popWebPage()
	{
		return (WebPage) pages.get();
	}
	
	public DownSetting getWebPageSetting()
	{
		return setting;
	}

	public void setWebPageSetting(DownSetting webPageInfo)
	{
		this.setting = webPageInfo;
	}

	/**
	 * Check if there are more WebPage to download.
	 */
	@Override
	public boolean hasNextWebPage()
	{
		return pages.length() > 0;
	}
	
	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void setName(String name)
	{
		this.name = name;		
	}
	
	/**
	 * Get the type.
	 */
	@Override
	public String getType()
	{
		return type;
	}
	
	/**
	 * Set the type.
	 */
	@Override
	public void setType(String type)	
	{
		this.type = type;
	}

	/**
	 * 现有需要下载的数量
	 * 
	 * @return The pages number to be downloaded.
	 */
	public int leftSize()
	{
		return pages.length();
	}
	
	/**
	 * 下载管理器需要下载的总数
	 * 
	 * @return 下载管理器需要下载的总数 
	 */
	@Override
	public int totalSize()
	{
		return totalSize;
	}
	
	/**
	 * Get the enable flag.
	 */
	@Override
	public boolean getEnable()
	{
		return enable;
	}

	/**
	 * Set the enable flag.
	 */
	public void setEnable(boolean enable)
	{
		this.enable = enable;
	}
	
	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}
	
	/**
	 * Stop the manager.
	 */
	public void stopDownloader()
	{
		this.stopManager = true;
		//this.state = PageManagerStatus.STATUS_STOP;
	}
	
	/**
	 * Restart the manager.
	 */
	@Override
	public void restartDownloader()
	{
		stopManager = false;
		setStatus(DownloaderStatus.STATUS_DOWN);
	}
	
	/**
	 * The Status of the value.	
	 */
	@Override
	public int getStatus()
	{
		return state;
	}

	/**
	 * Set the Status of the WebPageManager.
	 */
	@Override
	public void setStatus(int status)
	{
		this.state = status;
		if(setting != null)
		{	
			if(status == DownloaderStatus.STATUS_DOWN)
			{
				setting.setDowntime(DateUtil.getCurTimeStr());
			}
			else if(status == DownloaderStatus.STATUS_PREPARED)
			{
				setting.setPreparetime(DateUtil.getCurTimeStr());
				prepared = true;
			}
			else if(status == DownloaderStatus.STATUS_STOP)
			{
				setting.setStoptime(DateUtil.getCurTimeStr());
			}
			else if(status == DownloaderStatus.STATUS_FINISH)
			{
				setting.setFinishtime(DateUtil.getCurTimeStr());
			}

			setting.setStatus(status);
			setting.setCurindex(getCurIndex());
			setting.setTotal(totalSize());
			setting.setLeft(leftSize());
			

			Object obj = getCurrent();
			//System.out.println("Setting status info: " + obj);
			if(obj != null && obj instanceof WebPage)
			{
				setting.setInfo(((WebPage)obj).getFullURL());
			}
			else
			{
				setting.setInfo("");
			}

		}
		notifyWebPageStatusListeners(status);
	}
	
	@Override
	public void afterDownload(WebPage page, boolean flag)
	{
		if(setting != null)
		{
			setting.setCurindex(getCurIndex());
			setting.setLeft(leftSize());
			setting.setTotal(totalSize());
			
			Object obj = getCurrent();
			if(obj != null && obj instanceof WebPage)
			{
				setting.setInfo(((WebPage)obj).getFullURL());
			}
			else
			{
				setting.setInfo("");
			}
		}
	}

	
	/**
	 * 下载数据页面
	 * 
	 * @param page 网络页面
	 * @return 是否下载成功的标志
	 */
	public boolean download(WebPage page) throws UrlFetchException
	{
		return UrlFetcher.fetch(page);
	}
	
	/**
	 * 获得当前下载的对象
	 * @return 当前下载的对象
	 */
	public Object getCurrent()
	{
		if(pages == null)
		{
			return null;
		}
		return pages.getCurrent();
	}
	
	/**
	 * 通知监听器
	 */
	protected void notifyWebPageStatusListeners(int status)
	{
		WebPageStatusEvent event = new WebPageStatusEvent(this, status);
		for (WebPageStatusListener listener : listeners)
		{
			listener.webPageStatusChanged(event);
		}
	}

	/**
	 * Get the Encoding.
	 * @return
	 */
	public String getEncoding()
	{
		return encoding;
	}
	
	public int getInterval()
	{
		return interval;
	}

	public void setInterval(int interval)
	{
		this.interval = interval;
	}
	
	@Override
	public boolean prepare()
	{
		return true;
	}
	
	@Override
	public void afterPrepared()
	{
		if(totalSize <= 0)
		{
			totalSize = pages.length();
		}
		logger.info("There are " + totalSize + " pages and there are " + leftSize() + " pages to be downloaded.");
	}

	@Override
	public void beforeDownload(WebPage page)
	{
	}

	

	@Override
	public boolean isDownloaded(WebPage page)
	{
		return false;
	}

	@Override
	public void close() throws IOException
	{
	}

	public boolean isStopped()
	{
		return stopManager;
	}
	
	@Override
	public boolean isPrepared()
	{
		return prepared;
	}

	public void setStopManager(boolean stopManager)
	{
		this.stopManager = stopManager;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
	@Override
	public void incCounter()
	{
		curIndex ++;
	}
	
	public int getCurIndex()
	{
		return curIndex;
	}	
	
	/**
	 * Add the WebPageStatusListener.
	 * 
	 */
	@Override
	public void addWebPageStatusListener(WebPageStatusListener listener)
	{
		listeners.add(listener);		
	}
	
	public String getStart()
	{
		return start;
	}

	public void setStart(String start)
	{
		this.start = start;
	}

	public String getEnd()
	{
		return end;
	}

	public void setEnd(String end)
	{
		this.end = end;
	}

	public int getMaxActiveThreadNum()
	{
		return maxActiveThreadNum;
	}

	public void setMaxActiveThreadNum(int maxActiveThreadNum)
	{
		this.maxActiveThreadNum = maxActiveThreadNum;
	}

	public TaskMode getTaskMode()
	{
		return taskMode;
	}

	public void setTaskMode(TaskMode taskMode)
	{
		this.taskMode = taskMode;
	}

	@Override
	public String toString()
	{
		return "AbstractWebPageManager [baseURL=" + baseURL + ", name=" + name + ", tableName=" + tablename + ", type="
				+ type + ", enable=" + enable + ", encoding=" + encoding + ", pages=" + pages + ", interval=" + interval
				+ "]";
	}
}
