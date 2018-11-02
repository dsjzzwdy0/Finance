package com.loris.stock.bean.data.table.capital;

import com.loris.stock.bean.item.capital.CapitalFlowItem;

public class SimpleCapitalFlow extends AbstractCapitalFlow
{
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new instance of SimpleCapitalFlow.
	 */
	public SimpleCapitalFlow()
	{
		super(3);
		initCapitalFlow();
	}
	
	/**
	 * init the simple capital flow.
	 */
	private void initCapitalFlow()
	{
		CapitalFlowItem item;		
		item = getItem(0);
		item.setName("小单");
		item.setMinparam(0);
		item.setMaxparam(100);
		item = getItem(1);
		item.setName("中单");
		item.setMinparam(100);
		item.setMaxparam(500);
		item = getItem(2);
		item.setName("大单");
		item.setMinparam(500);
		item.setMaxparam(MAX_VALUE);
	}
}
