package au.org.aurin.ands.emp;

import java.io.IOException;

import oms3.annotations.Description;
import oms3.annotations.Execute;
import oms3.annotations.In;
import oms3.annotations.Name;
import oms3.annotations.Out;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPLogical;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.aurin.ands.emp.preload.LoadRScriptEmpcluster;
import au.org.aurin.ands.emp.preload.Rserve;

public class WardsClustering {
  
  private static final Logger LOGGER = LoggerFactory
      .getLogger(WardsClustering.class);
	
  @In
	@Description("Input R connection")
	public RConnection c;
	/**
	 * {@link RConnection} A valid connection to a running {@link Rserve}
	 * instance
	 */
	
  @In
  @Name("Geo-Distance Threshold")
	@Description("Set maximum distance beyond which polygons will not merge.")
	public int geodisthreshold = 20;
	/**
	 * {@link int} Input Integer for geo-distance threshold
	 */
	
  @In
  @Name("Target Cluster Number")
	@Description("Set minimum cluster number at which algorithm will stop.")
	public int targetclusternum = 1;
	/**
	 * {@link int} Input Integer for target cluster number
	 */
	
  @In
  @Name("Non-Spatial Attribute Selection")
	@Description("Select all non-spatial attributes required for analysis.")
	public String interestedColNamesString;
	/**
	 * {@link String} Input String for interested column names
	 */
	
  @In
  @Name("Non-Spatial Attribute Weights")
	@Description("Insert comma separated values. Values must sum to 1.")
	public String interestedColWeightsString;
	/**
	 * {@link String} Input String for interested column weights
	 */

  @In
  @Name("Additional Attributes For Display")
	@Description("Additional attributes for display in dataset tabular output.")
	public String displayColNamesString;
	/**
	 * {@link String} Input String for display column names string
	 */
	
  @In
  @Name("Non-Spatial Attribute Minimum Count")
	@Description("Select minimum non-spatial attribute for polygons to be included in cluster analysis.")
	public double ignoreEmptyRowJobNum = 1;
	/**
	 * {@link double} ignore data row if job numbers in all interested columns are less than this value
	 */
	
  @In
  @Name("Value Chain Mode")
	@Description("Perform clustering using value chain mode or not. " +
			"If false, the non-spatial attributes will be added up into a new column called 'vcvalue', " +
			"on which, the non-spatial distance will be computed and used as a factor to generate the final clustering result")
	public boolean vcmode = true;
	/**
	 * {@link boolean} perform clustering using value chain mode or not. if true, the interested columns will be added up into a new column called 'vcvalue', on which, the non-spatial distance will be computed and used as a factor to generate the final clustering resul
	 */
	
  @In
  @Name("Spatial vs Non-Spatial Distance Weights")
	@Description("Insert comma separated values. Values must sum to 1.")
	public String spatialNonSpatialDistWeightsString;
	/**
	 * {@link String} Input String for spatial and non-spatial distance weights
	 */
		
	@Description("R Connection output")
	@Out
	public RConnection cOut;
	
	@Execute
	public void compute() throws REXPMismatchException {
		try {
		  
		  LOGGER.debug("compute executed");

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
			
			
			//this.c.eval("try(eval(parse(text=script)),silent=FALSE)");
			this.cOut = this.c;
			LOGGER.debug("executeing eval");
			REXP r = this.cOut.eval("try(eval(parse(text=script)),silent=FALSE)");
			LOGGER.debug("eval executed");
      if (r.inherits("try-error")) throw new IllegalStateException(r.asString());
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
