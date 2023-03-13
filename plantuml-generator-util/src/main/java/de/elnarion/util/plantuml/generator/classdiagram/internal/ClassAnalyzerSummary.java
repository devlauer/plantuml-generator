package de.elnarion.util.plantuml.generator.classdiagram.internal;

import java.util.List;
import java.util.Map;

/**
 * The Class PlantUMLClassDiagramAnalyzeSummary.
 */
public class ClassAnalyzerSummary {

	/** The classes. */
	private final Map<String, UMLClass> classes;

	/** The classes and relationships. */
	private final Map<UMLClass, List<UMLRelationship>> classesAndRelationships;

	/**
	 * Instantiates a new plant UML class diagram analyze summary.
	 *
	 * @param paramClasses                 the param classes
	 * @param paramClassesAndRelationships the param classes and relationships
	 */
	public ClassAnalyzerSummary(Map<String, UMLClass> paramClasses,
								Map<UMLClass, List<UMLRelationship>> paramClassesAndRelationships) {
		classes = paramClasses;
		classesAndRelationships = paramClassesAndRelationships;
	}

	/**
	 * Gets the classes and relationships.
	 *
	 * @return the classes and relationships
	 */
	public Map<UMLClass, List<UMLRelationship>> getClassesAndRelationships() {
		return classesAndRelationships;
	}

	/**
	 * Gets the classes.
	 *
	 * @return the classes
	 */
	public Map<String, UMLClass> getClasses() {
		return classes;
	}

}
