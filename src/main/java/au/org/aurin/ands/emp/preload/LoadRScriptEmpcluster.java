package au.org.aurin.ands.emp.preload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link LoadRScriptEmpcluster} loads the following R-Scripts {@link File} as a 
 * {@link String} resource.
 *  
 * 
 * @author Irfan
 * 
 */
public class LoadRScriptEmpcluster {
  
  protected final static Logger LOG = LoggerFactory.getLogger(LoadRScriptEmpcluster.class);

	private static String[] rScriptResource = {
		"/geoJSON2DataFrame.r",
		"/wardsClustering.r",
		"/dataFrame2JSON.r"
	};
	
	private static String[] rScript;

	private LoadRScriptEmpcluster() {
		rScript = null;
	}


	/**
	 * {@link WardsClustering} analysis of explanatory and response variables
	 *  
	 * @return {@link String} representation of the WardsClustering R-Script
	 * @throws IOException
	 */
	public static String getWardsClusterScript() throws IOException {

		return getScripts()[1];
	}
	
	public static String getGeoJSON2DataFrameScript() throws IOException {

		return getScripts()[0];
	}

	public static String getDataFrame2JSONScript() throws IOException {

		return getScripts()[2];
	}
	
	/**
	 * The number of R scripts loaded by {@link LoadRScriptEmpcluster} class
	 * 
	 * @return total number of R-scripts loaded and available for use
	 */
	public static int numberOfScriptsLoaded() {
		
		return rScript.length;
	}
	
  /**
   * Represents all the algorithms implementation in R scripts as a
   * {@link String} array for each of the script
   * 
   * @return {@link String} array representation of R scripts available as a
   *         resource File
   * @throws IOException
   *             If unable to load the R resource File's
   */
  public static synchronized String[] getScripts() throws IOException {

    InputStream is = null;

    LOG.info("Start loading R-scripts");
    
    if (rScript == null) {

      try {
        rScript = new String[rScriptResource.length];
        for (int i = 0; i < rScriptResource.length; i++) {

          String resource = "/au/edu/uq/aurin/rscripts/" + rScriptResource[i];
          is = LoadRScriptEmpcluster.class.getResourceAsStream(resource);
          if (is == null) {
            throw new IOException("InputStream is = " + is);
          } else {
            // Assuming this is deployed on {L}unix also accepts
            // windows R-script files as input
            rScript[i] = IOUtils.toString(is).replaceAll(
                System.getProperty("line.separator"), IOUtils.LINE_SEPARATOR_UNIX);
            LOG.info("R-Script size: " + rScript[i].length() + " Bytes for "+ resource);
            is.close();
          }
        }
      } catch (IOException e) {
        throw new IOException("Unable to load read the resource file ");
      }
    } else {
       LOG.info(rScript.length + " R-scripts already loaded.");
    }
    
    LOG.info("Done loading R-scripts");
    return rScript;
  }

}
