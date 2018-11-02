package com.loris.stock.bean.data.table.capital;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.loris.base.bean.entity.AutoIdEntity;
import com.loris.stock.bean.item.capital.CapitalFlowItem;

public abstract class AbstractCapitalFlow extends AutoIdEntity
{
	private static final long serialVersionUID = 1L;

	/** The max value. */
	public static final int MAX_VALUE = 10000000;
	
	private String symbol;
	private String day;
	
	/** The capital flow items. */
	@TableField(exist = false)
	private List<CapitalFlowItem> flows = new ArrayList<CapitalFlowItem>();
	
	/**
	 * Create a new instance of AbstractBasicCapitalFlow.
	 * @param size
	 */
	public AbstractCapitalFlow(int size)
	{
		for(int i = 0; i < size; i ++)
		{
			flows.add(new CapitalFlowItem());
		}
	}
	

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public String getDay()
	{
		return day;
	}

	public void setDay(String day)
	{
		this.day = day;
	}
	
	/**
	 * Get the flow items.
	 * @return
	 */
	public List<CapitalFlowItem> getFlowItems()
	{
		return flows;
	}
	
	/**
	 * Get the CapitalFlowItem
	 * @param index
	 * @return
	 */
	public CapitalFlowItem getItem(int index)
	{
		if(index >= size() || index < 0)
		{
			throw new IllegalArgumentException("Index value " + index + " overflow.");
		}
		return flows.get(index);
	}

	public double getBuyValue(int index)
	{
		return getItem(index).getBuyamount();
	}
	
	public double getSellValue(int index)
	{
		return getItem(index).getSellamount();
	}
	
	public void setBuyValue(int index, double v)
	{
		getItem(index).setBuyamount(v);
		//buyValues[index] = v;
	}
	
	public void setSellValue(int index, double v)
	{
		getItem(index).setSellamount(v);
		//sellValues[index] = v;
	}
	
	/**
	 * Reset the CapitalFlowItem.
	 */
	public void reset()
	{
		for (CapitalFlowItem capitalFlowItem : flows)
		{
			capitalFlowItem.reset();
		}
	}
	
	/**
	 * Get the min value.
	 * @param index
	 * @return
	 */
	public int getMinParam(int index)
	{
		if(index >= size() || index < 0)
		{
			throw new IllegalArgumentException("Index value " + index + " overflow.");
		}
		else
		{
			return getItem(index).getMinparam();
			//return maxValues[index - 1];
		}
	}
	
	/**
	 * Get the max value.
	 * @param index
	 * @return
	 */
	public int getMaxParam(int index)
	{
		if(index >= size() || index < 0)
		{
			throw new IllegalArgumentException("Index value " + index + " overflow.");
		}
		else 
		{
			return getItem(index).getMaxparam();
			//return maxValues[index];
		}
	}
	
	public double getTotal()
	{
		double total = 0.0;
		for (CapitalFlowItem flow : flows)
		{
			total += flow.getTotalamount();
		}
		return total;
	}
	
	public double getTotaldiff()
	{
		double totalDiff = 0.0;
		for (CapitalFlowItem flow : flows)
		{
			totalDiff += flow.getBuyamount() - flow.getSellamount();
		}
		return totalDiff;
	}
	
	/**
	 * Get the size of the CapitalFlow.
	 * @return
	 */
	public int size()
	{
		return flows.size();
	}
	
	/**
	 * To String value.
	 */
	@Override
	public String toString()
	{
		return "BaseCapitalFlow [id=" + id + ", symbol=" + symbol + ", day=" + day + ", flows=" + flows + "]";
	}
}
