package com.loris.soccer.bean.table;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.entity.AutoIdEntity;

/**
 * 参数类型
 * @author jiean
 *
 */
@TableName("soccer_setting_parameters")
public class CorpSettingParameter extends AutoIdEntity
{
	public static final String PARAM_TYPE_STRING = "string";
	public static final String PARAM_TYPE_NUMBER = "number";
	public static final String PARAM_TYPE_BOOLEAN = "boolean";
	public static final String PARAM_TYPE_OBJECT = "object";
	public static final String PARAM_TYPE_DATE = "date";
	public static final String PARAM_TYPE_CORP = "corp";
	
	/***/
	private static final long serialVersionUID = 1L;
	
	protected final static String DELIMETER = ";";

	protected String pid;				//父ID值
	protected String pname;				//父名称
	protected String name;				//名称
	protected String type;				//值的类型
	protected String value;				//参数的值
	protected String value1;			//参数的值1
	protected String value2; 			//参数的值2
	protected int numvalue = 1;			//值的个数
	protected String classname;			//类的名称
	protected String desc;				//备注和说明， 如果是博彩公司，则为公司的来源，如zgzcw/ okooo
	
	
	public String getPid()
	{
		return pid;
	}
	public void setPid(String pid)
	{
		this.pid = pid;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	public int getNumvalue()
	{
		return numvalue;
	}
	public void setNumvalue(int numvalue)
	{
		this.numvalue = numvalue;
	}
	
	public String getPname()
	{
		return pname;
	}
	public void setPname(String pname)
	{
		this.pname = pname;
	}
	public String getClassname()
	{
		return classname;
	}
	public void setClassname(String classname)
	{
		this.classname = classname;
	}
	public void addValue(String value)
	{
		if(StringUtils.isEmpty(value))
		{
			return;
		}
		if(StringUtils.isEmpty(this.value))
		{
			this.value = value;
		}
		else
		{
			this.value += ";" + value;
		}
		numvalue ++;
	}
	
	public String getValue(int index)
	{
		if(index >= numvalue)
		{
			throw new UnsupportedOperationException("Error, there are " + numvalue 
					+ " in the parameter, but you want to get " + index + " index value, exceed the array number.");
		}
		
		String[] strings = value.split(DELIMETER);
		if(index >= strings.length)
		{
			throw new UnsupportedOperationException("Error, there are " + numvalue 
					+ " in the parameter, but you want to get " + index + " index value, exceed the array number.");
		}
		return strings[index];
	}
	public String getValue1()
	{
		return value1;
	}
	public void setValue1(String value1)
	{
		this.value1 = value1;
	}
	public String getValue2()
	{
		return value2;
	}
	public void setValue2(String value2)
	{
		this.value2 = value2;
	}
	public String getDesc()
	{
		return desc;
	}
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	@Override
	public String toString()
	{
		return "Parameter [pid=" + pid + ", pname=" + pname + ", name=" + name + ", type=" + type + ", value=" + value
				+ ", numvalue=" + numvalue + ", classname=" + classname + "]";
	}
}
