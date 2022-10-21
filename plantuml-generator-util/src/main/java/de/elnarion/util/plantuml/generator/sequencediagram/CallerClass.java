package de.elnarion.util.plantuml.generator.sequencediagram;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import de.elnarion.util.plantuml.generator.config.PlantUMLSequenceDiagramConfig;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * The Class CallerClass.
 */
public class CallerClass {

	private CtClass ctClass;

	private List<CtClass> superClasses = new LinkedList<>();

	private PlantUMLSequenceDiagramConfig config;

	private CallerClass callingClass;

	/**
	 * Instantiates a new caller class.
	 *
	 * @param paramClass        the param class
	 * @param paramConfig       the param config
	 * @param paramCallingClass the param calling class
	 * @throws NotFoundException 
	 */
	public CallerClass(CtClass paramClass, PlantUMLSequenceDiagramConfig paramConfig, CallerClass paramCallingClass) throws NotFoundException {
		ctClass = paramClass;
		config = paramConfig;
		callingClass = paramCallingClass;
		addSuperClassesToCallerClass(this, paramClass);
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return ctClass.getName();
	}

	/**
	 * Gets the parent classes.
	 *
	 * @return the parent classes
	 */
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

	public String getDiagramClassName() {
		if (callingClass!=null&&config.isHideSuperClass()&&callingClass.isSubTypeOf(this)) {
			return callingClass.getDiagramClassName();
		}
		if (config.isUseShortClassNames())
			return ctClass.getSimpleName();
		return ctClass.getName();
	}

	private boolean isSubTypeOf(CallerClass paramCallerClass) {
		return getParentClassNames().contains(paramCallerClass.getName());
	}

	protected List<CtClass> getSuperClasses() {
		return superClasses;
	}
	
}
