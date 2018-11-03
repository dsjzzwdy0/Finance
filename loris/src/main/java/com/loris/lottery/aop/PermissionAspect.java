package com.loris.lottery.aop;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.loris.base.bean.User;
import com.loris.lottery.util.WebConstants;

/**
 * 系统权限验证切面类
 * 
 * @author lin.r.x
 *
 */
@Aspect
@Component
public class PermissionAspect extends BaseAspect
{
	
	private User user;
	
	/**
	 * 在切面方法执行之前调用的方法
	 * @Description: 方法调用前触发 记录开始时间
	 * @author fei.lei
	 * @date 2016年11月23日 下午5:10
	 * @param joinPoint
	 */
	@Override
	public void before(JoinPoint joinPoint) throws IOException
	{
		//目标类的class形式
		String targetClass = joinPoint.getTarget().getClass().getName(); 
		//目标方法 
		String methodName = joinPoint.getSignature().getName();		
		HttpServletRequest request = getHttpServletRequest();
		
		user = (User)request.getSession().getAttribute(WebConstants.CURRENT_USER);		
		if(user != null)
		{
			System.out.println("User: " + user);
		}
		else
		{
			HttpServletResponse response = getHttpServletResponse();
			response.sendRedirect("../user/login?redirect=" + request.getRequestURI());
		}
		startTimeMillis = System.currentTimeMillis(); // 记录方法开始执行的时间		
		System.out.println("Permission: " + targetClass + " " + methodName + " " + startTimeMillis);		
	}
}
