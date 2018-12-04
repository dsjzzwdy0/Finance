package com.loris.soccer.bean.data.table;

import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.entity.AutoIdEntity;
import com.loris.soccer.bean.SoccerConstants;

/**
 * 公司()实体类型
 * 
 * @author dsj
 *
 */
@TableName("soccer_lottery_corporate")
public class Corporate extends AutoIdEntity
{
	/***/
	private static final long serialVersionUID = 1L;
	
	private String gid; 			// 编号
	private String name; 			// 名称
	private boolean ismain; 		// 是否主流
	private String type; 			// 欧赔公司op，亚盘公司 yp
	private String source; 			// 来源：okooo, zgzcw等
	
	public String getGid()
	{
		return gid;
	}
	
	public void setGid(String gid)
	{
		this.gid = gid;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public boolean isIsmain()
	{
		return ismain;
	}
	
	public void setIsmain(boolean ismain)
	{
		this.ismain = ismain;
	}
	
	public String getSource()
	{
		return source;
	}
	
	public void setSource(String source)
	{
		this.source = source;
	}
	
	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
	
	/**
	 * 判断两公司是否为同一公司
	 * 
	 * @param corp 公司
	 * @return 是否为同一家公司的标志
	 */
	public boolean isSame(Corporate corp)
	{
		if(id.equalsIgnoreCase(corp.id) || gid.equalsIgnoreCase(corp.gid) ||
				name.equalsIgnoreCase(corp.name))
		{
			return true;
		}
		if(SoccerConstants.ODDS_TYPE_OP.equalsIgnoreCase(type) && 
				SoccerConstants.ODDS_TYPE_YP.equalsIgnoreCase(corp.type) &&
				"明升".equalsIgnoreCase(name) && "明陞".equalsIgnoreCase(corp.name))
		{
			return true;
		}
		else if(SoccerConstants.ODDS_TYPE_OP.equalsIgnoreCase(corp.type) && 
				SoccerConstants.ODDS_TYPE_YP.equalsIgnoreCase(type) &&
				"明升".equalsIgnoreCase(corp.name) && "明陞".equalsIgnoreCase(name))
		{
			return true;
		}
		return false;
	}

	@Override
	public String toString()
	{
		return "Corporate [gid=" + gid + ", name=" + name + ", ismain=" + ismain + ", type=" + type + ", source="
		        + source + "]";
	}
}
