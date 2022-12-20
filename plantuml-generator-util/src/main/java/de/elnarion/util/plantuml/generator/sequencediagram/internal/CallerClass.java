package de.elnarion.util.plantuml.generator.sequencediagram.internal;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import de.elnarion.util.plantuml.generator.sequencediagram.config.PlantUMLSequenceDiagramConfig;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * The Class CallerClass.
 */
public class CallerClass {

	/** The ct class. */
	private CtClass ctClass;

	/** The super classes. */
	private List<CtClass> superClasses = new LinkedList<>();

	/** The config. */
	private PlantUMLSequenceDiagramConfig config;

	/** The calling class. */
	private CallerClass callingClass;

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

	/**
	 * Adds the super classes to caller class.
	 *
	 * @param callerClass the caller class
	 * @param declaringClass the declaring class
	 * @throws NotFoundException the not found exception
	 */
	private void addSuperClassesToCallerClass(CallerClass callerClass, CtClass declaringClass) throws NotFoundException {
			CtClass superClass = declaringClass.getSuperclass();
			if (superClass != null) {
				callerClass.getSuperClasses().add(superClass);
				// get all super classes recursively
				addSuperClassesToCallerClass(callerClass, superClass);
			}
	}

	/**
	 * Gets the diagram class name.
	 *
	 * @return the diagram class name
	 */
	public String getDiagramClassName() {
		if (callingClass!=null&&config.isHideSuperClass()&&callingClass.isSubTypeOf(this)) {
			return callingClass.getDiagramClassName();
		}
		if (config.isUseShortClassNames())
			return ctClass.getSimpleName();
		return ctClass.getName();
	}

	/**
	 * Checks if is sub type of.
	 *
	 * @param paramCallerClass the param caller class
	 * @return true, if is sub type of
	 */
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
