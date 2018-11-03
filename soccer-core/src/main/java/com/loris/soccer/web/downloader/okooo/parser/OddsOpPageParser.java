package com.loris.soccer.web.downloader.okooo.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.web.WebPage;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.bean.data.table.lottery.Corporate;
import com.loris.soccer.bean.okooo.OkoooOp;
import com.loris.soccer.web.downloader.okooo.OkoooDownloader;

public class OddsOpPageParser extends AbstractWebPageParser
{
	/** 欧赔列表数据 */
	List<OkoooOp> ops = new ArrayList<>();
	
	/** 欧赔公司列表 */
	List<Corporate> corps = new ArrayList<>();
	
	/** 比赛编号 */
	String mid;
	
	/**
	 * 获得欧赔列表数据
	 * 
	 * @return 欧赔列表数据
	 */
	public List<OkoooOp> getOps()
	{
		return ops;
	}
	
	/**
	 * 获得欧赔公司数据
	 * 
	 * @return
	 */
	public List<Corporate> getCorps()
	{
		return corps;
	}

	/**
	 * 获得比赛编号
	 * 
	 * @return
	 */
	public String getMid()
	{
		return mid;
	}

	//设置比赛编号
	public void setMid(String mid)
	{
		this.mid = mid;
	}

	/**
	 * 解析欧赔数据
	 * 
	 * @param page 欧赔数据页面 
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The Okooo JcPageParser is not completed or Content is null. ");
		}
		
		if(!"op".equals(page.getType()))
		{
			return false;
		}
		Document doc = parseHtml(page);
		if(doc == null)
		{
			return false;
		}
		
		Elements elements = doc.select("table tr");
		int size = elements.size();
		if(size <= 2)
		{			
			return false;
		}
		
		for(int i = 2; i < size; i ++)
		{
			parseOkoooOp(elements.get(i));
		}
		
		Element element = doc.selectFirst(".table_title1 .qq");
		parseCorporates(element);		
		
		return true;
	}
	
	/**
	 * 解析欧赔实时数据
	 * 
	 * @param element 数据元素
	 */
	protected void parseOkoooOp(Element element)
	{
		Elements elements = element.select("td");
		
		//非数据表
		if(elements.size() < 12)
		{
			return;
		}
		
		String time;
		float winodds;
		float drawodds;
		float loseodds;
		float winprob;
		float drawprob;
		float loseprob;
		float winkelly;
		float drawkelly;
		float losekelly;
		float lossratio;
		boolean isfirst = false;
		boolean isend = false;
		
		time = elements.get(0).text();
		winodds = getOddsValue(elements.get(2).text());
		drawodds = getOddsValue(elements.get(3).text());
		loseodds = getOddsValue(elements.get(4).text());
		winprob = getOddsValue(elements.get(5).text());
		drawprob = getOddsValue(elements.get(6).text());
		loseprob = getOddsValue(elements.get(7).text());
		winkelly = getOddsValue(elements.get(8).text());
		drawkelly = getOddsValue(elements.get(9).text());
		losekelly = getOddsValue(elements.get(10).text());
		lossratio = getOddsValue(elements.get(11).text());
		
		if(StringUtils.isEmpty(time))
		{
			return;
		}
		
		isend = time.endsWith("(终)");
		isfirst = time.endsWith("(初)");
		if(isend || isfirst)
		{
			time = time.substring(0, time.length() - 3);
		}
		
		OkoooOp op = new OkoooOp();
		op.setMid(mid);
		op.setTime(time);
		op.setWinodds(winodds);
		op.setDrawodds(drawodds);
		op.setLoseodds(loseodds);
		op.setWinprob(winprob);
		op.setDrawprob(drawprob);
		op.setLoseprob(loseprob);
		op.setWinkelly(winkelly);
		op.setDrawkelly(drawkelly);
		op.setLosekelly(losekelly);
		op.setLossratio(lossratio);
		op.setIsend(isend);
		op.setIsfirst(isfirst);
				
		ops.add(op);
	}
	
	/**
	 * 解析获得欧赔公司列表数据
	 * 
	 * @param element
	 */
	protected void parseCorporates(Element element)
	{
		String gname;
		String gid;
		
		Elements elements = element.select("a");
		for (Element element2 : elements)
		{
			gname = element2.text();
			gid = element2.attr("href");
			if(StringUtils.isNotEmpty(gid))
			{
				gid = getLastNumberValue(gid);
				Corporate corp = new Corporate();
				corp.setGid(gid);
				corp.setName(gname);
				corp.setSource(OkoooDownloader.SOURCE_OKOOO);
				corps.add(corp);
			}
		}
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	protected float getOddsValue(String value)
	{
		return NumberUtil.parseFloatFromString(value);
	}
}
