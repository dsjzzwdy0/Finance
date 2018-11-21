package com.loris.soccer.web.downloader.okooo.parser;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.DateUtil;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.analysis.util.OddsUtil;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.okooo.OkoooYp;
import com.loris.soccer.web.downloader.okooo.page.OkoooRequestHeaderWebPage;

public class OddsYpChildParser extends AbstractWebPageParser
{
	private static Logger logger = Logger.getLogger(OddsYpChildParser.class);
	
	/** 亚盘数据列表 */
	protected List<OkoooYp> yps = new ArrayList<>();
	
	/** 比赛时间 */
	protected Date currentTime;
	
	/** 比赛编号 */
	protected String mid;
	
	/** 赔率类型 */
	private String oddstype;
	
	/** 赔率公司类型*/
	static final String CORP_TYPE = SoccerConstants.ODDS_TYPE_YP;
	
	public OddsYpChildParser()
	{
		oddstype = CORP_TYPE;
	}
	
	/**
	 * 解析数据
	 * @param page 数据页面
	 * @return 解析成功与否的标志
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		//页面类型不正确
		if(!(page instanceof OkoooRequestHeaderWebPage) )
		{
			logger.info("Error page parameter, parse failed.");
			return false;
		}
		
		String content = page.getContent();		
		//空字符串，则表示为解析不可用
		if(StringUtils.isEmpty(content))
		{
			logger.info("The page content value is empty, parse failed.");
			return false;
		}
		
		int pos = content.indexOf("<script>");
		int lastPos = content.indexOf("</script>");
		
		//logger.info("StartPos: " + pos + ", LastPos: " + lastPos);
		String text = content.substring(pos + 8, lastPos);
		String result = getDataStr(text);
		
		try
		{
			parseJson(result);		
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			logger.info("Error when parse OddsYp: " + e.toString());
			return false;
		}
		
		return true;
	}
	
	/**
	 * 解析对象与数据
	 * @param json Json字符串
	 */
	protected void parseJson(String json)
	{
		JSONArray array = JSON.parseArray(json);
		for (Object object : array)
		{
			if(object instanceof JSONObject)
			{
				parseOddsYp((JSONObject)object);
			}
		}
	}
	
	/**
	 * 解析亚盘记录数据
	 * @param object
	 */
	protected void parseOddsYp(JSONObject object)
	{
		OkoooYp yp = new OkoooYp();
		yp.setOddstype(oddstype);
		yp.setMid(mid);
		
		for (String key : object.keySet())
		{
			Object value = object.get(key);
			if("Start".equalsIgnoreCase(key))
			{
				parseFirstOdds(yp, (JSONObject)value);
			}
			else if("CompanyName".equalsIgnoreCase(key))
			{
				yp.setGname(value.toString());
			}
			else if("MakerID".equalsIgnoreCase(key))
			{
				yp.setGid(value.toString());
			}
			else if("End".equalsIgnoreCase(key))
			{
				parseLastOdds(yp, (JSONObject)value);
			}
			else if("Updatetime".equalsIgnoreCase(key))
			{
				int l = ((Integer)value).intValue();
				if(currentTime != null)
				{
					Date d = DateUtil.add(currentTime, l);
					yp.setLasttime(DateUtil.DATE_TIME_FORMAT.format(d));
				}
			}
			else if("Createtime".equalsIgnoreCase(key))
			{
				long l = NumberUtil.parseLong(value.toString()) * 1000;
				yp.setFirsttime(DateUtil.formatDate(l));
			}
			else if("Kelly".equalsIgnoreCase(key))
			{
				parseKelly(yp, (JSONObject)value);
			}
			else if("Radio".equalsIgnoreCase(key))
			{
				parseProb(yp, (JSONObject)value);
			}
			else if("Payoff".equalsIgnoreCase(key))
			{
				yp.setLossratio(NumberUtil.parseFloat(value.toString()));
			}
		}
		yps.add(yp);
	}
	
	/**
	 * 解析最新的亚盘数据
	 * @param yp 亚盘记录
	 * @param object 数据对象
	 */
	protected void parseLastOdds(OkoooYp yp, JSONObject object)
	{
		for (String key : object.keySet())
		{
			Object value = object.get(key);
			if("BoundaryCnName".equalsIgnoreCase(key))
			{
				String handicap = OddsUtil.formatHandicapName(value.toString());
				yp.setHandicap(handicap);
			}
			else if("home".equalsIgnoreCase(key))
			{
				yp.setWinodds(NumberUtil.parseFloat(value.toString()));
			}
			else if("away".equalsIgnoreCase(key))
			{
				yp.setLoseodds(NumberUtil.parseFloat(value.toString()));
			}
		}
	}
	
	/**
	 * 解析初始亚盘数据
	 * @param yp 亚盘数据
	 * @param object 数据对象
	 */
	protected void parseFirstOdds(OkoooYp yp, JSONObject object)
	{
		for (String key2 : object.keySet())
		{
			Object value = object.get(key2);
			if("BoundaryCnName".equalsIgnoreCase(key2))
			{
				String handicap = OddsUtil.formatHandicapName(value.toString());
				yp.setFirsthandicap(handicap);
			}
			else if("home".equalsIgnoreCase(key2))
			{
				yp.setFirstwinodds(NumberUtil.parseFloat(value.toString()));
			}
			else if("away".equalsIgnoreCase(key2))
			{
				yp.setFirstloseodds(NumberUtil.parseFloat(value.toString()));
			}
		}
	}
	
	/**
	 * 解析凯利数据
	 * @param yp 亚盘数据
	 * @param object 数据对象
	 */
	protected void parseKelly(OkoooYp yp, JSONObject object)
	{
		for (String key : object.keySet())
		{
			Object value = object.get(key);
			if("home".equalsIgnoreCase(key))
			{
				yp.setWinkelly(NumberUtil.parseFloat(value.toString()));
			}
			else if("away".equalsIgnoreCase(key))
			{
				yp.setLosekelly(NumberUtil.parseFloat(value.toString()));
			}
		}
	}
	
	/**
	 * 解析概率值数据
	 * @param yp 亚盘数据
	 * @param object 数据对象
	 */
	protected void parseProb(OkoooYp yp, JSONObject object)
	{
		for (String key : object.keySet())
		{
			Object value = object.get(key);
			if("home".equalsIgnoreCase(key))
			{
				yp.setWinprob(NumberUtil.parseFloat(value.toString()));
			}
			else if("away".equalsIgnoreCase(key))
			{
				yp.setLoseprob(NumberUtil.parseFloat(value.toString()));
			}
		}
	}
	
	/**
	 * 解析得到JSON字符串
	 * @param text 输入的字符串
	 * @return 正确的结果
	 */
	protected String getDataStr(String text)
	{
		String[] strings = text.split("\n");
		for (String string : strings)
		{
			if(StringUtils.isNotEmpty(string))
			{
				string = string.trim();
				if(string.startsWith("var data_str"))
				{
					string = string.replace("var data_str = '", "");
					string = string.replace("';", "");
					return string;
				}
			}
		}
				
		return "";
	}

	public List<OkoooYp> getYps()
	{
		return yps;
	}

	public void setYps(List<OkoooYp> yps)
	{
		this.yps = yps;
	}

	public Date getCurrentTime()
	{
		return currentTime;
	}

	public void setCurrentTime(Date currentTime)
	{
		this.currentTime = currentTime;
	}

	public String getMid()
	{
		return mid;
	}

	public void setMid(String mid)
	{
		this.mid = mid;
	}
}
