package com.loris.stock.bean.item.util;

/**
 * 
 * @author usr
 *
 */
public class DataField
{
	/** The name of the Field. */
	private String name;
	
	/** The field type. */
	private FieldType type;
	
	/**
	 * Create a new instance of name.
	 */
	public DataField()
	{		
	}
	
	/**
	 * 
	 * @param name
	 */
	public DataField(String name)
	{
		this(name, FieldType.DOUBLE);
	}
	
	/**
	 * 
	 * @param name
	 * @param type
	 */
	public DataField(String name, FieldType type)
	{
		this.name = name;
		this.type = type;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Set the field name.
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Get the FieldType.
	 * @return
	 */
	public FieldType getFieldType()
	{
		return type;
	}
	
	/**
	 * 
	 * @param type
	 */
	public void setFieldType(FieldType type)
	{
		this.type = type;
	}
}
