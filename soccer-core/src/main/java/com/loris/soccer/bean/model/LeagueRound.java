package com.loris.soccer.bean.model;

import java.util.ArrayList;
import java.util.List;

import com.loris.soccer.bean.data.table.league.Round;

public class LeagueRound
{
	protected List<Round> rounds = new ArrayList<>();
	
	/**
	 * Add the Round.
	 * @param round
	 */
	public void add(Round round)
	{
		rounds.add(round);
	}
}
