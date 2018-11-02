package com.loris.base.context;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.context.ApplicationContext;

public class DefaultLorisContext extends AbstractLorisContext
{
	/** The DataSource bean default name. */
	private String dataSourceName = "dataSource";
	
	/**
	 * Create a new instance of DefaultLorisContext.
	 * 
	 * @param context
	 */
	public DefaultLorisContext(ApplicationContext context)
	{
		super(context);
	}
	
	/**
	 * Get the Connection.
	 * 
	 */
	@Override
	public Connection getConnection() throws SQLException
	{
		return getConnection(dataSourceName);
	}

	/**
	 * Get data source name.
	 * 
	 * @return
	 */
	public String getDataSourceName()
	{
		return dataSourceName;
	}

	/**
	 * Set the data source name.
	 * 
	 * @param dataSourceName
	 */
	public void setDataSourceName(String dataSourceName)
	{
		this.dataSourceName = dataSourceName;
	}
}
