package com.loris.base.web.util;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import com.loris.base.bean.web.WebPage;

/**
 * 优先序列管理器
 * @author jiean
 *
 */
public class PriorityPages
{
	/** 比较器 */
	protected static Comparator<WebPage> comparator = new Comparator<WebPage>()
	{
		@Override
		public int compare(WebPage o1, WebPage o2)
		{
			if(o1 == null && o2 != null) return -1;
			else if(o2 == null) return 1;
			else return o1.getPriority() - o2.getPriority();
		}
	};
	
	protected Queue<WebPage> pages = new PriorityQueue<>(comparator);
	
	/**
	 * Add the WebPage.
	 * @param page
	 */
	public void add(WebPage page)
	{
		pages.add(page);
	}
	
	public WebPage get()
	{
		return pages.poll();
	}
	
	public int size()
	{
		return pages.size();
	}
}
