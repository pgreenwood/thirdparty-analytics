package au.edu.uq.tests;

import java.io.IOException;
import junit.framework.Assert;
import org.junit.Test;

import au.org.aurin.ands.emp.preload.LoadRScriptEmpcluster;

public class LoadRScriptTest {

	/**
	 * {@link LoadRScriptEmpcluster} unit test
	 */
	@Test
	public void scriptLoaderTest() {

		try {
			String[] rScript = LoadRScriptEmpcluster.getScripts();
			Assert.assertNotNull("Unable to load the R source script", rScript);
			Assert.assertEquals(LoadRScriptEmpcluster.numberOfScriptsLoaded(), rScript.length);

			for (int i = 0; i < rScript.length; i++) {
				System.out.println("Script: " + "\n" + rScript[i]);
			}
			System.out.println(LoadRScriptEmpcluster.numberOfScriptsLoaded() + " Scripts Loaded");
			
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("Unable to load the R scripts");
		}
	}

}
