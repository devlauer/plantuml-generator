package de.elnarion.util.plantuml.generator.classdiagram.config;

import java.util.List;

/**
 * The Class PlantUMLClassDiagramConfigBuilder.
 */
public class PlantUMLClassDiagramConfigBuilder {

	/** The plant UML config. */
	private PlantUMLClassDiagramConfig plantUMLConfig = new PlantUMLClassDiagramConfig();

	/**
	 * Instantiates a new plant UML class diagram config builder.
	 *
	 * @param paramPackagesToScan the param packages to scan
	 */
	public PlantUMLClassDiagramConfigBuilder(List<String> paramPackagesToScan) {
		plantUMLConfig.setScanPackages(paramPackagesToScan);
	}

	/**
	 * Instantiates a new plant UML class diagram config builder.
	 *
	 * @param paramBlacklistRegexp the param blacklist regexp
	 * @param paramPackagesToScan  the param packages to scan
	 */
	public PlantUMLClassDiagramConfigBuilder(String paramBlacklistRegexp, List<String> paramPackagesToScan) {
		plantUMLConfig.setBlacklistRegexp(paramBlacklistRegexp);
		plantUMLConfig.setScanPackages(paramPackagesToScan);
	}

	/**
	 * Instantiates a new plant UML class diagram config builder.
	 *
	 * @param paramWhitelistRegexp the param whitelist regexp
	 */
	public PlantUMLClassDiagramConfigBuilder(String paramWhitelistRegexp) {
		plantUMLConfig.setWhitelistRegexp(paramWhitelistRegexp);
	}

	/**
	 * Instantiates a new plant UML class diagram config builder.
	 *
	 * @param paramPackagesToScan  the param packages to scan
	 * @param paramWhitelistRegexp the param whitelist regexp
	 */
	public PlantUMLClassDiagramConfigBuilder(List<String> paramPackagesToScan, String paramWhitelistRegexp) {
		plantUMLConfig.setWhitelistRegexp(paramWhitelistRegexp);
		if (paramPackagesToScan != null)
			plantUMLConfig.setScanPackages(paramPackagesToScan);
	}

	/**
	 * With class loader.
	 *
	 * @param paramDestinationClassLoader the param destination class loader
	 * @return PlantUMLConfigBuilder
	 */
	public PlantUMLClassDiagramConfigBuilder withClassLoader(ClassLoader paramDestinationClassLoader) {
		plantUMLConfig.setDestinationClassloader(paramDestinationClassLoader);
		return this;
	}

	/**
	 * With hide methods.
	 *
	 * @param paramHideMethods the param hide methods
	 * @return PlantUMLConfigBuilder
	 */
	public PlantUMLClassDiagramConfigBuilder withHideMethods(boolean paramHideMethods) {
		plantUMLConfig.setHideMethods(paramHideMethods);
		return this;
	}

	/**
	 * With JPA annotations.
	 *
	 * @param paramAddJPAAnnotations the param add JPA annotations
	 * @return the plant UML class diagram config builder
	 */
	public PlantUMLClassDiagramConfigBuilder withJPAAnnotations(boolean paramAddJPAAnnotations) {
		plantUMLConfig.setAddJPAAnnotations(paramAddJPAAnnotations);
		return this;
	}

	/**
	 * With hide fields parameter.
	 *
	 * @param paramHideFields the param hide fields
	 * @return PlantUMLConfigBuilder
	 */
	public PlantUMLClassDiagramConfigBuilder withHideFieldsParameter(boolean paramHideFields) {
		plantUMLConfig.setHideFields(paramHideFields);
		return this;
	}

	/**
	 * With hide classes.
	 *
	 * @param paramClassesToHide the param classes to hide
	 * @return PlantUMLConfigBuilder
	 */
	public PlantUMLClassDiagramConfigBuilder withHideClasses(List<String> paramClassesToHide) {
		plantUMLConfig.setHideClasses(paramClassesToHide);
		return this;
	}

	/**
	 * With remove methods.
	 *
	 * @param paramRemoveMethods the param remove methods
	 * @return PlantUMLConfigBuilder
	 */
	public PlantUMLClassDiagramConfigBuilder withRemoveMethods(boolean paramRemoveMethods) {
		plantUMLConfig.setRemoveMethods(paramRemoveMethods);
		return this;
	}

	/**
	 * use short class names in fields and methods.
	 *
	 * @param paramUseShortClassNames the param use short class names
	 * @return the plant UML class diagram config builder
	 */
	public PlantUMLClassDiagramConfigBuilder withUseShortClassNamesInFieldsAndMethods(
			boolean paramUseShortClassNamesInFieldsAndMethods) {
		plantUMLConfig.setUseShortClassNamesInFieldsAndMethods(paramUseShortClassNamesInFieldsAndMethods);
		return this;
	}

