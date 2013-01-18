package au.edu.uq.tests;

import java.io.IOException;
import junit.framework.Assert;
import org.junit.Test;

import au.edu.uq.preload.LoadRScript;

public class LoadRScriptTest {

	/**
	 * {@link LoadRScript} unit test
	 */
	@Test
	public void scriptLoaderTest() {

		try {
			String[] rScript = LoadRScript.getScripts();
			Assert.assertNotNull("Unable to load the R source script", rScript);
			Assert.assertEquals(LoadRScript.numberOfScriptsLoaded(), rScript.length);

			for (int i = 0; i < rScript.length; i++) {
				System.out.println("Script: " + "\n" + rScript[i]);
			}
			System.out.println(LoadRScript.numberOfScriptsLoaded() + " Scripts Loaded");
			
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("Unable to load the R scripts");
		}
	}

}
