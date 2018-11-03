package com.loris.stock.web.downloader.sina.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.loris.base.bean.web.WebPage;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.stock.bean.data.table.CompanyChangeInfo;
import com.loris.stock.bean.data.table.CompanyInfo;
import com.loris.stock.bean.data.table.StockInfo;
import com.loris.stock.web.page.StockBaseInfoWebPage;

public class StockBaseInfoParser extends AbstractWebPageParser
{
	/** company info. */
	private CompanyInfo companyInfo;
	
	/**
	 * Get the stock info.
	 * @return
	 */
	public StockInfo getStockInfo()
	{
		return companyInfo;
	}
	
	/**
	 * parse the page.
	 * 
	 * @param page
	 */
	@Override
	public boolean parseWebPage(WebPage p)
	{
		if (!(p instanceof StockBaseInfoWebPage))
		{
			return false;
		}
		StockBaseInfoWebPage page = (StockBaseInfoWebPage) p;
		Document document = parseHtml(page);
		if (document == null)
		{
			return false;
		}
		// System.out.println("content = " + page.getContent());

		Elements elements = document.select(".page .list");
		if (elements == null || elements.size() < 1)
		{
			return false;
		}

		// System.out.println("tr size = " + size);
		companyInfo = new CompanyInfo();
		companyInfo.setSymbol(page.getSymbol());

		Element element = elements.get(0);
		parseBaseInfo(companyInfo, element);

		if (elements.size() > 1)
		{
			element = elements.get(1);
			parseChangeInfo(companyInfo, element);
		}

		return true;
	}

	/**
	 * Parse the base info.
	 * 
	 * @param company
	 * @param element
	 */
	protected void parseBaseInfo(CompanyInfo company, Element element)
	{
		Elements els = element.select("tr");
		int size = els.size();

		for (int i = 0; i < size; i++)
		{
			Element element2 = els.get(i);
			if (i == 0)
			{
				// 名称,只有一个<td />
				continue;
			}
			Elements els2 = element2.select("td");
			// System.out.println(els2);
			int size2 = els2.size();
			for (int j = 0; j < size2 / 2; j++)
			{
				Element e1 = els2.get(j * 2);
				Element e2 = els2.get(j * 2 + 1);

				parseElement(company, e1, e2);
			}
		}
	}

	/**
	 * Parse the change info.
	 * 
	 * @param company
	 * @param element
	 */
	protected void parseChangeInfo(CompanyInfo company, Element element)
	{
		Elements els = element.select("tr");
		int size = els.size();

		for (int i = 0; i < size; i++)
		{
			if (i == 0 || i == 1)
			{
				// 名称,只有一个<td />
				continue;
			}
			Element element2 = els.get(i);
			Elements els2 = element2.select("td");
			// System.out.println(els2);
			int size2 = els2.size();
			if (size2 == 5)
			{
				CompanyChangeInfo info = new CompanyChangeInfo();
				info.setSymbol(company.getSymbol());
				info.setType(els2.get(0).text());
				info.setChangetime(els2.get(1).text());
				info.setPubtime(els2.get(2).text());
				info.setPrecontent(els2.get(3).text());
				info.setDestcontent(els2.get(4).text());
				company.addChangeInfo(info);
			}
		}
	}

	/**
	 * parse the element.
	 * 
	 * @param company
	 * @param first
	 * @param second
	 */
	protected void parseElement(CompanyInfo company, Element first, Element second)
	{
		String field = first.text();
		String value = second.text();
		if (value != null)
		{
			value = value.trim();
		}

		// System.out.println(field + "=" + value);

		switch (field)
		{
		case "法定名称":
			company.setCname(value);
			break;
		case "英文名称":
			company.setEname(value);
			break;
		case "成立日期":
			company.setFoundtime(value);
			break;
		case "上市市场":
			company.setExchange(value);
			break;
		case "所属行业":
			company.setIndustry(value);
			break;
		case "同行业公司数":
			break;
		case "公司电话":
			company.setTelphone(value);
			break;
		case "公司电子邮箱":
			company.setEmail(value);
			break;
		case "公司传真":
			company.setFax(value);
			break;
		case "注册资本(万元)":
			// System.out.println("注册资本：" + value);
			company.setCapital(NumberUtil.parseInt(value));
			break;
		case "法人代表":
			company.setRepresentative(value);
			break;
		case "董事会秘书":
			company.setSecretary(value);
			break;
		case "信息披露网址":
			company.setInfopage(value);
			break;
		case "董秘电话":
			company.setSectelphone(value);
			break;
		case "董秘电子邮箱":
			company.setSecemail(value);
			break;
		case "董秘传真":
			company.setSecfax(value);
			break;
		case "公司网址":
			company.setMainpage(value);
			break;
		case "注册地址":
			company.setRegaddr(value);
			break;
		case "办公地址":
			company.setWorkaddr(value);
			break;
		case "发行价格(元)":
			company.setIpoprice(value);
			break;
		case "上市日期":
			company.setIpotime(value);
			break;
		case "主承销商":
			company.setUnderwriter(value);
			break;
		case "上市推荐人":
			company.setRefree(value);
			break;
		case "所属板块":
			company.setPlate(value);
			break;
		case "经营范围":
			company.setScopebusi(value);
			break;
		case "公司沿革":
			company.setIntroduction(value);
			break;
		default:
			break;
		}
	}

}
