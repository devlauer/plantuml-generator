package de.elnarion.cli;

import de.elnarion.util.plantuml.generator.classdiagram.PlantUMLClassDiagramGenerator;
import de.elnarion.util.plantuml.generator.classdiagram.config.ClassifierType;
import de.elnarion.util.plantuml.generator.classdiagram.config.PlantUMLClassDiagramConfigBuilder;
import de.elnarion.util.plantuml.generator.classdiagram.config.VisibilityType;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.List;

@Command(name = "class", description = "Generate class diagrams.")
public class ClassDiagramCommand extends AbstractCommand {
    @Option(names = "--use-smetana", description = "Use Smetana renderer", defaultValue = "false")
    private boolean useSmetana;

    @Option(names = "--field-blacklist-regexp", description = "Regex for blacklisting fields", defaultValue = "")
    private String fieldBlacklistRegexp;

    @Option(names = "--method-blacklist-regexp", description = "Regex for blacklisting methods", defaultValue = "")
    private String methodBlacklistRegexp;

    @Option(names = "--max-visibility-fields", description = "Maximum visibility of fields", defaultValue = "PRIVATE")
    private VisibilityType maxVisibilityFields;

    @Option(names = "--max-visibility-methods", description = "Maximum visibility of methods", defaultValue = "PRIVATE")
    private VisibilityType maxVisibilityMethods;

    @Option(names = "--hide-fields", description = "Hide fields", defaultValue = "false")
    private boolean hideFields;

    @Option(names = "--hide-methods", description = "Hide methods", defaultValue = "false")
    private boolean hideMethods;

    @Option(names = "--scan-packages", split = ",", description = "Packages to scan", required = true)
    private List<String> scanPackages;

    @Option(names = "--whitelist-regexp", description = "Whitelist regular expression for the scan packages", defaultValue = "")
    private String whitelistRegexp;

    @Option(names = "--blacklist-regexp", description = "Blacklist regular expression for the scan packages", defaultValue = "")
    private String blacklistRegexp;

    @Option(names = "--hide-classes", split = ",", description = "List of classes to hide")
    private List<String> hideClasses;

    @Option(names = "--field-classifier-list-to-ignore", split = ",", description = "List of field classifiers to ignore")
    private List<ClassifierType> fieldClassifierListToIgnore;

    @Option(names = "--method-classifier-list-to-ignore", split = ",", description = "List of method classifiers to ignore")
    private List<ClassifierType> methodClassifierListToIgnore;

    @Option(names = "--remove-methods", description = "Remove methods", defaultValue = "false")
    private boolean removeMethods;

    @Option(names = "--add-jpa-annotations", description = "Add JPA annotations", defaultValue = "false")
    private boolean addJPAAnnotations;

    @Option(names = "--add-javax-validation-annotations", description = "Add Javax Validation annotations", defaultValue = "false")
    private boolean addJavaxValidationAnnotations;

    @Option(names = "--remove-fields", description = "Remove fields", defaultValue = "false")
    private boolean removeFields;

    @Option(names = "--use-short-class-names", description = "Use short class names", defaultValue = "false")
    private boolean useShortClassNames;

    @Option(names = "--use-short-class-names-in-fields-and-methods", description = "Use short class names in fields and methods", defaultValue = "false")
    private boolean useShortClassNamesInFieldsAndMethods;
    @Override
    public void run() {
        try {
            System.out.println("Starting plantuml generation");

            ClassLoader loader = getCompileClassLoader();
            PlantUMLClassDiagramConfigBuilder configBuilder;

            if (whitelistRegexp == null || "".equals(whitelistRegexp)) {
                configBuilder = new PlantUMLClassDiagramConfigBuilder(
                        (blacklistRegexp != null && !"".equals(blacklistRegexp)) ? blacklistRegexp : null,
                        scanPackages);
            } else {
                configBuilder = new PlantUMLClassDiagramConfigBuilder(scanPackages, whitelistRegexp);
            }

            configBuilder.withClassLoader(loader)
                    .withHideClasses(hideClasses)
                    .withHideFieldsParameter(hideFields)
                    .withUseSmetana(useSmetana)
                    .withHideMethods(hideMethods)
                    .addFieldClassifiersToIgnore(fieldClassifierListToIgnore)
                    .addMethodClassifiersToIgnore(methodClassifierListToIgnore)
                    .withRemoveFields(removeFields)
                    .withRemoveMethods(removeMethods)
                    .withFieldBlacklistRegexp(fieldBlacklistRegexp)
                    .withMethodBlacklistRegexp(methodBlacklistRegexp)
                    .withMaximumFieldVisibility(maxVisibilityFields)
                    .withMaximumMethodVisibility(maxVisibilityMethods)
                    .withJPAAnnotations(addJPAAnnotations)
                    .withJavaxValidationAnnotations(addJavaxValidationAnnotations)
                    .addAdditionalPlantUmlConfigs(getAdditionalPlantUmlConfigs())
                    .withUseShortClassNames(useShortClassNames)
                    .withUseShortClassNamesInFieldsAndMethods(useShortClassNamesInFieldsAndMethods);

            PlantUMLClassDiagramGenerator classDiagramGenerator = new PlantUMLClassDiagramGenerator(configBuilder.build());
            String classDiagramText = classDiagramGenerator.generateDiagramText();
            writeDiagramToFile(classDiagramText);
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
