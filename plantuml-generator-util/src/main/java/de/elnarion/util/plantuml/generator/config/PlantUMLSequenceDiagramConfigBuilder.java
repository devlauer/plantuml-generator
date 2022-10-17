package de.elnarion.util.plantuml.generator.config;

/**
 * The Class PlantUMLSequenceDiagramConfigBuilder.
 */
public class PlantUMLSequenceDiagramConfigBuilder {

	/** The sequence diagram config. */
	private PlantUMLSequenceDiagramConfig sequenceDiagramConfig;
	
	/**
	 * Instantiates a new plant UML sequence diagram config builder.
	 *
	 * @param paramStartClassName the param start class name
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
	 * Builds the.
	 *
	 * @return the plant UML sequence diagram config
	 */
	public PlantUMLSequenceDiagramConfig build() {
		return sequenceDiagramConfig;
	}
}
