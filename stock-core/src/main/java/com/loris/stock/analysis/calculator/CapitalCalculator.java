package com.loris.stock.analysis.calculator;

import java.util.List;

import com.loris.stock.bean.data.table.capital.AbstractCapitalFlow;
import com.loris.stock.bean.item.DetailedItem;
import com.loris.stock.bean.item.capital.CapitalFlowItem;
import com.loris.stock.bean.model.StockDayDetailList;

public class CapitalCalculator
{

	/**
	 * Get current basic capital flow record.
	 * @param dataset
	 * @param outputFlow
	 * @return
	 */
	public static boolean calculateCapitalFlow(StockDayDetailList dataset, AbstractCapitalFlow outputFlow)
	{
		outputFlow.reset();
		outputFlow.setSymbol(dataset.getSymbol());
		outputFlow.setDay(dataset.getDate());

		List<DetailedItem> items = dataset.getRecords();
		List<CapitalFlowItem> capitalFlowItems = outputFlow.getFlowItems();
		for (DetailedItem item : items)
		{
			for (CapitalFlowItem flow : capitalFlowItems)
			{
				if (flow.contains(item.getVolume()))
				{
					if (DetailedItem.Type.BUY == item.getType())
					{
						flow.addBuyamount(item.getAmount());
					}
					else
					{
						// 在这里，卖盘、中性盘都当成卖盘计算
						flow.addSellamount(item.getAmount());
					}
				}
			}
		}

		return true;
	}
	
	/**
	 * Calculate the capital flow.
	 * @param inputFlow
	 * @param outputFlow
	 * @return
	 */
	public static boolean calculateCapitalFlow(AbstractCapitalFlow inputFlow, AbstractCapitalFlow outputFlow)
	{
		List<CapitalFlowItem> sourceItems = inputFlow.getFlowItems();
		List<CapitalFlowItem> destItems = outputFlow.getFlowItems();
		
		for (CapitalFlowItem sourceItem : sourceItems)
		{
			for (CapitalFlowItem destItem : destItems)
			{
				if(destItem.contains(sourceItem))
				{
					destItem.addFlowItemValue(sourceItem);
				}
			}
		}
		return true;
	}
}
