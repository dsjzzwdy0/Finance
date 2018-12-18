package com.loris.soccer.bean.table;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.soccer.bean.item.IssueMatch;

@TableName("soccer_lottery_bd")
public class BdMatch extends IssueMatch
{
	/***/
	private static final long serialVersionUID = 1L;
	
	public void setBdMatch(BdMatch match)
	{
		setIssueMatch(match);
		this.date = match.date;
	}

	@Override
	public String toString()
	{
		return "BdMatch [issue=" + issue + ", date=" + date + ", ordinary=" + ordinary + ", mid=" + mid + ", lid=" + lid
				+ ", leaguename=" + leaguename + ", matchtime=" + matchtime + ", homeid=" + homeid + ", homename="
				+ homename + ", homelid=" + homelid + ", homerank=" + homerank + ", clientid=" + clientid
				+ ", clientname=" + clientname + ", clientlid=" + clientlid + ", clietrank=" + clientrank
				+ ", closetime=" + closetime + ", rangqiu=" + rangqiu + ", winodds=" + winodds + ", drawodds="
				+ drawodds + ", loseodds=" + loseodds + "]";
	}
}
