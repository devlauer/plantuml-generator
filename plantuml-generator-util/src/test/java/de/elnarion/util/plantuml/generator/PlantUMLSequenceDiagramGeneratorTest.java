package de.elnarion.util.plantuml.generator;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import de.elnarion.test.sequence.t0001.CallerA;
import de.elnarion.test.sequence.t0003.CallerClassA;
import de.elnarion.util.plantuml.generator.config.PlantUMLSequenceDiagramConfigBuilder;
import javassist.CannotCompileException;
import javassist.NotFoundException;

class PlantUMLSequenceDiagramGeneratorTest {

	private ClassLoader classLoader = this.getClass().getClassLoader();

	@Test
	void testBasicSequenceDiagram() throws NotFoundException, CannotCompileException, IOException {
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(
				CallerA.class.getName(), "callSomething");
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(classLoader.getResource("sequence_0001_basic_caller_test.txt"),
				StandardCharsets.UTF_8);

		// ACT
		String generatedDiagram = generator.generateDiagramText();

		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram),
				() -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
						generatedDiagram.replaceAll("\\s+", "")));
	}

	@Test
	void testBasicSequenceDiagramWithReturnType() throws NotFoundException, CannotCompileException, IOException {
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(
				CallerA.class.getName(), "callSomething").withShowReturnTypes(true);
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(classLoader.getResource("sequence_0002_basic_caller_test_with_return_types.txt"),
				StandardCharsets.UTF_8);

		// ACT
		String generatedDiagram = generator.generateDiagramText();

		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram),
				() -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
						generatedDiagram.replaceAll("\\s+", "")));
	}

	@Test
	void testBasicSuperClassSequenceDiagram() throws NotFoundException, CannotCompileException, IOException {
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(
				CallerClassA.class.getName(), "testSomething");
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(classLoader.getResource("sequence_0003_basic_super_class_sequence_diagram.txt"),
				StandardCharsets.UTF_8);

		// ACT
		String generatedDiagram = generator.generateDiagramText();

		// ASSERT
		assertAll(() -> assertNotNull(generatedDiagram),
				() -> assertEquals(expectedDiagramText.replaceAll("\\s+", ""),
						generatedDiagram.replaceAll("\\s+", "")));
	}

	@Test
	void testBasicSuperClassSequenceDiagramWithHideSuperClass() throws NotFoundException, CannotCompileException, IOException {
		// ARRANGE
		PlantUMLSequenceDiagramConfigBuilder builder = new PlantUMLSequenceDiagramConfigBuilder(
				CallerClassA.class.getName(), "testSomething").withHideSuperClass(true);
		PlantUMLSequenceDiagramGenerator generator = new PlantUMLSequenceDiagramGenerator(builder.build());
		String expectedDiagramText = IOUtils.toString(classLoader.getResource("sequence_0004_basic_super_class_sequence_diagram_with_hide_super_class.txt"),
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
