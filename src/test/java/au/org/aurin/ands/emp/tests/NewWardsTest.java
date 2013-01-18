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
import au.org.aurin.ands.emp.NewWards;
import au.org.aurin.ands.emp.Shp2RConnection;

public class NewWardsTest {

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
	
	@Test
	public void test() throws RserveException {
		
		System.out.println("========= Test case NewWards");
		Shp2RConnection shp2R = new Shp2RConnection();
		shp2R.shpUrl = "/Users/yiqunc/githubRepositories/thirdparty-analytics/src/main/resources/outputs/tmpRlt";
		shp2R.exec();
		
		NewWards wc = new NewWards();
		//wc.c = new RConnection();
		wc.c = shp2R.c;
		wc.geodisthreshold = 50;
		wc.interestedColNames = new String[3];
		wc.interestedColNames[0] = "X2310";
		wc.interestedColNames[1] = "X2412";
		wc.interestedColNames[2] = "X8500";
		
		wc.compute();

	}

}