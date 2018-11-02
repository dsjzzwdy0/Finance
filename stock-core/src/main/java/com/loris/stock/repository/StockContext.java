package com.loris.stock.repository;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.context.ApplicationContext;

import com.loris.base.context.AbstractLorisContext;

public class StockContext extends AbstractLorisContext
{
	
	public StockContext(ApplicationContext context)
	{
		super(context);
	}
	
	/**
	 * Get the Connection.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException
	{
		return getConnection("dataSource");
	}
}
