package com.loris.base.bean;

import java.io.Serializable;
import java.util.Map;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.loris.base.bean.entity.AutoIdEntity;

@TableName("loris_log")
public class Log extends AutoIdEntity implements Serializable
{
	/***/
	private static final long serialVersionUID = 1L;
	
    private String type;            	//日志类型  
    private String title;           	//日志标题  
    private String remoteaddr;          //请求地址  
    private String requesturi;          //URI   
    private String method;          	//请求方式  
    private String params;          	//提交参数  
    private String exception;           //异常    
    private String start;           	//开始时间  
    private String end;         		//结束时间  
    private String userid;          	//用户ID


	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getRemoteaddr()
	{
		return remoteaddr;
	}
	public void setRemoteaddr(String remoteaddr)
	{
		this.remoteaddr = remoteaddr;
	}
	public String getRequesturi()
	{
		return requesturi;
	}
	public void setRequesturi(String requesturi)
	{
		this.requesturi = requesturi;
	}
	public String getRequestUri()
	{
		return requesturi;
	}
	public void setRequestUri(String requestUri)
	{
		this.requesturi = requestUri;
	}
	public String getMethod()
	{
		return method;
	}
	public void setMethod(String method)
	{
		this.method = method;
	}
	public String getParams()
	{
		return params;
	}
	public void setParams(String params)
	{
		this.params = params;
	}
	public String getException()
	{
		return exception;
	}
	public void setException(String exception)
	{
		this.exception = exception;
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
	
    
	public String getUserid()
	{
		return userid;
	}
	public void setUserid(String userid)
	{
		this.userid = userid;
	}
	/**
     * 设置请求参数
     * @param paramMap
     */
    public void setMapToParams(Map<String, String[]> paramMap) {
        if (paramMap == null){
            return;
        }
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String[]> param : ((Map<String, String[]>)paramMap).entrySet()){
            params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            params.append(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue);
        }
        this.params = params.toString();
    }
}
