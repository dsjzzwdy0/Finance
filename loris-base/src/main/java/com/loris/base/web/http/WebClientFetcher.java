package com.loris.base.web.http;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.loris.base.bean.web.WebPage;
import com.loris.base.util.DateUtil;

/**
 * 
 * 网络数据下载 ，采用模拟客户端方式
 * 
 * @author jiean
 *
 */
public class WebClientFetcher implements AutoCloseable
{
	private static Logger logger = Logger.getLogger(WebClientFetcher.class);
	
	/** 这里采用单线程处理的方式 */
	WebClient client = null;
	
	/**
	 * 创建一个网络页面下载器，用于数据的下载
	 * 
	 * @param baseUrl 起始页面
	 * @return 返回下载器
	 * @throws Exception 抛出异常数据
	 */
	public static WebClientFetcher createFetcher(WebPage basePage) throws Exception
	{		
		WebClientFetcher fetcher = new WebClientFetcher();
		fetcher.createWebClient();
		try
		{
			//这里需要有一个起始页面，原因是为了获取起始页面的Cookie值
			if(basePage != null)
			{
				logger.info("Fetching Base Page: " + basePage.getFullURL());
				HtmlPage page = fetcher.fetch(basePage.getFullURL());
				String content = page.asXml();
				
				//检测数据是否为空
				if(StringUtils.isNotEmpty(content))
				{			
					basePage.setContent(content);
					basePage.setCompleted(true);
					basePage.setLoadtime(DateUtil.getCurTimeStr());
				}
			}
		}
		catch(Exception exception)
		{
			logger.info("Error when create Fetcher: " + exception.toString());
		}
		return fetcher;
	}
	
	/**
	 * 下载数据页面
	 * 
	 * @param url
	 * @throws IOException
	 */
	public HtmlPage fetch(String url) throws IOException
	{
		return client.getPage(url);
	}
	
	/**
	 * 下载网络数据
	 * 
	 * @param page
	 * @param client
	 * @param time
	 * @return
	 * @throws IOException
	 */
	protected static int fetch(WebPage page, WebClient client, int time) throws IOException
	{
		try
		{
			HtmlPage page2 = client.getPage(page.getFullURL());
			page.setContent(page2.asXml());
			page.setCompleted(true);
			page.setLoadtime(DateUtil.getCurTimeStr());
			int status = page2.getWebResponse().getStatusCode();
			page.setHttpstatus(status);	
			return status;
		}
		catch(com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException e)
		{
			e.printStackTrace();
			logger.info("Retrieve the page again. " + time);
			time ++;
			if(time >= 3)
			{
				return -1;
			}
			return fetch(page, client, time);
		}
	}
	
	/**
	 * Post the data.
	 * @param page Page Object.
	 * @param client WebClient
	 * @return times.
	 * @throws IOException
	 */
	protected static int post(WebPage page, WebClient client) throws IOException
	{
		String url = page.getFullURL();
		WebRequest request = new WebRequest(new URL(url));
		request.setHttpMethod(HttpMethod.POST);
		
		Map<String, String> params = page.getParams();		
		List<NameValuePair> requestParameters = new ArrayList<>();
        if (params != null && params.size() > 0)
        {  
            for (Entry<String, String> param : params.entrySet())
            {  
                requestParameters.add(new NameValuePair(param.getKey(), param.getValue()));  
            }  
        }         
        request.setRequestParameters(requestParameters);
        
        Page p = client.getPage(request);
        WebResponse webResponse = p.getWebResponse();  
        int status = webResponse.getStatusCode();
        if(p.isHtmlPage())
        {
        	 //client.waitForBackgroundJavaScript(100000);  
             page.setContent(((HtmlPage) p).asXml());
             page.setCompleted(true);
             page.setLoadtime(DateUtil.getCurTimeStr());
             page.setHttpstatus(status);
        }
        
		return status;
	}
	
	/**
	 * 添加访问头
	 * @param name 名称
	 * @param value 值
	 */
	public void addHeader(String name, String value)
	{
		client.addRequestHeader("Referer", value);
	}
	
	/**
	 * 删除访问头信息控制
	 * @param name 名称
	 */
	public void removeHeader(String name)
	{
		client.removeRequestHeader(name);
	}
	
	
	/**
	 * 下载一个数据页面
	 * 
	 * @param page 网络页
	 * @return 是否下载成功的标志
	 */
	public boolean fetch(WebPage page)
	{
		//添加网络请求头数据支持项
		if(page.isHasMoreHeader())
		{
			addHeaders(page.getHeaders());
		}

		logger.info("Fetching: " + page.getFullURL());
		try
		{
			if(page.getMethod() == WebPage.HTTP_METHOD_POST)
			{
				int status = post(page, client);
				logger.info("Download Page: " + page + ", Status=" + status + "\r\n");
				//page.getContent();
				return status == 200;
			}
			else
			{
				int status = fetch(page, client, 0);
				return status >= 0;
			}				
		}
		catch (FailingHttpStatusCodeException e)
		{
			
			e.printStackTrace();
			logger.info("Error occured when loading " + page.getFullURL() + ".");
			
			int statusCode = e.getStatusCode();
			if(statusCode == 503)
			{
				page.setHttpstatus(HttpStatus.SC_FORBIDDEN);
			}
			return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.info("Error occured when loading " + page.getFullURL() + ".");
			return false;
		}
		finally
		{
			//删除请求头数据支持项
			if(page.isHasMoreHeader())
			{
				removeHeaders(page.getHeaders());
			}
		}
	}
	
	/**
	 * 添加请求头数据
	 * @param headers
	 */
	protected void addHeaders(Map<String, String> headers)
	{
		if(headers == null)
		{
			return;
		}
		for (String key : headers.keySet())
		{
			String value = headers.get(key);
			client.addRequestHeader(key, value);
		}
	}
	
	/**
	 * 删除已经添加的头请求数据
	 * @param headers
	 */
	protected void removeHeaders(Map<String, String> headers)
	{
		if(headers == null)
		{
			return;
		}
		for (String key : headers.keySet())
		{
			client.removeRequestHeader(key);
		}
	}
	
	/**
	 * 创建下载客户端
	 */
	protected void createWebClient()
	{
		/**
	    LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
	    		"org.apache.commons.logging.impl.NoOpLog");  
	    LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
	    		"org.apache.commons.logging.impl.NoOpLog");
	    
	    java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit")
	        .setLevel(Level.OFF);
	     
	    java.util.logging.Logger.getLogger("org.apache.commons.httpclient")
	        .setLevel(Level.OFF);
	    java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);*/
	    
		client = new WebClient(BrowserVersion.FIREFOX_52);
		client.getOptions().setJavaScriptEnabled(true);
		client.getOptions().setCssEnabled(false);
		client.getCookieManager().setCookiesEnabled(true);
		client.setAjaxController(new NicelyResynchronizingAjaxController());
		client.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常 		
		client.getOptions().setTimeout(10000); // 设置连接超时时间 ，这里是10S。如果为0，则无限期等;
	}
	
	/**
	 * 设置等待javascript的执行时间
	 * @param millseconds 毫秒数
	 */
	public void waitForBackgroundJavaScript(int millseconds)
	{
		client.waitForBackgroundJavaScript(10000);
	}
	
	/**
	 * 关闭数据下载客户端
	 * 
	 */
	@Override
	public void close()
	{
		if(client != null)
		{
			client.close();
		}
	}
}
