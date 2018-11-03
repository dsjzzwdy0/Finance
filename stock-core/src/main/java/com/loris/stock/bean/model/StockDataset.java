package com.loris.stock.bean.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.loris.stock.bean.data.table.StockInfo;
import com.loris.stock.bean.item.DataItem;
import com.loris.stock.bean.item.StockDataItem;
import com.loris.stock.bean.item.interval.Interval;
import com.loris.stock.bean.item.util.DataHeader;

/**
 *
 * @author viorel.gheba
 */
public class StockDataset extends Dataset 
{
	private static final long serialVersionUID = 1L;
	
	/** The child DataSet value that analysis result. */
	protected Map<String, Dataset> childs = new Hashtable<String, Dataset>();
	
	
	/**
	 * Create a new instance of Dataset.
	 * 
	 * @param header
	 */
	public StockDataset()
	{
		this(null);
	}

	/**
	 * 
	 * @param stock
	 * @param interval
	 * @param list
	 */
	public StockDataset(List<DataItem> list)
	{
		this(list, null, null , true);
	}
	
	/**
	 * 
	 * @param stock
	 * @param interval
	 * @param list
	 * @param isSource
	 */
	public StockDataset(List<DataItem> list, StockInfo stock, Interval interval)
	{
		this(list, stock, interval, true);
	}
	
	/**
	 * 
	 * @param stock
	 * @param interval
	 * @param list
	 * @param isSource
	 */
	public StockDataset(List<DataItem> list, StockInfo stock, Interval interval, boolean isSource)
	{
		super(DataHeader.STOCKDATAHEADER);
		this.stock = stock;
		this.interval = interval;
		this.data = list;
		this.isSourceValue = isSource;
		this.parent = null;
	}
	
	
	/**
	 * 
	 * @param key
	 * @param dataset
	 */
	public void addDataset(String key, Dataset dataset)
	{
		dataset.setStock(stock);
		dataset.setInterval(interval);
		dataset.isSourceValue = false;
		childs.put(key, dataset);
	}
	
