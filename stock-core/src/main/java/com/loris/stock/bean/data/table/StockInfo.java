package com.loris.stock.bean.data.table;


import com.baomidou.mybatisplus.annotations.TableName;
import com.loris.base.bean.entity.AutoIdEntity;
import com.loris.base.util.SerialVersion;

/**
 *
 * @author viorel.gheba
 */
@TableName("stock_name")
public class StockInfo extends AutoIdEntity
{
	private static final long serialVersionUID = SerialVersion.APPVERSION;

	/** The Symbol of the Stock. */
	protected String symbol = "";
	
	/** The code of the stock. */
	protected String code = "";
	
	/** The name of the stock. */
	protected String name = "";
	
	/** The Exchange of the Stock. */	 
	private String exchange = "";

	/**
	 * Create a new instance of Stock.
	 */
	public StockInfo()
	{
	}

	/**
	 * Create a new instance of StockInfo.
	 * @param symbol
	 */
	public StockInfo(String symbol)
	{		
		this(symbol, "");
	}

	public StockInfo(String symbol, String exchange)
	{
		this();
		setSymbol(symbol);
		setExchange(exchange);
	}

	public void setSymbol(String symbol)
	{
		if(symbol == null)
			symbol = "";
		this.symbol = symbol;
		
		if(symbol.length() > 6)
		{
			setExchange(symbol.substring(0, 2));
		}
	}
	
	public String getCode()
	{
		return code;
	}
	
	public void setCode(String code)
	{
		this.code = code;
	}

	public String getSymbol()
	{
		return symbol;
	}
	
	public String getLabelText()
	{
		return name + " " + symbol;
	}

	public void setExchange(String exchange)
	{
		this.exchange = exchange;
	}

	public String getExchange()
	{
		return exchange;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public boolean hasName()
	{
		return name.hashCode() != "".hashCode();
	}

	public boolean isIndex()
	{
		return symbol.startsWith("$");
	}

	public boolean isFuture()
	{
		return symbol.endsWith(".F") || symbol.endsWith(".FD");
	}

	public String getSymbolRoot()
	{
		if (isFuture())
			return symbol.substring(0, symbol.length() - 2);
		else
			return symbol;
	}

	public String getKey()
	{
		return symbol + exchange;
	}

	public @Override boolean equals(Object obj)
	{
		if (obj == this)
			return true;

		if (!(obj instanceof StockInfo))
			return false;

		StockInfo that = (StockInfo) obj;

		if (!getSymbol().equals(that.getSymbol()))
			return false;
		if(!getCode().equals(that.getCode()))
			return false;
		if (!getExchange().equals(that.getExchange()))
			return false;
		if (!getName().equals(that.getName()))
			return false;

		return true;
	}

	public @Override int hashCode()
	{
		int hash = 7;
		hash = 47 * hash + (this.symbol != null ? this.symbol.hashCode() : 0);
		hash = 47 * hash + (this.exchange != null ? this.exchange.hashCode() : 0);
		// hash = 47 * hash + (this.companyName != null ?
		// this.companyName.hashCode() : 0);
		return hash;
	}

	public @Override String toString()
	{
		return symbol + "," + name + "," + exchange;
	}

}
