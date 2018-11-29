package com.loris.soccer.web.downloader.okooo.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.DateUtil;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.okooo.OkoooOp;
import com.loris.soccer.web.downloader.okooo.OkoooUtil;
import com.loris.soccer.web.downloader.okooo.page.OkoooRequestHeaderWebPage;
import com.loris.soccer.web.downloader.okooo.page.OkoooWebPage;

public class OddsOpChildParser extends AbstractWebPageParser
{
	private static Logger logger = Logger.getLogger(OddsOpChildParser.class);
	
	/** 比赛时间 */
	protected Date currentTime;
	
	/** 比赛编号 */
	protected String mid;
	
	/** 赔率类型 */
	private String oddstype;
	
	/** 欧赔数据 */
	protected List<OkoooOp> ops = new ArrayList<>();
	
	/** 公司的数量 */
	protected int corpNum = 0;
	
	/**
	 * Create a new instance of OddsOpChildParser
	 */
	public OddsOpChildParser()
	{
		this.oddstype = SoccerConstants.ODDS_TYPE_OP;
	}
	
	/**
	 * 解析数据
	 * @param page 数据页面
	 * @return 解析成功与否的标志
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		if(!(page instanceof OkoooWebPage) && !(page instanceof OkoooRequestHeaderWebPage))
		{
			return false;
		}
		
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The Okooo JcPageParser is not completed or Content is null. ");
		}
		
		Document doc = parseHtml(page);
		if(doc == null)
		{
			return false;
		}
		Element scriptEl = doc.selectFirst("script");
		if(scriptEl == null || StringUtils.isEmpty(scriptEl.data()))
		{
			logger.info("The script value is null.");
			return false;
		}
		
		String content = scriptEl.data();
		String dataStr = OkoooUtil.getDataStr(content, ";|\n", "var data_str");
		//logger.info(dataStr);
		if(StringUtils.isNotEmpty(dataStr))
		{
			parseJson(dataStr);
		}
		
		String staticStr = OkoooUtil.getDataStr(content, ";|\n", "var static_str");
		if(StringUtils.isNotEmpty(staticStr))
		{
			parseStaticInfo(staticStr);
		}
		//logger.info("StatisticStr: " + staticStr);
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
				parseOddsOp((JSONObject)object);
			}
		}
	}
	
	/**
	 * 解析亚盘记录数据
	 * @param object
	 */
	protected void parseOddsOp(JSONObject object)
	{
		OkoooOp yp = new OkoooOp();
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
		ops.add(yp);
	}
	
	/**
	 * 解析最新的亚盘数据
	 * @param op 亚盘记录
	 * @param object 数据对象
	 */
	protected void parseLastOdds(OkoooOp op, JSONObject object)
	{
		for (String key : object.keySet())
		{
			Object value = object.get(key);
			if("draw".equalsIgnoreCase(key))
			{
				op.setDrawodds(NumberUtil.parseFloat(value.toString()));
			}
			else if("home".equalsIgnoreCase(key))
			{
				op.setWinodds(NumberUtil.parseFloat(value.toString()));
			}
			else if("away".equalsIgnoreCase(key))
			{
				op.setLoseodds(NumberUtil.parseFloat(value.toString()));
			}
		}
	}
	
	/**
	 * 解析初始亚盘数据
	 * @param op 亚盘数据
	 * @param object 数据对象
	 */
	protected void parseFirstOdds(OkoooOp op, JSONObject object)
	{
		for (String key : object.keySet())
		{
			Object value = object.get(key);
			if("draw".equalsIgnoreCase(key))
			{
				op.setFirstdrawodds(NumberUtil.parseFloat(value.toString()));
			}
			else if("home".equalsIgnoreCase(key))
			{
				op.setFirstwinodds(NumberUtil.parseFloat(value.toString()));
			}
			else if("away".equalsIgnoreCase(key))
			{
				op.setFirstloseodds(NumberUtil.parseFloat(value.toString()));
			}
		}
	}
	
	/**
	 * 解析凯利数据
	 * @param yp 亚盘数据
	 * @param object 数据对象
	 */
	protected void parseKelly(OkoooOp yp, JSONObject object)
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
	protected void parseProb(OkoooOp yp, JSONObject object)
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
	 * 解析统计数据
	 * @param staticStr
	 */
	protected void parseStaticInfo(String staticStr)
	{
		JSONObject object = JSON.parseObject(staticStr);
		if(object != null)
		{
			this.corpNum = NumberUtil.parseInt(object.get("count"));
		}
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

	public String getOddstype()
	{
		return oddstype;
	}

	public void setOddstype(String oddstype)
	{
		this.oddstype = oddstype;
	}

	public List<OkoooOp> getOps()
	{
		return ops;
	}

	public int getCorpNum()
	{
		return corpNum;
	}
}
