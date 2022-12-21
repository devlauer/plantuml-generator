package de.elnarion.util.plantuml.generator.classdiagram.internal;

import static java.util.Collections.sort;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.elnarion.util.plantuml.generator.classdiagram.config.ClassifierType;
import de.elnarion.util.plantuml.generator.classdiagram.config.PlantUMLClassDiagramConfig;
import de.elnarion.util.plantuml.generator.classdiagram.config.VisibilityType;

/**
 * The Class PlantUMLClassDiagramAnalyzerAndMapper.
 */
public class ClassDiagramAnalyzerAndMapper {

	/** The plant UML config. */
	private PlantUMLClassDiagramConfig plantUMLConfig;
	/** The resolved classes. */
	private List<Class<?>> resolvedClasses = new ArrayList<>();

	/** The classes. */
	private Map<String, UMLClass> classes = new HashMap<>();

	/** The classes and relationships. */
	private Map<UMLClass, List<UMLRelationship>> classesAndRelationships = new HashMap<>();

	/**
	 * Instantiates a new plant UML class diagram analyzer and mapper.
	 *
	 * @param paramPlantUMLConfig the param plant UML config
	 */
	public ClassDiagramAnalyzerAndMapper(PlantUMLClassDiagramConfig paramPlantUMLConfig) {
		plantUMLConfig = paramPlantUMLConfig;
	}

	/**
	 * Analyze classes and map them to the internal class structure.
	 *
	 * @return the plant UML class diagram analyze summary
	 */
	public ClassDiagramAnalyzeSummary analyzeClassesAndMapThemToTheInternalClassStructure() {
		// read all classes from directories or jars
		resolvedClasses
				.addAll(new ClassResolver(plantUMLConfig.getDestinationClassloader(), plantUMLConfig.getScanPackages(),
						plantUMLConfig.getBlacklistRegexp(), plantUMLConfig.getWhitelistRegexp())
						.getAllDiagramClasses());
		// sort all classes for a reliable sorted result
		Collections.sort(resolvedClasses, (o1, o2) -> o1.getName().compareTo(o2.getName()));
		// map java classes to UMLClass, UMLField, UMLMethod and UMLRelationship objects
		for (final Class<?> clazz : resolvedClasses) {
			mapToDomainClasses(clazz);
		}
		return new ClassDiagramAnalyzeSummary(classes, classesAndRelationships);
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
		VisibilityType visibilityType = VisibilityType.PUBLIC;
		if (Modifier.isPrivate(modifiers)) {
			visibilityType = VisibilityType.PRIVATE;
		} else if (Modifier.isProtected(modifiers)) {
			visibilityType = VisibilityType.PROTECTED;
		}
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
			addJPAStereotype(paramClassObject, stereotypes);
		}

		final UMLClass umlClass = new UMLClass(visibilityType, classType, new ArrayList<>(), new ArrayList<>(),
				paramClassObject.getName(), stereotypes);
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
	 * Adds the JPA stereotype.
	 *
	 * @param paramClassObject the param class object
	 * @param stereotypes      the stereotypes
	 */
	private void addJPAStereotype(final Class<?> paramClassObject, List<UMLStereotype> stereotypes) {
		ClassLoader destinationClassloader = plantUMLConfig.getDestinationClassloader();
		if (destinationClassloader != null) {
			try {
				addStereoTypesForAnnotationClass(paramClassObject, stereotypes, destinationClassloader,
						"javax.persistence.Entity", "Entity");
				addStereoTypesForAnnotationClass(paramClassObject, stereotypes, destinationClassloader,
						"javax.persistence.Table", "Table");
				addStereoTypesForAnnotationClass(paramClassObject, stereotypes, destinationClassloader,
						"javax.persistence.MappedSuperclass", "MappedSuperclass");
			} catch (ClassNotFoundException | SecurityException | IllegalArgumentException e) {
				// ignore all exceptions
			}
		}
	}