	/**
	 * Get the ChildDataset.
	 * @param key
	 * @return
	 */
	public Dataset getDataset(String key)
	{
		return childs.get(key);
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Dataset> getDatasets()
	{
		return childs;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getDatasetCount()
	{
		return childs.size();
	}
	
	/**
	 * 
	 * @return
	 */
	public double getDiffValue()
	{
		double lastClose = getCloseAt(getLastIndex());
		double prevClose = getCloseAt(getLastIndex() - 1);
		double diff = lastClose - prevClose;
		return diff;
	}
	
	/**
	 * 求第INDEX个值的涨幅
	 * @param index
	 * @return
	 */
	public double getDiffValue(int index)
	{
		if (index < 0 || index >= data.size())
		{
			return 0.0;
		}
		
		double v = 0.0;
		if(index == 0)
		{
			v = getCloseAt(index) - getOpenAt(index);
		}
		else
		{
			v = getCloseAt(index) - getCloseAt(index - 1);
		}
		return v;
	}

	/**
	 * 
	 * @return
	 */
	public double getPercentValue()
	{
		double lastClose = getCloseAt(getLastIndex());
		double prevClose = getCloseAt(getLastIndex() - 1);
		double percent = ((lastClose - prevClose) / prevClose) * 100;
		return percent;
	}

	
	/**
	 * 
	 * @param index
	 * @param indValue
	 * @return
	 */
	public boolean isRising(int index)
	{
		return isRising(index, DataHeader.CLOSE_PRICE);
	}

	/**
	 * 
	 * @return
	 */
	public double[] getOpenValues()
	{
		return getValues(DataHeader.OPEN_PRICE);
	}

	/**
	 * 
	 * @return
	 */
	public double[] getHighValues()
	{
		return getValues(DataHeader.HIGH_PRICE);
	}

	public double[] getLowValues()
	{
		return getValues(DataHeader.LOW_PRICE);
	}

	public double[] getCloseValues()
	{
		return getValues(DataHeader.CLOSE_PRICE);
	}

	public double[] getVolumeValues()
	{
		return getValues(DataHeader.VOLUME_PRICE);
	}

	public double getOpenAt(int index)
	{
		if (index < 0 || index >= data.size())
		{
			return 0;
		}
		return data.get(index) != null ? data.get(index).getValue(DataHeader.OPEN_PRICE) : 0;
	}

	public void setOpenAt(int index, double value)
	{
		if (index < 0 || index >= data.size())
		{
			return;
		}
		data.get(index).setValue(0, value);
	}

	public double getHighAt(int index)
	{
		if (index < 0 || index >= data.size())
		{
			return 0;
		}
		return data.get(index) != null ? data.get(index).getValue(DataHeader.HIGH_PRICE) : 0;
	}

	public void setHighAt(int index, double value)
	{
		if (index < 0 || index >= data.size())
		{
			return;
		}
		data.get(index).setValue(DataHeader.HIGH_PRICE, value);
	}

	public double getLowAt(int index)
	{
		if (index < 0 || index >= data.size())
		{
			return 0;
		}
		return data.get(index) != null ? data.get(index).getValue(DataHeader.LOW_PRICE) : 0;
	}

	public void setLowAt(int index, double value)
	{
		if (index < 0 || index >= data.size())
		{
			return;
		}
		data.get(index).setValue(DataHeader.LOW_PRICE, value);
	}

	public double getCloseAt(int index)
	{
		if (index < 0 || index >= data.size())
		{
			return 0;
		}
		return data.get(index) != null ? data.get(index).getValue(DataHeader.CLOSE_PRICE) : 0;
	}

	public void setCloseAt(int index, double value)
	{
		if (index < 0 || index >= data.size())
		{
			return;
		}
		data.get(index).setValue(DataHeader.CLOSE_PRICE, value);
	}

	public double getVolumeAt(int index)
	{
		if (index < 0 || index >= data.size())
		{
			return 0;
		}
		return data.get(index) != null ? data.get(index).getValue(DataHeader.VOLUME_PRICE) : 0;
	}

	public void setVolumeAt(int index, double value)
	{
		if (index < 0 || index >= data.size())
		{
			return;
		}
		data.get(index).setValue(DataHeader.VOLUME_PRICE, value);
	}

	public double getAmountAt(int index)
	{
		if (index < 0 || index >= data.size())
		{
			return 0;
		}
		return data.get(index) != null ? data.get(index).getValue(DataHeader.AMOUNT_PRICE) : 0;
	}

	public void setAmountAt(int index, double value)
	{
		if (index < 0 || index >= data.size())
		{
			return;
		}
		data.get(index).setValue(DataHeader.AMOUNT_PRICE, value);
	}

	public double getLastOpen()
	{
		return data.get(getLastIndex()) != null ? data.get(getLastIndex()).getValue(DataHeader.OPEN_PRICE) : 0;
	}

	public double getLastHigh()
	{
		return data.get(getLastIndex()) != null ? data.get(getLastIndex()).getValue(DataHeader.HIGH_PRICE) : 0;
	}

	public double getLastLow()
	{
		return data.get(getLastIndex()) != null ? data.get(getLastIndex()).getValue(DataHeader.LOW_PRICE) : 0;
	}

	public double getLastClose()
	{
		return data.get(getLastIndex()) != null ? data.get(getLastIndex()).getValue(DataHeader.CLOSE_PRICE) : 0;
	}

	public double getLastVolume()
	{
		return data.get(getLastIndex()) != null ? data.get(getLastIndex()).getValue(DataHeader.VOLUME_PRICE) : 0;
	}

	public double getPriceAt(int index, String price)
	{
		int p = header.getFieldIndex(price);
		return getPriceAt(index, p);
	}

	public double getPriceAt(int index, int price)
	{
		switch (price)
		{
		case DataHeader.OPEN_PRICE:
			return getOpenAt(index);
		case DataHeader.HIGH_PRICE:
			return getHighAt(index);
		case DataHeader.LOW_PRICE:
			return getLowAt(index);
		case DataHeader.CLOSE_PRICE:
			return getCloseAt(index);
		case DataHeader.VOLUME_PRICE:
			return getVolumeAt(index);
		}
		return 0;
	}

	public double getLastPrice(String price)
	{
		int p = header.getFieldIndex(price);
		return getLastPrice(p);
	}

	public double getLastPrice(int price)
	{
		switch (price)
		{
		case DataHeader.OPEN_PRICE:
			return getLastOpen();
		case DataHeader.HIGH_PRICE:
			return getLastHigh();
		case DataHeader.LOW_PRICE:
			return getLastLow();
		case DataHeader.CLOSE_PRICE:
			return getLastClose();
		case DataHeader.VOLUME_PRICE:
			return getLastVolume();
		}
		return 0;
	}

	public double getMin()
	{
		return getMin(DataHeader.LOW_PRICE);
	}


	public double getMinNotZero()
	{
		return getMinNotZero(DataHeader.LOW_PRICE);
	}

	
	public double getMax()
	{
		return getMax(DataHeader.HIGH_PRICE);
	}
	

	/**
	 * 
	 * @return
	 */
	public double getMaxNotZero()
	{
		return getMaxNotZero(DataHeader.HIGH_PRICE);
	}
	/**
	 * 
	 * @param period
	 * @param end
	 * @return
	 */
	@Override
	public Dataset getVisibleDataset(int period, int end)
	{
		return new StockDataset(getVisibleItems(period, end));
	}

	/**
	 * 
	 * @param count
	 * @return
	 */
	public static StockDataset EMPTY(int count)
	{
		List<DataItem> list = new ArrayList<DataItem>();

		for (int i = 0; i < count; i++)
		{
			list.add(i, null);
		}

		return new StockDataset(list);
	}
	
	/**
	 * 
	 * @param count
	 * @return
	 */
	public static StockDataset EMPTY(DataHeader header, int count)
	{
		List<DataItem> list = new ArrayList<DataItem>();

		for (int i = 0; i < count; i++)
		{
			list.add(i, null);
		}

		return new StockDataset(list);
	}

	public static StockDataset CONST(StockDataset d, double ct)
	{
		if (d == null)
		{
			return null;
		}

		int count = d.getItemsCount();
		StockDataset result = EMPTY(count);

		for (int i = 0; i < count; i++)
		{
			result.setDataItem(i, new DataItem(d.getTimeAt(i), ct));
		}

		return result;
	}

	public static StockDataset LOG(StockDataset d)
	{
		if (d == null)
		{
			return null;
		}

		int count = d.getItemsCount();
		StockDataset result = EMPTY(count);

		for (int i = 0; i < count; i++)
		{
			if (d.getDataItem(i) != null)
			{
				double open = Math.log10(d.getOpenAt(i));
				double high = Math.log10(d.getHighAt(i));
				double low = Math.log10(d.getLowAt(i));
				double close = Math.log10(d.getCloseAt(i));
				double volume = Math.log10(d.getVolumeAt(i));
				double amount = Math.log10(d.getAmountAt(i));

				result.setDataItem(i, new StockDataItem(d.getTimeAt(i), open, high, low, close, volume, amount));
			}
		}

		return result;
	}

	public static StockDataset SUM(StockDataset d1, StockDataset d2)
	{
		if (d1 == null || d2 == null)
		{
			return null;
		}

		int count = d1.getItemsCount();
		StockDataset result = EMPTY(count);

		for (int i = 0; i < count; i++)
		{
			result.setDataItem(i,
					new StockDataItem(d1.getTimeAt(i), d1.getOpenAt(i) + d2.getOpenAt(i), d1.getHighAt(i) + d2.getHighAt(i),
							d1.getLowAt(i) + d2.getLowAt(i), d1.getCloseAt(i) + d2.getCloseAt(i),
							d1.getVolumeAt(i) + d2.getVolumeAt(i), d1.getAmountAt(i) + d2.getAmountAt(i)));
		}

		return result;
	}

	public static StockDataset DIFF(StockDataset d1, StockDataset d2)
	{
		if (d1 == null || d2 == null)
		{
			return null;
		}

		int count = d1.getItemsCount();
		StockDataset result = EMPTY(count);

		for (int i = 0; i < count; i++)
		{
			result.setDataItem(i,
					new StockDataItem(d1.getTimeAt(i), d1.getOpenAt(i) - d2.getOpenAt(i), d1.getHighAt(i) - d2.getHighAt(i),
							d1.getLowAt(i) - d2.getLowAt(i), d1.getCloseAt(i) - d2.getCloseAt(i),
							d1.getVolumeAt(i) - d2.getVolumeAt(i), d1.getAmountAt(i) - d2.getAmountAt(i)));
		}

		return result;
	}

	public static StockDataset MULTIPLY(StockDataset d, double value)
	{
		if (d == null)
		{
			return null;
		}

		int count = d.getItemsCount();
		StockDataset result = EMPTY(count);

		for (int i = 0; i < count; i++)
		{
			result.setDataItem(i,
					new StockDataItem(d.getTimeAt(i), d.getOpenAt(i) * value, d.getHighAt(i) * value, d.getLowAt(i) * value,
							d.getCloseAt(i) * value, d.getVolumeAt(i) * value, d.getAmountAt(i) * value));
		}

		return result;
	}

	public static StockDataset DIV(StockDataset d, double value)
	{
		if (d == null)
		{
			return null;
		}

		int count = d.getItemsCount();
		StockDataset result = EMPTY(count);

		for (int i = 0; i < count; i++)
		{
			result.setDataItem(i,
					new StockDataItem(d.getTimeAt(i), d.getOpenAt(i) / value, d.getHighAt(i) / value, d.getLowAt(i) / value,
							d.getCloseAt(i) / value, d.getVolumeAt(i) / value, d.getAmountAt(i) / value));
		}

		return result;
	}

	public static StockDataset HMA(StockDataset dataset, int period)
	{
		if (dataset == null)
		{
			return null;
		}

		StockDataset wma_n2 = StockDataset.WMA(dataset, period / 2);
		StockDataset wma_n = StockDataset.WMA(dataset, period);
		StockDataset two_wma_n2 = StockDataset.MULTIPLY(wma_n2, 2);
		StockDataset diff = StockDataset.DIFF(two_wma_n2, wma_n);

		StockDataset result = StockDataset.WMA(diff, (int) Math.sqrt(period));

		return result;
	}

	public static StockDataset SMA(StockDataset dataset, int period)
	{
		if (dataset == null)
		{
			return null;
		}

		int count = dataset.getItemsCount();
		StockDataset result = StockDataset.EMPTY(count);

		int j = 0;
		for (j = 0; j < count && (dataset.getDataItem(j) == null); j++)
		{
			result.setDataItem(j, null);
		}

		for (int i = j + period - 1; i < count; i++)
		{
			long time = dataset.getTimeAt(i);
			double open = 0;
			double high = 0;
			double low = 0;
			double close = 0;
			double volume = 0;
			double amount = 0;

			for (int k = 0; k < period; k++)
			{
				open += dataset.getOpenAt(i - k);
				high += dataset.getHighAt(i - k);
				low += dataset.getLowAt(i - k);
				close += dataset.getCloseAt(i - k);
				volume += dataset.getVolumeAt(i - k);
				amount += dataset.getAmountAt(i - k);
			}

			open /= (double) period;
			high /= (double) period;
			low /= (double) period;
			close /= (double) period;
			volume /= (double) period;
			amount /= (double) period;

			result.setDataItem(i, new StockDataItem(time, open, high, low, close, volume, amount));
		}
		return result;
	}

	public static StockDataset EMA(StockDataset dataset, int period)
	{
		if (dataset == null)
		{
			return null;
		}

		int count = dataset.getItemsCount();
		StockDataset result = StockDataset.EMPTY(count);

		int j = 0;
		for (j = 0; j < count && (dataset.getDataItem(j) == null); j++)
		{
			result.setDataItem(j, null);
		}

		double open = 0;
		double high = 0;
		double low = 0;
		double close = 0;
		double volume = 0;
		double amount = 0;

		for (int i = j; i < period + j && i < count; i++)
		{
			open += dataset.getOpenAt(i);
			high += dataset.getHighAt(i);
			low += dataset.getLowAt(i);
			close += dataset.getCloseAt(i);
			volume += dataset.getVolumeAt(i);
			amount += dataset.getAmountAt(i);

			if (i == period + j - 1)
			{
				open /= (double) period;
				high /= (double) period;
				low /= (double) period;
				close /= (double) period;
				volume /= (double) period;

				result.setDataItem(i, new StockDataItem(dataset.getTimeAt(i), open, high, low, close, volume, amount));
			}
			else
			{
				result.setDataItem(i, null);
			}
		}

		double k = 2 / ((double) (period + 1));
		for (int i = period + j; i < dataset.getItemsCount(); i++)
		{
			open = (dataset.getOpenAt(i) - open) * k + open;
			high = (dataset.getHighAt(i) - high) * k + high;
			low = (dataset.getLowAt(i) - low) * k + low;
			close = (dataset.getCloseAt(i) - close) * k + close;
			volume = (dataset.getVolumeAt(i) - volume) * k + volume;
			amount = (dataset.getAmountAt(i) - amount) * k + amount;

			result.setDataItem(i, new StockDataItem(dataset.getTimeAt(i), open, high, low, close, volume, amount));
		}

		return result;
	}

	/*
	 * Wilder does not use the standard exponential moving average formula.
	 *
	 * Indicators affected are:
	 * 
	 * Average True Range
	 * Directional Movement System
	 * Relative Strength Index
	 * Twiggs Money Flow developed by Colin Twiggs using Wilder's moving average
	 * formula.
	 *
	 * http://www.incrediblecharts.com/indicators/wilder_moving_average.php
	 * http://user42.tuxfamily.org/chart/manual/Exponential-Moving-Average.html
	 */
	/*public static Dataset EMAWilder(Dataset dataset, int period)
	{
		int classic_ema_period = 2 * period - 1;
		return Dataset.EMA(dataset, classic_ema_period);
	}*/

	public static StockDataset WMA(StockDataset dataset, int period)
	{
		if (dataset == null)
		{
			return null;
		}

		int count = dataset.getItemsCount();
		StockDataset result = StockDataset.EMPTY(count);
		double denominator = ((double) period * ((double) period + 1)) / 2;

		int j = 0;
		for (j = 0; j < count && (dataset.getDataItem(j) == null); j++)
		{
			result.setDataItem(j, null);
		}

		for (int i = j; i < period + j; i++)
		{
			result.setDataItem(i, null);
		}

		for (int i = period + j; i < count; i++)
		{
			double open = 0;
			double high = 0;
			double low = 0;
			double close = 0;
			double volume = 0;
			double amount = 0;

			for (int k = i - period; k < i; k++)
			{
				open += (period - i + k + 1) * dataset.getOpenAt(k);
				high += (period - i + k + 1) * dataset.getHighAt(k);
				low += (period - i + k + 1) * dataset.getLowAt(k);
				close += (period - i + k + 1) * dataset.getCloseAt(k);
				volume += (period - i + k + 1) * dataset.getVolumeAt(k);
				amount += (period - i + k + 1) * dataset.getAmountAt(k);
			}

			open /= denominator;
			high /= denominator;
			low /= denominator;
			close /= denominator;
			volume /= denominator;

			result.setDataItem(i, new StockDataItem(dataset.getTimeAt(i), open, high, low, close, volume, amount));
		}

		return result;
	}

	/*
	public static Dataset TEMA(Dataset dataset, int period)
	{
		if (dataset == null)
		{
			return null;
		}

		int count = dataset.getItemsCount();
		Dataset result = Dataset.EMPTY(dataset.getItemsCount());
		Dataset ema1 = EMA(dataset, period);
		Dataset ema2 = EMA(ema1, period);
		Dataset ema3 = EMA(ema2, period);

		int j = 0;
		for (j = 0; j < count && (dataset.getDataItem(j) == null || ema1.getDataItem(j) == null
				|| ema2.getDataItem(j) == null || ema3.getDataItem(j) == null); j++)
		{
			result.setDataItem(j, null);
		}

		for (int i = j; i < count; i++)
		{
			double open = 0;
			double high = 0;
			double low = 0;
			double close = 0;
			double volume = 0;
			double amount = 0;

			if (ema1.getCloseAt(i) != 0 && ema2.getCloseAt(i) != 0 && ema3.getCloseAt(i) != 0)
			{
				open = 3 * ema1.getOpenAt(i) - 3 * ema2.getOpenAt(i) + ema3.getOpenAt(i);
				high = 3 * ema1.getHighAt(i) - 3 * ema2.getHighAt(i) + ema3.getHighAt(i);
				low = 3 * ema1.getLowAt(i) - 3 * ema2.getLowAt(i) + ema3.getLowAt(i);
				close = 3 * ema1.getCloseAt(i) - 3 * ema2.getCloseAt(i) + ema3.getCloseAt(i);
				volume = 3 * ema1.getVolumeAt(i) - 3 * ema2.getVolumeAt(i) + ema3.getVolumeAt(i);
				amount = 3 * ema1.getAmountAt(i) - 3 * ema2.getAmountAt(i) + ema3.getAmountAt(i);
			}

			result.setDataItem(i, new DataItem(dataset.getTimeAt(i), open, high, low, close, volume, amount));
		}

		return result;
	}

	public static Dataset[] ADX(Dataset dataset, int period)
	{
		if (dataset == null)
		{
			return null;
		}

		int count = dataset.getItemsCount();
		Dataset pdi = Dataset.EMPTY(count);
		Dataset mdi = Dataset.EMPTY(count);
		Dataset dx = Dataset.EMPTY(count);

		Dataset tr = Dataset.EMPTY(count);
		Dataset hmhp = Dataset.EMPTY(count);
		Dataset lmlp = Dataset.EMPTY(count);

		for (int i = 1; i < count; i++)
		{
			long time = dataset.getTimeAt(i);

			double tr0 = dataset.getHighAt(i) - dataset.getCloseAt(i - 1);
			double tr1 = Math.abs(dataset.getHighAt(i) - dataset.getCloseAt(i - 1));
			tr0 = Math.max(tr0, tr1);
			tr1 = Math.abs(dataset.getLowAt(i) - dataset.getCloseAt(i - 1));
			tr.setDataItem(i, new DataItem(time, Math.max(tr0, tr1)));

			if (dataset.getHighAt(i) <= dataset.getHighAt(i - 1) && dataset.getLowAt(i) < dataset.getLowAt(i - 1))
			{
				lmlp.setDataItem(i, new DataItem(time, dataset.getLowAt(i - 1) - dataset.getLowAt(i)));
			}
			else if (dataset.getHighAt(i) > dataset.getHighAt(i - 1) && dataset.getLowAt(i) >= dataset.getLowAt(i - 1))
			{
				hmhp.setDataItem(i, new DataItem(time, dataset.getHighAt(i) - dataset.getHighAt(i - 1)));
			}
			else
			{
				double tempH = Math.abs(dataset.getHighAt(i) - dataset.getHighAt(i - 1));
				double tempL = Math.abs(dataset.getLowAt(i) - dataset.getLowAt(i - 1));

				if (tempH > tempL)
				{
					hmhp.setDataItem(i, new DataItem(time, tempH));
				}
				else
				{
					lmlp.setDataItem(i, new DataItem(time, tempL));
				}
			}
		}

		Dataset strDS = Dataset.EMA(tr, period);
		Dataset shmhpDS = Dataset.EMA(hmhp, period);
		Dataset slmlpDS = Dataset.EMA(lmlp, period);

		for (int i = period; i < count; i++)
		{
			long time = dataset.getTimeAt(i);
			double curPDI = 0;
			double curMDI = 0;

			if (strDS.getDataItem(i) != null)
			{
				if (strDS.getCloseAt(i) != 0)
				{
					curPDI = (shmhpDS.getCloseAt(i) / strDS.getCloseAt(i)) * 100;
					curMDI = (slmlpDS.getCloseAt(i) / strDS.getCloseAt(i)) * 100;
				}
			}

			pdi.setDataItem(i, new DataItem(time, curPDI));
			mdi.setDataItem(i, new DataItem(time, curMDI));

			if (curPDI + curMDI != 0)
			{
				dx.setDataItem(i, new DataItem(time, (Math.abs(curPDI - curMDI) / (curPDI + curMDI) * 100)));
			}
		}

		Dataset adx = Dataset.EMA(dx, period);

		Dataset[] result = new Dataset[3];
		result[0] = pdi;
		result[1] = mdi;
		result[2] = adx;
		return result;
	}*/


}
