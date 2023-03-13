package de.elnarion.util.plantuml.generator.sequencediagram.config;

/**
 * The Class PlantUMLSequenceDiagramConfigBuilder.
 */
public class PlantUMLSequenceDiagramConfigBuilder {

	/** The sequence diagram config. */
	private final PlantUMLSequenceDiagramConfig sequenceDiagramConfig;

	/**
	 * Instantiates a new plant UML sequence diagram config builder.
	 *
	 * @param paramStartClassName  the param start class name
	 * @param paramMethodStartName the param method start name
	 */
	public PlantUMLSequenceDiagramConfigBuilder(String paramStartClassName, String paramMethodStartName) {
		sequenceDiagramConfig = new PlantUMLSequenceDiagramConfig();
		sequenceDiagramConfig.setStartClass(paramStartClassName);
		sequenceDiagramConfig.setStartMethod(paramMethodStartName);
	}

	/**
	 * With classloader.
	 *
	 * @param paramDestinationClassloader the param destination classloader
	 * @return the plant UML sequence diagram config builder
	 */
	public PlantUMLSequenceDiagramConfigBuilder withClassloader(ClassLoader paramDestinationClassloader) {
		sequenceDiagramConfig.setDestinationClassloader(paramDestinationClassloader);
		return this;
	}

	/**
	 * With use short class name.
	 *
	 * @param paramUseShortClassNames the param use short class names
	 * @return the plant UML sequence diagram config builder
	 */
	public PlantUMLSequenceDiagramConfigBuilder withUseShortClassName(boolean paramUseShortClassNames) {
		sequenceDiagramConfig.setUseShortClassNames(paramUseShortClassNames);
		return this;
	}

	/**
	 * With show return types.
	 *
	 * @param paramShowReturnTypes the param show return types
	 * @return the plant UML sequence diagram config builder
	 */
	public PlantUMLSequenceDiagramConfigBuilder withShowReturnTypes(boolean paramShowReturnTypes) {
		sequenceDiagramConfig.setShowReturnTypes(paramShowReturnTypes);
		return this;
	}

	/**
	 * With hide super class.
	 *
	 * @param paramHideSuperClass the param hide super class
	 * @return the plant UML sequence diagram config builder
	 */
	public PlantUMLSequenceDiagramConfigBuilder withHideSuperClass(boolean paramHideSuperClass) {
		sequenceDiagramConfig.setHideSuperClass(paramHideSuperClass);
		return this;
	}

	/**
	 * With hide method name.
	 *
	 * @param paramHideMethodName the param hide method name
	 * @return the plant UML sequence diagram config builder
	 */
	public PlantUMLSequenceDiagramConfigBuilder withHideMethodName(boolean paramHideMethodName) {
		sequenceDiagramConfig.setHideMethodName(paramHideMethodName);
		return this;
	}


	/**
	 * With ignore standard classes.
	 *
	 * @param paramIgnoreStandardClasses the param ignore standard classes
	 * @return the plant UML sequence diagram config builder
	 */
	public PlantUMLSequenceDiagramConfigBuilder withIgnoreStandardClasses(boolean paramIgnoreStandardClasses) {
		sequenceDiagramConfig.setIgnoreStandardClasses(paramIgnoreStandardClasses);
		return this;
	}


	/**
	 * With ignore JPA entities.
	 *
	 * @param paramIgnoreJPAEntities the param ignore JPA entities
	 * @return the plant UML sequence diagram config builder
	 */
	public PlantUMLSequenceDiagramConfigBuilder withIgnoreJPAEntities(boolean paramIgnoreJPAEntities) {
		sequenceDiagramConfig.setIgnoreJPAEntities(paramIgnoreJPAEntities);
		return this;
	}

	/**
	 * With class blacklist regexp.
	 *
	 * @param paramClassBlacklistRegexp the param class blacklist regexp
	 * @return the plant UML sequence diagram config builder
	 */
	public PlantUMLSequenceDiagramConfigBuilder withClassBlacklistRegexp(String paramClassBlacklistRegexp) {
		sequenceDiagramConfig.setClassBlacklistRegexp(paramClassBlacklistRegexp);
		return this;
	}

	/**
	 * With method blacklist regexp.
	 *
	 * @param paramMethodBlacklistRegexp the param method blacklist regexp
	 * @return the plant UML sequence diagram config builder
	 */
	public PlantUMLSequenceDiagramConfigBuilder withMethodBlacklistRegexp(String paramMethodBlacklistRegexp) {
		sequenceDiagramConfig.setMethodBlacklistRegexp(paramMethodBlacklistRegexp);
		return this;
	}

	/**
	 * Builds the.
	 *
	 * @return the plant UML sequence diagram config
	 */
	public PlantUMLSequenceDiagramConfig build() {
		return sequenceDiagramConfig;
	}
}
