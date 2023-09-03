package de.elnarion.util.plantuml.generator.sequencediagram.internal;

import de.elnarion.util.plantuml.generator.sequencediagram.config.PlantUMLSequenceDiagramConfig;
import javassist.CtClass;
import javassist.NotFoundException;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Class CallerClass.
 */
public class CallerClass implements ICallerClass {

    /**
     * The ct class.
     */
    private final CtClass ctClass;

    /**
     * The super classes.
     */
    private final List<CtClass> superClasses = new LinkedList<>();

    /**
     * The config.
     */
    private final PlantUMLSequenceDiagramConfig config;

    /**
     * The calling class.
     */
    private final CallerClass callingClass;

    /**
     * Instantiates a new caller class.
     *
     * @param paramClass        the param class
     * @param paramConfig       the param config
     * @param paramCallingClass the param calling class
     * @throws NotFoundException the not found exception
     */
    public CallerClass(CtClass paramClass, PlantUMLSequenceDiagramConfig paramConfig, CallerClass paramCallingClass) throws NotFoundException {
        ctClass = paramClass;
        config = paramConfig;
        callingClass = paramCallingClass;
        addSuperClassesToCallerClass(this, paramClass);
    }

    @Override
    public String getName() {
        return ctClass.getName();
    }

    @Override
    public List<String> getParentClassNames() {
        return getSuperClasses().stream().map(CtClass::getName).collect(Collectors.toList());
    }

    private void addSuperClassesToCallerClass(CallerClass callerClass, CtClass declaringClass) throws NotFoundException {
        CtClass superClass = declaringClass.getSuperclass();
        if (superClass != null) {
            callerClass.getSuperClasses().add(superClass);
            // get all super classes recursively
            addSuperClassesToCallerClass(callerClass, superClass);
        }
    }

    @Override
    public String getDiagramClassName() {
        if (callingClass != null && config.isHideSuperClass() && callingClass.isSubTypeOf(this)) {
            return callingClass.getDiagramClassName();
        }
        if (config.isUseShortClassNames())
            return ctClass.getSimpleName();
        return ctClass.getName();
    }

    private boolean isSubTypeOf(CallerClass paramCallerClass) {
        return getParentClassNames().contains(paramCallerClass.getName());
    }

    /**
     * Gets the super classes.
     *
     * @return the super classes
     */
    protected List<CtClass> getSuperClasses() {
        return superClasses;
    }

}
