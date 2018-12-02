package com.loris.base.web.config.setting;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.entity.UUIDEntity;


@TableName("loris_downloader")
public class DownSetting extends UUIDEntity implements Cloneable
{
	/** UUID. */
	private static final long serialVersionUID = 1L;
	private String name;				//名称
	private String wid;                 //下载器标识
	private String classname;			//类名称
	private String category;			//分类名
	private String cateid;				//分类标识
	private String description;			//描述与说明
	private String encoding;			//编码
	private int interval;				//间隔时间
	private boolean enable;				//启用标志
	private String days;				//时间 2018-02-13, 2018-02-14
	private String start;				//开始时间
	private String end;					//结束时间
	private boolean daysection;  		//日期区间
	private boolean flag;             	//标识符	
	private String createtime;			//创建时间
	private String preparetime;			//预备下载的时间
	private String stoptime;			//停止时间
	private String downtime;			//下载时间
	private String finishtime;			//完成时间
	private int status;            		//当前状态： 0：已经初始化, 1：准备完毕, 2: 开始, 3: 暂停, 4: 结束
	private int curindex;               //当前下载的数据个数
	private int total;					//总需要下载的数据量
	private int left; 					//剩余的数据量
	private String params;				//参数数据
	
	@TableField(exist=false)
	private String info;
	
	@TableField(exist=false)
	protected Map<String, String> paramsMap = new HashMap<>();		//参数
	
	public String getWid()
	{
		return wid;
	}
	public void setWid(String wid)
	{
		this.wid = wid;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getCateid()
	{
		return cateid;
	}
	public void setCateid(String cateid)
	{
		this.cateid = cateid;
	}
	public String getEncoding()
	{
		return encoding;
	}
	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}
	public int getInterval()
	{
		return interval;
	}
	public void setInterval(int interval)
	{
		this.interval = interval;
	}
	public boolean isEnable()
	{
		return enable;
	}
	public void setEnable(boolean enable)
	{
		this.enable = enable;
	}
	public String getDays()
	{
		return days;
	}
	public void setDays(String days)
	{
		this.days = days;
	}
	public String getStart()
	{
		return start;
	}
	public void setStart(String start)
	{
		this.start = start;
	}
	public String getEnd()
	{
		return end;
	}
	public void setEnd(String end)
	{
		this.end = end;
	}
	public String getCategory()
	{
		return category;
	}
	public void setCategory(String category)
	{
		this.category = category;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getClassname()
	{
		return classname;
	}
	public void setClassname(String classname)
	{
		this.classname = classname;
	}
	public boolean isFlag()
	{
		return flag;
	}
	public void setFlag(boolean flag)
	{
		this.flag = flag;
	}
	public String getCreatetime()
	{
		return createtime;
	}
	public void setCreatetime(String createtime)
	{
		this.createtime = createtime;
	}
	
	public boolean isDaysection()
	{
		return daysection;
	}
	public void setDaysection(boolean daysection)
	{
		this.daysection = daysection;
	}
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	
	public String getStoptime()
	{
		return stoptime;
	}
	public void setStoptime(String stoptime)
	{
		this.stoptime = stoptime;
	}
	public String getPreparetime()
	{
		return preparetime;
	}
	public void setPreparetime(String preparetime)
	{
		this.preparetime = preparetime;
	}
	public String getDowntime()
	{
		return downtime;
	}
	public void setDowntime(String downtime)
	{
		this.downtime = downtime;
	}
	public String getFinishtime()
	{
		return finishtime;
	}
	public void setFinishtime(String finishtime)
	{
		this.finishtime = finishtime;
	}
	public int getCurindex()
	{
		return curindex;
	}
	public void setCurindex(int curindex)
	{
		this.curindex = curindex;
	}
	public int getTotal()
	{
		return total;
	}
	public void setTotal(int total)
	{
		this.total = total;
	}
	public int getLeft()
	{
		return left;
	}
	public void setLeft(int left)
	{
		this.left = left;
	}
	public String getInfo()
	{
		return info;
	}
	public void setInfo(String info)
	{
		this.info = info;
	}
	public String getParams()
	{
		return params;
	}
	public void setParams(String params)
	{
		this.params = params;
	}
	public Map<String, String> getParamsMap()
	{
		return paramsMap;
	}
	public void addParamsMap(String key, String value)
	{
		paramsMap.put(key, value);
	}
	
	public void encodeParams()
	{
		if(paramsMap == null || paramsMap.size() == 0)
		{
			params = "";
			return;
		}
		params = JSONObject.toJSONString(paramsMap);
	}
	
	public void decodeParams()
	{
		if(StringUtils.isEmpty(params))
		{
			return;
		}
		try
		{
			JSONObject object = JSONObject.parseObject(params);
			paramsMap.clear();
			if(object != null)
			{
				Object value;
				for (String key: object.keySet())
				{
					value = object.get(key);
					paramsMap.put(key, (String)value);
				}
			}
		}
		catch(Exception e)
		{			
		}
	}
	
	/**
	 * 克隆并设置数据
	 * @param setting
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public DownSetting cloneAndSetDownSetting(DownSetting setting) throws CloneNotSupportedException
	{
		DownSetting setting2 = (DownSetting)this.clone();
		if("gb2312".equalsIgnoreCase(setting.getEncoding()) || "utf-8".equalsIgnoreCase(setting.getEncoding()))
		{
			setting2.setEncoding(setting.getEncoding());
		}
		if(StringUtils.isNotEmpty(setting.getDays()))
		{
			setting2.setDays(setting.getDays());
		}
		if(setting.getInterval() > 0)
		{
			setting2.setInterval(setting.getInterval());
		}
		if(StringUtils.isNotEmpty(setting.start))
		{
			setting2.setStart(setting.start);
		}
		if(StringUtils.isNotEmpty(setting.end))
		{
			setting2.setEnd(setting.end);
		}
		return setting2;
	}
	
	@Override
	public String toString()
	{
		return "WebPageSetting [name=" + name + ", category=" + category + ", encoding=" + encoding 
				+ ", interval=" + interval + ", enable=" + enable + ", days=" + days + ", start=" + start
				+ ", end=" + end + ", description=" + description + ", flag=" + flag 
				+ ", classname=" + classname  + ", createtime=" + createtime + "]";
	}
}
