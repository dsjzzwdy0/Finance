package com.loris.lottery.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.web.WebPage;
import com.loris.base.bean.wrapper.PageWrapper;
import com.loris.base.bean.wrapper.Rest;
import com.loris.base.repository.BasicManager;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.WebCrawler;
import com.loris.base.web.config.ConfigParser;
import com.loris.base.web.config.setting.DownSetting;
import com.loris.base.web.manager.DownloaderStatus;
import com.loris.base.web.manager.Downloader;
import com.loris.base.web.manager.DownloaderCreator;
import com.loris.base.web.manager.event.WebPageStatusEvent;
import com.loris.base.web.manager.event.WebPageStatusListener;
import com.loris.base.web.repository.WebPageManager;
import com.loris.base.web.util.Monitor;
import com.loris.lottery.context.ApplicationContextHelper;

@Controller
@RequestMapping("/download")
public class DownloadController extends BaseController implements WebPageStatusListener
{
	private static Logger logger = Logger.getLogger(DownloadController.class);

	@Autowired
	private BasicManager basicManager;

	/** WebCrawler. */
	protected WebCrawler crawler;

	/**
	 * Create new DownloadController.
	 */
	public DownloadController()
	{
		crawler = WebCrawler.getInstance();
		try
		{
			ConfigParser.parseWebPageSettings(DownloadController.class.getResourceAsStream("/web-downloaders.xml"));
		}
		catch(Exception e)
		{
			logger.info(e.toString());
		}
	}
	
	/**
	 * 获得数据下载页面
	 * @return 下载页面视图
	 */
	@RequestMapping("/downpage")
	public ModelAndView getDownloadPage()
	{
		ModelAndView view = new ModelAndView("downpage.down");
		Map<String, List<DownSetting>> settings = WebCrawler.getWebPageSettingMap();
		view.addObject("settings", settings);
		view.addObject("page", "downpage");
		return view;
	}
	
	/**
	 * 获得数据下载页面
	 * @return 下载页面视图
	 */
	@RequestMapping("/admin")
	public ModelAndView getDownloadList()
	{
		ModelAndView view = new ModelAndView("downadmin.down");
		Map<String, List<DownSetting>> settings = WebCrawler.getWebPageSettingMap();
		view.addObject("settings", settings);
		view.addObject("page", "admin");
		return view;
	}
	
	/**
	 * 获得数据下载页面
	 * @return 下载页面视图
	 */
	@RequestMapping("/current")
	public ModelAndView getCurrentDownloadList()
	{
		ModelAndView view = new ModelAndView("current.down");
		Map<String, List<DownSetting>> settings = WebCrawler.getWebPageSettingMap();
		view.addObject("settings", settings);
		view.addObject("page", "current");
		return view;
	}
	
	/**
	 * 获得数据下载管理器的个数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/size")
	public Rest getWebPageManagerNumber()
	{
		EntityWrapper<DownSetting> ew = new EntityWrapper<>();
		List<DownSetting> settings = basicManager.getDownSettings(ew);
		return Rest.okData(settings);
	}

	/**
	 * 基本下载管理器列表信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/basiclist")
	public Rest getBasicWebPageInfos()
	{
		Map<String, List<?>> datas = new HashMap<>();
		datas.put("basic", WebCrawler.getWebPageSettings());
		return Rest.okData(datas);
	}
	
	/**
	 * 当前正在下载的数据管理器
	 * @return 当前正在下载的数据管理器
	 */
	@ResponseBody
	@RequestMapping("/curlist")
	public Rest getCurrentDownloader()
	{
		List<Downloader> downloaders = crawler.getCurrentDownloaders();
		List<DownSetting> settings = new ArrayList<>();
		for (Downloader downloader : downloaders)
		{
			settings.add(downloader.getWebPageSetting());
		}
		return Rest.okData(settings);
	}
	
	/**
	 * 基本下载页面信息
	 * @param name 名称
	 * @return 页面信息数据
	 */
	@ResponseBody
	@RequestMapping("/basicpage")
	public Rest getBasicWebPageInfo(String name)
	{
		for (DownSetting webPageInfo : WebCrawler.getWebPageSettings())
		{
			if (webPageInfo.getName().equalsIgnoreCase(name) || webPageInfo.getWid().equalsIgnoreCase(name))
			{
				return Rest.okData(webPageInfo);
			}
		}
		return Rest.failure("No '" + name + "' basic WebPageInfo exist.");
	}

	/**
	 * 获得当前正在下载的数据管理器
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/curpage")
	public Rest getCurrentWebPageInfo()
	{
		List<DownSetting> status = new ArrayList<>();
		List<Downloader> managers = crawler.getCurrentDownloaders();
		for (Downloader webPageManager : managers)
		{
			status.add(webPageManager.getWebPageSetting());
		}
		return Rest.okList(status, status.size());
	}
	
	/**
	 * 获得下载的数据列表
	 * @param pageNumber 页面序号
	 * @param pageSize 每页数量
	 * @param sortName 排序字段
	 * @param sortOrder 排序类型（升序或降序）
	 * @return 数据记录
	 */
	@ResponseBody
	@RequestMapping("/downlist")
	public PageWrapper<DownSetting> getPageSettings(String pageNumber, String pageSize, String sortName, String sortOrder, 
			String wid, String type)
	{
		EntityWrapper<DownSetting> ew = new EntityWrapper<>();
		if(StringUtils.isNotEmpty(sortName))
		{
			boolean isAsc = true;
			if(StringUtils.isEmpty(sortOrder) || "desc".equalsIgnoreCase(sortOrder))
			{
				isAsc = false;
			}
			ew.orderBy(sortName, isAsc);
		}
		if("category".equalsIgnoreCase(type))
		{
			//如果是空，或是所有的，不做任何过滤，这里是要指所有的
			if(StringUtils.isNotEmpty(wid) && (! "all".equalsIgnoreCase(wid)))
			{
				ew.eq("category", wid);
			}
		}
		else if("node".equalsIgnoreCase(type))
		{
			ew.eq("wid", wid);
		}
		
		//logger.info("WID: " + wid + " type: " + type);
		
		//分页 pageNumber--》页数    pageSize--》每页显示数据的条数
        int pNum = NumberUtil.parseInt(pageNumber);
        int pSize = NumberUtil.parseInt(pageSize);
        
        pSize = pSize == 0 ? 20 : pSize;
        Page<DownSetting> page = new Page<>(pNum, pSize);
        Page<DownSetting> selectPage = basicManager.getDownSettingPage(page, ew);
		
		return new PageWrapper<>(selectPage);
	}
	

