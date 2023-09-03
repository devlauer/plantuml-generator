package de.elnarion.util.plantuml.generator.sequencediagram.internal;

import de.elnarion.util.plantuml.generator.sequencediagram.config.PlantUMLSequenceDiagramConfig;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public class RecursivelyCalledMethod implements ICallerMethod {

    private final ICallerMethod recursivelyCalledMethod;

    /**
     * Instantiates a new recursively called method object for an uml diagram.
     */
    public RecursivelyCalledMethod(ICallerMethod paramRecursivelyCalledMethod) {
        this.recursivelyCalledMethod = paramRecursivelyCalledMethod;
    }


    @Override
    public String getMethodName() {
        return recursivelyCalledMethod.getMethodName();
    }

    @Override
    public List<ICallerMethod> getCallees() {
        // always empty list to interrupt the call sequence
        return new ArrayList<>();
    }

    @Override
    public ICallerClass getCallerClass() {
        return recursivelyCalledMethod.getCallerClass();
    }

    @Override
    public List<String> getDiagramParticipants() {
        // always empty list because all diagram participants are already provided by the recursively called method
        return new ArrayList<>();
    }

    @Override
    public Object getDiagramText() throws ClassNotFoundException, NotFoundException {
        return "";
    }

    @Override
    public String generateCallSequenceDiagramText(String paramIndent) throws NotFoundException {
        return "";
    }

    @Override
    public String getReturnType() throws NotFoundException {
        return recursivelyCalledMethod.getReturnType();
    }
}
