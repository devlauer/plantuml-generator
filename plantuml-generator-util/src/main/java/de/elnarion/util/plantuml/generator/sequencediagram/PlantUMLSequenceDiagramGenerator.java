package de.elnarion.util.plantuml.generator.sequencediagram;

import de.elnarion.util.plantuml.generator.sequencediagram.config.PlantUMLSequenceDiagramConfig;
import de.elnarion.util.plantuml.generator.sequencediagram.exception.NotFoundException;
import de.elnarion.util.plantuml.generator.sequencediagram.internal.CallerClass;
import de.elnarion.util.plantuml.generator.sequencediagram.internal.CallerMethod;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

/**
 * This class provides the ability to generate a sequence diagram from existing
 * java classes.
 */
public class PlantUMLSequenceDiagramGenerator {

	/** The config. */
	PlantUMLSequenceDiagramConfig config;

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
		ClassPool cp = getClassLoaderSpecificClassPool();
		try {
			CtMethod method = findStartingMethodInClassPool(cp);
			CallerMethod callerMethod = getCallerMethod(method, null);
			return generateDiagramTextFromCallerMethod(callerMethod);
		} catch (ClassNotFoundException | javassist.NotFoundException e) {
			throw new NotFoundException(e.getMessage(), e);
		} catch (CannotCompileException e) {
			// can not happen because nothing is changed and therefore no compilation is
			// needed.
			return "";
		}

	}

	private String generateDiagramTextFromCallerMethod(CallerMethod callerMethod)
			throws ClassNotFoundException, javassist.NotFoundException {
		return "@startuml" +
				System.lineSeparator() +
				callerMethod.getDiagramText() +
				"@enduml" +
				System.lineSeparator();
	}

	private CtMethod findStartingMethodInClassPool(ClassPool cp) throws javassist.NotFoundException {
		CtClass ctClass = cp.get(config.getStartClass());
		return ctClass.getDeclaredMethod(config.getStartMethod());
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
	 * @throws CannotCompileException the cannot compile exception
	 * @throws NotFoundException      the not found exception
	 */
	private CallerMethod getCallerMethod(CtMethod method, CallerClass callingClass)
			throws CannotCompileException, javassist.NotFoundException {
		CallerClass callerClass = new CallerClass(method.getDeclaringClass(), config, callingClass);
		CallerMethod callerMethod = new CallerMethod(method, callerClass, config);
		method.instrument(new ExprEditor() {
			@Override
			public void edit(MethodCall m) throws CannotCompileException {
				try {
					CtMethod calleeMethod = m.getMethod();
					CtClass calleeClass = calleeMethod.getDeclaringClass();
					if (isIgnoreCall(calleeClass, calleeMethod))
						return;
					callerMethod.getCallees().add(getCallerMethod(calleeMethod, callerClass));
				} catch (javassist.NotFoundException e) {
					// ignore - should not happen
				}
			}
		});
		return callerMethod;
	}

	/**
	 * Checks if is ignore call.
	 *
	 * @param calleeClass  the callee class
	 * @param calleeMethod the callee method
	 * @return true, if is ignore call
	 */
	private boolean isIgnoreCall(CtClass calleeClass, CtMethod calleeMethod) {
		boolean ignoreCall = config.isIgnoreStandardClasses() && isJavaStandardClass(calleeClass);
		// handle all ignore cases
		if (config.isIgnoreJPAEntities() && calleeClass.hasAnnotation("javax.persistence.Entity"))
			ignoreCall = true;
		if (config.getClassBlacklistRegexp() != null && isClassBlacklisted(calleeClass))
			ignoreCall = true;
		if (config.getMethodBlacklistRegexp() != null && isMethodBlacklisted(calleeMethod))
			ignoreCall = true;
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

}
