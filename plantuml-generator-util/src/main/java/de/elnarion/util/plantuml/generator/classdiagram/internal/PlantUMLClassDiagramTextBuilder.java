package de.elnarion.util.plantuml.generator.classdiagram.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.elnarion.util.plantuml.generator.classdiagram.config.PlantUMLClassDiagramConfig;

public class PlantUMLClassDiagramTextBuilder {

	private PlantUMLClassDiagramConfig plantUMLConfig;
	private Map<String, UMLClass> classes;
	private Map<UMLClass, List<UMLRelationship>> classesAndRelationships;
	
	
	public PlantUMLClassDiagramTextBuilder(PlantUMLClassDiagramConfig paramPlantUMLConfig, Map<String, UMLClass> paramUMLClasses,Map<UMLClass, List<UMLRelationship>> paramClassesAndRelationships) {
		plantUMLConfig = paramPlantUMLConfig;
		classes = paramUMLClasses;
		classesAndRelationships=paramClassesAndRelationships;
	}

	public String buildDiagramText() {
		// build the Plant UML String by calling the corresponding method on all
		// PlantUMLDiagramElements
		final StringBuilder builder = new StringBuilder();
		builder.append("@startuml");
		builder.append(System.lineSeparator());
		builder.append(System.lineSeparator());

		if (!plantUMLConfig.getAdditionalPlantUmlConfigs().isEmpty()) {
			plantUMLConfig.getAdditionalPlantUmlConfigs().forEach(additionalPlantUmlConfig -> {
				builder.append(additionalPlantUmlConfig);
				builder.append(System.lineSeparator());
			});
			builder.append(System.lineSeparator());
			builder.append(System.lineSeparator());
		}

		final List<UMLClass> listToCompare = new ArrayList<>();
		listToCompare.addAll(classes.values());
		// because the ordered list could be changed in between, sort the list
		Collections.sort(listToCompare, (o1, o2) -> o1.getName().compareTo(o2.getName()));
		final Collection<UMLClass> classesList = listToCompare;
		final List<UMLRelationship> relationships = new ArrayList<>();
		// add all class information to the diagram (includes field and method
		// information)
		// because of the full qualified class names packages are added automatically
		// and need not be added manually
		for (final UMLClass clazz : classesList) {
			relationships.addAll(classesAndRelationships.get(clazz));
			builder.append(clazz.getDiagramText());
			builder.append(System.lineSeparator());
			builder.append(System.lineSeparator());
		}
		builder.append(System.lineSeparator());
		builder.append(System.lineSeparator());
		// because the ordered list could be changed in between, sort the list
		Collections.sort(relationships, (o1, o2) -> o1.getDiagramText().compareTo(o2.getDiagramText()));
		// add all class relationships to the diagram
		for (final UMLRelationship relationship : relationships) {
			builder.append(relationship.getDiagramText());
			builder.append(System.lineSeparator());
		}
		addHideToggles(builder, classesList, relationships);
		builder.append(System.lineSeparator());
		builder.append(System.lineSeparator());
		// add diagram end to text
		builder.append("@enduml");
		return builder.toString();
		
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
