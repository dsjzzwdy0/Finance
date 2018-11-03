/*
 * @Author Irakli Nadareishvili
 * CVS-ID: $Id: UrlFetcher.java,v 1.4 2005/09/06 18:37:29 idumali Exp $
 *
 * Copyright (c) 2004 Development Gateway Foundation, Inc. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Common Public License v1.0 which accompanies this
 * distribution, and is available at:
 * http://www.opensource.org/licenses/cpl.php
 *
 *****************************************************************************/

package com.loris.base.web.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.log4j.Logger;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.web.WebPage;
import com.loris.base.util.DateUtil;
import com.loris.base.web.config.ConfigParser;
import com.loris.base.web.scheduler.MonitorThread;
import com.loris.base.web.util.Monitor;
import com.loris.base.web.util.DashBoard;

/**
 * This class is used to fetch an HTML of a page from a given URL.
 */
public class UrlFetcher
{

	private static Logger log = Logger.getLogger(UrlFetcher.class);
	// Used in parse() for the initial capacity of a set
	// private static final int AVERAGE_NUM_OF_LINKS = 100;
	// private static Pattern pattern;
	public static MultiThreadedHttpConnectionManager connectionManager;

	static
	{
		// final int flags = Pattern.CASE_INSENSITIVE | Pattern.DOTALL |
		// Pattern.MULTILINE | Pattern.UNICODE_CASE
		// | Pattern.CANON_EQ;

		// Match groups 1 and 3 are just for debugging, we are only interested
		// in group 2nd.
		// String regexp =
		// "<a.*?\\shref\\s*=\\s*([\\\"\\']*)(.*?)([\\\"\\'\\s]).*?>";
		// String regexp =
		// "<a.*?\\shref\\s*=\\s*([\\\"\\']*)(.*?)([\\\"\\'\\s].*?>|>)";

		// log.debug("Regular Expression to catch all anchors: " + regexp);
		// UrlFetcher.pattern = Pattern.compile(regexp, flags);

		// -- Reusable conneciton manager.
		connectionManager = new MultiThreadedHttpConnectionManager();
	}

	/**
	 * Fetch the page data from web.
	 * 
	 * @param page
	 * @return
	 * @throws UrlFetchException
	 */
	public static boolean fetch(WebPage page) throws UrlFetchException
	{
		log.debug("Fetching page: " + page);

		// String urlString = page.getURL();
		String method = page.getMethod();
		byte[] bs;
		if ("post".equalsIgnoreCase(method))
		{
			bs = postByteData(page);
		}
		else
		{
			bs = fetchByteData(page);
		}

		// 仅仅为字节流
		if (page.isBytevalue())
		{
			page.setBytes(bs);
		}
		else
		{
			try
			{
				page.setContent(uncompress(page.getEncoding(), bs, page.getZiptype()));
			}
			catch (Exception e)
			{
				log.warn("Error in Encoding " + page.getFullURL() + ": " + e);
				return false;
			}
		}
		// page.setBytes(bs);
		page.setCompleted(true);
		page.setLoadtime(DateUtil.getCurTimeStr());
		return true;
	}

