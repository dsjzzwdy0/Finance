package com.loris.soccer.bean.table;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.soccer.bean.item.MatchItem;

@TableName("soccer_lottery_zc")
public class ZcMatch extends MatchItem
{
	/***/
	private static final long serialVersionUID = 1L;

	private String issue;				//赛期
	private String ordinary;            //比赛序号：14场或9场内部编号
	private String kjtime;              //开奖时间
	private String closetime;           //开奖截止时间
	
	private String lid;                 //赛事编号
	private String leaguetype;          //赛事类型
	private String leaguename;          //赛事名称

	private String homename;            //主队名称
	private String homerank;            //主队排名
	
	private String clientname;          //客队名称
	private String clientrank;          //客队排名
	
	public String getIssue()
	{
		return issue;
	}
	public void setIssue(String issue)
	{
		this.issue = issue;
	}
	public String getOrdinary()
	{
		return ordinary;
	}
	public void setOrdinary(String ordinary)
	{
		this.ordinary = ordinary;
	}
	public String getKjtime()
	{
		return kjtime;
	}
	public void setKjtime(String kjtime)
	{
		this.kjtime = kjtime;
	}
	public String getClosetime()
	{
		return closetime;
	}
	public void setClosetime(String closetime)
	{
		this.closetime = closetime;
	}
	public String getLid()
	{
		return lid;
	}
	public void setLid(String lid)
	{
		this.lid = lid;
	}
	public String getLeaguetype()
	{
		return leaguetype;
	}
	public void setLeaguetype(String leaguetype)
	{
		this.leaguetype = leaguetype;
	}
	public String getLeaguename()
	{
		return leaguename;
	}
	public void setLeaguename(String leaguename)
	{
		this.leaguename = leaguename;
	}

	public String getHomename()
	{
		return homename;
	}
	public void setHomename(String homename)
	{
		this.homename = homename;
	}
	public String getHomerank()
	{
		return homerank;
	}
	public void setHomerank(String homerank)
	{
		this.homerank = homerank;
	}
	public String getClientname()
	{
		return clientname;
	}
	public void setClientname(String clientname)
	{
		this.clientname = clientname;
	}
	public String getClientrank()
	{
		return clientrank;
	}
	public void setClientrank(String clientrank)
	{
		this.clientrank = clientrank;
	}
	@Override
	public String toString()
	{
		return "ZcMatch [mid=" + mid + ", matchtime=" + matchtime + ", issue=" + issue + ", ordinary=" + ordinary
				+ ", kjtime=" + kjtime + ", closetime=" + closetime + ", lid=" + lid + ", leaguetype=" + leaguetype
				+ ", leaguename=" + leaguename + ", homeid=" + homeid + ", homename=" + homename + ", homerank="
				+ homerank + ", clientid=" + clientid + ", clientname=" + clientname + ", clientrank=" + clientrank
				+ "]";
	}
}
