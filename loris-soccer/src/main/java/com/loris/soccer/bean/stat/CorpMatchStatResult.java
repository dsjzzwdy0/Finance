package com.loris.soccer.bean.stat;

import java.util.ArrayList;
import java.util.List;

import com.loris.base.data.Region;

public class CorpMatchStatResult extends ArrayList<CorpMatchResult>
{
	/***/
	private static final long serialVersionUID = 1L;
	
	/** 区域数据	 */
	protected List<Region<Float>> regions;
	
	/**
	 * Create a new instance of CorpMatchStatResult
	 */
	public CorpMatchStatResult()
	{
	}
	
	/**
	 * 创建一个实例
	 * @param regions
	 */
	public CorpMatchStatResult(List<Region<Float>> regions)
	{
		this.regions = regions;
	}
	
	/**
	 * 获得比赛的某一个的数据
	 * @param gid
	 * @param min
	 * @param max
	 * @return
	 */
	public List<CorpMatchResult> getCorpMatchResult(String gid, String gname)
	{
		List<CorpMatchResult> results = new ArrayList<>();
		for (CorpMatchResult result : this)
		{
			if(gid.equals(result.getGid()))
			{
				results.add(result);
			}
		}
		
		if(results.isEmpty())
		{
			createCorpMatchResult(results, gid, gname);
			this.addAll(results);
		}
		
		return results;
	}
	
	/**
	 * 获得统计数据
	 * @param gid
	 * @param gname
	 */
	protected void createCorpMatchResult(List<CorpMatchResult> results, String gid, String gname)
	{
		for (Region<Float> region : regions)
		{
			CorpMatchResult result = new CorpMatchResult();
			result.setGid(gid);
			result.setGname(gname);
			result.setMin(region.getMin());
			result.setMax(region.getMax());			
			results.add(result);
		}
	}
}
