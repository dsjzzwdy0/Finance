package com.loris.base.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * 数据列表的使用类
 * @author jiean
 *
 */
public class ArraysUtil
{
	/**
	 * 数据检测器
	 * @author jiean
	 *
	 * @param <T>
	 */
	public interface EqualChecker<T>
	{
		boolean isSameObject(T obj);
	}
	
	/**
	 * 检测列表中是否存在相同的记录
	 * @param list 数据列表
	 * @param checker 数据检测器
	 * @return 是否有相同的记录
	 */
	public static<T> boolean hasSameObject(Collection<? extends T> list, EqualChecker<T> checker)
	{
		for (T t : list)
		{
			if(checker.isSameObject(t))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获得相同记录的数据
	 * @param list 数据列表
	 * @param checker 检测器
	 * @return 检测的记录
	 */
	public static<T> T getSameObject(Collection<? extends T> list, EqualChecker<T> checker)
	{
		for (T t : list)
		{
			if(checker.isSameObject(t))
			{
				return t;
			}
		}
		return null;
	}
	
	
	/**
	 * 按照数据检测器Checker和数据比较器，获得最大的值的记录
	 * @param list 数据记录
	 * @param checker 数据检测器
	 * @param comparator 比较器
	 * @return 返回最大的记录，如果不存在，则返回空值
	 */
	public static<T> T getLastObject(Collection<? extends T> list, EqualChecker<T> checker, Comparator<T> comparator)
	{
		T last = null;

		int r;		
		for (T tmp : list)
		{
			if(!checker.isSameObject(tmp))
			{
				continue;
			}
			
			if(last == null)
			{
				last = tmp;
				continue;
			}
			r = comparator.compare(last, tmp);
			if(r < 0)
			{
				last = tmp;
			}
		}
		return last;
	}
	
	
	/**
	 * 查找列表中是否存在相同的值
	 * @param list 数据列表值
	 * @param object 需要比较的值 
	 * @param comparator 比较类
	 * @return 返回是否有相同的值
	 */
	public static<T> boolean hasSameObject(Collection<? extends T> list, T object, Comparator<T> comparator)
	{
		int index = getSameObjectIndex(list, object, comparator);
		return (index > -1);
	}
	
	/**
	 * 查找列表中是否存在相同的值，如果存在，则返回该值的序号
	 * @param list 数据列表值
	 * @param object 需要比较的值 
	 * @param comparator 比较类
	 * @return 返回是否有相同的值
	 */
	public static<T> int getSameObjectIndex(Collection<? extends T> list, T object, Comparator<T> comparator)
	{
		int i = 0;		
		for (T t : list)
		{
			if(t == object)
			{
				return i;
			}
			int r = comparator.compare(t, object);
			if(r == 0)
			{
				return i;
			}
			i ++;
		}
		return -1;
	}
	
	/**
	 * 按照某一种方式获得列表数据
	 * @param sources 源数据
	 * @param dests 目标数据
	 * @param checker 数据检测器
	 */
	public static<T> int getListValues(Collection<? extends T> sources, List<T> dests, EqualChecker<T> checker)
	{
		for (T t : sources)
		{
			if(checker.isSameObject(t))
			{
				dests.add(t);
			}
		}
		return dests.size();
	}
	
	/**
	 * 获得源列表中数据的某一个字段的集合数据
	 * @param source 源列表
	 * @param dest 目标值
	 * @param fieldName 字段名称
	 */
	@SuppressWarnings("unchecked")
	public static<T, P> void getObjectFieldValue(Collection<P> source, List<T> dest, Class<?> clazz, String fieldName)
	{
		if(source == null || source.size() == 0)
		{
			return;
		}
		Method method = getField(clazz, fieldName);
		if(method == null)
		{
			throw new UnsupportedOperationException("There are no field of name '" + fieldName + "'.");
		}
		
		for (P p : source)
		{
			try
			{
				Object t = method.invoke(p);
				dest.add((T)t);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获得源列表中数据的某一个字段的集合数据
	 * @param source 源列表
	 * @param dest 目标值
	 * @param fieldName 字段名称
	 */
	public static<T, P> List<T> getObjectFieldValue(Collection<P> source, Class<?> clazz, String fieldName)
	{
		List<T> list = new ArrayList<>();
		getObjectFieldValue(source, list, clazz, fieldName);
		return list;
	}
	
	/**
	 * 获得某一个类的属性字段
	 * @param clazz 类
	 * @param fieldName 属性名称
	 * @return 字段
	 */
	protected static Method getField(Class<?> clazz, String fieldName)
	{
		Method[] methods = clazz.getMethods();
		String fname = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
		String name;
		for (Method method : methods)
		{
			name = method.getName();
			if(name.equals(fieldName) || name.equals(fname))
			{
				return method;
			}
		}
		return null;
	}
}
