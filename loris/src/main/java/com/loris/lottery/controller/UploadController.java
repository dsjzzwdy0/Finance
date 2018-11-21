package com.loris.lottery.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.wrapper.Rest;

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

	/**
	 * 上传数据
	 * 
	 * @param json
	 * @return
	 */
	@ResponseBody
	@RequestMapping("table")
	public Rest uploadTableRecord(String json)
	{
		logger.info("Upload Data: " + json);
		return Rest.ok();
	}

	/**
	 * 解析数据
	 * 
	 * @param json
	 * @return
	 */
	protected List<Object> parseObjects(String json)
	{
		JSONObject object = JSON.parseObject(json);
		if(object == null)
		{
			return null;
		}
		String type = object.getString("type");
		if(StringUtils.isEmpty(type))
		{
			logger.info("Error, the type is not specified.");
			return null;
		}
		
		JSONArray array = object.getJSONArray("values");
		if(array == null)
		{
			logger.info("Error, the values are null.");
			return null;
		}
		
		return null;
	}
}
