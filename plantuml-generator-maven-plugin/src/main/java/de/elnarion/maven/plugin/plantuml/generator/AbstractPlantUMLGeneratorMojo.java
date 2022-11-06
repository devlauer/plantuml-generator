package de.elnarion.maven.plugin.plantuml.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * The Class AbstractPlantUMLGeneratorMojo.
 */
public abstract class AbstractPlantUMLGeneratorMojo extends AbstractMojo {
	
	/** The Constant PREFIX. */
	static final String PREFIX = "plantuml-generator.";	
	
	/** The asciidoc diagram name. */
	@Parameter(property = PREFIX + "asciidocDiagramName", defaultValue = "", required = false)
	private String asciidocDiagramName;

	/** The asciidoc diagram image type. */
	@Parameter(property = PREFIX + "asciidocDiagramImageFormat", defaultValue = "png", required = false)
	private String asciidocDiagramImageFormat;

	/** The asciidoc diagram block delimiter. */
	@Parameter(property = PREFIX + "asciidocDiagramBlockDelimiter", defaultValue = "----", required = false)
	private String asciidocDiagramBlockDelimiter;
	
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
	
	/** The enable asciidoc wrapper. */
	@Parameter(property = PREFIX + "enableAsciidocWrapper", defaultValue = "false", required = false)
	private boolean enableAsciidocWrapper;

	/** The enable asciidoc wrapper. */
	@Parameter(property = PREFIX + "enableMarkdownWrapper", defaultValue = "false", required = false)
	private boolean enableMarkdownWrapper;
	
	/** Additional PlantUML configs. */
	@Parameter(property = PREFIX + "additionalPlantUmlConfigs", defaultValue = "", required = false)
	private List<String> additionalPlantUmlConfigs;
	

	/** The descriptor. */
	@Parameter(defaultValue = "${plugin}", readonly = true)
	private PluginDescriptor descriptor;

	/** The project. */
	@Parameter( defaultValue = "${project}", readonly = true )
	private MavenProject project;
	
	/**
	 * Wraps a plantuml diagram text by an asciidoc diagram block.
	 *
	 * @param paramClassDiagramTextToWrap the class diagram text
	 * @return the asciidoc plantuml diagram block
	 */
	protected String createAsciidocWrappedDiagramText(String paramClassDiagramTextToWrap) {
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
	 * Wraps a plantuml diagram text by an markdown diagram block.
	 *
	 * @param paramClassDiagramTextToWrap the class diagram text
	 * @return the markdown plantuml diagram block
	 */
	protected String createMarkdownWrappedDiagramText(String paramClassDiagramTextToWrap) {
		StringBuilder builder = new StringBuilder();
		builder.append("```plantuml");
		builder.append(System.lineSeparator());
		builder.append(paramClassDiagramTextToWrap);
		builder.append(System.lineSeparator());
		builder.append("```");
		builder.append(System.lineSeparator());
		return builder.toString();
	}
	
	/**
	 * Write diagram to file.
	 *
	 * @param classDiagramText the class diagram text
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void writeDiagramToFile(String classDiagramText) throws IOException {
		if (isEnableAsciidocWrapper()) {
			classDiagramText = createAsciidocWrappedDiagramText(classDiagramText);
		}
		if (isEnableMarkdowncWrapper()) {
			classDiagramText = createMarkdownWrappedDiagramText(classDiagramText);
		}
		getLog().debug("diagram text:");
		getLog().debug(classDiagramText);
		getLog().info("Diagram generated.");
		File outputFile = new File(getOutputDirectory().getAbsolutePath() + File.separator + getOutputFilename());
		if (!outputFile.exists()) {
			outputFile.getParentFile().mkdirs();
			boolean created = outputFile.createNewFile();
			if (!created) {
				getLog().error("Output file " + outputFile.getAbsolutePath() + " could not be created.");
				return;
			}
		}
		try(FileOutputStream outputStream =  new FileOutputStream(outputFile)){
			IOUtils.write(classDiagramText, outputStream, getEncoding());
		getLog().info("Diagram written to " + outputFile.getAbsolutePath());
		}
	}

	/**
	 * Gets the compile class loader.
	 *
	 * @return ClassLoader - the compile class loader
	 * @throws MojoExecutionException the mojo execution exception
	 */
	protected ClassLoader getCompileClassLoader() throws MojoExecutionException {
		try {
			List<String> runtimeClasspathElements = getProject().getRuntimeClasspathElements();
			List<String> compileClasspathElements = getProject().getCompileClasspathElements();
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
	 * Checks if is enable markdown wrapper.
	 *
	 * @return true, if is enable markdown wrapper
	 */
	public boolean isEnableMarkdowncWrapper() {
		return enableMarkdownWrapper;
	}

	/**
	 * Sets the enable markdown wrapper.
	 *
	 * @param enableMarkdownWrapper the new enable markdown wrapper
	 */
	public void setEnableMarkdownWrapper(boolean enableMarkdownWrapper) {
		this.enableMarkdownWrapper = enableMarkdownWrapper;
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

	/**
	 * Gets the additional plant uml configs.
	 *
	 * @return the additional plant uml configs
	 */
	public List<String> getAdditionalPlantUmlConfigs() {
		return additionalPlantUmlConfigs;
	}

	/**
	 * Sets the additional plant uml configs.
	 *
	 * @param additionalPlantUmlConfigs the new additional plant uml configs
	 */
	public void setAdditionalPlantUmlConfigs(List<String> additionalPlantUmlConfigs) {
		this.additionalPlantUmlConfigs = additionalPlantUmlConfigs;
	}

	
}
