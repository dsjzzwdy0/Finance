package com.loris.soccer.web.downloader.okooo.parser;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.DateUtil;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.data.table.lottery.Corporate;
import com.loris.soccer.bean.okooo.OkoooYp;
import com.loris.soccer.web.downloader.okooo.OkoooPageCreator;
import com.loris.soccer.web.downloader.okooo.page.OkoooWebPage;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwSoccerDownloader;

public class OddsYpPageParser extends AbstractWebPageParser
{
	private static Logger logger = Logger.getLogger(OddsYpPageParser.class);
	
	/** 亚洲盘口 */
	List<OkoooYp> yps = new ArrayList<>();
	
	/** 赔率公司 */
	List<Corporate> corps = new ArrayList<>();
	
	/** 赔率公司类型*/
	static final String CORP_TYPE = SoccerConstants.ODDS_TYPE_YP;

	private String mid;
	private String oddstype;
	private Date matchTime;
	
	
	public OddsYpPageParser()
	{
		this.oddstype = CORP_TYPE;
	}
	

	/**
	 * 解析网页信息数据
	 * @param page 网页数据
	 * @return 解析是否成功的标志
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The WebPage is not completed or Content is null. ");
		}

		if (!(page instanceof OkoooWebPage))
		{
			return false;
		}
		
		if(!(OkoooPageCreator.PAGE_TYPES[2].equalsIgnoreCase(page.getType())))
		{
			logger.info("The page " + page.toString() + " is not the '" + OkoooPageCreator.PAGE_TYPES[4] + "'");
			return false;
		}
		
		//网页数据解析
		OkoooWebPage page2 = (OkoooWebPage)page;
		mid = page2.getMid();
		
		Document document = parseHtml(page);
		Element element = document.selectFirst(".wrap .container_wrapper .noBberBottom  tbody");
		if(element == null)
		{
			logger.info("The root element is not exist, there are no data, parse error.");
			return false;
		}
		//解析数据
		return (parseOddsList(element) >= 1);
	}
	
	/**
	 * 解析列表数据
	 * @param element 元素
	 * @return 记录的个数
	 */
	protected int parseOddsList(Element element)
	{
		Elements elements = element.select("tr");
		for (Element element2 : elements)
		{
			parseOddsRecord(element2);
		}
		return yps.size();
	}
	
	/**
	 * 解析亚盘数据记录
	 * @param element 元素
	 */
	protected void parseOddsRecord(Element element)
	{
		Elements elements = element.children();
		if(elements.size() < 13)
		{
			//记录数据不正确
			return;
		}
		
		String gid;
		String ord;
		String gname;
		String firsttime;
		String firstwinodds;
		String firsthandicap;
		String firstloseodds;
		
		String time;
		String winodds;
		String handicap;
		String loseodds;
		
		String winkelly;
		String losekelly;
		String lossratio;
		
		Date d;
		
		long t = 0;
		gid = elements.get(0).selectFirst("input").attr("value");
		ord = elements.get(0).selectFirst("input").text();
		gname = elements.get(1).text();
		firsttime = elements.get(2).attr("title");
		firstwinodds = elements.get(2).text();
		firsthandicap = elements.get(3).text();
		firstloseodds = elements.get(4).text();
		if(StringUtils.isNotEmpty(firsttime))
		{
			t = getTime(firsttime);
			d = DateUtil.add(matchTime, -t);
			firsttime = DateUtil.DATE_TIME_FORMAT.format(d);
		}
		
		winodds = elements.get(5).text();
		handicap = elements.get(6).text();
		loseodds = elements.get(7).text();
		time = elements.get(8).attr("attr");
		if(StringUtils.isNotEmpty(time))
		{
			t = getTime(time);
			d = DateUtil.add(matchTime, -t);
			time = DateUtil.DATE_TIME_FORMAT.format(d);
		}
		
		winkelly = elements.get(10).text();
		losekelly = elements.get(11).text();
		lossratio = elements.get(12).text();
		
		OkoooYp yp = new OkoooYp();
		yp.setMid(mid);
		yp.setOrdinary(ord);
		yp.setOddstype(oddstype);
		yp.setGid(gid);
		yp.setGname(gname);
		yp.setFirsttime(firsttime);
		yp.setFirstwinodds(NumberUtil.parseFloatFromString(firstwinodds));
		yp.setFirsthandicap(firsthandicap);
		yp.setFirstloseodds(NumberUtil.parseFloatFromString(firstloseodds));
		
		yp.setLasttime(time);
		yp.setHandicap(handicap);
		yp.setWinodds(NumberUtil.parseFloatFromString(winodds));
		yp.setHandicap(handicap);
		yp.setLoseodds(NumberUtil.parseFloatFromString(loseodds));
		
		yp.setWinkelly(NumberUtil.parseFloatFromString(winkelly));
		yp.setLosekelly(NumberUtil.parseFloatFromString(losekelly));
		yp.setLossratio(NumberUtil.parseFloatFromString(lossratio));
		
		yps.add(yp);
		
		Corporate corporate = new Corporate();
		corporate.setGid(gid);
		corporate.setName(gname);
		corporate.setType(CORP_TYPE);
		corporate.setSource(ZgzcwSoccerDownloader.SOURCE_ZGZCW);
		corps.add(corporate);
	}
	
	/**
	 * 处理开出的赔率的时间
	 * @param time 时间字符串
	 * @return 时间毫秒数
	 */
	/**
	 * 解析时间，其格式为: 开赔时间：赛前61时27分
	 *  
	 * @param time 时间值
	 * @return 整型时间
	 */
	protected long getTime(String time)
	{
		int[] ts = NumberUtil.parseAllIntegerFromString(time);
		if(ts == null || ts.length < 2)
		{
			return 0;
		}
		else
		{
			long t = ts[0] * 3600 + ts[1] * 60;
			return t * 1000;
		}
	}
	

	public List<OkoooYp> getYps()
	{
		return yps;
	}

	public void setYps(List<OkoooYp> yps)
	{
		this.yps = yps;
	}

	public List<Corporate> getCorps()
	{
		return corps;
	}

	public void setCorps(List<Corporate> corps)
	{
		this.corps = corps;
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

	public Date getMatchtime()
	{
		return matchTime;
	}

	public void setMatchtime(Date matchtime)
	{
		this.matchTime = matchtime;
	}
}
