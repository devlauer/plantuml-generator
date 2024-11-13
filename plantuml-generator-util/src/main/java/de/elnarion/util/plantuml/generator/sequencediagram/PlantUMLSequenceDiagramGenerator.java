package de.elnarion.util.plantuml.generator.sequencediagram;

import de.elnarion.util.plantuml.generator.sequencediagram.config.PlantUMLSequenceDiagramConfig;
import de.elnarion.util.plantuml.generator.sequencediagram.exception.NotFoundException;
import de.elnarion.util.plantuml.generator.sequencediagram.internal.ICallerMethod;
import de.elnarion.util.plantuml.generator.sequencediagram.internal.SequenceAnalyzer;
import javassist.CannotCompileException;

/**
 * This class provides the ability to generate a sequence diagram from existing
 * java classes.
 */
public class PlantUMLSequenceDiagramGenerator {

    /**
     * The config.
     */
    final PlantUMLSequenceDiagramConfig config;

    /**
     * Instantiates a new plant UML sequence diagram generator.
     *
     * @param paramPlantUMLConfig the param plant UML config
     */
    public PlantUMLSequenceDiagramGenerator(final PlantUMLSequenceDiagramConfig paramPlantUMLConfig) {
        config = paramPlantUMLConfig;
    }

    /**
     * Generate diagram text.
     *
     * @return the string
     * @throws NotFoundException the not found exception
     */
    public String generateDiagramText() throws NotFoundException {
        SequenceAnalyzer sequenceAnalyzer = new SequenceAnalyzer(config);
        try {
            ICallerMethod callerMethod = sequenceAnalyzer.analyzeCallSequence();
            return generateDiagramTextFromCallerMethod(callerMethod);
        } catch (ClassNotFoundException | javassist.NotFoundException e) {
            throw new NotFoundException(e.getMessage(), e);
        } catch (CannotCompileException e) {
            // can not happen because nothing is changed and therefore no compilation is
            // needed.
            return "";
        }

    }

    private String generateDiagramTextFromCallerMethod(ICallerMethod callerMethod)
            throws ClassNotFoundException, javassist.NotFoundException {
        String plantumlContent = "@startuml" +
                System.lineSeparator() +
                callerMethod.getDiagramText() +
                "@enduml" +
                System.lineSeparator();
        // some inner class name contains '$' , which cause syntax error in plantuml
        // so replace it with '_'
        return plantumlContent.replaceAll("\\$", "_");
    }
}
