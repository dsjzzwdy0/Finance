package com.loris.lottery.aop;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.loris.base.repository.BasicManager;
import com.loris.lottery.context.ApplicationContextHelper;

public class BaseAspect
{
	private static Logger logger = Logger.getLogger(BaseAspect.class);
	
	protected long startTimeMillis = 0; // 开始时间
	protected long endTimeMillis = 0; // 结束时间
	
	protected static BasicManager basicManager;
	
	/**
	 * Create a new instance of BaseAspect.
	 */
	public BaseAspect()
	{
	}
	
	/**
	 * 获得基础数据类
	 */
	public void getBasicManager()
	{
		basicManager = ApplicationContextHelper.getApplicationContext().getBean(BasicManager.class);
	}
	
	/**
	 * 在切面方法执行之前调用的方法
	 * @Description: 方法调用前触发 记录开始时间
	 * @author fei.lei
	 * @date 2016年11月23日 下午5:10
	 * @param joinPoint
	 */
	public void before(JoinPoint joinPoint) throws IOException
	{
		// System.out.println("被拦截方法调用之后调用此方法，输出此语句");
		//request = getHttpServletRequest();
		// fileName 为例子
		//Object obj = request.getParameter("fileName");
		//System.out.println("方法调用前: " + obj);
		// user =
		// (User)request.getSession().getAttribute(SecurityConstant.CURRENT_LOGIN_USER);
		startTimeMillis = System.currentTimeMillis(); // 记录方法开始执行的时间
	}

	/**
	 * 
	 * @Description: 方法调用后触发 记录结束时间
	 * @author fei.lei
	 * @date 2016年11月23日 下午5:10
	 * @param joinPoint 结合点数据
	 */
	public void after(JoinPoint joinPoint)
	{
		endTimeMillis= System.currentTimeMillis();
	}
	
	/**
	 * 抛出异常时的处理方法
	 * @param e
	 * @throws Throwable
	 */
	public void afterThrowing(Exception e) throws Throwable 
	{
        logger.debug("Exception: " + e.toString());
    }
	

	/**
	 * 环绕触发方法
	 * @Title：around
	 * @Description: 环绕触发
	 * @author fei.lei
	 * @date 2016年11月23日 下午5:10
	 * @param joinPoint
	 * @return Object
	 * @throws Throwable
	 */
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable
	{
		return null;
	}
	
	/**
	 * @Description: 获取request
	 * @author fei.lei
	 * @date 2016年11月23日 下午5:10
	 * @param
	 * @return HttpServletRequest
	 */
	public HttpServletRequest getHttpServletRequest()
	{
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	/**
	 * @Description: 获取Response
	 * @author fei.lei
	 * @date 2016年11月23日 下午5:10
	 * @param
	 * @return HttpServletRequest
	 */
	public HttpServletResponse getHttpServletResponse()
	{
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
	}


	public long getStartTimeMillis()
	{
		return startTimeMillis;
	}


	public void setStartTimeMillis(long startTimeMillis)
	{
		this.startTimeMillis = startTimeMillis;
	}


	public long getEndTimeMillis()
	{
		return endTimeMillis;
	}


	public void setEndTimeMillis(long endTimeMillis)
	{
		this.endTimeMillis = endTimeMillis;
	}
}
