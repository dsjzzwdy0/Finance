package com.loris.soccer.bean.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.loris.base.data.PairValue;
import com.loris.soccer.bean.item.IssueMatch;

/**
 * 比赛数据映射
 * 
 * @author jiean
 *
 */
public class IssueMatchMapping
{
	protected String sourceName;
	protected String destName;
	
	//protected List<PairValue<JcMatch, JcMatch>> pairs;	
	protected Map<String, Mapping> pairs;
	
	/**
	 * 数据映射
	 * @author dsj
	 *
	 */
	public class Mapping extends PairValue<IssueMatch, IssueMatch>
	{
		/**
		 * Create a new instance of JcMatchMapping.
		 * @param source 比赛数据源
		 * @param dest 比赛数据目标
		 */
		public Mapping(IssueMatch source, IssueMatch dest)
		{
			super(source, dest);
		}
		
		/**
		 * 获得比赛源数据的比赛编号
		 * @return 比赛编号
		 */
		public String getSourceMid()
		{
			return key.getMid();
		}
		
		/**
		 * 获得目标比赛编号
		 * @return 比赛编号
		 */
		public String getDestMid()
		{
			return value.getMid();
		}
	}
	
	/**
	 * 创建一个映射
	 * @param source 源数据名称
	 * @param dest 目标数据名称
	 */
	public IssueMatchMapping(String source, String dest)
	{
		this.sourceName = source;
		this.destName = dest;
		pairs = new HashMap<>();
	}
	
	/**
	 * 添加一个数据映射记录
	 * @param source 源数据
	 * @param dest 目标数据
	 */
	public void addMapping(IssueMatch source, IssueMatch dest)
	{
		pairs.put(dest.getMid(), new Mapping(source, dest));
	}
	
	/**
	 * 检测是否在数据中
	 * @param match 比赛
	 * @return
	 */
	public boolean contains(IssueMatch match)
	{
		if(pairs.containsKey(match) || pairs.containsValue(match))
		{
			return true;
		}
		return false;
	}

	public String getSourceName()
	{
		return sourceName;
	}

	public void setSourceName(String sourceName)
	{
		this.sourceName = sourceName;
	}

	public String getDestName()
	{
		return destName;
	}

	public void setDestName(String destName)
	{
		this.destName = destName;
	}
	
	public int size()
	{
		return pairs.size();
	}
	
	/**
	 * 获得映射对象
	 * @param mid 源数据的比赛编号
	 * @return 数据映射
	 */
	public Mapping getMappingBySourceMID(String mid)
	{
		for (Mapping pair : pairs.values())
		{
			if(pair.getKey().getMid().equalsIgnoreCase(mid))
			{
				return pair;
			}
		}
		return null;
	}
	
	/**
	 * 获得数据映射对象
	 * @param mid 目标比赛编号
	 * @return 数据映射
	 */
	public Mapping getMappingByDestMID(String mid)
	{
		return pairs.get(mid);
	}
	
	/**
	 * 获得映射目标的比赛MID编号列表
	 * @return 目标比赛的MID编号
	 */
	public List<String> getDestMatchIds()
	{
		List<String> mids = new ArrayList<>();
		for (Mapping pair : pairs.values())
		{
			mids.add(pair.getDestMid());
		}
		return mids;
	}
	
	/**
	 * 获得映射源数据比赛的MID编号列表
	 * @return 源比赛的MID编号
	 */
	public List<String> getSourceMatchIds()
	{
		List<String> mids = new ArrayList<>();
		for (Mapping pair : pairs.values())
		{
			mids.add(pair.getSourceMid());
		}
		return mids;
	}
}
