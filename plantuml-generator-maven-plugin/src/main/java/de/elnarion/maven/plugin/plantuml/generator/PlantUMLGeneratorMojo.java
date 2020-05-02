package de.elnarion.maven.plugin.plantuml.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import de.elnarion.util.plantuml.generator.PlantUMLClassDiagramGenerator;
import de.elnarion.util.plantuml.generator.classdiagram.ClassifierType;
import de.elnarion.util.plantuml.generator.classdiagram.VisibilityType;
import de.elnarion.util.plantuml.generator.config.PlantUMLConfigBuilder;

/**
 * This Mojo is used as maven frontend of the PlantUMLClassDiagramGenerator in
 * the artifact plantuml-generator-util.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true, requiresDependencyResolution = ResolutionScope.COMPILE, requiresProject = true)
public class PlantUMLGeneratorMojo extends AbstractMojo {

	/** The Constant PREFIX. */
	static final String PREFIX = "plantuml-generator.";

	/** The encoding. */
	@Parameter(defaultValue = "${project.build.sourceEncoding}")
	private String encoding;

	/** The output directory. */
	@Parameter(property = PREFIX
			+ "outputDirectory", defaultValue = "${project.build.directory}/generated-docs", required = false)
	private File outputDirectory;

	/** The target file name. */
	@Parameter(property = PREFIX + "outputFilename", required = true)
	private String outputFilename;

	/** The hide fields. */
	@Parameter(property = PREFIX + "hideFields", defaultValue = "false", required = false)
	private boolean hideFields;

	/** The hide methods. */
	@Parameter(property = PREFIX + "hideMethods", defaultValue = "false", required = false)
	private boolean hideMethods;

	/** The enable asciidoc wrapper. */
	@Parameter(property = PREFIX + "enableAsciidocWrapper", defaultValue = "false", required = false)
	private boolean enableAsciidocWrapper;

	/** The scan packages. */
	@Parameter(property = PREFIX + "scanPackages", defaultValue = "", required = true)
	private List<String> scanPackages;

	/** The whitelist regular expression for the scan packages. */
	@Parameter(property = PREFIX + "whitelistRegexp", defaultValue = "", required = false)
	private String whitelistRegexp;

	/** The asciidoc diagram name. */
	@Parameter(property = PREFIX + "asciidocDiagramName", defaultValue = "", required = false)
	private String asciidocDiagramName;

	/** The asciidoc diagram image type. */
	@Parameter(property = PREFIX + "asciidocDiagramImageFormat", defaultValue = "png", required = false)
	private String asciidocDiagramImageFormat;

	/** The asciidoc diagram block delimiter. */
	@Parameter(property = PREFIX + "asciidocDiagramBlockDelimiter", defaultValue = "----", required = false)
	private String asciidocDiagramBlockDelimiter;

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

	/** The remove methods. */
	@Parameter(property = PREFIX + "removeFields", defaultValue = "false", required = false)
	private boolean removeFields;

	/** The remove methods. */
	@Parameter(property = PREFIX + "fieldBlacklistRegexp", defaultValue = "", required = false)
	private String fieldBlacklistRegexp = null;

	/** The remove methods. */
	@Parameter(property = PREFIX + "methodBlacklistRegexp", defaultValue = "", required = false)
	private String methodBlacklistRegexp = null;

	/** The remove methods. */
	@Parameter(property = PREFIX + "maxVisibilityFields", defaultValue = "PRIVATE", required = false)
	private VisibilityType maxVisibilityFields = VisibilityType.PRIVATE;

	/** The remove methods. */
	@Parameter(property = PREFIX + "maxVisibilityMethods", defaultValue = "PRIVATE", required = false)
	private VisibilityType maxVisibilityMethods = VisibilityType.PRIVATE;

	/** The descriptor. */
	@Parameter(defaultValue = "${plugin}", readonly = true)
	private PluginDescriptor descriptor;

	/** The project. */
	@Component
	private MavenProject project;

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
			PlantUMLConfigBuilder configBuilder;
			if (whitelistRegexp == null || "".equals(whitelistRegexp)) {
				configBuilder = new PlantUMLConfigBuilder(
						((blacklistRegexp != null && !"".equals(blacklistRegexp)) ? blacklistRegexp : null),
						scanPackages);
			} else {
				configBuilder = new PlantUMLConfigBuilder(whitelistRegexp, scanPackages);
			}
			configBuilder.withClassLoader(loader).withHideClasses(hideClasses).withHideFieldsParameter(hideFields)
					.withHideMethods(hideMethods).addFieldClassifiersToIgnore(fieldClassifierListToIgnore)
					.addMethodClassifiersToIgnore(methodClassifierListToIgnore).withRemoveFields(removeFields)
					.withRemoveMethods(removeMethods).withFieldBlacklistRegexp(fieldBlacklistRegexp)
					.withMethodBlacklistRegexp(methodBlacklistRegexp).withMaximumFieldVisibility(maxVisibilityFields)
					.withMaximumMethodVisibility(maxVisibilityMethods);
			classDiagramGenerator = new PlantUMLClassDiagramGenerator(configBuilder.build());
			String classDiagramText = classDiagramGenerator.generateDiagramText();
			if (enableAsciidocWrapper) {
				classDiagramText = createAsciidocWrappedDiagramText(classDiagramText);
			}
			getLog().debug("diagram text:");
			getLog().debug(classDiagramText);
			getLog().info("Diagram generated.");
			File outputFile = new File(outputDirectory.getAbsolutePath() + File.separator + outputFilename);
			if (!outputFile.exists()) {
				outputFile.getParentFile().mkdirs();
				boolean created = outputFile.createNewFile();
				if (!created) {
					getLog().error("Output file " + outputFile.getAbsolutePath() + " could not be created.");
					return;
				}
			}
			IOUtils.write(classDiagramText, new FileOutputStream(outputFile), encoding);
			getLog().info("Diagram written to " + outputFile.getAbsolutePath());
		} catch (Exception e) {
			getLog().error("Exception:" + e.getMessage());
			getLog().error(e);
		}
	}

	/**
	 * Wraps a plantuml diagram text by an asciidoc diagram block.
	 *
	 * @param paramClassDiagramTextToWrap the class diagram text
	 * @return the asciidoc plantuml diagram block
	 */
	private String createAsciidocWrappedDiagramText(String paramClassDiagramTextToWrap) {
		StringBuilder builder = new StringBuilder();
		builder.append("[plantuml,");
		if (asciidocDiagramName != null && !"".equals(asciidocDiagramName))
			builder.append(asciidocDiagramName);
		else
			builder.append(outputFilename + "." + asciidocDiagramImageFormat);
		builder.append(",");
		builder.append(asciidocDiagramImageFormat);
		builder.append("]");
		builder.append(System.lineSeparator());
		builder.append(asciidocDiagramBlockDelimiter);
		builder.append(System.lineSeparator());
		builder.append(paramClassDiagramTextToWrap);
		builder.append(System.lineSeparator());
		builder.append(asciidocDiagramBlockDelimiter);
		paramClassDiagramTextToWrap = builder.toString();
		return paramClassDiagramTextToWrap;
	}

	/**
	 * Gets the encoding.
	 *
	 * @return String - the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * Sets the encoding.
	 *
	 * @param encoding the encoding
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * Gets the output directory.
	 *
	 * @return File - the output directory
	 */
	public File getOutputDirectory() {
		return outputDirectory;
	}

	/**
	 * Sets the output directory.
	 *
	 * @param outputDirectory the output directory
	 */
	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	/**
	 * Gets the output filename.
	 *
	 * @return String - the output filename
	 */
	public String getOutputFilename() {
		return outputFilename;
	}

	/**
	 * Sets the output filename.
	 *
	 * @param outputFilename the output filename
	 */
	public void setOutputFilename(String outputFilename) {
		this.outputFilename = outputFilename;
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
	 * Gets the descriptor.
	 *
	 * @return PluginDescriptor - the descriptor
	 */
	public PluginDescriptor getDescriptor() {
		return descriptor;
	}

	/**
	 * Sets the descriptor.
	 *
	 * @param descriptor the descriptor
	 */
	public void setDescriptor(PluginDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Gets the project.
	 *
	 * @return MavenProject - the project
	 */
	public MavenProject getProject() {
		return project;
	}

	/**
	 * Sets the project.
	 *
	 * @param project the project
	 */
	public void setProject(MavenProject project) {
		this.project = project;
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
	 * Gets the compile class loader.
	 *
	 * @return ClassLoader - the compile class loader
	 * @throws MojoExecutionException the mojo execution exception
	 */
	private ClassLoader getCompileClassLoader() throws MojoExecutionException {
		try {
			List<String> runtimeClasspathElements = project.getRuntimeClasspathElements();
			List<String> compileClasspathElements = project.getCompileClasspathElements();
			ArrayList<URL> classpathURLs = new ArrayList<>();
			for (String element : runtimeClasspathElements) {
				classpathURLs.add(new File(element).toURI().toURL());
			}
			for (String element : compileClasspathElements) {
				classpathURLs.add(new File(element).toURI().toURL());
			}
			URL[] urlArray = classpathURLs.toArray(new URL[classpathURLs.size()]);
			return new URLClassLoader(urlArray, Thread.currentThread().getContextClassLoader());

		} catch (Exception e) {
			throw new MojoExecutionException("Unable to load project runtime !", e);
		}
	}

	/**
	 * Checks if is enable asciidoc wrapper.
	 *
	 * @return true, if is enable asciidoc wrapper
	 */
	public boolean isEnableAsciidocWrapper() {
		return enableAsciidocWrapper;
	}

	/**
	 * Sets the enable asciidoc wrapper.
	 *
	 * @param enableAsciidocWrapper the new enable asciidoc wrapper
	 */
	public void setEnableAsciidocWrapper(boolean enableAsciidocWrapper) {
		this.enableAsciidocWrapper = enableAsciidocWrapper;
	}

	/**
	 * Gets the asciidoc diagram name.
	 *
	 * @return the asciidoc diagram name
	 */
	public String getAsciidocDiagramName() {
		return asciidocDiagramName;
	}

	/**
	 * Sets the asciidoc diagram name.
	 *
	 * @param asciidocDiagramName the new asciidoc diagram name
	 */
	public void setAsciidocDiagramName(String asciidocDiagramName) {
		this.asciidocDiagramName = asciidocDiagramName;
	}

	/**
	 * Gets the asciidoc diagram image type.
	 *
	 * @return the asciidoc diagram image type
	 */
	public String getAsciidocDiagramImageFormat() {
		return asciidocDiagramImageFormat;
	}

	/**
	 * Sets the asciidoc diagram image type.
	 *
	 * @param asciidocDiagramImageFormat the new asciidoc diagram image type
	 */
	public void setAsciidocDiagramImageFormat(String asciidocDiagramImageFormat) {
		this.asciidocDiagramImageFormat = asciidocDiagramImageFormat;
	}

	/**
	 * Gets the asciidoc diagram block delimiter.
	 *
	 * @return the asciidoc diagram block delimiter
	 */
	public String getAsciidocDiagramBlockDelimiter() {
		return asciidocDiagramBlockDelimiter;
	}

	/**
	 * Sets the asciidoc diagram block delimiter.
	 *
	 * @param asciidocDiagramBlockDelimiter the new asciidoc diagram block delimiter
	 */
	public void setAsciidocDiagramBlockDelimiter(String asciidocDiagramBlockDelimiter) {
		this.asciidocDiagramBlockDelimiter = asciidocDiagramBlockDelimiter;
	}

}
