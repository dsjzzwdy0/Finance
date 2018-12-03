package com.loris.soccer.web.downloader.okooo.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.bean.data.table.CountryLogo;
import com.loris.soccer.bean.data.table.League;


/**
 * 联赛界面解析
 * 
 * @author jiean
 *
 */
public class SoccerPageParser extends AbstractWebPageParser
{
	/** 联赛列表*/
	List<League> leagues = new ArrayList<>();
	
	/** 国家赛事图标 */
	List<CountryLogo> logos = new ArrayList<>();
	
	/** 洲际类型 */
	protected String[] continents = new String[]{
		"", "欧洲赛事", "美洲赛事", "亚洲赛事", "洲际赛事"
	};
	
	/** Type of league */
	protected String type = "league";
	
	/**
	 * 获得联赛数据
	 * 
	 * @return 联赛数据
	 */
	public List<League> getLeagues()
	{
		return leagues;
	}
	
	/**
	 * 获得国家比赛的图标数据
	 * 
	 * @return 图标列表
	 */
	public List<CountryLogo> getLogos()
	{
		return logos;
	}

	/**
	 * 解析联赛数据
	 * 
	 * @param page 数据页面
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The Okooo LeaguePageParser is not completed or Content is null. ");
		}
		
		if(!"soccer".equalsIgnoreCase(page.getType()))
		{
			throw new IllegalArgumentException("The page type is not league.");
		}
		
		Document doc = parseHtml(page);
		if(doc == null)
		{
			return false;
		}
		
		return parseLeagues(doc);
	}
	
	/**
	 * 解析联赛数据
	 * 
	 * @param doc 文档结构图
	 * @return 是否解析成功的标志
	 */
	protected boolean parseLeagues(Document doc)
	{
		Element element = doc.selectFirst(".CenterWidth .LeagueInfoMapRight");
		if(element == null)
		{
			return false;
		}
		
		Elements elements = element.children();
		int size = elements.size();
		for (int i = 1; i < size; i ++)
		{
			Element el = elements.get(i);
			parseContinentLeagues(el, continents[i]);
		}
		return true;
	}

	/**
	 * 解析赛事列表信息
	 * 
	 * @param el 赛事数据
	 * @param continent 大洲
	 */
	protected void parseContinentLeagues(Element el, String continent)
	{
		Elements elements = el.children();
		for (Element element : elements)
		{
			parseContryLeagues(element, continent);
		}
	}
	
	/**
	 * 解析国家的联赛数据
	 * 
	 * @param element 数据元素
	 * @param continent 洲际类型
	 */
	protected void parseContryLeagues(Element element, String continent)
	{
		String country;
		String logo = null;
		String mainLid;
		
		Element e1 = element.selectFirst(".MatchInfoLogoName");
		country = e1.text();
		if(country != null)
		{
			country = country.trim();
		}
		e1 = element.selectFirst(".MatchInfoListLogo a");
		mainLid = e1.attr("href");
		mainLid = getLeadueId(mainLid);
		e1 = e1.selectFirst("img");
		if(e1 != null)
		{
			logo = e1.attr("src");
			
			CountryLogo logo2 = new CountryLogo();
			logo2.setCountry(country);
			logo2.setLogo(logo);
			
			logos.add(logo2);
		}

		Elements elements = element.select(".Toolbox div");
		for (Element element2 : elements)
		{
			if("clear".equalsIgnoreCase(element2.attr("class")))
			{
				continue;
			}
			String lid = element2.attr("onclick");
			String name = element2.text();
			if(StringUtils.isNotEmpty(lid))
			{
				lid = Integer.toString(NumberUtil.parseIntegerFromString(lid));
			}
			
			//名称为空，不添加数据
			if(StringUtils.isEmpty(name))
			{
				continue;
			}
			
			League league = new League();
			league.setContinent(continent);
			league.setCountry(country);
			league.setName(name);
			league.setLid(lid);
			league.setType(type);
			
			if(lid.equals(mainLid))
			{
				league.setLogo(logo);
			}
			
			leagues.add(league);
		}
	}
	
	/**
	 * Get the Lid value.
	 * 
	 * @param url
	 * @return
	 */
	protected String getLeadueId(String url)
	{
		String[] values = url.split(RITHG_SLASH.pattern());
		int size = values.length;
		String tid = values[size - 1];
		return tid;
	}
}
