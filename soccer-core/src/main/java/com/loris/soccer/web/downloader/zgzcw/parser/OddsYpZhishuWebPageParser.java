package com.loris.soccer.web.downloader.zgzcw.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.loris.base.bean.web.WebPage;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.data.table.odds.Yp;
import com.loris.soccer.web.downloader.util.ParserUtil;
import com.loris.soccer.web.downloader.zgzcw.page.OddsYpZhishuWebPage;

/**
 * 解析亚盘详细列表数据
 * 
 * @author jiean
 *
 */
public class OddsYpZhishuWebPageParser extends AbstractWebPageParser
{
	private List<Yp> oddslist = new ArrayList<>();
	
	private String mid;
	private String gid;
	private String gname;
	
	

	/**
	 * 解析亚盘数据
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The WebPage is not completed or Content is null. ");
		}
		
		if(!(page instanceof OddsYpZhishuWebPage))
		{
			return false;
		}
		
		OddsYpZhishuWebPage page2 = (OddsYpZhishuWebPage)page;
		mid = page2.getMid();
		gid = page2.getGid();
		gname = page2.getGname();
		
		Document document = parseHtml(page);
		Element element = document.selectFirst(".dxzkt .marb10 .dxzkt-tab");
		
		if(element == null)
		{
			return false;
		}
		
		return parseOddsList(element);
	}
	
	/**
	 * 解析亚盘数据列表
	 * 
	 * @param element 元素
	 * @return 解析是否成功标志
	 */
	protected boolean parseOddsList(Element element)
	{
		Elements elements = element.select("tr");
		if(elements.size() < 2)
		{
			return false;
		}
		for (int i = 2; i < elements.size(); i ++)
		{
			Element element2 = elements.get(i);
			parseOddsYp(element2);
		}
		return true;
	}
	
	/**
	 * 解析欧赔数据
	 * 
	 * @param element
	 */
	protected void parseOddsYp(Element element)
	{
		Yp yp = new Yp();
		yp.setGid(gid);
		yp.setGname(gname);
		yp.setMid(mid);
		
		Elements elements = element.select("td");
		//String time = elements.get(1).text();
		String lastime = elements.get(2).text();
		String winodds = elements.get(3).text();
		String handicap = elements.get(4).text();
		String loseodds = elements.get(5).text();
		String winprob = elements.get(6).text();
		String loseprob = elements.get(7).text();
		String winkelly = elements.get(8).text();
		String losekelly = elements.get(9).text();
		String lossratio = elements.get(10).text();		
		handicap = ParserUtil.formatHandicap(handicap);
		
		yp.setWinodds((float)NumberUtil.parseDoubleFromString(winodds));
		yp.setLoseodds((float)NumberUtil.parseDoubleFromString(loseodds));
		yp.setHandicap(handicap);
		yp.setLasttime(lastime);
		yp.setWinprob(NumberUtil.parseFloat(winprob));
		yp.setLoseprob(NumberUtil.parseFloat(loseprob));
		yp.setWinkelly(NumberUtil.parseFloat(winkelly));
		yp.setLosekelly(NumberUtil.parseFloat(losekelly));
		yp.setLossratio(NumberUtil.parseFloat(lossratio));
		yp.setOddstype(SoccerConstants.ODDS_TYPE_YP);
		
		oddslist.add(yp);
	}
		
	/**
	 * Get the OddsOpList.
	 * @return
	 */
	public List<Yp> getOddsList()
	{
		return oddslist;
	}
}
