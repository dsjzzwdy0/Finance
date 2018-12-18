package com.loris.soccer.repository;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.context.ApplicationContext;

import com.loris.base.context.AbstractLorisContext;


public class SoccerContext extends AbstractLorisContext
{	
	/**
	 * Create a new instance of SoccerContext.
	 */
	public SoccerContext()
	{
		super(null);
	}
	
	/**
	 * Create a new instance of ApplicationContext.
	 * @param context
	 */
	public SoccerContext(ApplicationContext context)
	{
		super(context);
	}

	/**
	 * Get the Database Connection.
	 * @return
	 */
	public Connection getConnection() throws SQLException
	{
		return getConnection("dataSource");
	}
}
