package com.loris.lottery.controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.ClientInfo;
import com.loris.base.bean.entity.Entity;
import com.loris.base.bean.wrapper.Rest;
import com.loris.base.bean.wrapper.TableRecordList;
import com.loris.base.util.DateUtil;
import com.loris.base.util.NumberUtil;
import com.loris.base.util.ReflectUtil;
import com.loris.soccer.bean.data.table.league.Match;
import com.loris.soccer.bean.data.table.lottery.BdMatch;
import com.loris.soccer.bean.data.table.lottery.JcMatch;
import com.loris.soccer.bean.data.table.odds.Op;
import com.loris.soccer.bean.data.table.odds.Yp;
import com.loris.soccer.repository.SoccerManager;

/**
 * 
 * @author jiean
 *
 */
@Controller
@RequestMapping("/upload")
public class UploadController extends BaseController
{
	private static Logger logger = Logger.getLogger(UploadController.class);
	
	@Autowired
	SoccerManager soccerManager;
	
	/** 注册的信息 */
	Map<String, ClientInfo> clients = new HashMap<>();
	
	/**
	 * 注册数据上传客户端
	 * @return 返回注册的状态
	 */
	@ResponseBody
	@RequestMapping("regist")
	public Rest registUploadClient()
	{
		ClientInfo info = getClientInfo();
		if(clients.containsKey(info.getAddr()))
		{
			logger.info("Has Registed: " + info);
		}
		else
		{
			logger.info("Regist: " + info);
		}
		clients.put(info.getAddr(), info);		
		return Rest.ok();
	}
	
	/**
	 * 注销客户端
	 * @return 返回注册的状态
	 */
	@ResponseBody
	@RequestMapping("unregist")
	public Rest unregistUploadClient()
	{
		ClientInfo info = getClientInfo();
		logger.info("Info: " + info);
		return Rest.ok();
	}

