package com.loris.soccer.analysis.element;

import java.io.Serializable;

import com.loris.soccer.bean.SoccerConstants;

public class OddsElement implements Serializable
{
	/** */
	private static final long serialVersionUID = 1L;

	String time;
	String firsttime;
	int index; 					// 1: Last, 0: First
	String gid; 				// 公司编号
	String gname; 				// 公司名称
	String type;
	
	float[] values;
	String source;
	
	protected OddsElement()
	{
		this(SoccerConstants.ODDS_TYPE_OP);
	}
	
	public OddsElement(String type)
	{
		this.type = type;
		switch (type)
		{
		case SoccerConstants.ODDS_TYPE_OP:
			values = new float[13];
			break;
		case SoccerConstants.ODDS_TYPE_YP:
			values = new float[11];
			break;
		default:
			break;
		}
	}

	public String getFirsttime()
	{
		return firsttime;
	}

	public void setFirsttime(String firsttime)
	{
		this.firsttime = firsttime;
	}

	public String getTime()
	{
		return time;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setTime(String time)
	{
		this.time = time;
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	public String getGid()
	{
		return gid;
	}

	public void setGid(String gid)
	{
		this.gid = gid;
	}

	public String getGname()
	{
		return gname;
	}

	public void setGname(String gname)
	{
		this.gname = gname;
	}

	public float[] getValues()
	{
		return values;
	}

	public void setValues(float[] values)
	{
		this.values = values;
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
	}
	
	public void setValue(int index, float value)
	{
		values[index] = value;
	}
}
