package com.loris.stock.analysis.calculator;

import com.loris.stock.analysis.talib.Core;
import com.loris.stock.analysis.talib.MAType;
import com.loris.stock.analysis.talib.MInteger;
import com.loris.stock.analysis.talib.TaLibUtilities;
import com.loris.stock.bean.item.DataItem;
import com.loris.stock.bean.item.util.DataField;
import com.loris.stock.bean.item.util.DataHeader;
import com.loris.stock.bean.model.Dataset;
import com.loris.stock.bean.model.StockDataset;

/**
 * 股票指标计算：
 * 1、MACD指标：
 * 2、KDJ指标：
 * @author usr
 *
 */
public class DatasetIndicator
{
	/**
	 * 数据集
	 */
	private StockDataset dataset = null;
	
	/** Default predefined header info. */
	public final static DataHeader HEADER_DEMA = DataHeader.createDataHeader(); 
	public final static DataHeader HEADER_MACD = DataHeader.createDataHeader();
	public final static DataHeader HEADER_KDJ = DataHeader.createDataHeader();
	
	/** The Core library. */
	private static Core core = new Core(); 
	
	/**
	 * Create a new instance of DatasetIndicator
	 */
	public DatasetIndicator()
	{
		HEADER_DEMA.addField(new DataField("dema", com.loris.stock.bean.item.util.FieldType.DOUBLE));
		
		HEADER_MACD.addField(new DataField("macd"));
		HEADER_MACD.addField(new DataField("signal"));
		HEADER_MACD.addField(new DataField("histogram"));
		
		HEADER_KDJ.addField(new DataField("K"));
		HEADER_KDJ.addField(new DataField("D"));
		HEADER_KDJ.addField(new DataField("J"));
	}
	
	/**
	 * Create a new instance of StockDataset.
	 * @param dataset
	 */
	public DatasetIndicator(StockDataset dataset)
	{
		this();
		this.dataset = dataset;
	}
	
	/**
	 * Set StockDataset.
	 * @param dataset
	 */
	public void setStockDataset(StockDataset dataset)
	{
		this.dataset = dataset;
	}
	
	/**
	 * Get the StockDataset.
	 * @return
	 */
	public StockDataset getStockDataset()
	{
		return dataset;
	}
	
	/**
	 * @param fast
	 * @param slow
	 * @param smooth
	 * @return
	 */
	public Dataset MACD(int fast, int slow, int smooth)
	{
		if(dataset == null)
		{
			throw new java.lang.IllegalArgumentException("No dataset to be set.");
		}
		
		//计算MACD指标
		StockDataset fastEMA = StockDataset.EMA(dataset, fast);
        StockDataset slowEMA = StockDataset.EMA(dataset, slow);
        
        int count = fastEMA.getItemsCount();
        Dataset macd = new Dataset(HEADER_MACD);
        DataItem item = null;

        for (int i = slow; i < count; i++)
        {
            double diff = fastEMA.getCloseAt(i) - slowEMA.getCloseAt(i);
            item = new DataItem(HEADER_MACD);
            item.setTime(fastEMA.getTimeAt(i));
            item.setValue(0, diff);
            macd.addDataItem(item);            
            //new DataItem(fastEMA.getTimeAt(i), diff)
        }
        
        //计算指示器信息
        double close = 0;
        for (int i = slow; i < slow + smooth; i++)
        {
            close += macd.getDataItem(i).getValue(0);
        }

        close /= smooth;
        for (int i = (slow + smooth); i < count; i++)
        {
        	item = macd.getDataItem(i);
        	if(item == null)
        		continue;
            double close2 = (2 * (item.getValue(0) - close)) / (1 + smooth) + close;
            item.setValue(1, close);
            //result.setDataItem(i, new DataItem(MACD.getTimeAt(i), close));
            close = close2;
        }
        
        //计算柱状数据
        for (int i = 0; i < count; i++)
        {
        	item = macd.getDataItem(i);
        	if(item == null)
        		continue;
        	double diff = item.getValue(0) - item.getValue(1);
        	item.setValue(2, diff);
            /*if (signal.getDataItem(i) != null && MACD.getDataItem(i) != null)
            {
                double diff = MACD.getCloseAt(i) - signal.getCloseAt(i);
                result.setDataItem(i, new DataItem(MACD.getTimeAt(i), diff));
            }*/
        }
        
		return macd;
	}
	