	/**
	 * 上传数据
	 * 
	 * @param json
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/table")
	public Rest uploadTableRecord(String json)
	{
		//logger.info("Upload Data: " + json);
		
		try
		{
			TableRecordList list = parseObjects(json);
			if(list == null || StringUtils.isEmpty(list.getClazzname()) || list.getRecords() == null)
			{
				String error = "Parse Error, the TableRecorList is not set correctly."; 
				logger.info(error);
				return Rest.failure(error);
			}
			List<? extends Entity> entities = list.getRecords();
			String clazz = list.getClazzname();
			
			if(Op.class.getName().equals(clazz))
			{
				List<Op> ops = transformRecords(entities);
				String mid = ops.get(0).getMid();
				logger.info("First Op Value: " + ops.get(0));
				synchronized(soccerManager)
				{
					soccerManager.addOrUpdateMatchOps(mid, ops);
				}
			}
			else if(Yp.class.getName().equals(clazz))
			{
				List<Yp> yps = transformRecords(entities);
				String mid = yps.get(0).getMid();
				logger.info("First Yp Value: " + yps.get(0));
				synchronized(soccerManager)
				{
					soccerManager.addOrUpdateMatchYps(mid, yps);
				}
			}
			else if(JcMatch.class.getName().equals(clazz))
			{
				List<JcMatch> jcMatchs = transformRecords(entities);
				soccerManager.addOrUpdateJcMatches(jcMatchs);
			}
			else if(BdMatch.class.getName().equals(clazz))
			{
				List<BdMatch> bdMatchs = transformRecords(entities);
				soccerManager.addOrUpdateBdMatches(bdMatchs);
			}
			else if(Match.class.getName().equals(clazz))
			{
				List<Match> matchs =transformRecords(entities);
				soccerManager.addOrUpdateMatches(matchs);
			}
			
			logger.info("Success to add " + entities.size() + " " + clazz + " records.");
			
			/*for (Entity entity : entities)
			{
				logger.info(entity);
			}*/
		}
		catch(Exception e)
		{
			e.printStackTrace();
			String error = "Error: " + e.toString();
			logger.info(error);
			return Rest.failure(error);
		}
		return Rest.ok();
	}
	
	/**
	 * 转换数据列表
	 * @param entities
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected static<T extends Entity> List<T> transformRecords(List<? extends Entity> entities)
	{
		List<T> records = new ArrayList<>();
		for (Entity entity : entities)
		{
			records.add((T)entity);
		}
		return records;
	}
	
	/**
	 * 获得当前的访问信息
	 * @return
	 */
	protected ClientInfo getClientInfo()
	{
		ClientInfo clientInfo = new ClientInfo();
		clientInfo.setAddr(request.getRemoteAddr());
		clientInfo.setHost(request.getRemoteHost());
		clientInfo.setUser(request.getRemoteUser());
		clientInfo.setPort(request.getRemotePort());
		clientInfo.setConnecttime(new Date());
		return clientInfo;
	}

	/**
	 * 解析数据
	 * 
	 * @param json
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	protected TableRecordList parseObjects(String json) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException
	{
		JSONObject object = JSON.parseObject(json);
		if(object == null)
		{
			return null;
		}
		String clazzname = object.getString("clazzname");
		if(StringUtils.isEmpty(clazzname))
		{
			logger.info("Error, the type is not specified.");
			return null;
		}
		
		JSONArray array = object.getJSONArray("records");
		if(array == null)
		{
			logger.info("Error, the values are null.");
			return null;
		}
			
		List<Entity> entities = new ArrayList<>();
		Class<?> clazz = Class.forName(clazzname);
		
		List<Field> fields = ReflectUtil.getAllFields(clazz, false);
		for (Field field : fields)
		{
			field.setAccessible(true);
		}
		for (Object rec : array)
		{
			Entity entity = (Entity) clazz.newInstance();

			JSONObject obj = (JSONObject)rec;
			for (String key : obj.keySet())
			{
				Object value = obj.get(key);
				for (Field field : fields)
				{
					if(key.equals(field.getName()))
					{
						String type = field.getType().getName();
						if(type.equals("java.lang.String")){  
							field.set(entity, (String)value);  
		                }  
		                else if(type.equals("java.util.Date"))
		                {
		                	Date date = DateUtil.tryToParseDate(value.toString());
		                	field.set(entity, date);  
		                }  
		                else if(type.equals("java.lang.Integer") || type.equals("int"))
		                {
		                	int t = NumberUtil.parseInt(value);
		                	field.set(entity, t);
		                }
		                else if(type.equals("java.lang.Float") || type.equals("float"))
		                {
		                	float f = NumberUtil.parseFloat(value);
		                	field.set(entity, f);
		                }
		                else if(type.equals("java.lang.Double") || type.equals("double"))
		                {
		                	double d = NumberUtil.parseDouble(value);
		                	field.set(entity, d);
		                }
		                else 
		                {
		                	try
							{
		                		field.set(entity, value);
							}
							catch(Exception e)
							{
								logger.info("MethodName: " + field.getName() + ", Argument Type: " +  type);
								e.printStackTrace();
							}
		                }
					}
					/*
					//不是Bean的方法，带有多个参数
					if(method.getParameterCount() < 1 || method.getParameterCount() > 1)
					{
						continue;
					}
					String methodName = method.getName();
					if(methodName.equals("set" + key.substring(0, 1).toUpperCase() + key.substring(1)))
					{
						String type = method.getParameterTypes()[0].getName();
						if(type.equals("java.lang.String")){  
		                    method.invoke(entity, (String)value);  
		                }  
		                else if(type.equals("java.util.Date"))
		                {
		                	Date date = DateUtil.tryToParseDate(value.toString());
		                    method.invoke(entity, date);  
		                }  
		                else if(type.equals("java.lang.Integer") || type.equals("int"))
		                {
		                	int t = NumberUtil.parseInt(object);
		                	method.invoke(entity, t);
		                }
		                else if(type.equals("java.lang.Float") || type.equals("float"))
		                {
		                	float f = NumberUtil.parseFloat(object);
		                	method.invoke(entity, f);
		                }
		                else if(type.equals("java.lang.Double") || type.equals("double"))
		                {
		                	double d = NumberUtil.parseDouble(object);
		                	method.invoke(entity, d);
		                }
		                else 
		                {
		                	try
							{
								method.invoke(entity, value);
							}
							catch(Exception e)
							{
								logger.info("MethodName: " + methodName + ", Argument Type: " +  type);
								e.printStackTrace();
							}
		                }

					}*/
				}
			}
			
			//logger.info(entity);
			entities.add(entity);
		}
		
		return new TableRecordList(clazzname, entities);
	}
}
