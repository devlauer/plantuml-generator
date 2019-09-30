package de.elnarion.util.plantuml.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * The Class PlantUMLClassDiagramGeneratorTest tests the
 * {@link PlantUMLClassDiagramGenerator}.
 */
public class PlantUMLClassDiagramGeneratorTest {

	/**
	 * Test generate diagram with a normal test case with different linked classes
	 * and compares the result with the text of the file 0001_general_diagram.txt.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Test
	public void test0001GenerateDiagram() throws IOException, ClassNotFoundException {
		List<String> scanpackages = new ArrayList<>();
		scanpackages.add("de.elnarion.test.domain.t0001");
		List<String> hideClasses = new ArrayList<>();
		hideClasses.add("de.elnarion.test.domain.ChildB");
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(this.getClass().getClassLoader(),
				scanpackages, null, hideClasses, false, false);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils
				.toString(this.getClass().getClassLoader().getResource("0001_general_diagram.txt"), "utf-8");
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	/**
	 * Test 0002 with different class types.
	 *
	 * @throws IOException            Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 */
	@Test
	public void test0002ClassTypes() throws IOException, ClassNotFoundException {
		List<String> scanpackages = new ArrayList<>();
		scanpackages.add("de.elnarion.test.domain.t0002");
		List<String> hideClasses = new ArrayList<>();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(this.getClass().getClassLoader(),
				scanpackages, null, hideClasses, false, false);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils
				.toString(this.getClass().getClassLoader().getResource("0002_class_types.txt"), "utf-8");
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	/**
	 * Test 0003 test different class relationships.
	 *
	 * @throws IOException            Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 */
	@Test
	public void test0003ClassRelationships() throws IOException, ClassNotFoundException {
		List<String> scanpackages = new ArrayList<>();
		scanpackages.add("de.elnarion.test.domain.t0003");
		List<String> hideClasses = new ArrayList<>();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(this.getClass().getClassLoader(),
				scanpackages, null, hideClasses, false, false);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils
				.toString(this.getClass().getClassLoader().getResource("0003_class_relationships.txt"), "utf-8");
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	/**
	 * Test 0004 test different field variations.
	 *
	 * @throws IOException            Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 */
	@Test
	public void test0004ClassFields() throws IOException, ClassNotFoundException {
		List<String> scanpackages = new ArrayList<>();
		scanpackages.add("de.elnarion.test.domain.t0004");
		List<String> hideClasses = new ArrayList<>();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(this.getClass().getClassLoader(),
				scanpackages, null, hideClasses, false, false);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils
				.toString(this.getClass().getClassLoader().getResource("0004_class_fields.txt"), "utf-8");
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	/**
	 * Test 0005 test different class method variations.
	 *
	 * @throws IOException            Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 */
	@Test
	public void test0005ClassMethods() throws IOException, ClassNotFoundException {
		List<String> scanpackages = new ArrayList<>();
		scanpackages.add("de.elnarion.test.domain.t0005");
		List<String> hideClasses = new ArrayList<>();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(this.getClass().getClassLoader(),
				scanpackages, null, hideClasses, false, false);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils
				.toString(this.getClass().getClassLoader().getResource("0005_class_methods.txt"), "utf-8");
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	/**
	 * Test 0006 test different packages as scan list.
	 *
	 * @throws IOException            Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 */
	@Test
	public void test0006DifferentPackages() throws IOException, ClassNotFoundException {
		List<String> scanpackages = new ArrayList<>();
		scanpackages.add("de.elnarion.test.domain.t0006.pck1");
		scanpackages.add("de.elnarion.test.domain.t0006.pck2");
		List<String> hideClasses = new ArrayList<>();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(this.getClass().getClassLoader(),
				scanpackages, null, hideClasses, false, false);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils
				.toString(this.getClass().getClassLoader().getResource("0006_different_packages.txt"), "utf-8");
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	/**
	 * Test 0007 test hide toggles.
	 *
	 * @throws IOException            Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 */
	@Test
	public void test0007HideParameters() throws IOException, ClassNotFoundException {
		List<String> scanpackages = new ArrayList<>();
		scanpackages.add("de.elnarion.test.domain.t0007");
		List<String> hideClasses = new ArrayList<>();
		hideClasses.add("de.elnarion.test.domain.t0007.ClassB");
		hideClasses.add("de.elnarion.test.domain.t0007.ClassC");
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(this.getClass().getClassLoader(),
				scanpackages, null, hideClasses, true, true);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils
				.toString(this.getClass().getClassLoader().getResource("0007_hide_parameters.txt"), "utf-8");
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	/**
	 * Test 0010 test parameterized aggregation type.
	 *
	 * @throws IOException            Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 */
	@Test
	public void test0010ParameterizedAggregationType() throws IOException, ClassNotFoundException {
		List<String> scanpackages = new ArrayList<>();
		scanpackages.add("de.elnarion.test.domain.t0010");
		List<String> hideClasses = new ArrayList<>();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(this.getClass().getClassLoader(),
				scanpackages, null, hideClasses, true, true);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils.toString(
				this.getClass().getClassLoader().getResource("0010_parameterized_aggregation_type.txt"), "utf-8");
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	/**
	 * Test custom classloader.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testClassloader() throws Exception {
		String filename = "0008_classloader_test.txt";
		URL testResourceURL = this.getClass().getClassLoader().getResource(filename);
		String testResourceURLString = testResourceURL.toString();
		String testClassPath = testResourceURLString.substring(0, testResourceURLString.length() - filename.length())
				+ "../../src/test/classes/";
		URL[] classesURLs = new URL[] { new URL(testClassPath) };
		URLClassLoader classLoader = new URLClassLoader(classesURLs);
		classLoader.loadClass("de.elnarion.maven.plugin.plantuml.generator.test.domain.ChildA");
		List<String> scanpackages = new ArrayList<>();
		scanpackages.add("de.elnarion.maven.plugin.plantuml.generator.test.domain");
		List<String> hideClasses = new ArrayList<>();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(classLoader, scanpackages, null,
				hideClasses, true, true);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils.toString(this.getClass().getClassLoader().getResource(filename), "utf-8");
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	/**
	 * Test classes contained in a package in a jar.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testJarPackage() throws Exception {
		String filename = "0009_jar_test.txt";
		List<String> scanpackages = new ArrayList<>();
		scanpackages.add("org.apache.commons.io.monitor");
		List<String> hideClasses = new ArrayList<>();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(this.getClass().getClassLoader(),
				scanpackages, null, hideClasses, true, true);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils.toString(this.getClass().getClassLoader().getResource(filename), "utf-8");
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	/**
	 * Test classes contained in a package in a jar.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testJarPackageWithBlacklist() throws Exception {
		String filename = "0011_jar_test_blacklist.txt";
		List<String> scanpackages = new ArrayList<>();
		scanpackages.add("org.apache.commons.io.monitor");
		List<String> hideClasses = new ArrayList<>();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(this.getClass().getClassLoader(),
				scanpackages, ".*FileEn.*", hideClasses, true, true);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils.toString(this.getClass().getClassLoader().getResource(filename), "utf-8");
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	/**
	 * Test classes contained in a package in a jar.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testJarPackageWithWithelist() throws Exception {
		String filename = "0012_jar_whitelist.txt";
		List<String> scanpackages = new ArrayList<>();
		scanpackages.add("org.apache.commons.io.monitor");
		List<String> hideClasses = new ArrayList<>();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(this.getClass().getClassLoader(),
				".*FileAl.*", hideClasses, true, true,null);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils.toString(this.getClass().getClassLoader().getResource(filename), "utf-8");
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}
	
}
