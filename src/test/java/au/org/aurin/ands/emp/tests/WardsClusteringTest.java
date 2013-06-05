package au.org.aurin.ands.emp.tests;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;


import au.org.aurin.ands.emp.WardsClustering;
import au.org.aurin.ands.emp.preload.LoadRScriptEmpcluster;
import au.org.aurin.ands.emp.preload.Rserve;


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
	
	@Test
	public void test() throws RserveException, IOException, REXPMismatchException {
		
		System.out.println("========= Test case NewWards");
		String path  = this.getClass().getClassLoader().getResource("data/testSample").getPath();
		path += "/" + "IssuePolygons_qgis";
		
		
    String rWorkingDir = this.getClass().getClassLoader().getResource("outputs").getPath();
    System.out.println(rWorkingDir);
      
    RConnection cOut = new RConnection();
    cOut.assign("script", LoadRScriptEmpcluster.getGeoJSON2DataFrameScript());
    cOut.assign("shpUrl", new REXPString(path));
    cOut.assign("geoJSONFilePath", new REXPString(path + ".geojson"));
    //cOut.assign("geojSONString", new REXPString(this.geojSONString));
    cOut.assign("spatialDataFormatMode", new REXPInteger(1));

    cOut.assign("rWorkingDir", new REXPString(rWorkingDir));
      
    cOut.eval("try(eval(parse(text=script)),silent=FALSE)");
   		
		WardsClustering wc = new WardsClustering();

		wc.cIn = cOut;
		
		wc.geodisthreshold = 10;
		wc.targetclusternum = 1;
		wc.interestedColNamesString = "X2310,X2412,X8500";
		wc.displayColNamesString = "LGA_CODE,LGA,ZONE_CODE";
		wc.interestedColWeightsString = "0.333,0.333,0.333";
		wc.spatialNonSpatialDistWeightsString = "0.9,0.1";
		wc.ignoreEmptyRowJobNum = 1;
		wc.vcmode = true;
		wc.compute();

	}
	
	

}