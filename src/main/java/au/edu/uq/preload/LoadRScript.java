package au.edu.uq.preload;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import au.edu.uq.classifiers.EqualInterval;
import au.edu.uq.classifiers.Interval;
import au.edu.uq.classifiers.QuantileInterval;
import au.edu.uq.statistics.GeneralisedLinearModel;
import au.edu.uq.statistics.NewRegression;
import au.edu.uq.statistics.PrincipalComponentAnalysis;
import au.edu.uq.statistics.ClusterAnalysisHierarchical;
import au.edu.uq.statistics.ClusterAnalysisKmeans;
import au.edu.uq.statistics.Regression;
import au.edu.uq.summaries.SummaryA;
import au.edu.uq.statistics.WinsorA;

/**
 * {@link LoadRScript} loads the following R-Scripts {@link File} as a 
 * {@link String} resource.
 *  
 * <p>Data Classifiers:
 * {@link EqualInterval}, {@link QuantileInterval}, {@link Interval} </p>
 * 
 * <p>Linear Model:
 * {@link Regression}, {@link GeneralisedLinearModel} </p>
 * 
 * <p>Component Analysis: 
 * {@link PrincipalComponentAnalysis} </p> 
 * 
 * <p>Cluster Analysis:
 * {@link ClusterAnalysisHierarchical}, {@link ClusterAnalysisKmeans} </p>
 * 
 * <p>Summary Statistic:
 * {@link SummaryA} </p>
 * 
 * @author irfan
 * 
 */
public class LoadRScript {

	private static String[] rScriptResource = {
			"/wards_v2.0.r"
	};
	
	private static String[] rScript;

	private LoadRScript() {
		rScript = null;
	}


	/**
	 * {@link WardsClustering} analysis of explanatory and response variables
	 *  
	 * @return {@link String} representation of the WardsClustering R-Script
	 * @throws IOException
	 */
	public static String getWardsClusterScript() throws IOException {

		return getScripts()[0];
	}

	/**
	 * The number of R scripts loaded by {@link LoadRScript} class
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
	 *         resource {@link File}
	 * @throws IOException
	 *             If unable to load the R resource {@link File}'s
	 */
	public static synchronized String[] getScripts() throws IOException {

		URL url = null;
		File f = null;

		if (rScript == null) {
			try {
				rScript = new String[rScriptResource.length];

				for (int i = 0; i < rScriptResource.length; i++) {
					url = LoadRScript.class.getResource(rScriptResource[i]);
					if (url == null)
						throw new IOException("url = " + url);

					f = FileUtils.toFile(url);
					if (f == null)
						throw new IOException("file f = " + f);

					rScript[i] = FileUtils.readFileToString(f);
//					System.out.println(rScript[i] + "\n Rscript loaded." + 
//							 		" script size = " + rScript[i].length());
				}
			} catch (IOException e) {
				throw new IOException("Unable to load read the resource file ");
			}
		} else {
//			System.out.println(rScript.length + " Rscripts already loaded.");
		}
		return rScript;
	}

}
