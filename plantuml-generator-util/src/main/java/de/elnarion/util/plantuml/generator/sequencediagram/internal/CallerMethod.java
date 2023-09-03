package de.elnarion.util.plantuml.generator.sequencediagram.internal;

import de.elnarion.util.plantuml.generator.sequencediagram.config.PlantUMLSequenceDiagramConfig;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The Class CallerMethod.
 */
public class CallerMethod implements ICallerMethod {

    /**
     * The class name.
     */
    private final ICallerClass callerClass;

    /**
     * The ct method.
     */
    private final CtMethod ctMethod;

    /**
     * The config.
     */
    private final PlantUMLSequenceDiagramConfig config;

    /**
     * The callees.
     */
    private final List<ICallerMethod> callees = new LinkedList<>();

    /**
     * Instantiates a new caller method.
     *
     * @param paramMethod      the param method
     * @param paramCallerClass the param caller class
     * @param paramConfig      the param config
     */
    public CallerMethod(CtMethod paramMethod, CallerClass paramCallerClass, PlantUMLSequenceDiagramConfig paramConfig) {
        ctMethod = paramMethod;
        config = paramConfig;
        callerClass = paramCallerClass;
    }

    @Override
    public String getMethodName() {
        return ctMethod.getName();
    }


    @Override
    public List<ICallerMethod> getCallees() {
        return callees;
    }

    @Override
    public ICallerClass getCallerClass() {
        return callerClass;
    }

    @Override
    public List<String> getDiagramParticipants() {
        List<String> diagramParticipants = new LinkedList<>();
        diagramParticipants.add(callerClass.getDiagramClassName());
        diagramParticipants.addAll(callees.stream().map(ICallerMethod::getDiagramParticipants).flatMap(List::stream)
                .collect(Collectors.toList()));
        return diagramParticipants;
    }

    @Override
    public Object getDiagramText() throws ClassNotFoundException, NotFoundException {
        String participantsString = generateParticipantsText();
        StringBuilder callerMethodDiagramTextBuilder = new StringBuilder();
        callerMethodDiagramTextBuilder.append(System.lineSeparator());
        callerMethodDiagramTextBuilder.append(participantsString);
        callerMethodDiagramTextBuilder.append(System.lineSeparator());
        callerMethodDiagramTextBuilder.append(System.lineSeparator());
        callerMethodDiagramTextBuilder.append("activate ");
        callerMethodDiagramTextBuilder.append(this.getCallerClass().getDiagramClassName());
        callerMethodDiagramTextBuilder.append(System.lineSeparator());
        String callSequenceDiagramText = generateCallSequenceDiagramText("\t");
        callerMethodDiagramTextBuilder.append(callSequenceDiagramText);
        callerMethodDiagramTextBuilder.append("deactivate ");
        callerMethodDiagramTextBuilder.append(this.getCallerClass().getDiagramClassName());
        callerMethodDiagramTextBuilder.append(System.lineSeparator());
        callerMethodDiagramTextBuilder.append(System.lineSeparator());
        return callerMethodDiagramTextBuilder.toString();
    }

    @Override
    public String generateCallSequenceDiagramText(String paramIndent) throws NotFoundException {
        StringBuilder callSequenceBuilder = new StringBuilder();
        for (ICallerMethod calleeMethod : callees) {
            String callerClassName = this.getCallerClass().getDiagramClassName();
            String calleeClassName = calleeMethod.getCallerClass().getDiagramClassName();
            callSequenceBuilder.append(paramIndent);
            callSequenceBuilder.append(callerClassName);
            callSequenceBuilder.append(" -> ");
            callSequenceBuilder.append(calleeClassName);
            if (!config.isHideMethodName()) {
                callSequenceBuilder.append(" : ");
                callSequenceBuilder.append(calleeMethod.getMethodName());
            }
            callSequenceBuilder.append(System.lineSeparator());

            callSequenceBuilder.append(paramIndent);
            callSequenceBuilder.append("activate ");
            callSequenceBuilder.append(calleeClassName);
            callSequenceBuilder.append(System.lineSeparator());

            if(!(calleeMethod instanceof RecursivelyCalledMethod)) {
                callSequenceBuilder.append(calleeMethod.generateCallSequenceDiagramText(paramIndent + "\t"));
                callSequenceBuilder.append(paramIndent);
                callSequenceBuilder.append("\t");
                callSequenceBuilder.append(calleeClassName);
                callSequenceBuilder.append(" --> ");
                callSequenceBuilder.append(callerClassName);
                if (config.isShowReturnTypes()) {
                    callSequenceBuilder.append(" : ");
                    callSequenceBuilder.append(calleeMethod.getReturnType());
                }
                callSequenceBuilder.append(System.lineSeparator());
            }

            callSequenceBuilder.append(paramIndent);
            callSequenceBuilder.append("deactivate ");
            callSequenceBuilder.append(calleeClassName);
            callSequenceBuilder.append(System.lineSeparator());
        }
        return callSequenceBuilder.toString();
    }

    @Override
    public String getReturnType() throws NotFoundException {
        if (config.isUseShortClassNames())
            return ctMethod.getReturnType().getSimpleName();
        return ctMethod.getReturnType().getName();
    }

    private String generateParticipantsText() {
        List<String> participants = getDiagramParticipants();
        // preserve calling order
        Set<String> distinctParticipants = new LinkedHashSet<>(participants);
        List<String> diagramParticipants = distinctParticipants.stream().map(e -> "participant " + e)
                .collect(Collectors.toList());
        return String.join(System.lineSeparator(), diagramParticipants);
    }

}
