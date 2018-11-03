package com.loris.soccer.analysis.data;

import com.loris.base.analysis.model.Freqs;
import com.loris.base.data.Variance;
import com.loris.soccer.bean.type.OddsValueType;

/**
 * 比赛中数据的方差、最大、最小值数据
 * 
 * @author jiean
 *
 */
public class OddsVariance extends Variance
{	
	String key;				//关键字
	String name;			//名称
	String time; 			//时间，统计的时间值
	OddsValueType type;		//数据类型
	Freqs counter;			//统计值
	
	/**
	 * Create a new instance of MatchOpVariance
	 */
	public OddsVariance()
	{
	}
	
	public OddsVariance(String key, OddsValueType type)
	{
		this.key = key;
		this.type = type;
	}

	public OddsValueType getType()
	{
		return type;
	}

	public void setType(OddsValueType type)
	{
		this.type = type;
	}

	public String getKey()
	{
		return key;
	}

	public String getTime()
	{
		return time;
	}

	public void setTime(String time)
	{
		this.time = time;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	/*public void setVariance(Variance variance)
	{
		super.setVariance(variance);
	}*/

	public Freqs getCounter()
	{
		return counter;
	}

	public void setCounter(Freqs counter)
	{
		this.counter = counter;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return "OddsVariance [key=" + key + ", name=" + name + ", time=" + time + ", type=" + type + ", counter="
				+ counter + ", size=" + size + ", min=" + min + ", max=" + max + ", avg=" + avg + ", totalerr="
				+ totalerr + ", stderr=" + stderr + ", overnum=" + overnum + "]";
	}
}