	/**
	 * Adds the stereo types for annotation class.
	 *
	 * @param paramClassObject       the param class object
	 * @param stereotypes            the stereotypes
	 * @param destinationClassloader the destination classloader
	 * @param annotationClassName    the annotation class name
	 * @param annotationName         the annotation name
	 * @throws ClassNotFoundException the class not found exception
	 */
	private void addStereoTypesForAnnotationClass(final Class<?> paramClassObject, List<UMLStereotype> stereotypes,
			ClassLoader destinationClassloader, String annotationClassName, String annotationName)
			throws ClassNotFoundException {
		Class<?> tableAnnotation = destinationClassloader.loadClass(annotationClassName);
		Annotation[] annotations = paramClassObject.getAnnotations();
		for (Annotation annotation : annotations) {
			if (tableAnnotation.isAssignableFrom(annotation.getClass())) {
				addAnnotationStereotype(stereotypes, annotation, annotationName, destinationClassloader);
			}
		}
	}

	/**
	 * Adds the annotation stereotype.
	 *
	 * @param stereotypes            the stereotypes
	 * @param annotation             the annotation
	 * @param annotationName         the annotation name
	 * @param destinationClassloader the destination classloader
	 */
	private void addAnnotationStereotype(List<UMLStereotype> stereotypes, Annotation annotation, String annotationName,
			ClassLoader destinationClassloader) {
		Map<String, List<String>> attributes = new TreeMap<>();
		// only for @Table
		if (annotationName != null && "Table".equals(annotationName)) {
			addAttributeIfExists(annotation, attributes, "name");
			addAttributeIfExists(annotation, attributes, "schema");
			addAttributeObjectListIfExists(annotation, attributes, "javax.persistence.Index", "indexes",
					destinationClassloader);
			addAttributeObjectListIfExists(annotation, attributes, "javax.persistence.UniqueConstraint",
					"uniqueConstraints", destinationClassloader);
		}
		UMLStereotype stereotype = new UMLStereotype(annotationName, attributes);
		stereotypes.add(stereotype);
	}

