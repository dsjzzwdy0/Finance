package com.loris.learning;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.encog.Encog;
import org.encog.app.analyst.AnalystFileFormat;
import org.encog.app.analyst.EncogAnalyst;
import org.encog.app.analyst.csv.normalize.AnalystNormalizeCSV;
import org.encog.app.analyst.csv.segregate.SegregateCSV;
import org.encog.app.analyst.csv.segregate.SegregateTargetPercent;
import org.encog.app.analyst.csv.shuffle.ShuffleCSV;
import org.encog.app.analyst.script.normalize.AnalystField;
import org.encog.app.analyst.wizard.AnalystWizard;
import org.encog.engine.network.activation.ActivationLinear;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.manhattan.ManhattanPropagation;
import org.encog.neural.pattern.ElmanPattern;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.persist.EncogDirectoryPersistence;
import org.encog.util.arrayutil.ClassItem;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.csv.CSVFormat;
import org.encog.util.simple.EncogUtility;

public class IncomePrediction
{
	private static Logger logger = Logger.getLogger(IncomePrediction.class);

	public static void shuffle(String source)
	{
		// Shuffle the CSV.
		ShuffleCSV shuffle = new ShuffleCSV();
		File csv = new File(source);
		shuffle.analyze(csv, true, CSVFormat.ENGLISH);
		shuffle.setProduceOutputHeaders(true);
		shuffle.process(new File(FilesPath.SHUFFLED_BASE_FILE));
	}

	public static void segregate(String source)
	{
		// Segregate file into training and evaluation files
		SegregateCSV seg = new SegregateCSV();

		seg.getTargets().add(new SegregateTargetPercent(new File(FilesPath.TRAINING_FILE), 75));
		seg.getTargets().add(new SegregateTargetPercent(new File(FilesPath.EVALUATION_FILE), 25));
		seg.setProduceOutputHeaders(true);
		seg.analyze(new File(source), true, CSVFormat.ENGLISH);
		seg.process();
	}

	public static void normalize()
	{
		// Analyst
		EncogAnalyst analyst = new EncogAnalyst();

		// Wizard
		AnalystWizard wizard = new AnalystWizard(analyst);
		wizard.wizard(new File(FilesPath.BASE_FILE), true, AnalystFileFormat.DECPNT_COMMA);

		analyst.getScript().getNormalize().getNormalizedFields().get(0).setAction(NormalizationAction.Normalize);
		analyst.getScript().getNormalize().getNormalizedFields().get(1).setAction(NormalizationAction.Equilateral);
		analyst.getScript().getNormalize().getNormalizedFields().get(2).setAction(NormalizationAction.Normalize);
		analyst.getScript().getNormalize().getNormalizedFields().get(3).setAction(NormalizationAction.Equilateral);
		analyst.getScript().getNormalize().getNormalizedFields().get(4).setAction(NormalizationAction.Normalize);
		analyst.getScript().getNormalize().getNormalizedFields().get(5).setAction(NormalizationAction.Equilateral);
		analyst.getScript().getNormalize().getNormalizedFields().get(6).setAction(NormalizationAction.Equilateral);
		analyst.getScript().getNormalize().getNormalizedFields().get(7).setAction(NormalizationAction.Equilateral);
		analyst.getScript().getNormalize().getNormalizedFields().get(8).setAction(NormalizationAction.Equilateral);
		analyst.getScript().getNormalize().getNormalizedFields().get(9).setAction(NormalizationAction.OneOf);
		analyst.getScript().getNormalize().getNormalizedFields().get(10).setAction(NormalizationAction.Normalize);
		analyst.getScript().getNormalize().getNormalizedFields().get(11).setAction(NormalizationAction.Normalize);
		analyst.getScript().getNormalize().getNormalizedFields().get(12).setAction(NormalizationAction.Normalize);
		analyst.getScript().getNormalize().getNormalizedFields().get(13).setAction(NormalizationAction.Equilateral);

		analyst.getScript().getNormalize().getNormalizedFields().get(14).setAction(NormalizationAction.OneOf);

		// Norm for Training
		AnalystNormalizeCSV norm = new AnalystNormalizeCSV();
		norm.analyze(new File(FilesPath.TRAINING_FILE), true, CSVFormat.ENGLISH, analyst);
		norm.setProduceOutputHeaders(true);
		norm.normalize(new File(FilesPath.NORMALIZED_TRAINING_FILE));

		// Norm for evaluation
		norm.analyze(new File(FilesPath.EVALUATION_FILE), true, CSVFormat.ENGLISH, analyst);
		norm.normalize(new File(FilesPath.NORMALIZED_EVAL_FILE));

		// Save the Analyst file
		analyst.save(new File(FilesPath.ANALYST_FILE));
	}
	
