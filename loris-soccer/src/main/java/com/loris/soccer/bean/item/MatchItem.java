package com.loris.soccer.bean.item;

import java.util.Date;
import java.util.Comparator;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.entity.AutoIdEntity;
import com.loris.base.util.DateUtil;

/**
 * 比赛实体
 * 
 * @author dsj
 *
 */
public class MatchItem extends AutoIdEntity implements Comparator<MatchItem>
{
	/***/
	private static final long serialVersionUID = 1L;

	protected String mid;       	//比赛编号
	protected String lid;       	//赛事类别
	protected String homeid;   		//主队
	protected String clientid; 		//客队
	protected String matchtime; 	//开赛时间
	
	public String getMid()
	{
		return mid;
	}
	public void setMid(String mid)
	{
		this.mid = mid;
	}
	public String getLid()
	{
		return lid;
	}
	public void setLid(String lid)
	{
		this.lid = lid;
	}
	public String getHomeid()
	{
		return homeid;
	}
	public void setHomeid(String homeid)
	{
		this.homeid = homeid;
	}
	public String getClientid()
	{
		return clientid;
	}
	public void setClientid(String clientid)
	{
		this.clientid = clientid;
	}
	
	public Date getMatchDate()
	{
		return DateUtil.tryToParseDate(matchtime);
	}
	
	public long getMatchtimevalue()
	{
		Date d1 = DateUtil.tryToParseDate(matchtime);
		if(d1 == null)
		{
			return 0;
		}
		else
		{
			return d1.getTime();
		}
	}
	
	public String getMatchtime()
	{
		return matchtime;
	}
	public void setMatchtime(String matchtime)
	{
		this.matchtime = matchtime;
	}
	
	public void setMatchItem(MatchItem item)
	{
		this.lid = item.lid;
		this.mid = item.mid;
		this.homeid = item.homeid;
		this.clientid = item.clientid;
		this.matchtime = item.matchtime;
	}
	
	
	/**
	 * 检测是否该队为此场比赛的主队
	 * 
	 * @param team 球队编号
	 * @return 是否主队的标志
	 */
	public boolean isHomeTeam(String team)
	{
		return homeid.equalsIgnoreCase(team);
	}
	
	/**
	 * 检测是否该队为此场比赛的客队
	 * 
	 * @param team 球队编号
	 * @return 是否客队的标志
	 */
	public boolean isClientTeam(String team)
	{
		return clientid.equalsIgnoreCase(team);
	}
	
	/**
	 * 检测该场比赛是否是此球队的比赛
	 * 
	 * @param team 球队编号
	 * @return 是否是该球队的比赛标志
	 */
	public boolean isTeam(String team)
	{
		return isHomeTeam(team) || isClientTeam(team);
	}
	
	/**
	 * 判断是否是两队的比赛
	 * 
	 * @param t1 队一的编号
	 * @param t2 队二的编号
	 * @return 是否的标志
	 */
	public boolean isTwoTeam(String t1, String t2)
	{
		return (homeid.equalsIgnoreCase(t1) && clientid.equalsIgnoreCase(t2) ||
				homeid.equalsIgnoreCase(t2) && clientid.equalsIgnoreCase(t1) );
	}

	/**
	 * 判断是否为主队的历史比赛
	 * 
	 * @param match 比赛数据
	 * @return 是否的标志
	 */
	public boolean isHomeTeamHistoryMatch(MatchItem match)
	{
		return isTeamHistoryMatch(homeid, matchtime, match);
	}
	
	/**
	 * 判断是否为客队的历史比赛
	 * 
	 * @param match 比赛数据
	 * @return 是否的标志
	 */
	public boolean isClientHistoryMatch(MatchItem match)
	{
		return isTeamHistoryMatch(clientid,  matchtime, match);
	}
	
	/**
	 * 判断是否为某一队比赛的历史比赛
	 * 
	 * @param tid 球队编号
	 * @param time 当前比赛时间
	 * @param match 比赛数据
	 * @return 是否历史比赛的标志
	 */
	protected boolean isTeamHistoryMatch(String tid, String time, MatchItem match)
	{
		if(StringUtils.isEmpty(tid))
		{
			return false;
		}
		if(tid.equalsIgnoreCase(match.getHomeid()) ||
				tid.equalsIgnoreCase(match.getClientid()))
		{
			if(time.compareTo(match.getMatchtime()) > 0)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 比赛两场比赛的先后顺序
	 * @param o 比赛
	 * @return
	 */
	public int compare(MatchItem o)
	{
		return compare(this, o);
	}
	
	@Override
	public int compare(MatchItem o1, MatchItem o2)
	{
		Date d1 = DateUtil.tryToParseDate(o1.getMatchtime());
		Date d2 = DateUtil.tryToParseDate(o2.getMatchtime());
		
		if(d1 == null && d2 == null)
		{
			return 0;
		}
		if(d1 != null)
		{
			return d1.compareTo(d2);
		}
		else
		{
			return -1;
		}
	}
	
	@Override
	public String toString()
	{
		return "Match [mid=" + mid + ", lid=" + lid + ", homeid=" + homeid + ", clientid=" + clientid + ", matchtime="
				+ matchtime + "]";
	}
	
}
