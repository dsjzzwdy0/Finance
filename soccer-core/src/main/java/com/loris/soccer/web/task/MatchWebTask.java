package com.loris.soccer.web.task;

import com.loris.base.web.task.AbstractTask;
import com.loris.soccer.bean.data.table.league.Match;

public class MatchWebTask extends AbstractTask
{
	/** 比赛数据 */
	protected Match match;

	@Override
	public void run()
	{
		
	}

	public Match getMatch()
	{
		return match;
	}

	public void setMatch(Match match)
	{
		this.match = match;
	}
}
