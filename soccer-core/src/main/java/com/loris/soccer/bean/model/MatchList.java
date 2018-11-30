package com.loris.soccer.bean.model;

import java.util.ArrayList;
import java.util.List;

import com.loris.soccer.bean.item.MatchItem;

public class MatchList extends ArrayList<MatchItem>
{
	/***/
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a new instance of MatchList.
	 */
	public MatchList()
	{
	}
	
	/**
	 * Create a new instance of MatchList.
	 * @param matchItems
	 */
	public MatchList(List<? extends MatchItem> matchItems)
	{
		addAll(matchItems);
	}
}
