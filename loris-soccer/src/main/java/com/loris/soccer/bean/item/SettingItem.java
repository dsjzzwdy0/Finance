package com.loris.soccer.bean.item;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.loris.base.bean.entity.UUIDEntity;
import com.loris.soccer.bean.table.CorpSettingParameter;

/**
 * 数据分析参数设置类
 * @author jiean
 *
 */
public class SettingItem extends UUIDEntity
{
	/***/
	private static final long serialVersionUID = 1L;
	
	protected String name; 					//名称
	protected String type;					//类型
	protected String user;					//创建用户
	protected String createtime;			//创建时间
	protected String modifytime;			//最后修改时间
	
	@TableField(exist=false)
	protected List<CorpSettingParameter> params = new ArrayList<>();
	
	/**
	 * Create a new instance of AnalysisSetting.
	 */
	public SettingItem()
	{
	}
	
	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getUser()
	{
		return user;
	}

	public void setUser(String user)
	{
		this.user = user;
	}

	public String getCreatetime()
	{
		return createtime;
	}

	public void setCreatetime(String createtime)
	{
		this.createtime = createtime;
	}

	public String getModifytime()
	{
		return modifytime;
	}

	public void setModifytime(String modifytime)
	{
		this.modifytime = modifytime;
	}

	public void setParams(List<CorpSettingParameter> params)
	{
		this.params = params;
	}

	/**
	 * Add the parameter.
	 * @param param
	 */
	public void addParameter(CorpSettingParameter param)
	{
		param.setPid(this.id);
		param.setPname(name);
		params.add(param);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<CorpSettingParameter> getParams()
	{
		return params;
	}
	
	public void clearParams()
	{
		this.params.clear();
	}

	public void addParams(List<CorpSettingParameter> params)
	{
		this.params.addAll(params);
	}
	
	public CorpSettingParameter getParameter(String name)
	{
		for (CorpSettingParameter parameter : params)
		{
			if(name.equals(parameter.getName()))
			{
				return parameter;
			}
		}
		return null;
	}

	@Override
	public String toString()
	{
		return "Setting [name=" + name + ", type=" + type + ", params=" + params + "]";
	}
}
