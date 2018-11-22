package com.loris.base.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectUtil
{
	/**
	 * 获取所有的方法
	 * @param clazz
	 * @return
	 */
	public static List<Method> getAllMethods(Class<?> clazz, boolean containsObject)
	{
		List<Method> methods = new ArrayList<>();
		Class<?> tmpClazz = clazz;
		while(tmpClazz != null)
		{
			if(!containsObject && tmpClazz.getName().toLowerCase().equals("java.lang.object"))
			{
				break;
			}
			methods.addAll(Arrays.asList(tmpClazz.getDeclaredMethods()));
			tmpClazz = tmpClazz.getSuperclass(); //得到父类,然后赋给自己
		}
		return methods;
	}
	
	/**
	 * 获得所声明的字段值
	 * @param clazz
	 * @param containsObject
	 * @return
	 */
	public static List<Field> getAllFields(Class<?> clazz, boolean containsObject)
	{
		List<Field> fields = new ArrayList<>();
		Class<?> tmpClazz = clazz;
		while(tmpClazz != null)
		{
			if(!containsObject && tmpClazz.getName().toLowerCase().equals("java.lang.object"))
			{
				break;
			}
			fields.addAll(Arrays.asList(tmpClazz.getDeclaredFields()));
			tmpClazz = tmpClazz.getSuperclass(); //得到父类,然后赋给自己
		}
		return fields;
	}
}
