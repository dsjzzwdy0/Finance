package com.loris.stock.bean.model;

import java.util.HashMap;
import java.util.Map;

import com.loris.stock.bean.data.table.StockInfo;

public class CompanyIndustryInfo extends StockInfo
{
	private static final long serialVersionUID = 1L;	
	private Map<String, String> industries = new HashMap<String, String>();
	
	/**
	 * Add the industry info.
	 * 
	 * @param type
	 * @param value
	 */
	public void addIndustry(String type, String value)
	{
		industries.put(type, value);
	}
	
	/**
	 * Get the industry.
	 * @param type
	 */
	public String getIndustry(String type)
	{
		return industries.get(type);
	}
	
	/**
	 * Get the industries.
	 * 
	 * @return
	 */
	public Map<String, String> getIndustries()
	{
		return industries;
	}

	@Override
	public String toString()
	{
		return "CompanyIndustryInfo [symbol=" + symbol + ", industries=" + industries + "]";
	}
}
