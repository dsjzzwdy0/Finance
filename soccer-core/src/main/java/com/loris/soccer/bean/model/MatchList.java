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
	
	/**
	 * 增加数据
	 * @param items
	 */
	public void addSingleItems(List<? extends MatchItem> items)
	{
		for (MatchItem matchItem : items)
		{
			addSingleItem(matchItem);
		}
	}
	
	/**
	 * 加入比赛数据
	 * @param item
	 */
	public void addSingleItem(MatchItem item)
	{
		for (MatchItem m : this)
		{
			if(m.getMid().equals(item.getMid()))
			{
				return;
			}
		}
		this.add(item);
	}
	
	/**
	 * 获得比赛的编号列表
	 * @return
	 */
	public Keys getMids()
	{
		Keys keys = new Keys();
		for (MatchItem m : this)
		{
			keys.add(m.getMid());
		}
		return keys;
	}
}
