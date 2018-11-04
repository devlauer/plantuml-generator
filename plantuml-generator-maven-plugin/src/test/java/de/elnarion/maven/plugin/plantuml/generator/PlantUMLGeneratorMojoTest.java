package de.elnarion.maven.plugin.plantuml.generator;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class PlantUMLGeneratorMojoTest makes a simple test of the Mojo
 * PlantUMLGeneratorMojo and checks if it could be loaded with a default
 * configuration.
 * 
 * No further testing is done here because the plugin needs a full maven
 * environment for its execution. This is done via integration test profile and
 * the maven invoker plugin (have a look at the pom.xml and the profile
 * run-its).
 */
public class PlantUMLGeneratorMojoTest extends AbstractMojoTestCase {

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	protected void setUp() throws Exception {
		// required for mojo lookups to work
		super.setUp();
	}

	/** {@inheritDoc} */
	@After
	protected void tearDown() throws Exception {
		// required
		super.tearDown();
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testMojoConvert() throws Exception {
		System.out.println("TEST MOJO");
		File testPom = new File(getBasedir(), "src/test/resources/unit/plantuml-generator/diagram1-test/pom.xml");
		assertNotNull(testPom);
		assertTrue(testPom.exists());

		PlantUMLGeneratorMojo mojo = (PlantUMLGeneratorMojo) lookupMojo("generate", testPom);
		assertNotNull(mojo);

	}
}
