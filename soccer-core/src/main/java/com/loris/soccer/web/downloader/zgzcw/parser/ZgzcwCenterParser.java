package com.loris.soccer.web.downloader.zgzcw.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.bean.data.table.League;

public class ZgzcwCenterParser extends AbstractWebPageParser
{
	/** The leagues. */
	protected List<League> leagues = new ArrayList<League>();
	
	/**
	 * Get the leagues.
	 * 
	 * @return
	 */
	public List<League> getLeagues()
	{
		return leagues;
	}
	
	/**
	 * parse the web page. 
	 * 
	 * @return
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The WebPage is not completed or Content is null. ");
		}

		Document doc = parseHtml(page);
		Elements gamesContentMatcheses = doc.select(".liansai .wrapper .mainbottom .gamesContent");

		int n = gamesContentMatcheses.size();
		System.out.println("Get element size = " + n);

		for (Element element : gamesContentMatcheses)
		{
			Element element2 = element.select(".mbl").first();
			{
				parseCountryLeagues(element2);
				/*Element contType = element2.select("span").first();
				
				if (contType != null)
				{
					System.out.println("洲赛事： " + contType.text());
				}
				else
				{
					break;
				}

				Elements nationElements = element2.select(".ls > .lslogo");
				System.out.println("Logo size: " + nationElements.size());
				for (Element element3 : nationElements)
				{
					Element nationType = element3.select(".lstitle").first();
					if (nationType != null)
					{
						System.out.println("国家赛事： " + nationType.text());
					}
					else
					{
						break;
					}

					Elements lianTypes = element3.select("a");
					for (Element element4 : lianTypes)
					{
						System.out.print(element4.attr("href"));
						System.out.println(" " + element4.text());
					}

				}*/
			}

			Element element3 = element.select(".mbr").first();
			{
				parseCupLeagues(element3);
				/*Element contType = element3.select("span").first();
				if (contType != null)
				{
					System.out.println("洲赛事： " + contType.text());
				}
				else
				{
					break;
				}

				Elements matchElements = element3.select(".bs a");
				System.out.println("Logo size: " + matchElements.size());
				for (Element element4 : matchElements)
				{
					System.out.print(element4.attr("href"));
					System.out.println(" " + element4.text());
				}*/
			}

		}
		return true;
	}
	
	/**
	 * 解析洲际杯赛内容
	 * 
	 * @param element
	 */
	protected void parseCupLeagues(Element element)
	{
		String continent;
		String url;
		String name;
		Element contType = element.select("span").first();
		continent = contType.text();

		Elements matchElements = element.select(".bs a");
		//System.out.println("Logo size: " + matchElements.size());
		for (Element element4 : matchElements)
		{
			url = element4.attr("href");
			name = element4.text();
			
			addLeague(continent, "", name, url);
			//System.out.print(element4.attr("href"));
			//System.out.println(" " + element4.text());
		}
	}
	
	/**
	 * 解析国内赛事内容
	 * 
	 * @param element
	 */
	protected void parseCountryLeagues(Element element)
	{
		String continent;
		String country;
		String url;
		String name;
		Element contType = element.select("span").first();
		continent = contType.text();
		
		Elements nationElements = element.select(".ls > .lslogo");
		for (Element element3 : nationElements)
		{
			Element nationType = element3.select(".lstitle").first();
			country = nationType.text();
			
						
			Elements lianTypes = element3.select("a");
			for (Element element4 : lianTypes)
			{
				url = element4.attr("href");
				name = element4.text();
				
				addLeague(continent, country, name, url);
				//System.out.print(element4.attr("href"));
				//System.out.println(" " + element4.text());
			}
		}
	}
	
	/**
	 * 
	 * @param continent
	 * @param country
	 * @param name
	 * @param url
	 */
	protected void addLeague(String continent, String country, String name, String url)
	{
		League league = new League();
		if(!parseLeagueType(league, url))
		{
			return;
		}
		league.setContinent(continent);
		league.setCountry(country);
		league.setName(name);
		league.setIntroduction("");
		
		leagues.add(league);
	}
	
	
	
	/**
	 * "/soccer/league/160"
	 * @param url
	 * @return
	 */
	protected boolean parseLeagueType(League league, String url)
	{
		String[] values = url.split(RITHG_SLASH.pattern());
		if(values == null || values.length < 3)
		{
			return false;
		}
		
		if(values.length == 4)
		{
			league.setType(values[2]);
			league.setLid(values[3]);
		}
		else
		{
			league.setType(values[1]);
			league.setLid(values[2]);
		}
		
		//System.out.println(url + " -> " + values[1] + ": " +values[2] + ": " + values[3]);
		
		return true;
	}

}
