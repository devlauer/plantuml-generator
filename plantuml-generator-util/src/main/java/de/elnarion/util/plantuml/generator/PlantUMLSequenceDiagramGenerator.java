package de.elnarion.util.plantuml.generator;

import de.elnarion.util.plantuml.generator.config.PlantUMLSequenceDiagramConfig;
import de.elnarion.util.plantuml.generator.sequencediagram.CallerClass;
import de.elnarion.util.plantuml.generator.sequencediagram.CallerMethod;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

/**
 * This class provides the ability to generate a sequence diagram from existing
 * java classes.
 */
public class PlantUMLSequenceDiagramGenerator {

	PlantUMLSequenceDiagramConfig config;

	public PlantUMLSequenceDiagramGenerator(final PlantUMLSequenceDiagramConfig paramPlantUMLConfig) {
		config = paramPlantUMLConfig;
	}

	public String generateDiagramText() throws NotFoundException, CannotCompileException, ClassNotFoundException {
		ClassPool cp = getClassLoaderSpecificClassPool();
		CtMethod method = findStartingMethodInClassPool(cp);
		CallerMethod callerMethod = getCallerMethod(method,null);
		return generateDiagramTextFromCallerMethod(callerMethod);
	}

	private String generateDiagramTextFromCallerMethod(CallerMethod callerMethod) throws ClassNotFoundException, NotFoundException {
		StringBuilder diagramStringBuilder = new StringBuilder();
		diagramStringBuilder.append("@startuml");
		diagramStringBuilder.append(System.lineSeparator());
		diagramStringBuilder.append(callerMethod.getDiagramText());
		diagramStringBuilder.append("@enduml");
		diagramStringBuilder.append(System.lineSeparator());
		return diagramStringBuilder.toString();
	}

	private CtMethod findStartingMethodInClassPool(ClassPool cp) throws NotFoundException {
		CtClass ctClass = cp.get(config.getStartClass());
		return ctClass.getDeclaredMethod(config.getStartMethod());
	}

	private ClassPool getClassLoaderSpecificClassPool() {
		ClassPool cp = ClassPool.getDefault();
		LoaderClassPath loaderClassPath = new LoaderClassPath(getClassLoader());
		cp.insertClassPath(loaderClassPath);
		return cp;
	}

	private CallerMethod getCallerMethod(CtMethod method, CallerClass callingClass) throws CannotCompileException, NotFoundException {
		CallerClass callerClass = new CallerClass(method.getDeclaringClass(),config,callingClass);
		CallerMethod callerMethod = new CallerMethod(method,callerClass,config);
		method.instrument(new ExprEditor() {
			@Override
			public void edit(MethodCall m) throws CannotCompileException {
				try {
					CtMethod calleeMethod = m.getMethod();
					CtClass calleeClass = calleeMethod.getDeclaringClass();
					if(isIgnoreCall(calleeClass,calleeMethod))
						return;
					callerMethod.getCallees().add(getCallerMethod(calleeMethod,callerClass));
				} catch (NotFoundException e) {
					// ignore - should not happen
				}
			}
		});
		return callerMethod;
	}

	private boolean isIgnoreCall(CtClass calleeClass,CtMethod calleeMethod) {
		boolean ignoreCall = false;
		// handle all ignore cases
		if(config.isIgnoreStandardClasses()&&isJavaStandardClass(calleeClass))
			ignoreCall = true;
		if(config.isIgnoreJPAEntities()&&calleeClass.hasAnnotation("javax.persistence.Entity"))
			ignoreCall = true;
		if(config.getClassBlacklistRegexp()!=null&&isClassBlacklisted(calleeClass))
			ignoreCall = true;
		if(config.getMethodBlacklistRegexp()!=null&&isMethodBlacklisted(calleeMethod))
			ignoreCall = true;
		return ignoreCall;
	}	
	
	private boolean isMethodBlacklisted(CtMethod calleeMethod) {
		return calleeMethod.getName().matches(config.getMethodBlacklistRegexp());
	}

	private boolean isClassBlacklisted(CtClass calleeClass) {
		return calleeClass.getName().matches(config.getClassBlacklistRegexp());
	}

	private boolean isJavaStandardClass(CtClass calleeClass) {
		String packageName = calleeClass.getPackageName();
		return packageName.startsWith("java.");
	}
	

	private ClassLoader getClassLoader() {
		ClassLoader classLoader = this.getClass().getClassLoader();
		if (config.getDestinationClassloader() != null) {
			classLoader = config.getDestinationClassloader();
		}
		return classLoader;
	}

}
