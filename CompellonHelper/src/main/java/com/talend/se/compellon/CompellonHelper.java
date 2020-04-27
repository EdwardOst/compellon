package com.talend.se.compellon;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.compellon.predictor.BinaryPrediction;
import com.compellon.predictor.Contribution;
import com.compellon.predictor.Identifier;
import com.compellon.predictor.NumericPrediction;
import com.compellon.predictor.PredictionRunner;
import com.compellon.predictor.api.PredictionApi;

import scala.reflect.ClassTag$;

public class CompellonHelper {

	private final PredictionRunner runner;
	private String uniqueColumn = "";
//	private PredictionRunner.PredictionVisitor<Void> visitor;

	public CompellonHelper(String predictorPath, String uniqueColumn) {
		runner = new PredictionRunner(predictorPath, PredictionRunner.PredictionType.Binary);
		this.uniqueColumn = uniqueColumn;
	}

	public CompellonHelper(String predictorPath) {
		this(predictorPath, "");
	}

	public PredictionRunner getRunner() {
		return runner;
	}

	public String getUniqueColumn() {
		return uniqueColumn;
	}

//	public void setVisitor(PredictionRunner.PredictionVisitor<Void> visitor) {
//		this.visitor = visitor;
//	}

	static public class PredictionResult {
		public int response;
		public double confidence;
		public boolean outOfRange;
		public boolean newCombination;
		public Contribution contributions[];
		
		PredictionResult(int response, double confidence, boolean outOfRange, boolean newCombination, Contribution[] contributions) {
			this.response = response;
			this.confidence = confidence;
			this.outOfRange = outOfRange;
			this.newCombination = newCombination;
			this.contributions = contributions;
		}
		
		public String toString() {
			return Integer.toString(response) + ", " 
					+ Double.toString(confidence) + ", "
					+ Boolean.toString(outOfRange) + ", "
					+ Boolean.toString(newCombination) + ", "
					+ Arrays.toString(contributions);
		}
	}
	
	public PredictionRunner.PredictionVisitor<Void> createVisitor(final List<PredictionResult> results) {
		PredictionRunner.PredictionVisitor<Void> visitor;
		visitor = new PredictionRunner.PredictionVisitor<Void>() {
			public Void caseBinary(PredictionApi.Out<BinaryPrediction> prediction) {
				int response = prediction.prediction().response();
				double confidence = (Double) prediction.prediction().confidence().get();
				boolean outOfRange = prediction.prediction().outOfRange();
				boolean newCombination = prediction.prediction().newCombination();
				Contribution[] contributions = (Contribution[]) prediction.prediction().contributors()
						.toArray(ClassTag$.MODULE$.apply(Contribution.class));
				results.add( new PredictionResult(response, confidence, outOfRange, newCombination, contributions) );
				return null;
			}

			public Void caseNumeric(PredictionApi.Out<NumericPrediction> prediction) {
				System.out.println(prediction);
				return null;
			}
		};
		return visitor;
	}

	public void run(List<String> header, List<List<String>> dataRows, List<PredictionResult> results) {
		PredictionRunner.PredictionVisitor<Void> visitor = this.createVisitor(results);
		if (uniqueColumn != "") {
			runner.predict(dataRows, header, uniqueColumn, visitor);
		} else {
			runner.predict(dataRows, header, PredictionRunner.IdFormat.RowIsId, visitor);
		}
	}

	public static void loadRows(Scanner scanner, List<List<String>> dataRows) {
		// Read in the prediction dataset and convert the data rows to a 2d list of strings
	    // The header (1st) row will be saved separately
	    String row;
	    List<String> rowSplits;
	    while (scanner.hasNext()) {
	      row = scanner.next();
	      rowSplits = Arrays.asList(row.split(","));
	      dataRows.add(rowSplits);
	    }
	}
	
	public static List<String> load(String dataPath, List<List<String>> dataRows) throws java.io.IOException {
	    Scanner scanner = new Scanner(new File(dataPath));
	    scanner.useDelimiter("\n");

	    String[] headerSplits = scanner.next().split(",");
		List<String> header = Arrays.asList(headerSplits);

		loadRows(scanner, dataRows);
	    scanner.close();
		return header;
	}
	
	
}
