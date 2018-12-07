package com.loris.soccer.web.downloader.eastsoccer;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loris.base.bean.wrapper.Result;
import com.loris.base.web.page.WebPage;
import com.loris.soccer.bean.data.table.Op;
import com.loris.soccer.bean.model.OpList;

public class EastSoccerWebPageParser
{
	private static Logger logger = Logger.getLogger(EastSoccerWebPageParser.class);
	
	/**
	 * 解析数据页面
	 * @param page
	 * @return
	 */
	public static Result parseMatchOps(WebPage page)
	{
		JSONObject object = parseJSONString(page.getContent());
		if(object == null)
		{
			return null;
		}
		
		Result result = new Result();
		for (String key: object.keySet())
		{
			if("ops".equalsIgnoreCase(key))
			{
				JSONArray array = object.getJSONArray(key);
				if(array == null)
				{
					continue;
				}
				OpList ops = new OpList();
				for (Object o : array)
				{
					if(o instanceof JSONObject)
					{
						Op op = parseOp((JSONObject)o);
						if(op != null)
						{
							ops.add(op);
						}
					}
				}
				if(!ops.isEmpty())
				{
					result.put("ops", ops);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 解析页面数据
	 * @param json
	 * @return
	 */
	private static JSONObject parseJSONString(String json)
	{
		JSONObject object = JSON.parseObject(json);
		if(object == null)
		{
			return null;
		}
		
		if(object.getInteger("status") != 200)
		{
			logger.info("The page content result is not 200, no result.");
			return null;
		}
		return object.getJSONObject("data");
	}
	
	/**
	 * 转对象数据
	 * @param object
	 * @return
	 */
	protected static Op parseOp(JSONObject object)
	{
		return object.toJavaObject(Op.class);
	}
}
