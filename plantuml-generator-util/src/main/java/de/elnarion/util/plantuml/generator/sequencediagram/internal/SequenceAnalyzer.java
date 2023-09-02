package de.elnarion.util.plantuml.generator.sequencediagram.internal;


import de.elnarion.util.plantuml.generator.sequencediagram.config.PlantUMLSequenceDiagramConfig;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

/**
 * Analyzes the call sequence of methods.
 */
public class SequenceAnalyzer {
    private final PlantUMLSequenceDiagramConfig config;

    public SequenceAnalyzer(PlantUMLSequenceDiagramConfig paramConfig) {
        this.config = paramConfig;
    }

    /**
     * Gets the class loader.
     *
     * @return the class loader
     */
    private ClassLoader getClassLoader() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        if (config.getDestinationClassloader() != null) {
            classLoader = config.getDestinationClassloader();
        }
        return classLoader;
    }

    /**
     * Gets the class loader specific class pool.
     *
     * @return the class loader specific class pool
     */
    private ClassPool getClassLoaderSpecificClassPool() {
        ClassPool cp = ClassPool.getDefault();
        LoaderClassPath loaderClassPath = new LoaderClassPath(getClassLoader());
        cp.insertClassPath(loaderClassPath);
        return cp;
    }

    /**
     * Gets the caller method.
     *
     * @param method       the method
     * @param callingClass the calling class
     * @return the caller method
     * @throws CannotCompileException if the class cannot be compiled
     */
    private CallerMethod getCallerMethod(CtMethod method, CallerClass callingClass) throws CannotCompileException, javassist.NotFoundException {
        CallerClass callerClass = new CallerClass(method.getDeclaringClass(), config, callingClass);
        CallerMethod callerMethod = new CallerMethod(method, callerClass, config);
        method.instrument(new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                try {
                    CtMethod calleeMethod = m.getMethod();
                    CtClass calleeClass = calleeMethod.getDeclaringClass();
                    if (isIgnoreCall(calleeClass, calleeMethod)) return;
                    callerMethod.getCallees().add(getCallerMethod(calleeMethod, callerClass));
                } catch (javassist.NotFoundException e) {
                    // ignore - should not happen
                }
            }
        });
        return callerMethod;
    }

    private CtMethod findStartingMethodInClassPool(ClassPool cp) throws javassist.NotFoundException {
        CtClass ctClass = cp.get(config.getStartClass());
        return ctClass.getDeclaredMethod(config.getStartMethod());
    }


    /**
     * Checks if to ignore the method call.
     *
     * @param calleeClass  the callee class
     * @param calleeMethod the callee method
     * @return true, if is to ignore the method call
     */
    private boolean isIgnoreCall(CtClass calleeClass, CtMethod calleeMethod) {
        boolean ignoreCall = config.isIgnoreStandardClasses() && isJavaStandardClass(calleeClass);
        // handle all ignore cases
        if (config.isIgnoreJPAEntities() && calleeClass.hasAnnotation("javax.persistence.Entity")) ignoreCall = true;
        if (config.getClassBlacklistRegexp() != null && isClassBlacklisted(calleeClass)) ignoreCall = true;
        if (config.getMethodBlacklistRegexp() != null && isMethodBlacklisted(calleeMethod)) ignoreCall = true;
        return ignoreCall;
    }

    /**
     * Checks if is method blacklisted.
     *
     * @param calleeMethod the callee method
     * @return true, if is method blacklisted
     */
    private boolean isMethodBlacklisted(CtMethod calleeMethod) {
        return calleeMethod.getName().matches(config.getMethodBlacklistRegexp());
    }


    /**
     * Checks if is class blacklisted.
     *
     * @param calleeClass the callee class
     * @return true, if is class blacklisted
     */
    private boolean isClassBlacklisted(CtClass calleeClass) {
        return calleeClass.getName().matches(config.getClassBlacklistRegexp());
    }

    /**
     * Checks if is java standard class.
     *
     * @param calleeClass the callee class
     * @return true, if is java standard class
     */
    private boolean isJavaStandardClass(CtClass calleeClass) {
        String packageName = calleeClass.getPackageName();
        return packageName.startsWith("java.");
    }

    /**
     * Analyze the call sequence for the configured starting method.
     *
     * @return CallerMethod an object representing an abstract form of a method being called in a sequence, capable of generating uml sequence diagram code
     */
    public CallerMethod analyzeCallSequence() throws NotFoundException, CannotCompileException {
        ClassPool cp = getClassLoaderSpecificClassPool();
        CtMethod method = findStartingMethodInClassPool(cp);
        return getCallerMethod(method, null);
    }
}