	/**
	 * 数据解码，按照数据编码的方式对数据进行解析
	 * 
	 * @param encoding
	 *            数据字符编码
	 * @param bytes
	 *            字节串
	 * @return 解码后的数据字符串
	 * @throws IOException
	 *             输入输出流异常
	 */
	public static String uncompress(String encoding, byte[] bytes, String ziptype) throws IOException
	{
		if ("gzip".equalsIgnoreCase(ziptype))
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			GZIPInputStream gunzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = gunzip.read(buffer)) >= 0)
			{
				out.write(buffer, 0, n);
			}

			// toString()使用平台默认编码，也可以显式的指定如toString(&quot;GBK&quot;)
			if (StringUtils.isNotEmpty(encoding))
			{
				return out.toString(encoding);
			}
			else
			{
				return out.toString();
			}
		}
		else
		{
			return new String(bytes, encoding);
		}
	}

	/**
	 * 
	 * @param urlString
	 * @return
	 * @throws UrlFetchException
	 */
	protected static byte[] postByteData(String urlString) throws UrlFetchException
	{
		// log.debug("Fetching URL " + urlString);

		byte[] content = null;

		// Prepare HTTP client instance
		HttpClient httpclient = new HttpClient(connectionManager);
		// HttpClientConnectionManager connectionManager = new
		// MultiThreadedHttpConnectionManager();
		// CloseableHttpClient httpClient2 =
		// HttpClients.custom().setConnectionManager(connectionManager).build();
		// httpclient.setHttpConnectionManager(connectionManager);

		HttpConnectionParams managerParams = httpclient.getHttpConnectionManager().getParams();
		// RequestConfig requestConfig =
		// RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
		// .build();
		managerParams.setConnectionTimeout(ConfigParser.getSettings().getConnectionTimeout());
		// managerParams.setParameter(ClientPNames.COOKIE_POLICY,
		// CookiePolicy.BEST_MATCH);

		// httpclient.setConnectionTimeout(ConfigParser.getSettings().getConnectionTimeout());
		httpclient.setState(CookieManager.getHttpState(urlString));

		// Prepare HTTP GET method
		PostMethod httpost = null;
		try
		{
			httpost = new PostMethod(urlString);
		}
		catch (IllegalArgumentException ex1)
		{
			throw new UrlFetchException();
		}

		// 系统统一配置的数据请求头
		Map<String, String> headers = ConfigParser.getSettings().getHeaders();
		if (headers != null)
		{
			addHeaderParameters(httpost, headers);
		}

		// Execute HTTP GET
		int result = 0;
		try
		{

			long startTime = System.currentTimeMillis();
			result = httpclient.executeMethod(httpost);

			if (result == HttpUtil.NOT_FOUND_404)
			{
				throw (new UrlFetchException("Page not Found."));
			}
			

			content = httpost.getResponseBody();

			// byte[] bs = httpget.getResponseBody();
			// System.out.println(new String(bs));
			// httpget.get
			// System.out.println(httpget.getResponseCharSet());

			long endTime = System.currentTimeMillis();
			DashBoard.add(urlString, endTime - startTime);

			// log.debug( "Content: " );
			// log.debug( content );

			// Save the cookies
			Cookie[] cookies = httpclient.getState().getCookies();
			for (int i = 0; i < cookies.length; i++)
			{
				CookieManager.addCookie(cookies[i]);
			}

			MonitorThread.calculateCurrentSpeed();

			synchronized (Monitor.watch)
			{
				Monitor.fetchedCounter++;
			}
			// log.info("FETCHED " + Crawler.fetchedCounter + "th URL: " +
			// urlString + " " + result);
			// log.debug ( "Response code: " + result );
			// CookieManager.printAllCookies();

			String redirectLocation;
			Header locationHeader = httpost.getResponseHeader("location");
			if (locationHeader != null)
			{
				redirectLocation = locationHeader.getValue();
				log.debug("Redirect Location: " + redirectLocation);

				if (redirectLocation != null)
				{
					// Perform Redirect!
					content = fetchByteArray(redirectLocation);
				}
				else
				{
					// The response is invalid and did not provide the new
					// location for
					// the resource. Report an error or possibly handle the
					// response
					// like a 404 Not Found error.
					log.error("Error redirecting");
				}

			}

		}
		catch (Exception ex)
		{
			throw (new UrlFetchException(ex));
		}
		finally
		{
			// Release current connection to the connection pool once you are
			// done
			httpost.releaseConnection();
		}

		/*
		 * log.debug( " C O O K I E S: " ); CookieManager.printAllCookies();
		 */
		return content;
	}

	/**
	 * 
	 * @param urlString
	 * @return
	 * @throws UrlFetchException
	 */
	protected static byte[] fetchByteData(WebPage page) throws UrlFetchException
	{
		String urlString = page.getFullURL();
		log.debug("Fetching URL " + urlString);

		byte[] content = null;

		// Prepare HTTP client instance
		HttpClient httpclient = new HttpClient(connectionManager);
		// HttpClientConnectionManager connectionManager = new
		// MultiThreadedHttpConnectionManager();
		// CloseableHttpClient httpClient2 =
		// HttpClients.custom().setConnectionManager(connectionManager).build();
		// httpclient.setHttpConnectionManager(connectionManager);

		HttpConnectionParams managerParams = httpclient.getHttpConnectionManager().getParams();
		// RequestConfig requestConfig =
		// RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
		// .build();
		managerParams.setConnectionTimeout(ConfigParser.getSettings().getConnectionTimeout());
		// managerParams.setParameter(ClientPNames.COOKIE_POLICY,
		// CookiePolicy.BEST_MATCH);

		// httpclient.setConnectionTimeout(ConfigParser.getSettings().getConnectionTimeout());
		httpclient.setState(CookieManager.getHttpState(urlString));

		// Prepare HTTP GET method
		GetMethod httpget = null;
		try
		{
			httpget = new GetMethod(urlString);
		}
		catch (IllegalArgumentException ex1)
		{
			ex1.printStackTrace();
			throw new UrlFetchException();
		}

		// 系统统一配置的数据请求头
		Map<String, String> headers = ConfigParser.getSettings().getHeaders();
		if (headers != null)
		{
			addHeaderParameters(httpget, headers);
		}

		// 请求某页面特殊的请求数据头
		headers = page.getHeaders();
		if (page.isHasMoreHeader() && headers != null)
		{
			addHeaderParameters(httpget, headers);
		}

		// Execute HTTP GET
		int result = 0;
		try
		{

			long startTime = System.currentTimeMillis();
			result = httpclient.executeMethod(httpget);
			page.setHttpstatus(result);
			
			if (result == HttpUtil.NOT_FOUND_404)
			{
				throw (new UrlFetchException("Page not Found."));
			}
			else if (result == HttpStatus.SC_FORBIDDEN)
			{
				page.setHttpstatus(result);
			}

			content = httpget.getResponseBody();

			// byte[] bs = httpget.getResponseBody();
			// System.out.println(new String(bs));
			// httpget.get
			// System.out.println(httpget.getResponseCharSet());

			long endTime = System.currentTimeMillis();
			DashBoard.add(urlString, endTime - startTime);

			// log.debug( "Content: " );
			// log.debug( content );

			// Save the cookies
			Cookie[] cookies = httpclient.getState().getCookies();
			for (int i = 0; i < cookies.length; i++)
			{
				CookieManager.addCookie(cookies[i]);
			}

			MonitorThread.calculateCurrentSpeed();

			synchronized (Monitor.watch)
			{
				Monitor.fetchedCounter++;
			}
			// log.info("FETCHED " + Crawler.fetchedCounter + "th URL: " +
			// urlString + " " + result);
			// log.debug ( "Response code: " + result );
			// CookieManager.printAllCookies();

			String redirectLocation;
			Header locationHeader = httpget.getResponseHeader("location");
			if (locationHeader != null)
			{
				redirectLocation = locationHeader.getValue();
				log.debug("Redirect Location: " + redirectLocation);

				if (redirectLocation != null)
				{
					// Perform Redirect!
					content = fetchByteArray(redirectLocation);
				}
				else
				{
					// The response is invalid and did not provide the new
					// location for
					// the resource. Report an error or possibly handle the
					// response
					// like a 404 Not Found error.
					log.error("Error redirecting");
				}

			}

		}
		catch (Exception ex)
		{
			throw (new UrlFetchException(ex));
		}
		finally
		{
			// Release current connection to the connection pool once you are
			// done
			httpget.releaseConnection();
		}

		return content;
	}

	/**
	 * Fetch the byte data.
	 * 
	 * @param urlString
	 * @return
	 * @throws UrlFetchException
	 */
	protected static byte[] fetchByteArray(String urlString) throws UrlFetchException
	{
		log.debug("Fetching URL " + urlString);

		byte[] content = null;

		// Prepare HTTP client instance
		HttpClient httpclient = new HttpClient(connectionManager);

		HttpConnectionManagerParams managerParams = httpclient.getHttpConnectionManager().getParams();
		managerParams.setConnectionTimeout(ConfigParser.getSettings().getConnectionTimeout());
		httpclient.setState(CookieManager.getHttpState(urlString));

		// Prepare HTTP GET method
		GetMethod httpget = null;
		try
		{
			httpget = new GetMethod(urlString);
		}
		catch (IllegalArgumentException ex1)
		{
			ex1.printStackTrace();
			throw new UrlFetchException(ex1.toString());
		}

		Map<String, String> headers = ConfigParser.getSettings().getHeaders();
		if (headers != null)
		{
			addHeaderParameters(httpget, headers);
		}

		// Execute HTTP GET
		int result = 0;
		try
		{

			long startTime = System.currentTimeMillis();
			result = httpclient.executeMethod(httpget);

			if (result == HttpUtil.NOT_FOUND_404)
			{
				throw (new UrlFetchException("Page not Found."));
			}

			content = httpget.getResponseBody();

			// byte[] bs = httpget.getResponseBody();
			// System.out.println(new String(bs));
			// httpget.get
			// System.out.println(httpget.getResponseCharSet());

			long endTime = System.currentTimeMillis();
			DashBoard.add(urlString, endTime - startTime);

			// log.debug( "Content: " );
			// log.debug( content );

			// Save the cookies
			Cookie[] cookies = httpclient.getState().getCookies();
			for (int i = 0; i < cookies.length; i++)
			{
				CookieManager.addCookie(cookies[i]);
			}

			MonitorThread.calculateCurrentSpeed();

			synchronized (Monitor.watch)
			{
				Monitor.fetchedCounter++;
			}
			// log.info("FETCHED " + Crawler.fetchedCounter + "th URL: " +
			// urlString + " " + result);
			// log.debug ( "Response code: " + result );
			// CookieManager.printAllCookies();

			String redirectLocation;
			Header locationHeader = httpget.getResponseHeader("location");
			if (locationHeader != null)
			{
				redirectLocation = locationHeader.getValue();
				log.debug("Redirect Location: " + redirectLocation);

				if (redirectLocation != null)
				{
					// Perform Redirect!
					content = fetchByteArray(redirectLocation);
				}
				else
				{
					// The response is invalid and did not provide the new
					// location for
					// the resource. Report an error or possibly handle the
					// response
					// like a 404 Not Found error.
					log.error("Error redirecting");
				}

			}

		}
		catch (Exception ex)
		{
			throw (new UrlFetchException(ex));
		}
		finally
		{
			// Release current connection to the connection pool once you are
			// done
			httpget.releaseConnection();
		}

		/*
		 * log.debug( " C O O K I E S: " ); CookieManager.printAllCookies();
		 */
		return content;
	}

	/**
	 * Get the ajax method information
	 * 
	 * @param page
	 * @return
	 */
	protected static byte[] postByteData(WebPage page) throws UrlFetchException
	{
		byte[] content = null;
		HttpClient client = new HttpClient();

		HttpConnectionParams managerParams = client.getHttpConnectionManager().getParams();
		// RequestConfig requestConfig =
		// RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
		// .build();
		managerParams.setConnectionTimeout(ConfigParser.getSettings().getConnectionTimeout());
		// managerParams.setParameter(ClientPNames.COOKIE_POLICY,
		// CookiePolicy.BEST_MATCH);

		// httpclient.setConnectionTimeout(ConfigParser.getSettings().getConnectionTimeout());
		client.setState(CookieManager.getHttpState(page.getFullURL()));

		// Prepare HTTP GET method
		PostMethod httppost = null;
		try
		{
			httppost = new PostMethod(page.getFullURL());
		}
		catch (IllegalArgumentException ex1)
		{
			ex1.printStackTrace();
			throw new UrlFetchException(ex1.toString());
		}

		// 这里是添加请求参数数据
		Map<String, String> params = page.getParams();
		if (params != null)
		{
			int paramSize = params.size();
			NameValuePair[] postData = new NameValuePair[paramSize];

			int i = 0;
			for (String key : params.keySet())
			{
				postData[i++] = new NameValuePair(key, params.get(key));
			}
			httppost.addParameters(postData);
		}

		// PostMethod httppost = new PostMethod(page.getURL());
		// httppost.addParameters(postData);
		Map<String, String> headers = ConfigParser.getSettings().getHeaders();
		if (headers != null)
		{
			addHeaderParameters(httppost, headers);
		}

		// 头部请求数据自定义
		headers = page.getHeaders();
		if (page.isHasMoreHeader() && headers != null)
		{
			addHeaderParameters(httppost, headers);			
		}

		try
		{
			// Execute the method.
			int statusCode = client.executeMethod(httppost);
			page.setHttpstatus(statusCode);

			if(statusCode == HttpStatus.SC_NOT_FOUND)
			{
				throw (new UrlFetchException("Page not Found."));
			}
			else if (statusCode == HttpStatus.SC_FORBIDDEN)
			{
				page.setHttpstatus(statusCode);
			}
			else if (statusCode != HttpStatus.SC_OK)
			{
				log.info("Method failed: " + httppost.getStatusLine());

			}

			// Read the response body.
			content = httppost.getResponseBody();

		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new UrlFetchException(e);
		}
		finally
		{
			try
			{
				httppost.releaseConnection();

			}
			catch (Exception e)
			{
			}
		}
		return content;
	}

	/**
	 * 添加客户端请求数据的头部数据
	 * 
	 * @param httpMethod
	 *            请求方法
	 * @param headers
	 *            头部数据
	 */
	protected static void addHeaderParameters(HttpMethod httpMethod, Map<String, String> headers)
	{
		if (headers == null)
		{
			return;
		}
		
		String value;
		for (String key : headers.keySet())
		{
			value = headers.get(key);
			httpMethod.addRequestHeader(key, value);
		}

		/*
		Set<String> headerKeys = headers.keySet();
		if (headerKeys != null)
		{
			Iterator<String> itHeaders = headerKeys.iterator();
			String key, value;

			while (itHeaders.hasNext())
			{
				key = (String) itHeaders.next();
				value = (String) headers.get(key);
				// log.debug( key + "=" + value);
				httpMethod.addRequestHeader(key, value);
			}
		}*/

	}

	/**
	 * Method fetching a url and returning HTML output.
	 *
	 * @param urlString
	 *            String
	 * @throws NullPointerException
	 * @return String
	 */

	public static String fetch(String urlString) throws UrlFetchException
	{
		String content = "";
		byte[] bs = fetchByteArray(urlString);
		if (bs != null)
		{
			content = new String(bs);
		}
		return content;

	}

	/**
	 * Method parsing HTML and returning a set of links found in there.
	 *
	 * @param url
	 *            String needed to compute the absolute pathes from the relative
	 *            pathes in HTML.
	 * @param html
	 *            String
	 * @return java.util.Set or null if no URLs found after parse.
	 * @todo In the current implementation HTML FRAME-based sites are not
	 *       parsed.
	 * 
	 * 
	 *       public static Set<String> parse(String url, String html) {
	 * 
	 *       Set<String> anchors = Collections.synchronizedSet(new
	 *       HashSet<String>(UrlFetcher.AVERAGE_NUM_OF_LINKS));
	 * 
	 *       Matcher matcher = UrlFetcher.pattern.matcher(html);
	 * 
	 *       // Debug code // matcher.find(); // for ( int i=0;
	 *       i<=matcher.groupCount(); i++) { //
	 *       log.debug("#"+matcher.group(i)+"#"); // }
	 * 
	 *       String domain = null; try { domain =
	 *       HttpUtil.getDomainFromUrl(url); } catch (MalformedURLException ex)
	 *       { // We can not parse URL that is malformed. return new
	 *       HashSet<String>(); }
	 * 
	 *       String baseURI = null; try { baseURI =
	 *       HttpUtil.getBaseUriFromUrl(url); } catch (MalformedURLException
	 *       ex1) { // We can not parse URL that is malformed. return new
	 *       HashSet<String>(); }
	 * 
	 *       String currentUrl = ""; boolean wrongURL = false;
	 * 
	 *       while (matcher.find()) {
	 * 
	 *       currentUrl = HttpUtil.canonizeURL(domain, baseURI,
	 *       matcher.group(2));
	 * 
	 *       wrongURL = false; try { new URL(currentUrl); } catch
	 *       (MalformedURLException ex2) { wrongURL = true; }
	 * 
	 *       // log.debug ( currentUrl );
	 * 
	 *       synchronized (Crawler.watch) { if (wrongURL == false &&
	 *       Crawler.crawlOrNot(currentUrl) == true) { // synchronized ( anchors
	 *       ) { anchors.add(currentUrl); // } } } }
	 * 
	 *       return anchors; }
	 */

}
