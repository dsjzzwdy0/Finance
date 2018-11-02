package com.loris.base.context;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public interface LorisContext
{
	/**
	 * Get the LorisContext environment.
	 * 
	 * @return
	 */
	ApplicationContext getApplicationContext();
	
	/**
	 * 获得数据库的连接.
	 * 
	 * @return
	 */
	Connection getConnection() throws SQLException;
	
	/**
	 * Get the bean.
	 * 
	 * @param name
	 * @return
	 * @throws BeansException
	 */
	Object getBean(String name) throws BeansException;
	
	
	/**
	 * Get the BeanObject.
	 * @param clazz Class type.
	 * @return Class object value.
	 * @throws BeansException
	 */
	<T> T getBean(Class<? extends T> clazz) throws BeansException;
	
	/**
	 * get the bean.
	 * 
	 * @param name
	 * @param requiredType
	 * @return
	 * @throws BeansException
	 */
	Object getBean(String name, Class<? extends Object> requiredType) throws BeansException;
}
