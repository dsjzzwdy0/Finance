package com.loris.base.context;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public abstract class AbstractLorisContext implements LorisContext
{
	/** The ApplicationContext instance. */
	private ApplicationContext applicationContext;
	
	
	protected AbstractLorisContext(ApplicationContext context)
	{
		applicationContext = context;
	}
	
	/**
	 * Set the ApplicationContext.
	 * 
	 * @param context
	 */
	public void setApplicationContext(ApplicationContext context)
	{
		applicationContext = context;
	}
	

	public ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}

	/**
	 * Get the DataSource.
	 * 
	 * @param name
	 * @return
	 */
	public DriverManagerDataSource getDataSource(String name) throws SQLException
	{
		return (DriverManagerDataSource) applicationContext.getBean("dataSource");
	}

	/**
	 * Get the connection.
	 * 
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection(String name) throws SQLException
	{
		DriverManagerDataSource dataSource = getDataSource(name);
		if (dataSource == null)
		{
			throw new SQLException("DataSource '" + name + "' is not exist, check the config file.");
		}
		return dataSource.getConnection();
	}

	/**
	 * 获取对象
	 * 
	 * @param name
	 * @return Object 一个以所给名字注册的bean的实例
	 * @throws BeansException
	 */
	public Object getBean(String name) throws BeansException
	{
		return applicationContext.getBean(name);
	}
	
	/**
	 * Get the BeanObject.
	 * @param clazz Class type.
	 * @return Class object value.
	 * @throws BeansException
	 */
	public <T> T getBean(Class<? extends T> clazz) throws BeansException
	{
		return applicationContext.getBean(clazz);
	}

	/**
	 * 获取类型为requiredType的对象
	 * 如果bean不能被类型转换，相应的异常将会被抛出（BeanNotOfRequiredTypeException）
	 * 
	 * @param name
	 *            bean注册名
	 * @param requiredType
	 *            返回对象类型
	 * @return Object 返回requiredType类型对象
	 * @throws BeansException
	 */
	public Object getBean(String name, Class<? extends Object> requiredType) throws BeansException
	{
		return applicationContext.getBean(name, requiredType);
	}

	/**
	 * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
	 * 
	 * @param name
	 * @return boolean
	 */
	public boolean containsBean(String name)
	{
		return applicationContext.containsBean(name);
	}

	/**
	 * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。
	 * 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
	 * 
	 * @param name
	 * @return boolean
	 * @throws NoSuchBeanDefinitionException
	 */
	public boolean isSingleton(String name) throws NoSuchBeanDefinitionException
	{
		return applicationContext.isSingleton(name);
	}

	/**
	 * @param name
	 * @return Class 注册对象的类型
	 * @throws NoSuchBeanDefinitionException
	 */
	public Class<?> getType(String name) throws NoSuchBeanDefinitionException
	{
		return applicationContext.getType(name);
	}

	/**
	 * 如果给定的bean名字在bean定义中有别名，则返回这些别名
	 * 
	 * @param name
	 * @return
	 * @throws NoSuchBeanDefinitionException
	 */
	public String[] getAliases(String name) throws NoSuchBeanDefinitionException
	{
		return applicationContext.getAliases(name);
	}

}
