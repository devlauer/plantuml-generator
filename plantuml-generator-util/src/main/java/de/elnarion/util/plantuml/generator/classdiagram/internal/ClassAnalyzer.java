package de.elnarion.util.plantuml.generator.classdiagram.internal;

import de.elnarion.util.plantuml.generator.classdiagram.config.ClassifierType;
import de.elnarion.util.plantuml.generator.classdiagram.config.PlantUMLClassDiagramConfig;
import de.elnarion.util.plantuml.generator.classdiagram.config.VisibilityType;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * The Class PlantUMLClassDiagramAnalyzerAndMapper.
 */
public class ClassAnalyzer {

	/** The plant UML config. */
	private final PlantUMLClassDiagramConfig plantUMLConfig;
	/** The resolved classes. */
	private final List<Class<?>> resolvedClasses = new ArrayList<>();

	/** The classes. */
	private final Map<String, UMLClass> classes = new HashMap<>();

	/** The classes and relationships. */
	private final Map<UMLClass, List<UMLRelationship>> classesAndRelationships = new HashMap<>();

	/**
	 * Instantiates a new plant UML class diagram analyzer and mapper.
	 *
	 * @param paramPlantUMLConfig the param plant UML config
	 */
	public ClassAnalyzer(PlantUMLClassDiagramConfig paramPlantUMLConfig) {
		plantUMLConfig = paramPlantUMLConfig;
	}

	/**
	 * Analyze classes and map them to the internal class structure.
	 *
	 * @return the plant UML class diagram analyze summary
	 */
	public ClassAnalyzerSummary analyzeClassesAndMapThemToTheInternalClassStructure() {
		// read all classes from directories or jars
		resolvedClasses
				.addAll(new ClassResolver(plantUMLConfig.getDestinationClassloader(), plantUMLConfig.getScanPackages(),
						plantUMLConfig.getBlacklistRegexp(), plantUMLConfig.getWhitelistRegexp())
						.getAllDiagramClasses());
		// sort all classes for a reliable sorted result
		resolvedClasses.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
		// map java classes to UMLClass, UMLField, UMLMethod and UMLRelationship objects
		for (final Class<?> clazz : resolvedClasses) {
			mapToDomainClasses(clazz);
		}
		return new ClassAnalyzerSummary(classes, classesAndRelationships);
	}

	/**
	 * Maps the java class to a {@link UMLClass}.
	 *
	 * @param paramClassObject - Class&lt;?&gt; - the java class object to be
	 *                         processed
	 */
	private void mapToDomainClasses(final Class<?> paramClassObject) {
		if (!includeClass(paramClassObject)) {
			return;
		}
		// do not process synthetic classes
		if (paramClassObject.isSynthetic()) {
			return;
		}

		final int modifiers = paramClassObject.getModifiers();

		ClassType classType = ClassType.CLASS;
		if (paramClassObject.isAnnotation()) {
			classType = ClassType.ANNOTATION;
		} else if (paramClassObject.isEnum()) {
			classType = ClassType.ENUM;
		} else if (paramClassObject.isInterface()) {
			classType = ClassType.INTERFACE;
		} else if (Modifier.isAbstract(modifiers)) {
			classType = ClassType.ABSTRACT_CLASS;
		}
		List<UMLStereotype> stereotypes = new ArrayList<>();
		if (plantUMLConfig.isAddJPAAnnotations()) {
			new JPAAnalyzerHelper().addJPAStereotype(paramClassObject, stereotypes,
					plantUMLConfig.getDestinationClassloader());
		}

		final UMLClass umlClass = new UMLClass(classType, new ArrayList<>(), new ArrayList<>(),
				AnalyzerUtil.getClassNameForClassesOrRelationships(paramClassObject, plantUMLConfig), stereotypes);
		final List<UMLRelationship> relationships = new ArrayList<>();
		classesAndRelationships.put(umlClass, relationships);
		classes.put(paramClassObject.getName(), umlClass);

		if (classType == ClassType.ENUM) {
			addEnumConstants(paramClassObject, umlClass);
		} else {
			addFields(paramClassObject.getDeclaredFields(), paramClassObject.getDeclaredMethods(), umlClass);
			addMethods(paramClassObject.getDeclaredMethods(), paramClassObject.getDeclaredFields(), umlClass);
		}
		addSuperClassRelationship(paramClassObject, umlClass);
		addInterfaceRelationship(paramClassObject, umlClass);
		addAnnotationRelationship(paramClassObject, umlClass);
	}