	public static BasicNetwork createFeedForwardNetwork(String networkFile)
	{
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setInputNeurons(97);
		pattern.addHiddenLayer(60);
		//pattern.addHiddenLayer(10);
		pattern.setOutputNeurons(2);
		pattern.setActivationFunction(new ActivationTANH());		
		//pattern.setActivationFunction(new ActivationReLU());
		BasicNetwork network = (BasicNetwork)pattern.generate();
		network.reset();
		
		EncogDirectoryPersistence.saveObject(new File(networkFile), network);
		return network;
	}

	// Elman network
	public static BasicNetwork createElmanNetwork(String networkFile)
	{
		// construct an Elman type network
		ElmanPattern pattern = new ElmanPattern();
		pattern.setActivationFunction(new ActivationSigmoid());
		pattern.setInputNeurons(97);
		pattern.addHiddenLayer(20);
		//pattern.addHiddenLayer(20);
		pattern.setOutputNeurons(2);
		
		BasicNetwork network = (BasicNetwork)pattern.generate();		
		EncogDirectoryPersistence.saveObject(new File(networkFile), network);
		return network;
	}

	public static void createNetwork(String networkFile)
	{
		BasicNetwork network = new BasicNetwork();

		network.addLayer(new BasicLayer(new ActivationLinear(), true, 97));
		// network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 10));
		network.addLayer(new BasicLayer(new ActivationLinear(), true, 49));
		//network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 30));
		//network.addLayer(new BasicLayer(new ActivationLinear(), true, 30));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 15));
		network.addLayer(new BasicLayer(new ActivationTANH(), false, 2));
		network.getStructure().finalizeStructure();
		network.reset();
		EncogDirectoryPersistence.saveObject(new File(networkFile), network);
	}

	public static void trainNetwork()
	{
		BasicNetwork network = (BasicNetwork) EncogDirectoryPersistence
				.loadObject(new File(FilesPath.TRAINED_NETWORK_FILE));
		MLDataSet trainingSet = EncogUtility.loadCSV2Memory(FilesPath.NORMALIZED_TRAINING_FILE, network.getInputCount(),
				network.getOutputCount(), true, CSVFormat.ENGLISH, false);

		// ResilientPropagation train = new ResilientPropagation(network,
		// trainingSet);

		double BPROP_LEARNING_RATE = 0.05;
		// double BPROP_MOMENTUM = 0.1;

		// Backpropagation train = new Backpropagation(network, trainingSet,
		// BPROP_LEARNING_RATE, BPROP_MOMENTUM);

		// quickprop training (not working)
		// TODO: flags for learning rate, default = 2
		//QuickPropagation train = new QuickPropagation(network, trainingSet, BPROP_LEARNING_RATE);
		
		ManhattanPropagation train = new ManhattanPropagation(network, trainingSet,
				BPROP_LEARNING_RATE);
		

		int epoch = 1;
		do
		{
			train.iteration();
			logger.info("Epoch: " + epoch + ", error: " + train.getError() + ", dropout: " + train.getDropoutRate());
			
			epoch++;
		} while (epoch <= 1500);

		EncogDirectoryPersistence.saveObject(new File(FilesPath.TRAINED_NETWORK_FILE), network);
	}

