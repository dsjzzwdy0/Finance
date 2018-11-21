package com.loris.soccer.web.downloader.zgzcw.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.loris.base.util.NumberUtil;
import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.data.table.odds.Op;
import com.loris.soccer.web.downloader.zgzcw.page.OddsOpZhishuWebPage;

public class OddsOpZhishuWebPageParser extends AbstractWebPageParser
{	
	/** The Odds. */
	private List<Op> oddsList = new ArrayList<Op>();
	
	private String mid;
	private String gid;
	private String gname;

	/**
	 * 解析欧盘数据
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The WebPage is not completed or Content is null. ");
		}
		
		if(!(page instanceof OddsOpZhishuWebPage))
		{
			return false;
		}
		
		OddsOpZhishuWebPage page2 = (OddsOpZhishuWebPage)page;
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
	 * 解析欧赔数据列表
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
			parseOddsOp(element2);
		}
		return true;
	}
	
	/**
	 * 解析欧赔数据
	 * 
	 * @param element
	 */
	protected void parseOddsOp(Element element)
	{
		Op op = new Op();
		op.setGid(gid);
		op.setGname(gname);
		op.setMid(mid);
		
		Elements elements = element.select("td");
		//String time = elements.get(1).text();
		String lastime = elements.get(2).text();
		String winodds = elements.get(3).text();
		String drawodds = elements.get(4).text();
		String loseodds = elements.get(5).text();
		String winprob = elements.get(6).text();
		String loseprob = elements.get(7).text();
		String winkelly = elements.get(8).text();
		String losekelly = elements.get(9).text();
		String lossratio = elements.get(10).text();
		
		op.setLasttime(lastime);
		op.setWinodds((float)NumberUtil.parseDoubleFromString(winodds));
		op.setDrawodds((float)NumberUtil.parseDoubleFromString(drawodds));
		op.setLoseodds((float)NumberUtil.parseDoubleFromString(loseodds));
		op.setWinprob(NumberUtil.parseFloat(winprob));
		op.setLoseprob(NumberUtil.parseFloat(loseprob));
		op.setWinkelly(NumberUtil.parseFloat(winkelly));
		op.setLosekelly(NumberUtil.parseFloat(losekelly));
		op.setLossratio(NumberUtil.parseFloat(lossratio));
		op.setDrawprob(NumberUtil.setFloatScale(2, 100.0f - op.getWinprob() - op.getLoseprob()));
		op.setDrawkelly(NumberUtil.setFloatScale(2, op.getDrawodds() * op.getDrawprob() / 100.0f));
		op.setOddstype(SoccerConstants.ODDS_TYPE_OP);
		
		oddsList.add(op);
	}
	
	/**
	 * Get the Odds List.
	 * 
	 * @return
	 */
	public List<Op> getOddsList()
	{
		return oddsList;
	}
}