	/**
	 * Reads all enum constants from the java enum class object and adds them as
	 * {@link UMLField} objects to the given {@link UMLClass} object. Only constants
	 * are included, values are ignored.
	 *
	 * @param paramClassObject - Class&lt;?&gt; - the class object of the enum to be
	 *                         processed
	 * @param paramUmlClass    - {@link UMLClass} - the UML class object where the
	 *                         field information should be added
	 */
	private void addEnumConstants(final Class<?> paramClassObject, final UMLClass paramUmlClass) {
		final Object[] enumConstants = paramClassObject.getEnumConstants();
		for (final Object enumConstant : enumConstants) {
			UMLField field = new FieldAnalyzer(plantUMLConfig).analyzeEnumConstant(enumConstant);
			if (field != null)
				paramUmlClass.addField(field);
		}
	}

	/**
	 * Adds an association relationship for each Annotation of a class which is also
	 * part of the diagram (needs to be an element of one of the configured
	 * packages).
	 *
	 * @param paramClassObject - Class&lt;?&gt; - the class object which should be
	 *                         checked for annotation relationships
	 * @param umlClass         - {@link UMLClass} - the UML class from which the
	 *                         relationship starts from
	 */
	private void addAnnotationRelationship(final Class<?> paramClassObject, final UMLClass umlClass) {
		final Annotation[] annotations = paramClassObject.getAnnotations();
		for (final Annotation annotation : annotations) {
			if (includeClass(annotation.annotationType())) {
				final UMLRelationship relationship = new UMLRelationship(null, null, null,
						AnalyzerUtil.getClassNameForClassesOrRelationships(paramClassObject, plantUMLConfig),
						AnalyzerUtil.getClassNameForClassesOrRelationships(annotation.annotationType(),
								plantUMLConfig),
						RelationshipType.ASSOCIATION, new ArrayList<>());
				addRelationship(umlClass, relationship);
			}
		}
	}

	/**
	 * Little helper method which links an {@link UMLRelationship} object to an
	 * {@link UMLClass} in the internal map.
	 *
	 * @param paramUmlClass        - {@link UMLClass} - the class which should be
	 *                             linked to the {@link UMLRelationship}
	 * @param paramUmlRelationship - {@link UMLRelationship} - the relationship
	 *                             which should be linked to the {@link UMLClass}
	 */
	private void addRelationship(final UMLClass paramUmlClass, final UMLRelationship paramUmlRelationship) {
		List<UMLRelationship> relationshipList = classesAndRelationships.computeIfAbsent(paramUmlClass,
				k -> new ArrayList<>());
		relationshipList.add(paramUmlRelationship);
	}

	/**
	 * Adds a realization relationship for each interface the given java class
	 * object implements and which is also part of the class diagram.
	 *
	 * @param paramClassObject - Class&lt;?&gt; - the class object which should be
	 *                         checked for interface relationships
	 * @param paramUmlClass    - {@link UMLClass} - the UML class from which the
	 *                         relationship starts from
	 */
	private void addInterfaceRelationship(final Class<?> paramClassObject, final UMLClass paramUmlClass) {
		final Class<?>[] interfaces = paramClassObject.getInterfaces();
		for (final Class<?> interfaceElement : interfaces) {
			if (includeClass(interfaceElement)) {
				final UMLRelationship relationship = new UMLRelationship(null, null, null,
						AnalyzerUtil.getClassNameForClassesOrRelationships(paramClassObject, plantUMLConfig),
						AnalyzerUtil.getClassNameForClassesOrRelationships(interfaceElement, plantUMLConfig),
						RelationshipType.REALIZATION, new ArrayList<>());
				addRelationship(paramUmlClass, relationship);
			}
		}
	}

