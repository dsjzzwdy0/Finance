package com.loris.ml;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.datumbox.framework.common.Configuration;
import com.datumbox.framework.core.common.dataobjects.Dataframe;
import com.datumbox.framework.core.common.dataobjects.Record;
import com.datumbox.framework.core.machinelearning.MLBuilder;
import com.datumbox.framework.core.machinelearning.classification.SoftMaxRegression;
import com.datumbox.framework.core.machinelearning.featureselection.PCA;
import com.datumbox.framework.core.machinelearning.modelselection.metrics.ClassificationMetrics;
import com.datumbox.framework.core.machinelearning.preprocessing.MinMaxScaler;

public class SoccerPredict
{
	private static Logger logger = Logger.getLogger(SoccerPredict.class);
	
	public static void main(String[] args)
	{
		try
		{
			SoccerPredict predict = new SoccerPredict();
			predict.predict();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 预测数据
	 * 
	 * @throws IOException
	 */
	public void predict() throws IOException
	{
		logger.info("Test for predict.");
		Configuration conf = Configuration.getConfiguration();
		Dataframe[] dataFrames = Datasets.recommenderSystemFood(conf);

		// Scale continuous variables
		MinMaxScaler.TrainingParameters nsParams = new MinMaxScaler.TrainingParameters();
		MinMaxScaler numericalScaler = MLBuilder.create(nsParams, conf);
		
		numericalScaler.fit_transform(dataFrames[0]);
		
		numericalScaler.save("Diabetes");

		// Feature Selection
		// -----------------

		// Perform dimensionality reduction using PCA
		
		Dataframe trainingDataframe = dataFrames[0];
		Dataframe testingDataframe = dataFrames[1];

		PCA.TrainingParameters featureSelectionParameters = new PCA.TrainingParameters();
		featureSelectionParameters.setMaxDimensions(trainingDataframe.xColumnSize() - 1); // remove
																							// one
																							// dimension
		featureSelectionParameters.setWhitened(false);
		featureSelectionParameters.setVariancePercentageThreshold(0.99999995);

		PCA featureSelection = MLBuilder.create(featureSelectionParameters, conf);
		featureSelection.fit_transform(trainingDataframe);
		featureSelection.save("Diabetes");

		// Fit the classifier
		// ------------------

		SoftMaxRegression.TrainingParameters param = new SoftMaxRegression.TrainingParameters();
		param.setTotalIterations(200);
		param.setLearningRate(0.1);

		SoftMaxRegression classifier = MLBuilder.create(param, conf);
		classifier.fit(trainingDataframe);
		classifier.save("Diabetes");

		// Use the classifier
		// ------------------

		// Apply the same numerical scaling on testingDataframe
		numericalScaler.transform(testingDataframe);

		// Apply the same featureSelection transformations on testingDataframe
		featureSelection.transform(testingDataframe);

		// Use the classifier to make predictions on the testingDataframe
		classifier.predict(testingDataframe);

		System.out.println("Results:");
		for (Map.Entry<Integer, Record> entry : testingDataframe.entries())
		{
			Integer rId = entry.getKey();
			Record r = entry.getValue();
			System.out.println("Record " + rId + " - Real Y: " + r.getY() + ", Predicted Y: " + r.getYPredicted());
		}

		// Get validation metrics on the test set
		ClassificationMetrics vm = new ClassificationMetrics(testingDataframe);
		System.out.println("Classifier Accuracy: " + vm.getAccuracy());

		// Clean up
		// --------

		// Delete scaler, featureselector and classifier.
		numericalScaler.delete();
		featureSelection.delete();
		classifier.delete();

		// Close Dataframes.
		trainingDataframe.close();
		testingDataframe.close();
	}
}
