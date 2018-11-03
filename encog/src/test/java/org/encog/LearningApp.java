package org.encog;

import java.io.File;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.encog.ConsoleStatusReportable;
import org.encog.Encog;
import org.encog.examples.ml.hmm.HMMSimpleContinuous;
import org.encog.ml.MLRegression;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.MLSequenceSet;
import org.encog.ml.data.versatile.NormalizationHelper;
import org.encog.ml.data.versatile.VersatileMLDataSet;
import org.encog.ml.data.versatile.columns.ColumnDefinition;
import org.encog.ml.data.versatile.columns.ColumnType;
import org.encog.ml.data.versatile.missing.MeanMissingHandler;
import org.encog.ml.data.versatile.sources.CSVDataSource;
import org.encog.ml.data.versatile.sources.VersatileDataSource;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.hmm.HiddenMarkovModel;
import org.encog.ml.hmm.alog.KullbackLeiblerDistanceCalculator;
import org.encog.ml.hmm.alog.MarkovGenerator;
import org.encog.ml.hmm.train.bw.TrainBaumWelch;
import org.encog.ml.model.EncogModel;
import org.encog.util.csv.CSVFormat;
import org.encog.util.csv.ReadCSV;
import org.encog.util.simple.EncogUtility;

import com.datumbox.examples.Classification;
import com.loris.learning.Equilateral;
import com.loris.learning.FilesPath;
import com.loris.learning.IncomePrediction;

/**
 * Unit test for simple App.
 */
public class LearningApp
{
	private static Logger logger = Logger.getLogger(LearningApp.class);