	/**
	 * Use short class names in all cases.
	 *
	 * @param paramUseShortClassNames the param use short class names
	 * @return the plant UML class diagram config builder
	 */
	public PlantUMLClassDiagramConfigBuilder withUseShortClassNames(boolean paramUseShortClassNames) {
		plantUMLConfig.setUseShortClassNames(paramUseShortClassNames);
		return this;
	}

	/**
	 * With remove fields.
	 *
	 * @param paramRemoveFields the param remove fields
	 * @return PlantUMLConfigBuilder
	 */
	public PlantUMLClassDiagramConfigBuilder withRemoveFields(boolean paramRemoveFields) {
		plantUMLConfig.setRemoveFields(paramRemoveFields);
		return this;
	}

	/**
	 * With field blacklist regexp.
	 *
	 * @param paramBlacklistFieldRegexp the param blacklist field regexp
	 * @return PlantUMLConfigBuilder
	 */
	public PlantUMLClassDiagramConfigBuilder withFieldBlacklistRegexp(String paramBlacklistFieldRegexp) {
		if (paramBlacklistFieldRegexp != null && paramBlacklistFieldRegexp.length() > 0)
			plantUMLConfig.setFieldBlacklistRegexp(paramBlacklistFieldRegexp);
		return this;
	}

	/**
	 * With method blacklist regexp.
	 *
	 * @param paramBlacklistMethodRegexp the param blacklist method regexp
	 * @return PlantUMLConfigBuilder
	 */
	public PlantUMLClassDiagramConfigBuilder withMethodBlacklistRegexp(String paramBlacklistMethodRegexp) {
		if (paramBlacklistMethodRegexp != null && paramBlacklistMethodRegexp.length() > 0)
			plantUMLConfig.setMethodBlacklistRegexp(paramBlacklistMethodRegexp);
		return this;
	}

	/**
	 * With maximum method visibility.
	 *
	 * @param paramVisibility the param visibility
	 * @return the plant UML class diagram config builder
	 */
	public PlantUMLClassDiagramConfigBuilder withMaximumMethodVisibility(VisibilityType paramVisibility) {
		plantUMLConfig.setMaxVisibilityMethods(paramVisibility);
		return this;
	}

	/**
	 * With maximum field visibility.
	 *
	 * @param paramVisibility the param visibility
	 * @return the plant UML class diagram config builder
	 */
	public PlantUMLClassDiagramConfigBuilder withMaximumFieldVisibility(VisibilityType paramVisibility) {
		plantUMLConfig.setMaxVisibilityFields(paramVisibility);
		return this;
	}

	/**
	 * Adds the field classifier to ignore.
	 *
	 * @param paramClassifier the param classifier
	 * @return the plant UML class diagram config builder
	 */
	public PlantUMLClassDiagramConfigBuilder addFieldClassifierToIgnore(ClassifierType paramClassifier) {
		if (paramClassifier != null)
			plantUMLConfig.getFieldClassifierToIgnore().add(paramClassifier);
		return this;
	}

	/**
	 * Adds the field classifiers to ignore.
	 *
	 * @param paramClassiferList the param classifer list
	 * @return the plant UML class diagram config builder
	 */
	public PlantUMLClassDiagramConfigBuilder addFieldClassifiersToIgnore(List<ClassifierType> paramClassiferList) {
		if (paramClassiferList != null)
			plantUMLConfig.getFieldClassifierToIgnore().addAll(paramClassiferList);
		return this;
	}

	/**
	 * Adds the method classifier to ignore.
	 *
	 * @param paramClassifier the param classifier
	 * @return the plant UML class diagram config builder
	 */
	public PlantUMLClassDiagramConfigBuilder addMethodClassifierToIgnore(ClassifierType paramClassifier) {
		if (paramClassifier != null)
			plantUMLConfig.getMethodClassifierToIgnore().add(paramClassifier);
		return this;
	}

	/**
	 * Adds the method classifiers to ignore.
	 *
	 * @param paramClassiferList the param classifer list
	 * @return the plant UML class diagram config builder
	 */
	public PlantUMLClassDiagramConfigBuilder addMethodClassifiersToIgnore(List<ClassifierType> paramClassiferList) {
		if (paramClassiferList != null)
			plantUMLConfig.getMethodClassifierToIgnore().addAll(paramClassiferList);
		return this;
	}

	/**
	 * Adds the additional plant uml configs.
	 *
	 * @param additionalPlantUmlConfigs the additional plant uml configs
	 * @return the plant UML class diagram config builder
	 */
	public PlantUMLClassDiagramConfigBuilder addAdditionalPlantUmlConfigs(List<String> additionalPlantUmlConfigs) {
		if (additionalPlantUmlConfigs != null)
			plantUMLConfig.getAdditionalPlantUmlConfigs().addAll(additionalPlantUmlConfigs);
		return this;
	}

	/**
	 * Builds the.
	 *
	 * @return PlantUMLConfig
	 */
	public PlantUMLClassDiagramConfig build() {
		return plantUMLConfig;
	}

}