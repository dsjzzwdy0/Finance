package com.loris.stock.web.downloader.sina.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.loris.base.bean.web.WebPage;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.stock.bean.data.table.StockInfo;
import com.loris.stock.bean.model.CompanyIndustryInfo;
import com.loris.stock.web.page.StockBaseInfoWebPage;

public class StockIndustryInfoParser extends AbstractWebPageParser
{
	private CompanyIndustryInfo industry;
	/**
	 * Parse the web page.
	 * 
	 * @param page.
	 */
	@Override
	public boolean parseWebPage(WebPage p)
	{
		if (!(p instanceof StockBaseInfoWebPage))
		{
			return false;
		}
		StockBaseInfoWebPage page = (StockBaseInfoWebPage) p;
		Document doc = parseHtml(page);
		if (doc == null)
		{
			return false;
		}
		
		Element element = doc.select(".page .list").first();
		if(element == null)
		{
			return false;
		}
		
		industry = new CompanyIndustryInfo();
		industry.setSymbol(page.getSymbol());
		
		Elements elements = element.select("tr");
		int size = elements.size();
		for(int i = 0; i < size; i ++)
		{
			if(i == 0)
			{
				continue;
			}
			
			Element element2 = elements.get(i);
			parseIndustry(industry, element2);
		}
		
		return true;
	}
	
	/**
	 * Parse the industry.
	 * 
	 * @param industry
	 * @param element
	 */
	protected void parseIndustry(CompanyIndustryInfo industry, Element element)
	{
		Elements els2 = element.select("td");
		int size = els2.size();
		if(size != 2)
			return;
		
		industry.addIndustry(els2.get(0).text(), els2.get(1).text());
	}

	public CompanyIndustryInfo getIndustry()
	{
		return industry;
	}
	
	public StockInfo getStockInfo()
	{
		return industry;
	}
}
