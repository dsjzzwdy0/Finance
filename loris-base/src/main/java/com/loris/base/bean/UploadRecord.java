package com.loris.base.bean;

import com.loris.base.bean.entity.AutoIdEntity;
import com.loris.base.util.DateUtil;

public class UploadRecord extends AutoIdEntity
{
	/***/
	private static final long serialVersionUID = 1L;

	protected String dataid;
	protected String source;
	protected String type;
	protected String recnum;
	protected String updatetime;
	protected String description;
	public String getDataid()
	{
		return dataid;
	}
	public void setDataid(String dataid)
	{
		this.dataid = dataid;
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
	public String getRecnum()
	{
		return recnum;
	}
	public void setRecnum(String recnum)
	{
		this.recnum = recnum;
	}
	public String getUpdatetime()
	{
		return updatetime;
	}
	public void setUpdatetime(String updatetime)
	{
		this.updatetime = updatetime;
	}
	public void setCurrenttime()
	{
		this.updatetime = DateUtil.getCurTimeStr();
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	@Override
	public String toString()
	{
		return "UploadRecord [source=" + source + ", type=" + type + ", recnum=" + recnum + ", updatetime=" + updatetime
				+ ", description=" + description + "]";
	}
}
