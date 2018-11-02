package com.loris.soccer.web.downloader.zgzcw.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.loris.base.bean.web.WebPage;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.base.web.util.URLParser;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.data.table.lottery.Corporate;
import com.loris.soccer.bean.data.table.odds.Op;
import com.loris.soccer.web.downloader.zgzcw.ZgzcwSoccerDownloader;
import com.loris.soccer.web.downloader.zgzcw.page.OddsOpWebPage;

public class OddsOpWebPageParser extends AbstractWebPageParser
{
	/** The Odds. */
	private List<Op> oddsList = new ArrayList<Op>();
	
	/** 赔率公司 */
	private List<Corporate> corps = new ArrayList<>();
	
	/** 赔率公司 */
	final static String CORP_TYPE = SoccerConstants.ODDS_TYPE_OP;
	
	private String mid;
	private String oddstype;

	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The WebPage is not completed or Content is null. ");
		}

		if(!(page instanceof OddsOpWebPage))
		{
			return false;
		}
		
		OddsOpWebPage page2 = (OddsOpWebPage)page;
		mid = page2.getMid();
		oddstype = SoccerConstants.ODDS_TYPE_OP;
		
		//解析数据
		Document document = parseHtml(page2);
		Element element = document.select(".main #data-body table tbody").first();
		if(element == null)
		{
			return false;
		}
		parseOddsList(element);
		
		return true;
	}
	
	/**
	 * parse odds list.
	 * 
	 * @param element
	 */
	public void parseOddsList(Element element)
	{
		Elements elements = element.select("tr");
		//System.out.println("Odds records is " + elements.size());
		for (Element element2 : elements)
		{
			parseOdds(element2);
		}
	}

	/**
	 * Parse the value.
	 * 
	 * @param element
	 */
	public void parseOdds(Element element)
	{
		Op odds = new Op();
		
		String first = element.attr("firsttime");
		String last = element.attr("lasttime");
		odds.setMid(mid);
		odds.setOddstype(oddstype);
		//odds.setType(type);
		
		odds.setFirsttime(first);
		odds.setLasttime(last);
		
		Elements elements = element.select("td");
		int size = elements.size();
		//System.out.println("[tr td] size is " + size);
		
		
		//解析欧盘
		if(size == 17)
		{
			//单位
			parseGamingCompany(odds, elements.get(0), elements.get(1), elements.get(5));
			Element[] els = new Element[6];
			els[0] = elements.get(2);
			els[1] = elements.get(3);
			els[2] = elements.get(4);
			els[3] = elements.get(5);
			els[4] = elements.get(6);
			els[5] = elements.get(7);			
			parseOddsValue(odds, els, elements.get(8));      //初始赔率、最新赔率、赔率时间
			
			els[0] = elements.get(9);
			els[1] = elements.get(10);
			els[2] = elements.get(11);
			els[3] = elements.get(12);
			els[4] = elements.get(13);
			els[5] = elements.get(14);
			parseProbAndKelly(odds, els, elements.get(15));    //概率与凯利指数、赔付率
		}
		else if(size == 14)
		{
			parseGamingCompany(odds, elements.get(0), elements.get(1), elements.get(5));
			Element[] els = new Element[6];
			els[0] = elements.get(2);
			els[1] = elements.get(3);
			els[2] = elements.get(4);
			els[3] = elements.get(5);
			els[4] = elements.get(6);
			els[5] = elements.get(7);			
			parseOddsValue(odds, els, elements.get(8));      //赔率
			
			els[0] = elements.get(9);
			els[1] = elements.get(10);
			els[2] = null;
			els[3] = elements.get(11);
			els[4] = elements.get(12);
			els[5] = null;
			parseProbAndKelly(odds, els, elements.get(13));    //概率与凯利指数、赔付率

		}
		else 
		{
			//其它的情况下无法解析数据
			return;
		}	
		
		addOdds(odds);
	}
	
	/**
	 * Add the odds value. 
	 * 
	 * @param odds
	 */
	protected void addOdds(Op odds)
	{
		oddsList.add(odds);
	}
	
	/**
	 * Parse the company. 
	 * 
	 * @param odds
	 * @param name
	 * @param id
	 */
	private void parseGamingCompany(Op odds, Element idx, Element name, Element id)
	{
		String index;
		String gname;
		String gid;
		boolean isMain = false;
		
		index = idx.text();
		gname = name.text();
		isMain = isMainCompany(name);
		gid = getGid(id.select("a").first());
		
		odds.setOrdinary(index);
		odds.setGid(gid);
		odds.setIsmain(isMain);
		odds.setGname(gname);
		
		Corporate company = new Corporate();
		company.setGid(gid);
		company.setName(gname);
		company.setIsmain(isMain);
		company.setSource(ZgzcwSoccerDownloader.SOURCE_ZGZCW);
		company.setType(CORP_TYPE);
		
		corps.add(company);
	}
	
	/**
	 * Parse the odds value.
	 * 
	 * @param odds
	 * @param oddsEls
	 */
	private void parseOddsValue(Op odds, Element[] oddsEls, Element time)
	{
		float wodds, dodds, lodds;
		
		wodds = getOddsValueAsAttr(oddsEls[0]);
		dodds = getOddsValueAsAttr(oddsEls[1]);
		lodds = getOddsValueAsAttr(oddsEls[2]);
		odds.setFirstwinodds(wodds);
		odds.setFirstdrawodds(dodds);
		odds.setFirstloseodds(lodds);
		
		wodds = getOddsValueAsAttr(oddsEls[3]);
		dodds = getOddsValueAsAttr(oddsEls[4]);
		lodds = getOddsValueAsAttr(oddsEls[5]);
		
		odds.setWinodds(wodds);
		odds.setDrawodds(dodds);
		odds.setLoseodds(lodds);
	}
	
	/**
	 * Parse the probability and Kelly value.
	 * 
	 * @param odds
	 * @param oddsEls
	 * @param el
	 */
	private void parseProbAndKelly(Op odds, Element[] oddsEls, Element el)
	{
		float l1, l2, l3;
		l1 = getOddsValueAsAttr(oddsEls[0]);;
		l2 = getOddsValueAsAttr(oddsEls[1]);;
		l3 = getOddsValueAsAttr(oddsEls[2]);;
		odds.setWinprob(l1);
		odds.setDrawprob(l2);
		odds.setLoseprob(l3);
		
		l1 = getOddsValueAsAttr(oddsEls[3]);;
		l2 = getOddsValueAsAttr(oddsEls[4]);;
		l3 = getOddsValueAsAttr(oddsEls[5]);;
		
		odds.setWinkelly(l1);
		odds.setDrawkelly(l2);
		odds.setLosekelly(l3);
		
		odds.setLossratio(getOddsValueAsAttr(el));
	}
	
	/**
	 * Get the odds value.
	 * 
	 * @param el
	 * @return
	 */
	private float getOddsValueAsAttr(Element el)
	{
		if(el != null)
		{
			return NumberUtil.parseFloat(el.attr("data"));
		}
		return 0.0f;
	}
	
	/**
	 * Get the Gambling Company.
	 * 
	 * @param element
	 * @return
	 */
	private String getGid(Element element)
	{
		Element detail = element.select("a").first();
		String url = detail.attr("href");
		
		try{
			URLParser parser = URLParser.fromURL(url).compile();
			return parser.getParameter("company_id");
			//odds.setGid(parser.getParameter("company_id"));
		}
		catch(Exception e)
		{			
		}
		return "";
	}
	
	/**
	 * Check there are the hot label.
	 * 
	 * @param element
	 * @return
	 */
	private boolean isMainCompany(Element element)
	{
		Element el = element.select(".hot").first();
		return el == null ? false : true;
	}

	/**
	 * Get the odds list.
	 * @return
	 */
	public List<Op> getOddsList()
	{
		return oddsList;
	}

	
	public void setOddsList(List<Op> oddsList)
	{
		this.oddsList = oddsList;
	}

	public String getMid()
	{
		return mid;
	}

	public void setMid(String mid)
	{
		this.mid = mid;
	}

	public List<Corporate> getCorps()
	{
		return corps;
	}

	public void setCorps(List<Corporate> corps)
	{
		this.corps = corps;
	}
}
