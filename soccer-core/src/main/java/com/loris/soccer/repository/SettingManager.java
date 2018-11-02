package com.loris.soccer.repository;

import java.util.ArrayList;
import java.util.List;

import com.loris.soccer.bean.setting.CorpSetting;
import com.loris.soccer.bean.setting.Parameter;
import com.loris.soccer.bean.setting.Setting;

/**
 * 配置管理器
 * @author jiean
 *
 */
public class SettingManager
{
	/** SettingManager. */
	private static SettingManager instance;
	
	/**
	 * Get the single instance of SettingManager.
	 * @return SettingManager
	 */
	public static SettingManager getSettingManager()
	{
		if(instance == null)
		{
			instance = new SettingManager();
		}
		return instance;
	}
	
	/** 设置列表 */
	protected List<Setting> settings = new ArrayList<>();
	
	/**
	 * The SettingManager.
	 */
	private SettingManager()
	{
		initialize();
	}
	
	/**
	 * 初始化设置管理器
	 */
	private void initialize()
	{
		settings.clear();
		SoccerManager soccerManager = SoccerManager.getInstance();
		List<Parameter> parameters = soccerManager.getParameters(null);
		
		Setting setting;
		for (Parameter parameter : parameters)
		{
			setting = getSetting(parameter.getPid());
			if(setting == null)
			{
				setting = new Setting();
				setting.setId(parameter.getPid());
				setting.setName(parameter.getPname());
				settings.add(setting);
			}
			setting.addParameter(parameter);
		}
	}
	
	/**
	 * 查找Setting对象
	 * @param id 根据ID值
	 * @return Setting对象
	 */
	protected Setting getSetting(String id)
	{
		for (Setting setting : settings)
		{
			if(id.equals(setting.getId()))
			{
				return setting;
			}
		}
		return null;
	}
	
	/**
	 * 查找配置数据
	 * @param sid 设置编号
	 * @param init 是否初始化
	 * @return 获得设置
	 */
	public Setting getSetting(String sid, boolean init)
	{
		if(init)
		{
			initialize();
		}
		return getSetting(sid);
	}
	
	/**
	 * 
	 * @param sid
	 * @return
	 */
	public CorpSetting getCorpSetting(String sid)
	{
		return SoccerManager.getInstance().getCorpSetting(sid);
	}
	
	/**
	 * Get All the Settings.
	 * @return setting list.
	 */
	public List<Setting> getSettings()
	{
		return settings;
	}
	
	/**
	 * 创建一个默认的博彩公司配置方案
	 * @return setting对象
	 */
	public Setting createDefaultCorpSetting()
	{
		String settingName = "默认方案";
		Setting setting = new Setting();
		setting.create();
		setting.setName(settingName);
		
		Parameter parameter = new Parameter();
		parameter.setName("平均欧赔");
		parameter.setType(Parameter.PARAM_TYPE_STRING);
		parameter.setValue("0");
		setting.addParameter(parameter);
		
		parameter = new Parameter();
		parameter.setName("Interwetten");
		parameter.setType(Parameter.PARAM_TYPE_STRING);
		parameter.setValue("21");
		setting.addParameter(parameter);
		
		return setting;
	}
	
	public static boolean updateSetting(Setting setting)
	{
		SoccerManager soccerManager = SoccerManager.getInstance();
		return soccerManager.addOrUpdateParameters(setting.getParams());
	}
}
