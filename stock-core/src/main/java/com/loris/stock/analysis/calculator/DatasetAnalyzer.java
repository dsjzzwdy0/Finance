package com.loris.stock.analysis.calculator;

import com.loris.stock.analysis.talib.Core;
import com.loris.stock.analysis.talib.MAType;
import com.loris.stock.analysis.talib.MInteger;
import com.loris.stock.analysis.talib.TaLibUtilities;
import com.loris.stock.bean.item.DataItem;
import com.loris.stock.bean.item.util.DataField;
import com.loris.stock.bean.item.util.DataHeader;
import com.loris.stock.bean.item.util.FieldType;
import com.loris.stock.bean.model.Dataset;
import com.loris.stock.bean.model.StockDataset;


/**
 * Stock数据分析模型
 * 1、股指计算(选择一系列走势正常的股票进行指数据计算):
 * 
 * @author dsj
 *
 */
public class DatasetAnalyzer 
{
	/** Default predefined header info. */
	public static DataHeader HEADER_DEMA = DataHeader.createDataHeader(); 
	public static DataHeader HEADER_MACD = DataHeader.createDataHeader();
	
	/** The Core library. */
	private static Core core = new Core(); 
	
	/** The instance of Analyzer. */
	public static DatasetAnalyzer instance = null;
	
	/**
	 * Get the default DatasetAnalyzer.
	 * @return
	 */
	public static DatasetAnalyzer getInstance()
	{
		if(instance == null)
		{
			instance = new DatasetAnalyzer();
		}
		return instance;
	}
	
	
	/**
	 * Create a new instance of Dataset Analyzer
	 * 
	 * @param dataset
	 */
	private DatasetAnalyzer()
	{
		this.inialize();
	}
	
	/**
	 * 初始化
	 */
	private void inialize()
	{
		HEADER_DEMA.addField(new DataField("dema", FieldType.DOUBLE));
		
		HEADER_MACD.addField(new DataField("macd"));
		HEADER_MACD.addField(new DataField("signal"));
		HEADER_MACD.addField(new DataField("histogram"));
	}
	
	/**
	 * 
	 * @param dataset
	 * @param fast
	 * @param slow
	 * @param smooth
	 * @return
	 */
	public Dataset MACD(StockDataset dataset, int fast, int slow, int smooth)
	{
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
	public Dataset DEMA(StockDataset dataset, int period)
	{
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
	 * @param dataset
	 * @param peirod
	 * @return
	 */
	public Dataset deltaClose(StockDataset dataset, int peirod)
	{
		double output0[], output1[];
		int count = 0;
				
		if (dataset != null && !dataset.isEmpty())
        {
            count = dataset.getItemsCount();
        }
		
		output0 = new double[count];
		output1 = new double[count];
		
		double close[] = dataset.getCloseValues();
		output0[0] = 0.0f;
		output1[0] = 0.0f;
		
		for(int i = 1; i < count; i ++)
		{
			output0[i] = close[i] - close[i - 1];
			output1[i] = output0[i] - output0[i - 1];
		}
		
		DataHeader header = new DataHeader();
		header.addField(new DataField("Derivat1", FieldType.DOUBLE));
		header.addField(new DataField("Derivat2", FieldType.DOUBLE));
		Dataset calculatedDataset = new Dataset(header);
		
		for (int i = 0; i < count; i++)
        {
			DataItem item = new DataItem(header);
			item.setTime(dataset.getTimeAt(i));
			item.setValue(0, output0[i]);
			item.setValue(1, output1[i]);
        	calculatedDataset.addDataItem(item);
        }
		
		return calculatedDataset;
	}
	
	//public double corelationOpenVolume()
}