	/**
	 * Adds a inheritance relationship for the super class of the given java class
	 * object and which needs also to be part of the class diagram.
	 *
	 * @param paramClassObject - Class&lt;?&gt; - the class object which should be
	 *                         checked for super class relationship
	 * @param paramUmlClass    - {@link UMLClass} - the UML class from which the
	 *                         relationship starts from
	 */
	private void addSuperClassRelationship(final Class<?> paramClassObject, final UMLClass paramUmlClass) {
		final Class<?> superClass = paramClassObject.getSuperclass();
		if (superClass != null && includeClass(superClass)) {
			final UMLRelationship relationship = new UMLRelationship(null, null, null,
					AnalyzerUtil.getClassNameForClassesOrRelationships(paramClassObject, plantUMLConfig),
					AnalyzerUtil.getClassNameForClassesOrRelationships(superClass, plantUMLConfig),
					RelationshipType.INHERITANCE, new ArrayList<>());
			addRelationship(paramUmlClass, relationship);
		}
	}

	/**
	 * Iterates over an array of declared methods of a java class, creates
	 * {@link UMLMethod} objects and adds them to the given {@link UMLClass} object.
	 *
	 * If a declared method is a getter or setter method of a declared field it is
	 * ignored.
	 *
	 * @param paramDeclaredMethods - Method[] - the array of declared methods in the
	 *                             java class corresponding to the given
	 *                             {@link UMLClass} object
	 * @param paramDeclaredFields  - Field[] - the array of declared fields in the
	 *                             java class corresponding to the given
	 *                             {@link UMLClass} object
	 * @param paramUmlClass        - {@link UMLClass} - the uml class where the
	 *                             {@link UMLMethod} objects should be added
	 */
	private void addMethods(final Method[] paramDeclaredMethods, final Field[] paramDeclaredFields, // NOSONAR
							final UMLClass paramUmlClass) {
		if (paramDeclaredMethods != null) {
			for (final Method method : paramDeclaredMethods) { // NOSONAR
				if (method.isSynthetic())
					continue;
				final String methodName = method.getName();
				// ignore normal getters and setters
				if ((methodName.startsWith("get") || methodName.startsWith("set") || methodName.startsWith("is"))
						&& paramDeclaredFields != null
						&& MethodAnalyzerUtil.isGetterOrSetterMethod(method, paramDeclaredFields)) {
					continue;
				}
				// Do not add method if they should be ignored/removed
				if (plantUMLConfig.isRemoveMethods())
					continue;
				// if there is a blacklist for method and the method name matches it, then
				// ignore/remove the field
				if (plantUMLConfig.getMethodBlacklistRegexp() != null
						&& methodName.matches(plantUMLConfig.getMethodBlacklistRegexp()))
					continue;
				String returnType = AnalyzerUtil.getClassNameForFieldsAndMethods(method.getReturnType(),
						plantUMLConfig);
				final Class<?>[] parameterTypes = method.getParameterTypes();
				final Map<String, String> parameters = convertToParameterStringMap(parameterTypes);
				final int modifier = method.getModifiers();
				final VisibilityType visibilityType = AnalyzerUtil.getVisibility(modifier);
				// check if method should be visible by maximum visibility
				if (!AnalyzerUtil.visibilityOk(plantUMLConfig.getMaxVisibilityMethods(), visibilityType))
					continue;
				final ClassifierType classifierType = AnalyzerUtil.getClassifier(modifier);
				if (plantUMLConfig.getMethodClassifierToIgnore().contains(classifierType))
					continue;
				final List<String> stereotypes = new ArrayList<>();
				if (method.isAnnotationPresent(Deprecated.class)) {
					stereotypes.add("deprecated");
				}
				if (Modifier.isSynchronized(modifier)) {
					stereotypes.add("synchronized");
				}
				final UMLMethod umlMethod = new UMLMethod(classifierType, visibilityType, returnType, methodName,
						parameters, stereotypes);
				paramUmlClass.addMethod(umlMethod);
			}
		}
	}

