package de.elnarion.maven.plugin.plantuml.generator;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import de.elnarion.util.plantuml.generator.PlantUMLSequenceDiagramGenerator;
import de.elnarion.util.plantuml.generator.config.PlantUMLSequenceDiagramConfigBuilder;

/**
 * This Mojo is used as maven frontend of the PlantUMLClassDiagramGenerator in
 * the artifact plantuml-generator-util.
 */
@Mojo(name = "generateSequenceDiagram", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true, requiresDependencyResolution = ResolutionScope.COMPILE, requiresProject = true)
public class PlantUMLSequenceDiagramGeneratorMojo extends AbstractPlantUMLGeneratorMojo {


	/** The blacklist regular expression for the scan packages. */
	@Parameter(property = PREFIX + "classBlacklistRegexp", defaultValue = "", required = false)
	private String classBlacklistRegexp;

	/** The hide method name. */
	@Parameter(property = PREFIX + "hideMethodName", defaultValue = "false", required = false)
	private boolean hideMethodName;

	/** The hide super class. */
	@Parameter(property = PREFIX + "hideSuperClass", defaultValue = "false", required = false)
	private boolean hideSuperClass;
	
	/** The ignore JPA entities. */
	@Parameter(property = PREFIX + "ignoreJPAEntities", defaultValue = "false", required = false)
	private boolean ignoreJPAEntities;

	/** The ignore standard classes. */
	@Parameter(property = PREFIX + "ignoreStandardClasses", defaultValue = "true", required = false)
	private boolean ignoreStandardClasses;
	
	/** The remove methods. */
	@Parameter(property = PREFIX + "methodBlacklistRegexp", defaultValue = "", required = false)
	private String methodBlacklistRegexp = null;

	/** The show return types. */
	@Parameter(property = PREFIX + "showReturnTypes", defaultValue = "false", required = false)
	private boolean showReturnTypes;

	/** The use short class names. */
	@Parameter(property = PREFIX + "useShortClassNames", defaultValue = "true", required = false)
	private boolean useShortClassNames;
	
	/** The start class. */
	@Parameter(property = PREFIX + "startClass", defaultValue = "", required = true)
	private String startClass = null;

	/** The start method. */
	@Parameter(property = PREFIX + "startMethod", defaultValue = "", required = true)
	private String startMethod = null;
	

	/**
	 * Execute.
	 *
	 * @throws MojoExecutionException the mojo execution exception
	 * @throws MojoFailureException   the mojo failure exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Starting plantuml sequence diagram generation ");
		try {
			ClassLoader loader = getCompileClassLoader();
			PlantUMLSequenceDiagramGenerator sequenceDiagramGenerator;
			PlantUMLSequenceDiagramConfigBuilder configBuilder = new PlantUMLSequenceDiagramConfigBuilder(startClass, startMethod);
			if(classBlacklistRegexp!=null&&!classBlacklistRegexp.isEmpty())
				configBuilder.withClassBlacklistRegexp(classBlacklistRegexp);
			if(methodBlacklistRegexp!=null&&!methodBlacklistRegexp.isEmpty())
				configBuilder.withMethodBlacklistRegexp(methodBlacklistRegexp);
			configBuilder.withClassloader(loader).withHideMethodName(hideMethodName).withHideSuperClass(hideSuperClass)
					.withIgnoreJPAEntities(ignoreJPAEntities).withIgnoreStandardClasses(ignoreStandardClasses)
					.withShowReturnTypes(showReturnTypes).withUseShortClassName(useShortClassNames);
			sequenceDiagramGenerator = new PlantUMLSequenceDiagramGenerator(configBuilder.build());
			String classDiagramText = sequenceDiagramGenerator.generateDiagramText();
			writeDiagramToFile(classDiagramText);
		} catch (Exception e) {
			getLog().error("Exception:" + e.getMessage());
			getLog().error(e);
		}
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


}
