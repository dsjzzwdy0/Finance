package com.loris.stock.bean.model;

import java.util.ArrayList;
import java.util.List;

import com.loris.stock.bean.data.table.HolderCountInfo;
import com.loris.stock.bean.data.table.StockInfo;

public class HolderCountList extends StockInfo
{
	private static final long serialVersionUID = 1L;
	
	/** The counts. */
	private List<HolderCountInfo> counts = new ArrayList<HolderCountInfo>();
	
	/**
	 * Add the holder count info.
	 * @param info
	 */
	public void addHolderCountInfo(HolderCountInfo info)
	{
		counts.add(info);
	}
	
	/**
	 * Get the counts.
	 * 
	 * @return
	 */
	public List<HolderCountInfo> getCounts()
	{
		return counts;
	}

	@Override
	public String toString()
	{
		return "HolderCountList [counts=" + counts + ", symbol=" + symbol + "]";
	}
}
