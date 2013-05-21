package au.org.aurin.ands.emp;

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;


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
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPVector;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPLogical;


import au.edu.uq.interfaces.Statistics;
import au.edu.uq.preload.LoadRScriptEmpcluster;
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
	
	@Description("Input Integer for geo-distance threshold")
	@In
	public int geodisthreshold = 20;
	/**
	 * {@link int} Input Integer for geo-distance threshold
	 */
	
	@Description("Input Integer for target cluster number")
	@In
	public int targetclusternum = 1;
	/**
	 * {@link int} Input Integer for target cluster number
	 */
	
	@Description("Input String for interested column names")
	@In
	public String interestedColNamesString;
	/**
	 * {@link String} Input String for interested column names
	 */
	
	@Description("Input String for interested column weights")
	@In
	public String interestedColWeightsString;
	/**
	 * {@link String} Input String for interested column weights
	 */

	@Description("Input String for display column names string")
	@In
	public String displayColNamesString;
	/**
	 * {@link String} Input String for display column names string
	 */
	
	@Description("igore data row if job numbers in all interested columns are less than this value.")
	@In
	public double ignoreEmptyRowJobNum = 1;
	/**
	 * {@link double} igore data row if job numbers in all interested columns are less than this value
	 */
	
	@Description("perform clustering using value chain mode or not. if true, the interested columns will be added up into a new column called 'vcvalue', on which, the non-spatial distance will be computed and used as a factor to generate the final clustering result")
	@In
	public boolean vcmode = true;
	/**
	 * {@link boolean} perform clustering using value chain mode or not. if true, the interested columns will be added up into a new column called 'vcvalue', on which, the non-spatial distance will be computed and used as a factor to generate the final clustering resul
	 */
	
	@Description("Input String for spatial and non-spatial distance weights")
	@In
	public String spatialNonSpatialDistWeightsString;
	/**
	 * {@link String} Input String for spatial and non-spatial distance weights
	 */
		
	@Description("R connection pass on")
	@Out
	public RConnection cOut;
	
	@Execute
	public void compute() {
		try {

		  System.out.println("hashcode cIn in WardClustering: " + c.hashCode());
			// setup the script to execute
			// 1. load the required script
			try {
				this.c.assign("script", LoadRScriptEmpcluster.getWardsClusterScript());
			} catch (IOException e) {
				e.printStackTrace();
			}


			// 2. setup the inputs
			this.c.assign("geodisthreshold", new REXPInteger(this.geodisthreshold));
			this.c.assign("targetclusternum", new REXPInteger(this.targetclusternum));
			this.c.assign("displayColNames", new REXPString(this.interestedColNamesString.split(",")));
			this.c.assign("interestedColNames", new REXPString(this.interestedColNamesString.split(",")));
			
			double[] interestedColWeights = {};
			try {
				interestedColWeights = this.convertStringArraytoDoubleArray(this.interestedColWeightsString.split(","));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
			
			this.c.assign("interestedColWeights", new REXPDouble(interestedColWeights));
			
			double[] spatialNonSpatialDistWeights = {0.5, 0.5};
			try {
				spatialNonSpatialDistWeights = this.convertStringArraytoDoubleArray(this.spatialNonSpatialDistWeightsString.split(","));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.c.assign("spatialNonSpatialDistWeights", new REXPDouble(spatialNonSpatialDistWeights));
			this.c.assign("gIgnoreEmptyRowJobNum", new REXPDouble(this.ignoreEmptyRowJobNum));
			this.c.assign("gVcMode", new REXPLogical(this.vcmode));
			this.c.assign("gErrorOccurs", new REXPLogical(false));

			// 3. call the function defined in the script
			this.c.eval("try(eval(parse(text=script)),silent=FALSE)");
			this.cOut = this.c;
			System.out.println("hashcode in WardClustering: " + cOut.hashCode());
			return;	

		} catch (REngineException e) {
			e.printStackTrace();
		}
		
	}
	
	private double[] convertStringArraytoDoubleArray(String[] sarray) throws NumberFormatException {
		if (sarray != null) {
		double rltarray[] = new double[sarray.length];
		for (int i = 0; i < sarray.length; i++) {
			rltarray[i] = Double.parseDouble(sarray[i]);
		}
			return rltarray;
		}
			return null;
		}
	
}
