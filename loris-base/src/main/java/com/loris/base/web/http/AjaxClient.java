package com.loris.base.web.http;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.log4j.Logger;

import com.loris.base.web.config.ConfigParser;
import com.loris.base.web.page.WebPage;

public class AjaxClient
{
	private static Logger log = Logger.getLogger(AjaxClient.class);
	/**
	 * Get the ajax method information
	 * 
	 * @param page
	 * @return
	 */
	public static byte[] post(WebPage page) throws UrlFetchException
	{
		byte[] content = null;
		
		HttpClient client = new HttpClient();
		Map<String, String> params = page.getHeaders();
		int paramSize = params.size();
		NameValuePair[] postData = new NameValuePair[paramSize];

		int i = 0;
		for (String key : params.keySet())
		{
			postData[i] = new NameValuePair(key, params.get(key));
		}

		HttpConnectionParams managerParams = client.getHttpConnectionManager().getParams();
		// RequestConfig requestConfig =
		// RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
		// .build();
		managerParams.setConnectionTimeout(ConfigParser.getSettings().getConnectionTimeout());
		// managerParams.setParameter(ClientPNames.COOKIE_POLICY,
		// CookiePolicy.BEST_MATCH);

		// httpclient.setConnectionTimeout(ConfigParser.getSettings().getConnectionTimeout());
		client.setState(CookieManager.getHttpState(page.getFullURL()));

		PostMethod httppost = new PostMethod(page.getFullURL());
		httppost.addParameters(postData);
		Map<String, String> headers = ConfigParser.getSettings().getHeaders();
		if (headers != null)
		{
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
					httppost.addRequestHeader(key, value);
				}
			}
		}

		try
		{
			// Execute the method.
			int statusCode = client.executeMethod(httppost);

			if (statusCode != HttpStatus.SC_OK)
			{
				log.info("Method failed: " + httppost.getStatusLine());
			}

			// Read the response body.
			content = httppost.getResponseBody();

		}
		catch (Exception e)
		{
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

	public static void method(HttpClient client, String url)
	{
		PostMethod httppost = new PostMethod(url);
		// "count":10,"ignoreCase":"false","paras":["a%"],"queryId":"getMenu"
		// NameValuePair pair = new NameValuePair("data",
		// "source_league_id=36&season=2017-2018&currentRound=2&seasonType=");

		NameValuePair pair0 = new NameValuePair("source_league_id", "36");
		NameValuePair pair1 = new NameValuePair("season", "2017-2018");
		NameValuePair pair2 = new NameValuePair("currentRound", "3");
		NameValuePair pair3 = new NameValuePair("dataType", "html");
		NameValuePair[] postData = new NameValuePair[]
		{
				pair0, pair1, pair2, pair3
		};

		// RequestEntity entity = new RequestEntity();

		// postData[0] = new NameValuePair("count", 10);
		// method.setRequestBody(body);// addParameters(postData);
		httppost.addParameters(postData);

		Map<String, String> headers = ConfigParser.getSettings().getHeaders();
		if (headers != null)
		{
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
					httppost.addRequestHeader(key, value);
				}
			}
		}

		// Provide custom retry handler is necessary
		/*
		 * method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
		 * new DefaultHttpMethodRetryHandler(3, false));
		 */

		try
		{
			// Execute the method.
			int statusCode = client.executeMethod(httppost);

			if (statusCode != HttpStatus.SC_OK)
			{
				System.err.println("Method failed: " + httppost.getStatusLine());
			}
			System.out.println("Status code : " + statusCode);

			// Read the response body.
			byte[] responseBody = httppost.getResponseBody();

			// Deal with the response.
			// Use caution: ensure correct character encoding and is not binary
			// data
			System.out.println("Test Data is : " + responseBody.length);
			System.out.println(new String(responseBody, "utf-8"));
		}
		catch (HttpException e)
		{
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			// Release the connection.
			httppost.releaseConnection();
		}
	}
}
