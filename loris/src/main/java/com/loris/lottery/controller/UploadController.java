package com.loris.lottery.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import com.loris.base.util.ReflectUtil;
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
			List<Entity> entities = list.getRecords();
			String clazz = list.getClazzname();
			
			if(Op.class.getName().equals(clazz))
			{
				List<Op> ops = transformRecords(entities);
				String mid = ops.get(0).getMid();
				soccerManager.addOrUpdateMatchOps(mid, ops);
			}
			else if(Yp.class.getName().equals(clazz))
			{
				List<Yp> yps = transformRecords(entities);
				String mid = yps.get(0).getMid();
				soccerManager.addOrUpdateMatchYps(mid, yps);
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
			
			logger.info("Success to add " + entities.size() + " " + clazz + " records.");
			
			/*for (Entity entity : entities)
			{
				logger.info(entity);
			}*/
		}
		catch(Exception e)
		{
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
	protected static<T extends Entity> List<T> transformRecords(List<Entity> entities)
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
		
		List<Method> methods = ReflectUtil.getAllMethods(clazz, false);		
		for (Object rec : array)
		{
			Entity entity = (Entity) clazz.newInstance();
			entities.add(entity);
			
			JSONObject obj = (JSONObject)rec;
			for (String key : obj.keySet())
			{
				Object value = obj.get(key);
				for (Method method : methods)
				{
					String methodName = method.getName();
					if(methodName.equals("set" + key.substring(0, 1).toUpperCase() + key.substring(1)))
					{
						method.invoke(entity, value);
					}
				}
			}
		}
		
		return new TableRecordList(clazzname, entities);
	}
}