	/**
	 * P
	 * 
	 * @param dataset
	 * @param period DEMA calculate variable.
	 * @return
	 */
	public Dataset DEMA(int period)
	{
		if(dataset == null)
		{
			throw new java.lang.IllegalArgumentException("No dataset to be set.");
		}
		
		//variables for TA-Lib utilization
	    int lookback;
	    double[] output;
	    MInteger outBegIdx;
	    MInteger outNbElement;
		int count = 0;
        if (dataset != null && !dataset.isEmpty())
        {
            count = dataset.getItemsCount();
        }
        /**
         * *******************************************************************
         */
        //This entire method is basically a copy/paste action into your own
        //code. The only thing you have to change is the next few lines of code.
        //Choose the 'lookback' method and appropriate 'calculation function'
        //from TA-Lib for your needs. You'll also need to ensure you gather
        //everything for your calculation as well. Everything else should stay
        //basically the same
        //prepare ta-lib variables
        output = new double[count];
        outBegIdx = new MInteger();
        outNbElement = new MInteger();

        //now do the calculation over the entire dataset
        //[First, perform the lookback call if one exists]
        //[Second, do the calculation call from TA-lib]
        lookback = core.movingAverageLookback(period, MAType.Dema);
        core.dema(0, count - 1, dataset.getCloseValues(), period, outBegIdx, outNbElement, output);

        //Everything between the /***/ lines is what needs to be changed.
        //Everything else remains the same. You are done with your part now.
        /**
         * *******************************************************************
         */
        //fix the output array's structure. TA-Lib does NOT match
        //indicator index and dataset index automatically. That's what
        //this function does for us.
        output = TaLibUtilities.fixOutputArray(output, lookback);

        Dataset calculatedDataset = new Dataset(HEADER_DEMA);
        for (int i = 0; i < output.length; i++)
        {
        	calculatedDataset.addDataItem(new DataItem(dataset.getTimeAt(i), output[i]));
            //calculatedDataset.setDataItem(i, new DataItem(dataset.getTimeAt(i), output[i]));
        }
        return calculatedDataset;
	}
	
	/**
	 * 
	 * @param periodK
	 * @param periodD
	 * @param periodSlowD
	 */
	public Dataset KDJ(int periodK, int periodD, int periodSlowD)
    {
		if(dataset == null || dataset.isEmpty())
		{
			throw new java.lang.IllegalArgumentException("No dataset to be set.");
		}
		
        int count = dataset.getItemsCount();

        /**********************************************************************/
        //This entire method is basically a copy/paste action into your own
        //code. The only thing you have to change is the next few lines of code.
        //Choose the 'lookback' method and appropriate 'calculation function'
        //from TA-Lib for your needs. Everything else should stay basically the
        //same

        //prepare ta-lib variables
        double[] outputFastD = new double[count];
        double[] outputFastK = new double[count];
        MInteger outBegIdx = new MInteger();
        MInteger outNbElement = new MInteger();

        //[your specific indicator variables need to be set first]
        //periodK = properties.getPeriodK();
        //periodD = properties.getPeriodD();
        //periodSlowD = properties.getSmooth();//actually this is the Slow%D in the indicator!

        double[] allHighs = dataset.getHighValues();
        double[] allLows = dataset.getLowValues();
        double[] allClose = dataset.getCloseValues();

        //now do the calculation over the entire dataset
        //[First, perform the lookback call if one exists]
        //[Second, do the calculation call from TA-lib]

        int lookback = core.stochLookback(periodK, periodD, MAType.Sma, periodSlowD, MAType.Sma);
        core.stoch(0, count-1, allHighs, allLows, allClose, periodK, periodD, MAType.Sma, periodSlowD, MAType.Sma, 
        		outBegIdx, outNbElement, outputFastK, outputFastD);

        //Everything between the /***/ lines is what needs to be changed.
        //Everything else remains the same. You are done with your part now.
        /**********************************************************************/

        //fix the output array's structure. TA-Lib does NOT match
        //indicator index and dataset index automatically. That's what
        //this function does for us.
        outputFastD = TaLibUtilities.fixOutputArray(outputFastD, lookback);
        outputFastK = TaLibUtilities.fixOutputArray(outputFastK, lookback);
        
        if(outputFastD.length != outputFastK.length)
        {
        	throw new java.lang.IllegalStateException("D.length " + outputFastD.length + " != K.length " + outputFastK.length);
        }
        
        Dataset d = new Dataset(HEADER_KDJ);
        DataItem item = null;

        //StockDataset calculatedDatasetFastD = StockDataset.EMPTY(dataset.getItemsCount());
        for (int i = 0; i < outputFastD.length; i++)
        {
        	item = new DataItem(HEADER_KDJ);
        	item.setTime(dataset.getTimeAt(i));
        	
        	item.setValue(0, outputFastK[i]);
        	item.setValue(1, outputFastD[i]);
        	item.setValue(2, outputFastK[i] * 3 - outputFastD[i] * 2);
        	
        	d.addDataItem(item);
            //calculatedDatasetFastD.setDataItem(i, new DataItem(dataset.getTimeAt(i), outputFastD[i]));
        }

        //calculatedDatasetFastK = StockDataset.EMPTY(dataset.getItemsCount());
        //for (int i = 0; i < outputFastK.length; i++)
        //    calculatedDatasetFastK.setDataItem(i, new DataItem(dataset.getTimeAt(i), outputFastK[i]));

        //addDataset(FASTD, calculatedDatasetFastD);
        //addDataset(FASTK, calculatedDatasetFastK);
        
        return d; 
    }
}
