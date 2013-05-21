package au.org.aurin.ands.emp.tests;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.util.JSONUtils;

import org.apache.commons.io.FileUtils;
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
import au.org.aurin.ands.emp.DataFrame2DataStore;
import au.org.aurin.ands.emp.WardsClustering;
import au.org.aurin.ands.emp.SpatialData2RConnection;


public class DataFrame2DataStoreTest {

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
	public void test() throws RserveException, IOException {
		
		System.out.println("========= Test case NewWards");
		SpatialData2RConnection sd2R = new SpatialData2RConnection();
		String path  = this.getClass().getClassLoader().getResource("data/testSample").getPath();
		path += "/" + "IssuePolygons";
		
//    String path  = this.getClass().getClassLoader().getResource("data/testSample").getPath();
//    path += "/" + "IssuePolygons";
		
		sd2R.shpUrl = path;
		///Users/philipgreenwood/gitRepositories/thirdparty-analytics/src/main/resources/data/ABS_data_by_DZN/DZN/smalldata.geojson
		path = path + ".geojson";
		sd2R.geoJSONFilePath = path;
		System.out.println("path =" + path);
		

		
//		try {
		  File file = new File(path.trim());
//		  sd2R.geojSONString = FileUtils.readFileToString(file);
//		} catch (IOException e) {
//      throw new IOException("Unable to load read the resource file ");
//    }
		    		    
		sd2R.spatialDataFormatMode = 1; //0=shp, 1=geoJSON file, 2=geoJSON string
		
		System.out.println(path);
		System.out.println(sd2R.geoJSONFilePath);
		
		sd2R.exec();
		
		WardsClustering wc = new WardsClustering();

		wc.c = sd2R.cOut;
		
		wc.geodisthreshold = 10;
		wc.targetclusternum = 1;
		wc.interestedColNamesString = "X2310,X2412,X8500";
		wc.displayColNamesString = "LGA_CODE,LGA,ZONE_CODE";
		wc.interestedColWeightsString = "0.333,0.333,0.333";
		wc.spatialNonSpatialDistWeightsString = "0.9,0.1";
		wc.ignoreEmptyRowJobNum = 1;
		wc.vcmode = true;
		wc.compute();
		
		DataFrame2DataStore df2ds= new DataFrame2DataStore();
		df2ds.cIn = wc.cOut;
		df2ds.xAurinUserId = "whatifguest";
		df2ds.pathToTmpData = sd2R.rWorkingDir + "/dump.geojson";
		df2ds.exec();
		System.out.println("URLinDataStore =" + df2ds.urlInDataStore);

	}
	
	

}