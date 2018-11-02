package com.loris.soccer.bean.setting;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.util.DateUtil;
import com.loris.soccer.bean.data.table.lottery.UserCorporate;

/**
 * 赔率公司配置，这里主要是为各种分析提供基础数据，配置需要哪些公司提供的数据<br>
 * 1、欧赔公司<br/>
 * 2、亚盘公司<br/>
 * 
 * @author jiean
 *
 */
@TableName("soccer_setting_corps")
public class CorpSetting extends Setting
{
	/***/
	private static final long serialVersionUID = 1L;

	/**
	 * 创建实例
	 */
	public CorpSetting()
	{
		this.type = "corp";
	}
	
	/**
	 * Create the UUID value.
	 * 
	 */
	@Override
	public void create()
	{
		super.create();
		String date = DateUtil.getCurTimeStr();
		this.createtime = date;
		this.modifytime = date;
		
		for (Parameter parameter : params)
		{
			parameter.setPid(getId());
		}
	}
	
	/**
	 * 添加公司数据
	 * 
	 * @param corp 公司数据
	 */
	public void addUserCorporate(UserCorporate corp)
	{
		Parameter parameter = createParameter(corp);		
		addParameter(parameter);
	}
	
	/**
	 * 按数据来源、公司类型查找赔率公司
	 * @param source 数据来源
	 * @param type 公司赔率类型
	 * @return 公司
	 */
	public List<Parameter> getCorporates(String source, String type)
	{
		List<Parameter> results = new ArrayList<>();
		for (Parameter param : params)
		{
			//是博彩公司的类型
			if(!Parameter.PARAM_TYPE_CORP.equals(param.getType()))
			{
				continue;
			}
			if(source.equalsIgnoreCase(param.getValue1()) && type.equalsIgnoreCase(param.getValue2()))
			{
				results.add(param);
			}
		}
		return results;
	}
	
	/**
	 * 获得公司列表ID值
	 * @param source 数据来源
	 * @param type 公司类型
	 * @return 公司ID值列表
	 */
	public List<String> getCorporateIds(String source, String type)
	{
		List<String> gids = new ArrayList<>();
		for (Parameter param : params)
		{
			if(!Parameter.PARAM_TYPE_CORP.equals(param.getType()))
			{
				continue;
			}
			if(source.equalsIgnoreCase(param.getValue1()) && type.equalsIgnoreCase(param.getValue2()))
			{
				gids.add(param.getValue());
			}
		}
		return gids;
	}
	
	/**
	 * 获得数据来源的列表信息项
	 * @return 数据来源类型
	 */
	public List<String> getSources()
	{
		List<String> sources = new ArrayList<>();
		for (Parameter param : params)
		{
			if(!Parameter.PARAM_TYPE_CORP.equals(param.getType()))
			{
				continue;
			}
			
			if(StringUtils.isEmpty(param.getValue1()))
			{
				continue;
			}
			
			if(!isExist(sources, param.getValue1()))
			{
				sources.add(param.getValue1());
			}
		}
		return sources;
	}
	
	/**
	 * 检测是否已经存在于列表中
	 * @param sources 已知的数据来源列表
	 * @param s 新的名称
	 * @return 是否存在的标志
	 */
	protected boolean isExist(List<String> sources, String s)
	{
		for (String string : sources)
		{
			if(string.equalsIgnoreCase(s))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 清除参数
	 */
	public void clearParams()
	{
		params.clear();
	}
	
	/**
	 * 从参数里获得博彩公司的实例
	 * @param parameter 参数
	 * @return 博彩公司
	 */
	public static UserCorporate getUserCorporate(Parameter parameter)
	{
		if(!parameter.getType().equals(Parameter.PARAM_TYPE_CORP))
		{
			return null;
		}
		UserCorporate corp = new UserCorporate();
		corp.setGid(parameter.getValue());
		corp.setName(parameter.getName());
		corp.setSource(parameter.getValue1());
		corp.setType(parameter.getValue2());
		return corp;
	}
	
	/**
	 * 通过博彩公司创建参数数据
	 * @param corp 博彩公司
	 * @return 参数列表
	 */
	public static Parameter createParameter(UserCorporate corp)
	{
		Parameter parameter = new Parameter();
		parameter.setClassname(UserCorporate.class.getName());
		parameter.setType(Parameter.PARAM_TYPE_CORP);		
		parameter.setName(corp.getName());
		parameter.setValue(corp.getGid());
		parameter.setValue1(corp.getSource());
		parameter.setValue2(corp.getType());			//博彩公司类型
		parameter.setNumvalue(1);
		return parameter;
	}
}
