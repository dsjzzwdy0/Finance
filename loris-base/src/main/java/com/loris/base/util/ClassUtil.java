package com.loris.base.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public interface ClassUtil
{
	/**
	 * Get the Super class field value.
	 * 
	 * @param fields
	 * @param clazz
	 */
	public static void getAllMethods(List<Method> fields, Class<?> clazz)
	{
		Method[] ms = clazz.getDeclaredMethods();
		for (Method method : ms)
		{
			//if(!method.getName().startsWith("get") || method.getName().startsWith("set"))
			//	continue;
			fields.add(method);
		}
		if (isBaseClass(clazz.getSuperclass().getName()))
		{
			return;
		}
		else
		{			
			getAllMethods(fields, clazz.getSuperclass());
		}
		
	}
	
	/**
	 * Get the Super class field value.
	 * 
	 * @param fields
	 * @param clazz
	 */
	public static void getAllFields(List<Field> fields, Class<?> clazz)
	{
		//System.out.println("Class Name: " + clazz.getName());
		Field[] fs = clazz.getDeclaredFields();
		for (Field field : fs)
		{
			fields.add(field);
		}
		if (isBaseClass(clazz.getSuperclass().getName()))
		{
			return;
		}
		else
		{			
			getAllFields(fields, clazz.getSuperclass());
		}
	}
	
	/**
	 * Check if the class is a base class.
	 * @param className
	 * @return
	 */
	public static boolean isBaseClass(String className)
	{
		if(className.startsWith("java.lang") || className.startsWith("java.util") ||
				className.startsWith("java.io"))
		{
			return true;
		}
		return false;
	}
}
