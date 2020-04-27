package com.talend.se.compellon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		final String defaultPredictorPath = "/Users/eost/eclipse-workspace/compellon/src/test/resources/predictor-15591.jar";
		final String defaultDataPath="/Users/eost/eclipse-workspace/compellon/src/test/resources/churn.csv";
//		final String defaultPredictorPath = "/Users/eost/eclipse-workspace/compellon/src/test/resources/1473635143909646133175PREDICTOR.jar";
//		final String defaultDataPath="/Users/eost/eclipse-workspace/compellon/src/test/resources/Demog_DS_TRAIN_15K_id.csv";

		
		System.out.println("Compellon.Main.main - start");

	    final String predictorPath = args.length > 0 ? args[0] : defaultPredictorPath;
	    final String dataPath = args.length > 1 ? args[1] : defaultDataPath;
	    final String uniqueColumn = args.length > 2 ? args[2] : "";
		
		System.out.println("predictorPath='" + predictorPath + "'");
		System.out.println("dataPath='" + dataPath + "'");
		System.out.println("uniqueColumn='" + uniqueColumn + "'");

		CompellonHelper compellon = new CompellonHelper(predictorPath, uniqueColumn);
		
		List<List<String>> dataRows = new ArrayList<List<String>>();
		List<String> header;
		try {
			header = CompellonHelper.load(dataPath, dataRows);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		System.out.println(dataRows.size() + " rows read from " + dataPath);

		List<CompellonHelper.PredictionResult> results = new ArrayList<CompellonHelper.PredictionResult>(dataRows.size());

		compellon.run(header, dataRows, results);

		for (CompellonHelper.PredictionResult result : results) {
			System.out.println(result);
		}
		System.out.println(results.size() + " results");

		System.out.println("\nCompellon.Main.main - end");
	}

}
