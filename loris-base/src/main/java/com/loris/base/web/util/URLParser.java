package com.loris.base.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLParser
{
	protected byte type;
	protected static final byte TYPE_URL = 1;
	protected static final byte TYPE_QUERY_STRING = 2;
	protected String url;
	protected String baseUrl;
	protected String queryString;
	protected String label;
	protected String charset = "utf-8";

	protected boolean compiled = false;
	public Map<String, String> parsedParams;
	protected URLDecoder urld = new URLDecoder();

	public static URLParser fromURL(String url)
	{
		URLParser parser = new URLParser();

		parser.type = 1;
		parser.url = url;

		String[] split = url.split("\\?", 2);
		parser.baseUrl = split[0];
		parser.queryString = (split.length > 1 ? split[1] : "");

		String[] split2 = url.split("#", 2);
		parser.label = (split2.length > 1 ? split2[1] : null);

		return parser;
	}

	public static URLParser fromQueryString(String queryString)
	{
		URLParser parser = new URLParser();

		parser.type = 2;
		parser.queryString = queryString;

		return parser;
	}

	public URLParser useCharset(String charset)
	{
		this.charset = charset;
		return this;
	}

	public URLParser compile() throws UnsupportedEncodingException
	{
		if (this.compiled)
		{
			return this;
		}
		String paramString = this.queryString.split("#")[0];
		String[] params = paramString.split("&");

		this.parsedParams = new HashMap<String, String>(params.length);
		for (String p : params)
		{
			String[] kv = p.split("=");
			if (kv.length == 2)
			{
				this.parsedParams.put(kv[0], URLDecoder.decode(kv[1], this.charset));
			}
		}
		this.compiled = true;

		return this;
	}

	public String getParameter(String name)
	{
		if (this.compiled)
		{
			return (String) this.parsedParams.get(name);
		}
		String paramString = this.queryString.split("#")[0];
		Matcher match = Pattern.compile("(^|&)" + name + "=([^&]*)").matcher(paramString);
		match.lookingAt();

		return match.group(2);
	}

	public URLParser setParameter(String name, String value) throws UnsupportedEncodingException
	{
		if (!this.compiled)
		{
			compile();
		}
		this.parsedParams.put(name, value);

		return this;
	}

	public String toURL() throws UnsupportedEncodingException
	{
		if (!this.compiled)
		{
			compile();
		}
		URLBuilder builder = new URLBuilder();

		if (this.type == 1)
		{
			builder.appendPath(this.baseUrl);
		}
		for (String k : this.parsedParams.keySet())
		{
			builder.appendParamEncode(k, (String) this.parsedParams.get(k), this.charset);
		}
		if (this.label != null)
		{
			builder.appendLabel(this.label);
		}
		return builder.toString();
	}

	public static void main(String[] args)
	{
		try
		{
			Pattern p = Pattern.compile("(\\d{1,4}-\\d{1,2}-\\d{1,2})");
			String str = "date=2017-10-31";
			Matcher matcher = p.matcher(str);
			System.out.println(matcher.find());
			System.out.println(matcher.start() + " ->" + matcher.end());

			String url = "http://market.finance.sina.com.cn/downxls.php?date=sh000002&symbol=2017-10-31";
			URLParser parser = URLParser.fromURL(url).compile();

			String value1 = parser.getParameter("symbol");
			String value2 = parser.getParameter("date");
			Matcher matcher2 = p.matcher(value1);

			if (matcher2.find())
			{
				parser.setParameter("symbol", value2);
				parser.setParameter("date", value1);
				
				System.out.println(parser.toURL());
			}

			System.out.println(fromURL(url).compile().getParameter("symbol"));
			/*
			 * System.err.println(
			 * fromURL(
			 * "https://www.google.com/search?q=test&hl=zh_cn&oq=test&gs_l=heirloom-serp.3...38011332.38012012.0.38012235.4.4.0.0.0.0.0.0..0.0.msedr...0...1ac.1.34.heirloom-serp..4.0.0.1q6YK2r8vHI")
			 * .compile().getParameter("gs_l"));
			 * 
			 * System.out.println(
			 * fromURL(
			 * "https://www.google.com/search?q=test&hl=zh_cn&oq=test&gs_l=heirloom-serp.3...38011332.38012012.0.38012235.4.4.0.0.0.0.0.0..0.0.msedr...0...1ac.1.34.heirloom-serp..4.0.0.1q6YK2r8vHI")
			 * .getParameter("q"));
			 * 
			 * System.out.println(
			 * fromURL(
			 * "https://www.google.com/search?q=test&hl=zh_cn&oq=test&gs_l=heirloom-serp.3...38011332.38012012.0.38012235.4.4.0.0.0.0.0.0..0.0.msedr...0...1ac.1.34.heirloom-serp..4.0.0.1q6YK2r8vHI")
			 * .compile().getParameter("q"));
			 */

			// System.out.println(
			// fromURL("https://www.google.com/search?q=test&hl=zh_cn&oq=test&gs_l=heirloom-serp.3...38011332.38012012.0.38012235.4.4.0.0.0.0.0.0..0.0.msedr...0...1ac.1.34.heirloom-serp..4.0.0.1q6YK2r8vHI#test-label")
			// .compile().setParameter("q", "tweaked").toURL());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
