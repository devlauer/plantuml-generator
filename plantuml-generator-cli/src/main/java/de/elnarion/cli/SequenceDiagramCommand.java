package de.elnarion.cli;

import de.elnarion.util.plantuml.generator.sequencediagram.PlantUMLSequenceDiagramGenerator;
import de.elnarion.util.plantuml.generator.sequencediagram.config.PlantUMLSequenceDiagramConfigBuilder;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;


@Command(name = "sequence", description = "Generate sequence diagrams.")
public class SequenceDiagramCommand extends AbstractCommand {
    @Option(names = "--class-blacklist-regexp", description = "Regex for blacklisting classes", defaultValue = "")
    private String classBlacklistRegexp;

    @Option(names = "--hide-method-name", description = "Hide method names in the diagram", defaultValue = "false")
    private boolean hideMethodName;

    @Option(names = "--hide-super-class", description = "Hide super classes in the diagram", defaultValue = "false")
    private boolean hideSuperClass;

    @Option(names = "--ignore-jpa-entities", description = "Ignore JPA entities in the diagram", defaultValue = "false")
    private boolean ignoreJPAEntities;

    @Option(names = "--ignore-standard-classes", description = "Ignore standard classes in the diagram", defaultValue = "true")
    private boolean ignoreStandardClasses;

    @Option(names = "--method-blacklist-regexp", description = "Regex for blacklisting methods", defaultValue = "")
    private String methodBlacklistRegexp;

    @Option(names = "--show-return-types", description = "Show return types in the diagram", defaultValue = "false")
    private boolean showReturnTypes;

    @Option(names = "--use-short-class-names", description = "Use short class names in the diagram", defaultValue = "true")
    private boolean useShortClassNames;

    @Option(names = "--start-class", description = "Start class for the sequence diagram", required = true)
    private String startClass;

    @Option(names = "--start-method", description = "Start method for the sequence diagram", required = true)
    private String startMethod;
    @Override
    public void run() {
        System.out.println("Starting plantuml sequence diagram generation");
        try {
            ClassLoader loader = getCompileClassLoader();
            PlantUMLSequenceDiagramGenerator sequenceDiagramGenerator;
            PlantUMLSequenceDiagramConfigBuilder configBuilder = new PlantUMLSequenceDiagramConfigBuilder(startClass, startMethod);

            if (classBlacklistRegexp != null && !classBlacklistRegexp.isEmpty()) {
                configBuilder.withClassBlacklistRegexp(classBlacklistRegexp);
            }
            if (methodBlacklistRegexp != null && !methodBlacklistRegexp.isEmpty()) {
                configBuilder.withMethodBlacklistRegexp(methodBlacklistRegexp);
            }

            configBuilder.withClassloader(loader)
                    .withHideMethodName(hideMethodName)
                    .withHideSuperClass(hideSuperClass)
                    .withIgnoreJPAEntities(ignoreJPAEntities)
                    .withIgnoreStandardClasses(ignoreStandardClasses)
                    .withShowReturnTypes(showReturnTypes)
                    .withUseShortClassName(useShortClassNames);

            sequenceDiagramGenerator = new PlantUMLSequenceDiagramGenerator(configBuilder.build());
            String sequenceDiagramText = sequenceDiagramGenerator.generateDiagramText();
            writeDiagramToFile(sequenceDiagramText);
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
