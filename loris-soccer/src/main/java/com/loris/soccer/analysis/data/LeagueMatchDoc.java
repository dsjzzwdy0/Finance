package com.loris.soccer.analysis.data;

public class LeagueMatchDoc extends MatchDoc
{
	protected String lid;
	protected String leaguename;
	
	public LeagueMatchDoc()
	{		
	}
	
	/**
	 * Create a new instance of LeagueMatchDoc
	 * @param lid
	 * @param leaguename
	 */
	public LeagueMatchDoc(String lid, String leaguename)
	{
		this.lid = lid;
		this.leaguename = leaguename;
	}
	
	public String getLid()
	{
		return lid;
	}
	public void setLid(String lid)
	{
		this.lid = lid;
	}
	public String getLeaguename()
	{
		return leaguename;
	}
	public void setLeaguename(String leaguename)
	{
		this.leaguename = leaguename;
	}
}
