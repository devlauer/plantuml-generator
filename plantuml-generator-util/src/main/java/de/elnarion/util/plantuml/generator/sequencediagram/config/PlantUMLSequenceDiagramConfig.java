package de.elnarion.util.plantuml.generator.sequencediagram.config;

/**
 * The Class PlantUMLSequenceDiagramConfig.
 */
public class PlantUMLSequenceDiagramConfig {

	/** The destination classloader. */
	private ClassLoader destinationClassloader = PlantUMLSequenceDiagramConfig.class.getClassLoader();
	
	/** The start class. */
	private String startClass = null;
	
	/** The start method. */
	private String startMethod = null;
	
	/** The use short class names. */
	private boolean useShortClassNames = true;
	
	/** The show return types. */
	private boolean showReturnTypes = false;
	
	/** The hide super class. */
	private boolean hideSuperClass = false;
	
	/** The ignore standard classes. */
	private boolean ignoreStandardClasses = true;
	
	/** The ignore JPA entities. */
	private boolean ignoreJPAEntities = false;
	
	/** The class blacklist regexp. */
	private String classBlacklistRegexp = null;
	
	/** The method blacklist regexp. */
	private String methodBlacklistRegexp = null;
	
	/** The hide method name. */
	private boolean hideMethodName = false;
	

	/**
	 * Gets the destination classloader.
	 *
	 * @return the destination classloader
	 */
	public ClassLoader getDestinationClassloader() {
		return destinationClassloader;
	}

	/**
	 * Sets the destination classloader.
	 *
	 * @param destinationClassloader the new destination classloader
	 */
	public void setDestinationClassloader(ClassLoader destinationClassloader) {
		this.destinationClassloader = destinationClassloader;
	}

	/**
	 * Gets the start class.
	 *
	 * @return the start class
	 */
	public String getStartClass() {
		return startClass;
	}

	/**
	 * Sets the start class.
	 *
	 * @param startClass the new start class
	 */
	public void setStartClass(String startClass) {
		this.startClass = startClass;
	}

	/**
	 * Gets the start method.
	 *
	 * @return the start method
	 */
	public String getStartMethod() {
		return startMethod;
	}

	/**
	 * Sets the start method.
	 *
	 * @param startMethod the new start method
	 */
	public void setStartMethod(String startMethod) {
		this.startMethod = startMethod;
	}

	/**
	 * Checks if is use short class names.
	 *
	 * @return true, if is use short class names
	 */
	public boolean isUseShortClassNames() {
		return useShortClassNames;
	}

	/**
	 * Sets the use short class names.
	 *
	 * @param useShortClassNames the new use short class names
	 */
	public void setUseShortClassNames(boolean useShortClassNames) {
		this.useShortClassNames = useShortClassNames;
	}

	/**
	 * Checks if is show return types.
	 *
	 * @return true, if is show return types
	 */
	public boolean isShowReturnTypes() {
		return showReturnTypes;
	}

	/**
	 * Sets the show return types.
	 *
	 * @param showReturnTypes the new show return types
	 */
	public void setShowReturnTypes(boolean showReturnTypes) {
		this.showReturnTypes = showReturnTypes;
	}

	/**
	 * Checks if is hide super class.
	 *
	 * @return true, if is hide super class
	 */
	public boolean isHideSuperClass() {
		return hideSuperClass;
	}

	/**
	 * Sets the hide super class.
	 *
	 * @param hideSuperClass the new hide super class
	 */
	public void setHideSuperClass(boolean hideSuperClass) {
		this.hideSuperClass = hideSuperClass;
	}

	/**
	 * Checks if is ignore standard classes.
	 *
	 * @return true, if is ignore standard classes
	 */
	public boolean isIgnoreStandardClasses() {
		return ignoreStandardClasses;
	}

	/**
	 * Sets the ignore standard classes.
	 *
	 * @param ignoreStandardClasses the new ignore standard classes
	 */
	public void setIgnoreStandardClasses(boolean ignoreStandardClasses) {
		this.ignoreStandardClasses = ignoreStandardClasses;
	}

	/**
	 * Checks if is ignore JPA entities.
	 *
	 * @return true, if is ignore JPA entities
	 */
	public boolean isIgnoreJPAEntities() {
		return ignoreJPAEntities;
	}

	/**
	 * Sets the ignore JPA entities.
	 *
	 * @param ignoreJPAEntities the new ignore JPA entities
	 */
	public void setIgnoreJPAEntities(boolean ignoreJPAEntities) {
		this.ignoreJPAEntities = ignoreJPAEntities;
	}

	/**
	 * Gets the class blacklist regexp.
	 *
	 * @return the class blacklist regexp
	 */
	public String getClassBlacklistRegexp() {
		return classBlacklistRegexp;
	}

	/**
	 * Sets the class blacklist regexp.
	 *
	 * @param classBlacklistRegexp the new class blacklist regexp
	 */
	public void setClassBlacklistRegexp(String classBlacklistRegexp) {
		this.classBlacklistRegexp = classBlacklistRegexp;
	}

	/**
	 * Gets the method blacklist regexp.
	 *
	 * @return the method blacklist regexp
	 */
	public String getMethodBlacklistRegexp() {
		return methodBlacklistRegexp;
	}

	/**
	 * Sets the method blacklist regexp.
	 *
	 * @param methodBlacklistRegexp the new method blacklist regexp
	 */
	public void setMethodBlacklistRegexp(String methodBlacklistRegexp) {
		this.methodBlacklistRegexp = methodBlacklistRegexp;
	}

	/**
	 * Checks if is hide method name.
	 *
	 * @return true, if is hide method name
	 */
	public boolean isHideMethodName() {
		return hideMethodName;
	}

	/**
	 * Sets the hide method name.
	 *
	 * @param hideMethodName the new hide method name
	 */
	public void setHideMethodName(boolean hideMethodName) {
		this.hideMethodName = hideMethodName;
	}

}
