package com.loris.ml;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.datumbox.framework.common.Configuration;
import com.datumbox.framework.core.common.dataobjects.Dataframe;

public class SoccerPredict
{
	private static Logger logger = Logger.getLogger(SoccerPredict.class);

	/**
	 * 预测数据
	 * 
	 * @throws IOException
	 */
	public void predict() throws IOException
	{
		logger.info("Test for predict.");
		Configuration conf = Configuration.getConfiguration();
		Dataframe trainingData = new Dataframe(conf);
		trainingData.add(Datasets.<String>newDataVector(new String[]
		{ "pos", "pos" }, "pos"));
		trainingData.size();

		trainingData.delete();
		trainingData.close();
	}
}
