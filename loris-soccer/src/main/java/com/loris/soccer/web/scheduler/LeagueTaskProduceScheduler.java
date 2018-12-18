package com.loris.soccer.web.scheduler;

import com.loris.soccer.web.task.SoccerTask;

/**
 * 联赛比赛数据自动更新
 * 1、一天更新一次联赛类型数据(Zgzcw.center)：数据中心页面
 * 2、六个小时：更新一次联赛最新的页面数据
 * 
 * @author jiean
 *
 */
public class LeagueTaskProduceScheduler extends TaskProduceScheduler<SoccerTask>
{

	@Override
	public boolean initialize()
	{
		return false;
	}

	@Override
	public boolean produce()
	{
		return false;
	}

	@Override
	public boolean shouldBegin()
	{
		return false;
	}

}