	/**
	 * Adds the attribute object list if exists.
	 *
	 * @param annotation             the annotation
	 * @param attributes             the attributes
	 * @param annotationClassName    the annotation class name
	 * @param methodName             the method name
	 * @param destinationClassloader the destination classloader
	 */
	private void addAttributeObjectListIfExists(Annotation annotation, Map<String, List<String>> attributes,
			String annotationClassName, String methodName, ClassLoader destinationClassloader) {
		try {
			Method nameMethod = annotation.getClass().getMethod(methodName);
			if (nameMethod != null) {
				Class<?> valueAnnotation = destinationClassloader.loadClass(annotationClassName);
				Object nameObject = nameMethod.invoke(annotation);
				if (nameObject != null && nameObject.getClass().isArray()
						&& nameObject.getClass().getComponentType().equals(valueAnnotation)) {
					List<String> values = addAnnotationArrayToAttributeList(valueAnnotation, nameObject);
					if (!values.isEmpty()) {
						sort(values);
						attributes.put(methodName, values);
					}
				}
			}
		} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| ClassNotFoundException e) {
			// ignore, because method is not found or can not be called or has illegal
			// arguments
		}
	}

	/**
	 * Adds the annotation array to attribute list.
	 *
	 * @param valueAnnotation the value annotation
	 * @param nameObject      the name object
	 * @return the list
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	private List<String> addAnnotationArrayToAttributeList(Class<?> valueAnnotation, Object nameObject)
			throws IllegalAccessException, InvocationTargetException {
		List<String> values = new ArrayList<>();
		int length = Array.getLength(nameObject);
		for (int i = 0; i < length; i++) {
			Object arrayElement = Array.get(nameObject, i);
			StringBuilder elementStringBuilder = new StringBuilder();
			Method[] methods = valueAnnotation.getDeclaredMethods();
			if (methods.length > 0) {
				addMethodArrayValueString(valueAnnotation, arrayElement, elementStringBuilder, methods);
			}
			values.add(elementStringBuilder.toString());
		}
		return values;
	}

	/**
	 * Adds the method array value string.
	 *
	 * @param valueAnnotation      the value annotation
	 * @param arrayElement         the array element
	 * @param elementStringBuilder the element string builder
	 * @param methods              the methods
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	private void addMethodArrayValueString(Class<?> valueAnnotation, Object arrayElement,
			StringBuilder elementStringBuilder, Method[] methods)
			throws IllegalAccessException, InvocationTargetException {
		elementStringBuilder.append(valueAnnotation.getSimpleName());
		elementStringBuilder.append(" (");
		List<String> methodAttributesString = new ArrayList<>();
		for (Method method : methods) {
			String name = method.getName();
			if (method.getParameterCount() == 0) {
				String methodValueString = createMethodValueString(arrayElement, method, name);
				if (methodValueString != null && !methodValueString.isEmpty()) {
					methodAttributesString.add(methodValueString);
				}
			}
		}
		sort(methodAttributesString);
		elementStringBuilder.append(String.join(",", methodAttributesString));
		elementStringBuilder.append(" )");
	}

	/**
	 * Creates the method value string.
	 *
	 * @param arrayElement the array element
	 * @param method       the method
	 * @param name         the name
	 * @return the string
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	private String createMethodValueString(Object arrayElement, Method method, String name)
			throws IllegalAccessException, InvocationTargetException {
		Object result = method.invoke(arrayElement);
		StringBuilder elementStringBuilder = new StringBuilder();
		if ((result instanceof String && !((String) result).isEmpty())
				|| (!(result instanceof String) && result != null)) {
			elementStringBuilder.append(name);
			elementStringBuilder.append("=[");
			if (result.getClass().isArray()) {
				handleMethodArrayResultString(elementStringBuilder, result);
			} else {
				elementStringBuilder.append(result);
			}
			elementStringBuilder.append("]");

		}
		return elementStringBuilder.toString();
	}

	/**
	 * Handle method array result string.
	 *
	 * @param elementStringBuilder the element string builder
	 * @param result               the result
	 */
	private void handleMethodArrayResultString(StringBuilder elementStringBuilder, Object result) {
		int resultLength = Array.getLength(result);
		for (int j = 0; j < resultLength; j++) {
			if (j > 0)
				elementStringBuilder.append(",");
			elementStringBuilder.append(Array.get(result, j));
		}
	}

	/**
	 * Adds the attribute if exists.
	 *
	 * @param annotation the annotation
	 * @param attributes the attributes
	 * @param methodname the methodname
	 */
	private void addAttributeIfExists(Annotation annotation, Map<String, List<String>> attributes, String methodname) {
		try {
			Method nameMethod = annotation.getClass().getMethod(methodname);
			if (nameMethod != null) {
				Object nameObject = nameMethod.invoke(annotation);
				if (nameObject instanceof String && !((String) nameObject).isEmpty()) {
					List<String> values = new ArrayList<>();
					values.add((String) nameObject);
					attributes.put(methodname, values);
				}
			}
		} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// ignore, because method is not found or can not be called or has illegal
			// arguments
		}
	}

	/**
	 * Gets the column annotation string.
	 *
	 * @param annotation     the annotation
	 * @param annotationName the annotation name
	 * @return the column annotation string
	 */
	private String getColumnAnnotationString(Annotation annotation, String annotationName) {
		StringBuilder builder = new StringBuilder();
		builder.append("@");
		builder.append(annotationName);
		try {
			Method nameMethod = annotation.getClass().getMethod("name");
			if (nameMethod != null) {
				builder.append("(\"");
				Object nameObject = nameMethod.invoke(annotation);
				if (nameObject instanceof String) {
					builder.append(nameObject);
				}
				builder.append("\")");
			}
		} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// ignore, method not found or not callable
		}
		return builder.toString();
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
			final UMLField field = new UMLField(ClassifierType.NONE, VisibilityType.PUBLIC, enumConstant.toString(),
					null, new ArrayList<>());
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
		if (annotations != null) {
			for (final Annotation annotation : annotations) {
				if (includeClass(annotation.annotationType())) {
					final UMLRelationship relationship = new UMLRelationship(null, null, null,
							paramClassObject.getName(), annotation.annotationType().getName(),
							RelationshipType.ASSOCIATION, new ArrayList<>());
					addRelationship(umlClass, relationship);
				}
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
		if (relationshipList == null) {
			relationshipList = new ArrayList<>();
			classesAndRelationships.put(paramUmlClass, relationshipList);
		}
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
		if (interfaces != null) {
			for (final Class<?> interfaceElement : interfaces) {
				if (includeClass(interfaceElement)) {
					final UMLRelationship relationship = new UMLRelationship(null, null, null,
							paramClassObject.getName(), interfaceElement.getName(), RelationshipType.REALIZATION,
							new ArrayList<>());
					addRelationship(paramUmlClass, relationship);
				}
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
			final UMLRelationship relationship = new UMLRelationship(null, null, null, paramClassObject.getName(),
					superClass.getName(), RelationshipType.INHERITANCE, new ArrayList<>());
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
						&& paramDeclaredFields != null && isGetterOrSetterMethod(method, paramDeclaredFields)) {
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
				String returnType = method.getReturnType().getName();
				returnType = removeJavaLangPackage(returnType);
				final Class<?>[] parameterTypes = method.getParameterTypes();
				final Map<String, String> parameters = convertToParameterStringMap(parameterTypes);
				final int modifier = method.getModifiers();
				final VisibilityType visibilityType = getVisibility(modifier);
				// check if method should be visible by maximum visibility
				if (!visibilityOk(plantUMLConfig.getMaxVisibilityMethods(), visibilityType))
					continue;
				final ClassifierType classifierType = getClassifier(modifier);
				if (plantUMLConfig.getMethodClassifierToIgnore().contains(classifierType))
					continue;
				final List<String> stereotypes = new ArrayList<>();
				if (method.isAnnotationPresent(Deprecated.class)) {
					stereotypes.add("deprecated");
				}
				if (Modifier.isSynchronized(modifier)) {
					stereotypes.add("synchronized");
				}
				final de.elnarion.util.plantuml.generator.classdiagram.internal.UMLMethod umlMethod = new de.elnarion.util.plantuml.generator.classdiagram.internal.UMLMethod(
						classifierType, visibilityType, returnType, methodName, parameters, stereotypes);
				paramUmlClass.addMethod(umlMethod);
			}
		}
	}

	/**
	 * Removes the java lang package string from a full qualified class name. This
	 * makes it easier to read the class diagram.
	 *
	 * @param paramTypeName - String - the full qualified type name
	 * @return String - the type name without the java.lang package name
	 */
	private String removeJavaLangPackage(String paramTypeName) {
		if (paramTypeName.startsWith("java.lang.")) {
			paramTypeName = paramTypeName.substring("java.lang.".length(), paramTypeName.length());
		}
		return paramTypeName;
	}

	/**
	 * Gets the classifier type (none, abstract_static, static, abstract) from the
	 * modifier int.
	 *
	 * @param paramModifier - int - the modifier used for determining the classifier
	 *                      type
	 * @return {@link ClassifierType} - the classifier type
	 */
	private ClassifierType getClassifier(final int paramModifier) {
		ClassifierType classifierType = ClassifierType.NONE;
		if (Modifier.isStatic(paramModifier)) {
			if (Modifier.isAbstract(paramModifier)) {
				classifierType = ClassifierType.ABSTRACT_STATIC;
			} else {
				classifierType = ClassifierType.STATIC;
			}
		} else if (Modifier.isAbstract(paramModifier)) {
			classifierType = ClassifierType.ABSTRACT;
		}
		return classifierType;
	}

	/**
	 * Gets the visibility type (package_private, public, private, protected) from
	 * the modifier int.
	 *
	 * @param paramModifier - int - the modifier used for determining the visibility
	 *                      type
	 * @return {@link VisibilityType} - the visibility type
	 */
	private VisibilityType getVisibility(final int paramModifier) {
		VisibilityType visibilityType = VisibilityType.PACKAGE_PRIVATE;
		if (Modifier.isPublic(paramModifier)) {
			visibilityType = VisibilityType.PUBLIC;
		} else if (Modifier.isPrivate(paramModifier)) {
			visibilityType = VisibilityType.PRIVATE;
		} else if (Modifier.isProtected(paramModifier)) {
			visibilityType = VisibilityType.PROTECTED;
		}
		return visibilityType;
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
					parameterName = parameterName.substring(parameterName.lastIndexOf('.') + 1, parameterName.length());
				}
				parameterName = "param" + parameterName + counter;
				parameters.put(parameterName, removeJavaLangPackage(parameter.getName()));
				counter++;
			}
		}
		return parameters;
	}

	/**
	 * Checks if a method is a getter or a setter method of a declared field.
	 *
	 * @param paramMethod         - Method - the method to be checked
	 * @param paramDeclaredFields - Field[] - the declared fields used for the check
	 * @return true, if is getter or setter method
	 */
	private boolean isGetterOrSetterMethod(final Method paramMethod, final Field[] paramDeclaredFields) {
		String methodName = paramMethod.getName();
		if (methodName.startsWith("get")) {
			methodName = methodName.substring(3, methodName.length());
		} else if (methodName.startsWith("is")) {
			methodName = methodName.substring(2, methodName.length());
		} else if (methodName.startsWith("set")) {
			methodName = methodName.substring(3, methodName.length());
		}
		for (final Field field : paramDeclaredFields) {
			final String fieldName = field.getName();
			if (fieldName.equalsIgnoreCase(methodName)) {
				return true;
			}
		}
		return false;
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
				if (relationshipAdded) {
					// do nothing - skip processing
				} else if (includeClass(type)) {
					List<String> annotations = new ArrayList<>();
					addJPAFieldAnnotationsToList(field, paramDeclaredMethods, annotations);
					final UMLRelationship relationship = createUMLRelationship4Field(field, type, annotations);
					addRelationship(paramUmlClass, relationship);
				} else {
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
			relationship = new UMLRelationship(null, null, field.getName(), field.getDeclaringClass().getName(),
					type.getName(), RelationshipType.COMPOSITION, annotations);
		} else {
			relationship = new UMLRelationship(null, null, field.getName(), field.getDeclaringClass().getName(),
					type.getName(), RelationshipType.DIRECTED_ASSOCIATION, annotations);
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
		// Do not add field if they should be ignored/removed
		if (plantUMLConfig.isRemoveFields())
			return;
		// if there is a blacklist for field and the field name matches it, then
		// ignore/remove the field
		if (plantUMLConfig.getFieldBlacklistRegexp() != null
				&& field.getName().matches(plantUMLConfig.getFieldBlacklistRegexp()))
			return;
		final int modifier = field.getModifiers();
		List<String> annotationStringList = new ArrayList<>();
		if (plantUMLConfig.isAddJPAAnnotations()) {
			addJPAFieldAnnotationsToList(field, paramDeclaredMethods, annotationStringList);
		}
		final ClassifierType classifierType = getClassifier(modifier);
		if (plantUMLConfig.getFieldClassifierToIgnore().contains(classifierType))
			return;
		VisibilityType visibilityType = getVisibility(modifier);
		if (hasGetterAndSetterMethod(field.getName(), paramDeclaredMethods)) {
			visibilityType = VisibilityType.PUBLIC;
		}
		// check if field should be visible by maximum visibility
		if (!visibilityOk(plantUMLConfig.getMaxVisibilityFields(), visibilityType))
			return;
		final UMLField umlField = new UMLField(classifierType, visibilityType, field.getName(),
				removeJavaLangPackage(type.getName()), annotationStringList);
		paramUmlClass.addField(umlField);
	}

	/**
	 * Adds the JPA field annotations to list.
	 *
	 * @param field                the field
	 * @param paramDeclaredMethods the param declared methods
	 * @param annotationStringList the annotation string list
	 */
	private void addJPAFieldAnnotationsToList(final java.lang.reflect.Field field, Method[] paramDeclaredMethods,
			List<String> annotationStringList) {
		ClassLoader destinationClassloader = plantUMLConfig.getDestinationClassloader();
		if (destinationClassloader != null) {
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"javax.persistence.Column");
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"javax.persistence.Id");
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"javax.persistence.Transient");
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"javax.persistence.OneToOne");
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"javax.persistence.OneToMany");
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"javax.persistence.ManyToMany");
			addJPAFieldAnnotationClassToList(field, paramDeclaredMethods, annotationStringList, destinationClassloader,
					"javax.persistence.ManyToOne");
		}
	}

	/**
	 * Adds the JPA field annotation class to list.
	 *
	 * @param field                    the field
	 * @param paramDeclaredMethods     the param declared methods
	 * @param annotationStringList     the annotation string list
	 * @param destinationClassloader   the destination classloader
	 * @param paramAnnotationClassname the param annotation classname
	 */
	private void addJPAFieldAnnotationClassToList(final java.lang.reflect.Field field, Method[] paramDeclaredMethods, // NOSONAR
			List<String> annotationStringList, ClassLoader destinationClassloader, String paramAnnotationClassname) {
		try {
			boolean foundAnnotation = false;
			Class<?> columnAnnotation = destinationClassloader.loadClass(paramAnnotationClassname);
			Annotation[] annotations = field.getAnnotations();
			for (Annotation annotation : annotations) {
				if (columnAnnotation.isAssignableFrom(annotation.getClass())) {
					annotationStringList.add(getColumnAnnotationString(annotation, columnAnnotation.getSimpleName()));
					foundAnnotation = true;
				}
			}
			if (!foundAnnotation) {
				for (Method declaredMethod : paramDeclaredMethods) {
					if (isGetterOrSetterMethod(declaredMethod, new Field[] { field })) {
						annotations = declaredMethod.getAnnotations();
						for (Annotation annotation : annotations) {
							if (columnAnnotation.isAssignableFrom(annotation.getClass())) {
								annotationStringList
										.add(getColumnAnnotationString(annotation, columnAnnotation.getSimpleName()));
							}
						}
					}
				}
			}
		} catch (ClassNotFoundException | SecurityException | IllegalArgumentException e) {
			// ignore all exceptions
		}
	}

	/**
	 * Checks if the given visibilityType of a field or method should lead to an
	 * appreance on the diagram according to the configured maximum visibility.
	 * 
	 * @param maxVisibilityFields {@link VisibilityType} - the configured maximum
	 *                            visibility to check against
	 * @param visibilityType      {@link VisibilityType} - the visibility of a field
	 *                            or method which could be part of the diagram
	 * @return boolean - if the field or method should be visible on the diagram
	 */
	private boolean visibilityOk(VisibilityType maxVisibilityFields, VisibilityType visibilityType) {
		if (maxVisibilityFields != null) {
			// if maximum is public only public is allowed as visibility type
			if (maxVisibilityFields.equals(VisibilityType.PUBLIC) && !visibilityType.equals(VisibilityType.PUBLIC))
				return false;
			// if maximum is protected only public and protected are allowed
			if (maxVisibilityFields.equals(VisibilityType.PROTECTED) && !(visibilityType.equals(VisibilityType.PUBLIC)
					|| visibilityType.equals(VisibilityType.PROTECTED)))
				return false;
			// if maximum is package_private then only public, package_private and protected
			// are
			// allowed
			if (maxVisibilityFields.equals(VisibilityType.PACKAGE_PRIVATE)
					&& !(visibilityType.equals(VisibilityType.PUBLIC)
							|| visibilityType.equals(VisibilityType.PACKAGE_PRIVATE)
							|| visibilityType.equals(VisibilityType.PROTECTED)))
				return false;
		}
		// everything else like maximum private or no defined maximum visibility leads
		// to
		// an ok check
		return true;
	}

	/**
	 * Checks if a field has getter and setter methods.
	 *
	 * @param paramFieldName       - String - the field name
	 * @param paramDeclaredMethods - Method[] - the methods which should be used for
	 *                             the check
	 * @return true, if successful
	 */
	private boolean hasGetterAndSetterMethod(final String paramFieldName, final Method[] paramDeclaredMethods) {
		final String getterMethodName = "get" + paramFieldName;
		final String setterMethodName = "set" + paramFieldName;
		final String isMethodName = "is" + paramFieldName;
		boolean hasGetterMethod = false;
		boolean hasSetterMethod = false;
		if (paramDeclaredMethods != null) {
			for (final Method method : paramDeclaredMethods) {
				final String methodName = method.getName();
				if (methodName.equalsIgnoreCase(getterMethodName) || methodName.equalsIgnoreCase(isMethodName)) {
					hasGetterMethod = true;
				} else if (methodName.equalsIgnoreCase(setterMethodName)) {
					hasSetterMethod = true;
				}
				if (hasSetterMethod && hasGetterMethod) {
					return true;
				}
			}
		}
		return false;
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
						List<String> annotations = new ArrayList<>();
						addJPAFieldAnnotationsToList(paramField, paramDeclaredMethods, annotations);
						final UMLRelationship relationship = new UMLRelationship("1", "0..*", paramField.getName(),
								paramField.getDeclaringClass().getName(), (typeArgumentClass).getName(),
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
		} else if (paramType instanceof TypeVariable<?>) {
			// Nothing to do ignore type variables
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
