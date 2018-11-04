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
	@Parameter(property = PREFIX + "hideMethodsDirectory", defaultValue = "false", required = false)
	private boolean hideMethods;

	/** The scan packages. */
	@Parameter(property = PREFIX + "scanPackages", defaultValue = "", required = true)
	private List<String> scanPackages;

	/** The hide classes. */
	@Parameter(property = PREFIX + "hideClasses", defaultValue = "", required = false)
	private List<String> hideClasses;

	/** The descriptor. */
	@Parameter(defaultValue = "${plugin}", readonly = true)
	private PluginDescriptor descriptor;

	/** The project. */
	@Component
	private MavenProject project;

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
			PlantUMLClassDiagramGenerator classDiagramGenerator = new PlantUMLClassDiagramGenerator(loader,
					scanPackages, hideClasses, hideFields, hideMethods);
			String classDiagramText = classDiagramGenerator.generateDiagramText();
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

}
