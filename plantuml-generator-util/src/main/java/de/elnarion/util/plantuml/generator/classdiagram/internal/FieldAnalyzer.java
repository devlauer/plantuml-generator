package de.elnarion.util.plantuml.generator.classdiagram.internal;

import de.elnarion.util.plantuml.generator.classdiagram.config.ClassifierType;
import de.elnarion.util.plantuml.generator.classdiagram.config.PlantUMLClassDiagramConfig;
import de.elnarion.util.plantuml.generator.classdiagram.config.VisibilityType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class FieldAnalyzer.
 */
class FieldAnalyzer {

	/** The plant UML config. */
	final PlantUMLClassDiagramConfig plantUMLConfig;

	/**
	 * Instantiates a new field analyzer.
	 *
	 * @param paramPlantUMLConfig the param plant UML config
	 */
	FieldAnalyzer(PlantUMLClassDiagramConfig paramPlantUMLConfig) {
		plantUMLConfig = paramPlantUMLConfig;
	}

	/**
	 * Analyze field.
	 *
	 * @param field                the field
	 * @param type                 the type
	 * @param paramDeclaredMethods the param declared methods
	 * @return the UML field
	 */
	UMLField analyzeField(final java.lang.reflect.Field field, final Class<?> type, Method[] paramDeclaredMethods) {
		// Do not add field if they should be ignored/removed
		if (plantUMLConfig.isRemoveFields())
			return null;
		// if there is a blacklist for field and the field name matches it, then
		// ignore/remove the field
		if (plantUMLConfig.getFieldBlacklistRegexp() != null
				&& field.getName().matches(plantUMLConfig.getFieldBlacklistRegexp()))
			return null;
		final int modifier = field.getModifiers();
		List<String> annotationStringList = new ArrayList<>();
		if (plantUMLConfig.isAddJPAAnnotations()) {
			annotationStringList = new JPAAnalyzerHelper().addJPAFieldAnnotationsToList(field, paramDeclaredMethods,
					plantUMLConfig.getDestinationClassloader());
		}
		final ClassifierType classifierType = AnalyzerUtil.getClassifier(modifier);
		if (plantUMLConfig.getFieldClassifierToIgnore().contains(classifierType))
			return null;
		VisibilityType visibilityType = AnalyzerUtil.getVisibility(modifier);
		if (FieldAnalyzerUtil.hasGetterAndSetterMethod(field.getName(), paramDeclaredMethods)) {
			visibilityType = VisibilityType.PUBLIC;
		}
		// check if field should be visible by maximum visibility
		if (!AnalyzerUtil.visibilityOk(plantUMLConfig.getMaxVisibilityFields(), visibilityType))
			return null;
		return new UMLField(classifierType, visibilityType, field.getName(),
				AnalyzerUtil.getClassNameForFieldsAndMethods(type, plantUMLConfig), annotationStringList);

	}

	/**
	 * Analyze enum constant.
	 *
	 * @param enumConstant the enum constant
	 * @return the UML field
	 */
	public UMLField analyzeEnumConstant(Object enumConstant) {
		return new UMLField(ClassifierType.NONE, VisibilityType.PUBLIC, enumConstant.toString(), null,
				new ArrayList<>());

	}

}
