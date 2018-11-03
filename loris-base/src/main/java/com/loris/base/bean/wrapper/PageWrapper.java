package com.loris.base.bean.wrapper;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;

public class PageWrapper<T>
{
	private int total = 0;
	private int page = 0;
	private int size = 0;
	private List<T> rows = null;
	
	public PageWrapper()
	{		
	}
	
	public PageWrapper(Page<T> pageobj)
	{
		if(pageobj != null)
		{
			total = pageobj.getTotal();
			page = pageobj.getPages();
			size = pageobj.getSize();
			rows = pageobj.getRecords();
		}
	}
	
	/**
	 * Create a new instance of PageWrapper.
	 * 
	 * @param rows
	 */
	public PageWrapper(List<T> rows)
	{
		if(rows != null)
		{
			this.rows = rows;
			total = rows.size();
			size = rows.size();
			page = 1;
		}
	}
	
	/**
	 * Create a new PageWrapper.
	 * 
	 * @param rows
	 * @param total
	 * @param size
	 * @param pages
	 */
	public PageWrapper(List<T> rows, int total, int size, int page)
	{
		this.rows = rows;
		this.total = total;
		this.size = size;
		this.page = page;
	}
	
	public int getTotal()
	{
		return total;
	}
	public void setTotal(int total)
	{
		this.total = total;
	}
	public List<T> getRows()
	{
		return rows;
	}
	public void setRows(List<T> rows)
	{
		this.rows = rows;
	}
	public int getPage()
	{
		return page;
	}
	public void setPage(int page)
	{
		this.page = page;
	}
	public int getSize()
	{
		return size;
	}
	public void setSize(int size)
	{
		this.size = size;
	}
}