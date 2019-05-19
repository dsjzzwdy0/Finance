package com.loris.soccer.web.scheduler;

/**
 * 调度程序
 * 
 * @author jiean
 *
 */
public interface Scheduler extends Runnable
{
	/**
	 * 重置调度器
	 */
	void reset();
	
	/**
	 * 是否应该开始
	 * @return 是否需要开始的标志
	 */
	boolean shouldBegin();
}
