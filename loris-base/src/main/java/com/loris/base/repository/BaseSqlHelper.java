package com.loris.base.repository;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.entity.Entity;
import com.loris.base.util.ReflectUtil;

public class BaseSqlHelper
{
	@Autowired
	protected JdbcTemplate jdbcTemplate;

	/** 批量数据 */
	protected int batchSize = 1000;

	/**
	 * 批量插入数据
	 * 
	 * @param entities
	 * @return 是否成功的标志
	 */
	public boolean insertBatch(List<? extends Entity> entities) throws SQLException
	{
		Class<? extends Entity> clazz = entities.get(0).getClass();
		TableName tableName = clazz.getAnnotation(TableName.class);
		if(tableName == null)
		{
			throw new SQLException("The " + clazz.getName() + " has no table name defined.");
		}
		
		List<Field> fields = new ArrayList<>();
		getEntitySQLFields(clazz, fields);
		if (fields.size() <= 0)
		{
			throw new SQLException("The " + clazz.getName() + " has no field defined.");
		}

		String sql = createSQL(tableName, fields);
		System.out.println(sql);

		Connection connection = null;
		PreparedStatement ps = null;

		try
		{
			connection = jdbcTemplate.getDataSource().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(sql);

			int i = 0;
			for (Entity entity : entities)
			{
				try
				{
					if(setFieldValue(entity, fields, ps))
					{
						ps.addBatch();
					}
					i++;
				}
				catch (IllegalAccessException e)
				{
				}
				if (i % batchSize == 0)
				{
					ps.executeBatch();
					connection.commit();
				}
			}

			if (i % batchSize != 0)
			{
				ps.executeBatch();
				connection.commit();
			}
			return true;
		}
		catch (SQLException e)
		{
			//e.printStackTrace();
			try
			{
				if (connection != null)
				{
					connection.rollback();
				}
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			throw e;
		}
		finally
		{
			if (ps != null)
			{
				ps.close();
			}
			if (connection != null)
			{
				connection.close();
			}
		}
	}

	/**
	 * 
	 * @param entity
	 * @param fields
	 * @param ps
	 */
	protected boolean setFieldValue(Entity entity, List<Field> fields, PreparedStatement ps) throws IllegalAccessException, SQLException
	{
		int index = 1;

		for (Field field : fields)
		{			
			String typeName = field.getType().getName(); 
			try { 
				switch (typeName) {
				case "java.lang.Boolean": 
				case "boolean": 
					Boolean b = field.getBoolean(entity);
					ps.setBoolean(index, b);
					break;
				case "bytes":
					byte[] bytes = (byte[])field.get(entity);
					ps.setBytes(index, bytes);
					break;
				case "java.lang.Character": 
				case "char": 
					break; 
				case "java.lang.Byte": 
				case "byte": 
					Byte b2 = field.getByte(entity); 
					ps.setByte(index, b2);
					break; 
				case "java.lang.Short": 
				case "short": 
					Short readShort = field.getShort(entity); 
					ps.setShort(index, readShort);
					break; 
				case "java.lang.Integer": 
				case "int": 
					Integer readInt = field.getInt(entity); 
					ps.setInt(index, readInt);
					break; 
				case "java.lang.Long": 
				case "long": 
					long l = field.getLong(entity);
					ps.setLong(index, l);
					break; 
				case "java.lang.Float": 
				case "float": 
					Float readFloat = field.getFloat(entity);
					ps.setFloat(index, readFloat);
					break;
				case "java.lang.Double": 
				case "double": 
					Double readDouble = field.getDouble(entity); 
					ps.setDouble(index, readDouble);
					break; 
				case "java.lang.String": 
					Object object = field.get(entity);
					String string = null;
					if(object != null)
					{
						string = object.toString();
					}
					ps.setString(index, string);
					break; 
				default: 
					throw new RuntimeException(typeName + "不支持，bug"); }
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			index ++;
		}
		return true;
	}
	
	/**
	 * 创建拼接插入数据的SQL语句
	 * @param tableName
	 * @param fields
	 * @return
	 */
	protected String createSQL(TableName tableName, List<Field> fields)
	{
		String sql = "insert into " + tableName.value() + " (";
		String values = " values(";
		
		int fieldIndex = 0;
		for (Field field : fields)
		{
			fieldIndex ++;
			sql += field.getName();
			values += "?";
			
			if(fieldIndex != fields.size())
			{
				sql += ", ";
				values += ", ";
			}			
		}		
		sql += ") " + values + ")";		
		return sql;
	}

	/**
	 * 获得实体所有的字段
	 * 
	 * @param clazz
	 * @param fields
	 */
	protected void getEntitySQLFields(Class<? extends Entity> clazz, List<Field> fields)
	{
		List<Field> allFields = ReflectUtil.getAllFields(clazz, false);
		for (Field field : allFields)
		{
			if(Modifier.isStatic(field.getModifiers()))
			{
				continue;
			}
			TableField tableField = field.getAnnotation(TableField.class);
			if (tableField == null || tableField.exist())
			{
				field.setAccessible(true);
				fields.add(field);
			}
		}
	}
}
