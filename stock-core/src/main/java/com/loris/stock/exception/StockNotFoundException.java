package com.loris.stock.exception;

import com.loris.base.util.SerialVersion;

/**
 *
 * @author Viorel
 */
public class StockNotFoundException extends Exception
{
	private static final long serialVersionUID = SerialVersion.APPVERSION;
	public StockNotFoundException()
	{
		super("Stock not found.");
	}
	
	public StockNotFoundException(String info)
	{
		super(info);
	}

	
	@Override public String toString()
	{
		return "Stock not found.";
	}

}
