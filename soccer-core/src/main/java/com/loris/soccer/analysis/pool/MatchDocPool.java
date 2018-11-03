package com.loris.soccer.analysis.pool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.loris.soccer.analysis.data.MatchData;
import com.loris.soccer.analysis.data.MatchDoc;
import com.loris.soccer.bean.setting.CorpSetting;

/**
 * 比赛数据池
 * 
 * @author jiean
 *
 */
public class MatchDocPool
{
	/** 数据池的大小 */
	protected int poolSize = 5;
	
	/** 数据池 */
	protected Map<String, MatchDoc> dataPools = new HashMap<>();
	
	/** 唯一的实例 */
	private static MatchDocPool instance = null;
	
	/**
	 * 唯一的一个引用实例
	 * @return 数据实例
	 */
	public static MatchDocPool getInstance()
	{
		if(instance == null)
		{
			instance = new MatchDocPool();
		}
		return instance;
	}
	
	private MatchDocPool()
	{
	}
	
	
	/**
	 * 获得数据容器
	 * @param issue 比赛期号
	 * @return 数据容器
	 */
	public MatchDoc getMatchDocsFromPool(String issue, boolean needReresh)
	{
		MatchDoc dataVector = dataPools.get(issue);
		if(dataVector != null)
		{
			if(needReresh)
			{
				dataPools.remove(issue);
			}
			else
			{
				return dataVector;
			}
		}
						
		//CorpSetting configure = dataVector == null ?
		//		MatchDocLoader.getDefaultCorpSetting() : dataVector.getCorpSetting();
		CorpSetting corpSetting = MatchDocLoader.getDefaultCorpSetting();
		dataVector = MatchDocLoader.getMatchDoc(issue, corpSetting);
		
		if(dataVector != null)
		{
			maintainDataPool(issue, dataVector);
		}
		
		return dataVector;
	}
	
	/**
	 * 维护数据池中的内容
	 * @param dataVector 数据池
	 */
	protected void maintainDataPool(String issue, MatchDoc dataVector)
	{
		//先删除最早的数据
		if(dataPools.size() >= poolSize)
		{
			List<String> issues = new ArrayList<>();
			issues.addAll(dataPools.keySet());
			Collections.sort(issues);
			
			String key = issues.get(0);
			dataPools.remove(key);
		}

		dataPools.put(issue, dataVector);
	}
	
	
	/**
	 * 从内存中查找竞彩数据成果
	 * @param mid 比赛编号
	 * @return 竞彩数据结果
	 */
	public MatchData getJcMatchDataFromPool(String mid)
	{
		MatchData data = null;
		for (MatchDoc dataVector : dataPools.values())
		{
			data = dataVector.getMatchData(mid);
			if(data != null)
			{
				return data;
			}
		}
		return null;
	}

	public int getPoolSize()
	{
		return poolSize;
	}

	public void setPoolSize(int poolSize)
	{
		this.poolSize = poolSize;
	}
}
