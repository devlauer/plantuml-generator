package de.elnarion.util.plantuml.generator.sequencediagram;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import de.elnarion.test.sequence.t0001.CallerA;
import de.elnarion.test.sequence.t0002.CallerClassA;
import de.elnarion.test.sequence.t0003.MovieService;
import de.elnarion.test.sequence.t0004.User;
import de.elnarion.util.plantuml.generator.sequencediagram.PlantUMLSequenceDiagramGenerator;
import de.elnarion.util.plantuml.generator.sequencediagram.config.PlantUMLSequenceDiagramConfigBuilder;
import de.elnarion.util.plantuml.generator.sequencediagram.exception.NotFoundException;

class PlantUMLSequenceDiagramGeneratorTest {

	private ClassLoader classLoader = this.getClass().getClassLoader();

	@Test
	void test0001BasicSequenceDiagram() throws NotFoundException, IOException {
		// tag::basecallersequence[]
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(CallerA.class.getName(), // <1>
				"callSomething"); // <2>
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build()); // <3>
		String expectedDiagramText = IOUtils.toString(classLoader.getResource("sequence/0001_basic_caller_test.txt"),
				StandardCharsets.UTF_8);

		// ACT
		String generatedDiagram = generator.generateDiagramText(); // <4>

		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram), () -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
				generatedDiagram.replaceAll("\\s+", "")));
		// end::basecallersequence[]
	}

	@Test
	void test0001BasicSequenceDiagramWithLongClassNames() throws NotFoundException, IOException {
		// tag::baselongclassnames[]
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(CallerA.class.getName(),
				"callSomething").withUseShortClassName(false); // <1>
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(
				classLoader.getResource("sequence/0001_basic_caller_with_long_class_names.txt"),
				StandardCharsets.UTF_8);

		// ACT
		String generatedDiagram = generator.generateDiagramText();

		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram), () -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
				generatedDiagram.replaceAll("\\s+", "")));
		// end::baselongclassnames[]
	}

	@Test
	void test0001BasicSequenceDiagramWithShowReturnType() throws NotFoundException, IOException {
		// tag::baseshowreturntypes[]
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(CallerA.class.getName(),
				"callSomething").withShowReturnTypes(true); // <1>
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(
				classLoader.getResource("sequence/0001_basic_caller_test_with_return_types.txt"),
				StandardCharsets.UTF_8);

		// ACT
		String generatedDiagram = generator.generateDiagramText();

		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram), () -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
				generatedDiagram.replaceAll("\\s+", "")));
		// end::baseshowreturntypes[]
	}

	@Test
	void test0001BasicSequenceDiagramWithShowReturnTypeAndLongClassNames() throws NotFoundException, IOException {
		// tag::baseshowreturntypesandlongclassnames[]
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(CallerA.class.getName(),
				"callSomething").withShowReturnTypes(true).withUseShortClassName(false);
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(
				classLoader.getResource("sequence/0001_basic_caller_test_with_return_types_and_long_class_names.txt"),
				StandardCharsets.UTF_8);

		// ACT
		String generatedDiagram = generator.generateDiagramText();

		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram), () -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
				generatedDiagram.replaceAll("\\s+", "")));
		// end::baseshowreturntypesandlongclassnames[]
	}

	@Test
	void test0002BasicSuperClassSequenceDiagram() throws NotFoundException, IOException {
		// tag::superclasses[]
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(
				CallerClassA.class.getName(), "testSomething");
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(
				classLoader.getResource("sequence/0002_basic_super_class_sequence_diagram.txt"),
				StandardCharsets.UTF_8);

		// ACT
		String generatedDiagram = generator.generateDiagramText();

		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram), () -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
				generatedDiagram.replaceAll("\\s+", "")));
		// end::superclasses[]
	}

	@Test
	void test0002BasicSuperClassSequenceDiagramWithHideSuperClass() throws NotFoundException, IOException {
		// tag::superclassesandhidesuperclasses[]
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(
				CallerClassA.class.getName(), "testSomething").withHideSuperClass(true); // <1>
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(
				classLoader.getResource("sequence/0002_basic_super_class_sequence_diagram_with_hide_super_class.txt"),
				StandardCharsets.UTF_8);

		// ACT
		String generatedDiagram = generator.generateDiagramText();

		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram), () -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
				generatedDiagram.replaceAll("\\s+", "")));
		// end::superclassesandhidesuperclasses[]
	}

	@Test
	void test0003JPASequenceDiagramWithoutOptions() throws IOException, NotFoundException {
		// tag::jpa[]
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(
				MovieService.class.getName(), "doSomeBusiness");
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(
				classLoader.getResource("sequence/0003_jpa_test_without_options.txt"), StandardCharsets.UTF_8);

		// ACT
		String generatedDiagram = generator.generateDiagramText();

		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram), () -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
				generatedDiagram.replaceAll("\\s+", "")));
		// end::jpa[]
	}

	@Test
	void test0003JPASequenceDiagramWithStandardClasses() throws IOException, NotFoundException {
		// tag::jpawithignorestandardclasses[]
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(
				MovieService.class.getName(), "doSomeBusiness").withIgnoreStandardClasses(false); // <1>
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(
				classLoader.getResource("sequence/0003_jpa_test_with_standard_classes.txt"), StandardCharsets.UTF_8);

		// ACT
		String generatedDiagram = generator.generateDiagramText();

		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram), () -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
				generatedDiagram.replaceAll("\\s+", "")));
		// end::jpawithignorestandardclasses[]
	}

	@Test
	void test0003JPASequenceDiagramWithIgnoreJPAEntities() throws IOException, NotFoundException {
		// tag::jpawithignoreentities[]
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(
				MovieService.class.getName(), "doSomeBusiness").withIgnoreJPAEntities(true); // <1>
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(
				classLoader.getResource("sequence/0003_jpa_test_with_ignore_jpa_entities.txt"), StandardCharsets.UTF_8);

		// ACT
		String generatedDiagram = generator.generateDiagramText();

		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram), () -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
				generatedDiagram.replaceAll("\\s+", "")));
		// end::jpawithignoreentities[]
	}

	@Test
	void test0003JPASequenceDiagramWithIgnoreJPAEntitiesAndHideMethodNames() throws IOException, NotFoundException {
		// tag::jpawithignoreentitiesandhidemethodnames[]
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(
				MovieService.class.getName(), "doSomeBusiness").withIgnoreJPAEntities(true).withHideMethodName(true); // <1>
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(
				classLoader.getResource("sequence/0003_jpa_test_with_ignore_jpa_entities_and_hide_method_names.txt"),
				StandardCharsets.UTF_8);

		// ACT
		String generatedDiagram = generator.generateDiagramText();

		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram), () -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
				generatedDiagram.replaceAll("\\s+", "")));
		// end::jpawithignoreentitiesandhidemethodnames[]
	}

	@Test
	void test0004SequenceDiagramWithBlacklistedClasses() throws IOException, NotFoundException {
		// tag::blacklistedclasses[]
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(User.class.getName(),
				"interaction").withClassBlacklistRegexp(".*(Controller|Model)"); // <1>
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(
				classLoader.getResource("sequence/0004_sequence_diagram_with_blacklisted_classes.txt"),
				StandardCharsets.UTF_8);

		// ACT
		String generatedDiagram = generator.generateDiagramText();

		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram), () -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
				generatedDiagram.replaceAll("\\s+", "")));
		// end::blacklistedclasses[]
	}

	@Test
	void test0004SequenceDiagramWithBlacklistedMethod() throws IOException, NotFoundException {
		// tag::blacklistedmethods[]
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(User.class.getName(),
				"interaction").withMethodBlacklistRegexp("getData"); // <1>
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(
				classLoader.getResource("sequence/0004_sequence_diagram_with_blacklisted_method.txt"),
				StandardCharsets.UTF_8);

		// ACT
		String generatedDiagram = generator.generateDiagramText();

		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram), () -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
				generatedDiagram.replaceAll("\\s+", "")));
		// end::blacklistedmethods[]
	}

	@Test
	void test0004Classloader() throws Exception {
		// tag::customclassloader[]
		String testClassPath = "file:///" + System.getProperty("user.dir") + "/src/test/classes/";
		URL[] classesURLs = new URL[] { new URL(testClassPath) };
		URLClassLoader customClassLoader = new URLClassLoader(classesURLs); // <1>
		customClassLoader.loadClass("de.elnarion.test.sequence.t0004.User");
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(User.class.getName(),
				"interaction").withClassloader(customClassLoader); // <2>

		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(
				classLoader.getResource("sequence/0004_sequence_diagram_with_custom_classloader.txt"),
				StandardCharsets.UTF_8);

		// ACT
		String generatedDiagram = generator.generateDiagramText();

		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram), () -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
				generatedDiagram.replaceAll("\\s+", "")));
		// end::customclassloader[]
	}

}