	/**
	 * Converts an array of parameter types to a string map with synthetic parameter
	 * names and their mapped full qualified type name.
	 *
	 * Parameter names are synthetic because in Java 7 the parameter names cannot be
	 * determined in all cases.
	 *
	 * @param paramParameterTypes - Class&lt;?&gt;[] - the array of all parameter
	 *                            types which should be processed
	 * @return Map&lt;String,String&gt; - the result
	 */
	private Map<String, String> convertToParameterStringMap(final Class<?>[] paramParameterTypes) {
		final Map<String, String> parameters = new LinkedHashMap<>();
		if (paramParameterTypes != null) {
			int counter = 1;
			for (final Class<?> parameter : paramParameterTypes) {
				String parameterName = parameter.getName();
				if (parameterName.lastIndexOf('.') > 0) {
					parameterName = parameterName.substring(parameterName.lastIndexOf('.') + 1);
				}
				parameterName = "param" + parameterName + counter;
				String parameterType = AnalyzerUtil.getClassNameForFieldsAndMethods(parameter, plantUMLConfig);
				parameters.put(parameterName, parameterType);
				counter++;
			}
		}
		return parameters;
	}

	/**
	 * Creates {@link UMLField} or {@link UMLRelationship} objects for all given
	 * fields. And adds them to the given {@link UMLClass} object.
	 *
	 * If a field type is part of the class diagram (directly or via List or Set) it
	 * is added as {@link UMLRelationship}. If not it is added as {@link UMLField}
	 * object.
	 *
	 * If a field has a getter an a setter method its visibility is upgraded to
	 * public.
	 *
	 * @param paramDeclaredFields  Field[] - the Field objects which are the base
	 *                             for the {@link UMLField} or
	 *                             {@link UMLRelationship} objects.
	 * @param paramDeclaredMethods Method[] - the method objects of the java class
	 *                             corresponding to the given {@link UMLClass}
	 *                             object
	 * @param paramUmlClass        {@link UMLClass} - the uml class objects to which
	 *                             the {@link UMLField} or {@link UMLRelationship}
	 *                             objects should be added
	 */
	private void addFields(final Field[] paramDeclaredFields, final Method[] paramDeclaredMethods,
						   final UMLClass paramUmlClass) {
		if (paramDeclaredFields != null) {
			for (final java.lang.reflect.Field field : paramDeclaredFields) {
				if (field.isSynthetic())
					continue;
				final Class<?> type = field.getType();
				final boolean relationshipAdded = addAggregationRelationship(paramUmlClass, field,
						paramDeclaredMethods);
				if (includeClass(type)) {
					List<String> annotations = new JPAAnalyzerHelper().addJPAFieldAnnotationsToList(field,
							paramDeclaredMethods, plantUMLConfig.getDestinationClassloader());
					final UMLRelationship relationship = createUMLRelationship4Field(field, type, annotations);
					addRelationship(paramUmlClass, relationship);
				} else if (!relationshipAdded) {
					addFieldToUMLClass(paramUmlClass, field, type, paramDeclaredMethods);
				}
			}
		}

	}

	/**
	 * Creates the UML relationship 4 field.
	 *
	 * @param field       the field
	 * @param type        the type
	 * @param annotations the annotations
	 * @return the UML relationship
	 */
	private UMLRelationship createUMLRelationship4Field(final java.lang.reflect.Field field, final Class<?> type,
														List<String> annotations) {
		final UMLRelationship relationship;
		if (Modifier.isFinal(field.getModifiers())) {
			relationship = new UMLRelationship(null, null, field.getName(),
					AnalyzerUtil.getClassNameForClassesOrRelationships(field.getDeclaringClass(), plantUMLConfig),
					AnalyzerUtil.getClassNameForClassesOrRelationships(type, plantUMLConfig),
					RelationshipType.COMPOSITION, annotations);
		} else {
			relationship = new UMLRelationship(null, null, field.getName(),
					AnalyzerUtil.getClassNameForClassesOrRelationships(field.getDeclaringClass(), plantUMLConfig),
					AnalyzerUtil.getClassNameForClassesOrRelationships(type, plantUMLConfig),
					RelationshipType.DIRECTED_ASSOCIATION, annotations);
		}
		return relationship;
	}

