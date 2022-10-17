package de.elnarion.util.plantuml.generator;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import de.elnarion.util.plantuml.generator.classdiagram.ClassifierType;
import de.elnarion.util.plantuml.generator.classdiagram.VisibilityType;
import de.elnarion.util.plantuml.generator.config.PlantUMLClassDiagramConfig;
import de.elnarion.util.plantuml.generator.config.PlantUMLClassDiagramConfigBuilder;

/**
 * The Class PlantUMLClassDiagramGeneratorTest tests the
 * {@link PlantUMLClassDiagramGenerator}.
 */
class PlantUMLClassDiagramGeneratorTest {

	private ClassLoader classLoader = this.getClass().getClassLoader();

	/**
	 * Test generate diagram with a normal test case with different linked classes
	 * and compares the result with the text of the file 0001_general_diagram.txt.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Test
	void test0001GenerateDiagram() throws IOException, ClassNotFoundException {
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("de.elnarion.test.domain.t0001");
		List<String> hideClasses = new ArrayList<>();
		hideClasses.add("de.elnarion.test.domain.ChildB");
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(classLoader,
				scanPackages, null, hideClasses, false, false);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils
				.toString(classLoader.getResource("0001_general_diagram.txt"), StandardCharsets.UTF_8);
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
	void test0002ClassTypes() throws IOException, ClassNotFoundException {
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("de.elnarion.test.domain.t0002");
		List<String> hideClasses = new ArrayList<>();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(classLoader,
				scanPackages, null, hideClasses, false, false);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils
				.toString(classLoader.getResource("0002_class_types.txt"), StandardCharsets.UTF_8);
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
	void test0003ClassRelationships() throws IOException, ClassNotFoundException {
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("de.elnarion.test.domain.t0003");
		List<String> hideClasses = new ArrayList<>();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(classLoader,
				scanPackages, null, hideClasses, false, false);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils
				.toString(classLoader.getResource("0003_class_relationships.txt"), StandardCharsets.UTF_8);
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
	void test0004ClassFields() throws IOException, ClassNotFoundException {
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("de.elnarion.test.domain.t0004");
		List<String> hideClasses = new ArrayList<>();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(classLoader,
				scanPackages, null, hideClasses, false, false);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils
				.toString(classLoader.getResource("0004_class_fields.txt"), StandardCharsets.UTF_8);
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
	void test0005ClassMethods() throws IOException, ClassNotFoundException {
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("de.elnarion.test.domain.t0005");
		List<String> hideClasses = new ArrayList<>();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(classLoader,
				scanPackages, null, hideClasses, false, false);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils
				.toString(classLoader.getResource("0005_class_methods.txt"), StandardCharsets.UTF_8);
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
	void test0006DifferentPackages() throws IOException, ClassNotFoundException {
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("de.elnarion.test.domain.t0006.pck1");
		scanPackages.add("de.elnarion.test.domain.t0006.pck2");
		List<String> hideClasses = new ArrayList<>();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(classLoader,
				scanPackages, null, hideClasses, false, false);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils
				.toString(classLoader.getResource("0006_different_packages.txt"), StandardCharsets.UTF_8);
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
	void test0007HideParameters() throws IOException, ClassNotFoundException {
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("de.elnarion.test.domain.t0007");
		List<String> hideClasses = new ArrayList<>();
		hideClasses.add("de.elnarion.test.domain.t0007.ClassB");
		hideClasses.add("de.elnarion.test.domain.t0007.ClassC");
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(classLoader,
				scanPackages, null, hideClasses, true, true);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils
				.toString(classLoader.getResource("0007_hide_parameters.txt"), StandardCharsets.UTF_8);
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
	void test0008Classloader() throws Exception {
		String filename = "0008_classloader_test.txt";
		String testClassPath = "file:///" + System.getProperty("user.dir") + "/src/test/classes/";
		URL[] classesURLs = new URL[] { new URL(testClassPath) };
		URLClassLoader customClassLoader = new URLClassLoader(classesURLs);
		customClassLoader.loadClass("de.elnarion.maven.plugin.plantuml.generator.test.domain.ChildA");
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("de.elnarion.maven.plugin.plantuml.generator.test.domain");
		List<String> hideClasses = new ArrayList<>();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(customClassLoader, scanPackages, null,
				hideClasses, true, true);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils.toString(classLoader.getResource(filename), StandardCharsets.UTF_8);
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
	void test0009JarPackage() throws Exception {
		String filename = "0009_jar_test.txt";
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("org.apache.commons.io.monitor");
		List<String> hideClasses = new ArrayList<>();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(classLoader,
				scanPackages, null, hideClasses, true, true);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils.toString(classLoader.getResource(filename), StandardCharsets.UTF_8);
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
	void test0010ParameterizedAggregationType() throws IOException, ClassNotFoundException {
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("de.elnarion.test.domain.t0010");
		List<String> hideClasses = new ArrayList<>();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(classLoader,
				scanPackages, null, hideClasses, true, true);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils.toString(
				this.getClass().getClassLoader().getResource("0010_parameterized_aggregation_type.txt"), StandardCharsets.UTF_8);
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
	void test0011JarPackageWithBlacklist() throws Exception {
		String filename = "0011_jar_test_blacklist.txt";
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("org.apache.commons.io.monitor");
		List<String> hideClasses = new ArrayList<>();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(classLoader,
				scanPackages, ".*FileEn.*", hideClasses, true, true);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils.toString(classLoader.getResource(filename), StandardCharsets.UTF_8);
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
	void test0012JarPackageWithWhitelist() throws Exception {
		String filename = "0012_jar_whitelist.txt";
		List<String> hideClasses = new ArrayList<>();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(classLoader,
				"org\\.apache\\.commons\\.io.*FileAl.*", hideClasses, true, true, null);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils.toString(classLoader.getResource(filename), StandardCharsets.UTF_8);
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	@Test
	void test0013MaxVisibilityFields() throws Exception {
		String filename = "0013_max_visibility_fields_public.txt";
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("de.elnarion.test.domain.t0013");
		PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
				.withClassLoader(classLoader)
				.withMaximumFieldVisibility(VisibilityType.PUBLIC).build();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils.toString(classLoader.getResource(filename), StandardCharsets.UTF_8);
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
		config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
				.withClassLoader(classLoader)
				.withMaximumFieldVisibility(VisibilityType.PROTECTED).build();
		generator = new PlantUMLClassDiagramGenerator(config);
		result = generator.generateDiagramText();
		filename = "0013_max_visibility_fields_protected.txt";
		expectedDiagramText = IOUtils.toString(classLoader.getResource(filename), StandardCharsets.UTF_8);
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
		config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
				.withClassLoader(classLoader)
				.withMaximumFieldVisibility(VisibilityType.PACKAGE_PRIVATE).build();
		generator = new PlantUMLClassDiagramGenerator(config);
		result = generator.generateDiagramText();
		filename = "0013_max_visibility_fields_package_private.txt";
		expectedDiagramText = IOUtils.toString(classLoader.getResource(filename), StandardCharsets.UTF_8);
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
		config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
				.withClassLoader(classLoader)
				.withMaximumFieldVisibility(VisibilityType.PRIVATE).build();
		generator = new PlantUMLClassDiagramGenerator(config);
		result = generator.generateDiagramText();
		filename = "0013_max_visibility_fields_private.txt";
		expectedDiagramText = IOUtils.toString(classLoader.getResource(filename), StandardCharsets.UTF_8);
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	@Test
	void test0014MaxVisibilityMethods() throws Exception {
		String filename = "0014_max_visibility_methods_public.txt";
		classLoader.loadClass("de.elnarion.test.domain.t0014.Testclass");
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("de.elnarion.test.domain.t0014");
		PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
				.withClassLoader(classLoader)
				.withMaximumMethodVisibility(VisibilityType.PUBLIC).build();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils.toString(classLoader.getResource(filename), StandardCharsets.UTF_8);
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
		config = new PlantUMLClassDiagramConfigBuilder(scanPackages).
				withClassLoader(classLoader)
				.withMaximumMethodVisibility(VisibilityType.PROTECTED).build();
		generator = new PlantUMLClassDiagramGenerator(config);
		result = generator.generateDiagramText();
		filename = "0014_max_visibility_methods_protected.txt";
		expectedDiagramText = IOUtils.toString(classLoader.getResource(filename), StandardCharsets.UTF_8);
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
		config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
				.withClassLoader(classLoader)
				.withMaximumMethodVisibility(VisibilityType.PACKAGE_PRIVATE).build();
		generator = new PlantUMLClassDiagramGenerator(config);
		result = generator.generateDiagramText();
		filename = "0014_max_visibility_methods_package_private.txt";
		expectedDiagramText = IOUtils.toString(classLoader.getResource(filename), StandardCharsets.UTF_8);
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
		config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
				.withClassLoader(classLoader)
				.withMaximumMethodVisibility(VisibilityType.PRIVATE).build();
		generator = new PlantUMLClassDiagramGenerator(config);
		result = generator.generateDiagramText();
		filename = "0014_max_visibility_methods_private.txt";
		expectedDiagramText = IOUtils.toString(classLoader.getResource(filename), StandardCharsets.UTF_8);
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	@Test
	void test0015RemoveMethods() throws Exception {
		String filename = "0015_remove_methods.txt";
		classLoader.loadClass("de.elnarion.test.domain.t0015.Testclass1");
		classLoader.loadClass("de.elnarion.test.domain.t0015.Testclass2");
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("de.elnarion.test.domain.t0015");
		PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
				.withClassLoader(classLoader)
				.withRemoveMethods(true).build();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils.toString(classLoader.getResource(filename), StandardCharsets.UTF_8);
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	@Test
	void test0016RemoveFields() throws Exception {
		String filename = "0016_remove_fields.txt";
		classLoader.loadClass("de.elnarion.test.domain.t0016.Testclass1");
		classLoader.loadClass("de.elnarion.test.domain.t0016.Testclass2");
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("de.elnarion.test.domain.t0016");
		PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
				.withClassLoader(classLoader)
				.withRemoveFields(true).build();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils.toString(classLoader.getResource(filename), StandardCharsets.UTF_8);
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	@Test
	void test0017BlacklistMethods() throws Exception {
		String filename = "0017_blacklist_methods.txt";
		classLoader.loadClass("de.elnarion.test.domain.t0017.Testclass1");
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("de.elnarion.test.domain.t0017");
		PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
				.withClassLoader(classLoader)
				.withMethodBlacklistRegexp(".*doSomething1.*").build();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils.toString(classLoader.getResource(filename), StandardCharsets.UTF_8);
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	@Test
	void test0018BlacklistFields() throws Exception {
		String filename = "0018_blacklist_fields.txt";
		classLoader.loadClass("de.elnarion.test.domain.t0018.Testclass1");
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("de.elnarion.test.domain.t0018");
		PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
				.withClassLoader(classLoader)
				.withFieldBlacklistRegexp("test1").build();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils.toString(classLoader.getResource(filename), StandardCharsets.UTF_8);
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	@Test
	void test0019IgnoreClassifierFields() throws Exception {
		String filename = "0019_ignore_classifier_fields.txt";
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("de.elnarion.test.domain.t0019");
		PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
				.withClassLoader(classLoader)
				.addFieldClassifierToIgnore(ClassifierType.STATIC).build();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils.toString(classLoader.getResource(filename), StandardCharsets.UTF_8);
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	@Test
	void test0020IgnoreClassifierMethods() throws Exception {
		String filename = "0020_ignore_classifier_methods.txt";
		classLoader.loadClass("de.elnarion.test.domain.t0020.Testclass");
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("de.elnarion.test.domain.t0020");
		PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
				.withClassLoader(classLoader)
				.addMethodClassifierToIgnore(ClassifierType.STATIC).build();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils.toString(classLoader.getResource(filename), StandardCharsets.UTF_8);
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	@Test
	void test0021JPAAnnotations() throws Exception {
		String filename = "0021_jpa_annotations.txt";
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("de.elnarion.test.domain.t0021");
		PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
				.withClassLoader(classLoader)
				.withJPAAnnotations(true).build();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils.toString(classLoader.getResource(filename), StandardCharsets.UTF_8);
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	@Test
	void test0022PrivateFinalFields() throws Exception {
		String filename = "0022_private_final_field.txt";
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("de.elnarion.test.domain.t0022");
		PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(".*\\$.*|com.xx.common.converter.BeanConverter|.*\\.metamodel\\..*",scanPackages)
				.withClassLoader(classLoader)
				.withHideMethods(false)
				.withHideFieldsParameter(false)
				.withMaximumFieldVisibility(VisibilityType.PRIVATE)
				.withMaximumMethodVisibility(VisibilityType.PUBLIC)
				.withMethodBlacklistRegexp("(hashCode|equals)").build();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils.toString(classLoader.getResource(filename), StandardCharsets.UTF_8);
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}

	/**
	 * Test generate diagram with a normal test case with different linked classes and additional PlantUML configs
	 * and compares the result with the text of the file 0023_additional-plant-uml-configs.txt.
	 *
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Test
	void test0023GenerateDiagramWithAdditionalPlantUmlConfigs() throws IOException, ClassNotFoundException {
		List<String> scanPackages = new ArrayList<>();
		scanPackages.add("de.elnarion.test.domain.t0023");
		List<String> additionalPlantUmlConfigs = new ArrayList<>();
		additionalPlantUmlConfigs.add("left to right direction");
		additionalPlantUmlConfigs.add("scale 2/3");
		PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
				.withClassLoader(classLoader)
				.addAdditionalPlantUmlConfigs(additionalPlantUmlConfigs)
				.build();
		PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
		String result = generator.generateDiagramText();
		String expectedDiagramText = IOUtils
				.toString(classLoader.getResource("0023_additional-plant-uml-configs.txt"), "utf-8");
		assertNotNull(result);
		assertNotNull(expectedDiagramText);
		assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
	}
}
