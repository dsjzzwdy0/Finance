package com.loris.soccer.web.downloader.okooo.parser;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.DateUtil;
import com.loris.base.util.NumberUtil;
import com.loris.base.web.page.WebPage;
import com.loris.base.web.parser.AbstractWebPageParser;
import com.loris.soccer.bean.SoccerConstants;
import com.loris.soccer.bean.data.table.Match;
import com.loris.soccer.bean.okooo.OkoooOp;
import com.loris.soccer.web.downloader.okooo.page.OkoooWebPage;

/**
 * 解析欧赔主页数据页面
 * 
 * @author jiean
 *
 */
public class OddsOpPageParser extends AbstractWebPageParser
{
	/** 欧赔数据 */
	List<OkoooOp> ops = new ArrayList<>();
	
	/** 比赛信息  */
	Match match;
	
	/** 比赛编号 */
	String mid;
	
	int corpNum = 0;
	
	String homename;
	String clientname;
	Date matchTime;
	
	public List<OkoooOp> getOps()
	{
		return ops;
	}

	public void setOps(List<OkoooOp> ops)
	{
		this.ops = ops;
	}

	public Match getMatch()
	{
		return match;
	}

	public void setMatch(Match match)
	{
		this.match = match;
	}

	public String getMid()
	{
		return mid;
	}

	public void setMid(String mid)
	{
		this.mid = mid;
	}

	public String getHomename()
	{
		return homename;
	}

	public void setHomename(String homename)
	{
		this.homename = homename;
	}

	public String getClientname()
	{
		return clientname;
	}

	public void setClientname(String clientname)
	{
		this.clientname = clientname;
	}

	public int getCorpNum()
	{
		return corpNum;
	}

	public void setCorpNum(int corpNum)
	{
		this.corpNum = corpNum;
	}

	/**
	 * 解析欧赔主页数据
	 * 
	 * @param page 欧赔主页页面
	 */
	@Override
	public boolean parseWebPage(WebPage page)
	{
		if (!page.isCompleted() || page.getContent() == null)
		{
			throw new IllegalArgumentException("The Okooo JcPageParser is not completed or Content is null. ");
		}
		
		if(!(page instanceof OkoooWebPage))
		{
			return false;
		}
		Document doc = parseHtml(page);
		if(doc == null)
		{
			return false;
		}
		
		//解析比赛信息
		Element matchEl = doc.selectFirst(".container_wrapper .qbox");
		if(matchEl == null)
		{
			return false;
		}		
		parseMatchInfo(matchEl);
		
		//解析百家欧赔数据
		Elements oddsEls = doc.select("#data_main_content table tbody tr");
		for (Element element : oddsEls)
		{
			parseOddsOp(element);
		}
		
		//解析一共有多少家公司
		Element totalCompNumEl = doc.selectFirst("#data_footer_float .noBberBottom tbody tr td .oddthfooterbtn .oddthfooter02 #matchNum");
		if(totalCompNumEl != null)
		{
			String val = totalCompNumEl.text();
			this.corpNum = NumberUtil.parseInt(val);
		}
		return true;
	}
	
