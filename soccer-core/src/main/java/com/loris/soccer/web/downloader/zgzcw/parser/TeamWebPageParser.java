package com.loris.soccer.web.downloader.zgzcw.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.bean.table.Team;
import com.loris.soccer.web.downloader.zgzcw.page.TeamWebPage;

public class TeamWebPageParser extends AbstractWebPageParser
{
	/** 球队信息 */
	protected Team team;
	
	/**
	 * 解析球队基本信息
	 * 
	 * @param page 页面数据
	 * @return 返回是否解析成功
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The WebPage is not completed or Content is null. ");
		}

		if (!(page instanceof TeamWebPage))
		{
			throw new IllegalArgumentException("The WebPage is not a validate RankWebPage. ");
		}
		
		Document doc = parseHtml(page);
		Element element = doc.select(".teampage .right .star_out").first();
		if(element == null)
		{
			return false;
		}
		
		if(team == null)
		{
			team = new Team();
			team.setTid(((TeamWebPage)page).getTid());
		}
		
		//球队基本信息
		Element element1 = element.selectFirst(".star_dl");
		parseTeamBasicInfo(element1);
		
		element1 = element.selectFirst(".intro_dl .introduceDiv");
		team.setIntroduction(element1.text());
		
		return true;
	}
	
	/**
	 * 解析球队的基本信息
	 * 
	 * @param element
	 */
	protected void parseTeamBasicInfo(Element element)
	{
		Elements elements = element.children();
		for (Element element2 : elements)
		{
			if("dt".equalsIgnoreCase(element2.tagName()))
			{
				String logo = element2.selectFirst("img").attr("src");
				String name = element2.selectFirst("span").text();
				
				team.setName(name);
				team.setLogo(logo);
			}			
			else
			{
				Elements els = element2.children();
				if(els.size() < 2)
				{
					continue;
				}
				
				String key = els.get(0).text();
				String value = els.get(1).text();
				
				//System.out.println(els.get(0));
				//System.out.println(els.get(1));

				if(key == null)
				{
					continue;
				}
				
				if(key.startsWith("国家"))
				{
					team.setCountry(value);
				}
				else if(key.startsWith("球队成立"))
				{
					team.setFoundtime(value);
				}
				else if(key.startsWith("联赛"))
				{					
				}
				else if(key.startsWith("主教练"))
				{
					team.setCoach(value);
				}
				else if(key.startsWith("城市"))
				{
					team.setCity(value);
				}
				else if(key.startsWith("主队球场"))
				{
				}
				else if(key.startsWith("官网"))
				{
					team.setMainpage(value);
				}
			}
			
		}
	}

	public Team getTeam()
	{
		return team;
	}

	public void setTeam(Team team)
	{
		this.team = team;
	}
}
