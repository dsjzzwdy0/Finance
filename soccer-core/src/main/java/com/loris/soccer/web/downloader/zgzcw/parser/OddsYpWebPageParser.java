package com.loris.soccer.web.downloader.zgzcw.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.DateUtil;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.data.table.Corporate;
import com.loris.soccer.bean.data.table.Yp;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwSoccerDownloader;
import com.loris.soccer.web.downloader.zgzcw.page.OddsYpWebPage;

/**
 * 解析亚盘数据
 * 
 * @author jiean
 *
 */
public class OddsYpWebPageParser extends AbstractWebPageParser
{
	protected static String dataAttr = "data";
	protected static String compIdAttr = "cid";
	
	/** 亚洲盘口 */
	List<Yp> yps = new ArrayList<>();
	
	/** 赔率公司 */
	List<Corporate> corps = new ArrayList<>();
	
	/** 赔率公司类型*/
	static final String CORP_TYPE = SoccerConstants.ODDS_TYPE_YP;

	private String mid;
	private String oddstype;
	
	protected Date matchTime;

	/**
	 * 解析亚盘信息页面
	 * @param page.
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The WebPage is not completed or Content is null. ");
		}

		if (!(page instanceof OddsYpWebPage))
		{
			return false;
		}

		OddsYpWebPage page2 = (OddsYpWebPage) page;
		mid = page2.getMid();
		oddstype = "yp";

		// 解析数据
		Document document = parseHtml(page2);
		Element element = document.select(".main #data-body table tbody").first();
		if(element == null)
		{
			return false;
		}
		parseYpList(element);

		return true;
	}
	
	/**
	 * 解析亚盘数据
	 * 
	 * @param element
	 */
	protected void parseYpList(Element element)
	{
		Elements elements = element.select("tr");
		//System.out.println("Odds records is " + elements.size());
		for (Element element2 : elements)
		{
			parseYp(element2);
		}
	}

	/**
	 * Parse the value.
	 * 
	 * @param element
	 */
	public void parseYp(Element element)
	{
		Yp yp = new Yp();
		
		String first = element.attr("firsttime");
		String last = element.attr("lasttime");
		yp.setMid(mid);
		yp.setOddstype(oddstype);
		
		yp.setFirsttime(first);
		yp.setLasttime(last);
		
		Elements elements = element.select("td");
		int size = elements.size();
	
		if(size >= 14)
		{
			String ord = elements.get(0).selectFirst("label").text();			
			String name = elements.get(1).text();
			float firstwinyp = NumberUtil.parseFloat(elements.get(2).attr(dataAttr));			
			String firsthandicap = elements.get(3).attr(dataAttr);			
			float firstloseyp = NumberUtil.parseFloat(elements.get(4).attr(dataAttr));			
			float lastwinyp = NumberUtil.parseFloat(elements.get(5).attr(dataAttr));
			String compid = elements.get(5).attr(compIdAttr);
			String lasthandicap = elements.get(6).attr(dataAttr);
			float lastloseyp = NumberUtil.parseFloat(elements.get(7).attr(dataAttr));
			String updatetime = elements.get(8).selectFirst("em").attr("title");
			float homeprob = NumberUtil.parseFloat(elements.get(9).attr(dataAttr));
			float guestprob = NumberUtil.parseFloat(elements.get(10).attr(dataAttr));
			float homekelly = NumberUtil.parseFloat(elements.get(11).attr(dataAttr));
			float guestkelly = NumberUtil.parseFloat(elements.get(12).attr(dataAttr));
			float lossratio = NumberUtil.parseFloat(elements.get(13).attr(dataAttr));
			
			
			yp.setOrdinary(ord);
			yp.setGid(compid);
			yp.setGname(name);
			yp.setFirstwinodds(firstwinyp);
			yp.setFirsthandicap(firsthandicap);
			yp.setFirstloseodds(firstloseyp);
			yp.setWinodds(lastwinyp);
			
			if(StringUtils.isNotEmpty(lasthandicap))
			{
				lasthandicap = formatHandicap(lasthandicap);
			}			
			yp.setHandicap(lasthandicap);
			yp.setLoseodds(lastloseyp);
			yp.setWinprob(homeprob);
			yp.setLoseprob(guestprob);
			yp.setWinkelly(homekelly);
			yp.setLosekelly(guestkelly);
			yp.setLossratio(lossratio);
			
			if(StringUtils.isNotEmpty(updatetime) && matchTime != null)
			{
				long t = getTime(updatetime);
				Date d = DateUtil.add(matchTime, -t);
				String time = DateUtil.DATE_TIME_FORMAT.format(d);
				yp.setLasttime(time);
			}		
			
			yps.add(yp);
			
			Corporate corporate = new Corporate();
			corporate.setGid(compid);
			corporate.setName(name);
			corporate.setType(CORP_TYPE);
			corporate.setSource(ZgzcwSoccerDownloader.SOURCE_ZGZCW);
			corps.add(corporate);
		}	
	}
	
	/**
	 * 标准化亚盘让球数据
	 * @param handicap
	 * @return
	 */
	protected String formatHandicap(String handicap)
	{
		handicap = handicap.replace("↓|↑|→", "");
		//handicap = handicap.replaceAll("↑", "");
		return handicap;
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
		if(ts == null)
		{
			return 0;
		}
		else if(ts.length == 1)
		{
			long t = ts[0] * 60;
			return t * 1000;
		}
		else
		{
			long t = ts[0] * 3600 + ts[1] * 60;
			return t * 1000;
		}
	}

	/**
	 * Get yp list.
	 * 
	 * @return
	 */
	public List<Yp> getOddsList()
	{
		return yps;
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

	public List<Corporate> getCorps()
	{
		return corps;
	}

	public void setCorps(List<Corporate> corps)
	{
		this.corps = corps;
	}

	public Date getMatchTime()
	{
		return matchTime;
	}

	public void setMatchTime(Date matchTime)
	{
		this.matchTime = matchTime;
	}
}