	public static void main(String[] args)
	{
		try
		{
			// testINDArray();
			// testPredict();
			// testNeuralNetwork();			
			//predictMpg();
			
			//testIncomePredict();
			//testEquilateral();
			
			//testHmmMethod();
			
			testDatumBox();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void testDatumBox() throws Exception
	{
		//Clustering.main(null);
		//Regression.main(null);
		
		Classification.testClass();
	}
	
	
	public static void testHmmMethod() {
		
		HiddenMarkovModel hmm = HMMSimpleContinuous.buildContHMM();
		HiddenMarkovModel learntHmm = HMMSimpleContinuous.buildContInitHMM();
		
		MarkovGenerator mg = new MarkovGenerator(hmm);
		MLSequenceSet training = mg.generateSequences(200,100);
		
		int seqCount = training.getSequenceCount();
		for(int i = 0; i < seqCount; i ++)
		{
			MLDataSet dataSet = training.getSequence(i);
			logger.info(i + ": " + dataSet.toString());
		}
		
		TrainBaumWelch bwl = new TrainBaumWelch(learntHmm,training);
		
		KullbackLeiblerDistanceCalculator klc = 
			new KullbackLeiblerDistanceCalculator();
		
		System.out.println("Training Continuous Hidden Markov Model with Baum Welch");
		
		for(int i=1;i<=100;i++) 
		{
			double e = klc.distance(learntHmm, hmm);
			System.out.println("Iteration #"+i+": Difference: " + e);
			
			StringBuffer sb = new StringBuffer();
			sb.append("(" + hmm.getPi(0) + ", " + hmm.getPi(1) + ") -> learnt");
			sb.append("(" + learntHmm.getPi(0) + ", " + learntHmm.getPi(1) + ")");
			
			logger.info(sb.toString());
			bwl.iteration();
			learntHmm = (HiddenMarkovModel)bwl.getMethod();
		}		
	}
	
	
	
	public static void testEquilateral() throws Exception
	{
		Equilateral equilateral = new Equilateral(10,  1.0,  -1.0);
		equilateral.printMatrix();
	}
	
	
	public static void testIncomePredict() throws Exception
	{
		//logger.info("Shuffle file...");
		//IncomePrediction.shuffle(FilesPath.BASE_FILE);

		//logger.info("Segregate file into training and evaluation...");
		//IncomePrediction.segregate(FilesPath.SHUFFLED_BASE_FILE);

		//logger.info("Normalize data...");
		//IncomePrediction.normalize();

		logger.info("Create network...");
		IncomePrediction.createNetwork(FilesPath.TRAINED_NETWORK_FILE);
		//IncomePrediction.createElmanNetwork(FilesPath.TRAINED_NETWORK_FILE);
		//IncomePrediction.createFeedForwardNetwork(FilesPath.TRAINED_NETWORK_FILE);

		logger.info("Training network...");
		IncomePrediction.trainNetwork();

		logger.info("Evaluate network...");
		IncomePrediction.evaluate();
		
		//logger.info("Evaluate one...");
		//IncomePrediction.
	}
	

	public static void predictMpg()
	{
		try
		{
			// Download the data that we will attempt to model.
			File filename = new File("D:/index/test-data/auto-mpg.data");

			// Define the format of the data file.
			// This area will change, depending on the columns and
			// format of the file that you are trying to model.
			CSVFormat format = new CSVFormat('.', ' '); // decimal point and
														// space separated
			VersatileDataSource source = new CSVDataSource(filename, false, format);

			VersatileMLDataSet data = new VersatileMLDataSet(source);
			data.getNormHelper().setFormat(format);

			ColumnDefinition columnMPG = data.defineSourceColumn("mpg", 0, ColumnType.continuous);
			ColumnDefinition columnCylinders = data.defineSourceColumn("cylinders", 1, ColumnType.ordinal);
			// It is very important to predefine ordinals, so that the order is
			// known.
			columnCylinders.defineClass(new String[]
			{ "3", "4", "5", "6", "8" });
			data.defineSourceColumn("displacement", 2, ColumnType.continuous);
			ColumnDefinition columnHorsePower = data.defineSourceColumn("horsepower", 3, ColumnType.continuous);
			data.defineSourceColumn("weight", 4, ColumnType.continuous);
			data.defineSourceColumn("acceleration", 5, ColumnType.continuous);
			ColumnDefinition columnModelYear = data.defineSourceColumn("model_year", 6, ColumnType.ordinal);
			columnModelYear.defineClass(new String[]
			{ "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82" });
			data.defineSourceColumn("origin", 7, ColumnType.nominal);

			// Define how missing values are represented.
			data.getNormHelper().defineUnknownValue("?");
			data.getNormHelper().defineMissingHandler(columnHorsePower, new MeanMissingHandler());

			// Analyze the data, determine the min/max/mean/sd of every column.
			data.analyze();

			// Map the prediction column to the output of the model, and all
			// other columns to the input.
			data.defineSingleOutputOthersInput(columnMPG);

			// Create feedforward neural network as the model type.
			// MLMethodFactory.TYPE_FEEDFORWARD.
			// You could also other model types, such as:
			// MLMethodFactory.SVM: Support Vector Machine (SVM)
			// MLMethodFactory.TYPE_RBFNETWORK: RBF Neural Network
			// MLMethodFactor.TYPE_NEAT: NEAT Neural Network
			// MLMethodFactor.TYPE_PNN: Probabilistic Neural Network
			EncogModel model = new EncogModel(data);
			model.selectMethod(data, MLMethodFactory.TYPE_FEEDFORWARD);

			// Send any output to the console.
			model.setReport(new ConsoleStatusReportable());

			// Now normalize the data. Encog will automatically determine the
			// correct normalization
			// type based on the model you chose in the last step.
			data.normalize();

			// Hold back some data for a final validation.
			// Shuffle the data into a random ordering.
			// Use a seed of 1001 so that we always use the same holdback and
			// will get more consistent results.
			model.holdBackValidation(0.3, true, 1001);

			// Choose whatever is the default training type for this model.
			model.selectTrainingType(data);

			// Use a 5-fold cross-validated train. Return the best method found.
			MLRegression bestMethod = (MLRegression) model.crossvalidate(5, true);

			// Display the training and validation errors.
			System.out.println("Training error: " + model.calculateError(bestMethod, model.getTrainingDataset()));
			System.out.println("Validation error: " + model.calculateError(bestMethod, model.getValidationDataset()));

			// Display our normalization parameters.
			NormalizationHelper helper = data.getNormHelper();
			System.out.println(helper.toString());

			// Display the final model.
			System.out.println("Final model: " + bestMethod);

			// Loop over the entire, original, dataset and feed it through the
			// model.
			// This also shows how you would process new data, that was not part
			// of your
			// training set. You do not need to retrain, simply use the
			// NormalizationHelper
			// class. After you train, you can save the NormalizationHelper to
			// later
			// normalize and denormalize your data.
			ReadCSV csv = new ReadCSV(filename, false, format);
			String[] line = new String[7];
			MLData input = helper.allocateInputVector();

			while (csv.next())
			{
				StringBuilder result = new StringBuilder();

				line[0] = csv.get(1);
				line[1] = csv.get(2);
				line[2] = csv.get(3);
				line[3] = csv.get(4);
				line[4] = csv.get(5);
				line[5] = csv.get(6);
				line[6] = csv.get(7);

				String correct = csv.get(0);
				helper.normalizeInputVector(line, input.getData(), false);
				MLData output = bestMethod.compute(input);
				String predictedMPG = helper.denormalizeOutputVectorToString(output)[0];

				result.append(Arrays.toString(line));
				result.append(" -> predicted: ");
				result.append(predictedMPG);
				result.append("(correct: ");
				result.append(correct);
				result.append(")");

				System.out.println(result.toString());
			}

			// Delete data file and shut down.
			filename.delete();
			Encog.getInstance().shutdown();

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	public static void testNeuralNetwork()
	{
		try
		{
			// Download the data that we will attempt to model.
			File irisFile = new File("D:/index/test-data/iris.data");

			// Define the format of the data file.
			// This area will change, depending on the columns and
			// format of the file that you are trying to model.
			VersatileDataSource source = new CSVDataSource(irisFile, false, CSVFormat.DECIMAL_POINT);
			VersatileMLDataSet data = new VersatileMLDataSet(source);
			data.defineSourceColumn("sepal-length", 0, ColumnType.continuous);
			data.defineSourceColumn("sepal-width", 1, ColumnType.continuous);
			data.defineSourceColumn("petal-length", 2, ColumnType.continuous);
			data.defineSourceColumn("petal-width", 3, ColumnType.continuous);

			// Define the column that we are trying to predict.
			ColumnDefinition outputColumn = data.defineSourceColumn("species", 4, ColumnType.nominal);

			// Analyze the data, determine the min/max/mean/sd of every column.
			data.analyze();

			// Map the prediction column to the output of the model, and all
			// other columns to the input.
			data.defineSingleOutputOthersInput(outputColumn);

			// Create feedforward neural network as the model type.
			// MLMethodFactory.TYPE_FEEDFORWARD.
			// You could also other model types, such as:
			// MLMethodFactory.SVM: Support Vector Machine (SVM)
			// MLMethodFactory.TYPE_RBFNETWORK: RBF Neural Network
			// MLMethodFactor.TYPE_NEAT: NEAT Neural Network
			// MLMethodFactor.TYPE_PNN: Probabilistic Neural Network
			EncogModel model = new EncogModel(data);
			// model.selectMethod(data, MLMethodFactory.TYPE_FEEDFORWARD);
			model.selectMethod(data, MLMethodFactory.TYPE_RBFNETWORK);

			// Send any output to the console.
			model.setReport(new ConsoleStatusReportable());

			// Now normalize the data. Encog will automatically determine the
			// correct normalization
			// type based on the model you chose in the last step.
			data.normalize();

			// Hold back some data for a final validation.
			// Shuffle the data into a random ordering.
			// Use a seed of 1001 so that we always use the same holdback and
			// will get more consistent results.
			model.holdBackValidation(0.3, true, 1001);

			// Choose whatever is the default training type for this model.
			model.selectTrainingType(data);

			// Use a 5-fold cross-validated train. Return the best method found.
			MLRegression bestMethod = (MLRegression) model.crossvalidate(5, true);

			// Display the training and validation errors.
			System.out.println(
					"Training error: " + EncogUtility.calculateRegressionError(bestMethod, model.getTrainingDataset()));
			System.out.println("Validation error: "
					+ EncogUtility.calculateRegressionError(bestMethod, model.getValidationDataset()));

			// Display our normalization parameters.
			NormalizationHelper helper = data.getNormHelper();
			System.out.println(helper.toString());

			// Display the final model.
			System.out.println("Final model: " + bestMethod);

			// Loop over the entire, original, dataset and feed it through the
			// model.
			// This also shows how you would process new data, that was not part
			// of your
			// training set. You do not need to retrain, simply use the
			// NormalizationHelper
			// class. After you train, you can save the NormalizationHelper to
			// later
			// normalize and denormalize your data.
			ReadCSV csv = new ReadCSV(irisFile, false, CSVFormat.DECIMAL_POINT);
			String[] line = new String[4];
			MLData input = helper.allocateInputVector();

			while (csv.next())
			{
				StringBuilder result = new StringBuilder();
				line[0] = csv.get(0);
				line[1] = csv.get(1);
				line[2] = csv.get(2);
				line[3] = csv.get(3);
				String correct = csv.get(4);
				helper.normalizeInputVector(line, input.getData(), false);
				MLData output = bestMethod.compute(input);
				String irisChosen = helper.denormalizeOutputVectorToString(output)[0];

				result.append(Arrays.toString(line));
				result.append(" -> predicted: ");
				result.append(irisChosen);
				result.append("(correct: ");
				result.append(correct);
				result.append(")");

				System.out.println(result.toString());
			}

			// Delete data file ande shut down.
			irisFile.delete();
			Encog.getInstance().shutdown();

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}
}
