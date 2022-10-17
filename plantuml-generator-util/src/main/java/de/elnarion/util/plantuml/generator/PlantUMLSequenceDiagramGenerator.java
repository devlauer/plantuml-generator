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

	public String generateDiagramText() throws NotFoundException, CannotCompileException {
		ClassPool cp = getClassLoaderSpecificClassPool();
		CtMethod method = findStartingMethodInClassPool(cp);
		CallerMethod callerMethod = getCallerMethod(method,null);
		return generateDiagramTextFromCallerMethod(callerMethod);
	}

	private String generateDiagramTextFromCallerMethod(CallerMethod callerMethod) {
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

	private CallerMethod getCallerMethod(CtMethod method, CallerClass callingClass) throws CannotCompileException {
		CallerClass callerClass = new CallerClass(method.getDeclaringClass(),config,callingClass);
		CallerMethod callerMethod = new CallerMethod(method,callerClass,config);
		method.instrument(new ExprEditor() {
			@Override
			public void edit(MethodCall m) throws CannotCompileException {
				try {
					CtMethod calleeMethod = m.getMethod();
					callerMethod.getCallees().add(getCallerMethod(calleeMethod,callerClass));
				} catch (NotFoundException e) {
					e.printStackTrace();
				}
			}
		});
		return callerMethod;
	}


	private ClassLoader getClassLoader() {
		ClassLoader classLoader = this.getClass().getClassLoader();
		if (config.getDestinationClassloader() != null) {
			classLoader = config.getDestinationClassloader();
		}
		return classLoader;
	}

}
