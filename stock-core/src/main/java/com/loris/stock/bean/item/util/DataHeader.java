package com.loris.stock.bean.item.util;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * 
 * @author usr
 *
 */
public class DataHeader
{
	/** Stock Header information. */
	public static final int OPEN_PRICE = 0;
	public static final int HIGH_PRICE = 1;
	public static final int LOW_PRICE = 2;
	public static final int CLOSE_PRICE = 3;
	public static final int VOLUME_PRICE = 4;
	public static final int AMOUNT_PRICE = 5;
	
	public static final String TIME = "Time";
	public static final String OPEN = "Open";
	public static final String HIGH = "High";
	public static final String LOW = "Low";
	public static final String CLOSE = "Close";
	public static final String VOLUME = "Volume";
	public static final String AMOUNT = "Amount";
	
	/** The DataField list. */
	private List<DataField> fields = new ArrayList<DataField>();

	/** The Stock Data Header. */
	public static DataHeader STOCKDATAHEADER = new StockDataHeader();
	
	/**
	 * 
	 * @return
	 */
	public static DataHeader createDataHeader()
	{
		DataHeader header = new DataHeader();
		header.addField(new DataField(TIME, FieldType.LONG));
		return header;
	}
	
	public DataHeader()
	{
	}
	
	/**
	 * Create a new instance of DataHeader with fields.
	 * @param fields
	 */
	public DataHeader(List<DataField> fields)
	{
		for(DataField field : fields)
		{
			fields.add(field);
		}
	}
	
	/**
	 * The field number.
	 * @return
	 */
	public int getFieldNum()
	{
		return fields.size();
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public DataField getDataField(int index)
	{
		return fields.get(index);
	}
	
	/**
	 * Add the DataField.
	 * @param field
	 */
	public void addField(DataField field)
	{
		fields.add(field);
	}
	
	/**
	 * return fields.
	 * @return
	 */
	public List<DataField> getFields()
	{
		return fields;
	}
	
	/**
	 * 
	 * @param price
	 * @return
	 */
	public int getFieldIndex(String price)
	{
		int size = fields.size();
		for(int i = 0; i < size; i ++)
		{
			if(price.equalsIgnoreCase(fields.get(i).getName()))
				return i;
		}
		return -1;
	}
	
	/**
	 * Get the field names.
	 * @return
	 */
	public String[] getFieldNames()
	{
		int size = fields.size();
		String fieldNames[] = new String[size];
		for(int i = 0; i < size; i ++)
		{
			fieldNames[i] = fields.get(i).getName();
		}
		return fieldNames;
	}
	
	/**
	 * 
	 * @return
	 */
	public static DataHeader getStockDataHeader()
	{
		return STOCKDATAHEADER;
	}
	
	/**
	 * 
	 * @author usr
	 *
	 */
	private static class StockDataHeader extends DataHeader
	{
		StockDataHeader()
		{
			addField(new DataField(TIME, FieldType.LONG));
			addField(new DataField(OPEN, FieldType.DOUBLE));
			addField(new DataField(HIGH, FieldType.DOUBLE));
			addField(new DataField(LOW, FieldType.DOUBLE));
			addField(new DataField(CLOSE, FieldType.DOUBLE));
			addField(new DataField(VOLUME, FieldType.DOUBLE));
			addField(new DataField(AMOUNT, FieldType.DOUBLE));
		}
	}
	
}
