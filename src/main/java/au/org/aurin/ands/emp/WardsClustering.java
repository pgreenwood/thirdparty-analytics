package au.org.aurin.ands.emp;

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;

import oms3.annotations.Author;
import oms3.annotations.Description;
import oms3.annotations.Execute;
import oms3.annotations.In;
import oms3.annotations.Keywords;
import oms3.annotations.License;
import oms3.annotations.Name;
import oms3.annotations.Out;
import oms3.annotations.Status;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPList;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;

import au.edu.uq.interfaces.Statistics;
import au.edu.uq.preload.LoadRScript;
import au.edu.uq.preload.Rserve;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;

import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;


public class WardsClustering {
	
	@Description("Input R connection")
	@In
	public RConnection c;
	/**
	 * {@link RConnection} A valid connection to a running {@link Rserve}
	 * instance
	 */
	
	@Description("REXP result complex object")
	@Out
	public REXP worker;
	/**
	 * The result of {@link NewRegression} as an {@link REXP} object containing
	 * all of the results from R
	 */
	
	@Description("shp file path of tmp result")
	@Out
	public String tmpResultPath;
	
	@Execute
	public void compute() {
		try {

			// setup the script to execute
			// 1. load the required script
			try {
				this.c.assign("script", LoadRScript.getWardsClusterScript());
			} catch (IOException e) {
				e.printStackTrace();
			}

			// 2. setup the inputs
			//this.c.assign("depVars", this.dependentVarNames);
			//this.c.assign("indepVars", this.independentVarNames);

			// REXP worker = this.c.eval("try(print(R.Version()))");
			// REXP worker = null;
			// worker = this.c.eval("try(print(str(dataF)))");
			// String res = worker.toDebugString();
			// System.out.println("res = " + res);

			// worker = this.c.eval("try(print(str(depVars)))");
			// String res1 = worker.toDebugString();
			// System.out.println("res1 = " + res1);
			//
			// worker = this.c.eval("try(print(str(indepVars)))");
			// String res2 = worker.toDebugString();
			// System.out.println("res2 = " + res2);

			// 3. call the function defined in the script
			this.worker = c.eval("try(eval(parse(text=script)),silent=FALSE)");
			
			if(worker == null){ 
				System.out.println("worker init failed");
				return;}
			
			//System.out.println("worker result = " + this.worker.toDebugString());
			
			// 4. setup the output results
			this.setupOutputs();
			

		} catch (REngineException e) {
			e.printStackTrace();
		}
		
	}

	private void setupOutputs() {

		RList resultL = null;

		try {
			if (!this.worker.isNull()) {
				System.out.println(" We have results back from R");

				if (this.worker.inherits("try-error") || this.worker.isString()) {
					throw new REXPMismatchException(this.worker,
							"Try-Error from R \n" + this.worker.toString());
				} else if (this.worker.isList()) {
					// result list reply
					resultL = this.worker.asList();
					System.out.println("resultL.size() = " + resultL.size());
					for (int i = 0; i < resultL.size(); i++) {
						System.out.println("r" + i + " = "
								+ resultL.at(i).asString());
					}

					//this.tmpResultPath = resultL.at(resultL.size() - 1).asList()
					//this.tmpResultPath = resultL.at(resultL.size() - 1).asList().at("JSON").asString();
					//System.out.println("JSON R-RESULT = " + this.tmpResultPath);
					
					//parse result to geojson
					URL shpUrl = getClass().getResource("/outputs/tmpRlt.shp");
				    
					//File file = new File("/Users/yiqunc/githubRepositories/thirdparty-analytics/src/main/resources/outputs/newDataFrames.shp");				
					if(shpUrl != null)
					{
						File file = new File(shpUrl.getFile());
					try {
						System.out.println("shp file feature JSON =======1");
						FileDataStore store = FileDataStoreFinder.getDataStore(file);
						System.out.println("shp file feature JSON =======2");
						SimpleFeatureSource featureSource = store.getFeatureSource();
						System.out.println("shp file feature JSON =======3");
						//SimpleFeatureCollection simpleFeatureCollection = featureSource.getFeatures();	
						//FeatureJSON fjson = new FeatureJSON();	
						//String rltString =  fjson.toString(simpleFeatureCollection);
						//System.out.println("shp file feature JSON  = " + rltString);						
					}catch (Exception e)
					{
						e.printStackTrace();
					}
					}
					else
					{
						System.out.println("not found =======");
						
					}
					
					/*
					 * JSONObject jsonInputs; JSON objInputs =
					 * JSONSerializer.toJSON(resultL.at(0) .asList());
					 * System.out.println("jsonInput object = " + "\n" +
					 * objInputs.toString()); JSONObject joInputs =
					 * JSONObject.fromObject(objInputs); System.out .println(
					 * "json INPUT conversion from String to JAVA JSON OBJECT: "
					 * + "\n" + joInputs.toString());
					 */
//					JSONObject jsonOutputs;
//					JSON objOutputs = JSONSerializer.toJSON(resultL
//							.at(resultL.size() - 1).asList().at("JSON")
//							.asString());
//					System.out.println("jsonOutput object from R = " + "\n"
//							+ objOutputs.toString());
//					this.jsonResult = new String(objOutputs.toString());
//
//					JSONArray joOutputs = JSONArray.fromObject(objOutputs);
//					System.out
//							.println("json OUTPUT conversion from String to JAVA JSON OBJECT: "
//									+ "\n" + joOutputs.toString());
				}
			}
		} catch (REXPMismatchException me) {
			me.printStackTrace();
		}

	}
}
