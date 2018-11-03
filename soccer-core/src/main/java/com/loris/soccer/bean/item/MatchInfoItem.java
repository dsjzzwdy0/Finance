package com.loris.soccer.bean.item;

public class MatchInfoItem extends MatchItem
{
	/***/
	private static final long serialVersionUID = 1L;

	protected String leaguename;   	//赛事名称
	protected String homename;     	//主队名称
	protected String clientname;   	//客队名称


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
	public String getLeaguename()
	{
		return leaguename;
	}
	public void setLeaguename(String leaguename)
	{
		this.leaguename = leaguename;
	}
	public void setMatchInfoItem(MatchInfoItem item)
	{
		setMatchItem(item);
		leaguename = item.leaguename ;  
		homename = item.homename;       
		clientname = item.clientname;  
	}
}
