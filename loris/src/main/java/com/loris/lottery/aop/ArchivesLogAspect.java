package com.loris.lottery.aop;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.loris.base.bean.Log;
import com.loris.base.bean.User;
import com.loris.base.util.DateUtil;
import com.loris.base.util.StringUtil;
import com.loris.lottery.util.WebConstants;


/**
 * 系统日志切面类
 * 
 * @author lin.r.x
 *
 */
@Aspect
@Component
public class ArchivesLogAspect extends BaseAspect
{
	private static Logger logger = Logger.getLogger(ArchivesLogAspect.class);
	
	protected int maxFieldLen = 100;

	
	/**
	 * 方法调用后触发 记录结束时间
	 * @Description: 方法调用后触发 记录结束时间
	 * @author fei.lei
	 * @date 2016年11月23日 下午5:10
	 * @param joinPoint 结合点数据
	 */
	@Override
	public void after(JoinPoint joinPoint)
	{
		try
		{
			HttpServletRequest request = getHttpServletRequest();
			String targetName = joinPoint.getTarget().getClass().getName();
			String methodName = joinPoint.getSignature().getName();
			Object[] arguments = joinPoint.getArgs();
			User user = (User)request.getSession().getAttribute(WebConstants.CURRENT_USER);
			
			endTimeMillis = System.currentTimeMillis();
			String startTime = DateUtil.formatDate(startTimeMillis);	// 格式化开始时间
			String endTime = DateUtil.formatDate(endTimeMillis);		// 格式化结束时间
			String requesturi = request.getRequestURI();
			String parameters = "";
			
			if(arguments != null && arguments.length != 0)
			{
				parameters = Arrays.toString(arguments);
			}
						
			Log log = new Log();
			log.setType(targetName);
			log.setTitle(methodName);
			log.setMethod(request.getMethod());
			log.setStart(startTime);
			log.setEnd(endTime);
			log.setRemoteaddr(request.getRemoteAddr());
			log.setRequesturi(requesturi);
			log.setUserid((user == null) ? "anonymous" : user.getUsername());
			log.setParams(StringUtil.getMaxStringValue(parameters, maxFieldLen));
		
			if(basicManager == null)
			{
				getBasicManager();
			}
			basicManager.addLog(log);
		}
		catch(Exception e)
		{
			logger.info(e.toString());
		}
	}
}
