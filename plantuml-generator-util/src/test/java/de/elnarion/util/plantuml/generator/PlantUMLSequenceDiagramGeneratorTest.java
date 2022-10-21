package de.elnarion.util.plantuml.generator;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import de.elnarion.test.sequence.t0001.CallerA;
import de.elnarion.test.sequence.t0002.CallerClassA;
import de.elnarion.test.sequence.t0003.MovieService;
import de.elnarion.test.sequence.t0004.User;
import de.elnarion.util.plantuml.generator.config.PlantUMLSequenceDiagramConfigBuilder;
import javassist.CannotCompileException;
import javassist.NotFoundException;

class PlantUMLSequenceDiagramGeneratorTest {

	private ClassLoader classLoader = this.getClass().getClassLoader();

	@Test
	void test0001BasicSequenceDiagram() throws NotFoundException, CannotCompileException, IOException, ClassNotFoundException {
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(
				CallerA.class.getName(), "callSomething");
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(classLoader.getResource("sequence/0001_basic_caller_test.txt"),
				StandardCharsets.UTF_8);

		// ACT
		String generatedDiagram = generator.generateDiagramText();

		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram),
				() -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
						generatedDiagram.replaceAll("\\s+", "")));
	}

	@Test
	void test0001BasicSequenceDiagramWithShowReturnType() throws NotFoundException, CannotCompileException, IOException, ClassNotFoundException {
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(
				CallerA.class.getName(), "callSomething").withShowReturnTypes(true);
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(classLoader.getResource("sequence/0001_basic_caller_test_with_return_types.txt"),
				StandardCharsets.UTF_8);

		// ACT
		String generatedDiagram = generator.generateDiagramText();

		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram),
				() -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
						generatedDiagram.replaceAll("\\s+", "")));
	}

	@Test
	void test0002BasicSuperClassSequenceDiagram() throws NotFoundException, CannotCompileException, IOException, ClassNotFoundException {
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(
				CallerClassA.class.getName(), "testSomething");
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(classLoader.getResource("sequence/0002_basic_super_class_sequence_diagram.txt"),
				StandardCharsets.UTF_8);

		// ACT
		String generatedDiagram = generator.generateDiagramText();

		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram),
				() -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
						generatedDiagram.replaceAll("\\s+", "")));
	}

	@Test
	void test0002BasicSuperClassSequenceDiagramWithHideSuperClass() throws NotFoundException, CannotCompileException, IOException, ClassNotFoundException {
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(
				CallerClassA.class.getName(), "testSomething").withHideSuperClass(true);
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(classLoader.getResource("sequence/0002_basic_super_class_sequence_diagram_with_hide_super_class.txt"),
				StandardCharsets.UTF_8);

		// ACT
		String generatedDiagram = generator.generateDiagramText();

		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram),
				() -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
						generatedDiagram.replaceAll("\\s+", "")));
	}
	
	@Test
	void test0003JPASequenceDiagramWithoutOptions() throws IOException, NotFoundException, CannotCompileException, ClassNotFoundException {
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(
				MovieService.class.getName(), "doSomeBusiness");
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(classLoader.getResource("sequence/0003_jpa_test_without_options.txt"),
				StandardCharsets.UTF_8);
		
		// ACT
		String generatedDiagram = generator.generateDiagramText();
		
		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram),
				() -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
						generatedDiagram.replaceAll("\\s+", "")));
	}

	@Test
	void test0003JPASequenceDiagramWithIgnoreJPAEntities() throws IOException, NotFoundException, CannotCompileException, ClassNotFoundException {
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(
				MovieService.class.getName(), "doSomeBusiness").withIgnoreJPAEntities(true);
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(classLoader.getResource("sequence/0003_jpa_test_with_ignore_jpa_entities.txt"),
				StandardCharsets.UTF_8);
		
		// ACT
		String generatedDiagram = generator.generateDiagramText();
		
		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram),
				() -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
						generatedDiagram.replaceAll("\\s+", "")));
	}

	@Test
	void test0003JPASequenceDiagramWithIgnoreJPAEntitiesAndHideMethodNames() throws IOException, NotFoundException, CannotCompileException, ClassNotFoundException {
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(
				MovieService.class.getName(), "doSomeBusiness").withIgnoreJPAEntities(true).withHideMethodName(true);
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(classLoader.getResource("sequence/0003_jpa_test_with_ignore_jpa_entities_and_hide_method_names.txt"),
				StandardCharsets.UTF_8);
		
		// ACT
		String generatedDiagram = generator.generateDiagramText();
		
		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram),
				() -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
						generatedDiagram.replaceAll("\\s+", "")));
	}

	@Test
	void test0004SequenceDiagramWithBlacklistedClasses() throws IOException, NotFoundException, CannotCompileException, ClassNotFoundException {
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(
				User.class.getName(), "interaction").withClassBlacklistRegexp(".*(Controller|Model)");
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(classLoader.getResource("sequence/0004_sequence_diagram_with_blacklisted_classes.txt"),
				StandardCharsets.UTF_8);
		
		// ACT
		String generatedDiagram = generator.generateDiagramText();
		
		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram),
				() -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
						generatedDiagram.replaceAll("\\s+", "")));
	}

	@Test
	void test0005SequenceDiagramWithBlacklistedMethod() throws IOException, NotFoundException, CannotCompileException, ClassNotFoundException {
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(
				User.class.getName(), "interaction").withMethodBlacklistRegexp("getData");
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(classLoader.getResource("sequence/0005_sequence_diagram_with_blacklisted_method.txt"),
				StandardCharsets.UTF_8);
		
		// ACT
		String generatedDiagram = generator.generateDiagramText();
		System.out.println(generatedDiagram);
		
		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram),
				() -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
						generatedDiagram.replaceAll("\\s+", "")));
	}
	
}
