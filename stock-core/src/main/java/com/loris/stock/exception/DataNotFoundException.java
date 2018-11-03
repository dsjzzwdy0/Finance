package com.loris.stock.exception;

import com.loris.base.util.SerialVersion;

/**
 *
 * @author Viorel
 */
public class DataNotFoundException extends Exception
{
	private static final long serialVersionUID = SerialVersion.APPVERSION;
	private String message;

	public DataNotFoundException(String message)
	{
		super(message);
		this.message = message;
	}

	@Override public String toString()
	{
		return this.message;
	}

}
