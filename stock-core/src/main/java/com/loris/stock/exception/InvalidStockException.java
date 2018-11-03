package com.loris.stock.exception;

import com.loris.base.util.SerialVersion;

/**
 *
 * @author Viorel
 */
public class InvalidStockException extends Exception
{
	private static final long serialVersionUID = SerialVersion.APPVERSION;
	public InvalidStockException()
	{
		super("The symbol is invalid. Please enter a valid symbol.");
	}

	@Override public String toString()
	{
		return "The symbol is invalid. Please enter a valid symbol.";
	}

}
