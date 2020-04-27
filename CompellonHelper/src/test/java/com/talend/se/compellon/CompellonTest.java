package com.talend.se.compellon;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.joda.time.LocalDate;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.compellon.predictor.BinaryPrediction;
import com.compellon.predictor.Contribution;
import com.compellon.predictor.Identifier;
import com.compellon.predictor.NumericPrediction;
import com.compellon.predictor.PredictionRunner;
import com.compellon.predictor.api.PredictionApi;
import com.talend.se.compellon.CompellonHelper;

import scala.reflect.ClassTag$;


public class CompellonTest {

	private CompellonHelper compellon;
	
	private static String predictorPath = "src/test/resources/1473635143909646133175PREDICTOR.jar";
	private static String dataPath="src/test/resources/Demog_DS_TRAIN_15K_id.csv";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		compellon = new CompellonHelper(predictorPath);
	}

	@Test
	public void test() throws java.io.IOException {
		
		System.out.println("CompellonTest.test");
		
		List<List<String>> dataRows = new ArrayList<List<String>>();
		List<String> header = CompellonHelper.load(dataPath, dataRows);
		System.out.println(dataRows.size() + " rows read from " + dataPath);

		List<CompellonHelper.PredictionResult> results = new ArrayList<CompellonHelper.PredictionResult>(dataRows.size());

		compellon.run(header, dataRows, results);

		for (CompellonHelper.PredictionResult result : results) {
			System.out.println(result);
		}
		System.out.println(results.size() + " results");
	}

}
