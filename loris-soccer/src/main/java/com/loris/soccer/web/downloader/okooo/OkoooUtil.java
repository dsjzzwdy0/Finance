package com.loris.soccer.web.downloader.okooo;

import com.baomidou.mybatisplus.toolkit.StringUtils;

public class OkoooUtil
{
	/**
	 * 解析得到JSON字符串
	 * @param text 输入的字符串
	 * @return 正确的结果
	 */
	public static String getDataStr(String content, String splitStr, String field)
	{
		String[] strings = content.split(splitStr);
		for (String string : strings)
		{
			if(StringUtils.isNotEmpty(string))
			{
				string = string.trim();
				if(string.startsWith(field))
				{
					string = string.replace(field, "");
					string = string.replaceAll("=", "");
					string = string.replace("'", "");
					return string;
				}
			}
		}
				
		return "";
	}
}
