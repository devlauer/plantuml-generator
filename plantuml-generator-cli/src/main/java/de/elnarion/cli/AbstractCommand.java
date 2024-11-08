package de.elnarion.cli;

import org.apache.commons.io.IOUtils;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand  implements Runnable{

    @Option(names = "--asciidoc-diagram-name", description = "Name of the Asciidoc diagram", defaultValue = "")
    String asciidocDiagramName;

    @Option(names = "--asciidoc-diagram-image-format", description = "Image format for Asciidoc diagram", defaultValue = "png")
    String asciidocDiagramImageFormat;

    @Option(names = "--asciidoc-diagram-block-delimiter", description = "Block delimiter for Asciidoc diagrams", defaultValue = "----")
    String asciidocDiagramBlockDelimiter;

    @Option(names = "--encoding", description = "Encoding of the output", defaultValue = "UTF-8")
    String encoding;

    @Option(names = "--output-directory", description = "Directory to output files", defaultValue = "./generated-docs")
    File outputDirectory;

    @Option(names = "--output-filename", description = "Name of the output file", required = true )
    String outputFilename;

    @Option(names = "--enable-asciidoc-wrapper", description = "Enable Asciidoc wrapper", defaultValue = "false")
    boolean enableAsciidocWrapper;

    @Option(names = "--enable-markdown-wrapper", description = "Enable Markdown wrapper", defaultValue = "false")
    boolean enableMarkdownWrapper;

    @Option(names = "--additional-configs", split = ",", description = "Additional PlantUML configurations, split by ',' ", defaultValue = "")
    List<String> additionalPlantUmlConfigs;

    // add option  to  pass classpath urls  for classloader , required, split by ','
    @Option(names = "--source-file-path", split = ",", description = "source file path to compile , split by ',' ", required = false)
    List<String> sourceFilePath;

    @Option(names = "--classpath-urls", split = ",", description = "Classpath URLs for classloader, split by , ", required = false)
    List<String> classpathURLs;

    protected void writeDiagramToFile(String classDiagramText) throws Exception{
        if (isEnableAsciidocWrapper()) {
            classDiagramText = createAsciidocWrappedDiagramText(classDiagramText);
        }
        if (isEnableMarkdownWrapper()) {
            classDiagramText = createMarkdownWrappedDiagramText(classDiagramText);
        }

        File outputFile = new File(getOutputDirectory().getAbsolutePath() + File.separator + getOutputFilename());
        if (!outputFile.exists()) {
            outputFile.getParentFile().mkdirs();
            boolean created = outputFile.createNewFile();
            if (!created) {
                System.out.println("Output file " + outputFile.getAbsolutePath() + " could not be created.");
                return;
            }
        }
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) { //NOSONAR - unencrypted file is wanted here
            IOUtils.write(classDiagramText, outputStream, getEncoding());
            System.out.println("Diagram written to " + outputFile.getAbsolutePath());
        }
    }
    protected ClassLoader getCompileClassLoader() throws Exception {
        try {
            List<URL> cpURLS = new ArrayList<>();

            if(sourceFilePath!= null && sourceFilePath.size() > 0){

                compileJava2Class(sourceFilePath);

                for (String sourcefilePath : sourceFilePath) {
                    File file = new File(sourcefilePath);
                    cpURLS.add(file.toURI().toURL());
                }
            }

            if(classpathURLs != null &&  classpathURLs.size() >= 0){
                for (String classPath : classpathURLs) {
                    if (classPath.endsWith(".jar")){
                        File file = new File(classPath);
                        cpURLS.add(file.toURI().toURL());
                    } else
                    {
                        File dir = new File(classPath);
                        if(!dir.isDirectory()){
                            // skip non directory
                            continue;
                        }else {
                            File[] files = dir.listFiles((d, name) -> name.endsWith(".jar"));
                            for (File file : files) {
                                cpURLS.add(file.toURI().toURL());
                            }
                        }
                    }
                }
            }

            URL[] urlArray = cpURLS.toArray(new URL[0]);

            return new URLClassLoader(urlArray, Thread.currentThread().getContextClassLoader());

        } catch (Exception e) {
            throw new RuntimeException("Unable to load classpath runtime !", e);
        }
    }

    private void compileJava2Class(List<String> filePaths){
        // Recursion get all java file in filePaths
        List<String> allJavaFiles = findAllJavaFiles(filePaths);

        DynamicCompileUtils.compile(allJavaFiles,classpathURLs);
    }
    private List<String> findAllJavaFiles(List<String> filePaths) {
        List<String> javaFiles = new ArrayList<>();
        for (String filePath : filePaths) {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    List<String> subFilePaths = new ArrayList<>();
                    for (File f : files) {
                        subFilePaths.add(f.getAbsolutePath());
                    }
                    javaFiles.addAll(findAllJavaFiles(subFilePaths));
                }
            } else if (filePath.endsWith(".java")) {
                javaFiles.add(filePath);
            }
        }
        return javaFiles;
    }

    protected String createMarkdownWrappedDiagramText(String paramClassDiagramTextToWrap) {
        return "```plantuml" +
                System.lineSeparator() +
                paramClassDiagramTextToWrap +
                System.lineSeparator() +
                "```" +
                System.lineSeparator();
    }

    protected String createAsciidocWrappedDiagramText(String paramClassDiagramTextToWrap) {
        StringBuilder builder = new StringBuilder();
        builder.append("[plantuml,");
        if (asciidocDiagramName != null && !"".equals(asciidocDiagramName))
            builder.append(asciidocDiagramName);
        else {
            builder.append(outputFilename);
            builder.append(".");
            builder.append(asciidocDiagramImageFormat);
        }
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

    public String getEncoding() {
        return encoding;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public String getOutputFilename() {
        return outputFilename;
    }

    public boolean isEnableAsciidocWrapper() {
        return enableAsciidocWrapper;
    }

    public boolean isEnableMarkdownWrapper() {
        return enableMarkdownWrapper;
    }

    public List<String> getAdditionalPlantUmlConfigs() {
        return additionalPlantUmlConfigs;
    }
}