	public static void evaluate()
	{
		BasicNetwork network = (BasicNetwork) EncogDirectoryPersistence
				.loadObject(new File(FilesPath.TRAINED_NETWORK_FILE));
		EncogAnalyst analyst = new EncogAnalyst();
		analyst.load(FilesPath.ANALYST_FILE);
		MLDataSet evaluationSet = EncogUtility.loadCSV2Memory(FilesPath.NORMALIZED_EVAL_FILE, network.getInputCount(),
				network.getOutputCount(), true, CSVFormat.ENGLISH, false);

		//
		/*
		 * int classCount =
		 * analyst.getScript().getNormalize().getNormalizedFields().get(14).
		 * getClasses().size(); logger.info("Class Count: " + classCount);
		 * double normalizationHigh =
		 * analyst.getScript().getNormalize().getNormalizedFields().get(14).
		 * getNormalizedHigh(); double normalizationLow =
		 * analyst.getScript().getNormalize().getNormalizedFields().get(14).
		 * getNormalizedLow(); logger.info("High: " + normalizationHigh +
		 * ", Low: " + normalizationLow); Equilateral eq = new
		 * Equilateral(classCount, normalizationHigh, normalizationLow);
		 * eq.printMatrix();
		 */

		AnalystField field = analyst.getScript().getNormalize().getNormalizedFields().get(14);

		int count = 0;
		int CorrectCount = 0;

		for (MLDataPair item : evaluationSet)
		{
			count++;
			BasicMLData output = (BasicMLData) network.compute(item.getInput());
			double[] idealArray = item.getIdealArray();
			logger.info("Input: " + Arrays.toString(idealArray));
			// logger.info("Input: " + Arrays.toString(item.getInputArray()));
			logger.info("Output: " + Arrays.toString(output.getData()));

			/*
			 * int idealClassInt = eq.decode(idealArray); String idealClass =
			 * analyst.getScript().getNormalize().getNormalizedFields().get(14).
			 * getClasses().get(idealClassInt).getName();
			 * 
			 * int predictedClassInt = eq.decode(output.getData()); String
			 * predictedClass =
			 * analyst.getScript().getNormalize().getNormalizedFields().get(14).
			 * getClasses().get(predictedClassInt).getName();
			 */
			ClassItem idealClass = field.determineClass(idealArray);
			ClassItem predictClass = field.determineClass(output.getData());

			if (idealClass.getIndex() == predictClass.getIndex())
				CorrectCount++;

			System.out.println(
					"Count: " + count + " - Ideal: " + idealClass.getName() + " Predicted: " + predictClass.getName());
		}
		System.out.println("Total test count: " + count);
		System.out.println("Total correct prediction count: " + CorrectCount);
		System.out.println("Success: " + (CorrectCount * 100) / count + "%.");
		
		Encog.getInstance().shutdown();
	}

	public void evaluateOne()
	{

		EncogAnalyst analyst = new EncogAnalyst();
		// Wizard
		AnalystWizard wizard = new AnalystWizard(analyst);
		wizard.wizard(new File(FilesPath.BASE_FILE), true, AnalystFileFormat.DECPNT_COMMA);
		String input = "34,Federal-gov,67083,Bachelors,13,Never-married,Exec-manageria,Unmarried,Asian-Pac-Islander,Male,1471,0,40,Cambodia";
		printToCSV(input);

		AnalystNormalizeCSV norm = new AnalystNormalizeCSV();
		norm.setProduceOutputHeaders(true);

		// Norm for evaluation
		norm.analyze(new File(FilesPath.SINGLE_EVAL), true, CSVFormat.ENGLISH, analyst);
		norm.normalize(new File(FilesPath.SINGLE_EVAL));

		// Save the Analyst file
		analyst.save(new File(FilesPath.ANALYST_FILE));

		// BasicNetwork network = (BasicNetwork)
		EncogDirectoryPersistence.loadObject(new File(FilesPath.TRAINED_NETWORK_FILE));

		// AnalystNormalize norm = new AnalystNormalize(analyst.getScript());
		// norm.
		//
		// BasicMLData data = new BasicMLData();

	}

	public void printToCSV(String line)
	{
		try (PrintWriter out = new PrintWriter(FilesPath.SINGLE_EVAL))
		{
			out.println(line);
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
