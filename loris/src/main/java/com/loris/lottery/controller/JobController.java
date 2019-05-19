/**  
 * All rights Reserved, Designed By www.loris.com
 * @Title:  JobController.java   
 * @Package com.loris.lottery.controller   
 * @Description: 本项目用于天津东方足彩数据的存储、共享、处理等   
 * @author: 东方足彩    
 * @date:   2019年1月28日 下午8:59:32   
 * @version V1.0.0
 * @Copyright: 2019 www.loris.com Inc. All rights reserved. 
 * 注意：本内容仅限于天津东方足彩有限公司传阅，禁止外泄以及用于其他的商业目
 */
package com.loris.lottery.controller;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.loris.lottery.job.BaseJob;
import com.loris.lottery.job.IssueDataDownloadJob;
import com.loris.lottery.job.LiveDataDownloadJob;

/**
 * @ClassName: JobController
 * @Description: 任务控制器
 * @author: 东方足彩
 * @date: 2019年1月28日 下午8:59:32
 * @Copyright: 2019 www.tydic.com Inc. All rights reserved.
 *             注意：本内容仅限于天津东方足彩有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/job")
public class JobController
{
	private static Logger logger = Logger.getLogger(JobController.class);
	
	public final static String JOB_GROUP_NAME = "SOCCER_JOB_NAME";
	
	private Scheduler scheduler;
	
	public JobController()
	{
		try
		{
			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
	        scheduler = schedulerFactory.getScheduler();//可以通过SchedulerFactory创建一个Scheduler实例

			// 启动调度器
			scheduler.start();
	        
	        addAndStart(IssueDataDownloadJob.class.getName(), JOB_GROUP_NAME, "0 10 12 * * ?");
	        addAndStart(LiveDataDownloadJob.class.getName(), JOB_GROUP_NAME, "0 50 17 * * ?");
		}
		catch(SchedulerException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@RequestMapping(value="/addjob")
	public void addjob(@RequestParam(value = "jobClassName") String jobClassName,
			@RequestParam(value = "jobGroupName") String jobGroupName,
			@RequestParam(value = "cronExpression") String cronExpression) throws Exception
	{
		addAndStart(jobClassName, jobGroupName, cronExpression);
	}

	public void addAndStart(String jobClassName, String jobGroupName, String cronExpression) throws Exception
	{
		// 构建job信息
		JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass())
				.withIdentity(jobClassName, jobGroupName).build();
		// 表达式调度构建器(即任务执行的时间)
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
		// 按新的cronExpression表达式构建一个新的trigger
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName)
				.withSchedule(scheduleBuilder).build();
		
		logger.info("Add job: " + jobClassName + " to Group: " + jobGroupName + ", cron: " + cronExpression);
		try
		{
			scheduler.scheduleJob(jobDetail, trigger);
		}
		catch (SchedulerException e)
		{
			logger.warn("创建定时任务失败：" + e);
			e.printStackTrace();
		}
	}

	public static BaseJob getClass(String classname) throws Exception
	{
		Class<?> class1 = Class.forName(classname);
		return (BaseJob) class1.newInstance();
	}
}
