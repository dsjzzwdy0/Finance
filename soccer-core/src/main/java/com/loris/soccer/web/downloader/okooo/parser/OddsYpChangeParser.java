package com.loris.soccer.web.downloader.okooo.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.loris.base.bean.web.WebPage;
import com.loris.base.util.DateUtil;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.analysis.util.OddsUtil;
import com.loris.soccer.bean.okooo.OkoooYp;
import com.loris.soccer.web.downloader.okooo.OkoooPageCreator;
import com.loris.soccer.web.downloader.okooo.page.OkoooWebPage;

/**
 * 澳客网亚盘变化数据下载
 * 
 * @author jiean
 *
 */
public class OddsYpChangeParser extends AbstractWebPageParser
{	
	private static Logger logger = Logger.getLogger(OddsYpChangeParser.class);
	
	/** The OddsList. */
	private List<OkoooYp> oddslist = new ArrayList<>();
	
	private String mid;
	private String gid;
	private String gname;
	
	/**
	 * 解析页面数据
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The WebPage is not completed or Content is null. ");
		}
		
		if(!(page instanceof OkoooWebPage))
		{
			return false;
		}
		
		if(!(OkoooPageCreator.PAGE_TYPES[4].equalsIgnoreCase(page.getType())))
		{
			logger.info("The page " + page.toString() + " is not the '" + OkoooPageCreator.PAGE_TYPES[4] + "'");
			return false;
		}
		
		OkoooWebPage page2 = (OkoooWebPage)page;
		mid = page2.getMid();
		gid = page2.getGid();
		
		Document document = parseHtml(page);
		Element element = document.selectFirst(".wrap table tbody");
		if(element == null)
		{
			logger.info("The root element is not exist, there are no data, parse error.");
			return false;
		}
		
		//解析数据表		
		return (parseOddsList(element) > 0);
	}
	
	/**
	 * 解析数据列表，详细变化的数据
	 * @param element 元素
	 * @return 记录数
	 */
	protected int parseOddsList(Element element)
	{
		Elements elements = element.select("tr");		
		for (Element element2 : elements)
		{
			String type = element2.attr("class");
			if("tableh".equalsIgnoreCase(type))
			{
				//表头
				continue;
			}
			else if("titlebg".equalsIgnoreCase(type))
			{
				//标题
				continue;
			}
				
			parseOddsRecord(element2);
		}
		return oddslist.size();
	}
	
	/**
	 * 解析数据记录
	 * @param element 数据记录
	 */
	protected void parseOddsRecord(Element element)
	{
		Elements elements = element.children();
		if(elements.size() < 5)
		{
			return;
		}
		String time = elements.get(0).text();
		String winodds = elements.get(2).text();
		String handicap = elements.get(3).text();
		String loseodds = elements.get(4).text();
		
		if(time.endsWith("(新)"))
		{
			time = time.replace("(新)", "");
		}
		else if(time.endsWith("(初)"))
		{
			time = time.replace("(初)", "");
		}

		OkoooYp yp = new OkoooYp();
		yp.setGid(gid);
		yp.setGname(gname);
		yp.setMid(mid);
		
		yp.setWinodds(NumberUtil.parseFloatFromString(winodds));
		yp.setLoseodds(NumberUtil.parseFloatFromString(loseodds));
		yp.setHandicap(OddsUtil.formatHandicapName(handicap));
		
		Date date = DateUtil.tryToParseDate(time);
		if(date != null)
		{
			yp.setLasttime(DateUtil.DATE_TIME_FORMAT.format(date));
		}
		
		oddslist.add(yp);
	}

	public List<OkoooYp> getOddslist()
	{
		return oddslist;
	}

	public void setOddslist(List<OkoooYp> oddslist)
	{
		this.oddslist = oddslist;
	}

	public String getMid()
	{
		return mid;
	}

	public void setMid(String mid)
	{
		this.mid = mid;
	}

	public String getGid()
	{
		return gid;
	}

	public void setGid(String gid)
	{
		this.gid = gid;
	}

	public String getGname()
	{
		return gname;
	}

	public void setGname(String gname)
	{
		this.gname = gname;
	}
}