	/**
	 * 解析欧赔数据
	 * 
	 * @param element 欧赔数据
	 */
	protected void parseOddsOp(Element element)
	{
		int firsttime;
		String gid;
		String gname;
		//String ordinary;
		
		float firstwinodds;
		float firstdrawodds;
		float firstloseodds;
		
		Elements cols = element.select("td");
		if(cols.size() < 10)
		{
			return;
		}
		firsttime = NumberUtil.parseInt(element.attr("data-time"));
		gid = cols.get(0).selectFirst("input").attr("value");
		//ordinary = cols.get(0).select("label span").text();
		gname = cols.get(1).text();
		
		//以下为初始赔率值
		firstwinodds = getOddsValue(cols.get(2).text());
		firstdrawodds = getOddsValue(cols.get(3).text());
		firstloseodds = getOddsValue(cols.get(4).text());
		
		OkoooOp op = new OkoooOp();
		op.setGid(gid);
		op.setGname(gname);
		//op.setIsfirst(true);
		op.setIsend(false);
		op.setFirstwinodds(firstwinodds);
		op.setFirstdrawodds(firstdrawodds);
		op.setFirstloseodds(firstloseodds);
		
		Date d = DateUtil.add(matchTime, - ((long) firsttime) * 1000);
		op.setFirsttime(DateUtil.DATE_TIME_FORMAT.format(d));		
		//ops.add(op);
		
		//以下为最新赔率值
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
		String time;
		long t = 0;
		
		winodds = getOddsValue(cols.get(5).text());
		drawodds = getOddsValue(cols.get(6).text());
		loseodds = getOddsValue(cols.get(7).text());		
		time = cols.get(8).attr("attr");
		if(StringUtils.isNotEmpty(time))
		{
			t = getTime(time);
			d = DateUtil.add(matchTime, -t);
			time = DateUtil.DATE_TIME_FORMAT.format(d);
		}
		
		winprob = getOddsValue(cols.get(9).text());
		drawprob = getOddsValue(cols.get(10).text());
		loseprob = getOddsValue(cols.get(11).text());
		winkelly = getOddsValue(cols.get(12).text());
		drawkelly = getOddsValue(cols.get(13).text());
		losekelly = getOddsValue(cols.get(14).text());
		lossratio = getOddsValue(cols.get(15).text());
		
		//op = new OkoooOp();
		op.setGid(gid);
		op.setGname(gname);
		op.setMid(mid);
		op.setTime(time);	
		op.setLasttime(time);
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
		op.setIsend(false);
		op.setIsfirst(false);
		op.setOddstype("op");
		op.setSource(SoccerConstants.DATA_SOURCE_OKOOO);
				
		ops.add(op);
	}
	
	
	/**
	 * 解析比赛信息
	 * 
	 * @param element 元素数据
	 */
	protected void parseMatchInfo(Element element)
	{
		String round;
		//String leaguename;
		String lid;
		String season;
		//String seasonName;
		String matchtime;
		String score;
		
		Elements elements = element.children();
		if(elements.size() < 2)
		{
			//比赛信息不全
			return;
		}
		
		//以下为第一个qbox_1的信息数据
		Element e1 = elements.get(0);		
		Elements els = e1.select(".qbx #lunci span"); //id="lunci"
		if(els.size() < 2)
		{
			return;
		}
		
		Elements els2 = els.get(0).select("a");
		//leaguename = els2.get(0).text();
		lid = getLastNumberValue(els2.get(0).attr("href"));
		//seasonName = els2.get(1).text();
		season = getLastNumberValue(els2.get(1).attr("href"));
		
		round = els.get(1).text();
		if(StringUtils.isNotEmpty(round))
		{
			round = Integer.toString( NumberUtil.parseIntegerFromString(round));
		}
		
		matchtime = e1.selectFirst(".qbx_2 p").text();
		if(StringUtils.isNotEmpty(matchtime))
		{
			matchTime = DateUtil.tryToParseDate(matchtime);
			if(matchTime != null)
			{
				matchtime = DateUtil.DATE_TIME_FORMAT.format(matchTime);
			}
		}
		
		//以下为第二个qbox_2的信息数据
		e1 = elements.get(1);
		els2 = e1.select("#matchTeam .qb2_t_1");
		if(els2.size() < 2)
		{
			return;
		}
		
		homename = els2.get(0).selectFirst(".qpai_zi").text();
		clientname = els2.get(1).selectFirst(".qpai_zi_1").text();
		
		Element e2 = e1.selectFirst(".vs");
		els = e2.select("span");
		if(els == null || els.size() < 2)
		{
			score = "";
		}
		else
		{
			score = els.get(0).text() + ":" + els.get(1).text();
		}
		
		
		if(match == null)
		{
			match = new Match();
		}
		match.setMid(mid);
		match.setLid(lid);
		match.setMatchtime(matchtime);
		match.setSeason(season);
		match.setRound(round);
		match.setScore(score);
	}
	
	/**
	 * 解析时间，其格式为: 开赔时间：赛前61时27分
	 *  
	 * @param time 时间值
	 * @return 整型时间
	 */
	protected long getTime(String time)
	{
		int[] ts = NumberUtil.parseAllIntegerFromString(time);
		if(ts == null || ts.length < 2)
		{
			return 0;
		}
		else
		{
			long t = ts[0] * 3600 + ts[1] * 60;
			return t * 1000;
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
