package de.elnarion.maven.plugin.plantuml.generator;

import de.elnarion.util.plantuml.generator.classdiagram.PlantUMLClassDiagramGenerator;
import de.elnarion.util.plantuml.generator.classdiagram.config.ClassifierType;
import de.elnarion.util.plantuml.generator.classdiagram.config.PlantUMLClassDiagramConfigBuilder;
import de.elnarion.util.plantuml.generator.classdiagram.config.VisibilityType;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.util.List;

/**
 * This Mojo is used as maven frontend of the PlantUMLClassDiagramGenerator in
 * the artifact plantuml-generator-util.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true, requiresDependencyResolution = ResolutionScope.COMPILE, requiresProject = true)
public class PlantUMLGeneratorMojo extends AbstractPlantUMLGeneratorMojo {

	/** The use Smetana renderer. */
	@Parameter(property = PREFIX + "useSmetana", defaultValue = "false", required = false)
	private boolean useSmetana = false;
	/** The remove methods. */
	@Parameter(property = PREFIX + "fieldBlacklistRegexp", defaultValue = "", required = false)
	private final String fieldBlacklistRegexp = null;
	/** The remove methods. */
	@Parameter(property = PREFIX + "methodBlacklistRegexp", defaultValue = "", required = false)
	private final String methodBlacklistRegexp = null;
	/** The remove methods. */
	@Parameter(property = PREFIX + "maxVisibilityFields", defaultValue = "PRIVATE", required = false)
	private final VisibilityType maxVisibilityFields = VisibilityType.PRIVATE;
	/** The remove methods. */
	@Parameter(property = PREFIX + "maxVisibilityMethods", defaultValue = "PRIVATE", required = false)
	private final VisibilityType maxVisibilityMethods = VisibilityType.PRIVATE;
	/** The hide fields. */
	@Parameter(property = PREFIX + "hideFields", defaultValue = "false", required = false)
	private boolean hideFields;
	/** The hide methods. */
	@Parameter(property = PREFIX + "hideMethods", defaultValue = "false", required = false)
	private boolean hideMethods;
	/** The scan packages. */
	@Parameter(property = PREFIX + "scanPackages", defaultValue = "", required = true)
	private List<String> scanPackages;
	/** The whitelist regular expression for the scan packages. */
	@Parameter(property = PREFIX + "whitelistRegexp", defaultValue = "", required = false)
	private String whitelistRegexp;
	/** The blacklist regular expression for the scan packages. */
	@Parameter(property = PREFIX + "blacklistRegexp", defaultValue = "", required = false)
	private String blacklistRegexp;
	/** The hide classes. */
	@Parameter(property = PREFIX + "hideClasses", defaultValue = "", required = false)
	private List<String> hideClasses;
	/** the list of all field classifiers to ignore. */
	@Parameter(property = PREFIX + "fieldClassifierListToIgnore", defaultValue = "", required = false)
	private List<ClassifierType> fieldClassifierListToIgnore;
	/** the list of all method classifiers to ignore. */
	@Parameter(property = PREFIX + "methodClassifierListToIgnore", defaultValue = "", required = false)
	private List<ClassifierType> methodClassifierListToIgnore;
	/** The remove methods. */
	@Parameter(property = PREFIX + "removeMethods", defaultValue = "false", required = false)
	private boolean removeMethods;
	/** JPA annotations flag. */
	@Parameter(property = PREFIX + "addJPAAnnotations", defaultValue = "false", required = false)
	private boolean addJPAAnnotations;
	/** Validation annotations flag. */
	@Parameter(property = PREFIX + "addValidationAnnotations", defaultValue = "false", required = false)
	private boolean addValidationAnnotations;
	/** The remove fields. */
	@Parameter(property = PREFIX + "removeFields", defaultValue = "false", required = false)
	private boolean removeFields;
	@Parameter(property = PREFIX + "useShortClassNames", defaultValue = "false", required = false)
	private boolean useShortClassNames;
	@Parameter(property = PREFIX + "useShortClassNamesInFieldsAndMethods", defaultValue = "false", required = false)
	private boolean useShortClassNamesInFieldsAndMethods;

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
		getLog().info("Starting plantuml generation ");
		try {
			ClassLoader loader = getCompileClassLoader();
			PlantUMLClassDiagramGenerator classDiagramGenerator;
			PlantUMLClassDiagramConfigBuilder configBuilder;
			if (whitelistRegexp == null || "".equals(whitelistRegexp)) {
				configBuilder = new PlantUMLClassDiagramConfigBuilder(
						((blacklistRegexp != null && !"".equals(blacklistRegexp)) ? blacklistRegexp : null),
						scanPackages);
			} else {
				configBuilder = new PlantUMLClassDiagramConfigBuilder(scanPackages, whitelistRegexp);
			}
			configBuilder.withClassLoader(loader).withHideClasses(hideClasses).withHideFieldsParameter(hideFields)
					.withUseSmetana(useSmetana)
					.withHideMethods(hideMethods).addFieldClassifiersToIgnore(fieldClassifierListToIgnore)
					.addMethodClassifiersToIgnore(methodClassifierListToIgnore).withRemoveFields(removeFields)
					.withRemoveMethods(removeMethods).withFieldBlacklistRegexp(fieldBlacklistRegexp)
					.withMethodBlacklistRegexp(methodBlacklistRegexp).withMaximumFieldVisibility(maxVisibilityFields)
					.withMaximumMethodVisibility(maxVisibilityMethods).withJPAAnnotations(addJPAAnnotations)
					.withValidationAnnotations(addValidationAnnotations)
					.addAdditionalPlantUmlConfigs(getAdditionalPlantUmlConfigs())
					.withUseShortClassNames(useShortClassNames)
					.withUseShortClassNamesInFieldsAndMethods(useShortClassNamesInFieldsAndMethods);
			classDiagramGenerator = new PlantUMLClassDiagramGenerator(configBuilder.build());
			String classDiagramText = classDiagramGenerator.generateDiagramText();
			writeDiagramToFile(classDiagramText);
		} catch (Exception e) {
			getLog().error("Exception:" + e.getMessage());
			getLog().error(e);
		}
	}

	/**
	 * Checks if Smetana is enabled.
	 *
	 * @return true, enabled.
	 */
	public boolean isUseSmetana() {
		return useSmetana;
	}

	/**
	 * Sets the Smetana.
	 *
	 * @param useSmetana set use Smetana
	 */
	public void setUseSmetana(boolean useSmetana) {
		this.useSmetana = useSmetana;
	}

	/**
	 * Checks if is hide fields.
	 *
	 * @return true, if is hide fields
	 */
	public boolean isHideFields() {
		return hideFields;
	}

	/**
	 * Sets the hide fields.
	 *
	 * @param hideFields the hide fields
	 */
	public void setHideFields(boolean hideFields) {
		this.hideFields = hideFields;
	}

	/**
	 * Checks if is hide methods.
	 *
	 * @return true, if is hide methods
	 */
	public boolean isHideMethods() {
		return hideMethods;
	}

	/**
	 * Sets the hide methods.
	 *
	 * @param hideMethods the hide methods
	 */
	public void setHideMethods(boolean hideMethods) {
		this.hideMethods = hideMethods;
	}

	/**
	 * Gets the scan packages.
	 *
	 * @return List - the scan packages
	 */
	public List<String> getScanPackages() {
		return scanPackages;
	}

	/**
	 * Sets the scan packages.
	 *
	 * @param scanPackages the scan packages
	 */
	public void setScanPackages(List<String> scanPackages) {
		this.scanPackages = scanPackages;
	}

	/**
	 * Gets the hide classes.
	 *
	 * @return List - the hide classes
	 */
	public List<String> getHideClasses() {
		return hideClasses;
	}

	/**
	 * Sets the hide classes.
	 *
	 * @param hideClasses the hide classes
	 */
	public void setHideClasses(List<String> hideClasses) {
		this.hideClasses = hideClasses;
	}

	/**
	 * Gets the whitelist regexp.
	 *
	 * @return the whitelist regexp
	 */
	public String getWhitelistRegexp() {
		return whitelistRegexp;
	}

	/**
	 * Sets the whitelist regexp.
	 *
	 * @param whitelistRegexp the new whitelist regexp
	 */
	public void setWhitelistRegexp(String whitelistRegexp) {
		this.whitelistRegexp = whitelistRegexp;
	}

	/**
	 * Gets the blacklist regexp.
	 *
	 * @return the blacklist regexp
	 */
	public String getBlacklistRegexp() {
		return blacklistRegexp;
	}

	/**
	 * Sets the blacklist regexp.
	 *
	 * @param blacklistRegexp the new blacklist regexp
	 */
	public void setBlacklistRegexp(String blacklistRegexp) {
		this.blacklistRegexp = blacklistRegexp;
	}

	/**
	 * Checks if is adds the JPA annotations.
	 *
	 * @return true, if is adds the JPA annotations
	 */
	public boolean isAddJPAAnnotations() {
		return addJPAAnnotations;
	}

	/**
	 * Sets the adds the JPA annotations.
	 *
	 * @param addJPAAnnotations the new adds the JPA annotations
	 */
	public void setAddJPAAnnotations(boolean addJPAAnnotations) {
		this.addJPAAnnotations = addJPAAnnotations;
	}

	/**
	 * Checks if is adds Validation annotations.
	 *
	 * @return true, if Validation annotations are enabled
	 */
	public boolean isAddValidationAnnotations() {
		return addValidationAnnotations;
	}

	/**
	 * Sets the adds Validation annotations.
	 *
	 * @param addValidationAnnotations if true, adds Validation annotations
	 */
	public void setAddValidationAnnotations(boolean addValidationAnnotations) {
		this.addValidationAnnotations = addValidationAnnotations;
	}
}
