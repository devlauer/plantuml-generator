package de.elnarion.util.plantuml.generator.classdiagram.internal;

import de.elnarion.util.plantuml.generator.classdiagram.config.PlantUMLClassDiagramConfig;

import java.util.*;

/**
 * The Class PlantUMLClassDiagramTextBuilder.
 */
public class PlantUMLClassDiagramTextBuilder {

	/** The plant UML config. */
	private final PlantUMLClassDiagramConfig plantUMLConfig;

	/** The classes. */
	private final Map<String, UMLClass> classes;

	/** The classes and relationships. */
	private final Map<UMLClass, List<UMLRelationship>> classesAndRelationships;


	/**
	 * Instantiates a new plant UML class diagram text builder.
	 *
	 * @param paramPlantUMLConfig the param plant UML config
	 * @param paramSummary        the param summary
	 */
	public PlantUMLClassDiagramTextBuilder(PlantUMLClassDiagramConfig paramPlantUMLConfig,
										   ClassAnalyzerSummary paramSummary) {
		plantUMLConfig = paramPlantUMLConfig;
		classes = paramSummary.getClasses();
		classesAndRelationships = paramSummary.getClassesAndRelationships();
	}

	/**
	 * Builds the diagram text.
	 *
	 * @return the string
	 */
	public String buildDiagramText() {
		// build the Plant UML String by calling the corresponding method on all
		// PlantUMLDiagramElements
		final StringBuilder builder = new StringBuilder();
		startUMLDiagram(builder);
		addTwoSpaceLinesToBuilder(builder);

		appendAdditionalPlantUMLConfigsToBuilder(builder);

		final Collection<UMLClass> sortedClassesList = sortClasses();
		// add all class information to the diagram (includes field and method
		// information)
		// because of the full qualified class names packages are added automatically
		// and need not be added manually
		List<UMLRelationship> relationships = getSortedRelationshipsFromClasses(sortedClassesList);
		appendSortedClassesToBuilder(builder, sortedClassesList);
		addTwoSpaceLinesToBuilder(builder);
		addRelationshipsToBuilder(builder, relationships);
		addHideToggles(builder, sortedClassesList, relationships);
		addTwoSpaceLinesToBuilder(builder);
		endUmlDiagram(builder);
		return builder.toString();

	}

	/**
	 * End uml diagram.
	 *
	 * @param builder the builder
	 */
	private void endUmlDiagram(final StringBuilder builder) {
		// add diagram end to text
		builder.append("@enduml");
	}

	/**
	 * Start UML diagram.
	 *
	 * @param builder the builder
	 */
	private void startUMLDiagram(final StringBuilder builder) {
		builder.append("@startuml");
		if(plantUMLConfig.isUseSmetana()) {
			builder.append("!pragma layout smetana");
		}
	}

	/**
	 * Gets the sorted relationships from classes.
	 *
	 * @param sortedClassesList the sorted classes list
	 * @return the sorted relationships from classes
	 */
	private List<UMLRelationship> getSortedRelationshipsFromClasses(final Collection<UMLClass> sortedClassesList) {
		final List<UMLRelationship> relationships = new ArrayList<>();
		for (final UMLClass clazz : sortedClassesList) {
			relationships.addAll(classesAndRelationships.get(clazz));
		}
		sortRelationships(relationships);
		return relationships;
	}

	/**
	 * Append sorted classes to builder.
	 *
	 * @param builder           the builder
	 * @param sortedClassesList the sorted classes list
	 */
	private void appendSortedClassesToBuilder(final StringBuilder builder, final Collection<UMLClass> sortedClassesList) {
		for (final UMLClass clazz : sortedClassesList) {
			builder.append(clazz.getDiagramText());
			addTwoSpaceLinesToBuilder(builder);
		}
	}

	/**
	 * Sort relationships.
	 *
	 * @param relationships the relationships
	 */
	private void sortRelationships(final List<UMLRelationship> relationships) {
		// because the ordered list could be changed in between, sort the list
		relationships.sort(Comparator.comparing(UMLRelationship::getDiagramText));
	}

	/**
	 * Adds the relationships to builder.
	 *
	 * @param builder       the builder
	 * @param relationships the relationships
	 */
	private void addRelationshipsToBuilder(final StringBuilder builder, final List<UMLRelationship> relationships) {
		// add all class relationships to the diagram
		for (final UMLRelationship relationship : relationships) {
			builder.append(relationship.getDiagramText());
			builder.append(System.lineSeparator());
		}
	}

	/**
	 * Adds the two space lines to builder.
	 *
	 * @param builder the builder
	 */
	private void addTwoSpaceLinesToBuilder(final StringBuilder builder) {
		builder.append(System.lineSeparator());
		builder.append(System.lineSeparator());
	}

	/**
	 * Sort classes.
	 *
	 * @return the collection
	 */
	private Collection<UMLClass> sortClasses() {
		final List<UMLClass> listToCompare = new ArrayList<>(classes.values());
		// because the ordered list could be changed in between, sort the list
		listToCompare.sort(Comparator.comparing(UMLClass::getName));
		return listToCompare;
	}

	/**
	 * Append additional plant UML configs to builder.
	 *
	 * @param builder the builder
	 */
	private void appendAdditionalPlantUMLConfigsToBuilder(final StringBuilder builder) {
		if (!plantUMLConfig.getAdditionalPlantUmlConfigs().isEmpty()) {
			plantUMLConfig.getAdditionalPlantUmlConfigs().forEach(additionalPlantUmlConfig -> {
				builder.append(additionalPlantUmlConfig);
				builder.append(System.lineSeparator());
			});
			addTwoSpaceLinesToBuilder(builder);
		}
	}

	/**
	 * Adds the hide toggles to the class diagram string. Toggles are set for hide
	 * methods, hide field and for each full qualified class name in the list
	 * hideClasses. Only if there are classes and relationships in the diagram the
	 * toggles are set.
	 *
	 * @param paramBuilder     - StringBuilder - the builder used to generate the
	 *                         class diagram text
	 * @param paramClassesList - Collection&lt;{@link UMLClass}&gt; - the list of
	 *                         all classes in the class diagram
	 * @param relationships    - List&lt;{@link UMLRelationship}&gt; - the list of
	 *                         all relationships in the class diagram
	 */
	private void addHideToggles(final StringBuilder paramBuilder, final Collection<UMLClass> paramClassesList,
								final List<UMLRelationship> relationships) {
		if ((paramClassesList != null && !paramClassesList.isEmpty()) || (!relationships.isEmpty())) {
			if (plantUMLConfig.isHideFields()) {
				paramBuilder.append(System.lineSeparator());
				paramBuilder.append("hide fields");
			}
			if (plantUMLConfig.isHideMethods()) {
				paramBuilder.append(System.lineSeparator());
				paramBuilder.append("hide methods");
			}
			if (plantUMLConfig.getHideClasses() != null && !plantUMLConfig.getHideClasses().isEmpty()) {
				for (final String hideClass : plantUMLConfig.getHideClasses()) {
					paramBuilder.append(System.lineSeparator());
					paramBuilder.append("hide ");
					paramBuilder.append(hideClass);
				}
			}
		}
	}

}
