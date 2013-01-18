package au.org.aurin.ands.emp.tests;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.util.JSONUtils;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import au.edu.uq.preload.Rserve;
import au.org.aurin.ands.emp.WardsClustering;

public class WardsClusteringTest {

	@BeforeClass
	public static void initRserve() {
		boolean rRunning = false;
		// 0. Start Rserve - This should already be running, if not we start it
		rRunning = Rserve.checkLocalRserve();
		System.out.println("Rserve running? " + rRunning);
		if (!rRunning) {
			Assert.fail("Without Rserve running we cannot proceed");
		}
	}
	@AfterClass
	public static void terminateRserve() {
		boolean rRunning = true;
		// Stop Rserve if we started it
		rRunning = Rserve.shutdownRserve();
		System.out.println("Rserve shutdown? " + rRunning);
		if (!rRunning) {
			Assert.fail("Cannot Shutdown Rserve, Check if there are permissions "
					+ "to shut it down if the process is owned by a different user");
		}
	}
	/*
	public REXP dataGenerator() {

		REXP data = null;

		double[] d1 = new double[] { 1.1, 2.2, 3.3, 11.1, 22.2, 33.3 }; // col1
		double[] d2 = new double[] { 10.0, 20.0, 30.0, 40.0, 50.0, 60.0 }; // col2
		double[] d3 = new double[] { 100.0, 200.0, 300.0, 400.0, 500.0, 600.0 }; // col1
//		double[] d4 = new double[] { 100.1, 200.2, 300.3, 1100.1, 2200.2, 33000.3 }; // col2
		double[] d4 = new double[] { 100.1, 200.2, 300.3, 110.1, 220.2, 3300.3 }; // col2
		 
		List<double[]> dataL = new ArrayList<double[]>();
		dataL.add(d1);
		dataL.add(d2);
		dataL.add(d3);
		dataL.add(d4);

		RList a = new RList();
		// add each column separately
		a.put("iCol1", new REXPDouble(dataL.get(0)));
		a.put("iCol2", new REXPDouble(dataL.get(1)));
		
		a.put("dCol1", new REXPDouble(dataL.get(2)));
		a.put("dCol2", new REXPDouble(dataL.get(3)));

		try {
			data = REXP.createDataFrame(a);
		} catch (REXPMismatchException e) {
			e.printStackTrace();
		}

		return data;
	}
	*/
	@Test
	public void test() throws RserveException {
		System.out.println("Test case WardsClustering");
		
		//WardsClustering wc = new WardsClustering();
		//wc.c = new RConnection();
		//wc.compute();

	}

}