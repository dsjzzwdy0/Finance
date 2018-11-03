package com.loris.stock.web.downloader.sina.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.loris.base.bean.web.WebPage;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.stock.bean.data.table.HolderCountInfo;
import com.loris.stock.bean.data.table.StockInfo;
import com.loris.stock.bean.model.HolderCountList;
import com.loris.stock.web.page.StockBaseInfoWebPage;

public class StockHolderCountParser extends AbstractWebPageParser
{
	/** holder count list. */
	private HolderCountList list;
	
	/**
	 * Get the holder count list.
	 * 
	 * @return
	 */
	public HolderCountList getHolderCountList()
	{
		return list;
	}
	
	/**
	 * Get the stock info.
	 * @return
	 */
	public StockInfo getStockInfo()
	{
		return list;
	}
	
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
		if (element == null)
		{
			return false;
		}

		list = new HolderCountList();
		list.setSymbol(page.getSymbol());

		Elements elements = element.select("tr");
		int size = elements.size();
		for (int i = 0; i < size; i++)
		{
			if (i == 0)
			{
				continue;
			}

			Element element2 = elements.get(i);
			parseHolderCountInfo(list, element2);
		}

		return true;
	}

	/**
	 * Parse the industry.
	 * 
	 * @param industry
	 * @param element
	 */
	protected void parseHolderCountInfo(HolderCountList countlist, Element element)
	{
		Elements els2 = element.select("td");
		int size = els2.size();
		if (size != 5)
			return;

		HolderCountInfo info = new HolderCountInfo();
		info.setSymbol(countlist.getSymbol());
		info.setPubtime(els2.get(0).text());
		info.setTotalcount(NumberUtil.parseDouble(els2.get(1).text()));
		info.setPershares(NumberUtil.parseDouble(els2.get(2).text()));
		info.setTotalshares(NumberUtil.parseDouble(els2.get(3).text()));
		info.setCircshares(NumberUtil.parseDouble(els2.get(4).text()));
		countlist.addHolderCountInfo(info);
	}

}