	/**
	 * 开始启动数据下载.
	 * 
	 * @return 开始下载的标志
	 */
	@ResponseBody
	@RequestMapping("/start")
	public Rest downloadData(DownSetting setting)
	{
		logger.info("Start Download Thread: " + setting);

		Downloader downloader = getOrCreateWebPageDownloader(setting);
		if (downloader == null)
		{
			String info = "Create download manager failure.";
			logger.info(info);
			return Rest.failure(info);
		}
		
		if(Monitor.checkInDownload(downloader))
		{
			String info = "Error, downloader " + downloader.getWebPageSetting().getId() + " is running.";
			logger.info(info);
			return Rest.failure(info);
		}

		// 启动线程
		new Thread(new WebPageRunnable(downloader)
		{
			@Override
			public void run()
			{
				crawler.startMainSchedulerThread(downloader);
			}
		}).start();

		return Rest.okData(setting);
	}

	/**
	 * 停止某一数据下载管理器 Data.
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/stop")
	public Rest stopDownload(String id)
	{
		Downloader downloader = crawler.getWebPageDownloader(id);

		if (downloader == null)
		{
			downloader = crawler.getWebPageDownloaderByID(id);
		}

		if (downloader == null)
		{
			return Rest.failure("No '" + id + "' be found.");
		}
		downloader.stopDownloader();
		return Rest.okData(downloader.getWebPageSetting());
	}

	/**
	 * Stop the down manager.
	 * 
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/stopTo")
	public Rest stopDownManager(DownSetting pageInfo)
	{
		return Rest.ok();
	}

	/**
	 * Get the status.
	 * 
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/status")
	public Rest getStatus(String id)
	{
		DownSetting status = null;
		Downloader manager = crawler.getWebPageDownloaderByID(id);
		if(manager != null)
		{
			status = manager.getWebPageSetting();
		}
		else
		{
			status = basicManager.selectById(id);
		}

		if (status == null)
		{
			return Rest.failure("No '" + id + "' be found.");
		}		
		return Rest.okData(status);
	}
	
	/**
	 * 创建页面参数
	 * @param name 名称
	 * @return 数据页面
	 */
	@ResponseBody
	@RequestMapping("/createPage")
	public Rest createPage(String name)
	{
		if(StringUtils.isEmpty(name))
		{
			name = "com.loris.soccer.web.page.LotteryWebPage";
		}
		WebPage page = WebPageManager.createWebPage(name);
		return Rest.okData(page);
	}
	

	/**
	 * 信息状态的改变
	 * 
	 * @param evt
	 *            通知的事项
	 */
	@Override
	public void webPageStatusChanged(WebPageStatusEvent evt)
	{
		Downloader downloader = evt.getWebPageDownloader();
		// 下载完成，从当前下载列表中删除
		if (evt.getStatus() == DownloaderStatus.STATUS_FINISH)
		{
			crawler.removeDownloader(downloader);
		}

		// 更新
		if (downloader != null && downloader.getWebPageSetting() != null)
		{
			// 更新数据
			basicManager.addOrUpdateDownSettingById(evt.getWebPageDownloader().getWebPageSetting());
		}
	}

	/**
	 * Create a new WebPageManager.
	 * 
	 * @param setting
	 * @return
	 */
	protected Downloader getOrCreateWebPageDownloader(DownSetting setting)
	{
		Downloader downloader = null;
		if (StringUtils.isEmpty(setting.getId()))
		{
			basicManager.addOrUpdateDownSettingById(setting);
			downloader = DownloaderCreator.createDownloader(setting, this);
		}
		else
		{
			downloader = crawler.getWebPageDownloaderByID(setting.getId());
			if(downloader == null)
			{
				downloader = DownloaderCreator.createDownloader(setting, this);
			}
			else
			{
				//设置下载管理器信息
				DownloaderCreator.setDownloader(downloader, setting);
			}
			
			//如果是已经停止，则需要重新启动
			if(downloader.isStopped())
			{
				downloader.restartDownloader();
			}
		}
		if (downloader != null)
		{			
			downloader.setLorisContext(ApplicationContextHelper.getLorisContext());
			crawler.addWebPageDownloader(downloader);
		}
		return downloader;
	}
}

/**
 * 用于启动线程
 * 
 * @author jiean
 *
 */
abstract class WebPageRunnable implements Runnable
{
	Downloader downloader;

	public WebPageRunnable(Downloader downloader)
	{
		this.downloader = downloader;
	}
}
