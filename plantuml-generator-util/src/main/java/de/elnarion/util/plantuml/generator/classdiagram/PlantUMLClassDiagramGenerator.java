package de.elnarion.util.plantuml.generator.classdiagram;

import java.io.IOException;

import de.elnarion.util.plantuml.generator.classdiagram.config.PlantUMLClassDiagramConfig;
import de.elnarion.util.plantuml.generator.classdiagram.internal.PlantUMLClassDiagramAnalyzeSummary;
import de.elnarion.util.plantuml.generator.classdiagram.internal.PlantUMLClassDiagramAnalyzerAndMapper;
import de.elnarion.util.plantuml.generator.classdiagram.internal.PlantUMLClassDiagramTextBuilder;

/**
 * This class provides the ability to generate a PlantUML class diagram out of a
 * list of package names. Therefore this class scans directories and jars for
 * all classes contained directly in these packages and generates a class
 * diagram out of them via reflection
 * 
 * To be able to get the right classes you have to provide the necessary
 * ClassLoader, which is able to load these classes.
 * 
 * Currently this generator supports the following class types:
 * <ul>
 * <li>class</li>
 * <li>abstract class</li>
 * <li>annotation</li>
 * <li>enum</li>
 * <li>interface</li>
 * </ul>
 * 
 * with some restrictions:
 * 
 * The type annotation does not contain any further information than its name
 * and no Annotation relationships are included in the diagram.
 * 
 * The type enum contains only the constants.
 * 
 * 
 * All other types contain field and method informations.
 * 
 * Relationships are included for
 * <ul>
 * <li>inheritance</li>
 * <li>realization</li>
 * <li>aggregation (only for List and Set types)</li>
 * <li>usage (only direct usage via field)
 * </ul>
 * 
 * You can hide all fields via hideFields parameter or you can hide all methods
 * via hideMethods parameter.
 * 
 * If you want to hide a list of classes you have to provide a String List with
 * full qualified class names.
 */
public class PlantUMLClassDiagramGenerator {

	/** The plant UML config. */
	private PlantUMLClassDiagramConfig plantUMLConfig;
	
	/**
	 * Instantiates a new plant UML class diagram generator.
	 *
	 * @param paramPlantUMLConfig the param plant UML config
	 */
	public PlantUMLClassDiagramGenerator(final PlantUMLClassDiagramConfig paramPlantUMLConfig) {
		plantUMLConfig = paramPlantUMLConfig;
	}

	/**
	 * Generate the class diagram string for all classes in the configured packages.
	 *
	 * @return String - the text containing all Plant UML class diagram definitions
	 * @throws ClassNotFoundException - thrown if a class in a package could not be
	 *                                found or if a package does not contain any
	 *                                class information
	 * @throws IOException            - thrown if a class or jar File could not be
	 *                                read
	 */
	public String generateDiagramText() throws ClassNotFoundException, IOException {
		PlantUMLClassDiagramAnalyzeSummary summary = new PlantUMLClassDiagramAnalyzerAndMapper(plantUMLConfig).analyzeClassesAndMapThemToTheInternalClassStructure();
		return new PlantUMLClassDiagramTextBuilder(plantUMLConfig, summary).buildDiagramText();
	}


}
