package de.elnarion.util.plantuml.generator.classdiagram.internal;

import java.lang.reflect.Modifier;

import de.elnarion.util.plantuml.generator.classdiagram.config.ClassifierType;
import de.elnarion.util.plantuml.generator.classdiagram.config.PlantUMLClassDiagramConfig;
import de.elnarion.util.plantuml.generator.classdiagram.config.VisibilityType;

/**
 * The Class AnalyzerUtil.
 */
class AnalyzerUtil {

	/**
	 * Instantiates a new analyzer util.
	 */
	private AnalyzerUtil() {
		// just a static utiltity
	}

	/**
	 * Gets the classifier type (none, abstract_static, static, abstract) from the
	 * modifier int.
	 *
	 * @param paramModifier - int - the modifier used for determining the classifier
	 *                      type
	 * @return {@link ClassifierType} - the classifier type
	 */
	protected static ClassifierType getClassifier(final int paramModifier) {
		ClassifierType classifierType = ClassifierType.NONE;
		if (Modifier.isStatic(paramModifier)) {
			if (Modifier.isAbstract(paramModifier)) {
				classifierType = ClassifierType.ABSTRACT_STATIC;
			} else {
				classifierType = ClassifierType.STATIC;
			}
		} else if (Modifier.isAbstract(paramModifier)) {
			classifierType = ClassifierType.ABSTRACT;
		}
		return classifierType;
	}

	/**
	 * Gets the visibility type (package_private, public, private, protected) from
	 * the modifier int.
	 *
	 * @param paramModifier - int - the modifier used for determining the visibility
	 *                      type
	 * @return {@link VisibilityType} - the visibility type
	 */
	protected static VisibilityType getVisibility(final int paramModifier) {
		VisibilityType visibilityType = VisibilityType.PACKAGE_PRIVATE;
		if (Modifier.isPublic(paramModifier)) {
			visibilityType = VisibilityType.PUBLIC;
		} else if (Modifier.isPrivate(paramModifier)) {
			visibilityType = VisibilityType.PRIVATE;
		} else if (Modifier.isProtected(paramModifier)) {
			visibilityType = VisibilityType.PROTECTED;
		}
		return visibilityType;
	}

	/**
	 * Removes the java lang package string from a full qualified class name. This
	 * makes it easier to read the class diagram.
	 *
	 * @param paramTypeName - String - the full qualified type name
	 * @return String - the type name without the java.lang package name
	 */
	protected static String removeJavaLangPackage(String paramTypeName) {
		if (paramTypeName.startsWith("java.lang.")) {
			paramTypeName = paramTypeName.substring("java.lang.".length(), paramTypeName.length());
		}
		return paramTypeName;
	}

	/**
	 * Gets the class name according to the given configuration for parameters or
	 * return types in methods or fields in classes.
	 *
	 * @param parameter           the parameter
	 * @param paramPlantUMLConfig the param plant UML config
	 * @return the class name
	 */
	public static String getClassNameForFieldsAndMethods(final Class<?> parameter,
			PlantUMLClassDiagramConfig paramPlantUMLConfig) {
		String parameterType;
		if (paramPlantUMLConfig.isUseShortClassNames()
				|| paramPlantUMLConfig.isUseShortClassNamesInFieldsAndMethods()) {
			parameterType = parameter.getSimpleName();
		} else {
			parameterType = parameter.getName();
		}
		parameterType = AnalyzerUtil.removeJavaLangPackage(parameterType);
		return parameterType;
	}

	/**
	 * Gets the class name for classes or relationships.
	 *
	 * @param parameter           the parameter
	 * @param paramPlantUMLConfig the param plant UML config
	 * @return the class name for classtypes
	 */
	public static String getClassNameForClassesOrRelationships(final Class<?> parameter,
			PlantUMLClassDiagramConfig paramPlantUMLConfig) {
		String parameterType;
		if (paramPlantUMLConfig.isUseShortClassNames()) {
			parameterType = parameter.getSimpleName();
		} else {
			parameterType = parameter.getName();
		}
		parameterType = AnalyzerUtil.removeJavaLangPackage(parameterType);
		return parameterType;
	}

	/**
	 * Checks if the given visibilityType of a field or method should lead to an
	 * appreance on the diagram according to the configured maximum visibility.
	 * 
	 * @param maxVisibilityFields {@link VisibilityType} - the configured maximum
	 *                            visibility to check against
	 * @param visibilityType      {@link VisibilityType} - the visibility of a field
	 *                            or method which could be part of the diagram
	 * @return boolean - if the field or method should be visible on the diagram
	 */
	protected static boolean visibilityOk(VisibilityType maxVisibilityFields, VisibilityType visibilityType) {
		if (maxVisibilityFields != null) {
			// if maximum is public only public is allowed as visibility type
			if (maxVisibilityFields.equals(VisibilityType.PUBLIC) && !visibilityType.equals(VisibilityType.PUBLIC))
				return false;
			// if maximum is protected only public and protected are allowed
			if (maxVisibilityFields.equals(VisibilityType.PROTECTED) && !(visibilityType.equals(VisibilityType.PUBLIC)
					|| visibilityType.equals(VisibilityType.PROTECTED)))
				return false;
			// if maximum is package_private then only public, package_private and protected
			// are
			// allowed
			if (maxVisibilityFields.equals(VisibilityType.PACKAGE_PRIVATE)
					&& !(visibilityType.equals(VisibilityType.PUBLIC)
							|| visibilityType.equals(VisibilityType.PACKAGE_PRIVATE)
							|| visibilityType.equals(VisibilityType.PROTECTED)))
				return false;
		}
		// everything else like maximum private or no defined maximum visibility leads
		// to
		// an ok check
		return true;
	}
}