	/**
	 * Creates {@link UMLField} object for the given field if the field should be
	 * added to the diagram (blacklist-check,remove-check,visibility-check).
	 *
	 * @param paramUmlClass        {@link UMLClass} - the uml class to which the
	 *                             {@link UMLField} should be added
	 * @param field                Field - the field objects which are basse for the
	 *                             {@link UMLField}
	 * @param type                 Class - the type of the field
	 * @param paramDeclaredMethods Method[] - the method objects of the java class
	 *                             corresponding to the given {@link UMLClass}
	 *                             object
	 */
	private void addFieldToUMLClass(final UMLClass paramUmlClass, final java.lang.reflect.Field field,
									final Class<?> type, Method[] paramDeclaredMethods) {
		FieldAnalyzer fieldAnalyzer = new FieldAnalyzer(plantUMLConfig);
		UMLField umlField = fieldAnalyzer.analyzeField(field, type, paramDeclaredMethods);
		if (umlField != null)
			paramUmlClass.addField(umlField);
	}

	/**
	 * Adds an aggregation relationship to a given {@link UMLClass} object if the
	 * field is a set or list, which has a generic type argument, which references a
	 * class in the diagram.
	 *
	 * @param paramUmlClass        - {@link UMLClass} - the from side of the
	 *                             relationship
	 * @param paramField           - Field - the java reflection field which should
	 *                             be processed
	 * @param paramDeclaredMethods - Method[] - the methods of the class which
	 *                             contains the aggregation field
	 * @return true, if an aggregation relationship was created and added
	 */
	private boolean addAggregationRelationship(final UMLClass paramUmlClass, final Field paramField,
											   final Method[] paramDeclaredMethods) {
		final Type type = paramField.getType();
		final Type genericType = paramField.getGenericType();
		boolean isRelationshipAggregation = false;
		if (Collection.class.isAssignableFrom((Class<?>) type) && genericType instanceof ParameterizedType
				&& (((ParameterizedType) genericType).getRawType().equals(Set.class)
				|| ((ParameterizedType) genericType).getRawType().equals(List.class))) {
			final Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
			if (actualTypeArguments != null) {
				for (final Type typeArgument : actualTypeArguments) {
					final Class<?> typeArgumentClass = getClassForType(typeArgument);
					if (((typeArgumentClass != null) && includeClass(typeArgumentClass))) {
						List<String> annotations = new JPAAnalyzerHelper().addJPAFieldAnnotationsToList(paramField,
								paramDeclaredMethods, plantUMLConfig.getDestinationClassloader());
						final UMLRelationship relationship = new UMLRelationship("1", "0..*", paramField.getName(),
								AnalyzerUtil.getClassNameForClassesOrRelationships(paramField.getDeclaringClass(),
										plantUMLConfig),
								AnalyzerUtil.getClassNameForClassesOrRelationships(typeArgumentClass, plantUMLConfig),
								RelationshipType.AGGREGATION, annotations);
						addRelationship(paramUmlClass, relationship);
						isRelationshipAggregation = true;
					}
				}
			}
		}
		return isRelationshipAggregation;
	}

	/**
	 * Gets the class for type.
	 *
	 * @param paramType the type argument
	 * @return Class - the class for type
	 */
	private Class<?> getClassForType(final Type paramType) {
		Class<?> typeArgumentClass = null;
		if (paramType instanceof ParameterizedType) {
			if (((ParameterizedType) paramType).getRawType() instanceof Class<?>) {
				typeArgumentClass = (Class<?>) ((ParameterizedType) paramType).getRawType();
			}
		} else if (paramType instanceof Class<?>) {
			typeArgumentClass = (Class<?>) paramType;
		}
		return typeArgumentClass;
	}

	/**
	 * Checks if a class is a member of one of the scanned packages.
	 *
	 * @param paramClass - Class&lt;?&gt; - the class to be checked
	 * @return true, if class is a member of the scanned packages
	 */
	private boolean includeClass(final Class<?> paramClass) {
		return resolvedClasses.contains(paramClass);
	}

}
